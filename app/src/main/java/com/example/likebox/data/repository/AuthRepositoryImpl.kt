package com.example.likebox.data.repository

import android.app.Activity
import com.example.likebox.di.ApplicationScope
import com.example.likebox.domain.model.auth.User
import com.example.likebox.domain.repository.AuthRepository
import com.example.likebox.presentation.state.auth.AuthState
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    @ApplicationScope private val scope: CoroutineScope // 주입받는 방식
) : AuthRepository {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    override val authState = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser = _currentUser.asStateFlow()

    private var verificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null


    init {
        auth.addAuthStateListener { firebaseAuth ->
            scope.launch {
                when {
                    firebaseAuth.currentUser != null -> {
                        try {
                            val hasConnectedPlatform = checkPlatformConnection(firebaseAuth.currentUser!!.uid)
                            if (_authState.value is AuthState.Error) {
                                // 에러 상태일 경우 유지
                                return@launch
                            }
                            _authState.value = if (hasConnectedPlatform) {
                                AuthState.Authenticated
                            } else {
                                AuthState.NeedsPlatformSetup
                            }
                            updateCurrentUser(firebaseAuth.currentUser!!.uid)
                        } catch (e: Exception) {
                            _authState.value = AuthState.Error(e.message ?: "Authentication check failed")
                            _currentUser.value = null
                        }
                    }
                    else -> {
                        // 에러 상태가 아닐 때만 NotAuthenticated로 변경
                        if (_authState.value !is AuthState.Error) {
                            _authState.value = AuthState.NotAuthenticated
                        }
                        resetPhoneAuth()
                        _currentUser.value = null
                    }
                }
            }
        }
    }

    private suspend fun updateCurrentUser(uid: String) {
        try {
            val userDoc = firestore.collection("users")
                .document(uid)
                .get()
                .await()
            _currentUser.value = userDoc.toObject<User>()
        } catch (e: Exception) {
            // 에러 처리
        }
    }

    override suspend fun signIn(email: String, password: String): Result<Unit> =
        try {
            _authState.value = AuthState.Loading
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: FirebaseAuthException) {
            val message = when (e) {
                is FirebaseAuthInvalidUserException ->
                    "This email is not registered. Please sign up first."
                is FirebaseAuthInvalidCredentialsException ->
                    "Incorrect password. Please try again."
                is FirebaseAuthWeakPasswordException ->
                    "Password should be at least 6 characters"
                else -> e.message ?: "Sign in failed"
            }
            _authState.value = AuthState.Error(message)
            Result.failure(Exception(message))
        }

    override suspend fun signUp(
        email: String,
        password: String,
        nickname: String
    ): Result<Unit> = try {
        // 1. Firebase Auth에 사용자 생성
        val authResult = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = authResult.user?.uid ?: throw Exception("Failed to create user")

        // 2. 이메일 인증 메일 발송
        auth.currentUser?.sendEmailVerification()?.await()

        // 3. Firestore에 사용자 정보 저장
        val user = User(
            userId = uid,
            email = email,
            nickName = nickname,
            phoneNumber = null,
            profilePictureUrl = null,
            connectedPlatforms = emptyList()
        )

        firestore.collection("users")
            .document(uid)
            .set(user)
            .await()

        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // 이메일 인증 상태 확인 메서드 추가
    override suspend fun checkEmailVerification(): Result<Boolean> = try {
        auth.currentUser?.reload()?.await()
        val isVerified = auth.currentUser?.isEmailVerified ?: false
        Result.success(isVerified)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun verifyPhoneCode(verificationCode: String): Result<Unit> {
        return try {
            val currentVerificationId = verificationId
            if (currentVerificationId == null) {
                resetPhoneAuth()
                return Result.failure(Exception("Verification ID not found"))
            }
            val credential = PhoneAuthProvider.getCredential(currentVerificationId, verificationCode)

            // Firebase Auth로 로그인
            val authResult = auth.signInWithCredential(credential).await()

            // 새로운 사용자인 경우 Firestore에 문서 생성
            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
            if (isNewUser) {
                val user = User(
                    userId = authResult.user?.uid ?: throw Exception("User ID not found"),
                    phoneNumber = authResult.user?.phoneNumber?.toLongOrNull() ?: 0,
                    email = "",
                    nickName = "",
                    profilePictureUrl = null,
                    connectedPlatforms = emptyList()
                )

                firestore.collection("users")
                    .document(user.userId)
                    .set(user)
                    .await()
            }

            resetPhoneAuth() // 성공시에도 초기화
            Result.success(Unit)
        } catch (e: Exception) {
            resetPhoneAuth() // 실패시 초기화
            when (e) {
                is FirebaseAuthInvalidCredentialsException -> {
                    Result.failure(Exception("Invalid verification code"))
                }
                else -> Result.failure(e)
            }
        }
    }

    // resendVerificationCode도 실패시 초기화하도록 수정
    override suspend fun resendVerificationCode(
        phoneNumber: String,
        activity: Activity
    ): Result<Unit> {
        return try {
            val currentResendToken = resendToken
            if (currentResendToken == null) {
                resetPhoneAuth()
                return Result.failure(Exception("Resend token not found"))
            }

            suspendCoroutine { continuation ->
                val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        scope.launch {
                            try {
                                auth.signInWithCredential(credential).await()
                                resetPhoneAuth()
                                continuation.resume(Result.success(Unit))
                            } catch (e: Exception) {
                                resetPhoneAuth()
                                continuation.resume(Result.failure(e))
                            }
                        }
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        resetPhoneAuth()
                        when (e) {
                            is FirebaseAuthInvalidCredentialsException -> {
                                continuation.resume(Result.failure(Exception("Invalid phone number")))
                            }
                            is FirebaseTooManyRequestsException -> {
                                continuation.resume(Result.failure(Exception("Too many requests")))
                            }
                            else -> {
                                continuation.resume(Result.failure(e))
                            }
                        }
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        this@AuthRepositoryImpl.verificationId = verificationId
                        resendToken = token
                        continuation.resume(Result.success(Unit))
                    }
                }

                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phoneNumber)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(activity)
                    .setCallbacks(callbacks)
                    .setForceResendingToken(currentResendToken)
                    .build()

                PhoneAuthProvider.verifyPhoneNumber(options)
            }
        } catch (e: Exception) {
            resetPhoneAuth()
            Result.failure(e)
        }
    }

    override fun resetPhoneAuth() {
        verificationId = null
        resendToken = null
    }

    override suspend fun signOut(): Result<Unit> = try {
        auth.signOut()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getCurrentUser(): Result<User> = try {
        val firebaseUser = auth.currentUser ?: throw Exception("Not authenticated")

        val userDoc = firestore.collection("users")
            .document(firebaseUser.uid)
            .get()
            .await()

        val user = userDoc.toObject<User>() ?: throw Exception("User data not found")
        Result.success(user)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signInWithPhoneNumber(
        phoneNumber: String,
        activity: Activity,
        password: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    private suspend fun checkPlatformConnection(uid: String): Boolean {
        return try {
            val document = firestore.collection("users")
                .document(uid)
                .get()
                .await()
            
            val connectedPlatforms = document.get("connectedPlatforms") as? List<*>
            connectedPlatforms?.isNotEmpty() == true
        } catch (e: Exception) {
            false
        }
    }
}
package com.example.likebox.data.repository

import android.app.Activity
import com.example.likebox.di.ApplicationScope
import com.example.likebox.domain.model.auth.User
import com.example.likebox.domain.repository.AuthRepository
import com.example.likebox.presentation.view.screens.auth.state.AuthState
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseAuthWebException
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
import kotlin.coroutines.Continuation
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


    override suspend fun signInWithEmail(email: String, password: String): Result<Unit> = try {
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password).await()
        Result.success(Unit)
    } catch (e: FirebaseAuthException) {
        handleFirebaseAuthError(e)
    }

    override suspend fun signInWithPhoneNumber(
        phoneNumber: String,
        activity: Activity,
        password: String
    ): Result<Unit> = try {
        suspendCoroutine { continuation ->
            val callbacks = setupPhoneAuthCallbacks(phoneNumber, continuation, isSignUp = true)
            setupPhoneAuthCallbacks(phoneNumber, continuation)
            initiatePhoneNumberVerification(
                phoneNumber = phoneNumber,
                activity = activity,
                callbacks = callbacks
            )
        }
    } catch (e: Exception) {
        resetPhoneAuth()
        Result.failure(e)
    }

    override suspend fun signUpWithEmail(
        email: String,
        password: String,
        nickname: String
    ): Result<Unit> {
        return try {
            val emailExists = checkEmailExists(email).getOrNull()
            if (emailExists == true) {
                return Result.failure(Exception("Email is already registered"))
            }

            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("Failed to create user")

            createUserInFirestore(uid, email, nickname)
            sendEmailVerification()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUpWithPhoneNumber(
        phoneNumber: String,
        activity: Activity,
        password: String
    ): Result<Unit> = try {
        suspendCoroutine { continuation ->
            val callbacks = setupPhoneAuthCallbacks(phoneNumber, continuation, isSignUp = true)
            initiatePhoneNumberVerification(
                phoneNumber = phoneNumber,
                activity = activity,
                callbacks = callbacks
            )
        }
    } catch (e: Exception) {
        resetPhoneAuth()
        Result.failure(e)
    }

    override suspend fun deleteAccount(): Result<Unit> {
        return try {
            val user = auth.currentUser ?: return Result.failure(Exception("User not authenticated"))
            val uid = user.uid

            // 1. Firestore에서 사용자 데이터 삭제
            firestore.collection("users")
                .document(uid)
                .delete()
                .await()

            // 2. Firebase Auth에서 사용자 삭제
            user.delete().await()

            // 3. 상태 초기화
            _authState.value = AuthState.NotAuthenticated
            _currentUser.value = null

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): Result<User> = try {
        val firebaseUser = auth.currentUser ?: throw Exception("Not authenticated")

        val userDoc = firestore.collection("users")
            .document(firebaseUser.uid)
            .get()
            .await()

        val user = userDoc.toObject<User>() ?: throw Exception("User data not found")
        _currentUser.value = user
        Result.success(user)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun checkEmailExists(email: String): Result<Boolean> = try {
        val querySnapshot = firestore.collection("users")
            .whereEqualTo("email", email)
            .get()
            .await()

        Result.success(!querySnapshot.isEmpty)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun checkEmailVerification(): Result<Boolean> = try {
        auth.currentUser?.reload()?.await()
        val isVerified = auth.currentUser?.isEmailVerified ?: false
        Result.success(isVerified)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun resendVerificationCode(
        phoneNumber: String,
        activity: Activity
    ): Result<Unit> {
        return try {
            val currentResendToken = resendToken ?: return Result.failure(Exception("Resend token not found"))

            suspendCoroutine { continuation ->
                val callbacks = createPhoneAuthCallbacks(phoneNumber, continuation)
                initiatePhoneNumberVerification(phoneNumber, activity, currentResendToken, callbacks)
            }
        } catch (e: Exception) {
            resetPhoneAuth()
            Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Unit> = try {
        auth.signOut()
        _authState.value = AuthState.NotAuthenticated
        _currentUser.value = null
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private fun createPhoneAuthCallbacks(
        phoneNumber: String,
        continuation: Continuation<Result<Unit>>
    ): PhoneAuthProvider.OnVerificationStateChangedCallbacks {
        return object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                scope.launch {
                    try {
                        auth.signInWithCredential(credential).await()
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
                    is FirebaseAuthInvalidCredentialsException ->
                        continuation.resume(Result.failure(Exception("Invalid phone number")))
                    is FirebaseTooManyRequestsException ->
                        continuation.resume(Result.failure(Exception("Too many requests")))
                    else -> continuation.resume(Result.failure(e))
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
    }

    private fun initiatePhoneNumberVerification(
        phoneNumber: String,
        activity: Activity,
        resendToken: PhoneAuthProvider.ForceResendingToken? = null,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)

        resendToken?.let { token ->
            optionsBuilder.setForceResendingToken(token)
        }

        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }

    override fun resetPhoneAuth() {
        verificationId = null
        resendToken = null
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

    /**
     * Firestore에 새로운 사용자 정보 생성
     */
    private suspend fun createUserInFirestore(
        uid: String,
        email: String? = null,
        nickname: String? = null,
        phoneNumber: Long? = null
    ) {
        val user = User(
            userId = uid,
            email = email ?: "",
            nickName = nickname ?: "",
            phoneNumber = phoneNumber,
            profilePictureUrl = null,
            connectedPlatforms = emptyList()
        )

        firestore.collection("users")
            .document(uid)
            .set(user)
            .await()
    }

    /**
     * 전화번호 인증 코드 확인
     */
    override suspend fun verifyPhoneCode(verificationCode: String): Result<Unit> {
        return try {
            val currentVerificationId = verificationId ?:
            return Result.failure(Exception("Verification ID not found"))

            val credential = PhoneAuthProvider.getCredential(
                currentVerificationId,
                verificationCode
            )

            val authResult = auth.signInWithCredential(credential).await()

            // 새로운 사용자인 경우 Firestore에 문서 생성
            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
            if (isNewUser) {
                val phoneNumber = authResult.user?.phoneNumber?.filter { it.isDigit() }?.toLongOrNull()
                createUserInFirestore(
                    uid = authResult.user?.uid ?: throw Exception("User ID not found"),
                    phoneNumber = phoneNumber
                )
            }

            resetPhoneAuth() // 성공 후 상태 초기화
            Result.success(Unit)
        } catch (e: Exception) {
            resetPhoneAuth()

            when (e) {
                is FirebaseAuthInvalidCredentialsException ->
                    Result.failure<Unit>(Exception("Invalid verification code"))
                else -> Result.failure(e)
            }.also {
                resetPhoneAuth() // 실패 후 상태 초기화
            }
        }
    }

    /**
     * 전화번호 인증 콜백 설정
     */
    private fun setupPhoneAuthCallbacks(
        phoneNumber: String,
        continuation: Continuation<Result<Unit>>,
        isSignUp: Boolean = false
    ): PhoneAuthProvider.OnVerificationStateChangedCallbacks {
        return object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                scope.launch {
                    try {
                        val authResult = auth.signInWithCredential(credential).await()

                        // 회원가입인 경우 추가 처리
                        if (isSignUp && authResult.additionalUserInfo?.isNewUser == true) {
                            val phoneNum = phoneNumber.filter { it.isDigit() }.toLongOrNull()
                            createUserInFirestore(
                                uid = authResult.user?.uid ?: throw Exception("User ID not found"),
                                phoneNumber = phoneNum
                            )
                        }

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
                val error = when (e) {
                    is FirebaseAuthInvalidCredentialsException ->
                        Exception("Invalid phone number")
                    is FirebaseTooManyRequestsException ->
                        Exception("Too many requests. Please try again later")
                    else -> e
                }
                continuation.resume(Result.failure(error))
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
    }

    /**
     * 이메일 인증 메일 발송
     */
    private suspend fun sendEmailVerification(): Result<Unit> {
        return try {
            val currentUser = auth.currentUser ?:
            return Result.failure(Exception("No authenticated user found"))

            currentUser.sendEmailVerification().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to send verification email: ${e.message}"))
        }
    }

    // Firebase Auth Error 핸들링
    private fun handleFirebaseAuthError(e: FirebaseAuthException): Result<Unit> {
        val message = when (e) {
            // 이메일 관련 에러
            is FirebaseAuthInvalidUserException -> when (e.errorCode) {
                "ERROR_USER_NOT_FOUND" -> "No account found with this email"
                "ERROR_USER_DISABLED" -> "This account has been disabled"
                "ERROR_USER_TOKEN_EXPIRED" -> "Session expired. Please sign in again"
                else -> "Account not found or disabled"
            }

            // 인증정보 관련 에러
            is FirebaseAuthInvalidCredentialsException -> when (e.errorCode) {
                "ERROR_INVALID_EMAIL" -> "Please enter a valid email address"
                "ERROR_WRONG_PASSWORD" -> "Incorrect password. Please try again"
                "ERROR_INVALID_CREDENTIAL" -> when {
                    e.message?.contains("email", ignoreCase = true) == true ->
                        "Please check your email format"
                    e.message?.contains("password", ignoreCase = true) == true ->
                        "Please check your password"
                    else -> "Invalid login information. Please check your details"
                }
                "ERROR_INVALID_VERIFICATION_CODE" -> "Invalid verification code. Please try again"
                "ERROR_INVALID_VERIFICATION_ID" -> "Verification failed. Please request a new code"
                else -> "Invalid credentials provided"
            }

            // 비밀번호 관련 에러
            is FirebaseAuthWeakPasswordException ->
                "Password should be at least 6 characters"

            // 이메일 중복 에러
            is FirebaseAuthUserCollisionException -> when (e.errorCode) {
                "ERROR_EMAIL_ALREADY_IN_USE" ->
                    "This email is already registered"
                "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" ->
                    "Account exists with different credentials"
                else -> "Account already exists"
            }

            // 너무 많은 요청
            is FirebaseTooManyRequestsException ->
                "Too many attempts. Please try again later"

            // 네트워크 에러
            is FirebaseAuthWebException ->
                "Network error. Please check your connection"

            // 기타 에러
            else -> e.message ?: "Authentication failed"
        }

        _authState.value = AuthState.Error(message)
        return Result.failure(Exception(message))
    }

}
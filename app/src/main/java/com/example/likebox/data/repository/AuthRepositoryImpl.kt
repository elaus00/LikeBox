package com.example.likebox.data.repository

import com.example.likebox.domain.model.auth.User
import com.example.likebox.domain.repository.AuthRepository
import com.example.likebox.domain.repository.PlatformRepository
import com.example.likebox.presentation.state.AuthState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val platformRepository: PlatformRepository
) : AuthRepository {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    override val authState = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser = _currentUser.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        firebaseAuth.addAuthStateListener { auth ->
            scope.launch {
                checkAuthState()
            }
        }
    }

    private suspend fun checkAuthState() {
        val user = firebaseAuth.currentUser

        if (user == null) {
            _authState.value = AuthState.NotAuthenticated
            _currentUser.value = null
            return
        }

        try {
            // Result<List<MusicPlatform>>을 처리
            platformRepository.getConnectedPlatforms()
                .fold(
                    onSuccess = { platforms ->
                        if (platforms.isEmpty()) {
                            _authState.value = AuthState.NeedsPlatformSetup
                            return
                        }

                        // Firestore에서 사용자 정보 가져오기
                        val userDoc = firestore.collection("users")
                            .document(user.uid)
                            .get()
                            .await()

                        val userData = userDoc.toObject(User::class.java)
                        _currentUser.value = userData
                        _authState.value = AuthState.Authenticated
                    },
                    onFailure = { e ->
                        _authState.value = AuthState.Error(e.message ?: "Failed to get connected platforms")
                    }
                )
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.message ?: "Authentication check failed")
        }
    }

    override suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUp(email: String, password: String, nickname: String): Result<Unit> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

            // 사용자 정보 Firestore에 저장
            val user = User(
                userId = authResult.user!!.uid,
                email = email,
                nickName = nickname,
                profilePictureUrl = null,
                connectedPlatforms = emptyList(),
                phoneNumber = null
            )

            firestore.collection("users")
                .document(user.userId)
                .set(user)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
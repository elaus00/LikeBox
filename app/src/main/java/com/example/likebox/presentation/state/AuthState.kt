package com.example.likebox.presentation.state

sealed class AuthState {
    // 인증된 상태: 로그인 되어있고, 플랫폼 연동도 완료됨
    data object Authenticated : AuthState()

    // 인증되지 않은 상태: 로그인이 필요함
    data object NotAuthenticated : AuthState()

    // 인증은 됐지만 플랫폼 연동이 필요한 상태
    data object NeedsPlatformSetup : AuthState()

    // 에러 상태를 추가할 수도 있습니다
    data class Error(val message: String) : AuthState()

    // 로딩 상태를 추가할 수도 있습니다
    data object Loading : AuthState()
}
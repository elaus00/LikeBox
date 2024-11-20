package com.example.likebox.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.likebox.domain.usecase.auth.AuthState
import com.example.likebox.domain.usecase.auth.CheckAuthStateUseCase
import com.example.likebox.presentation.view.navigation.NavigationCommand
import com.example.likebox.presentation.view.navigation.NavigationState
import com.example.likebox.presentation.view.screens.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 앱의 전체 화면 이동(네비게이션)을 관리하는 ViewModel
 *
 * 주요 기능:
 * - 화면 이동 상태 관리 (navigationState)
 * - 화면 이동 명령 처리 (navigationCommands)
 * - 이전 화면 이력 관리
 * - 다양한 화면 이동 패턴 지원 (일반 이동, 스택 초기화, 뒤로 가기 등)
 */
@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val checkAuthStateUseCase: CheckAuthStateUseCase
) : ViewModel() {

    private val _navigationState = MutableStateFlow(NavigationState())
    val navigationState = _navigationState.asStateFlow()

    private val _navigationCommands = Channel<NavigationCommand>()
    val navigationCommands = _navigationCommands.receiveAsFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            _isLoading.value = false
            try {
                when (checkAuthStateUseCase()) {
                    AuthState.Authenticated -> {
                        _navigationCommands.send(NavigationCommand.NavigateToAndClearStack(Screens.Root.Main))
                    }
                    AuthState.NotAuthenticated -> {
                        _navigationCommands.send(NavigationCommand.NavigateToAndClearStack(Screens.Root.Auth))
                    }
                    AuthState.NeedsPlatformSetup -> {
                        _navigationCommands.send(NavigationCommand.NavigateToAndClearStack(Screens.Auth.PlatformSetup.Selection))
                    }
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    // === 인증 관련 화면 이동 함수들 ===
    fun navigateToSignIn() = NavigationCommand.NavigateTo(Screens.Auth.SignIn)
    fun navigateToSignUp() = NavigationCommand.NavigateTo(Screens.Auth.SignUp)
    fun navigateToOnboarding() = NavigationCommand.NavigateTo(Screens.Auth.OnBoarding)
    fun navigateToPlatformSetup() = NavigationCommand.NavigateTo(Screens.Auth.PlatformSetup.Selection)

    // === 메인 화면 이동 함수들 ===
    fun navigateToHome() = NavigationCommand.NavigateTo(Screens.Main.Home.Root)
    fun navigateToSearch() = NavigationCommand.NavigateTo(Screens.Main.Search.Root)
    fun navigateToLibrary() = NavigationCommand.NavigateTo(Screens.Main.Library.Root)
    fun navigateToSettings() = NavigationCommand.NavigateTo(Screens.Main.Settings.Root)

    // === 라이브러리 관련 화면 이동 ===
    fun navigateToPlaylistDetail(playlistId: String) =
        NavigationCommand.NavigateToWithArgs(
            Screens.Main.Library.Details.PlaylistDetail(playlistId),
            playlistId
        )

    // === 검색 관련 화면 이동 ===
    fun navigateToSearchResults(query: String, category: String): NavigationCommand? {
        val screen = when (category) {
            "tracks" -> Screens.Main.Search.Results.Category.Tracks(query)
            "artists" -> Screens.Main.Search.Results.Category.Artists(query)
            "albums" -> Screens.Main.Search.Results.Category.Albums(query)
            "playlists" -> Screens.Main.Search.Results.Category.Playlists(query)
            else -> null
        }
        return screen?.let { NavigationCommand.NavigateToWithArgs(it, query) }
    }

    // === 플랫폼별 화면 이동 ===
    fun navigateToPlatformContent(platform: String): NavigationCommand? {
        val screen = when (platform) {
            "spotify" -> Screens.Main.Home.Platform.Spotify
            "youtube" -> Screens.Main.Home.Platform.Youtube
            "apple-music" -> Screens.Main.Home.Platform.AppleMusic
            else -> null
        }
        return screen?.let { NavigationCommand.NavigateTo(it) }
    }

    // === 설정 관련 화면 이동 ===
    fun navigateToAccountSettings() = NavigationCommand.NavigateTo(Screens.Main.Settings.Account.Root)
    fun navigateToSecuritySettings() = NavigationCommand.NavigateTo(Screens.Main.Settings.Account.Security)
    fun navigateToNotificationSettings() = NavigationCommand.NavigateTo(Screens.Main.Settings.Account.Notifications)
    fun navigateToLinkedPlatforms() = NavigationCommand.NavigateTo(Screens.Main.Settings.Account.LinkedPlatforms)

    fun navigateBack() = NavigationCommand.NavigateBack

    fun onNavigationComplete() {
        _navigationState.update { it.copy(isNavigating = false) }
    }

    fun sendNavigationCommand(command: NavigationCommand) {
        viewModelScope.launch {
            _navigationCommands.send(command)
        }
    }
}
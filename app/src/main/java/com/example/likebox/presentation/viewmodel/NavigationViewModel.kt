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
            _isLoading.value = true
            try {
                when (checkAuthStateUseCase()) {
                    AuthState.Authenticated -> {
                        navigate(NavigationCommand.NavigateToAndClearStack(Screens.Home.Root))
                    }
                    AuthState.NotAuthenticated -> {
                        navigate(NavigationCommand.NavigateToAndClearStack(Screens.Auth.OnBoarding))
                    }
                    AuthState.NeedsPlatformSetup -> {
                        navigate(NavigationCommand.NavigateToAndClearStack(Screens.Auth.PlatformSetup.Root))
                    }
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
    /**
     * 주어진 네비게이션 명령을 처리하는 함수
     *
     * @param command 실행할 네비게이션 명령
     * - NavigateTo: 일반적인 화면 이동
     * - NavigateToAndClearStack: 이전 화면들을 모두 제거하고 새로운 화면으로 이동
     * - NavigateBack: 이전 화면으로 돌아가기
     * - NavigateToRoot: 루트 화면으로 이동
     * - NavigateToWithArgs: 인자와 함께 새로운 화면으로 이동
     */
    fun navigate(command: NavigationCommand) {
        viewModelScope.launch {
            when (command) {
                is NavigationCommand.NavigateTo -> {
                    _navigationState.update { currentState ->
                        currentState.copy(
                            previousScreens = currentState.previousScreens + currentState.currentScreen,
                            currentScreen = command.screen,
                            isNavigating = true
                        )
                    }
                    _navigationCommands.send(command)
                }

                is NavigationCommand.NavigateToAndClearStack -> {
                    _navigationState.update { currentState ->
                        currentState.copy(
                            previousScreens = emptyList(),
                            currentScreen = command.screen,
                            isNavigating = true
                        )
                    }
                    _navigationCommands.send(command)
                }

                is NavigationCommand.NavigateBack -> {
                    _navigationState.update { currentState ->
                        if (currentState.previousScreens.isNotEmpty()) {
                            currentState.copy(
                                currentScreen = currentState.previousScreens.last(),
                                previousScreens = currentState.previousScreens.dropLast(1),
                                isNavigating = true
                            )
                        } else {
                            currentState
                        }
                    }
                    _navigationCommands.send(command)
                }

                is NavigationCommand.NavigateToRoot -> {
                    _navigationState.update { currentState ->
                        currentState.copy(
                            previousScreens = emptyList(),
                            currentScreen = command.screen,
                            isNavigating = true
                        )
                    }
                    _navigationCommands.send(command)
                }

                is NavigationCommand.NavigateToWithArgs<*> -> {
                    _navigationState.update { currentState ->
                        currentState.copy(
                            previousScreens = currentState.previousScreens + currentState.currentScreen,
                            currentScreen = command.screen,
                            isNavigating = true
                        )
                    }
                    _navigationCommands.send(command)
                }
            }
        }
    }

    // === 인증 관련 화면 이동 함수들 ===
    fun navigateToSignIn() = navigate(NavigationCommand.NavigateTo(Screens.Auth.SignIn))
    fun navigateToSignUp() = navigate(NavigationCommand.NavigateTo(Screens.Auth.SignUp))
    fun navigateToOnboarding() = navigate(NavigationCommand.NavigateTo(Screens.Auth.OnBoarding))
    fun navigateToPlatformSetup() = navigate(NavigationCommand.NavigateTo(Screens.Auth.PlatformSetup.Root))

    // === 메인 화면 이동 함수들 ===
    fun navigateToHome() = navigate(NavigationCommand.NavigateToAndClearStack(Screens.Home.Root))
    fun navigateToSearch() = navigate(NavigationCommand.NavigateTo(Screens.Search.Root))
    fun navigateToLibrary() = navigate(NavigationCommand.NavigateTo(Screens.Library.Root))
    fun navigateToSettings() = navigate(NavigationCommand.NavigateTo(Screens.Settings.Root))

    // === 라이브러리 관련 화면 이동 ===
    /**
     * 플레이리스트 상세 화면으로 이동
     * @param playlistId 표시할 플레이리스트의 고유 ID
     */
    fun navigateToPlaylistDetail(playlistId: String) = navigate(
        NavigationCommand.NavigateToWithArgs(
            Screens.Library.Details.PlaylistDetail(playlistId),
            playlistId
        )
    )

    // === 검색 관련 화면 이동 ===
    /**
     * 검색 결과 화면으로 이동
     * @param query 검색어
     * @param category 검색 카테고리 (tracks, artists, albums, playlists)
     */
    fun navigateToSearchResults(query: String, category: String) {
        val screen = when (category) {
            "tracks" -> Screens.Search.Results.Category.Tracks(query)
            "artists" -> Screens.Search.Results.Category.Artists(query)
            "albums" -> Screens.Search.Results.Category.Albums(query)
            "playlists" -> Screens.Search.Results.Category.Playlists(query)
            else -> null
        }
        screen?.let { navigate(NavigationCommand.NavigateToWithArgs(it, query)) }
    }

    // === 플랫폼별 화면 이동 ===
    /**
     * 특정 음악 플랫폼의 콘텐츠 화면으로 이동
     * @param platform 플랫폼 이름 (spotify, youtube, apple-music)
     */
    fun navigateToPlatformContent(platform: String) {
        val screen = when (platform) {
            "spotify" -> Screens.Home.Platform.Spotify
            "youtube" -> Screens.Home.Platform.Youtube
            "apple-music" -> Screens.Home.Platform.AppleMusic
            else -> null
        }
        screen?.let { navigate(NavigationCommand.NavigateTo(it)) }
    }

    // 이전 화면으로 돌아가기
    fun navigateBack() = navigate(NavigationCommand.NavigateBack)

    /**
     * 화면 이동이 완료되었음을 표시
     * 애니메이션이나 전환 효과가 끝난 후 호출됨
     */
    fun onNavigationComplete() {
        _navigationState.update { it.copy(isNavigating = false) }
    }
}
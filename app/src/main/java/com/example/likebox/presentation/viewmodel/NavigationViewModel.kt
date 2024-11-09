package com.example.likebox.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.likebox.presentation.view.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor() : ViewModel() {

    // 현재 화면 상태
    private val _currentScreen = MutableStateFlow<Screens>(Screens.Auth.Onboarding)
    val currentScreen = _currentScreen.asStateFlow()

    // Navigation 이벤트를 위한 Channel
    private val _navigationEvents = Channel<NavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    // 이전 화면들의 스택
    private val _navigationStack = MutableStateFlow<List<Screens>>(listOf())
    val navigationStack = _navigationStack.asStateFlow()

    // Navigation Event 처리
    fun navigate(event: NavigationEvent) {
        viewModelScope.launch {
            when (event) {
                is NavigationEvent.NavigateTo -> {
                    _navigationStack.value = _navigationStack.value + _currentScreen.value
                    _currentScreen.value = event.screen
                    _navigationEvents.send(event)
                }

                is NavigationEvent.NavigateBack -> {
                    if (_navigationStack.value.isNotEmpty()) {
                        _currentScreen.value = _navigationStack.value.last()
                        _navigationStack.value = _navigationStack.value.dropLast(1)
                        _navigationEvents.send(NavigationEvent.NavigateBack)
                    }
                }

                is NavigationEvent.NavigateToAndClearStack -> {
                    _navigationStack.value = emptyList()
                    _currentScreen.value = event.screen
                    _navigationEvents.send(event)
                }
            }
        }
    }

    // Auth Flow Navigation
    fun navigateToSignIn() = navigate(NavigationEvent.NavigateTo(Screens.Auth.SignIn))
    fun navigateToSignUp() = navigate(NavigationEvent.NavigateTo(Screens.Auth.SignUp))
    fun navigateToOnboarding() = navigate(NavigationEvent.NavigateTo(Screens.Auth.Onboarding))

    // Platform Setup Flow Navigation
    fun navigateToPlatformSelection() = navigate(NavigationEvent.NavigateTo(Screens.PlatformSetup.Selection))
    fun navigateToPlatformConnection() = navigate(NavigationEvent.NavigateTo(Screens.PlatformSetup.Connection))

    // Main Flow Navigation
    fun navigateToMain() = navigate(NavigationEvent.NavigateToAndClearStack(Screens.Main))

    // Back Navigation
    fun navigateBack() = navigate(NavigationEvent.NavigateBack)
}

// Navigation Events를 정의하는 sealed interface
sealed interface NavigationEvent {
    data class NavigateTo(val screen: Screens) : NavigationEvent
    data class NavigateToAndClearStack(val screen: Screens) : NavigationEvent
    object NavigateBack : NavigationEvent
}

// Navigation 상태를 관리하는 데이터 클래스
data class NavigationState(
    val currentScreen: Screens = Screens.Auth.Onboarding,
    val navigationStack: List<Screens> = emptyList()
)
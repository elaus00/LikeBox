package com.example.likebox.presentation.view.navigation

import com.example.likebox.presentation.view.screens.Screens

sealed interface NavigationCommand {
    data class NavigateTo(val screen: Screens) : NavigationCommand
    data class NavigateToAndClearStack(val screen: Screens) : NavigationCommand
    data object NavigateBack : NavigationCommand
    data class NavigateToWithArgs<T>(val screen: Screens, val args: T) : NavigationCommand
    data class NavigateToRoot(val screen: Screens) : NavigationCommand
}

data class NavigationState(
    val currentScreen: Screens = Screens.Auth.OnBoarding,
    val previousScreens: List<Screens> = emptyList(),
    val isNavigating: Boolean = false
)
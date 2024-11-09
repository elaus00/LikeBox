package com.example.likebox.presentation.view.navigation

sealed class Screens(val route: String) {
    // Auth Flow
    object Auth : Screens("auth") {
        object Onboarding : Screens("auth/onboarding")
        object SignIn : Screens("auth/sign_in")
        object SignUp : Screens("auth/sign_up")
    }

    // Platform Setup Flow
    object PlatformSetup : Screens("platform_setup") {
        object Selection : Screens("platform_setup/selection")
        object Connection : Screens("platform_setup/connection")
    }

    // Main Flow
    object Main : Screens("main") {
        object Home : Screens("main/home")
        object Search : Screens("main/search")
        object Library : Screens("main/library")
        object Settings : Screens("main/settings")

        // Bottom Navigation에서 사용할 수 있는 route 목록
        val bottomNavRoutes = listOf(
            Home.route,
            Search.route,
            Library.route,
            Settings.route
        )
    }
}
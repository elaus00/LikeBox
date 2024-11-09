package com.example.likebox.navigation

sealed class Screen(val route: String) {

    data object Auth : Screen("auth") {
        data object Onboarding : Screen("auth/onboarding")
        data object SignIn : Screen("auth/signin")
        data object SignUp : Screen("auth/signup")
    }

    data object PlatformSetup : Screen("platform_setup") {
        data object Selection : Screen("platform_setup/selection")
        data object Connection : Screen("platform_setup/connection")
    }

    data object Main : Screen("main") {
        data object Home : Screen("main/home")
        data object Search : Screen("main/search")
        data object Library : Screen("main/library")
        data object Settings : Screen("main/settings")
    }

    data object Library : Screen("library") {
        data object Likes : Screen("library/likes")
        data object Playlists : Screen("library/playlists")
        data object Albums : Screen("library/albums")
        data object Artists : Screen("library/artists")
    }

    data object Settings : Screen("settings") {
        data object PlatformManage : Screen("settings/platform")
        data object Notifications : Screen("settings/notifications")
        data object Account : Screen("settings/account")
        data object AppInfo : Screen("settings/info")
    }
}


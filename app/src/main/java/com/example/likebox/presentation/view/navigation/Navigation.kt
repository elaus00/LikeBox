package com.example.likebox.presentation.view.navigation

sealed class Screen(val route: String) {

    data object Auth : Screens("auth") {
        data object Onboarding : Screens("auth/onboarding")
        data object SignIn : Screens("auth/signin")
        data object SignUp : Screens("auth/signup")
    }

    data object PlatformSetup : Screens("platform_setup") {
        data object Selection : Screens("platform_setup/selection")
        data object Connection : Screens("platform_setup/connection")
    }

    data object Main : Screens("main") {
        data object Home : Screens("main/home")
        data object Search : Screens("main/search")
        data object Library : Screens("main/library")
        data object Settings : Screens("main/settings")
    }

    data object Library : Screens("library") {
        data object Likes : Screens("library/likes")
        data object Playlists : Screens("library/playlists")
        data object Albums : Screens("library/albums")
        data object Artists : Screens("library/artists")
    }

    data object Settings : Screens("settings") {
        data object PlatformManage : Screens("settings/platform")
        data object Notifications : Screens("settings/notifications")
        data object Account : Screens("settings/account")
        data object AppInfo : Screens("settings/info")
    }
}


package com.example.likebox

import LibraryScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.likebox.navigation.PlatformConnectionScreen
import com.example.likebox.navigation.PlatformSelectionScreen

sealed class Screen(val route: String) {
    object Auth : Screen("auth") {
        object SignIn : Screen("auth/signin")
        object SignUp : Screen("auth/signup")
    }

    object PlatformSetup : Screen("platform_setup") {
        object Selection : Screen("platform_setup/selection")
        object Connection : Screen("platform_setup/connection")
    }

    object Main : Screen("main") {
        object Home : Screen("main/home")
        object Search : Screen("main/search")
        object Library : Screen("main/library")
        object Settings : Screen("main/settings")
    }

    object Library : Screen("library") {
        object Likes : Screen("library/likes")
        object Playlists : Screen("library/playlists")
        object Albums : Screen("library/albums")
        object Artists : Screen("library/artists")
    }

    object Settings : Screen("settings") {
        object PlatformManage : Screen("settings/platform")
        object Notifications : Screen("settings/notifications")
        object Account : Screen("settings/account")
        object AppInfo : Screen("settings/info")
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Auth.route
    ) {
        // Auth Navigation Graph
        navigation(
            startDestination = Screen.Auth.SignIn.route,
            route = Screen.Auth.route
        ) {
            composable(Screen.Auth.SignIn.route) {
                SignInScreen(
                    onNavigateToSignUp = {
                        navController.navigate(Screen.Auth.SignUp.route)
                    },
                    onSignInSuccess = {
                        navController.navigate(Screen.PlatformSetup.route)
                    }
                )
            }
            composable(Screen.Auth.SignUp.route) {
                SignUpScreen(
                    onNavigateToSignIn = {
                        navController.navigate(Screen.Auth.SignIn.route)
                    },
                    onSignUpSuccess = {
                        navController.navigate(Screen.PlatformSetup.route)
                    }
                )
            }
        }

        // Platform Setup Navigation Graph
        navigation(
            startDestination = Screen.PlatformSetup.Selection.route,
            route = Screen.PlatformSetup.route
        ) {
            composable(Screen.PlatformSetup.Selection.route) {
                PlatformSelectionScreen(
                    onPlatformsSelected = {
                        navController.navigate(Screen.PlatformSetup.Connection.route)
                    }
                )
            }
            composable(Screen.PlatformSetup.Connection.route) {
                PlatformConnectionScreen(
                    onConnectionComplete = {
                        navController.navigate(Screen.Main.route)
                    }
                )
            }
        }

        // Main Navigation Graph
        navigation(
            startDestination = Screen.Main.Home.route,
            route = Screen.Main.route
        ) {
            composable(Screen.Main.Home.route) {
                HomeScreen()
            }
            composable(Screen.Main.Search.route) {
                SearchScreen()
            }
            composable(Screen.Main.Library.route) {
                LibraryScreen()
            }
            composable(Screen.Main.Settings.route) {
                SettingsScreen()
            }
        }
    }
}


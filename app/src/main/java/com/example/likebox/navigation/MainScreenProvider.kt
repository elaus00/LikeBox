package com.example.likebox.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.likebox.MainScreen
import com.example.likebox.Screen
import com.example.likebox.SignInScreen
import com.example.likebox.SignUpScreen

@Composable
fun NavigationProvider(
    startDestination: String = Screen.Auth.route,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Auth Flow
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

        // Platform Setup Flow
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
                        navController.navigate(Screen.Main.route) {
                            popUpTo(Screen.Auth.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        // Main Flow
        composable(Screen.Main.route) {
            MainScreen()
        }
    }
}

@Composable
fun PlatformConnectionScreen(onConnectionComplete: () -> Unit) {
    TODO()
}

@Composable
fun PlatformSelectionScreen(onPlatformsSelected: () -> Unit) {
    TODO()
}

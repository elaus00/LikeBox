package com.example.likebox.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.likebox.ui.screens.MainScreen
import com.example.likebox.ui.screens.register.OnBoarding
import com.example.likebox.ui.screens.register.SignInScreen
import com.example.likebox.ui.screens.register.SignUpScreen

/*
NavigationProvider를 통해 시작 주소와 navController를 전달.
NavigationProvider는 NavHost를 호출해서 앱의 네비게이션 구조를 정의한다.
NavHost 내부적으로 AuthFlow에 대한 navigation, Platform SetUp에 대한 navigation, Main Flow에 대한 navigation을 정의한다.
Screen 객체는 앱의 주요 화면 경로를 정의함.
*/

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
            startDestination = Screen.Auth.Onboarding.route,
            route = Screen.Auth.route
        ) {
            composable(Screen.Auth.Onboarding.route){
                OnBoarding(navController = navController)
            }
            composable(Screen.Auth.SignIn.route) {
                SignInScreen(
                    onNavigateToSignUp = {
                        navController.navigate(Screen.Auth.SignUp.route)
                    },
                    onSignInSuccess = {
                        navController.navigate(Screen.PlatformSetup.route)
                    },
                    navController = navController
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

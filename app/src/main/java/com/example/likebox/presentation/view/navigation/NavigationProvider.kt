package com.example.likebox.presentation.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.likebox.presentation.view.screens.MainScreen
import com.example.likebox.presentation.view.screens.register.OnBoarding
import com.example.likebox.presentation.view.screens.register.SignInScreen
import com.example.likebox.presentation.view.screens.register.SignUpScreen

/*
NavigationProvider를 통해 시작 주소와 navController를 전달.
NavigationProvider는 NavHost를 호출해서 앱의 네비게이션 구조를 정의한다.
NavHost 내부적으로 AuthFlow에 대한 navigation, Platform SetUp에 대한 navigation, Main Flow에 대한 navigation을 정의한다.
Screen 객체는 앱의 주요 화면 경로를 정의함.
*/

@Composable
fun NavigationProvider(
    startDestination: String = Screens.Auth.route,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Auth Flow
        navigation(
            startDestination = Screens.Auth.Onboarding.route,
            route = Screens.Auth.route
        ) {
            composable(Screens.Auth.Onboarding.route){
                OnBoarding(navController = navController)
            }
            composable(Screens.Auth.SignIn.route) {
                SignInScreen(
                    onNavigateToSignUp = {
                        navController.navigate(Screens.Auth.SignUp.route)
                    },
                    onSignInSuccess = {
                        navController.navigate(Screens.PlatformSetup.route)
                    },
                    navController = navController
                )
            }
            composable(Screens.Auth.SignUp.route) {
                SignUpScreen(
                    onNavigateToSignIn = {
                        navController.navigate(Screens.Auth.SignIn.route)
                    },
                    onSignUpSuccess = {
                        navController.navigate(Screens.PlatformSetup.route)
                    }
                )
            }
        }

        // Platform Setup Flow
        navigation(
            startDestination = Screens.PlatformSetup.Selection.route,
            route = Screens.PlatformSetup.route
        ) {
            composable(Screens.PlatformSetup.Selection.route) {
                PlatformSelectionScreen(
                    onPlatformsSelected = {
                        navController.navigate(Screens.PlatformSetup.Connection.route)
                    }
                )
            }
            composable(Screens.PlatformSetup.Connection.route) {
                PlatformConnectionScreen(
                    onConnectionComplete = {
                        navController.navigate(Screens.Main.route) {
                            popUpTo(Screens.Auth.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        // Main Flow
        composable(Screens.Main.route) {
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

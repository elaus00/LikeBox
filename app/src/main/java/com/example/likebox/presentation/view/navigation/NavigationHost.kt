package com.example.likebox.presentation.view.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.hilt.navigation.compose.hiltViewModel

// 화면들
import com.example.likebox.presentation.view.screens.auth.OnBoardingScreen
import com.example.likebox.presentation.view.screens.auth.SignInScreen
import com.example.likebox.presentation.view.screens.auth.SignUpScreen
import com.example.likebox.presentation.view.screens.auth.platform.PlatformSelectionScreen
import com.example.likebox.presentation.view.screens.auth.platform.PlatformConnectionScreen
import com.example.likebox.presentation.view.screens.home.HomeScreen
import com.example.likebox.presentation.view.screens.search.SearchScreen
import com.example.likebox.presentation.view.screens.library.PlaylistsScreen
import com.example.likebox.presentation.view.screens.Screens
import com.example.likebox.presentation.view.screens.library.LibraryScreen
import com.example.likebox.presentation.view.screens.settings.SettingsScreen

// ViewModel과 Navigation 관련
import com.example.likebox.presentation.viewmodel.NavigationViewModel


@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: NavigationViewModel = hiltViewModel()
) {
    val currentScreen by viewModel.navigationState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationCommands.collect { command ->
            when (command) {
                is NavigationCommand.NavigateTo -> navController.navigate(command.screen.route)
                is NavigationCommand.NavigateToAndClearStack -> {
                    navController.navigate(command.screen.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
                is NavigationCommand.NavigateBack -> navController.navigateUp()
                is NavigationCommand.NavigateToRoot -> {
                    navController.navigate(command.screen.route) {
                        popUpTo(command.screen.route) { inclusive = true }
                    }
                }
                is NavigationCommand.NavigateToWithArgs<*> -> navController.navigate(command.screen.route)
            }
            viewModel.onNavigationComplete()
        }
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screens.Auth.Root.route
    ) {
        // Auth Flow
        navigation(
            startDestination = Screens.Auth.OnBoarding.route,
            route = Screens.Auth.Root.route
        ) {
            composable(Screens.Auth.OnBoarding.route) {
                OnBoardingScreen(navController)
            }
            composable(Screens.Auth.SignIn.route) {
                SignInScreen(navController)
            }
            composable(Screens.Auth.SignUp.route) {
                SignUpScreen(navController)
            }

            // Platform Setup - Auth의 하위 네비게이션
            navigation(
                startDestination = Screens.Auth.PlatformSetup.Selection.route,
                route = Screens.Auth.PlatformSetup.Root.route
            ) {
                composable(Screens.Auth.PlatformSetup.Selection.route) {
                    PlatformSelectionScreen()
                }
                composable(
                    route = Screens.Auth.PlatformSetup.Connection.route,
                    arguments = listOf(
                        navArgument("platformId") { type = NavType.StringType }
                    )
                ) { entry ->
                    entry.arguments?.getString("platformId")?.let {
                        PlatformConnectionScreen(platformId = it)
                    }
                }
                composable(Screens.Auth.PlatformSetup.Confirmation.route) {
                    Text("Platform Setup Confirmation")
                }
            }
        }

        // Home Flow
        navigation(
            startDestination = Screens.Home.Root.route,
            route = "home_graph"  // 그래프 route는 화면 route와 구분
        ) {
            composable(Screens.Home.Root.route) {
                HomeScreen()
            }

            // Platform-specific screens
            Screens.Home.Platform::class.sealedSubclasses.forEach { platform ->
                val route = (platform.objectInstance as? Screens.Home.Platform)?.route
                route?.let {
                    composable(it) {
                        when(platform.objectInstance) {
                            is Screens.Home.Platform.Spotify -> Text("Spotify Home")
                            is Screens.Home.Platform.Youtube -> Text("Youtube Home")
                            is Screens.Home.Platform.AppleMusic -> Text("Apple Music Home")
                            null -> Unit
                        }
                    }
                }
            }
        }

        // Search Flow
        navigation(
            startDestination = Screens.Search.Root.route,
            route = "search_graph"  // 그래프 route는 화면 route와 구분
        ) {
            composable(Screens.Search.Root.route) {
                SearchScreen()
            }

            composable(Screens.Search.Results.Category.Tracks.route,
                arguments = listOf(
                    navArgument("query") { type = NavType.StringType }
                )
            ) { entry ->
                Text("Tracks Search Results: ${entry.arguments?.getString("query")}")
            }
            // 다른 검색 결과 화면들...
        }

        // Library Flow
        navigation(
            startDestination = Screens.Library.Root.route,
            route = "library_graph"  // 그래프 route는 화면 route와 구분
        ) {
            composable(Screens.Library.Root.route) {
                LibraryScreen()
            }
            composable(Screens.Library.Playlists.route) {
                PlaylistsScreen()
            }
            composable(
                route = Screens.Library.Details.PlaylistDetail.route,
                arguments = listOf(
                    navArgument("playlistId") { type = NavType.StringType }
                )
            ) { entry ->
                Text("Playlist Detail: ${entry.arguments?.getString("playlistId")}")
            }
        }

        // Settings Flow
        navigation(
            startDestination = Screens.Settings.Root.route,
            route = "settings_graph"  // 그래프 route는 화면 route와 구분
        ) {
            composable(Screens.Settings.Root.route) {
                SettingsScreen()
            }
            composable(Screens.Settings.Profile.route) {
                Text("Profile Screen")
            }

            // Account Settings Sub-graph
            navigation(
                startDestination = Screens.Settings.Account.Root.route,
                route = "settings_account_graph"  // 하위 그래프 route도 구분
            ) {
                composable(Screens.Settings.Account.Root.route) {
                    Text("Account Settings")
                }
                composable(Screens.Settings.Account.Security.route) {
                    Text("Security Settings")
                }
                composable(Screens.Settings.Account.Notifications.route) {
                    Text("Notification Settings")
                }
                composable(Screens.Settings.Account.LinkedPlatforms.route) {
                    Text("Linked Platforms")
                }
            }
        }
    }
}
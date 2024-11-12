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
                is NavigationCommand.NavigateTo -> {
                    navController.navigate(command.screen.route)
                }
                is NavigationCommand.NavigateToAndClearStack -> {
                    navController.navigate(command.screen.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
                is NavigationCommand.NavigateBack -> {
                    navController.navigateUp()
                }
                is NavigationCommand.NavigateToRoot -> {
                    navController.navigate(command.screen.route) {
                        popUpTo(command.screen.route) { inclusive = true }
                    }
                }
                is NavigationCommand.NavigateToWithArgs<*> -> {
                    navController.navigate(command.screen.route)
                }
            }
            viewModel.onNavigationComplete()
        }
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screens.Auth.OnBoarding.route
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
                SignInScreen(
                    navController = navController
                )
            }
            composable(Screens.Auth.SignUp.route) {
                SignUpScreen(
                    navController = navController
                )
            }

            // Platform Setup Flow
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
                    entry.arguments?.getString("platformId")
                        ?.let { PlatformConnectionScreen(platformId = it) }
                }
                composable(Screens.Auth.PlatformSetup.Confirmation.route) {
                    Text("Platform Setup Confirmation") // TODO: Implement PlatformConfirmationScreen
                }
            }
        }

        // Home Flow
        navigation(
            startDestination = Screens.Home.Root.route,
            route = "home"
        ) {
            composable(Screens.Home.Root.route) {
                HomeScreen()
            }
        }

        // Search Flow
        navigation(
            startDestination = Screens.Search.Root.route,
            route = "search"
        ) {
            composable(Screens.Search.Root.route) {
                SearchScreen()
            }

            // Search Results by Category
            composable(
                route = Screens.Search.Results.Category.Tracks.route,
                arguments = listOf(
                    navArgument("query") { type = NavType.StringType }
                )
            ) { entry ->
                Text("Tracks Search Results: ${entry.arguments?.getString("query")}")
                // TODO: Implement TracksResultScreen
            }

            // More search result categories...
        }

        // Library Flow
        navigation(
            startDestination = Screens.Library.Root.route,
            route = "library"
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
                // TODO: Implement PlaylistDetailScreen
            }
        }

        // Settings Flow
        navigation(
            startDestination = Screens.Settings.Root.route,
            route = "settings"
        ) {
            composable(Screens.Settings.Root.route) {
                SettingsScreen()
            }
            composable(Screens.Settings.Profile.route) {
                Text("Profile Screen") // TODO: Implement ProfileScreen
            }
            composable(Screens.Settings.Account.LinkedPlatforms.route) {
                Text("Linked Platforms") // TODO: Implement LinkedPlatformsScreen
            }

            navigation(
                startDestination = Screens.Settings.Account.Root.route,
                route = "account"
            ) {
                composable(Screens.Settings.Account.Root.route) {
                    Text("Account Settings") // TODO: Implement AccountSettingsScreen
                }
                composable(Screens.Settings.Account.Security.route) {
                    Text("Security Settings") // TODO: Implement SecuritySettingsScreen
                }
                composable(Screens.Settings.Account.Notifications.route) {
                    Text("Notification Settings") // TODO: Implement NotificationSettingsScreen
                }
            }
        }
    }
}

package com.example.likebox.presentation.view.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.likebox.domain.model.Settings
import com.example.likebox.domain.model.settings.NotificationSettings
import com.example.likebox.domain.model.settings.SyncSettings
import com.example.likebox.domain.model.settings.ThemeSettings

// 화면들
import com.example.likebox.presentation.view.screens.auth.OnBoardingScreen
import com.example.likebox.presentation.view.screens.auth.SignInScreen
import com.example.likebox.presentation.view.screens.auth.SignUpScreen
import com.example.likebox.presentation.view.screens.auth.platform.PlatformSelectionScreen
import com.example.likebox.presentation.view.screens.auth.platform.PlatformConnectionScreen
import com.example.likebox.presentation.view.screens.home.HomeScreen
import com.example.likebox.presentation.view.screens.search.SearchScreen
import com.example.likebox.presentation.view.screens.Screens
import com.example.likebox.presentation.view.screens.library.ArtistDetailScreen
import com.example.likebox.presentation.view.screens.library.LibraryScreen
import com.example.likebox.presentation.view.screens.library.AlbumDetailScreen
import com.example.likebox.presentation.view.screens.library.TrackDetailScreen
import com.example.likebox.presentation.view.screens.library.detail.PlaylistDetailScreen
import com.example.likebox.presentation.view.screens.settings.SettingsScreen

// ViewModel과 Navigation 관련


@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screens.Root.Auth.route
    ) {
        // Auth Graph
        navigation(
            startDestination = Screens.Auth.OnBoarding.route,
            route = Screens.Root.Auth.route
        ) {
            composable(Screens.Auth.OnBoarding.route) {
                OnBoardingScreen(navController)
            }
            composable(Screens.Auth.SignIn.route) {
                SignInScreen(navController)
            }
            composable(Screens.Auth.OnBoarding.route) {
                OnBoardingScreen(navController)
            }
            composable(Screens.Auth.SignIn.route) {
                SignInScreen(navController)
            }
            composable(Screens.Auth.SignUp.Root.route) {
                SignUpScreen(navController)
            }
            composable(Screens.Auth.PlatformSetup.Selection.route) {
                PlatformSelectionScreen(navController)
            }
            composable(
                route = Screens.Auth.PlatformSetup.Connection.route,
                arguments = listOf(
                    navArgument("platformId") { type = NavType.StringType }
                )
            ) { entry ->
                entry.arguments?.getString("platformId")?.let { platformId ->
                    PlatformConnectionScreen(
                        platformId = platformId,
                        navController = navController
                    )
                }
            }
            composable(Screens.Auth.PlatformSetup.Confirmation.route) {
                Text("Platform Setup Confirmation")
            }
        }

        // Main Graph
        navigation(
            startDestination = Screens.Main.Home.Root.route,
            route = Screens.Root.Main.route
        ) {
            // Home Flow
            composable(Screens.Main.Home.Root.route) {
                HomeScreen(navController = navController)
            }

            Screens.Main.Home.Platform::class.sealedSubclasses.forEach { platform ->
                val route = (platform.objectInstance as? Screens.Main.Home.Platform)?.route
                route?.let {
                    composable(it) {
                        when(platform.objectInstance) {
                            is Screens.Main.Home.Platform.Spotify -> Text("Spotify Home")
                            is Screens.Main.Home.Platform.Youtube -> Text("Youtube Home")
                            is Screens.Main.Home.Platform.AppleMusic -> Text("Apple Music Home")
                            null -> Unit
                        }
                    }
                }
            }

            // Search Flow
            composable(Screens.Main.Search.Root.route) {
                SearchScreen(
                    navController = navController,
                    onNavigateBack = { navController.navigateUp() })
            }

            composable(
                Screens.Main.Search.Results.Category.Tracks.route,
                arguments = listOf(
                    navArgument("query") { type = NavType.StringType }
                )
            ) { entry ->
                Text("Tracks Search Results: ${entry.arguments?.getString("query")}")
            }

            // Library Flow
            composable(Screens.Main.Library.Root.route) {
                LibraryScreen(
                    navController = navController,
                    onNavigateToPlaylist = { playlistId ->
                        navController.navigate(
                            Screens.Main.Library.Details.PlaylistDetail(playlistId).route
                        )
                    },
                    onNavigateToAlbum = { albumId ->
                        navController.navigate(
                            Screens.Main.Library.Details.AlbumDetail(albumId).route
                        )
                    },
                    onNavigateToArtist = { artistId ->
                        navController.navigate(
                            Screens.Main.Library.Details.ArtistDetail(artistId).route
                        )
                    }
                )
            }

            composable(
                route = Screens.Main.Library.Details.TrackDetail.route,
                arguments = listOf(
                    navArgument("trackId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val trackId = backStackEntry.arguments?.getString("playlistId")
                TrackDetailScreen(
                    trackId = trackId!!,
                    navController = navController
                )
            }

            composable(
                route = Screens.Main.Library.Details.PlaylistDetail.route,
                arguments = listOf(
                    navArgument("playlistId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val playlistId = backStackEntry.arguments?.getString("playlistId")
                PlaylistDetailScreen(
                    playlistId = playlistId!!,
                    navController = navController
                )
            }

            composable(
                route = Screens.Main.Library.Details.AlbumDetail.route,
                arguments = listOf(
                    navArgument("albumId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val albumId = backStackEntry.arguments?.getString("albumId")
                AlbumDetailScreen(
                    navController = navController,
                    albumId = albumId
                )
            }

            composable(
                route = Screens.Main.Library.Details.ArtistDetail.route,
                arguments = listOf(
                    navArgument("artistId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val artistId = backStackEntry.arguments?.getString("artistId")
                ArtistDetailScreen(artistId = artistId)
            }

            composable(
                route = Screens.Main.Library.Details.PlaylistDetail.route,
                arguments = listOf(
                    navArgument("playlistId") { type = NavType.StringType }
                )
            ) { entry ->
                Text("Playlist Detail: ${entry.arguments?.getString("playlistId")}")
            }

            // Settings Flow
            composable(Screens.Main.Settings.Root.route) {
                SettingsScreen(
                    settings = Settings(
                        userId = "",  // 실제로는 사용자 ID가 필요합니다
                        theme = ThemeSettings(),
                        sync = SyncSettings(),
                        notification = NotificationSettings(),
                        language = "en",
                        lastUpdated = System.currentTimeMillis()
                    ),
                    user = null,  // null or provide mock user
                    onSettingsChanged = { },
                    onProfileEdit = {
                        navController.navigate(Screens.Main.Settings.Profile.route)
                    },
                    onProfileImageEdit = { },
                    onExportData = { },
                    onImportData = { },
                    onResetSettings = { }
                )
            }
        }
    }
}




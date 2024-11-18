package com.example.likebox.presentation.view.screens

sealed class Screens(val route: String) {
    // Root navigation
    sealed class Root(route: String): Screens(route) {
        data object Auth : Root("auth_graph")
        data object Main : Root("main_graph")
    }

    // Auth Flow
    sealed class Auth(route: String) : Screens(route) {
        data object SignIn : Auth("auth/signin")
        data object SignUp : Auth("auth/signup")
        data object OnBoarding : Auth("auth/onboarding")

        sealed class PlatformSetup(route: String) : Auth(route) {
            data object Selection : PlatformSetup("auth/platform-setup/selection")
            data object Connection : PlatformSetup("auth/platform-setup/connection/{platformId}") {
                fun createRoute(platformId: String) = "auth/platform-setup/connection/$platformId"
            }
            data object Confirmation : PlatformSetup("auth/platform-setup/confirmation")
        }
    }

    // Main Flow
    sealed class Main(route: String) : Screens(route) {
        sealed class Home(route: String) : Main(route) {
            data object Root : Home("main/home")

            sealed class Platform(route: String) : Home(route) {
                data object Spotify : Platform("main/home/platform/spotify")
                data object Youtube : Platform("main/home/platform/youtube")
                data object AppleMusic : Platform("main/home/platform/apple-music")
            }
        }

        sealed class Search(route: String) : Main(route) {
            data object Root : Search("main/search")

            sealed class Results(route: String) : Search(route) {
                data object Root : Results("main/search/results")

                sealed class Category(route: String) : Results(route) {
                    data class Tracks(val query: String) : Category("main/search/results/tracks/$query") {
                        companion object {
                            const val route = "main/search/results/tracks/{query}"
                        }
                    }
                    data class Artists(val query: String) : Category("main/search/results/artists/$query") {
                        companion object {
                            const val route = "main/search/results/artists/{query}"
                        }
                    }
                    data class Albums(val query: String) : Category("main/search/results/albums/$query") {
                        companion object {
                            const val route = "main/search/results/albums/{query}"
                        }
                    }
                    data class Playlists(val query: String) : Category("main/search/results/playlists/$query") {
                        companion object {
                            const val route = "main/search/results/playlists/{query}"
                        }
                    }
                }

                sealed class PlatformResults(route: String) : Results(route) {
                    data class Spotify(val query: String) : PlatformResults("main/search/results/spotify/$query") {
                        companion object {
                            const val route = "main/search/results/spotify/{query}"
                        }
                    }
                    data class Youtube(val query: String) : PlatformResults("main/search/results/youtube/$query") {
                        companion object {
                            const val route = "main/search/results/youtube/{query}"
                        }
                    }
                    data class AppleMusic(val query: String) : PlatformResults("main/search/results/apple-music/$query") {
                        companion object {
                            const val route = "main/search/results/apple-music/{query}"
                        }
                    }
                }
            }
        }

        sealed class Library(route: String) : Main(route) {
            data object Root : Library("main/library")
            data object Playlists : Library("main/library/playlists")
            data object Albums : Library("main/library/albums")
            data object Artists : Library("main/library/artists")

            sealed class Details(route: String) : Library(route) {
                data class PlaylistDetail(val playlistId: String) : Details("main/library/playlist/$playlistId") {
                    companion object {
                        const val route = "main/library/playlist/{playlistId}"
                    }
                }
                data class ArtistDetail(val artistId: String) : Details("main/library/artist/$artistId") {
                    companion object {
                        const val route = "main/library/artist/{artistId}"
                    }
                }
                data class AlbumDetail(val albumId: String) : Details("main/library/album/$albumId") {
                    companion object {
                        const val route = "main/library/album/{albumId}"
                    }
                }
                data class TrackDetail(val trackId: String) : Details("main/library/track/$trackId") {
                    companion object {
                        const val route = "main/library/track/{trackId}"
                    }
                }
            }

            sealed class Platform(route: String) : Library(route) {
                data object Spotify : Platform("main/library/platform/spotify")
                data object Youtube : Platform("main/library/platform/youtube")
                data object AppleMusic : Platform("main/library/platform/apple-music")
            }
        }

        sealed class Settings(route: String) : Main(route) {
            data object Root : Settings("main/settings")
            data object Profile : Settings("main/settings/profile")

            sealed class Account(route: String) : Settings(route) {
                data object Root : Account("main/settings/account")
                data object Security : Account("main/settings/account/security")
                data object Notifications : Account("main/settings/account/notifications")
                data object LinkedPlatforms : Account("main/settings/account/platforms")

                sealed class Platform(route: String) : Account(route) {
                    data object Spotify : Platform("main/settings/account/platforms/spotify")
                    data object Youtube : Platform("main/settings/account/platforms/youtube")
                    data object AppleMusic : Platform("main/settings/account/platforms/apple-music")
                }
            }
        }
    }
}
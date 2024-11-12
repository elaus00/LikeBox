package com.example.likebox.presentation.view.screens

sealed class Screens(val route: String) {
    sealed class Auth(route: String) : Screens(route) {
        data object Root : Auth("auth")
        data object SignIn : Auth("auth/signin")
        data object SignUp : Auth("auth/signup")
        data object OnBoarding : Auth("auth/onboarding")

        sealed class PlatformSetup(route: String) : Auth(route) {
            data object Root : PlatformSetup("auth/platform-setup")
            data object Selection : PlatformSetup("auth/platform-setup/selection")
            data object Connection : PlatformSetup("auth/platform-setup/connection/{platformId}") {
                fun createRoute(platformId: String) = "auth/platform-setup/connection/$platformId"
            }
            data object Confirmation : PlatformSetup("auth/platform-setup/confirmation")
        }
    }

    sealed class Home(route: String) : Screens(route) {
        data object Root : Home("home")

        sealed class Platform(route: String) : Home(route) {
            data object Spotify : Platform("home/platform/spotify")
            data object Youtube : Platform("home/platform/youtube")
            data object AppleMusic : Platform("home/platform/apple-music")
        }
    }

    sealed class Search(route: String) : Screens(route) {
        data object Root : Search("search")

        sealed class Results(route: String) : Search(route) {
            data object Root : Results("search/results")

            sealed class Category(route: String) : Results(route) {
                data class Tracks(val query: String) : Category("search/results/tracks/$query") {
                    companion object {
                        const val route = "search/results/tracks/{query}"
                    }
                }
                data class Artists(val query: String) : Category("search/results/artists/$query") {
                    companion object {
                        const val route = "search/results/artists/{query}"
                    }
                }
                data class Albums(val query: String) : Category("search/results/albums/$query") {
                    companion object {
                        const val route = "search/results/albums/{query}"
                    }
                }
                data class Playlists(val query: String) : Category("search/results/playlists/$query") {
                    companion object {
                        const val route = "search/results/playlists/{query}"
                    }
                }
            }

            sealed class PlatformResults(route: String) : Results(route) {
                data class Spotify(val query: String) : PlatformResults("search/results/spotify/$query") {
                    companion object {
                        const val route = "search/results/spotify/{query}"
                    }
                }
                data class Youtube(val query: String) : PlatformResults("search/results/youtube/$query") {
                    companion object {
                        const val route = "search/results/youtube/{query}"
                    }
                }
                data class AppleMusic(val query: String) : PlatformResults("search/results/apple-music/$query") {
                    companion object {
                        const val route = "search/results/apple-music/{query}"
                    }
                }
            }
        }
    }

    sealed class Library(route: String) : Screens(route) {
        data object Root : Library("library")
        data object Playlists : Library("library/playlists")
        data object Albums : Library("library/albums")
        data object Artists : Library("library/artists")

        sealed class Details(route: String) : Library(route) {
            data class PlaylistDetail(val playlistId: String) : Details("library/playlist/$playlistId") {
                companion object {
                    const val route = "library/playlist/{playlistId}"
                }
            }
            data class ArtistDetail(val artistId: String) : Details("library/artist/$artistId") {
                companion object {
                    const val route = "library/artist/{artistId}"
                }
            }
            data class AlbumDetail(val albumId: String) : Details("library/album/$albumId") {
                companion object {
                    const val route = "library/album/{albumId}"
                }
            }
        }

        sealed class Platform(route: String) : Library(route) {
            data object Spotify : Platform("library/platform/spotify")
            data object Youtube : Platform("library/platform/youtube")
            data object AppleMusic : Platform("library/platform/apple-music")
        }
    }

    sealed class Settings(route: String) : Screens(route) {
        data object Root : Settings("settings")
        data object Profile : Settings("settings/profile")

        sealed class Account(route: String) : Settings(route) {
            data object Root : Account("settings/account")
            data object Security : Account("settings/account/security")
            data object Notifications : Account("settings/account/notifications")
            data object LinkedPlatforms : Account("settings/account/platforms")

            sealed class Platform(route: String) : Account(route) {
                data object Spotify : Platform("settings/account/platforms/spotify")
                data object Youtube : Platform("settings/account/platforms/youtube")
                data object AppleMusic : Platform("settings/account/platforms/apple-music")
            }
        }
    }
}
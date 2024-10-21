package com.example.likebox.data.SpotifyRepository

import com.example.likebox.domain.*
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Header

class SpotifyRepository(private val api: SpotifyApi) : MusicPlatformRepository {
    override suspend fun getLikedSongs(): List<Song> {
        return api.getLikedTracks().items.map { item ->
                Song(
                        id = item.track.id,
                        title = item.track.name,
                        artist = item.track.artists.firstOrNull()?.name ?: "Unknown Artist"
            )
        }
    }
}

interface SpotifyApi {
    @GET("me/tracks")
    suspend fun getLikedTracks(@Header("Authorization") token: String): SpotifyLikedTracksResponse
    abstract fun getLikedTracks(): SpotifyLikedTracksResponse
}

data class SpotifyLikedTracksResponse(
        val items: List<SpotifyTrackItem>
)

data class SpotifyTrackItem(
        val track: SpotifyTrack
)

data class SpotifyTrack(
        val id: String,
        val name: String,
        val artists: List<SpotifyArtist>
)

data class SpotifyArtist(
        val name: String
)

// Retrofit 인스턴스 생성
val retrofit = Retrofit.Builder()
    .baseUrl("https://api.spotify.com/v1/")
    .build()

val spotifyApi = retrofit.create(SpotifyApi::class.java)
val spotifyRepository = SpotifyRepository(spotifyApi)
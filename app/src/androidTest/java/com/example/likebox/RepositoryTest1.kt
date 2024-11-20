package com.example.likebox

import com.example.likebox.data.model.dto.AlbumDto
import com.example.likebox.data.model.dto.ArtistDto
import com.example.likebox.data.model.dto.PlaylistDto
import com.example.likebox.data.model.dto.TrackDto
import com.google.firebase.functions.FirebaseFunctions
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Before
import org.junit.After
import org.junit.Test
import com.example.likebox.data.repository.MusicRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class RepositoryTest1 {
    private lateinit var repo: MusicRepositoryImpl
    private lateinit var functions: FirebaseFunctions
    private lateinit var authCode: String
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val testEmail: String = "test@example.com"
    private val testPassword: String = "testPassword123!"

    private suspend fun createTestUser() {
        try {
            auth.createUserWithEmailAndPassword(testEmail, testPassword).await()
            println("✅ 테스트 유저 생성 성공")
        } catch (e: Exception) {
            println("ℹ️ 테스트 유저 생성 실패 (이미 존재할 수 있음): ${e.message}")
        }
    }

    private suspend fun signIn(): Boolean {
        return try {
            val result = auth.signInWithEmailAndPassword(testEmail, testPassword).await()
            println("✅ 로그인 성공: ${result.user?.email}")
            true
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            println("❌ 로그인 실패 - 잘못된 인증 정보: ${e.message}")
            false
        } catch (e: FirebaseAuthInvalidUserException) {
            println("❌ 로그인 실패 - 유저 없음: ${e.message}")
            false
        } catch (e: Exception) {
            println("❌ 로그인 실패 - 기타 오류: ${e.message}")
            false
        }
    }

    @Before
    fun setup() {
        runBlocking {
            try {
                // 이전 세션 정리
                auth.signOut()
                println("🔓 초기 로그아웃 완료")

                // 테스트 유저 생성 및 로그인
                // createTestUser()
                signIn()

                // Firebase Functions 초기화
                functions = FirebaseFunctions.getInstance("asia-northeast3")
                repo = MusicRepositoryImpl(functions)

                // 초기 상태로 리셋
                auth.signOut()
                println("🔄 셋업 완료")
            } catch (e: Exception) {
                println("❌ 셋업 중 오류 발생: ${e.message}")
                throw e
            }
        }
    }

    @After
    fun tearDown() {
        runBlocking {
            auth.signOut()
            println("🔓 테스트 종료 후 로그아웃")
        }
    }

    @Test
    fun repo1() {
        runBlocking {
            try {
                // 로그인
                assertTrue("로그인 실패", signIn())

                val response = functions
                    .getHttpsCallable("createDefault")
                    .call()
                    .await()

                val result = response.getData() as? Map<String, Any>
                println("📦 getDummies 결과: ${result?.get("message")}")

            } catch (e: Exception) {
                println("❌ 테스트 중 오류 발생: ${e.message}")
                throw e
            }
        }
    }

    @Test
    fun generateUrl() {
        runBlocking {
            try {
                // 1. 로그인
                assertTrue("로그인 실패", signIn())

                val data = mapOf(
                    "platform" to "SPOTIFY"
                )
                // 2. 인증 URL 생성
                val generateUrlResponse = functions
                    .getHttpsCallable("generateUrl")
                    .call(data)
                    .await()

                val urlResult = generateUrlResponse.getData() as? Map<String, Any>
                val authUrl = urlResult?.get("url") as? String

                println("🔗 생성된 인증 URL: $authUrl")

                // 3. 여기서 실제로는 authUrl을 통해 브라우저에서 인증을 진행하고
                // 리다이렉트된 URL에서 authCode를 추출해야 합니다.
                // 테스트 환경에서는 이 과정을 시뮬레이션하기 위해
                // 콘솔에 URL을 출력하고 사용자가 수동으로 진행할 수 있도록 합니다.
                println("⚠️ 테스트 진행을 위해:")
                println("1. 위 URL을 브라우저에서 열어주세요")
                println("2. 스포티파이 로그인을 진행해주세요")
                println("3. 리다이렉트된 URL에서 'code' 파라미터 값을 복사해주세요")


            } catch (e: Exception) {
                println("❌ 테스트 중 오류 발생: ${e.message}")
                throw e
            }
        }
    }

    @Test
    fun generateTokenTest() {
        runBlocking {
            try {
                // 로그인
                assertTrue("로그인 실패", signIn())

                authCode = "AQDBWsDPGZVxRPUHR9PinAH4YG6YMPDAGlwTL_PJYjI9FFmlYFSUh6cXxHCEbxIzSPdo1NSBQ2a9t_kTbwSR8gRS7cHl8Jdu_QvgAU02btMZaB8A6B-Fk8r-Bypx7qlG0f2llgmDTWSktzOyc3-LWWNqiG6vON6viOSlNDoVruG7WWzkKS21Vjis_Lv242UU4JNIVadAVDk8VgEGJl6sPi2IhmvvWu_mmUsaoS1WN4MoIut-f5lnolldTilaiNZVLutSMNlKj_YRrxskR7TBDD1NhckEkeoLtF1wmOqVBo4nS1Pvz5risix90KcNVs5o7ZvShxoqR-YG0qi5rocNNai3vKcU6xyQb-gCK3HPArKkpjg_WhGkQRnxUnZEgueqxgYvo9JrONBESVG-6g"
                val data = mapOf(
                    "authCode" to authCode,
                    "platform" to "SPOTIFY"
                )

                val response = functions
                    .getHttpsCallable("generateToken")
                    .call(data)
                    .await()

                val result = response.getData() as? Map<String, Any>
                println("🎵 토큰 생성 결과: ${result?.get("message")}")

            } catch (e: Exception) {
                println("❌ 테스트 중 오류 발생: ${e.message}")
                throw e
            }
        }
    }

    @Test
    fun synchContent(){
        runBlocking {
            try {
                // 로그인
                assertTrue("로그인 실패", signIn())

                var data = mapOf(
                    "platform" to "SPOTIFY",
                    "contentType" to "TRACK"
                )

                var response = functions
                    .getHttpsCallable("synchContent")
                    .call(data)
                    .await()

                var result = response.getData() as? Map<String, Any> ?: run {
                    throw Exception()
                }

                var success = result["success"] as? Boolean ?: false

                println("$success")

                data = mapOf(
                    "platform" to "SPOTIFY",
                    "contentType" to "PLAYLIST"
                )

                response = functions
                    .getHttpsCallable("synchContent")
                    .call(data)
                    .await()

                result = response.getData() as? Map<String, Any> ?: run {
                    throw Exception()
                }

                success = result["success"] as? Boolean ?: false

                println("$success")

                data = mapOf(
                    "platform" to "SPOTIFY",
                    "contentType" to "ALBUM"
                )

                response = functions
                    .getHttpsCallable("synchContent")
                    .call(data)
                    .await()

                result = response.getData() as? Map<String, Any> ?: run {
                    throw Exception()
                }

                success = result["success"] as? Boolean ?: false

                println("$success")

                data = mapOf(
                    "platform" to "SPOTIFY",
                    "contentType" to "ARTIST"
                )

                response = functions
                    .getHttpsCallable("synchContent")
                    .call(data)
                    .await()

                result = response.getData() as? Map<String, Any> ?: run {
                    throw Exception()
                }

                success = result["success"] as? Boolean ?: false

                println("$success")

            } catch (e: Exception) {
                println("❌ 테스트 중 오류 발생: ${e.message}")
                throw e
            }
        }
    }

    @Test
    fun getPlatformContent() {
        runBlocking {
            try{
                assertTrue("로그인 실패", signIn())

                val data = mapOf(
                    "platforms" to listOf("SPOTIFY")
                )

                val timestamp = System.currentTimeMillis()

                // Playlists
                val playlistResponse = functions
                    .getHttpsCallable("getPlatformsPlaylists")
                    .call(data)
                    .await()

                val playlistResult = playlistResponse.getData() as? Map<String, Any>
                if (playlistResult?.get("success") == false) println("Failed to get playlists")

                val playlists = (playlistResult?.get("data") as? List<Map<String, Any>>)?.map { item ->
                    PlaylistDto.fromMap(item).toDomain(
                        createdAt = timestamp,
                        updatedAt = timestamp
                    )
                } ?: emptyList()

                // Albums
                val albumResponse = functions
                    .getHttpsCallable("getPlatformsAlbums")
                    .call(data)
                    .await()

                val albumResult = albumResponse.getData() as? Map<String, Any>
                if (albumResult?.get("success") == false) println("Failed to get albums")

                val albums = (albumResult?.get("data") as? List<Map<String, Any>>)?.map { item ->
                    AlbumDto.fromMap(item).toDomain(
                        createdAt = timestamp,
                        updatedAt = timestamp
                    )
                } ?: emptyList()

                // Artists
                val artistResponse = functions
                    .getHttpsCallable("getPlatformsArtists")
                    .call(data)
                    .await()

                val artistResult = artistResponse.getData() as? Map<String, Any>
                if (artistResult?.get("success") == false) println("Failed to get artists")

                val artists = (artistResult?.get("data") as? List<Map<String, Any>>)?.map { item ->
                    ArtistDto.fromMap(item).toDomain(
                        createdAt = timestamp,
                        updatedAt = timestamp
                    )
                } ?: emptyList()

                // Tracks
                val trackResponse = functions
                    .getHttpsCallable("getPlatformsTracks")
                    .call(data)
                    .await()

                val trackResult = trackResponse.getData() as? Map<String, Any>
                if (trackResult?.get("success") == false) println("Failed to get tracks")

                val tracks = (trackResult?.get("data") as? List<Map<String, Any>>)?.map { item ->
                    TrackDto.fromMap(item).toDomain(
                        createdAt = timestamp,
                        updatedAt = timestamp
                    )
                } ?: emptyList()

                // Print all content
                println("\n=== Playlists ===")
                playlists.forEachIndexed { index, playlist ->
                    println("\nPlaylist #${index + 1}")
                    println("id: ${playlist.id}")
                    println("platformId: ${playlist.platformId}")
                    println("platform: ${playlist.platform}")
                    println("name: ${playlist.name}")
                    println("thumbnailUrl: ${playlist.thumbnailUrl}")
                    println("description: ${playlist.description}")
                    println("trackCount: ${playlist.trackCount}")
                    println("owner: ${playlist.owner}")
                    println("createdAt: ${playlist.createdAt}")
                    println("updatedAt: ${playlist.updatedAt}")
                    println("tracks: ${playlist.tracks.size} tracks")
                }

                println("\n=== Albums ===")
                albums.forEachIndexed { index, album ->
                    println("\nAlbum #${index + 1}")
                    println("id: ${album.id}")
                    println("platformId: ${album.platformId}")
                    println("platform: ${album.platform}")
                    println("name: ${album.name}")
                    println("thumbnailUrl: ${album.thumbnailUrl}")
                    println("artists: ${album.artists}")
                    println("releaseDate: ${album.releaseDate}")
                    println("trackCount: ${album.trackCount}")
                    println("createdAt: ${album.createdAt}")
                    println("updatedAt: ${album.updatedAt}")
                    println("tracks: ${album.tracks.size} tracks")
                }

                println("\n=== Artists ===")
                artists.forEachIndexed { index, artist ->
                    println("\nArtist #${index + 1}")
                    println("id: ${artist.id}")
                    println("platformId: ${artist.platformId}")
                    println("platform: ${artist.platform}")
                    println("name: ${artist.name}")
                    println("thumbnailUrl: ${artist.thumbnailUrl}")
                    println("genres: ${artist.genres}")
                    println("followerCount: ${artist.followerCount}")
                    println("externalUrl: ${artist.externalUrl}")
                    println("popularity: ${artist.popularity}")
                    println("createdAt: ${artist.createdAt}")
                    println("updatedAt: ${artist.updatedAt}")
                }

                println("\n=== Tracks ===")
                tracks.forEachIndexed { index, track ->
                    println("\nTrack #${index + 1}")
                    println("id: ${track.id}")
                    println("platformId: ${track.platformId}")
                    println("platform: ${track.platform}")
                    println("name: ${track.name}")
                    println("thumbnailUrl: ${track.thumbnailUrl}")
                    println("artists: ${track.artists}")
                    println("album: ${track.album}")
                    println("durationMs: ${track.durationMs}")
                    println("createdAt: ${track.createdAt}")
                    println("updatedAt: ${track.updatedAt}")
                }

            } catch (e: Exception){
                println("❌ 에러 발생: ${e.message}")
                throw e
            }
        }
    }
}
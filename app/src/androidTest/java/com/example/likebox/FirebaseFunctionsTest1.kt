package com.example.likebox

import com.google.firebase.functions.FirebaseFunctions
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Before
import org.junit.Test
import com.example.likebox.data.model.dto.AlbumDto
import com.example.likebox.data.model.dto.PlaylistDto
import com.example.likebox.data.model.dto.TrackDto

import com.example.likebox.domain.model.library.Track
import com.example.likebox.domain.model.library.Playlist
import com.example.likebox.domain.model.library.Album
import com.example.likebox.domain.model.library.ContentType
import com.example.likebox.domain.model.library.MusicPlatform
import com.google.gson.Gson

class FirebaseFunctionTest1 {
    private lateinit var functions: FirebaseFunctions

    @Before
    fun setup() {
        functions = FirebaseFunctions.getInstance("asia-northeast3")
    }

    @Test
    fun testFunction1Call() {
        runBlocking {
            try {
                val testData = mapOf(
                    "testParam" to "Hello from Android Test",
                    "timestamp" to System.currentTimeMillis()
                )

                println("📱 테스트 시작: $testData")

                val response = functions
                    .getHttpsCallable("testFunction1")
                    .call(testData)
                    .await()

                val result = response.getData() as? Map<String, Any>
                println("📱 서버 응답: $result")

                // success 체크
                assertTrue("API 호출이 실패했습니다", result?.get("success") as Boolean)

            } catch (e: Exception) {
                println("❌ 에러 발생: ${e.message}")
                throw e  // 테스트 실패 처리
            }
        }
    }

    @Test
    fun testFunction2Call() {
        runBlocking {
            try{
                val response = functions
                    .getHttpsCallable("saveDummies")
                    .call()
                    .await()

                val result = response.getData() as? Map<String, Any>
                println("📱 서버 응답: $result")

            } catch (e: Exception){
                println("❌ 에러 발생: ${e.message}")
                throw e
            }
        }
    }

    @Test
    fun testFunction3Call() {
        runBlocking {
            try{
                val response = functions
                    .getHttpsCallable("getDummies")
                    .call()
                    .await()

                val result = response.getData() as? Map<String, Any> ?: emptyMap()
                println("📱 서버 응답: $result")

                val datas = result["data"] as Map<String, Any>

                val tracksdata = datas["tracks"] as List<Map<String, Any>>
                val playlistsdata = datas["playlists"] as List<Map<String, Any>>
                val albumsdata = datas["albums"] as List<Map<String, Any>>


                val tracks = tracksdata.map { item ->
                    TrackDto.fromMap(item)
                        .toDomain(
                            createdAt = System.currentTimeMillis(),
                            updatedAt = System.currentTimeMillis()
                        )
                }

                val playlists = playlistsdata.map { item ->
                    PlaylistDto.fromMap(item)
                        .toDomain(
                            createdAt = System.currentTimeMillis(),
                            updatedAt = System.currentTimeMillis()
                        )
                }

                val albums = albumsdata.map { item ->
                    AlbumDto.fromMap(item)
                        .toDomain(
                            createdAt = System.currentTimeMillis(),
                            updatedAt = System.currentTimeMillis()
                        )
                }

//                id = domain.id,
//                pid = domain.platformId,
//                platform = domain.platform.name,
//                name = domain.name,
//                thumbnailUrl = domain.thumbnailUrl,
//                artists = domain.artists,
//                albumName = domain.album,
//                durationMs

                tracks.forEach{ track ->
                    println(track.id)
                    println(track.platformId)
                    println(track.platform.name)
                    println(track.name)
                    println(track.thumbnailUrl)
                    println(track.artists)
                    println(track.album)
                    println(track.durationMs)
                }

//                id = id,
//                platformId = pid,
//                platform = MusicPlatform.fromId(platform),
//                name = name,
//                thumbnailUrl = thumbnailUrl,
//                updatedAt = updatedAt,
//                createdAt = createdAt,
//                description = description,
//                trackCount = trackCount,
//                owner = owner,
//                tracks = tracks

                playlists.forEach { pl ->
                    println(pl.id)
                    println(pl.platformId)
                    println(pl.platform.name)
                    println(pl.name)
                    println(pl.thumbnailUrl)
                    println(pl.updatedAt)
                    println(pl.createdAt)
                    println(pl.description)
                    println(pl.trackCount)
                    println(pl.owner)
                    pl.tracks.forEach { track ->
                        println(track.id)
                        println(track.platformId)
                        println(track.platform.name)
                        println(track.name)
                        println(track.thumbnailUrl)
                        println(track.artists)
                        println(track.album)
                        println(track.durationMs)
                    }
                }


//                id = id,
//                platformId = pid,
//                platform = MusicPlatform.fromId(platform),
//                name = name,
//                thumbnailUrl = thumbnailUrl,
//                updatedAt = updatedAt,
//                createdAt = createdAt,
//                artists = artists,
//                releaseDate = releasedDate,
//                trackCount = trackCount,
//                tracks = tracks

                albums.forEach{ a ->
                    println(a.id)
                    println(a.platformId)
                    println(a.platform.name)
                    println(a.name)
                    println(a.thumbnailUrl)
                    println(a.updatedAt)
                    println(a.createdAt)
                    println(a.artists)
                    println(a.releaseDate)
                    println(a.trackCount)
                    a.tracks.forEach { track ->
                        println(track.id)
                        println(track.platformId)
                        println(track.platform.name)
                        println(track.name)
                        println(track.thumbnailUrl)
                        println(track.artists)
                        println(track.album)
                        println(track.durationMs)
                    }
                }


            } catch (e: Exception){
                println("❌ 에러 발생: ${e.message}")
                throw e
            }
        }
    }

    @Test
    fun testFunction4Call() {
        runBlocking {
            try{

            } catch (e: Exception){
                println("❌ 에러 발생: ${e.message}")
                throw e
            }
        }
    }
}
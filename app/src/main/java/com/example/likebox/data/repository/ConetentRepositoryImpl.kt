package com.example.likebox.data.repository

import com.example.likebox.data.model.dto.AlbumDto
import com.example.likebox.data.model.dto.PlaylistDto
import com.example.likebox.data.model.dto.TrackDto
import com.example.likebox.domain.model.library.MusicContent
import com.example.likebox.domain.model.library.MusicPlatform
import com.example.likebox.domain.repository.ContentRepository
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ContentRepositoryImpl @Inject constructor(
    private val functions: FirebaseFunctions
) : ContentRepository {

    override suspend fun getRecentContents(
        platform: MusicPlatform,
        limit: Int
    ): Result<List<MusicContent>> {
        return try {
            val data = mapOf(
                "platform" to platform.name,
                "limit" to limit
            )

            val response = functions
                .getHttpsCallable("getRecentContents")
                .call(data)
                .await()

            val result = response.getData() as? Map<String, Any> ?: run {
                return Result.failure(Exception("Invalid response format"))
            }

            if (result["success"] == false) {
                return Result.failure(Exception("Failed to fetch recent contents"))
            }

            val contentList = result["data"] as? List<Map<String, Any>> ?: emptyList()

            val contents = contentList.mapNotNull { item ->
                when (item["type"] as? String) {
                    "TRACK" -> TrackDto.fromMap(item).toDomain(
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    "ALBUM" -> AlbumDto.fromMap(item).toDomain(
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    "PLAYLIST" -> PlaylistDto.fromMap(item).toDomain(
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    else -> null
                }
            }

            Result.success(contents)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getContent(contentId: String): Result<MusicContent> {
        return try {
            val data = mapOf(
                "contentId" to contentId
            )

            val response = functions
                .getHttpsCallable("getContent")
                .call(data)
                .await()

            val result = response.getData() as? Map<String, Any> ?: run {
                return Result.failure(Exception("Invalid response format"))
            }

            if (result["success"] == false) {
                return Result.failure(Exception("Failed to fetch content"))
            }

            val item = result["data"] as? Map<String, Any> ?: run {
                return Result.failure(Exception("Invalid content format"))
            }

            val content = when (item["type"] as? String) {
                "TRACK" -> TrackDto.fromMap(item).toDomain(
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
                "ALBUM" -> AlbumDto.fromMap(item).toDomain(
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
                "PLAYLIST" -> PlaylistDto.fromMap(item).toDomain(
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
                else -> throw Exception("Unknown content type")
            }

            Result.success(content)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchContents(
        query: String,
        platform: MusicPlatform?
    ): Result<List<MusicContent>> {
        return try {
            val data = mutableMapOf(
                "query" to query
            ).apply {
                platform?.let { this["platform"] = it.name }
            }

            val response = functions
                .getHttpsCallable("searchContents")
                .call(data)
                .await()

            val result = response.getData() as? Map<String, Any> ?: run {
                return Result.failure(Exception("Invalid response format"))
            }

            if (result["success"] == false) {
                return Result.failure(Exception("Failed to search contents"))
            }

            val contentList = result["data"] as? List<Map<String, Any>> ?: emptyList()

            val contents = contentList.mapNotNull { item ->
                when (item["type"] as? String) {
                    "TRACK" -> TrackDto.fromMap(item).toDomain(
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    "ALBUM" -> AlbumDto.fromMap(item).toDomain(
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    "PLAYLIST" -> PlaylistDto.fromMap(item).toDomain(
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    else -> null
                }
            }

            Result.success(contents)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
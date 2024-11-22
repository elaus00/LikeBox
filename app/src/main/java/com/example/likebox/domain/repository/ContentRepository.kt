package com.example.likebox.domain.repository

import com.example.likebox.domain.model.library.MusicContent
import com.example.likebox.domain.model.library.MusicPlatform

interface ContentRepository {
    suspend fun getRecentContents(
        platform: MusicPlatform,
        limit: Int = 10
    ): Result<List<MusicContent>>

    suspend fun getContent(contentId: String): Result<MusicContent>

    suspend fun searchContents(
        query: String,
        platform: MusicPlatform? = null
    ): Result<List<MusicContent>>
}
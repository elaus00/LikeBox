package com.example.likebox.data.model.dto


interface MusicContentDto<T> {
    val id: String
    val pid: String
    val platform: String
    val name: String
    val thumbnailUrl: String

    fun toDomain(
        createdAt: Long = System.currentTimeMillis(),
        updatedAt: Long = System.currentTimeMillis()
    ): T
}
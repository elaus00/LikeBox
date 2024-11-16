package com.example.likebox.data.model.dto

import com.example.likebox.domain.model.library.Track


// 1. Firebase에서 받는 DTO
data class TrackDto(
    var id: String = "",
    val name: String = "",
    val artists: List<String> = emptyList(),
    val addedAt: Long = 0L
)

// 2. 매핑을 담당하는 Mapper 클래스
object TrackMapper {
    fun TrackDto.toDomain(): Track {
        TODO()
    }

    fun Track.toDto(): TrackDto {
        TODO()
    }
}



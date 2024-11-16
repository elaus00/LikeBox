package com.example.likebox.domain.model

/**
 * 아티스트 정보를 나타내는 도메인 모델
 *
 * @property id 아티스트의 고유 식별자
 * @property platformId 플랫폼에서의 아티스트 ID
 * @property platform 음악 플랫폼 (Spotify, Apple Music 등)
 * @property name 아티스트 이름
 * @property thumbnailUrl 아티스트 대표 이미지 URL
 * @property genres 아티스트의 장르 목록
 * @property followerCount 팔로워 수
 * @property externalUrl 플랫폼에서의 아티스트 페이지 URL
 * @property popularity 인기도 (0-100)
 */
data class Artist(
    override val id: String,
    override val platformId: String,
    override val platform: MusicPlatform,
    override val name: String,
    override val thumbnailUrl: String,
    override val createdAt: Long,
    override val updatedAt: Long,
    val genres: List<String>,
    val followerCount: Int,
    val externalUrl: String,
    val popularity: Int
) : MusicContent
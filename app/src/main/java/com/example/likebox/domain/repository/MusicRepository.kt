package com.example.likebox.domain.repository

import com.example.likebox.domain.model.library.Album
import com.example.likebox.domain.model.library.Artist
import com.example.likebox.domain.model.library.ContentType
import com.example.likebox.domain.model.library.MusicContent
import com.example.likebox.domain.model.library.MusicPlatform
import com.example.likebox.domain.model.library.Playlist
import com.example.likebox.domain.model.library.Track

/**
 * 음악 콘텐츠 관리를 담당하는 Repository interface
 * 음악 콘텐츠의 CRUD 및 검색 기능을 정의
 */
interface MusicRepository {
    /**
     * 플랫폼별 좋아요한 콘텐츠 조회
     * @param platform 대상 플랫폼
     * @param contentType 콘텐츠 타입
     * @return 좋아요한 콘텐츠 목록
     */
    suspend fun getLikedContent(
        platform: MusicPlatform,
        contentType: ContentType
    ): Result<List<MusicContent>>

    /**
     * 콘텐츠 좋아요 추가
     * @param content 좋아요 추가할 콘텐츠
     */
    suspend fun addToLiked(content: MusicContent): Result<Unit>

    /**
     * 콘텐츠 좋아요 제거
     * @param content 좋아요 제거할 콘텐츠
     */
    suspend fun removeFromLiked(content: MusicContent): Result<Unit>

    /**
     * 콘텐츠 동기화
     * @param platform 대상 플랫폼
     * @param contentType 동기화할 콘텐츠 타입
     */
    suspend fun syncContent(
        platform: MusicPlatform,
        contentType: ContentType
    ): Result<Unit>

    /**
     * 트랙 목록 조회
     * @param platforms 대상 플랫폼 목록
     * @return 트랙 목록
     */
    suspend fun getTracks(platforms: Set<MusicPlatform>): Result<List<Track>>

    /**
     * 앨범 목록 조회
     * @param platforms 대상 플랫폼 목록
     * @return 앨범 목록
     */
    suspend fun getAlbums(platforms: Set<MusicPlatform>): Result<List<Album>>

    /**
     * 플레이리스트 목록 조회
     * @param platforms 대상 플랫폼 목록
     * @return 플레이리스트 목록
     */
    suspend fun getPlaylists(platforms: Set<MusicPlatform>): Result<List<Playlist>>

    /**
     * 앨범 상세 정보 조회
     * @param albumId 앨범 ID
     * @return 앨범 정보
     */
    suspend fun getAlbumById(albumId: String): Result<Album>

    /**
     * 플레이리스트 상세 정보 조회
     * @param playlistId 플레이리스트 ID
     * @return 플레이리스트 정보
     */
    suspend fun getPlaylistById(playlistId: String): Result<Playlist>

    /**
     * 아티스트 상세 정보 조회
     * @param artistId 아티스트 ID
     * @return 아티스트 정보
     */
    suspend fun getArtistById(artistId: String): Result<Artist>

    /**
     * 아티스트의 트랙 목록 조회
     * @param artistId 아티스트 ID
     * @return 트랙 목록
     */
    suspend fun getArtistTracks(artistId: String): Result<List<Track>>

    /**
     * 아티스트의 앨범 목록 조회
     * @param artistId 아티스트 ID
     * @return 앨범 목록
     */
    suspend fun getArtistAlbums(artistId: String): Result<List<Album>>

    /**
     * 콘텐츠 타입별 개수 조회
     * @param platform 대상 플랫폼
     * @param contentType 콘텐츠 타입
     * @return 콘텐츠 개수
     */
    suspend fun getContentCount(
        platform: MusicPlatform,
        contentType: ContentType
    ): Result<Int>

    /**
     * 로컬 캐시 데이터 삭제
     */
    suspend fun clearLocalCache(): Result<Unit>

    /**
     * 콘텐츠 검색
     * @param query 검색어
     * @param contentType 검색할 콘텐츠 타입
     * @param platforms 검색할 플랫폼 목록
     * @return 검색된 콘텐츠 목록
     */
    suspend fun searchContent(
        query: String,
        contentType: ContentType,
        platforms: Set<MusicPlatform>
    ): Result<List<MusicContent>>

    // Todo : 임시로 Any 반환 타입으로 설정. 수정해야 함
    suspend fun getPlaylist(playlistId: String): Any
}
package com.example.likebox.domain.repository

import com.example.likebox.domain.model.MusicPlatform
import com.example.likebox.domain.model.PlatformAuth
import java.time.LocalDateTime

/**
 * 음악 플랫폼 연결 및 인증 관리를 담당하는 Repository interface
 */
interface PlatformRepository {
    /**
     * 플랫폼 연결 상태 확인
     * @return 플랫폼별 연결 상태 Map
     */
    suspend fun getConnectedPlatforms(): Result<List<MusicPlatform>>

    /**
     * 특정 플랫폼의 연결 상태 확인
     * @param platform 확인할 플랫폼
     * @return 연결 여부
     */
    suspend fun isPlatformConnected(platform: MusicPlatform): Result<Boolean>

    /**
     * 플랫폼 인증 정보 저장
     * @param platformAuth 저장할 플랫폼 인증 정보
     */
    suspend fun savePlatformAuth(platformAuth: PlatformAuth): Result<Unit>

    /**
     * 플랫폼 인증 정보 조회
     * @param platform 조회할 플랫폼
     * @return 플랫폼 인증 정보
     */
    suspend fun getPlatformAuth(platform: MusicPlatform): Result<PlatformAuth>

    /**
     * 플랫폼 연결 해제
     * @param platform 연결 해제할 플랫폼
     */
    suspend fun disconnectPlatform(platform: MusicPlatform): Result<Unit>

    /**
     * 플랫폼 토큰 갱신
     * @param platform 토큰을 갱신할 플랫폼
     * @return 갱신된 인증 정보
     */
    suspend fun refreshPlatformToken(platform: MusicPlatform): Result<PlatformAuth>

    /**
     * 플랫폼별 마지막 동기화 시간 저장
     * @param platform 대상 플랫폼
     * @param timestamp 동기화 시간
     */
    suspend fun updateLastSyncTime(platform: MusicPlatform, timestamp: Long): Result<Unit>

    /**
     * 플랫폼별 마지막 동기화 시간 조회
     * @param platform 대상 플랫폼
     * @return 마지막 동기화 시간
     */
    suspend fun getLastSyncTime(platform: MusicPlatform): Result<Long>

    /**
     * 모든 플랫폼 연결 해제
     */
    suspend fun disconnectAllPlatforms(): Result<Unit>

    /**
     * 플랫폼 연결 시도
     * @param platform 연결할 플랫폼
     * @param authCode 인증 코드
     */
    suspend fun connectPlatform(platform: MusicPlatform, authCode: String): Result<PlatformAuth>


    /**
     * 플랫폼 에러 로그 저장
     * @param platform 대상 플랫폼
     * @param error 에러 정보
     */
    suspend fun logPlatformError(
        platform: MusicPlatform,
        error: String
    ): Result<Unit>
}
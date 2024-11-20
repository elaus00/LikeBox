package com.example.likebox.domain.repository

import com.example.likebox.domain.model.library.MusicPlatform
import com.example.likebox.domain.model.library.PlatformAuth
import com.example.likebox.presentation.state.auth.SyncStatus
import kotlinx.coroutines.flow.Flow

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

    /**
     * 모든 플랫폼 동기화 실행
     * @return 동기화 결과
     */
    suspend fun syncAllPlatforms(): Result<Unit>

    /**
     * 특정 플랫폼 동기화 실행
     * @param platform 동기화할 플랫폼
     * @return 동기화 결과
     */
    suspend fun syncPlatform(platform: MusicPlatform): Result<Unit>

    /**
     * 플랫폼의 동기화 상태를 관찰
     * @param platform 관찰할 플랫폼
     * @return 동기화 상태 Flow
     */
    fun observeSyncStatus(platform: MusicPlatform): Flow<SyncStatus>

    /**
     * 모든 플랫폼의 마지막 동기화 시간 중 가장 최근 시간 조회
     * @return 가장 최근 동기화 시간
     */
    suspend fun getLastSyncTime(): Result<Long>

    /**
     * 현재 진행 중인 동기화 작업 취소
     */
    suspend fun cancelSync(): Result<Unit>

    /**
     * 플랫폼 동기화 상태 업데이트
     * @param platform 대상 플랫폼
     * @param status 새로운 동기화 상태
     */
    suspend fun updateSyncStatus(
        platform: MusicPlatform,
        status: SyncStatus
    ): Result<Unit>

    /**
     * 플랫폼별 동기화 상태 조회
     * @return 플랫폼별 동기화 상태 Map
     */
    suspend fun getPlatformSyncStatuses(): Result<Map<MusicPlatform, SyncStatus>>
}

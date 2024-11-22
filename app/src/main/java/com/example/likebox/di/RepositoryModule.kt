package com.example.likebox.di

import com.example.likebox.data.repository.*
import com.example.likebox.data.repository.mock.MockMusicRepositoryImpl
import com.example.likebox.data.repository.mock.MockPlatformRepositoryImpl
import com.example.likebox.data.repository.mock.MockSettingsRepositoryImpl
import com.example.likebox.domain.repository.*
import com.google.firebase.storage.FirebaseStorage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindPlatformRepository(
        platformRepositoryImpl: PlatformRepositoryImpl
    ): PlatformRepository

    @Binds
    @Singleton
    abstract fun bindSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    @Singleton
    abstract fun bindMusicRepository(
        musicRepositoryImpl: MusicRepositoryImpl
    ): MusicRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository


    @Singleton
    @Binds
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindContentRepository(
        contentRepositoryImpl: ContentRepositoryImpl
    ): ContentRepository


    companion object {
        @Provides
        @Singleton
        fun provideFirebaseStorage(): FirebaseStorage {
            return FirebaseStorage.getInstance()
        }
    }

    // Test용 Mock 구현체
    @Binds
    @Singleton
    @Mock  // Mock 구현체도 같은 모듈에 추가
    abstract fun bindMockMusicRepository(
        mockMusicRepositoryImpl: MockMusicRepositoryImpl
    ): MusicRepository

    @Binds
    @Singleton
    @Mock
    abstract fun bindMockSettingsRepository(
        mockSettingsRepositoryImpl: MockSettingsRepositoryImpl
    ): SettingsRepository

    @Binds
    @Singleton
    @Mock
    abstract fun bindMockPlatformRepository(
        mockPlatformRepositoryImpl: MockPlatformRepositoryImpl
    ) : PlatformRepository
}
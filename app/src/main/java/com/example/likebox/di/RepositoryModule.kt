package com.example.likebox.di

import com.example.likebox.data.repository.AuthRepositoryImpl
import com.example.likebox.data.repository.MockSettingsRepositoryImpl
import com.example.likebox.data.repository.MusicRepositoryImpl
import com.example.likebox.data.repository.PlatformRepositoryImpl
import com.example.likebox.data.repository.SearchRepositoryImpl
import com.example.likebox.data.repository.SettingsRepositoryImpl
import com.example.likebox.domain.repository.AuthRepository
import com.example.likebox.domain.repository.MusicRepository
import com.example.likebox.domain.repository.PlatformRepository
import com.example.likebox.domain.repository.SearchRepository
import com.example.likebox.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
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

//    @Binds
//    @Singleton
//    abstract fun bindSettingsRepository(
//        settingsRepositoryImpl: SettingsRepositoryImpl
//    ): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        mockSettingsRepositoryImpl: MockSettingsRepositoryImpl
    ): SettingsRepository

}
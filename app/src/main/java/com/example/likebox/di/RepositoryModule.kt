package com.example.likebox.di

import com.example.likebox.data.repository.AuthRepositoryImpl
import com.example.likebox.data.repository.mock.MockSettingsRepositoryImpl
import com.example.likebox.data.repository.MusicRepositoryImpl
import com.example.likebox.data.repository.PlatformRepositoryImpl
import com.example.likebox.data.repository.SearchRepositoryImpl
import com.example.likebox.data.repository.UserRepositoryImpl
import com.example.likebox.domain.repository.AuthRepository
import com.example.likebox.domain.repository.MusicRepository
import com.example.likebox.domain.repository.PlatformRepository
import com.example.likebox.domain.repository.SearchRepository
import com.example.likebox.domain.repository.SettingsRepository
import com.example.likebox.domain.repository.UserRepository
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

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseStorage(): FirebaseStorage {
            return FirebaseStorage.getInstance()
        }
    }

}
package com.example.likebox.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.example.likebox.data.firebase.FirebaseService
import com.example.likebox.data.firebase.FirebaseServiceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseService(): FirebaseService {
        return FirebaseServiceImpl()
    }
}
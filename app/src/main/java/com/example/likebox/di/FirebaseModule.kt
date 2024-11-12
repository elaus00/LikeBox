package com.example.likebox.di

import com.example.likebox.data.firebase.FirebaseService
import com.example.likebox.data.firebase.FirebaseServiceImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseService(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): FirebaseService {
        return FirebaseServiceImpl(auth, firestore)
    }
}
package com.example.likebox.di

import com.example.likebox.data.firebase.FirebaseService
import com.example.likebox.data.firebase.FirebaseServiceImpl
import com.example.likebox.data.firebase.SearchFirebaseService
import com.example.likebox.data.firebase.SearchFirebaseServiceImpl
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.functions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {
    // Feat: for firebase functions
    @Provides
    @Singleton
    fun provideFirebaseFunctions(): FirebaseFunctions {
        return Firebase.functions("asia-northeast3")
    }

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

    @Provides
    @Singleton
    fun provideSearchFirebaseService(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): SearchFirebaseService {
        return SearchFirebaseServiceImpl(firestore, auth)
    }
}
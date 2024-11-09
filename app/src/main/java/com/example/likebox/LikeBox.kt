package com.example.likebox

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LikeBox : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("AppLifecycle", "1. LikeBox onCreate")
    }
}
package com.example.likebox

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory

class LikeBoxTest : Application() {
    override fun onCreate() {
        super.onCreate()
        if (FirebaseApp.getApps(this).isEmpty()) {  // 초기화되어 있지 않을 때만
            initializeTestFirebase()
        } else {
            println("🔄 Firebase 이미 초기화되어 있음")
        }
    }

    private fun initializeTestFirebase() {
        try {
            FirebaseApp.initializeApp(this)
            println("✅ Firebase 초기화 성공")

            FirebaseAppCheck.getInstance().apply {
                setTokenAutoRefreshEnabled(true)
                installAppCheckProviderFactory(
                    DebugAppCheckProviderFactory.getInstance()
                )
            }
            println("🔧 테스트용 Firebase App Check 설정 완료")
        } catch (e: Exception) {
            println("❌ Firebase 초기화 실패: ${e.message}")
        }
    }
}
package com.example.likebox.domain.model

import com.example.likebox.domain.model.settings.NotificationSettings
import com.example.likebox.domain.model.settings.SyncSettings
import com.example.likebox.domain.model.settings.ThemeSettings

data class Settings(
    val userId: String,
    val theme: ThemeSettings = ThemeSettings(),
    val sync: SyncSettings = SyncSettings(),
    val notification: NotificationSettings = NotificationSettings(),
    val language: String = "en",
    val lastUpdated: Long = System.currentTimeMillis()
)

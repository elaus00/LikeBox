package com.example.likebox.domain.model.settings

data class NotificationSettings(
    val enabled: Boolean = true,
    val syncNotifications: Boolean = true,
    val contentUpdateNotifications: Boolean = true
)
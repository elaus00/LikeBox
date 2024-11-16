package com.example.likebox.domain.model.settings

enum class SyncInterval(val hours: Int) {
    HOURLY(1),
    DAILY(24),
    WEEKLY(168),
    MONTHLY(720)
}
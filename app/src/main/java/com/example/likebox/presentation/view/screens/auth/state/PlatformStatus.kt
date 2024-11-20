package com.example.likebox.presentation.view.screens.auth.state

data class PlatformSyncStatus(
    val isConnected: Boolean,
    val syncStatus: SyncStatus
)

enum class SyncStatus {
    COMPLETED, IN_PROGRESS, ERROR, IDLE
}
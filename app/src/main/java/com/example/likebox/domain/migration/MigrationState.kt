package com.example.likebox.domain.migration

sealed class MigrationState {
    object Idle : MigrationState()
    object Loading : MigrationState()
    data class InProgress(val progress: Int) : MigrationState()
    data class Completed(val timestamp: Long?) : MigrationState()
    data class Error(val message: String) : MigrationState()
}
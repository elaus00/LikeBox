package com.example.likebox.domain.repository

import com.example.likebox.domain.model.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun getSettings(): Flow<Settings>
    suspend fun updateSettings(settings: Settings): Result<Unit>
    suspend fun exportUserData(): Result<String> // Returns JSON string of user data
    suspend fun importUserData(jsonData: String): Result<Unit>
    suspend fun resetSettings(): Result<Unit>
}
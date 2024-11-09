package com.example.likebox.data.model.dto

data class PlatformAuthDto(
    val platform: String = "",
    val access_token: String = "",
    val refresh_token: String = "",
    val expires_at: String = "",
    val user_id: String = "",
    val is_valid: Boolean = true
)
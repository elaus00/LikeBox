package com.example.likebox.domain.repository

interface AuthRepository {
    suspend fun isLoggedIn() : Boolean
}

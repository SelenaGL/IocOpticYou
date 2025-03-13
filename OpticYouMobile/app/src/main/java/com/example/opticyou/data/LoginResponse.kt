package com.example.opticyou.data

/**
 * Data class to save the server response (login success and role)
 */
data class LoginResponse(
    val success: Boolean,
    val token: String,
    val role: String
)
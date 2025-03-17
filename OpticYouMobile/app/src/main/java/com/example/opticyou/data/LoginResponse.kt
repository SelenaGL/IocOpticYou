package com.example.opticyou.data

/**
 * * Classe de dades que representa la resposta del servidor després d'un intent de login.
 */
data class LoginResponse(
    val success: Boolean,
    val token: String,
    val rol: String
)
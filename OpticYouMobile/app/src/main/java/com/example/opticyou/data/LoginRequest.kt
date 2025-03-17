package com.example.opticyou.data

/**
 * Classe de dades per enviar la petició d'inici de sessió al servidor.
 */
data class LoginRequest(
    val email: String,
    val password: String
)
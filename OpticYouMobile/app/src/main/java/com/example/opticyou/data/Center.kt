package com.example.opticyou.data

/**
 * Representa un centre amb la seva informació bàsica.
 */
data class Center (
    val idClinica: Long? = null,
    val nom: String,
    val direccio: String,
    val telefon: String,
    val horari_opertura: String,
    val horari_tancament: String,
    val email: String,
    val token: String
)
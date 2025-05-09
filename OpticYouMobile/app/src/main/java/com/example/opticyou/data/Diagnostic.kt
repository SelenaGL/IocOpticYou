package com.example.opticyou.data

/**
 * Representa un diagnòstic amb la seva informació bàsica.
 */
data class Diagnostic(
    val iddiagnostic: Long? = null,
    val descripcio: String,
    val date: String,
    val historialId: Long
)
package com.example.opticyou.data

/**
 * Representa un diagnòstic amb la seva informació bàsica.
 */
data class Diagnostic(
    val idDiagnostic: Long,
    val description: String,
    val date: String,
    val historialId: Long
)
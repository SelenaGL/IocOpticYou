package com.example.opticyou.data

import java.time.LocalDateTime

/**
 * Representa un centre amb la seva informació bàsica.
 */
data class Historial (
    val idhistorial: Long? = null,
    val dataCreacio: String,
    val patologies: String,
    val client: Client?,
)
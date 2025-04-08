package com.example.opticyou.data

import java.time.LocalDateTime

/**
 * Representa un historial amb la seva informació.
 */
data class Historial (
    val idhistorial: Long? = null,
    val dataCreacio: String,
    val patologies: String,
    val client: Client?,
)
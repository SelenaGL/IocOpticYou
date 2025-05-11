package com.example.opticyou.data

/**
 * Representa un treballador amb la seva informació bàsica.
 */
data class Treballador(
    val idUsuari: Long? = null,
    val nom: String,
    val email: String,
    val contrasenya: String?,
    val especialitat: String,
    val estat: String,
    val iniciJornada: String,
    val diesJornada: String,
    val fiJornada: String,
    val clinicaId: Long
)
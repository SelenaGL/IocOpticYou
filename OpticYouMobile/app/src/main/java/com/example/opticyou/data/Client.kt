package com.example.opticyou.data

/**
 * Representa un usuari amb la seva informació bàsica.
 */
data class Client (
    val idUsuari: Long? = null,
    val nom: String,
    val email: String,
    val contrasenya: String,
    val telefon: String,
    val sexe: String,
    val dataNaixament: String,
    val clinicaId: Long,
    val historialId: Long
)
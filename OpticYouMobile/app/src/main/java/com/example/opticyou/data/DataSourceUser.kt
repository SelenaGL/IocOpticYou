
package com.example.opticyou.data

import com.example.opticyou.Screens

/**
 * Objecte que proporciona les opcions disponibles pel rol usuari
 */
object DataSourceUser {
    val options=mapOf(
        "Historial clínic" to Screens.Clients.name,
        "Demanar cita" to Screens.List.name,
        "Veure cites" to Screens.Add.name,
        "Pautes" to "4",
        "Perfil personal" to Screens.Profile.name
        )
}


package com.example.opticyou.data

import com.example.opticyou.Screens

/**
 * Objecte que proporciona les opcions disponibles pel rol aministrador
 */
object DataSourceAdmin {
    val options=mapOf(
        "Gestió de centres" to Screens.Centres.name,
        "Gestió de clients" to Screens.Clients.name,
        "Gestió d'historials" to Screens.Historials.name,
        "Gestió de cites" to Screens.List.name,
        "Gestió de pautes" to Screens.Add.name,
        "Configuració" to "4")
}

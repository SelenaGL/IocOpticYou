package com.example.opticyou

import com.example.opticyou.communications.ServerRequests
import com.example.opticyou.communications.network.ClientServerCommunication
import com.example.opticyou.communications.network.HistorialServerCommunication
import com.example.opticyou.communications.network.RetrofitClient
import com.example.opticyou.data.Client
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HistorialCrudIntegrationTest {

    /**
     * Prova l'obtenció de la llista d'historials.
     */
    @Test
    fun testGetAllHistorial() = runBlocking {
        RetrofitClient.setBaseUrlForTesting("http://10.0.2.2:8083/")

        // Crida login per obtenir un token vàlid
        val loginResponse = ServerRequests.login("admin@optica.cat", "1234")
        assertNotNull("El login no hauria de retornar null", loginResponse)
        assertTrue("El login ha de ser exitós", loginResponse!!.success)
        println("Token obtingut: ${loginResponse.token}")
        val token = loginResponse.token

        // Obtenim la llista d'historials des del servidor
        val historials = HistorialServerCommunication.getAllHistorial(token)
        println("Historials obtinguts: $historials")
        assertNotNull("La llista d'historials no pot ser null", historials)
    }

    /**
     * Prova l'actualització d'un historial existent.
     */
    @Test
    fun testUpdateHistorial() = runBlocking {
        RetrofitClient.setBaseUrlForTesting("http://10.0.2.2:8083/")

        val loginResponse = ServerRequests.login("administrador", "1234")
        assertNotNull("El login no hauria de retornar null", loginResponse)
        assertTrue("El login ha de ser exitós", loginResponse!!.success)
        println("Token obtingut: ${loginResponse.token}")
        val token = loginResponse.token

        // Obtenim la llista d'historials
        val historials = HistorialServerCommunication.getAllHistorial(token)
        assertNotNull("La llista d'historials no pot ser null", historials)
        // Per a la prova, seleccionem un dels historials
        val historialToUpdate = historials!!.firstOrNull()
        assertNotNull("S'ha de trobar un historial per actualitzar", historialToUpdate)

        // Creem una versió actualitzada modificant, per exemple, el camp patologies
        val updatedHistorial = historialToUpdate!!.copy(patologies = "Actualitzat")
        val updateResult = HistorialServerCommunication.updateHistorial(token, updatedHistorial)
        println("Resultat de l'actualització de l'historial: $updateResult")
        assertTrue("L'actualització de l'historial ha de ser exitosa", updateResult)

        // Opcionalment, comprovem que l'historial s'ha actualitzat realment
        val retrieved = HistorialServerCommunication.getHistorialById(token, updatedHistorial.idhistorial!!)
        assertNotNull("L'historial recuperat no pot ser null", retrieved)
        assertEquals("Actualitzat", retrieved?.patologies)
    }
}
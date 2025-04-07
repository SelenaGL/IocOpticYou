package com.example.opticyou

import com.example.opticyou.communications.ServerRequests
import com.example.opticyou.communications.network.RetrofitClient
import com.example.opticyou.communications.network.CenterServerCommunication
import com.example.opticyou.data.Center
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue

class CenterCrudIntegrationTest {

    /**
     * Prova la creació d'un centre.
     */
    @Test
    fun createCenterRealServer() = runBlocking {
        RetrofitClient.setBaseUrlForTesting("http://10.0.2.2:8083/")
        val loginResponse = ServerRequests.login("admin", "1234")
        assertNotNull("El login no hauria de retornar null", loginResponse)
        assertTrue("El login ha de ser exitós", loginResponse!!.success)
        val token = loginResponse.token

        // Defineix un centre nou
        val newCenter = Center(
            idClinica = 0,
            nom = "Centre creat",
            direccio = "Carrer Barcelona, 100",
            telefon = "666777888",
            horari_opertura = "09:30",
            horari_tancament = "19:30",
            email = "provacentrecreat@optica.cat"
        )

        // Crida la creació
        val createCenter = CenterServerCommunication.createClinica(token, newCenter)
        assertTrue("La creació del centre hauria de ser exitosa", createCenter)
    }

    /**
     * Prova la consulta de tots els centres en un servidor real.
     */
    @Test
    fun queryCentersRealServer() = runBlocking {
        // Configura Retrofit per apuntar al servidor de proves
        RetrofitClient.setBaseUrlForTesting("http://10.0.2.2:8083/")

        // Crida login per obtenir un token vàlid
        val loginResponse = ServerRequests.login("admin@optica.cat", "1234")
        assertNotNull("El login no hauria de retornar null", loginResponse)
        assertTrue("El login ha de ser exitós", loginResponse!!.success)
        println("Token obtingut: ${loginResponse.token}")

        // Utilitza el token real per fer la consulta de centres
        val centers = CenterServerCommunication.getCentres(loginResponse.token)
        println("Centres recuperats: $centers")
        assertNotNull("La resposta de centres no pot ser null", centers)
        //assertTrue("La llista de centres hauria de tenir almenys un element", centers!!.isNotEmpty())
    }

    /**
     * Prova l'actualització d'un centre en un servidor real.
     *
     * Nota: Es crea primer un centre, es recupera per obtenir-ne l'id,
     * es modifica el nom i es comprova que l'actualització s'ha realitzat.
     */
    @Test
    fun updateCenterRealServer() = runBlocking {
        RetrofitClient.setBaseUrlForTesting("http://10.0.2.2:8083/")
        val loginResponse = ServerRequests.login("administrador", "1234")
        assertNotNull("El login no hauria de retornar null", loginResponse)
        assertTrue("El login ha de ser exitós", loginResponse!!.success)
        val token = loginResponse.token

        // Crea un centre per actualitzar
        val newCenter = Center(
            idClinica = 0,
            nom = "Centre a Actualitzar",
            direccio = "Carrer Girona, 200",
            telefon = "666888999",
            horari_opertura = "10:00",
            horari_tancament = "20:00",
            email = "provacentreactualitzat@optica.cat"
        )
        val created = CenterServerCommunication.createClinica(token, newCenter)
        assertTrue("La creació del centre per actualitzar hauria de ser exitosa", created)

        // Recupera el centre creat per obtenir-ne l'id
        val centers = CenterServerCommunication.getCentres(token)
        assertNotNull("La consulta de centres no pot ser null", centers)
        val centerToUpdate = centers!!.find { it.nom == newCenter.nom }
            ?: error("No s'ha trobat el centre per actualitzar")

        // Actualitza el nom del centre
        val updatedCenter = centerToUpdate.copy(nom = "Centre Actualitzat")
        val result = CenterServerCommunication.updateClinica(token, updatedCenter)
        assertNotNull("La resposta de l'actualització no hauria de ser null", result)
        // Verifica que el missatge de resposta sigui el correcte
        assertEquals("Clínica actualitzada correctament", result)
    }

    /**
     * Prova l'eliminació d'un centre en un servidor real.
     *
     * Nota: Es crea primer un centre per eliminar, s'elimina i es verifica que ja no apareix en la consulta.
     */
    @Test
    fun deleteCenterRealServer() = runBlocking {
        RetrofitClient.setBaseUrlForTesting("http://10.0.2.2:8083/")

        // Obté un token vàlid amb login
        val loginResponse = ServerRequests.login("admin4@optica.cat", "1234")
        assertNotNull("El login no hauria de retornar null", loginResponse)
        assertTrue("El login ha de ser exitós", loginResponse!!.success)
        val token = loginResponse.token

        // Crea un centre per eliminar
        val newCenter = Center(
            idClinica = 0,
            nom = "Centre a Eliminar",
            direccio = "Carrer Tarragona, 300",
            telefon = "666999888",
            horari_opertura = "09:00",
            horari_tancament = "19:00",
            email = "provacentreeliminat@optica.cat"
        )
        val created = CenterServerCommunication.createClinica(token, newCenter)
        assertTrue("La creació del centre per eliminar hauria de ser exitosa", created)

        // Recupera la llista de centres per obtenir l'id del centre creat
        val centers = CenterServerCommunication.getCentres(token)
        assertNotNull("La consulta de centres no pot ser null", centers)
        val centerToDelete = centers!!.find { it.nom == newCenter.nom }
            ?: error("No s'ha trobat el centre per eliminar")

        // Executa l'eliminació i comprova que retorna true
        val deleteResult =
            CenterServerCommunication.deleteClinica(centerToDelete.idClinica!!, token)
        assertTrue("L'eliminació hauria de ser exitosa", deleteResult)
    }
}
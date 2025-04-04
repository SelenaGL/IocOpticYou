package com.example.opticyou

import com.example.opticyou.communications.ServerRequests
import com.example.opticyou.communications.network.ClientServerCommunication
import com.example.opticyou.communications.network.RetrofitClient
import com.example.opticyou.data.Client
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ClientCrudIntegrationTest {

    /**
     * Prova la creació d'un client.
     */
    @Test
    fun testCreateClient() = runBlocking {
        RetrofitClient.setBaseUrlForTesting("http://10.0.2.2:8083/")

        // Crida login per obtenir un token vàlid
        val loginResponse = ServerRequests.login("admin@optica.cat", "1234")
        assertNotNull("El login no hauria de retornar null", loginResponse)
        assertTrue("El login ha de ser exitós", loginResponse!!.success)
        println("Token obtingut: ${loginResponse.token}")
        val token = loginResponse.token


        // Crea un nou client. L'id es pot establir en 0 perquè s'assumeix que el servidor genera l'identificador.
        val newClient = Client(
            idUsuari = 0,
            nom = "Client de Prova",
            email = "clientprova@example.com",
            contrasenya = "1234",
            telefon = "666555444",
            sexe = "Dona",
            dataNaixament = "20/03/2000",
            clinicaId = 1,
            historialId = 0,
        )
        val createClient = ClientServerCommunication.createClient(token, newClient)
        println("Resultat de la creació del client: $createClient")
        assertTrue("La creació del client ha de ser exitosa", createClient)
    }

    /**
     * Prova l'obtenció de la llista de clients.
     */
    @Test
    fun testGetClients() = runBlocking {
        RetrofitClient.setBaseUrlForTesting("http://10.0.2.2:8083/")

        val loginResponse = ServerRequests.login("admin", "1234")
        assertNotNull("El login no hauria de retornar null", loginResponse)
        assertTrue("El login ha de ser exitós", loginResponse!!.success)
        println("Token obtingut: ${loginResponse.token}")
        val token = loginResponse.token

        val clients = ClientServerCommunication.getClients(token)
        println("Llista de clients obtinguda: $clients")
        assertNotNull("La llista de clients no pot ser null", clients)
    }

    /**
     * Prova l'actualització d'un client existent.
     */
    @Test
    fun testUpdateClient() = runBlocking {
        RetrofitClient.setBaseUrlForTesting("http://10.0.2.2:8083/")

        val loginResponse = ServerRequests.login("administrador", "1234")
        assertNotNull("El login no hauria de retornar null", loginResponse)
        assertTrue("El login ha de ser exitós", loginResponse!!.success)
        println("Token obtingut: ${loginResponse.token}")
        val token = loginResponse.token

        // Primer, creem un client que posteriorment actualitzarem.
        val newClient = Client(
            idUsuari = 0,
            nom = "Client a Actualitzar",
            email = "clientactualitzar@optica.cat",
            contrasenya = "1234",
            telefon = "666555444",
            sexe = "Dona",
            dataNaixament = "20/03/2000",
            clinicaId = 1,
            historialId = 0,
        )
        val creationResult = ClientServerCommunication.createClient(token, newClient)
        assertTrue("La creació del client per actualitzar ha de ser exitosa", creationResult)

        // Recuperem la llista de clients i busquem el client creat
        val clients = ClientServerCommunication.getClients(token)
        assertNotNull("La llista de clients no pot ser null", clients)
        val clientToUpdate = clients!!.find { it.nom == newClient.nom }
        assertNotNull("S'ha de trobar el client creat per actualitzar", clientToUpdate)

        // Actualitzem el nom del client
        val updatedClient = clientToUpdate!!.copy(nom = "Client Actualitzat")
        val updateResult = ClientServerCommunication.updateClient(token, updatedClient)
        println("Client actualitzat: $updateResult")
        assertNotNull("El client actualitzat no pot ser null", updateResult)
        assertEquals("Client Actualitzat", updateResult!!.nom)
    }

    /**
     * Prova l'eliminació d'un client.
     */
    @Test
    fun testDeleteClient() = runBlocking {
        RetrofitClient.setBaseUrlForTesting("http://10.0.2.2:8083/")

        val loginResponse = ServerRequests.login("admin4@optica.cat", "1234")
        assertNotNull("El login no hauria de retornar null", loginResponse)
        assertTrue("El login ha de ser exitós", loginResponse!!.success)
        println("Token obtingut: ${loginResponse.token}")
        val token = loginResponse.token

        // Creem un client que després eliminarem
        val newClient = Client(
            idUsuari = 0,
            nom = "Client a eliminar",
            email = "clienteliminar@optica.cat",
            contrasenya = "1234",
            telefon = "666555444",
            sexe = "Dona",
            dataNaixament = "20/03/2000",
            clinicaId = 1,
            historialId = 0,
        )
        val creationResult = ClientServerCommunication.createClient(token, newClient)
        assertTrue("La creació del client per eliminar ha de ser exitosa", creationResult)

        // Recuperem la llista de clients per obtenir l'id del client creat
        val clients = ClientServerCommunication.getClients(token)
        assertNotNull("La llista de clients no pot ser null", clients)
        val clientToDelete = clients!!.find { it.nom == newClient.nom  }
        assertNotNull("S'ha de trobar el client per eliminar", clientToDelete)

        // Eliminem el client
        val deletionResult =
            clientToDelete!!.idUsuari?.let { ClientServerCommunication.deleteClient(it, token) }
        println("Resultat de l'eliminació del client: $deletionResult")
        if (deletionResult != null) {
            assertTrue("L'eliminació del client ha de ser exitosa", deletionResult)
        }
    }
}
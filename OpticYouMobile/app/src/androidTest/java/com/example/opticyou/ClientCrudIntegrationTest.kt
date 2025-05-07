package com.example.opticyou

import com.example.opticyou.communications.ServerRequests
import com.example.opticyou.communications.network.ClientServerCommunication
import com.example.opticyou.communications.network.RetrofitClient
import com.example.opticyou.communications.network.TreballadorServerCommunication
import com.example.opticyou.data.Client
import com.example.opticyou.data.Treballador
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ClientCrudIntegrationTest {

    /**
     * Prova la creació d'un treballador.
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


        // Crea un nou treballador.
        val newTreballador = Treballador(
            idUsuari = 0,
            nom = "Treballador creat",
            email = "treballadorreat@optica.cat",
            contrasenya = "1234",
            especialitat = "666555444",
            estat = "Dona",
            iniciJornada = "10:00",
            diesJornada = "5",
            fiJornada = "20:00",
            clinicaId = 1,
        )
        val createClient = TreballadorServerCommunication.create(token, newTreballador)
        println("Resultat de la creació del treballador: $createClient")
        assertTrue("La creació del treballador ha de ser exitosa", createClient)
    }

    /**
     * Prova l'obtenció de la llista de treballadors.
     */
    @Test
    fun testGetClients() = runBlocking {
        RetrofitClient.setBaseUrlForTesting("http://10.0.2.2:8083/")

        val loginResponse = ServerRequests.login("admin", "1234")
        assertNotNull("El login no hauria de retornar null", loginResponse)
        assertTrue("El login ha de ser exitós", loginResponse!!.success)
        println("Token obtingut: ${loginResponse.token}")
        val token = loginResponse.token

        val treballadors = TreballadorServerCommunication.getAll(token)
        println("Llista de treballadors obtinguda: $treballadors")
        assertNotNull("La llista de treballadors no pot ser null", treballadors)
    }

    /**
     * Prova l'actualització d'un treballador existent.
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
            dataNaixament = "20/03/2010",
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
            dataNaixament = "20/03/2015",
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
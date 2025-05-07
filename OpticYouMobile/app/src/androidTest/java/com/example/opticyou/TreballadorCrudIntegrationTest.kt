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

class TreballadorCrudIntegrationTest {

        /**
     * Prova la creació d'un treballador.
     */
    @Test
    fun testCreateTreballador() = runBlocking {
        RetrofitClient.setBaseUrlForTesting("https://10.0.2.2:8083/")

        // Crida login per obtenir un token vàlid
        val loginResponse = ServerRequests.login("admin@exemple.com", "admin123")
        assertNotNull("El login no hauria de retornar null", loginResponse)
        assertTrue("El login ha de ser exitós", loginResponse!!.success)
        println("Token obtingut: ${loginResponse.token}")
        val token = loginResponse.token


        // Crea un nou treballador.
        val newTreballador = Treballador(
            idUsuari = 0,
            nom = "Treballador creat",
            email = "treballadorcreat@optica.cat",
            contrasenya = "1234",
            especialitat = "Optometrista",
            estat = "actiu",
            iniciJornada = "10:00",
            diesJornada = "5",
            fiJornada = "20:00",
            clinicaId = 1,
        )
        val createTreballador = TreballadorServerCommunication.create(token, newTreballador)
        println("Resultat de la creació del treballador: $createTreballador")
        assertTrue("La creació del treballador ha de ser exitosa", createTreballador)
    }

    /**
     * Prova l'obtenció de la llista de treballadors.
     */
    @Test
    fun testGetTreballadors() = runBlocking {
        RetrofitClient.setBaseUrlForTesting("https://10.0.2.2:8083/")

        val loginResponse = ServerRequests.login("admin@optica.com", "admin123")
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
    fun testUpdateTreballador() = runBlocking {
        RetrofitClient.setBaseUrlForTesting("https://10.0.2.2:8083/")

        val loginResponse = ServerRequests.login("administrador@optica.com", "admin123")
        assertNotNull("El login no hauria de retornar null", loginResponse)
        assertTrue("El login ha de ser exitós", loginResponse!!.success)
        println("Token obtingut: ${loginResponse.token}")
        val token = loginResponse.token

        // Crea un treballador que modificarem
        val newTreballador = Treballador(
            idUsuari = 0,
            nom = "Treballador a actualitzar",
            email = "treballadoractualitzar@optica.cat",
            contrasenya = "1234",
            especialitat = "Optometrista",
            estat = "actiu",
            iniciJornada = "10:00",
            diesJornada = "5",
            fiJornada = "20:00",
            clinicaId = 1,
        )
        val creationResult = TreballadorServerCommunication.create(token, newTreballador)
        assertTrue("La creació del treballador per actualitzar ha de ser exitosa", creationResult)

        // Recuperem la llista de treballadors i busquem el treballador creat
        val treballadors = TreballadorServerCommunication.getAll(token)
        assertNotNull("La llista de treballadors no pot ser null", treballadors)
        val treballadorToUpdate = treballadors!!.find { it.nom == newTreballador.nom }
        assertNotNull("S'ha de trobar el treballador creat per actualitzar", treballadorToUpdate)

        // Actualitzem el nom del client
        val updatedTreballador = treballadorToUpdate!!.copy(nom = "Treballador Actualitzat")
        val updateResult = TreballadorServerCommunication.update(token, updatedTreballador)
        println("Treballador actualitzat: $updateResult")
        assertNotNull("El treballador actualitzat no pot ser null", updateResult)
        assertTrue("Treballador Actualitzat", updateResult)
    }

    /**
     * Prova l'eliminació d'un client.
     */
    @Test
    fun testDeleteClient() = runBlocking {
        RetrofitClient.setBaseUrlForTesting("https://10.0.2.2:8083/")

        val loginResponse = ServerRequests.login("admin@exemple.com", "admin123")
        assertNotNull("El login no hauria de retornar null", loginResponse)
        assertTrue("El login ha de ser exitós", loginResponse!!.success)
        println("Token obtingut: ${loginResponse.token}")
        val token = loginResponse.token

        // Creem un client que després eliminarem
        val newTreballador = Treballador(
            idUsuari = 0,
            nom = "Client a eliminar",
            email = "clienteliminar@optica.cat",
            contrasenya = "1234",
            especialitat = "Optometrista",
            estat = "actiu",
            iniciJornada = "10:00",
            diesJornada = "5",
            fiJornada = "20:00",
            clinicaId = 1,
        )
        val creationResult = TreballadorServerCommunication.create(token, newTreballador)
        assertTrue("La creació del treballador per eliminar ha de ser exitosa", creationResult)

        // Recuperem la llista de clients per obtenir l'id del client creat
        val treballadors = TreballadorServerCommunication.getAll(token)
        assertNotNull("La llista de clients no pot ser null", treballadors)
        val treballadorToDelete = treballadors!!.find { it.nom == newTreballador.nom  }
        assertNotNull("S'ha de trobar el client per eliminar", treballadorToDelete)

        // Eliminem el client
        val deletionResult = TreballadorServerCommunication.delete(token, treballadorToDelete!!.idUsuari!!)
        println("Resultat de l'eliminació del client: $deletionResult", )
        assertTrue("L'eliminació del client ha de ser exitosa", deletionResult)
    }
}

package com.example.opticyou

import com.example.opticyou.communications.network.ClientServerCommunication
import com.example.opticyou.communications.network.RetrofitClient
import com.example.opticyou.data.Client
import io.mockk.coEvery
import io.mockk.mockkObject
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.mock.Calls

class ClientCrudUnitTest {

    private val token = "token"

    private val client = Client(
        idUsuari = 1,
        nom = "Client de Prova",
        email = "clientprova@optica.cat",
        contrasenya = "1234",
        telefon = "666555444",
        sexe = "Dona",
        dataNaixament = "20/03/2000",
        clinicaId = 1,
        historialId = 0
    )

    @Before
    fun setup() {
        // utilitzem mock per simular les dades
        mockkObject(RetrofitClient)
    }

    @Test
    fun testCreateClientSuccess() = runBlocking {
        // Simulem un Response exitós
        coEvery { RetrofitClient.instance.createClient("Bearer $token", client) } returns
                Calls.response(Response.success<Void>(null))


        val result = ClientServerCommunication.createClient(token, client)
        assertTrue("La creació del client ha de ser exitosa", result)
    }

    @Test
    fun testGetClientsSuccess() = runBlocking {
        // Simulem una resposta exitosa que retorni una llista de clients
        val clients = listOf(client)
        coEvery { RetrofitClient.instance.getAllClients("Bearer $token") } returns
                Calls.response(Response.success(clients))

        val result = ClientServerCommunication.getClients(token)
        assertNotNull("La llista de clients no pot ser null", result)
        assertEquals("Ha de retornar 1 client", 1, result?.size)
        assertEquals(client, result?.first())
    }

    @Test
    fun testUpdateClientSuccess() = runBlocking {
        // Creem una versió actualitzada del client
        val updatedClient = client.copy(nom = "Client Actualitzat")
        // Simulem que la crida a updateClient retorna una resposta exitosa
        coEvery { RetrofitClient.instance.updateClient("Bearer $token", updatedClient) } returns
                Calls.response(
                    Response.success(
                        "".toResponseBody("text/plain".toMediaTypeOrNull())
                    )
                )

        val result = ClientServerCommunication.updateClient(token, updatedClient)
        assertNotNull("El client actualitzat no pot ser null", result)
        assertEquals("Client Actualitzat", result?.nom)
    }

    @Test
    fun testDeleteClientSuccess() = runBlocking {
        // Simulem que la crida DELETE retorna un missatge que indica que l'eliminació ha estat exitosa.
        coEvery { RetrofitClient.instance.deleteClient(1, "Bearer $token") } returns
                Calls.response(Response.success(null as Void?))

        val result = ClientServerCommunication.deleteClient(1, token)
        assertTrue("L'eliminació del client ha de ser exitosa", result)
    }
}
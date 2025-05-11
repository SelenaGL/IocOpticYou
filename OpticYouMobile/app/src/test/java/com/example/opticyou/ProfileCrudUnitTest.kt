package com.example.opticyou

import com.example.opticyou.communications.network.ClientServerCommunication
import com.example.opticyou.communications.network.RetrofitClient
import com.example.opticyou.data.Client
import io.mockk.coEvery
import io.mockk.mockkObject
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.mock.Calls

class ProfileCrudUnitTest {

    private val token = "token"
    private val userId = 10L

    private val client = Client(
        idUsuari    = userId,
        nom         = "Test profile",
        email       = "profile@optica.cat",
        contrasenya = "****",
        telefon     = "666666666",
        sexe        = "Dona",
        dataNaixament = "01/01/1990",
        clinicaId   = 1,
        historialId = 1
    )

    @Before
    fun setup() {
        mockkObject(RetrofitClient)
    }

    @Test
    fun testGetProfileSuccess() = runBlocking {
        coEvery { RetrofitClient.instance.getClientById(userId,"Bearer $token") } returns
                Calls.response(Response.success(client))

        val result = ClientServerCommunication.getClientById(userId, token)
        assertNotNull("El perfil no pot ser null", result)
        assertEquals("Ha de retornar 1 perfil", client, result)

    }

    @Test
    fun testUpdateProfileSuccess() = runBlocking {
        coEvery { RetrofitClient.instance.updateClientSelf("Bearer $token", client) } returns
                Calls.response(Response.success("Profile actualitzat correctament"))

        val result = ClientServerCommunication.updateSelf(token, client)
        assertTrue("L'actualització ha de ser exitosa", result)
    }

    @Test
    fun testDeleteProfileSuccess() = runBlocking {
        coEvery { RetrofitClient.instance.deleteClientSelf("Bearer $token") } returns
                Calls.response(Response.success("Perfil eliminat correctament"))

        val result = ClientServerCommunication.deleteSelf(token)
        assertTrue("L'eliminació del perfil ha de ser exitosa", result)
    }
}
package com.example.opticyou

import com.example.opticyou.communications.network.TreballadorServerCommunication
import com.example.opticyou.communications.network.RetrofitClient
import com.example.opticyou.data.Treballador
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

class TreballadorCrudUnitTest {

    private val token = "token"

    private val treballador = Treballador(
        idUsuari    = 1L,
        nom         = "Test Treballador",
        email       = "treballador@optica.cat",
        contrasenya = "1234",
        especialitat= "Optometrista",
        estat       = "Actiu",
        iniciJornada= "10:00",
        diesJornada = "5",
        fiJornada   = "20:00",
        clinicaId   = 1
    )

    @Before
    fun setup() {
        mockkObject(RetrofitClient)
    }

    @Test
    fun testGetAllTreballadorsSuccess() = runBlocking {
        val list = listOf(treballador)
        coEvery { RetrofitClient.instance.getAllTreballadors("Bearer $token") } returns
                Calls.response(Response.success(list))

        val result = TreballadorServerCommunication.getAll(token)
        assertNotNull("La llista no ha de ser null", result)
        assertEquals("Ha de retornar 1 treballador", 1, result?.size)
        assertEquals(treballador, result?.first())
    }

    @Test
    fun testCreateTreballadorSuccess() = runBlocking {
        coEvery { RetrofitClient.instance.createTreballador("Bearer $token", treballador) } returns
                Calls.response(Response.success<Void>(null))

        val result = TreballadorServerCommunication.create(token, treballador)
        assertTrue("La creació ha de ser exitosa", result)
    }

    @Test
    fun testUpdateTreballadorSuccess() = runBlocking {
        coEvery { RetrofitClient.instance.updateTreballador("Bearer $token", treballador) } returns
                Calls.response(Response.success("Treballador actualitzat correctament"))


        val result = TreballadorServerCommunication.update(token, treballador)
        assertTrue("L'actualització ha de ser exitosa", result)
    }

    @Test
    fun testDeleteTreballadorSuccess() = runBlocking {
        coEvery { RetrofitClient.instance.deleteTreballador(treballador.idUsuari!!, "Bearer $token") } returns
                Calls.response(Response.success("Treballador eliminat correctament"))

        val result = TreballadorServerCommunication.delete(token, treballador.idUsuari!!)
        assertTrue("L'eliminació ha de ser exitosa", result)
    }
}
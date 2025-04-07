package com.example.opticyou

import com.example.opticyou.communications.network.CenterServerCommunication
import com.example.opticyou.communications.network.RetrofitClient
import com.example.opticyou.data.Center
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

class CenterCrudUnitTest {

    private val token = "token"

    private val center = Center(
        idClinica = 0,
        nom = "Centre prova",
        direccio = "Carrer Lleida, 10",
        telefon = "666555444",
        horari_opertura = "09:00",
        horari_tancament = "19:00",
        email = "centreprova@optica.cat"
    )

    @Before
    fun setup() {
        mockkObject(RetrofitClient)
    }

    @Test
    fun testCreateCenterSuccess() = runBlocking {
        coEvery { RetrofitClient.instance.createClinica("Bearer $token", center) } returns
                Calls.response(Response.success<Void>(null))

        val result = CenterServerCommunication.createClinica(token, center)
        assertTrue("La creació del centre ha de ser exitosa", result)
    }

    @Test
    fun testGetCentersSuccess() = runBlocking {
        val fakeCenters = listOf(center)
        coEvery { RetrofitClient.instance.getAllClinicas("Bearer $token") } returns
                Calls.response(Response.success(fakeCenters))

        val result = CenterServerCommunication.getCentres(token)
        assertNotNull("La llista de centres no pot ser null", result)
        assertEquals("Ha de retornar 1 centre", 1, result?.size)
        assertEquals(center, result?.first())
    }

    @Test
    fun testUpdateCenterSuccess() = runBlocking {
        val updatedCenter = center.copy(nom = "Centre Actualitzat")
        coEvery { RetrofitClient.instance.updateClinica("Bearer $token", updatedCenter) } returns
                Calls.response(Response.success("Clínica actualitzada correctament"))

        val result = CenterServerCommunication.updateClinica(token, updatedCenter)
        assertNotNull("El missatge d'actualització no pot ser null", result)
        assertEquals("Clínica actualitzada correctament", result)
    }

    @Test
    fun testDeleteCenterSuccess() = runBlocking {
        coEvery { RetrofitClient.instance.deleteClinica(1, "Bearer $token") } returns
                Calls.response(Response.success("Clínica eliminada correctament"))

        val result = CenterServerCommunication.deleteClinica(1, token)
        assertTrue("L'eliminació del centre ha de ser exitosa", result)
    }
}
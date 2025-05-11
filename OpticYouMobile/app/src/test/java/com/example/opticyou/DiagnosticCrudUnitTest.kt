package com.example.opticyou

import com.example.opticyou.communications.network.DiagnosticServerCommunication
import com.example.opticyou.communications.network.RetrofitClient
import com.example.opticyou.data.Diagnostic
import io.mockk.coEvery
import io.mockk.mockkObject
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.mock.Calls

class DiagnosticCrudUnitTest {

    private val token = "token"

    // Creem un historial de prova.
    private val diagnostic = Diagnostic(
        iddiagnostic = 1,
        descripcio = "prova diagnòstic",
        date = "2025-05-15T10:30",
        historialId = 1
    )

    @Before
    fun setup() {
        // Utilitzem MockK per simular les crides a RetrofitClient
        mockkObject(com.example.opticyou.communications.network.RetrofitClient)
    }

    @Test
    fun testCreateDiagnosticSuccess() = runBlocking {
        coEvery { RetrofitClient.instance.createDiagnostic("Bearer $token", diagnostic) } returns
                Calls.response(Response.success<Void>(null))

        val result = DiagnosticServerCommunication.createDiagnostic(token, diagnostic)
        assertTrue("La creació del diagnostic ha de ser exitosa", result)
    }

    @Test
    fun testGetAllDiagnosticsSuccess() = runBlocking {
        val list = listOf(diagnostic)
        coEvery { RetrofitClient.instance.getAllDiagnostics("Bearer $token") } returns
                Calls.response(Response.success(list))

        val result = DiagnosticServerCommunication.getDiagnostics(token)
        assertNotNull("La llista de diagnostics no pot ser null", result)
        assertEquals("Ha de retornar 1 diagnostic", 1, result?.size)
        assertEquals(diagnostic, result?.first())
    }

    @Test
    fun testUpdateDiagnosticSuccess() = runBlocking {
        coEvery { RetrofitClient.instance.updateDiagnostic("Bearer $token", diagnostic) } returns
                Calls.response(Response.success("Dx actualitzat correctament"))

        val result = DiagnosticServerCommunication.updateDiagnostic(token, diagnostic)
        assertTrue("L'actualització ha de ser exitosa", result)

    }

    @Test
    fun testDeleteDiagnosticSuccess() = runBlocking {
        coEvery { RetrofitClient.instance.deleteDiagnostic(1, "Bearer $token") } returns
                Calls.response(Response.success("Perfil eliminat correctament"))

        val result = DiagnosticServerCommunication.deleteDiagnostic(1,token)
        assertTrue("L'eliminació del perfil ha de ser exitosa", result)
    }
}
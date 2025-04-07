package com.example.opticyou

import com.example.opticyou.communications.network.HistorialServerCommunication
import com.example.opticyou.data.Historial
import io.mockk.coEvery
import io.mockk.mockkObject
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.mock.Calls
import java.time.LocalDateTime

class HistorialCrudUnitTest {

    private val token = "token"

    // Creem un historial de prova. Si el camp client és no nul, pots definir un dummy; en aquest exemple ho deixem null.
    private val historial = Historial(
        idhistorial = 1,
        dataCreacio = LocalDateTime.now().toString(),
        patologies = "Sense antecedents",
        client = null
    )

    @Before
    fun setup() {
        // Utilitzem MockK per simular les crides a RetrofitClient
        mockkObject(com.example.opticyou.communications.network.RetrofitClient)
    }


    @Test
    fun testGetAllHistorialSuccess() = runBlocking {
        // Simulem una resposta exitosa que retorni una llista amb un historial
        val historials = listOf(historial)
        coEvery { com.example.opticyou.communications.network.RetrofitClient.instance.getAllHistorial("Bearer $token") } returns
                Calls.response(Response.success(historials))

        val result = HistorialServerCommunication.getAllHistorial(token)
        assertNotNull("La llista d'historials no pot ser null", result)
        assertEquals("Ha de retornar 1 historial", 1, result?.size)
        assertEquals(historial, result?.first())
    }

    @Test
    fun testUpdateHistorialSuccess() = runBlocking {
        // Creem una versió actualitzada de l'historial
        val updatedHistorial = historial.copy(patologies = "Patologies Actualitzades")
        // Simulem que la crida a updateHistorial retorna una resposta exitosa (el servidor retorna text)
        coEvery { com.example.opticyou.communications.network.RetrofitClient.instance.updateHistorial("Bearer $token", updatedHistorial) } returns
                Calls.response(Response.success("Historial actualitzat correctament"))

        val result = HistorialServerCommunication.updateHistorial(token, updatedHistorial)
        assertTrue("L'actualització de l'historial ha de ser exitosa", result)
    }
}
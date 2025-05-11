package com.example.opticyou

import com.example.opticyou.communications.ServerRequests
import com.example.opticyou.communications.network.ClientServerCommunication
import com.example.opticyou.communications.network.DiagnosticServerCommunication
import com.example.opticyou.communications.network.RetrofitClient
import com.example.opticyou.communications.network.TreballadorServerCommunication
import com.example.opticyou.data.Client
import com.example.opticyou.data.Diagnostic
import com.example.opticyou.data.Treballador
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DiagnosticCrudIntegrationTest {

    /**
     * Prova la creació d’un diagnòstic.
     */
    @Test
    fun testCreateDiagnostic() = runBlocking {
        // Posa l’URL de test
        RetrofitClient.setBaseUrlForTesting("https://10.0.2.2:8083/")

        // Fem login per obtenir el token
        val loginResponse = ServerRequests.login("admin@exemple.com", "admin123")
        assertNotNull("El login no hauria de retornar null", loginResponse)
        assertTrue("El login ha de ser exitós", loginResponse!!.success)
        val token = loginResponse.token

        // Creem un nou diagnòstic
        val newDiagnostic = Diagnostic(
            iddiagnostic = 0,
            date = "2025-05-09",
            descripcio = "Creació Dx",
            historialId = 1
        )
        val creationResult = DiagnosticServerCommunication.createDiagnostic(token, newDiagnostic)
        println("Resultat de la creació del diagnòstic: $creationResult")
        assertTrue("La creació del diagnòstic ha de ser exitosa", creationResult)
    }

    /**
     * Prova l’obtenció de la llista de diagnòstics per historial.
     */
    @Test
    fun testGetDiagnosticsByHistorial() = runBlocking {
        RetrofitClient.setBaseUrlForTesting("https://10.0.2.2:8083/")
        val loginResponse = ServerRequests.login("admin@optica.com", "admin123")
        assertNotNull("El login no hauria de retornar null", loginResponse)
        assertTrue("El login ha de ser exitós", loginResponse!!.success)
        val token = loginResponse.token

        // Carreguem tots els diagnòstics per l’historial 7
        val diagnostics = DiagnosticServerCommunication.getDiagnosticById(1, token)
        println("Diagnostics obtinguts: $diagnostics")
        assertNotNull("La llista de diagnòstics no pot ser null", diagnostics)
        assertTrue("Hi ha d’haver almenys un diagnòstic", diagnostics!!.isNotEmpty())
    }

    /**
     * Prova l’actualització d’un diagnòstic existent.
     */
    @Test
    fun testUpdateDiagnostic() = runBlocking {
        RetrofitClient.setBaseUrlForTesting("https://10.0.2.2:8083/")
        val loginResponse = ServerRequests.login("administrador@optica.com", "admin123")
        assertNotNull("El login no hauria de retornar null", loginResponse)
        assertTrue("El login ha de ser exitós", loginResponse!!.success)
        val token = loginResponse.token

        // Primer obtenim algun diagnòstic existent
        val diagnostics = DiagnosticServerCommunication.getDiagnosticById(1, token)
        assertNotNull("La llista de diagnòstics no pot ser null", diagnostics)
        val toUpdate = diagnostics!!.first()

        // Modifiquem la descripció
        val updatedDiagnostic = toUpdate.copy(descripcio = "Descripció modificada")
        val updateResult = DiagnosticServerCommunication.updateDiagnostic(token, updatedDiagnostic)
        println("Resultat de l’actualització: $updateResult")
        assertTrue("L’actualització del diagnòstic ha de ser exitosa", updateResult)
    }


    /**
     * Prova l’eliminació d’un diagnòstic.
     */
    @Test
    fun testDeleteDiagnostic() = runBlocking {
        RetrofitClient.setBaseUrlForTesting("https://10.0.2.2:8083/")
        val loginResponse = ServerRequests.login("administrador@exemple.com", "admin123")
        assertNotNull("El login no hauria de retornar null", loginResponse)
        assertTrue("El login ha de ser exitós", loginResponse!!.success)
        val token = loginResponse.token

        // Creem un diagnòstic nou per assegurar-nos que existeix
        val newDiagnostic = Diagnostic(
            iddiagnostic = 0,
            date = "2025-05-09",
            descripcio = "Per eliminar",
            historialId = 1
        )
        val created = DiagnosticServerCommunication.createDiagnostic(token, newDiagnostic)
        assertTrue("La creació per eliminar ha de ser exitosa", created)

        // Recarreguem i trobem el que acabem de crear
        val diagnostics = DiagnosticServerCommunication.getDiagnosticById(1, token)
        val toDelete = diagnostics!!.find { it.descripcio == "Per eliminar" }
        assertNotNull("Ha de trobar el diagnòstic creat per eliminar", toDelete)

        // Eliminem-lo
        val deletionResult = DiagnosticServerCommunication.deleteDiagnostic(toDelete!!.iddiagnostic!!, token)
        println("Resultat de l’eliminació: $deletionResult")
        assertTrue("L’eliminació del diagnòstic ha de ser exitosa", deletionResult)
    }
}

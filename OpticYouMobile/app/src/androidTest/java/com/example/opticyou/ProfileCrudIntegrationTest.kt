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

class ProfileCrudIntegrationTest {

    private val testUserId = 47L

    /**
     * Prova l'obtenció del perfil propi.
     */
    @Test
    fun testGetClients() = runBlocking {
        RetrofitClient.setBaseUrlForTesting("https://10.0.2.2:8083/")

        val loginResponse = ServerRequests.login("adriana@optica.cat", "5678")
        assertNotNull("El login no hauria de retornar null", loginResponse)
        assertTrue("El login ha de ser exitós", loginResponse!!.success)
        println("Token obtingut: ${loginResponse.token}")
        val token = loginResponse.token

        val profile  = ClientServerCommunication.getClientById(testUserId,token)
        println("Perfil obtingut: $profile")
        assertNotNull("El perfil no pot ser null", profile)
    }

    /**
     * Prova l'actualització del perfil propi.
     */
    @Test
    fun testUpdateClient() = runBlocking {
        RetrofitClient.setBaseUrlForTesting("https://10.0.2.2:8083/")

        val loginResponse = ServerRequests.login("joan@optica.cat", "1111")
        assertNotNull("El login no hauria de retornar null", loginResponse)
        assertTrue("El login ha de ser exitós", loginResponse!!.success)
        println("Token obtingut: ${loginResponse.token}")
        val token = loginResponse.token

        // Carreguem el perfil actual
        val perfilOriginal = ClientServerCommunication.getClientById(testUserId, token)
        assertNotNull("El perfil original no pot ser null", perfilOriginal)

        // Fem una còpia amb el nom modificat
        val perfilModificat = perfilOriginal!!.copy(nom = "Perfil Modificat")

        // Truquem a updateSelf
        val resultat = ClientServerCommunication.updateSelf(token, perfilModificat)
        assertTrue("L’actualització del perfil ha de ser exitosa", resultat)

    }

    /**
     * Prova l'eliminació d'un client.
     */
    @Test
    fun testDeleteClient() = runBlocking {
        RetrofitClient.setBaseUrlForTesting("https://10.0.2.2:8083/")

        val loginResponse = ServerRequests.login("carme@optica.cat", "2222")
        assertNotNull("El login no hauria de retornar null", loginResponse)
        assertTrue("El login ha de ser exitós", loginResponse!!.success)
        println("Token obtingut: ${loginResponse.token}")
        val token = loginResponse.token

        // Carreguem el perfil actual
        val perfilOriginal = ClientServerCommunication.getClientById(testUserId, token)
        assertNotNull("El perfil original no pot ser null", perfilOriginal)

        // El client s'elimina a si mateix
        val resultat = ClientServerCommunication.deleteSelf(token)
        println("Resultat de l'eliminació: $resultat")
        assertTrue("L’eliminació del perfil ha de ser exitosa", resultat)
    }
}
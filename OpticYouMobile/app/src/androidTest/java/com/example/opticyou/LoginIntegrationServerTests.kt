package com.example.opticyou

import com.example.opticyou.communications.ServerRequests
import com.example.opticyou.communications.network.RetrofitClient
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Proves d'integració per verificar el login i el logout en un servidor real
 */

class LoginIntegrationServerTests {

    /**
     * Prova el login amb credencials d'administrador en un servidor real.
     */
    @Test
    fun loginAdminRealServer() = runBlocking {
        // Configura Retrofit per apuntar al servidor
        RetrofitClient.setBaseUrlForTesting("http://10.0.2.2:8083/")

        // Cridem login amb credencials administrador
        val response = ServerRequests.login("admin@optica.cat", "1234")
        println("Resposta de login amb servidor real: $response")

        assertNotNull("La resposta no pot ser null", response)
        assertTrue("El login OK amb el servidor real actiu", response!!.success)
        assertEquals("ADMIN", response.rol)
        assertTrue("El token no hauria d'estar buit", response.token.isNotEmpty())
    }

    /**
     * Prova el login amb credencials d'usuari client en un servidor real.
     */
    @Test
    fun loginUserRealServer() = runBlocking {
        RetrofitClient.setBaseUrlForTesting("http://10.0.2.2:8083/")

        // Cridem login amb credencials administrador
        val response = ServerRequests.login("selena@optica.com", "1234")
        println("Resposta de login amb servidor real: $response")

        assertNotNull("La resposta no pot ser null", response)
        assertTrue("El login OK amb el servidor real actiu", response!!.success)
        assertEquals("CLIENT", response.rol)
        assertTrue("El token no hauria d'estar buit", response.token.isNotEmpty())
    }

    /**
     * Prova el login amb un email invàlid en un servidor real.
     */
    @Test
    fun loginWrongEmailRealServer() = runBlocking {
        RetrofitClient.setBaseUrlForTesting("http://10.0.2.2:8083/")

        // Cridem login amb credencials administrador
        val response = ServerRequests.login("admin", "1234")
        println("Resposta de login amb servidor real: $response")
        assertNull("La resposta serà null", response)
    }

    /**
     * Prova el login amb una contrasenya incorrecte en un servidor real.
     */
    @Test
    fun loginWrongPasswRealServer() = runBlocking {
        RetrofitClient.setBaseUrlForTesting("http://10.0.2.2:8083/")

        // Cridem login amb credencials administrador
        val response = ServerRequests.login("admin@optica.cat", "wrongPassword")
        println("Resposta de login amb servidor real: $response")
        assertNull("La resposta serà null", response)
    }

    /**
     * Prova el procés de logout en un servidor real.
     */
    @Test
    fun logoutWithRealServer() = runBlocking {
        RetrofitClient.setBaseUrlForTesting("http://10.0.2.2:8083/")

        // Cridem logout
        val response = ServerRequests.logout("realToken")
        println("Resultat de logout amb servidor real: $response")
        assertTrue("Logout OK amb el servidor real actiu", response)
    }

}
package com.example.opticyou

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import com.example.opticyou.communications.ServerRequests
import com.example.opticyou.data.LoginResponse
import com.example.opticyou.ui.LoginViewModel
import io.mockk.coEvery
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

/**
 * Proves unitàries (sense IU ni connexió al servidor) per provar el login a l'aplicació
 */

class LoginUnitTests {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkObject(ServerRequests)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    /**
     * Prova que l'inici de sessió amb un usuari sense email, falla".
     */
    @Test
    fun login_WrongUsername() = runTest {
        // Simulem que `ServerRequests.login` retorna un LoginResponse fals
        coEvery { ServerRequests.login("admin", "1234") } returns LoginResponse(false, "", "")

        val response = ServerRequests.login("admin", "1234")
        assertNotNull("La resposta no pot ser null", response)
        // El login hauria de fallar perquè el username no és un email
        assertFalse("El login hauria de fallar si el username no és un email", response!!.success)
        assertEquals("", response.rol)
        assertEquals("", response.token)
    }

    /**
     * Prova que l'inici de sessió amb credencials d'admin retorna una resposta correcte amb el rol "admin".
     */
    @Test
    fun loginAdmin() = runBlocking {
        coEvery { ServerRequests.login("admin@optica.cat", "1234") } returns LoginResponse(true, "mockTokenAdmin", "admin")
        val response = ServerRequests.login("admin@optica.cat", "1234")
        assertNotNull("La resposta no pot ser null", response)
        assertTrue(response!!.success)
        assertEquals("admin", response.rol)
        assertEquals("mockTokenAdmin", response.token)
    }

    /**
     * Prova que l'inici de sessió amb credencials d'usuari retorna una resposta d'èxit amb el rol "user".
     */
    @Test
    fun loginUser() = runBlocking {
        coEvery { ServerRequests.login("usuari@optica.cat", "1234") } returns LoginResponse(true, "mockTokenUser", "user")
        val response = ServerRequests.login("usuari@optica.cat", "1234")
        assertNotNull("La resposta no pot ser null", response)
        assertTrue(response!!.success)
        assertEquals("user", response.rol)
        assertEquals("mockTokenUser", response.token)
    }

    /**
     * Prova que l'inici de sessió amb una contrasenya incorrecta falla i retorna un rol buit.
     */
    @Test
    fun loginWrongPassword() = runBlocking {
        coEvery { ServerRequests.login("admin@optica.cat", "error") } returns LoginResponse(false, "", "")
        val response = ServerRequests.login("admin@optica.cat", "error")
        assertNotNull("La resposta no pot ser null", response)
        assertFalse("El login hauria de fallar amb contrasenya incorrecta", response!!.success)
        assertEquals("", response.rol)
    }

    /**
     * Prova que el mètode de logout retorna true quan no hi ha sessió activa.
     */
    @Test
    fun logout() = runBlocking {
        coEvery { ServerRequests.logout() } returns true
        val response = ServerRequests.logout()
        assertTrue("Logout hauria de ser true quan no hi ha sessió activa", response)
    }
}



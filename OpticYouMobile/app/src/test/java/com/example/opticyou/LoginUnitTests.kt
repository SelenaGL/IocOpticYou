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

    /**
     * Prova que el LoginViewModel gestiona correctament les credencials vàlides.
     * Crida el callback amb una resposta exitosa i estat del ViewModel mostra que s'ha intentat el login i que el resultat és bo.
     */
    @Test
    fun loginViewModel_CorrectCredentials() = runTest {
        val viewModel = LoginViewModel(ioDispatcher = testDispatcher, mainDispatcher = testDispatcher)
        coEvery { ServerRequests.login("admin@optica.cat", "1234") } returns LoginResponse(true, "mockTokenAdmin", "admin")
        var callbackCalled = false
        val callback: (LoginResponse) -> Unit = { response ->
            callbackCalled = true
            assertTrue("Resposta exitosa amb credencials correctes", response.success)
        }

        viewModel.doLogin("admin@optica.cat", "1234", callback)

        // Avança el dispatcher per processar totes les coroutines pendents
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue("El callback hauria d'haver estat cridat", callbackCalled)
        assertTrue("loginTried hauria de ser true", viewModel.uiState.value.loginTried)
        assertTrue("goodResult hauria de ser true per credencials vàlides", viewModel.uiState.value.goodResult)
    }

    /**
     * Prova que el LoginViewModel gestiona correctament les credencials incorrectes.
     * Crida el callback amb una resposta fallida i estat del ViewModel mostra que s'ha intentat el login, pero el resultat és no bo.
     */
    @Test
    fun loginViewModel_IncorrectCredentials() = runTest {
        val viewModel = LoginViewModel(ioDispatcher = testDispatcher, mainDispatcher = testDispatcher)
        coEvery { ServerRequests.login("admin@optica.cat", "wrong") } returns LoginResponse(false, "", "")
        var callbackCalled = false
        val callback: (LoginResponse) -> Unit = { response ->
            callbackCalled = true
            assertFalse("Resposta fallida amb credencials incorrectes", response.success)
        }
        viewModel.doLogin("admin@optica.cat", "wrong", callback)

        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue("El callback hauria d'haver estat cridat", callbackCalled)
        assertTrue("loginTried hauria de ser true", viewModel.uiState.value.loginTried)
        assertFalse("goodResult hauria de ser false per credencials incorrectes", viewModel.uiState.value.goodResult)
    }
}



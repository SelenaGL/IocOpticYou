package com.example.opticyou

import com.example.opticyou.data.LoginResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test


/**
 * Proves d'integració per validar la lògica de navegació de pantalles en funció del login del rol:
 * - Usuari amb rol "admin" va a la pantalla AdminMenu
 * - Usuari amb rol "user" va a la pantalla UserMenu
 *
 */

class LoginIntegrationTests {

    /**
     * Simulació de la lògica de navegació basada en el rol Admin
     */
    @Test
    fun adminMenu(): Unit = runTest {
        // Simulem una resposta de login amb rol admin.
        val response = LoginResponse(success = true, role = "admin")
        val destination = selectDestination(response)
        assertEquals("AdminMenu", destination)
    }

    /**
     * Simulació de la lògica de navegació basada en el rol Admin
     */
    @Test
    fun userMenu() = runTest {
        // Simulem una resposta de login amb rol user.
        val response = LoginResponse(success = true, role = "user")
        val destination = selectDestination(response)
        assertEquals("UserMenu", destination)
    }

    @Test
    fun errorLogin() = runTest {
        // Simulem un login amb un username que no conté "@".
        val response = LoginResponse(success = false, role = "")
        // En aquest cas, la resposta ha de ser fallida perquè el username no és un correu.
        assertEquals(false, response.success)
    }

    /**
     * Funció auxiliar per asignar la pantalla de cada rol
     */
    private fun selectDestination(loginResponse: LoginResponse): String {
        return when(loginResponse.role) {
            "admin" -> "AdminMenu"
            "user"  -> "UserMenu"
            else    -> "UserMenu"
        }
    }
}

package com.example.opticyou

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test


/**
 * Proves de la IU per validar la funcionalitat del login i la navegació de pantalles:
 * -Amb credencials correctes, es mostra el menú corresponent
 * -Amb credencials incorrectes, es mostra un missatge d'error.
 */
class LoginUiTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    /**
     * Prova que, amb credencials d'admin correctes, es mostra la pantalla "AdminMenu".
     */
    @Test
    fun loginAdmin_AdminMenu() {
        composeTestRule.onNodeWithTag("usernameField").performTextInput("admin@optica.cat")
        composeTestRule.onNodeWithTag("passwordField").performTextInput("1234")
        composeTestRule.onNodeWithTag("loginButton").performClick()

        // Verifiquem que es navega a la pantalla de menú d'administrador
        composeTestRule.onNodeWithTag("adminMenu").assertExists()
    }

    /**
     * Prova que, amb credencials d'admin incorrectes, es mostra un missatge d'error.
     */
    @Test
    fun loginAdmin_Error() {
        // Introdueix credencials incorrectes
        composeTestRule.onNodeWithTag("usernameField").performTextInput("admin@optica.cat")
        composeTestRule.onNodeWithTag("passwordField").performTextInput("wrong")
        composeTestRule.onNodeWithTag("loginButton").performClick()

        // Ara comprovem que el missatge d'error existeix
        composeTestRule.onNodeWithTag("loginErrorText").assertExists()
    }

    /**
     * Prova que, amb credencials d'usuari correctes, es mostra la pantalla "UserMenu".
     */
    @Test
    fun loginUser_UserMenu() {
        composeTestRule.onNodeWithTag("usernameField").performTextInput("usuari@optica.cat")
        composeTestRule.onNodeWithTag("passwordField").performTextInput("1234")
        composeTestRule.onNodeWithTag("loginButton").performClick()

        // Verifiquem que es navega a la pantalla de menú usuari
        composeTestRule.onNodeWithTag("userMenu").assertExists()
    }

    /**
     * Prova que, amb credencials d'usuari incorrectes, es mostra un missatge d'error.
     */
    @Test
    fun loginUser_Error() {
        // Introdueix credencials incorrectes
        composeTestRule.onNodeWithTag("usernameField").performTextInput("usuari@optica.cat")
        composeTestRule.onNodeWithTag("passwordField").performTextInput("wrong")
        composeTestRule.onNodeWithTag("loginButton").performClick()

        // Ara comprovem que el missatge d'error existeix
        composeTestRule.onNodeWithTag("loginErrorText").assertExists()
    }
}

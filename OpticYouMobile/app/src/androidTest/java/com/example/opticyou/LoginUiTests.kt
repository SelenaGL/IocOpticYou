import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.opticyou.MainActivity
import org.junit.Rule
import org.junit.Test

/**
 * Proves de la IU per validar la funcionalitat del login i la navegació de pantalles.
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

        // ✅ Espera per donar temps a la navegació
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("adminMenu").assertExists()
    }

    /**
     * Prova que, amb credencials d'admin incorrectes, es mostra un missatge d'error.
     */
    @Test
    fun loginAdmin_Error() {
        composeTestRule.onNodeWithTag("usernameField").performTextInput("admin@optica.cat")
        composeTestRule.onNodeWithTag("passwordField").performTextInput("wrong")
        composeTestRule.onNodeWithTag("loginButton").performClick()

        // ✅ Espera per assegurar que el missatge d'error es mostra
        composeTestRule.waitForIdle()
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

        // ✅ Espera per donar temps a la navegació
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("userMenu").assertExists()
    }

    /**
     * Prova que, amb credencials d'usuari incorrectes, es mostra un missatge d'error.
     */
    @Test
    fun loginUser_Error() {
        composeTestRule.onNodeWithTag("usernameField").performTextInput("usuari@optica.cat")
        composeTestRule.onNodeWithTag("passwordField").performTextInput("wrong")
        composeTestRule.onNodeWithTag("loginButton").performClick()

        // ✅ Espera per assegurar que el missatge d'error es mostra
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("loginErrorText").assertExists()
    }
}
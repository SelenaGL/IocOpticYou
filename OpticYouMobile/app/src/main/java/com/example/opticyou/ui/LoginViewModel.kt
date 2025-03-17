package com.example.opticyou.ui

import androidx.lifecycle.viewModelScope
import com.example.opticyou.communications.ServerRequests
import com.example.opticyou.data.LoginResponse
import com.example.opticyou.data.LoginUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * ViewModel que gestiona el procés d'inici de sessió.
 *
 */
class LoginViewModel(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
    private val serverRequests: ServerRequests = ServerRequests // Permet substituir per un Fake en tests
) : IOViewModel() {

    protected val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun setLoginTried(tried: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                loginTried = tried
            )
        }
    }

    /**
     * Realitza el procés de login.
     *
     * @param username Nom d'usuari.
     * @param password Contrasenya.
     * @param onSuccess Resultat que es crida si el login és correcte.
     */
    fun doLogin(username: String, password: String, onSuccess: (LoginResponse) -> Unit) {
        println("doLogin() cridat amb: $username - $password")  // Debug log

        viewModelScope.launch(ioDispatcher) {
            val response = try {
                serverRequests.login(username, password)
            } catch (e: Exception) {
                println("Error a ServerRequests.login(): ${e.message}")  // Debug log
                null
            }
            withContext(mainDispatcher) {
                println("Resposta del servidor: $response")

                // Assegurem-nos que actualitza el `StateFlow`
                _uiState.value = _uiState.value.copy(
                    loginTried = true,
                    goodResult = response?.success == true
                )

                println("Estat actualitzat: loginTried=${_uiState.value.loginTried}, goodResult=${_uiState.value.goodResult}")

                if (response != null && response.success) {
                    onSuccess(response)
                } else {
                    onSuccess(LoginResponse(success = false, token = "", rol = ""))
                }
            }
        }
    }
}
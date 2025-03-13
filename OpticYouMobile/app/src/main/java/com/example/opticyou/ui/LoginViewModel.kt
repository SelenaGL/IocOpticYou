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
 * Conté el mètode per iniciar sessió al servidor i obté el resultat.
 *
 */
class LoginViewModel(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
) : IOViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun setLoginTried(tried: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                loginTried = tried
            )
        }
    }

    /**
     * Inici de sessió al serviro
     */
    fun doLogin(username: String, password: String, onSuccess: (LoginResponse) -> Unit) {
        println("doLogin() cridat amb: $username - $password")  // Debug log

        viewModelScope.launch(Dispatchers.IO) {
            val response = try {
                ServerRequests.login(username, password)
            } catch (e: Exception) {
                println("Error a ServerRequests.login(): ${e.message}")  // Debug log
                null
            }
            withContext(Dispatchers.Main) {
                println("🔍 Resposta del servidor: $response")

                if (response != null && response.success) {
                    onSuccess(response)
                } else {
                    onSuccess(LoginResponse(success = false, token = "", role = ""))
                }
            }
        }
    }

}
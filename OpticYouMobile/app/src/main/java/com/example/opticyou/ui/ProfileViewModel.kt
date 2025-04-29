package com.example.opticyou.ui


import androidx.lifecycle.viewModelScope
import com.example.opticyou.communications.network.ClientServerCommunication
import com.example.opticyou.data.Client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel que gestiona les operacions CRUD per a clients.
 */
class ProfileViewModel : IOViewModel() {

    private val _client = MutableStateFlow<Client?>(null)
    val client: StateFlow<Client?> = _client

    private var authToken: String? = null
    private var userId: Long = 0L

    /**
     * Estableix el token i l'ID del client autenticat per a operacions self-service.
     */
    fun setAuthData(token: String, id: Long) {
        authToken = token
        userId = id
    }

    /**
     * Carrega el perfil del servidor
     *
     * @param onResult rep un booleà indicant l'èxit.
     */
    fun loadProfile(onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            val token = authToken ?: return@launch onResult(false)
            val client = withContext(Dispatchers.IO) {
                ClientServerCommunication.getClientById(userId, token)
            }
            if (client != null) {
                _client.value = client
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    /**
     * Actualitza les dades d'un client existent.
     *
     * @param updatedClient Client amb les noves dades.
     * @param onResult rep un booleà indicant l'èxit.
     */
    fun updateProfile(
        updatedClient: Client,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val token = authToken ?: return@launch onResult(false)
            val upClient = withContext(Dispatchers.IO) {
                ClientServerCommunication.updateSelf(token, updatedClient)
            }
            if (upClient) _client.value = updatedClient
            onResult(upClient)
        }
    }

    /**
     * Elimina un client.
     *
     * @param onResult rep un booleà indicant l'èxit.
     */
    fun deleteProfile(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val token = authToken ?: return@launch onResult(false)
            val delClient = withContext(Dispatchers.IO) {
                ClientServerCommunication.deleteSelf(token)
            }
            if (delClient) _client.value = null
            onResult(delClient)
        }
    }
}
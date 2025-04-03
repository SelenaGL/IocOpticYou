package com.example.opticyou.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.opticyou.communications.network.ClientServerCommunication
import com.example.opticyou.data.Client
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona les operacions CRUD per a clients.
 */
class ClientViewModel : ViewModel() {

    private val _clients = MutableStateFlow<List<Client>>(emptyList())
    val clients: StateFlow<List<Client>> = _clients

    private var authToken: String? = null

    /**
     * Estableix el token d'autenticació per a les peticions.
     *
     * @param token El token d'autenticació.
     */
    fun setAuthToken(token: String) {
        authToken = token
    }

    /**
     * Carrega la llista de clients des del servidor.
     */
    fun loadClients() {
        val token = authToken ?: return
        viewModelScope.launch {
            val clientsFromServer = ClientServerCommunication.getClients(token)
            if (clientsFromServer != null) {
                _clients.value = clientsFromServer
            }
        }
    }

    /**
     * Crea un nou client.
     *
     * @param nom Nom del client.
     * @param email Email del client.
     * @param contrasenya Contrasenya del client.
     * @param telefon Telèfon del client.
     * @param sexe Sexe del client.
     * @param dataNaixament Data de naixement.
     * @param clinicaId ID de la clínica.
     * @param historialId ID del historial.
     * @param onResult Callback que rep un booleà indicant l'èxit.
     */
    fun createClient(
        nom: String,
        email: String,
        contrasenya: String,
        telefon: String,
        sexe: String,
        dataNaixament: String,
        clinicaId: Long,
        historialId: Long,
        onResult: (Boolean) -> Unit
    ) {
        val token = authToken ?: ""
        // Crea un nou client (l'id es crea al servidor)
        val newClient = Client(
            idUsuari = null,
            nom = nom,
            email = email,
            contrasenya=contrasenya,
            telefon = telefon,
            sexe = sexe,
            dataNaixament = dataNaixament,
            clinicaId = clinicaId,
            historialId = historialId
        )
        viewModelScope.launch {
            val success = ClientServerCommunication.createClient(token, newClient)
            onResult(success)
            if (success) {
                // Actualitza la llista de clients després de crear-ne un de nou
                loadClients()
            }
        }
    }

    /**
     * Actualitza les dades d'un client existent.
     *
     * @param updatedClient Client amb les noves dades.
     * @param onResult Callback que rep un booleà indicant l'èxit.
     */
    fun updateClient(updatedClient: Client, onResult: (Boolean) -> Unit) {
        val token = authToken ?: return
        viewModelScope.launch {
            val client = ClientServerCommunication.updateClient(token, updatedClient)
            if (client != null) {
                _clients.value = _clients.value.map { current ->
                    if (current.idUsuari == client.idUsuari) client else current
                }
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    /**
     * Elimina un client.
     *
     * @param client Client a eliminar.
     * @param onResult Callback que rep un booleà indicant l'èxit.
     */
    fun deleteClient(client: Client, onResult: (Boolean) -> Unit) {
        val token = authToken ?: return
        val id = client.idUsuari ?: return
        viewModelScope.launch {
            val success = ClientServerCommunication.deleteClient(id, token)
            if (success) {
                _clients.value = _clients.value.filter { it.idUsuari != client.idUsuari }
            }
            onResult(success)
        }
    }
}
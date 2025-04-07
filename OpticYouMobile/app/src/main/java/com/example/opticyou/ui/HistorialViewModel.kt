package com.example.opticyou.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.opticyou.communications.network.ClientServerCommunication
import com.example.opticyou.communications.network.HistorialServerCommunication
import com.example.opticyou.data.Client
import com.example.opticyou.data.Historial
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona les operacions CRUD pels historials.
 */
class HistorialViewModel : ViewModel() {

    private val _historials = MutableStateFlow<List<Historial>>(emptyList())
    val historials: StateFlow<List<Historial>> = _historials

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
     * Carrega la llista d'historials des del servidor.
     *
     * @param onResult Callback que retorna true si la càrrega ha estat exitosa, false en cas contrari.
     */
    fun loadHistorials(onResult: (Boolean) -> Unit = {}) {
        val token = authToken ?: return onResult(false)
        viewModelScope.launch {
            val list = HistorialServerCommunication.getAllHistorial(token)
            if (list != null) {
                _historials.value = list
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    /**
     * Obté un historial pel seu identificador.
     *
     * @param id Identificador de l'historial.
     * @param onResult Callback que retorna l'objecte [Historial] obtingut o null si hi ha error.
     */
    fun getHistorialById(id: Long, onResult: (Historial?) -> Unit) {
        val token = authToken ?: return onResult(null)
        viewModelScope.launch {
            val historial = HistorialServerCommunication.getHistorialById(token, id)
            onResult(historial)
        }
    }

    /**
     * Actualitza un historial existent.
     *
     * @param updatedHistorial Objecte [Historial] amb les dades actualitzades.
     * @param onResult Callback que retorna true si l'actualització ha estat exitosa, false en cas contrari.
     */
    fun updateHistorial(updatedHistorial: Historial, onResult: (Boolean) -> Unit) {
        val token = authToken ?: return onResult(false)
        viewModelScope.launch {
            val success = HistorialServerCommunication.updateHistorial(token, updatedHistorial)
            if (success) {
                _historials.value = _historials.value.map {
                    if (it.idhistorial == updatedHistorial.idhistorial) updatedHistorial else it
                }
            }
            onResult(success)
        }
    }
}
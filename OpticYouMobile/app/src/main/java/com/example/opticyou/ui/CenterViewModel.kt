package com.example.opticyou.ui

import androidx.lifecycle.viewModelScope
import com.example.opticyou.communications.network.CenterServerCommunication
import com.example.opticyou.data.Center
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CenterViewModel:IOViewModel () {

    // Llista de centres gestionada com a StateFlow
    private val _centres = MutableStateFlow<List<Center>>(emptyList())
    val centres: StateFlow<List<Center>> = _centres

    // Variable per guardar el token d'autenticació
    private var authToken: String? = null

    // Funció per establir el token (es pot cridar des d'una pantalla d'autenticació o des d'un ViewModel global)
    fun setAuthToken(token: String) {
        authToken = token
    }

    // Carrega la llista de centres des del servidor
    fun loadCentres() {
        val token = authToken ?: return
        viewModelScope.launch {
            val centresFromServer = CenterServerCommunication.getCentres(token)
            if (centresFromServer != null) {
                _centres.value = centresFromServer
            }
        }
    }

    // Afegeix un nou centre
    fun addCentre(name: String, address: String) {
        val token = authToken ?: return
        viewModelScope.launch {
            // L'id es pot establir a 0 perquè el servidor pot assignar-ne un
            val newCentre = Center(id = 0, name = name, address = address)
            val addedCentre = CenterServerCommunication.addCentre(token, newCentre)
            addedCentre?.let {
                _centres.value = _centres.value + it
            }
        }
    }

    // Actualitza un centre existent
    fun updateCentre(updatedCentre: Center) {
        val token = authToken ?: return
        viewModelScope.launch {
            val centre = CenterServerCommunication.updateCentre(token, updatedCentre)
            centre?.let {
                _centres.value = _centres.value.map { current ->
                    if (current.id == it.id) it else current
                }
            }
        }
    }

    // Elimina un centre
    fun deleteCentre(centre: Center) {
        val token = authToken ?: return
        viewModelScope.launch {
            val success = CenterServerCommunication.deleteCentre(token, centre.id)
            if (success) {
                _centres.value = _centres.value.filter { it.id != centre.id }
            }
        }
    }
}
package com.example.opticyou.ui

import androidx.lifecycle.viewModelScope
import com.example.opticyou.communications.network.CenterServerCommunication
import com.example.opticyou.data.Center
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


/**
 * ViewModel que gestiona les operacions relacionades amb els centres.
 */
class CenterViewModel : IOViewModel() {

    // Llista de centres gestionada com a StateFlow
    private val _centres = MutableStateFlow<List<Center>>(emptyList())
    val centres: StateFlow<List<Center>> = _centres

    // Variable per guardar el token d'autenticació
    private var authToken: String? = null

    /**
     * Funció per establir el token per a les peticions al servidor.
     *
     * @param token Token d'autenticació obtingut després de l'inici de sessió.
     */
    fun setAuthToken(token: String) {
        authToken = token
    }

    /**
     * Carrega la llista de centres des del servidor.
     */
    fun loadCentres(onResult: (Boolean) -> Unit) {
        val token = authToken
            ?: return onResult(false) //Si no hi ha token d'autenticació, no es realitza cap operació.
        viewModelScope.launch {
            val centresServer = CenterServerCommunication.getCentres(token)
            if (centresServer != null) {
                _centres.value = centresServer
                println("Centres carregats correctament")
                onResult(true)
            } else {
                println("Error al carregar els centres")
                onResult(false)
            }
        }
    }

    /**
     * Afegeix un nou centre al servidor.
     *
     */
    fun addClinica(
        nom: String,
        direccio: String,
        telefon: String,
        horari_opertura: String,
        horari_tancament: String,
        email: String,
        onResult: (Boolean) -> Unit // per notificar el resultat
    ) {
        val token = authToken ?: ""
        println("Token utilitzat per la creació: $token")

        // L'id inicialmente s'estableix a 0 perquè el servidor li assignarà un
        val newCenter = Center(
            idClinica = 0,
            nom = nom,
            direccio = direccio,
            telefon = telefon,
            horari_opertura = horari_opertura,
            horari_tancament = horari_tancament,
            email = email,
        )
        viewModelScope.launch {
            val addedCentre = CenterServerCommunication.createClinica(token, newCenter)
            if (addedCentre) {
                println("Clínica creada correctament")
            } else {
                println("Error en la creació de la clínica")
            }
            onResult(addedCentre)
        }
    }

    /**
     * Actualitza les dades d'un centre existent.
     *
     * @param updatedCentre Centre amb les dades actualitzades.
     */
    fun updateClinica(updatedCentre: Center, onResult: (Boolean) -> Unit) {
        // Validem que els camps obligatoris no siguin buits
        if (updatedCentre.nom.isBlank() || updatedCentre.email.isBlank() || updatedCentre.telefon.isBlank()) {
            onResult(false)
            return
        }
        val token = authToken ?: return onResult(false)
        viewModelScope.launch {
            val result = CenterServerCommunication.updateClinica(token, updatedCentre)
            if (result != null && result == "Clínica actualitzada correctament") {
                _centres.value = _centres.value.map { current ->
                    if (current.idClinica == updatedCentre.idClinica) updatedCentre else current
                }
                onResult(true)
            } else {
                println("Error en l'actualització: $result")
                onResult(false)
            }
        }
    }


    /**
     * Elimina un centre del servidor.
     *
     * @param centre Centre que es vol eliminar.
     */
    fun deleteClinica(centre: Center, onResult: (Boolean) -> Unit) {
        val token = authToken ?: return onResult(false)
        viewModelScope.launch {
            val id = centre.idClinica ?: return@launch
            val success = CenterServerCommunication.deleteClinica(id, token)
            if (success) {
                _centres.value = _centres.value.filter { it.idClinica != centre.idClinica }
                println("Centre eliminat correctament")
            } else {
                println("Error en l'eliminació del centre")
            }
            onResult(success)
        }
    }
}
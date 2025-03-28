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
class CenterViewModel:IOViewModel () {

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
    fun loadCentres() {
        val token = authToken ?: return //Si el token d'autenticació no està establert, la funció no realitza cap operació.
        viewModelScope.launch {
            val centresFromServer = CenterServerCommunication.getCentres(token)
            if (centresFromServer != null) {
                _centres.value = centresFromServer
            }
        }
    }

    /**
     * Afegeix un nou centre al servidor.
     *
     */
    fun addClinica(nom: String,
                   direccio: String,
                   telefon: String,
                   horari_opertura: String,
                   horari_tancament: String,
                   email: String
    ) {
        val token = authToken ?: ""
        println("Token utilitzat per la creació: $token")
        //val token = authToken ?: return

            // L'id inicialmente s'estableix a 0 perquè el servidor li assignarà un
            val newCenter = Center(
                idClinica = null,
                nom = nom,
                direccio = direccio,
                telefon = telefon,
                horari_opertura = horari_opertura,
                horari_tancament = horari_tancament,
                email = email,
                token = token
            )
        viewModelScope.launch {
            val addedCentre = CenterServerCommunication.createClinica(token, newCenter)
            if (addedCentre) {
                println("Clínica creada correctament")
            } else {
                println("Error en la creació de la clínica")
            }
        }
    }

    /**
     * Actualitza les dades d'un centre existent.
     *
     * @param updatedCentre Centre amb les dades actualitzades.
     */
    fun updateClinica(updatedCentre: Center) {
        viewModelScope.launch {
            val centre = CenterServerCommunication.updateClinica(updatedCentre)
            centre?.let {
                _centres.value = _centres.value.map { current ->
                    if (current.idClinica == it.idClinica) it else current
                }
            }
        }
    }

    /**
     * Elimina un centre del servidor.
     *
     * @param centre Centre que es vol eliminar.
     */
//    fun deleteClinica(centre: Center) {
//        val token = authToken ?: return
//        viewModelScope.launch {
//            val success = CenterServerCommunication.deleteClinica(centre.idClinica, token)
//            if (success) {
//                _centres.value = _centres.value.filter { it.idClinica != centre.idClinica }
//            }
//        }
//    }
}
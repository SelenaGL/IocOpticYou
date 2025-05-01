package com.example.opticyou.ui


import androidx.lifecycle.viewModelScope
import com.example.opticyou.communications.network.ClientServerCommunication
import com.example.opticyou.communications.network.TreballadorServerCommunication
import com.example.opticyou.data.Treballador
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona les operacions CRUD per als treballador.
 */
class TreballadorViewModel : IOViewModel() {

    private val _treballadors = MutableStateFlow<List<Treballador>>(emptyList())
    val treballadors: StateFlow<List<Treballador>> = _treballadors

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
     * Carrega la llista de treballadors des del servidor.
     */
    fun loadTreballadors(onResult: (Boolean)->Unit = {}) {
        val token = authToken ?: return onResult(false)
        viewModelScope.launch {
            val list = TreballadorServerCommunication.getAll(token)
            if (list != null) {
                _treballadors.value = list
                onResult(true)
            } else onResult(false)
        }
    }

    /**
     * Crea un nou treballador.
     *
     * @param Dades treballador.
     * @param onResult rep un booleà indicant l'èxit.
     */
    fun addTreballador(

        nom: String,
        email: String,
        especialitat: String,
        estat: String,
        iniciJornada: String,
        diesJornada: String,
        fiJornada: String,
        clinicaId: Long,

        onResult: (Boolean) -> Unit
    ) {
        val token = authToken ?: ""
        // Crea un nou client (l'id es crea al servidor)
        val newTreballador = Treballador(
            idUsuari = null,
            nom = nom,
            email = email,
            especialitat = especialitat,
            estat = estat,
            iniciJornada = iniciJornada,
            diesJornada = diesJornada,
            fiJornada = fiJornada,
            clinicaId = clinicaId,

        )
        viewModelScope.launch {
            val success = TreballadorServerCommunication.create(token, newTreballador)
            onResult(success)
        }
    }



    /**
     * Actualitza les dades d'un treballador existent.
     *
     * @param updatedTreballador Treballador amb les noves dades.
     * @param onResult rep un booleà indicant l'èxit.
     */
    fun updateTreballador(updatedTreballador: Treballador, onResult: (Boolean)->Unit) {
        val token = authToken ?: return onResult(false)
        viewModelScope.launch {
            val ok = TreballadorServerCommunication.update(token, updatedTreballador)
            if (ok) _treballadors.value = _treballadors.value.map { if (it.idUsuari == updatedTreballador.idUsuari) updatedTreballador else it }
            onResult(ok)
        }
    }

    /**
     * Elimina un treballador.
     *
     * @param treballador Treballador a eliminar.
     * @param onResult rep un booleà indicant l'èxit.
     */
    fun deleteTreballador(treballador: Treballador, onResult: (Boolean)->Unit) {
        val token = authToken ?: return onResult(false)
        viewModelScope.launch {
            val ok = TreballadorServerCommunication.delete(token, treballador.idUsuari!!)
            if (ok) _treballadors.value = _treballadors.value.filter { it.idUsuari != treballador.idUsuari }
            onResult(ok)
        }
    }
}
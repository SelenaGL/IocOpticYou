package com.example.opticyou.ui

import com.example.opticyou.communications.network.DiagnosticServerCommunication
import com.example.opticyou.data.Diagnostic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona les operacions CRUD per als diagnòstics.
 */
class DiagnosticViewModel: IOViewModel() {

    private val _diagnostics = MutableStateFlow<List<Diagnostic>>(emptyList())
    val diagnostics: StateFlow<List<Diagnostic>> = _diagnostics

    private var authToken: String? = null

    /**
     * Estableix el token d'autenticació per a les peticions.
     *
     * @param token El token d'autenticació.
     */
    fun setAuthToken(token: String) {
        authToken = token
    }

    /** Carrega tots els diagnòstics des del servidor. */
    fun loadDiagnostics(historialId: Long, onResult: (Boolean) -> Unit = {}) {
        val token = authToken ?: return onResult(false)
        viewModelScope.launch {
            val list = DiagnosticServerCommunication.getDiagnostics(token)
            if (list != null) {
                _diagnostics.value = list
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    /** Carrega un diagnòstic pel seu ID */
    fun loadDiagnosticById(id: Long, onResult: (Boolean) -> Unit = {}) {
        val token = authToken ?: return onResult(false)
        viewModelScope.launch {
            val list = DiagnosticServerCommunication.getDiagnosticById(id ,token)
            if (list != null) {
                _diagnostics.value = list
                onResult(true)
            } else onResult(false)
        }
    }

    /** Crea un nou diagnòstic. */
    fun addDiagnostic(
        descripcio: String,
        date: String,
        historialId: Long,
        onResult: (Boolean) -> Unit
    ) {
        val token = authToken ?: return onResult(false)
        val newDiag = Diagnostic(
            iddiagnostic = 0L,
            descripcio = descripcio,
            date = date,
            historialId = historialId
        )
        viewModelScope.launch {
            val success = DiagnosticServerCommunication.createDiagnostic(token, newDiag)
            onResult(success)
        }
    }

    /** Actualitza un diagnòstic existent. */
    fun updateDiagnostic(
        updated: Diagnostic,
        onResult: (Boolean) -> Unit
    ) {
        val token = authToken ?: return onResult(false)
        viewModelScope.launch {
            val success = DiagnosticServerCommunication.updateDiagnostic(token, updated)
            onResult(success)
            if (success) {
                _diagnostics.value = _diagnostics.value.map {
                    if (it.iddiagnostic == updated.iddiagnostic) updated else it
                }
            }
        }
    }

    /** Elimina un diagnòstic. */
    fun deleteDiagnostic(
        diag: Diagnostic,
        onResult: (Boolean) -> Unit
    ) {
        val token = authToken ?: return onResult(false)
        viewModelScope.launch {
            val success =
                diag.iddiagnostic?.let { DiagnosticServerCommunication.deleteDiagnostic(it, token) }
            if (success != null) {
                onResult(success)
            }
            if (success == true) {
                _diagnostics.value =
                    _diagnostics.value.filter { it.iddiagnostic != diag.iddiagnostic }
            }
        }
    }
}

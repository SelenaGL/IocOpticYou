package com.example.opticyou.ui

import com.example.opticyou.data.Historial
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DiagnosticViewModel {

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
}

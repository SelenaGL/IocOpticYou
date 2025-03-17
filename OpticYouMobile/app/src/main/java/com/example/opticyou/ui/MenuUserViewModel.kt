package com.example.opticyou.ui

import androidx.lifecycle.viewModelScope
import com.example.opticyou.communications.ServerRequests
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona el procés de logout pels usuaris.
 */
class MenuUserViewModel : IOViewModel() {

    /**
     * Realitza el procés de logout.
     *
     * @param token Token de la sessió.
     */
    fun logout(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val success = ServerRequests.logout(token)
            println("Resultat de logout: $success")
        }
    }
}
package com.example.opticyou.ui

import androidx.lifecycle.viewModelScope
import com.example.opticyou.data.User
import com.example.opticyou.communications.ServerRequests
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
  * Holds the method to do "logout" from the server and fills the operation result,
 * which can be retrieved from the screen
 */
class MenuUserViewModel : IOViewModel() {

    fun logout(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val success = ServerRequests.logout(token)
            println("Resultat de logout: $success")
        }
    }
}
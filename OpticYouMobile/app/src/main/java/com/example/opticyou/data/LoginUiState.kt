
package com.example.opticyou.data

/**
 * Data class that represents the current UI state in terms of the fact if it has been the login tried
 */
data class LoginUiState(
    /** Has the login been tried? **/
    var loginTried:Boolean=false,
    val goodResult: Boolean=false
)

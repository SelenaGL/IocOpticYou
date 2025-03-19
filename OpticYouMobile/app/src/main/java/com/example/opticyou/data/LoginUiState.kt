
package com.example.opticyou.data

/**
 * Classe de dades que representa l'estat actual de la IU en relació amb el procés de login.
 */
data class LoginUiState(
    /**  Intent fer login. Per defecte és `false` **/
    var loginTried:Boolean=false,
    /**  Resultat de login correcte. Per defecte és `false` **/
    val goodResult: Boolean=false
)

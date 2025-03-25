package com.example.opticyou.communications.network

import com.example.opticyou.data.Center
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse


/**
 * Objecte que gestiona la comunicació amb el servidor per a les operacions relacionades amb els centres.
 */
object CenterServerCommunication {
    /**
     * Obté una llista de centres des del servidor.
     *
     * @param token Token necessari per a la petició.
     * @return llista de centres o null en cas que hi hagi un error.
     */
    suspend fun getCentres(token: String): List<Center>? = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.instance.getCentres(token).awaitResponse()
            if (response.isSuccessful) {
                response.body()
            } else {
                println("Error a l'obtenir els centres: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            println("Excepció a getCentres: ${e.message}")
            null
        }
    }

    /**
     * Afegeix un nou centre al servidor.
     *
     * @param token Token necessari per a la petició.
     * @param centre Objecte [Center] que conté la info del centre que volem afegir.
     * @return El centre afegit o null en cas d'error.
     */
    suspend fun addCentre(token: String, centre: Center): Center? = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.instance.addCentre(token, centre).awaitResponse()
            if (response.isSuccessful) {
                response.body()
            } else {
                println("Error a l'afegir el centre: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            println("Excepció a addCentre: ${e.message}")
            null
        }
    }

    /**
     * Actualitza les dades d'un centre existent al servidor.
     *
     * @param token Token necessari per a la petició.
     * @param centre Objecte [Center] que conté les dades actualitzades del centre.
     * @return El centre actualitzat o null en cas d'error.
     */
    suspend fun updateCentre(token: String, centre: Center): Center? = withContext(Dispatchers.IO) {
        try {
            val response =
                RetrofitClient.instance.updateCentre(token, centre.idclinica, centre).awaitResponse()
            if (response.isSuccessful) {
                response.body()
            } else {
                println("Error a l'actualitzar el centre: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            println("Excepció a updateCentre: ${e.message}")
            null
        }
    }

    /**
     * Elimina un centre del servidor.
     *
     * @param token Token necessari per a la petició.
     * @param id Identificador del centre que es vol eliminar.
     * @return true si la petició s'ha realitzat correctament o false en cas contrari.
     */
    suspend fun deleteCentre(token: String, id: Int): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.instance.deleteCentre(token, id).awaitResponse()
            if (response.isSuccessful) {
                true
            } else {
                println("Error a l'eliminar el centre: ${response.code()} - ${response.message()}")
                false
            }
        } catch (e: Exception) {
            println("Excepció a deleteCentre: ${e.message}")
            false
        }
    }
}
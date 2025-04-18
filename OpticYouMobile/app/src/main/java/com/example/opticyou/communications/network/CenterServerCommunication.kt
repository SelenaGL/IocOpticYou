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
            val bearerToken = "Bearer $token"
            val response = RetrofitClient.instance.getAllClinicas(bearerToken).awaitResponse()
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
     * Crea un nou centre al servidor.
     *
     * @param token Token necessari per a la petició.
     * @param centre Objecte [Center] que conté la info del centre que volem afegir.
     * @return True si la creació s'ha realitzat correctament, false en cas d'error.
     */
    suspend fun createClinica(token:String, center: Center): Boolean = withContext(Dispatchers.IO) {
        try {
            val bearerToken ="Bearer $token"
            val response = RetrofitClient.instance.createClinica(bearerToken, center).awaitResponse()
            println("Resposta del servidor en la creació: ${response.code()}")
            response.isSuccessful
        } catch (e: Exception) {
            println("Error en la creació de clínica: ${e.message}")
            false
        }
    }


    /**
     * Actualitza les dades d'un centre existent al servidor.
     *
     * @param token Token necessari per a la petició.
     * @param centre Objecte [Center] que conté les dades actualitzades del centre.
     * @return missatge retornat pel servidor
     */
    suspend fun updateClinica(token: String, centre: Center): String? = withContext(Dispatchers.IO) {
        try {
            val bearerToken = "Bearer $token"
            val response = RetrofitClient.instance.updateClinica(bearerToken, centre).awaitResponse()
            if (response.isSuccessful) {
                response.body()  // Retorna el missatge "Clínica actualitzada correctament"
            } else {
                println("Error a l'actualitzar el centre: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            println("Excepció a updateClinica: ${e.message}")
            null
        }
    }

    /**
     * Elimina un centre del servidor.
     *
     * @param token Token necessari per a la petició.
     * @param id Identificador del centre que es vol eliminar.
     * @return True si la petició s'ha realitzat correctament, false en cas contrari.
     */
    suspend fun deleteClinica(id: Long, token: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val bearerToken = "Bearer $token"
            val response = RetrofitClient.instance.deleteClinica(id, bearerToken).awaitResponse()
            if (response.isSuccessful) {
                val body = response.body()?.trim()  // Eliminem espais en blanc
                body == "Clínica eliminada correctament"
            } else {
                println("Error a l'eliminar el centre: ${response.code()} - ${response.message()}")
                false
            }
        } catch (e: Exception) {
            println("Excepció a deleteClinica: ${e.message}")
            false
        }
    }
}

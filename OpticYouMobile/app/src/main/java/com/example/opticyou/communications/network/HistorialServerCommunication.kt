package com.example.opticyou.communications.network

import com.example.opticyou.data.Center
import com.example.opticyou.data.Client
import com.example.opticyou.data.Historial
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse


/**
 * Objecte que gestiona la comunicació amb el servidor per a les operacions relacionades amb els historials.
 */
object HistorialServerCommunication {
    /**
     * Obté la llista d'historials.
     *
     * @param token Token d'autenticació.
     * @return Llista de [Historial] o null si hi ha error.
     */
    suspend fun getAllHistorial(token: String): List<Historial>? = withContext(Dispatchers.IO) {
        try {
            val bearerToken = "Bearer $token"
            val response = RetrofitClient.instance.getAllHistorial(bearerToken).awaitResponse()
            if (response.isSuccessful) {
                response.body()
            } else {
                println("Error en obtenir els historials: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            println("Excepció en getAllHistorial: ${e.message}")
            null
        }
    }

    /**
     * Obté un historial pel seu identificador.
     *
     * @param token Token d'autenticació.
     * @param id Identificador de l'historial.
     * @return L'objecte [Historial] o null si hi ha error.
     */
    suspend fun getHistorialById(token: String, id: Long): Historial? = withContext(Dispatchers.IO) {
        try {
            val bearerToken = "Bearer $token"
            val response = RetrofitClient.instance.getHistorialById(id, bearerToken).awaitResponse()
            if (response.isSuccessful) {
                response.body()
            } else {
                println("Error en obtenir l'historial per ID: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            println("Excepció en getHistorialById: ${e.message}")
            null
        }
    }

    /**
     * Actualitza un historial existent.
     *
     * @param token Token d'autenticació.
     * @param historial Objecte [Historial] amb les dades actualitzades.
     * @return True si l'actualització ha estat exitosa, false en cas contrari.
     */
    suspend fun updateHistorial(token: String, historial: Historial): Boolean = withContext(Dispatchers.IO) {
        try {
            val bearerToken = "Bearer $token"
            val response = RetrofitClient.instance.updateHistorial(bearerToken, historial).awaitResponse()
            if (response.isSuccessful) {
                true
            } else {
                println("Error en actualitzar l'historial: ${response.code()} - ${response.message()}")
                false
            }
        } catch (e: Exception) {
            println("Excepció en updateHistorial: ${e.message}")
            false
        }
    }
    }
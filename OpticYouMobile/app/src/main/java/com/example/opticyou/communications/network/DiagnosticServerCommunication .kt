package com.example.opticyou.communications.network

import com.example.opticyou.data.Center
import com.example.opticyou.data.Client
import com.example.opticyou.data.Diagnostic
import com.example.opticyou.data.Historial
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse


/**
 * Objecte que gestiona la comunicació amb el servidor per a les operacions relacionades amb els diagnòstics.
 */
object DiagnosticServerCommunication  {
    /**
     * Obté la llista de diagnòstics.
     *
     * @param token Token d'autenticació.
     * @return Llista de [Diagnostic] o null si hi ha error.
     */
    suspend fun getDiagnostics(token: String): List<Diagnostic>? = withContext(Dispatchers.IO) {
        try {
            val bearer = "Bearer $token"
            val response = RetrofitClient.instance.getAllDiagnostics(bearer).awaitResponse()
            if (response.isSuccessful) {
                response.body()
            } else {
                println("Error al carregar diagnòstics: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            println("Excepció a getDiagnostics: ${e.message}")
            null
        }
    }

    /**
     * Obté un historial pel seu identificador.
     *
     * @param id Identificador del diagnòstic.
     * @param token Token d'autenticació.
     * @return L'objecte [Diagnostic] o null si hi ha error.
     */
    suspend fun getDiagnosticById(id: Long, token: String): List<Diagnostic>? = withContext(Dispatchers.IO) {
        try {
            val bearer = "Bearer $token"
            val response = RetrofitClient.instance.getDiagnosticsByHistorial(id, bearer).awaitResponse()
            if (response.isSuccessful) {
                response.body()
            } else {
                println("Error al obtenir diagnòstic $id: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            println("Excepció a getDiagnosticById: ${e.message}")
            null
        }
    }

    /**
     * Crea un nou diagnòstic.
     *
     * @param token Token d'autenticació.
     * @param diagnostic Objecte [Diagnostic] amb la informació a afegir.
     * @return true si la creació ha estat exitosa, false en cas contrari.
     */
    suspend fun createDiagnostic(token: String, diagnostic: Diagnostic): Boolean = withContext(Dispatchers.IO) {
        try {
            val bearer = "Bearer $token"
            val response = RetrofitClient.instance.createDiagnostic(bearer, diagnostic).awaitResponse()
            println("Resposta del servidor en crear diagnòstic: ${response.code()}")
            response.isSuccessful
        } catch (e: Exception) {
            println("Error en crear diagnòstic: ${e.message}")
            false
        }
    }

    /**
     * Actualitza un diagnòstic existent.
     *
     * @param token Token d'autenticació.
     * @param diagnostic Objecte [Diagnostic] amb les noves dades.
     * @return True si l'actualització ha estat exitosa, false en cas contrari.
     */
    suspend fun updateDiagnostic(token: String, diagnostic: Diagnostic): Boolean = withContext(Dispatchers.IO) {
        try {
            val bearer = "Bearer $token"
            val response = RetrofitClient.instance.updateDiagnostic(bearer, diagnostic).awaitResponse()
            if (response.isSuccessful) {
                println("Diagnòstic actualitzat correctament")
                true
            } else {
                println("Error al actualitzar diagnòstic: ${response.code()} - ${response.message()}")
                false
            }
        } catch (e: Exception) {
            println("Excepció a updateDiagnostic: ${e.message}")
            false
        }
    }

    /**
     * Elimina un diagnòstic per ID.
     *
     * @param id ID del diagnòstic a eliminar.
     * @param token Token d'autenticació.
     * @return true si l’eliminació ha estat exitosa, false en cas contrari.
     */
    suspend fun deleteDiagnostic(id: Long, token: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val bearer = "Bearer $token"
            val response = RetrofitClient.instance.deleteDiagnostic(id, bearer).awaitResponse()
            if (response.isSuccessful) {
                println("Diagnòstic eliminat correctament")
                true
            } else {
                println("Error eliminar diagnòstic: ${response.code()} - ${response.message()}")
                false
            }
        } catch (e: Exception) {
            println("Excepció a deleteDiagnostic: ${e.message}")
            false
        }
    }

}
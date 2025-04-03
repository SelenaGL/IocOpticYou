package com.example.opticyou.communications.network

import com.example.opticyou.data.Center
import com.example.opticyou.data.Client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse


/**
 * Objecte que gestiona la comunicació amb el servidor per a les operacions relacionades amb els clients.
 */
object ClientServerCommunication {
    /**
     * Obté la llista de clients.
     *
     * @param token Token d'autenticació.
     * @return Llista de [Client] o null si hi ha error.
     */
    suspend fun getClients(token: String): List<Client>? = withContext(Dispatchers.IO) {
        try {
            val bearerToken = "Bearer $token"
            val response = RetrofitClient.instance.getAllClients(bearerToken).awaitResponse()
            if (response.isSuccessful) {
                response.body()
            } else {
                println("Error a l'obtenir els clients: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            println("Excepció a getClients: ${e.message}")
            null
        }
    }

    /**
     * Crea un nou client al servidor.
     *
     * @param token Token d'autenticació.
     * @param client Objecte [Client] amb les dades a afegir.
     * @return True si la creació ha estat exitosa, false en cas contrari.
     */
    suspend fun createClient(token: String, client: Client): Boolean = withContext(Dispatchers.IO) {
        try {
            val bearerToken = "Bearer $token"
            val response = RetrofitClient.instance.createClient(bearerToken, client).awaitResponse()
            println("Resposta del servidor en la creació: ${response.code()}")
            response.isSuccessful
        } catch (e: Exception) {
            println("Error en la creació del client: ${e.message}")
            false
        }
    }

    /**
     * Actualitza un client existent.
     *
     * @param token Token d'autenticació.
     * @param client Objecte [Client] amb les noves dades.
     * @return El [Client] actualitzat o null si hi ha error.
     */
    suspend fun updateClient(token: String, client: Client): Client? = withContext(Dispatchers.IO) {
        try {
            val bearerToken = "Bearer $token"
            val response = RetrofitClient.instance.updateClient(bearerToken, client).awaitResponse()
            if (response.isSuccessful) {
                // per evitar l'error de deserielització
                client
            } else {
                println("Error a l'actualitzar el client: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            println("Excepció a updateClient: ${e.message}")
            null
        }
    }

    /**
     * Elimina un client pel seu identificador.
     *
     * @param id Identificador del client.
     * @param token Token d'autenticació.
     * @return True si l'operació ha estat exitosa, false en cas contrari.
     */
    suspend fun deleteClient(id: Long, token: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val bearerToken = "Bearer $token"
            val response = RetrofitClient.instance.deleteClient(id, bearerToken).awaitResponse()
            if (response.isSuccessful) {
                true
            } else {
                println("Error a l'eliminar el client: ${response.code()} - ${response.message()}")
                false
            }
        } catch (e: Exception) {
            println("Excepció a deleteClient: ${e.message}")
            false
        }
    }
}
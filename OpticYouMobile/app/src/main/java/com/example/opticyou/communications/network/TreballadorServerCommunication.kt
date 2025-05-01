package com.example.opticyou.communications.network

import com.example.opticyou.data.Client
import com.example.opticyou.data.Treballador
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

/**
 * Objecte que gestiona la comunicació amb el servidor per a les operacions relacionades amb els treballadors.
 */
object TreballadorServerCommunication {

    /**
     * Obté la llista de clients.
     *
     * @param token Token d'autenticació.
     * @return Llista de [Treballador] o null si hi ha error.
     */
    suspend fun getAll(token: String): List<Treballador>? = withContext(Dispatchers.IO) {
        val resp = RetrofitClient.instance
            .getAllTreballadors("Bearer $token")
            .awaitResponse()
        resp.body().takeIf { resp.isSuccessful }
    }

    /**
     * Crea un nou treballador al servidor.
     *
     * @param token Token d'autenticació.
     * @param treballador Objecte [Treballador] amb les dades a afegir.
     * @return True si la creació ha estat exitosa, false en cas contrari.
     */
    suspend fun create(token: String, treballador: Treballador): Boolean = withContext(Dispatchers.IO) {
        RetrofitClient.instance
            .createTreballador("Bearer $token", treballador)
            .awaitResponse()
            .isSuccessful
    }

    /**
     * Actualitza un treballador existent.
     *
     * @param token Token d'autenticació.
     * @param treballador Objecte [Treballador] amb les noves dades.
     * @return El [Treballador] actualitzat o null si hi ha error.
     */
    suspend fun update(token: String, treballador: Treballador): Boolean = withContext(Dispatchers.IO) {
        val res = RetrofitClient.instance.updateTreballador("Bearer $token", treballador).awaitResponse()
        res.isSuccessful
    }

    /**
     * Elimina un treballador pel seu identificador.
     *
     * @param id Identificador del treballador.
     * @param token Token d'autenticació.
     * @return True si l'operació ha estat exitosa, false en cas contrari.
     */
    suspend fun delete(token: String, id: Long): Boolean = withContext(Dispatchers.IO) {
        RetrofitClient.instance.deleteTreballador(id, "Bearer $token").awaitResponse().isSuccessful
    }
}
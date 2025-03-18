package com.example.opticyou.communications.network

import com.example.opticyou.data.LoginRequest
import com.example.opticyou.data.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

/**
 * Gestiona tota la comunicació amb el servidor
 */

object ServerCommunication {

    /**
     * Mètode pel procés de login d'un usuari.
     *
     * @param username Nom d'usuari.
     * @param password Contrasenya de l'usuari.
     * @return [LoginResponse] si la petició és exitosa, `null` en cas contrari.
     */
    suspend fun login(username: String, password: String): LoginResponse? {
        println("Enviant petició de login amb $username - $password")
        return withContext(Dispatchers.IO) {
            try {
                //Intentem fer login amb el servidor real
                val response =
                    RetrofitClient.instance.login(LoginRequest(username, password)).awaitResponse()
                println("Resposta del servidor: ${response.code()} - ${response.message()}")
                println(
                    "Contingut de la resposta: ${
                        response.body()?.toString() ?: "Resposta buida"
                    }"
                )

                if (response.isSuccessful) {
                    val body = response.body()
                    body
                } else {
                    null
                }
            } catch (e: Exception) {
                println("Excepció de connexió: ${e.message}")
                null
                //println("Retornant mock data.")
                //mockLogin(username, password) // Retornem un mock si el servidor no està disponible
            }
        }
    }

//    /**
//     * Simulació de login quan el servidor no està disponible.
//     *
//     * @param username Nom d'usuari.
//     * @param password Contrasenya de l'usuari.
//     * @return [LoginResponse] simulada.
//     */
//    private fun mockLogin(username: String, password: String): LoginResponse {
//        // Si el username no conté un "@", considerem que no és vàlid
//        if (!username.contains("@")) {
//            return LoginResponse(success = false, token = "", rol = "")
//        }
//        // Admin
//        if (username == "admin@optica.cat") {
//            return if (password == "1234") {
//                LoginResponse(success = true, rol = "admin", token = "mockAdminToken")
//            } else {
//                LoginResponse(success = false, token = "", rol = "")
//            }
//        }
//        // Usuari
//        return if (password == "1234") {
//            LoginResponse(success = true, rol = "user", token = "mockUserToken")
//        } else {
//            LoginResponse(success = false, token = "", rol = "")
//        }
//    }

    /**
     * Realitza el procés de logout d'un usuari.
     *
     * @param token Token d'autenticació de l'usuari.
     * @return `true` si la petició és exitosa, `false` en cas contrari.
     */
    suspend fun logout(token: String? = null): Boolean = withContext(Dispatchers.IO) {
        if (token == null) {
            println("No hi ha token")
            return@withContext true
        }
        try {
            val response = RetrofitClient.instance.logoutString(token).awaitResponse()
            println("Resposta del servidor: ${response.code()} - ${response.message()}")
            println("Contingut de la resposta: ${response.body() ?: "null"}")
            response.isSuccessful
        } catch (e: Exception) {
            println("Error en logout: ${e.message}")
            false
//            println("Retornant mock data.")
//            mockLogout()
        }
    }

//    /**
//     * Simulació del procés de logout quan el servidor no està disponible.
//     *
//     * @return `true` per defecte.
//     */
//    private fun mockLogout(): Boolean = true

//    suspend fun queryUser(username: String): User? {
//        return withContext(Dispatchers.IO) {
//            try {
//                val response = RetrofitClient.instance.getUser(username).awaitResponse()
//                if (response.isSuccessful) response.body() else null
//            } catch (e: Exception) {
//                println("Error al recuperar usuari. Retornant mock.")
//                mockQueryUser(username)
//            }
//        }
//    }
//
//    suspend fun listUsers(): List<User>? {
//        return withContext(Dispatchers.IO) {
//            try {
//                val response = RetrofitClient.instance.getAllUsers().awaitResponse()
//                if (response.isSuccessful) response.body() else null
//            } catch (e: Exception) {
//                println("Error al llistar usuaris. Retornant mock.")
//                mockListUsers()
//            }
//        }
//    }
//
}

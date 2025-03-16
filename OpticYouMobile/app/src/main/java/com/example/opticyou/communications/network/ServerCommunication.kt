package com.example.opticyou.communications.network

import com.example.opticyou.data.User
import com.example.opticyou.data.LoginRequest
import com.example.opticyou.data.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

/**
 * Gestiona tota la comunicació amb el servidor
 */

object ServerCommunication {

    // Mètode per a fer login (real o mock si el servidor no està llest)
    suspend fun login(username: String, password: String): LoginResponse? {
        println("Enviant petició de login amb $username - $password")
        return withContext(Dispatchers.IO) {
            try {
                //Intentem fer login amb el servidor real
                val response =
                    RetrofitClient.instance.login(LoginRequest(username, password)).awaitResponse()
                println("Resposta del servidor: ${response.code()} - ${response.message()}")
                println("Contingut de la resposta: ${response.body()?.toString() ?: "Resposta buida"}")

                if (response.isSuccessful) {
                    val body = response.body()
                    body
                } else {
                    null
                }
            } catch (e: Exception) {
                println("Excepció de connexió: ${e.message}")
                println("Retornant mock data.")
                mockLogin(username, password) // Retornem un mock si el servidor no està disponible
            }
        }
    }


    // Simulació de login quan el servidor no està disponible
    private fun mockLogin(username: String, password: String): LoginResponse {
        // Si el username no conté un "@", considerem que no és vàlid
        if (!username.contains("@")) {
            return LoginResponse(success = false, token = "", rol = "")
        }
        // Admin
        if (username == "admin@optica.cat") {
            return if (password == "1234") {
                LoginResponse(success = true, rol = "admin", token = "mockAdminToken")
            } else {
                LoginResponse(success = false, token = "", rol = "")
            }
        }
        // Usuari
        return if (password == "1234") {
            LoginResponse(success = true, rol = "user", token = "mockUserToken")
        } else {
            LoginResponse(success = false, token = "", rol = "")
        }
    }

    suspend fun queryUser(username: String): User? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.instance.getUser(username).awaitResponse()
                if (response.isSuccessful) response.body() else null
            } catch (e: Exception) {
                println("Error al recuperar usuari. Retornant mock.")
                mockQueryUser(username)
            }
        }
    }

    // 🔹 Simulació de resposta per obtenir usuaris si el servidor no està disponible
    private fun mockQueryUser(username: String): User? {
        return when (username) {
            "admin@optica.cat" -> User("admin@optica.cat", "Administrador Mock")
            "user@optica.cat" -> User("user@optica.cat", "Usuari Mock")
            "100" -> null // Simulem "usuari inexistent"
            "200" -> throw Exception("Error en les comunicacions") // 🔥 Simulem un error de xarxa
            else -> User(username, "Usuari Fictici")
        }
    }

    suspend fun listUsers(): List<User>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.instance.getAllUsers().awaitResponse()
                if (response.isSuccessful) response.body() else null
            } catch (e: Exception) {
                println("Error al llistar usuaris. Retornant mock.")
                mockListUsers()
            }
        }
    }

    private fun mockListUsers(): List<User> {
        return listOf(
            User("admin@optica.cat", "Administrador Mock"),
            User("user@optica.cat", "Usuari Mock"),
            User("nou@optica.cat", "Nou Usuari Mock")
        )
    }


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
            println("Retornant mock data.")
            mockLogout()
        }
    }

    private fun mockLogout(): Boolean = true
}

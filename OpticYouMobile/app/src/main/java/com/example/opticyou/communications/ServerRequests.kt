package com.example.opticyou.communications

import com.example.opticyou.communications.network.RetrofitClient
import com.example.opticyou.data.LoginRequest
import com.example.opticyou.data.LoginResponse
import com.example.opticyou.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

/**
 * Object with functions for calling the server
 */

object ServerRequests {

    // A mutex is used to ensure that reads and writes are thread-safe.
    private val accessMutex = Mutex()

    //Validació d'usuari i contrasenya sense servidor
//    suspend fun login(username: String, password: String): LoginResponse? = accessMutex.withLock {
//        // Validació: el nom d'usuari ha de ser un correu electrònic.
//        if (!username.contains("@")) {
//            return LoginResponse(success = false, role = "")
//        }
//        // Simulem que si la contrasenya és "1234", el login és exitós.
//        // Si el nom d'usuari és "admin@optica.cat", retornem el rol "admin", en cas contrari "user".
//        return if (password == "1234") {
//            val role = if (username.equals("admin@optica.cat", ignoreCase = true)) "admin" else "user"
//            LoginResponse(success = true, role = role)
//        } else {
//            // Si la contrasenya no és "1234", el login falla.
//            LoginResponse(success = false, role = "")
//        }
//    }

    //Validació login amb servidor
    suspend fun login(username: String, password: String): LoginResponse? {
        println("🔍 Enviant petició de login amb $username - $password")  // Debug log

        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.instance.login(LoginRequest(username, password)).awaitResponse()
                println("Resposta del servidor: ${response.code()} - ${response.message()} - ${response.errorBody()?.string()}")
                println("Cos de la resposta: ${response.errorBody()?.string()}")

                if (response.isSuccessful) {
                    val body = response.body()
                    println("JSON rebut: $body")
                    println("JSON rebut: ${body?.toString()}")
                    body
                } else {
                    println("Error en la resposta: ${response.errorBody()?.string()}")  // Debug log
                    null
                }
            } catch (e: Exception) {
                println("Excepció de connexió: ${e.message}")  // Debug log
                null
            }
        }
    }


    suspend fun logout():Boolean = accessMutex.withLock {
        return (CommController.doLogout()==CommController.OK_RETURN_CODE)
    }

    suspend fun isLogged():Boolean = accessMutex.withLock {
        return CommController.isLogged
    }

    suspend fun queryUser(username:String): User? = accessMutex.withLock {
          return CommController.doQueryUser(username)
    }

    suspend fun listUsers():Array<User>? = accessMutex.withLock {
        return CommController.doListUsers()
    }

    suspend fun addUser(user:User):Boolean = accessMutex.withLock {
        return (CommController.doAddUser(user)==CommController.OK_RETURN_CODE)

    }
}


package com.example.opticyou.communications

import com.example.opticyou.communications.network.RetrofitClient
import com.example.opticyou.communications.network.ServerCommunication
import com.example.opticyou.data.LoginRequest
import com.example.opticyou.data.LoginResponse
import com.example.opticyou.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

/**
 * Object with functions for calling the server.
 * Fa de pont perquè ServerCommunication gestioni tota la communicació
 */

object ServerRequests {

    // A mutex is used to ensure that reads and writes are thread-safe.
    private val accessMutex = Mutex()

    open suspend fun login(username: String, password: String): LoginResponse? = accessMutex.withLock {
        return ServerCommunication.login(username, password)
    }

    suspend fun queryUser(username: String): User? = accessMutex.withLock {
        return ServerCommunication.queryUser(username)
    }

    suspend fun listUsers(): List<User>? = accessMutex.withLock {
        return ServerCommunication.listUsers()
    }

    suspend fun logout(token: String? = null): Boolean = accessMutex.withLock {
        return ServerCommunication.logout(token)
    }

//    suspend fun isLogged():Boolean = accessMutex.withLock {
//        return CommController.isLogged
//    }
//
//    suspend fun queryUser(username:String): User? = accessMutex.withLock {
//          return CommController.doQueryUser(username)
//    }
//
//    suspend fun addUser(user:User):Boolean = accessMutex.withLock {
//        return (CommController.doAddUser(user)==CommController.OK_RETURN_CODE)
//
//    }
}


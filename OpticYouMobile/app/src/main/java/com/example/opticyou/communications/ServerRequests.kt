package com.example.opticyou.communications

import com.example.opticyou.communications.network.AuthServerCommunication
import com.example.opticyou.data.LoginResponse
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


/**
 * Objecte que proporciona funcions per realitzar crides al servidor.
 * Fa de pont perquè AuthServerCommunication gestioni tota la communicació
 */

object ServerRequests {

    /**
     * Mutex per garantir que les lectures i escriptures siguin segures en fils concurrents.
     */
    private val accessMutex = Mutex()

    /**
     * Realitza el procés de login d'un usuari.
     *
     * @param username Nom d'usuari.
     * @param password Contrasenya de l'usuari.
     * @return [LoginResponse] si la petició és exitosa, `null` en cas contrari.
     */
    open suspend fun login(username: String, password: String): LoginResponse? = accessMutex.withLock {
        return AuthServerCommunication.login(username, password)
    }

    /**
     * Realitza el procés de logout d'un usuari.
     *
     * @param token Token d'autenticació de la sessió (opcional).
     * @return `true` si la petició és exitosa o `false` en cas contrari.
     */

    suspend fun logout(token: String? = null): Boolean = accessMutex.withLock {
        return AuthServerCommunication.logout(token)
    }

//    suspend fun queryUser(username: String): User? = accessMutex.withLock {
//        return ServerCommunication.queryUser(username)
//    }
//
//    suspend fun listUsers(): List<User>? = accessMutex.withLock {
//        return ServerCommunication.listUsers()
//    }

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


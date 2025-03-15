package com.example.opticyou.communications

import com.example.opticyou.data.User
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.InetSocketAddress
import java.net.Socket

/**
 * Communication specific class
 * @author professor
 */
//object CommController {
//    var BAD_VALUE: Int = -1
//    // emmagatzema el codi de sessió en memòria. Aquesta informació només existirà mentre l'app està en execució.
//    private var sessionCode = BAD_VALUE
//    private var port = BAD_VALUE
//    private var serverName: String? = null
//
//    const val OK_RETURN_CODE: Int = 0
//
//    // Names of the requests
//    const val LOGIN: String = "LOGIN"
//    const val LOGOUT: String = "LOGOUT"
//    const val LIST_USERS: String = "LIST_USERS"
//    const val ADD_USER: String = "ADD_USER"
//
//
//    //opens a connection to the server
//    private fun connect(): Socket? {
//        val s: Socket
//        if (port == BAD_VALUE || serverName == null) {
//            return null
//        } else {
//            try {
//                s = Socket()
//                s.connect(InetSocketAddress(serverName, port), 4000)
//
//                return s
//            } catch (ex: IOException) {
//                return null
//            }
//        }
//    }
//
//    val isLogged: Boolean
//        /**
//         * returns true if client is logged and false otherwhise
//         * @return
//         */
//        get() = sessionCode != BAD_VALUE
//
//    /**
//     * Set the value of port
//     *
//     * @param port new value of port
//     */
//    fun setPort(port: Int) {
//        CommController.port = port
//    }
//
//    /**
//     * Set the value of serverName
//     *
//     * @param serverName new value of serverName
//     */
//    fun setServerName(serverName: String?) {
//        CommController.serverName = serverName
//    }
//
//    /**
//     * Makes a login request to the server
//     * @param user  username
//     * @param password password
//     * @return result code
//     */
//    fun doLogin(user: String, password: String): Int {
//        val login = EndPointValues(LOGIN)
//        login.addPrimitiveData(user)
//        login.addPrimitiveData(password)
//        val ret = talkToServer(login) ?: return BAD_VALUE
//
//        val returnCode = ret.returnCode
//
//        if (returnCode == OK_RETURN_CODE) {
//            sessionCode = ret.getData(0, Int::class.java) as Int
//        }
//
//        return ret.returnCode
//    }
//
//    /**
//     * Makes a logout request to the server
//     * @return result code
//     */
//    fun doLogout(): Int {
//        if (sessionCode == BAD_VALUE) return OK_RETURN_CODE
//
//        val logout = EndPointValues(LOGOUT)
//
//        logout.addPrimitiveData(sessionCode)
//
//        val ret = talkToServer(logout) ?: return BAD_VALUE
//
//        val code = ret.returnCode
//
//        if (code == OK_RETURN_CODE) {
//            sessionCode = BAD_VALUE
//        }
//
//        return code
//    }
//
//    /**
//     * Makes a "list user" request to the server
//     * @return result users array; null if error.
//     */
//    fun doListUsers(): Array<User>? {
//        if (sessionCode == BAD_VALUE) return null
//
//        val listUsers = EndPointValues(LIST_USERS)
//
//        listUsers.addPrimitiveData(sessionCode)
//
//        val ret = talkToServer(listUsers) ?: return null
//
//        val returnCode = ret.returnCode
//
//        if (returnCode == OK_RETURN_CODE) {
//            val users = arrayOf<User>()
//            return ret.getData(0, users.javaClass) as Array<User>
//        } else {
//            return null
//        }
//    }
//
//    /**
//     * Makes an "add user" request to the server
//     * @param user  user to be added
//     * @return result code
//     */
//    fun doAddUser(user: User?): Int {
//        val addUser = EndPointValues(ADD_USER)
//        addUser.addPrimitiveData(sessionCode)
//        addUser.addDataObject(user)
//
//        val ret = talkToServer(addUser) ?: return BAD_VALUE
//
//        return ret.returnCode
//    }
//
//    /**
//     * Makes a "query user" request to the server
//     * @param username  username to search
//     * @return result user with this username; null if error
//     */
//    fun doQueryUser(username: String): User? {
//        return if (username == "99") { // simulates non-existent key
//            null
//        } else {
//            User(username, "Mock value")
//        }
//    }
//
//    // Sends message to the server and returns its response.
//    // messages are serialized as Json values before and after communication
//    private fun talkToServer(message: EndPointValues): ReturnValues? {
//        try {
//            val socket = connect()
//
//            val gson = Gson()
//
//            if (socket == null) return null
//
//            val output = PrintWriter(OutputStreamWriter(socket.getOutputStream()), true)
//
//
//            output.println(gson.toJson(message))
//
//            val input = BufferedReader(InputStreamReader(socket.getInputStream()))
//
//            val data = input.readLine()
//
//            val ret = gson.fromJson(data, ReturnValues::class.java)
//
//            socket.close()
//
//            return ret
//        } catch (ex: IOException) {
//            return null
//        }
//    }
//
//}
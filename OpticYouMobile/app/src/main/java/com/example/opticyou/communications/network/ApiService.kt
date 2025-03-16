package com.example.opticyou.communications.network

import com.example.opticyou.data.LoginRequest
import com.example.opticyou.data.LoginResponse
import com.example.opticyou.data.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

//Classe per definir els EndPoints que l'app cridarà al servidor
interface ApiService {
    @POST("auth/login-user")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("usuari/all")
    fun getAllUsers(): Call<List<User>>

    @GET("usuari/{username}")
    fun getUser(@Path("username") username: String): Call<User>

    @POST("auth/logout-string")
    fun logoutString(@Body jwt: String): Call<Boolean>

}

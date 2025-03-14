package com.example.opticyou.communications.network

import com.example.opticyou.data.LoginRequest
import com.example.opticyou.data.LoginResponse
import com.example.opticyou.data.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

//Classe per definir els EndPoints que l'app cridarà al servidor
interface ApiService {
    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("usuari/all")
    fun getAllUsers(): Call<List<User>>

    @GET("usuari/{username}")
    fun getUser(@Path("username") username: String): Call<User>


    @POST("logout")
    fun logout(): Call<Boolean>

}

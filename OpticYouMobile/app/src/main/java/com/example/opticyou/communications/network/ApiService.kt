package com.example.opticyou.communications.network

import com.example.opticyou.data.Center
import com.example.opticyou.data.LoginRequest
import com.example.opticyou.data.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


/**
 * Defineix els EndPoints de l'API que l'aplicació utilitza per comunicar-se amb el servidor.
 */
interface ApiService {
    /**
     * Endpoint pel login d'un usuari.
     *
     * @param loginRequest Dades de login de l'usuari.
     * @return [LoginResponse] amb la resposta d'autenticació.
     */
    @POST("auth/login-user")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>


    /**
     * Endpoint pel logout d'un usuari.
     *
     * @param jwt Token de la sessió
     * @return `true` si el logout és correcte, `false` si no..
     */
    @POST("auth/logout-string")
    fun logoutString(@Body jwt: String): Call<Boolean>

    @GET("centres")
    fun getCentres(@Header("Authorization") token: String): Call<List<Center>>

    @POST("centres")
    fun addCentre(@Header("Authorization") token: String, @Body centre: Center): Call<Center>

    @PUT("centres/{id}")
    fun updateCentre(@Header("Authorization") token: String, @Path("id") id: Int, @Body centre: Center): Call<Center>

    @DELETE("centres/{id}")
    fun deleteCentre(@Header("Authorization") token: String, @Path("id") id: Int): Call<Void>

//    @GET("usuari/all")
//    fun getAllUsers(): Call<List<User>>
//
//    @GET("usuari/{username}")
//    fun getUser(@Path("username") username: String): Call<User>

}

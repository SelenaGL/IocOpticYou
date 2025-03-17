package com.example.opticyou.communications.network

import com.example.opticyou.data.LoginRequest
import com.example.opticyou.data.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


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

//    @GET("usuari/all")
//    fun getAllUsers(): Call<List<User>>
//
//    @GET("usuari/{username}")
//    fun getUser(@Path("username") username: String): Call<User>

}

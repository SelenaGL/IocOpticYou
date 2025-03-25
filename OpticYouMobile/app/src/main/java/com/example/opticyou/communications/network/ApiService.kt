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

    /**
     * Obté la llista de centres.
     *
     * @param token Token d'autenticació inclòs a la petició.
     * @return [List] de [Center] que conté els centres registrats.
     */
    @GET("centres")
    fun getCentres(token: String): Call<List<Center>>

    /**
     * Afegeix un nou centre.
     *
     * @param token Token d'autenticació inclòs a la petició.
     * @param centre Objecte [Center] amb la informació del centre a afegir.
     * @return Objecte [Center] amb les dades del centre afegit.
     */
    @POST("centres")
    fun addCentre(token: String, @Body centre: Center): Call<Center>

    /**
     * Actualitza les dades d'un centre existent.
     *
     * @param token Token d'autenticació inclòs a la petició.
     * @param idclinica Identificador del centre a actualitzar.
     * @param centre Objecte [Center] amb les noves dades.
     * @return Objecte [Center] amb les dades actualitzades.
     */
    @PUT("centres/{idclinica}")
    fun updateCentre(token: String, @Path("idclinica") id: Int, @Body centre: Center): Call<Center>

    /**
     * Elimina un centre.
     *
     * @param token Token d'autenticació inclòs a la petició.
     * @param idclinica Identificador del centre a eliminar.
     * @return [Call] de tipus [Void] que indica la realització de l'operació.
     */
    @DELETE("centres/{idclinica}")
    fun deleteCentre(token: String, @Path("idclinica") id: Int): Call<Void>

//    @GET("usuari/all")
//    fun getAllUsers(): Call<List<User>>
//
//    @GET("usuari/{username}")
//    fun getUser(@Path("username") username: String): Call<User>

}

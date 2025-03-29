package com.example.opticyou.communications.network

import com.example.opticyou.data.Center
import com.example.opticyou.data.LoginRequest
import com.example.opticyou.data.LoginResponse
import okhttp3.ResponseBody
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
    @GET("clinica")
    fun getAllClinicas(@Header("Authorization") token: String): Call<List<Center>>

    /**
     * Obté la llista de centres.
     *
     * @param token Token d'autenticació inclòs a la petició.
     * @param id Codi del centre a cercar
     * @return [Center] de la consulta
     */
    @GET("clinica/{id}")
    fun getClinicaById(@Path("id") id: Long, @Header("Authorization") token: String): Call<Center>

    /**
     * Afegeix un nou centre.
     *
     * @param token Token d'autenticació inclòs a la petició.
     * @param centre Objecte [Center] amb la informació del centre a afegir.
     * @return estat de la petició de creació.
     */
    @POST("clinica")
    fun createClinica(@Header("Authorization") token: String, @Body center: Center): Call<Void>

    /**
     * Actualitza les dades d'un centre existent.
     *
     * @param token Token d'autenticació inclòs a la petició.
     * @param centre Objecte [Center] amb les noves dades.
     * @return missatge informatiu
     */
    @PUT("clinica/update")
    fun updateClinica(@Header("Authorization") token: String, @Body centre: Center): Call<String>

    /**
     * Elimina un centre.
     *
     * @param token Token d'autenticació inclòs a la petició.
     * @param idclinica Identificador del centre a eliminar.
     * @return missatge informatiu
     */
    @DELETE("clinica/{id}")
    fun deleteClinica(@Path("id") id: Long, @Header("Authorization") token: String): Call<String>
}

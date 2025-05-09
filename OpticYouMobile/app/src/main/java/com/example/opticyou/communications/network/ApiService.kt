package com.example.opticyou.communications.network

import com.example.opticyou.data.Center
import com.example.opticyou.data.Client
import com.example.opticyou.data.Diagnostic
import com.example.opticyou.data.Historial
import com.example.opticyou.data.LoginRequest
import com.example.opticyou.data.LoginResponse
import com.example.opticyou.data.Treballador
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

    /**
     * Endpoint per obtenir la llista de tots els clients.
     *
     * @param token Token d'autenticació inclòs al header.
     * @return Llista de [Client].
     */
    @GET("client")
    fun getAllClients(@Header("Authorization") token: String): Call<List<Client>>

    /**
     * Endpoint per obtenir un client pel seu identificador (per rol CLIENT només retorna el propi perfil)
     *
     * @param id Identificador del client.
     * @param token Token d'autenticació.
     * @return Un [Client].
     */
    @GET("client/{id}")
    fun getClientById(@Path("id") id: Long, @Header("Authorization") token: String): Call<Client>

    /**
     * Endpoint per crear un nou client.
     *
     * @param token Token d'autenticació.
     * @param client Objecte [Client] amb les dades a afegir.
     * @return [Void] si la creació ha estat exitosa.
     */
    @POST("client")
    fun createClient(@Header("Authorization") token: String, @Body client: Client): Call<Void>

    /**
     * Endpoint per actualitzar un client existent.
     *
     * @param token Token d'autenticació.
     * @param client Objecte [Client] amb les noves dades.
     * @return L'objecte [Client] actualitzat.
     */
    @PUT("client/update")
    fun updateClient(@Header("Authorization") token: String, @Body client: Client): Call<ResponseBody>

    /**
     * Endpoint per eliminar un client pel seu identificador.
     *
     * @param id Identificador del client a eliminar.
     * @param token Token d'autenticació.
     * @return [Void] si l'operació ha estat exitosa.
     */
    @DELETE("client/{id}")
    fun deleteClient(@Path("id") id: Long, @Header("Authorization") token: String): Call<Void>


    /**
     * Actualitza les dades del client autenticat
     *
     * @param token Token d'autenticació.
     * @param client Objecte [Client] amb les noves dades.
     *
     */
    @PUT("client/update_client")
    fun updateClientSelf(
        @Header("Authorization") token: String,
        @Body client: Client
    ): Call<String>

    /**
     * Elimina el compte del client autenticat
     *
     * @param token Token d'autenticació.
     * @return [Void] si l'operació ha estat exitosa.
     */
    @DELETE("client/delete_client")
    fun deleteClientSelf(
        @Header("Authorization") token: String
    ): Call<String>


    /**
     * Endpoint per obtenir la llista de tots els historials.
     *
     * @param token Token d'autenticació inclòs al header.
     * @return Llista de [Historial].
     */
    @GET("historial")
    fun getAllHistorial(@Header("Authorization") token: String): Call<List<Historial>>

    /**
     * Endpoint per obtenir un historial pel seu identificador.
     *
     * @param id Identificador de l'historial.
     * @param token Token d'autenticació.
     * @return L'objecte [Historial].
     */
    @GET("historial/{id}")
    fun getHistorialById(@Path("id") id: Long, @Header("Authorization") token: String): Call<Historial>

    /**
     * Endpoint per actualitzar un historial existent.
     *
     * @param token Token d'autenticació.
     * @param historial Objecte [Historial] amb les noves dades.
     * @return Missatge informatiu.
     */
    @PUT("historial/update")
    fun updateHistorial(@Header("Authorization") token: String, @Body historial: Historial): Call<String>

    @POST("treballador")
    fun createTreballador(
        @Header("Authorization") token: String, @Body treballador: Treballador): Call<Void>

    @GET("treballador")
    fun getAllTreballadors(
        @Header("Authorization") token: String): Call<List<Treballador>>

    @GET("treballador/{id}")
    fun getTreballadorById(
        @Path("id") id: Long, @Header("Authorization") token: String): Call<Treballador>

    @PUT("treballador/update")
    fun updateTreballador(
        @Header("Authorization") token: String, @Body treballador: Treballador): Call<String>

    @DELETE("treballador/{id}")
    fun deleteTreballador(
        @Path("id") id: Long, @Header("Authorization") token: String): Call<String>

    @GET("diagnostic")
    fun getAllDiagnostics(@Header("Authorization") token: String): Call<List<Diagnostic>>

    @GET("diagnostic/historial/{id}")
    fun getDiagnosticsByHistorial(@Path("id") historialId: Long,@Header("Authorization") token: String
    ): Call<List<Diagnostic>>

    @POST("diagnostic")
    fun createDiagnostic(@Header("Authorization") token: String, @Body diagnostic: Diagnostic): Call<Void>

    @PUT("diagnostic/update")
    fun updateDiagnostic(@Header("Authorization") token: String, @Body diagnostic: Diagnostic): Call<String>

    @DELETE("diagnostic/{id}")
    fun deleteDiagnostic(@Path("id") id: Long, @Header("Authorization") token: String): Call<String>
}


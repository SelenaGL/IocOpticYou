package com.example.opticyou.communications.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Objecte encarregat de la configuració de Retrofit per realitzar peticions al servidor.
 */
object RetrofitClient {
    private var BASE_URL = "http://10.0.2.2:8083/"

    // Configura l'interceptor per visualitzar el log de les peticions/respostes.
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Mostra headers i cos de la petició/resposta
    }

    // Crea un OkHttpClient que inclogui l'interceptor
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()


    /**
     * Instància única de [ApiService] per realitzar peticions HTTP.
     */
    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    // Funció per canviar la base URL en proves
    fun setBaseUrlForTesting(url: String) {
        BASE_URL = url
    }
}
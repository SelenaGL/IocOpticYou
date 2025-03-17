package com.example.opticyou.communications.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Objecte encarregat de la configuració de Retrofit per realitzar peticions al servidor.
 */
object RetrofitClient {
    private var BASE_URL = "http://10.0.2.2:8083/"

    /**
     * Instància única de [ApiService] per realitzar peticions HTTP.
     */
    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    // Funció per canviar la base URL en proves
    fun setBaseUrlForTesting(url: String) {
        BASE_URL = url
    }
}
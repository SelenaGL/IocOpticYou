package com.example.opticyou.communications.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Configurar el retrofit per fer peticions al servidor
object RetrofitClient {
    private var BASE_URL = "http://10.0.2.2:8083/"

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
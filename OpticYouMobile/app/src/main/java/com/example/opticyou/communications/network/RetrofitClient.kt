package com.example.opticyou.communications.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Objecte encarregat de la configuració de Retrofit per realitzar peticions al servidor.
 */


object RetrofitClient {
    private var BASE_URL = "https://10.0.2.2:8083/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Creem un TrustManager
    private fun getUnsafeOkHttpClient(): OkHttpClient {
        // TrustManager que accepta qualsevol certificat
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) = Unit
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) = Unit
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        })

        // Iniciem un SSLContext amb aquest TrustManager
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCerts, SecureRandom())
        val sslSocketFactory = sslContext.socketFactory

        return OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            // HostnameVerifier que accepta qualsevol hostname
            .hostnameVerifier { _, _ -> true }
            .addInterceptor(loggingInterceptor)
            .build()
    }

    // Configura Gson en mode lenient per acceptar JSON mal formatat
    private val gson = GsonBuilder()
        .setLenient()
        .create()

    /**
     * Instància única de [ApiService] per realitzar peticions HTTP.
     */
    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // Aquí canviem perquè faci servir el client "unsafe"
            .client(getUnsafeOkHttpClient())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }

    fun setBaseUrlForTesting(url: String) {
        BASE_URL = url
    }
}
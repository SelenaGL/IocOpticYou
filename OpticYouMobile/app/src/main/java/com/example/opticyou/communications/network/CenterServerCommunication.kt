package com.example.opticyou.communications.network

import com.example.opticyou.data.Center
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

object CenterServerCommunication {
    suspend fun getCentres(token: String): List<Center>? = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.instance.getCentres(token).awaitResponse()
            if (response.isSuccessful) {
                response.body()
            } else {
                println("Error a l'obtenir els centres: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            println("Excepció a getCentres: ${e.message}")
            null
        }
    }

    suspend fun addCentre(token: String, centre: Center): Center? = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.instance.addCentre(token, centre).awaitResponse()
            if (response.isSuccessful) {
                response.body()
            } else {
                println("Error a l'afegir el centre: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            println("Excepció a addCentre: ${e.message}")
            null
        }
    }

    suspend fun updateCentre(token: String, centre: Center): Center? = withContext(Dispatchers.IO) {
        try {
            val response =
                RetrofitClient.instance.updateCentre(token, centre.id, centre).awaitResponse()
            if (response.isSuccessful) {
                response.body()
            } else {
                println("Error a l'actualitzar el centre: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            println("Excepció a updateCentre: ${e.message}")
            null
        }
    }

    suspend fun deleteCentre(token: String, id: Int): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.instance.deleteCentre(token, id).awaitResponse()
            if (response.isSuccessful) {
                true
            } else {
                println("Error a l'eliminar el centre: ${response.code()} - ${response.message()}")
                false
            }
        } catch (e: Exception) {
            println("Excepció a deleteCentre: ${e.message}")
            false
        }
    }
}
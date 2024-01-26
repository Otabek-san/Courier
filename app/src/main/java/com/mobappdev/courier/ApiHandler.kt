package com.mobappdev.courier

import retrofit2.Call
import retrofit.GsonConverterFactory
import retrofit.Retrofit

class ApiHandler {

    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://google.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
    }

    suspend fun getStores(): Call<List<Store>>? {
        return try {
            apiService.getStores()
        } catch (e: Exception) {
            null
        }
    }
}

package com.mobappdev.courier

import retrofit.GsonConverterFactory
import retrofit.Retrofit

object RetrofitInstance {
    private const val BASE_URL = "your_base_url" // Replace with your actual base URL

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
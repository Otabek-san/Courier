package com.mobappdev.courier

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiService {

    @GET("stores/list")
    fun getStores(): Call<List<Store>>

    @POST("store/add")
    fun addStore(@Body store: Store): Call<Store>

}
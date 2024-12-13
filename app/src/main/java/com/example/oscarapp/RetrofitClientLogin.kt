package com.example.oscarapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientLogin {
    private const val BASE_URL = "https://d621-38-43-102-217.ngrok-free.app"


    val instance: Retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
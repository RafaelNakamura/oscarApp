package com.example.oscarapp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("filme")
    fun getMovies(): Call<List<Movie>>

    @GET("diretor")
    fun getDirectors(): Call<List<Director>>

    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("votar") // Endpoint para votar
    fun votar(@Body votoRequest: VotoRequest): Call<VotoResponse>
}

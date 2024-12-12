package com.example.oscarapp

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("filme")
    fun getMovies(): Call<List<Movie>>

    @GET("diretor")
    fun getDirectors(): Call<List<Director>>
}

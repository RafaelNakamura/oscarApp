package com.example.oscarapp

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String?,
    val voto: Vote
)

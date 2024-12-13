package com.example.oscarapp

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val usuarioId: Int,
    val token: String?,
    val votos: Vote
)

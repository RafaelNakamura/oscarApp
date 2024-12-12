package com.example.oscarapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ConfirmVoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_vote)

        val tvSelectedMovie = findViewById<TextView>(R.id.tvSelectedMovie)
        val tvSelectedDirector = findViewById<TextView>(R.id.tvSelectedDirector)
        val etToken = findViewById<EditText>(R.id.etToken)
        val btnSendVote = findViewById<Button>(R.id.btnSendVote)

        // Simulação de dados passados das outras telas
        val selectedMovie = intent.getStringExtra("SELECTED_MOVIE") ?: "Nenhum filme selecionado"
        val selectedDirector = intent.getStringExtra("SELECTED_DIRECTOR") ?: "Nenhum diretor selecionado"

        tvSelectedMovie.text = "Filme Selecionado: $selectedMovie"
        tvSelectedDirector.text = "Diretor Selecionado: $selectedDirector"

        // Envia o voto
        btnSendVote.setOnClickListener {
            val token = etToken.text.toString()
            if (token.isEmpty()) {
                Toast.makeText(this, "Por favor, insira o token.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Simular envio para o servidor
            val success = token == "1234" // Token correto para teste
            if (success) {
                Toast.makeText(this, "Voto confirmado com sucesso!", Toast.LENGTH_SHORT).show()
                // Bloquear modificações
                finish()
            } else {
                Toast.makeText(this, "Token inválido, tente novamente.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

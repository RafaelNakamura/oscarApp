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

import android.content.Context

class ConfirmVoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_vote)

        val tvSelectedMovie = findViewById<TextView>(R.id.tvSelectedMovie)
        val tvSelectedDirector = findViewById<TextView>(R.id.tvSelectedDirector)
        val btnConfirmVote = findViewById<Button>(R.id.btnConfirmVote)

        // Recupera os valores do SharedPreferences
        val sharedPreferences = getSharedPreferences("MovieVotePrefs", Context.MODE_PRIVATE)
        val selectedMovie = sharedPreferences.getString("SELECTED_MOVIE", "Nenhum filme selecionado")
        val selectedDirector = sharedPreferences.getString("SELECTED_DIRECTOR", "Nenhum diretor selecionado")

        // Exibe os valores na tela
        tvSelectedMovie.text = "Filme Selecionado: $selectedMovie"
        tvSelectedDirector.text = "Diretor Selecionado: $selectedDirector"

        // Confirma o voto
        btnConfirmVote.setOnClickListener {
            if (selectedMovie == "Nenhum filme selecionado" || selectedDirector == "Nenhum diretor selecionado") {
                Toast.makeText(this, "Você precisa selecionar um filme e um diretor antes de confirmar.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Voto confirmado com sucesso!", Toast.LENGTH_SHORT).show()
                clearSharedPreferences()
                finish() // Finaliza a activity
            }
        }
    }

    private fun clearSharedPreferences() {
        val sharedPreferences = getSharedPreferences("MovieVotePrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear() // Limpa todos os valores
        editor.apply()
    }
}


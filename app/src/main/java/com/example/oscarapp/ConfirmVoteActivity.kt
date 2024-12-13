package com.example.oscarapp

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import androidx.appcompat.app.AlertDialog

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

        // Ao clicar no botão de confirmar voto
        btnConfirmVote.setOnClickListener {
            // Verifica se ambos os campos foram preenchidos
            if (selectedMovie == "Nenhum filme selecionado" || selectedDirector == "Nenhum diretor selecionado") {
                Toast.makeText(this, "Você precisa selecionar um filme e um diretor antes de confirmar.", Toast.LENGTH_SHORT).show()
            } else {
                // Exibe o AlertDialog de confirmação
                if (selectedMovie != null && selectedDirector != null) {
                    showConfirmationDialog(selectedMovie, selectedDirector)
                }
            }
        }
    }

    // Função que cria e exibe o AlertDialog
    private fun showConfirmationDialog(movieName: String, directorName: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmar Voto")
        builder.setMessage("Você tem certeza de que deseja votar no filme: $movieName e no diretor: $directorName?")

        // Botão "Sim"
        builder.setPositiveButton("Sim") { dialog, which ->
            // Salva o voto
            saveVote(movieName, directorName)
            Toast.makeText(this, "Voto confirmado!", Toast.LENGTH_SHORT).show()
            finish() // Finaliza a activity e retorna à tela anterior
        }

        // Botão "Cancelar"
        builder.setNegativeButton("Cancelar") { dialog, which ->
            // Não faz nada, apenas fecha o diálogo
            dialog.dismiss()
        }

        // Exibe o diálogo
        builder.create().show()
    }

    // Função que salva o voto no SharedPreferences
    private fun saveVote(movieName: String, directorName: String) {
        val sharedPreferences = getSharedPreferences("MovieVotePrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("FINAL_MOVIE_VOTE", movieName)
        editor.putString("FINAL_DIRECTOR_VOTE", directorName)
        editor.apply()

        // Limpa os dados temporários (caso desejado)
        clearTemporaryVoteData()
    }

    // Limpa os dados temporários de seleção
    private fun clearTemporaryVoteData() {
        val sharedPreferences = getSharedPreferences("MovieVotePrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("SELECTED_MOVIE")
        editor.remove("SELECTED_DIRECTOR")
        editor.apply()
    }
}


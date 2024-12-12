package com.example.oscarapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import android.content.Context

class DirectorVoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_director_vote)

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupDirectors)
        val btnConfirmVote = findViewById<Button>(R.id.btnConfirmDirectorVote)

        // Lista simulada de diretores
        val directors = listOf("James Cameron", "Alfred Hitchcock", "Steven Spielberg")
        directors.forEach { director ->
            val radioButton = RadioButton(this)
            radioButton.text = director
            radioGroup.addView(radioButton)
        }

        btnConfirmVote.setOnClickListener {
            val selectedRadioButtonId = radioGroup.checkedRadioButtonId
            if (selectedRadioButtonId == -1) {
                Toast.makeText(this, "Por favor, selecione um diretor.", Toast.LENGTH_SHORT).show()
            } else {
                val selectedDirector = findViewById<RadioButton>(selectedRadioButtonId).text.toString()
                saveSelectedDirector(selectedDirector)
                Toast.makeText(this, "Você votou no diretor: $selectedDirector", Toast.LENGTH_SHORT).show()
                finish() // Finaliza a activity e retorna para a anterior
            }
        }
    }

    private fun saveSelectedDirector(directorName: String) {
        val sharedPreferences = getSharedPreferences("MovieVotePrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("SELECTED_DIRECTOR", directorName)
        editor.apply()
    }
}


package com.example.oscarapp

import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DirectorVoteActivity : AppCompatActivity() {

    private lateinit var selectedDirector: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_director_vote)

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupDirectors)
        val btnConfirmVote = findViewById<Button>(R.id.btnConfirmDirectorVote)

        // Lista simulada de diretores
        val directors = listOf("Diretor 1", "Diretor 2", "Diretor 3", "Diretor 4")

        // Adiciona os RadioButtons dinamicamente
        directors.forEach { director ->
            val radioButton = RadioButton(this)
            radioButton.text = director
            radioGroup.addView(radioButton)
        }

        // Lida com a seleção do RadioGroup
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            selectedDirector = radioButton.text.toString()
        }

        // Confirma o voto
        btnConfirmVote.setOnClickListener {
            if (::selectedDirector.isInitialized) {
                Toast.makeText(this, "Você votou em: $selectedDirector", Toast.LENGTH_SHORT).show()
                // Salvar voto local ou passar para ConfirmVoteActivity
            } else {
                Toast.makeText(this, "Selecione um diretor para votar.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

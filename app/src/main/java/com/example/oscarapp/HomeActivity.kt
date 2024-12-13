package com.example.oscarapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val btnVoteMovie = findViewById<Button>(R.id.btnVoteMovie)
        val btnVoteDirector = findViewById<Button>(R.id.btnVoteDirector)
        val btnConfirmVote = findViewById<Button>(R.id.btnConfirmVote)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val tvWelcomeMsg = findViewById<TextView>(R.id.tvWelcomeMessage)

        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("AUTH_TOKEN", "Token não encontrado")
        val votoFilme = sharedPreferences.getInt("VOTO_FILME", -1)
        val votoDiretor = sharedPreferences.getInt("VOTO_DIRETOR", -1)

        if (!(votoFilme == -1 && votoDiretor == -1)) {
            btnVoteDirector.isEnabled = false
            btnVoteMovie.isEnabled = false
        }

        tvWelcomeMsg.text = "Bem-vindo! Seu token de autenticação é: $authToken"

        btnVoteMovie.setOnClickListener {
            startActivity(Intent(this, MovieVoteActivity::class.java))
        }

        btnVoteDirector.setOnClickListener {
            startActivity(Intent(this, DirectorVoteActivity::class.java))
        }

        btnConfirmVote.setOnClickListener {
            startActivity(Intent(this, ConfirmVoteActivity::class.java))
        }

        btnLogout.setOnClickListener {
            finish()
        }
    }
}

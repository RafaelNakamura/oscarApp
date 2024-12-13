package com.example.oscarapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var btnVoteMovie: Button
    private lateinit var btnVoteDirector: Button
    private lateinit var btnConfirmVote: Button
    private lateinit var btnLogout: Button
    private lateinit var tvWelcomeMsg: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnVoteMovie = findViewById(R.id.btnVoteMovie)
        btnVoteDirector = findViewById(R.id.btnVoteDirector)
        btnConfirmVote = findViewById(R.id.btnConfirmVote)
        btnLogout = findViewById(R.id.btnLogout)
        tvWelcomeMsg = findViewById(R.id.tvWelcomeMessage)

        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("AUTH_TOKEN", "Token não encontrado")
        tvWelcomeMsg.text = "Bem-vindo! Seu token é: $authToken"

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

    override fun onResume() {
        super.onResume()

        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val votoFilme = sharedPreferences.getInt("VOTO_FILME", -1)
        val votoDiretor = sharedPreferences.getInt("VOTO_DIRETOR", -1)

        if(votoFilme != -1 && votoDiretor != -1){
            btnVoteMovie.isEnabled = false
            btnVoteDirector.isEnabled = false
        }
    }
}

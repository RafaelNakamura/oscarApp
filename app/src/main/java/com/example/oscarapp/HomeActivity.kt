package com.example.oscarapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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

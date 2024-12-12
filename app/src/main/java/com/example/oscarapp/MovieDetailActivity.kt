package com.example.oscarapp

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MovieDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        val imgPoster = findViewById<ImageView>(R.id.imgDetailPoster)
        val tvName = findViewById<TextView>(R.id.tvDetailName)
        val tvGenre = findViewById<TextView>(R.id.tvDetailGenre)
        val btnVote = findViewById<Button>(R.id.btnVoteMovie)

        val name = intent.getStringExtra("MOVIE_NAME")
        val genre = intent.getStringExtra("MOVIE_GENRE")
        val posterUrl = intent.getStringExtra("MOVIE_POSTER")

        tvName.text = name
        tvGenre.text = genre
        // imgPoster.setImageResource(R.drawable.ic_placeholder)

        btnVote.setOnClickListener {
            Toast.makeText(this, "Você votou em $name!", Toast.LENGTH_SHORT).show()
        }
    }
}

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
import android.content.Context
import com.squareup.picasso.Picasso

class MovieDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        val imgPoster = findViewById<ImageView>(R.id.imgDetailPoster)
        val tvName = findViewById<TextView>(R.id.tvDetailName)
        val tvGenre = findViewById<TextView>(R.id.tvDetailGenre)
        val btnVote = findViewById<Button>(R.id.btnVoteMovie)

        // Obtém os dados enviados pelo Intent
        val name = intent.getStringExtra("MOVIE_NAME") ?: "Nome não disponível"
        val genre = intent.getStringExtra("MOVIE_GENRE") ?: "Gênero não disponível"
        val posterUrl = intent.getStringExtra("MOVIE_POSTER")
        val id = intent.getIntExtra("MOVIE_ID", -1)

        // Exibe os dados na tela
        tvName.text = name
        tvGenre.text = genre
        if (!posterUrl.isNullOrEmpty()) {
            Picasso.get().load(posterUrl).into(imgPoster)
        }

        // Configura o botão "Votar"
        btnVote.setOnClickListener {
            saveSelectedMovie(name, id)
            Toast.makeText(this, "Você votou no filme: $name", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveSelectedMovie(movieName: String, id: Int) {
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("SELECTED_MOVIE", movieName)
        editor.putInt("SELECTED_MOVIE_ID", id)
        editor.apply()
        finish()
    }

}


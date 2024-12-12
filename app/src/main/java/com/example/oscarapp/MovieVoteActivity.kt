package com.example.oscarapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MovieVoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_vote)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewMovies)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Lista simulada de filmes
        val movies = listOf(
            Movie("Filme 1", "Ação", "https://example.com/poster1.jpg"),
            Movie("Filme 2", "Comédia", "https://example.com/poster2.jpg"),
            Movie("Filme 3", "Drama", "https://example.com/poster3.jpg")
        )

        val adapter = MoviesAdapter(movies) { selectedMovie ->
            // Clique no filme: navegue para detalhes
            val intent = Intent(this, MovieDetailActivity::class.java)
            intent.putExtra("MOVIE_NAME", selectedMovie.name)
            intent.putExtra("MOVIE_GENRE", selectedMovie.genre)
            intent.putExtra("MOVIE_POSTER", selectedMovie.posterUrl)
            startActivity(intent)
        }

        recyclerView.adapter = adapter
    }
}


class MoviesAdapter(
    private val movies: List<Movie>, // Lista de objetos Movie
    private val onMovieClick: (Movie) -> Unit // Callback para cliques nos itens
) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMovieName: TextView = itemView.findViewById(R.id.tvMovieName)
        val tvGenre: TextView = itemView.findViewById(R.id.tvGenre)
        val imgPoster: ImageView = itemView.findViewById(R.id.imgPoster)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.tvMovieName.text = movie.name
        holder.tvGenre.text = movie.genre
        // holder.imgPoster.setImageResource(R.drawable.ic_placeholder)

        holder.itemView.setOnClickListener { onMovieClick(movie) }
    }

    override fun getItemCount() = movies.size
}


package com.example.oscarapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import retrofit2.Call

class MovieVoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_vote)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewMovies)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fazendo a chamada à API para buscar os filmes
        val api = RetrofitConfig.instance.create(ApiService::class.java)
        progressBar.visibility = View.VISIBLE

        api.getMovies().enqueue(object : retrofit2.Callback<List<Movie>> {
            override fun onResponse(call: Call<List<Movie>>, response: retrofit2.Response<List<Movie>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val movies = response.body() ?: emptyList()

                    // Configura o adapter do RecyclerView com os dados dos filmes
                    recyclerView.adapter = MoviesAdapter(movies) { movie ->
                        // Ao clicar em um filme, chama a MovieDetailActivity
                        val intent = Intent(this@MovieVoteActivity, MovieDetailActivity::class.java)
                        intent.putExtra("MOVIE_NAME", movie.nome)
                        intent.putExtra("MOVIE_GENRE", movie.genero)
                        intent.putExtra("MOVIE_POSTER", movie.foto)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this@MovieVoteActivity, "Erro ao carregar filmes.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@MovieVoteActivity, "Falha na comunicação: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}



class MoviesAdapter(
    private val movies: List<Movie>,
    private val onMovieClick: (Movie) -> Unit
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
        holder.tvMovieName.text = movie.nome
        holder.tvGenre.text = movie.genero
        Picasso.get().load(movie.foto).into(holder.imgPoster)

        holder.itemView.setOnClickListener { onMovieClick(movie) }
    }

    override fun getItemCount() = movies.size
}



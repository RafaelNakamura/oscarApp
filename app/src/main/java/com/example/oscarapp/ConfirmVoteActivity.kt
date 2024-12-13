package com.example.oscarapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import androidx.appcompat.app.AlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConfirmVoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_vote)

        val tvSelectedMovie = findViewById<TextView>(R.id.tvSelectedMovie)
        val tvSelectedDirector = findViewById<TextView>(R.id.tvSelectedDirector)
        val btnConfirmVote = findViewById<Button>(R.id.btnConfirmVote)
        val progressBar = findViewById<ProgressBar>(R.id.progressBarConfirm)

        // Exibe o ProgressBar enquanto os dados estão sendo carregados
        progressBar.visibility = View.VISIBLE
        btnConfirmVote.isEnabled = false

        // Recupera os valores do SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("AUTH_TOKEN", null)
        var selectedMovie = sharedPreferences.getString("SELECTED_MOVIE", "Nenhum filme selecionado")
        var selectedDirector = sharedPreferences.getString("SELECTED_DIRECTOR", "Nenhum diretor selecionado")
        val selectedMovieId = sharedPreferences.getInt("SELECTED_MOVIE_ID", -1)
        val selectedDirectorId = sharedPreferences.getInt("SELECTED_DIRECTOR_ID", -1)

        val votoFilme = sharedPreferences.getInt("VOTO_FILME", -1)
        val votoDiretor = sharedPreferences.getInt("VOTO_DIRETOR", -1)

        // Variáveis para rastrear carregamento
        var isDirectorsLoaded = false
        var isMoviesLoaded = false

        // Função para atualizar a tela após os dados serem carregados
        fun updateUI() {
            if (isDirectorsLoaded && isMoviesLoaded) {
                progressBar.visibility = View.GONE
                btnConfirmVote.isEnabled = votoFilme == -1 && votoDiretor == -1
                tvSelectedMovie.text = "Filme Selecionado: $selectedMovie"
                tvSelectedDirector.text = "Diretor Selecionado: $selectedDirector"
            }
        }

        // Carrega os diretores
        fetchDirectors { directors ->
            directors.forEach { director ->
                if (director.id == votoDiretor) {
                    selectedDirector = director.nome
                }
            }
            isDirectorsLoaded = true
            updateUI()
        }

        // Carrega os filmes
        fetchMovies { movies ->
            movies.forEach { movie ->
                if (movie.id == votoFilme) {
                    selectedMovie = movie.nome
                }
            }
            isMoviesLoaded = true
            updateUI()
        }

        // Ao clicar no botão de confirmar voto
        btnConfirmVote.setOnClickListener {
            if (selectedMovie == "Nenhum filme selecionado" || selectedDirector == "Nenhum diretor selecionado") {
                Toast.makeText(this, "Você precisa selecionar um filme e um diretor antes de confirmar.", Toast.LENGTH_SHORT).show()
            } else {
                if (selectedMovie != null && selectedDirector != null) {
                    showConfirmationDialog(selectedMovieId, selectedDirectorId, token ?: "")
                }
            }
        }
    }


    // Função que cria e exibe o AlertDialog
    private fun showConfirmationDialog(movie: Int, director: Int, token: String) {
        val etToken = findViewById<EditText>(R.id.etToken)
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val usuarioId = sharedPreferences.getInt("USER_ID", -1)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmar Voto")
        builder.setMessage("Você confirma o seu Voto?")

        // Botão "Sim"
        builder.setPositiveButton("Sim") { dialog, which ->
            // Verifica se o token está correto
            if (token.isEmpty() || token != etToken.text.toString()) {
                Toast.makeText(this, "Digite o token corretamente!", Toast.LENGTH_SHORT).show()
                return@setPositiveButton // Interrompe o fluxo dentro do botão
            }

            // Executa os próximos passos somente se o token estiver correto
            enviarVoto(usuarioId, movie, director)
        }

        // Botão "Cancelar"
        builder.setNegativeButton("Cancelar") { dialog, which ->
            dialog.dismiss()
        }

        // Exibe o diálogo
        builder.create().show()
    }

    // Limpa os dados temporários de seleção
    private fun clearTemporaryVoteData() {
        val sharedPreferences = getSharedPreferences("MovieVotePrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("SELECTED_MOVIE")
        editor.remove("SELECTED_DIRECTOR")
        editor.apply()
    }

    private fun enviarVoto(usuarioId: Int, filme: Int, diretor: Int) {
        val api = RetrofitClientLogin.instance.create(ApiService::class.java)
        val votoRequest = VotoRequest(filme, diretor, usuarioId)
        val btnConfirmVote = findViewById<Button>(R.id.btnConfirmVote)

        api.votar(votoRequest).enqueue(object : Callback<VotoResponse> {
            override fun onResponse(call: Call<VotoResponse>, response: Response<VotoResponse>) {
                if (response.isSuccessful) {
                    val votoResponse = response.body()
                    if (votoResponse != null && votoResponse.success) {
                        Toast.makeText(this@ConfirmVoteActivity, "Voto registrado com sucesso!", Toast.LENGTH_SHORT).show()
                        saveLocalVote(votoRequest)
                        btnConfirmVote.isEnabled = false;
                    } else {
                        Toast.makeText(this@ConfirmVoteActivity, votoResponse?.message ?: "Erro desconhecido", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ConfirmVoteActivity, "Erro ao registrar voto: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<VotoResponse>, t: Throwable) {
                Toast.makeText(this@ConfirmVoteActivity, "Falha na comunicação: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveLocalVote(voto: VotoRequest) {
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("VOTO_FILME", voto.filme);
        editor.putInt("VOTO_DIRETOR", voto.diretor);
        editor.apply()
    }

    private fun fetchDirectors(callback: (List<Director>) -> Unit) {
        val api = RetrofitConfig.instance.create(ApiService::class.java)
        api.getDirectors().enqueue(object : Callback<List<Director>> {
            override fun onResponse(call: Call<List<Director>>, response: Response<List<Director>>) {
                if (response.isSuccessful) {
                    val directors = response.body() ?: emptyList()
                    callback(directors)
                } else {
                }
            }

            override fun onFailure(call: Call<List<Director>>, t: Throwable) {
            }
        })
    }

    private fun fetchMovies(callback: (List<Movie>) -> Unit) {
        val api = RetrofitConfig.instance.create(ApiService::class.java)
        api.getMovies().enqueue(object : Callback<List<Movie>> {
            override fun onResponse(call: Call<List<Movie>>, response: Response<List<Movie>>) {
                if (response.isSuccessful) {
                    val movies = response.body() ?: emptyList()
                    callback(movies)
                } else {
                }
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
            }
        })
    }

    override fun onResume() {
        super.onResume()

        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val votoFilme = sharedPreferences.getInt("VOTO_FILME", -1)
        val votoDiretor = sharedPreferences.getInt("VOTO_DIRETOR", -1)
        val btnConfirmVote = findViewById<Button>(R.id.btnConfirmVote)

        if (!(votoFilme == -1 && votoDiretor == -1)) {
            btnConfirmVote.isEnabled = false
        }
    }
}


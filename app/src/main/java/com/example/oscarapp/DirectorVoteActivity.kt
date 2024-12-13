package com.example.oscarapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DirectorVoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_director_vote)

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupDirectors)
        val btnConfirmVote = findViewById<Button>(R.id.btnConfirmDirectorVote)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        progressBar.visibility = View.VISIBLE

        fetchDirectors { directors ->
            progressBar.visibility = View.GONE
            directors.forEach { director ->
                val radioButton = RadioButton(this)
                radioButton.text = director.nome
                radioGroup.addView(radioButton)
            }
        }

        btnConfirmVote.setOnClickListener {
            val selectedRadioButtonId = radioGroup.checkedRadioButtonId
            if (selectedRadioButtonId == -1) {
                Toast.makeText(this, "Por favor, selecione um diretor.", Toast.LENGTH_SHORT).show()
            } else {
                val selectedDirector = findViewById<RadioButton>(selectedRadioButtonId).text.toString()
                saveSelectedDirector(selectedDirector)
                Toast.makeText(this, "Você votou no diretor: $selectedDirector", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun fetchDirectors(callback: (List<Director>) -> Unit) {
        val api = RetrofitConfig.instance.create(ApiService::class.java)
        api.getDirectors().enqueue(object : Callback<List<Director>> {
            override fun onResponse(call: Call<List<Director>>, response: Response<List<Director>>) {
                if (response.isSuccessful) {
                    val directors = response.body() ?: emptyList()
                    callback(directors)
                } else {
                    Toast.makeText(this@DirectorVoteActivity, "Erro ao carregar diretores.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Director>>, t: Throwable) {
                Toast.makeText(this@DirectorVoteActivity, "Falha na comunicação: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveSelectedDirector(directorName: String) {
        val sharedPreferences = getSharedPreferences("MovieVotePrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("SELECTED_DIRECTOR", directorName)
        editor.apply()
    }
}

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

        // Faz a requisição para buscar os diretores
        fetchDirectors { directors ->
            progressBar.visibility = View.GONE
            directors.forEach { director ->
                val radioButton = RadioButton(this)
                radioButton.text = director.nome // Nome do diretor
                radioButton.tag = director.id    // Salva o ID do diretor na tag
                radioGroup.addView(radioButton)
            }
        }

        btnConfirmVote.setOnClickListener {
            val selectedRadioButtonId = radioGroup.checkedRadioButtonId
            if (selectedRadioButtonId == -1) {
                Toast.makeText(this, "Por favor, selecione um diretor.", Toast.LENGTH_SHORT).show()
            } else {
                val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
                val selectedDirectorName = selectedRadioButton.text.toString()
                val selectedDirectorId = selectedRadioButton.tag.toString().toInt()

                saveSelectedDirector(selectedDirectorName, selectedDirectorId)
                Toast.makeText(this, "Você votou no diretor: $selectedDirectorName", Toast.LENGTH_SHORT).show()
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

    private fun saveSelectedDirector(directorName: String, directorId: Int) {
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("SELECTED_DIRECTOR", directorName)
        editor.putInt("SELECTED_DIRECTOR_ID", directorId)
        editor.apply()
    }
}

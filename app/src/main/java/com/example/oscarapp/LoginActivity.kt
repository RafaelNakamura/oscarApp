package com.example.oscarapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        clearSharedPreferences()

        val username = findViewById<EditText>(R.id.etUsername)
        val password = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val progressBar = findViewById<ProgressBar>(R.id.progressBarLogin)

        btnLogin.setOnClickListener {
            val user = username.text.toString()
            val pass = password.text.toString()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Exibe o ProgressBar e desabilita o botão enquanto a autenticação ocorre
            progressBar.visibility = View.VISIBLE
            btnLogin.isEnabled = false

            authenticate(user, pass, progressBar, btnLogin)
        }
    }

    private fun authenticate(username: String, password: String, progressBar: ProgressBar, btnLogin: Button) {
        val api = RetrofitClientLogin.instance.create(ApiService::class.java)
        val loginRequest = LoginRequest(username, password)

        api.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                progressBar.visibility = View.GONE
                btnLogin.isEnabled = true

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        saveToken(loginResponse.token ?: "", loginResponse.usuarioId)
                        if (loginResponse.votos != null) {
                            saveVote(loginResponse.votos)
                        }
                        Toast.makeText(this@LoginActivity, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                        navigateToNextActivity()
                    } else {
                        Toast.makeText(this@LoginActivity, loginResponse?.message ?: "Erro desconhecido", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Erro no login: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                btnLogin.isEnabled = true
                Toast.makeText(this@LoginActivity, "Falha na comunicação: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveVote(voto: Vote) {
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("VOTO_FILME", voto.filme)
        editor.putInt("VOTO_DIRETOR", voto.diretor)
        editor.apply()
    }

    private fun saveToken(token: String, id: Int) {
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("AUTH_TOKEN", token)
        editor.putInt("USER_ID", id)
        editor.apply()
    }

    private fun navigateToNextActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun clearSharedPreferences() {
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}


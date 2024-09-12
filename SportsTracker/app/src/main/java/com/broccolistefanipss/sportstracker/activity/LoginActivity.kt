package com.broccolistefanipss.sportstracker.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.broccolistefanipss.sportstracker.databinding.ActivityLoginBinding
import com.broccolistefanipss.sportstracker.manager.LoginManager
import com.broccolistefanipss.sportstracker.manager.SessionManager

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sessionManager = SessionManager(this)

        // controlla se l'utente è già loggato
        if (sessionManager.isLoggedIn) {
            val intent = Intent(this, BotMenuActivity::class.java)
            startActivity(intent)
            finish()
        }

        val usernameEditText: EditText = binding.usernameLogin
        val passwordEditText: EditText = binding.passwordLogin
        val loginButton: Button = binding.loginButton
        val welcomeButton: TextView = binding.welcomeButton

        // gestisce click del pulsante di login
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Inserisci sia nome utente che password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // utilizza LoginManager per tentare il login
            val loginManager = LoginManager(this)
            if (loginManager.attemptLogin(username, password)) {
                // se il login ha successo, imposta i valori nel SessionManager
                sessionManager.setLogin(true)
                sessionManager.userName = username

                // vai alla BotMenuActivity
                val intent = Intent(this, BotMenuActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "UserName o password errati, riprova", Toast.LENGTH_SHORT).show()
            }
        }

        // pulsante per reindirizzare alla WelcomeActivity
        welcomeButton.setOnClickListener {
            btnWelcomeOnclick()
        }
    }

    private fun btnWelcomeOnclick() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }
}
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
    private val usernameEditText: EditText = binding.usernameLogin
    private val passwordEditText: EditText = binding.passwordLogin
    private val loginButton: Button = binding.loginButton
    private val welcomeButton: TextView = binding.welcomeButton
    private val sessionManager = SessionManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // controlla se l'utente è già loggato
        if (sessionManager.isLoggedIn) {
            val intent = Intent(this, BotMenuActivity::class.java)
            startActivity(intent)
            finish()
        }

        // gestisce click del pulsante di login
        loginButton.setOnClickListener {
            btnLoginOnClick()
        }

        // pulsante per reindirizzare alla WelcomeActivity
        welcomeButton.setOnClickListener {
            btnWelcomeOnclick()
        }
    }

    private fun btnLoginOnClick() {
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Inserisci sia nome utente che password", Toast.LENGTH_SHORT).show()
            return
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
    private fun btnWelcomeOnclick() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }
}
package com.broccolistefanipss.esamedazero.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.broccolistefanipss.esamedazero.R
import com.broccolistefanipss.esamedazero.manager.LoginManager
import com.broccolistefanipss.esamedazero.manager.SessionManager

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // Imposta il layout dell'activity

        // Crea un'istanza di SessionManager
        val sessionManager = SessionManager(this)

        // Controlla se l'utente è già loggato
        if (sessionManager.isLoggedIn) {
            // Se l'utente è già loggato, reindirizza all'activity BotMenuActivity
            val intent = Intent(this, BotMenuActivity::class.java)
            startActivity(intent)
            finish() // Termina LoginActivity
        }

        // Trova gli elementi dell'interfaccia utente nel layout
        val usernameEditText: EditText = findViewById(R.id.usernameLogin)
        val passwordEditText: EditText = findViewById(R.id.passwordLogin)
        val loginButton: Button = findViewById(R.id.loginButton)

        // Imposta un listener sul pulsante di login
        loginButton.setOnClickListener {
            // Ottiene il nome utente e la password inseriti dall'utente
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Validazione dell'input dell'utente
            if (username.isEmpty() || password.isEmpty()) {
                // Se il nome utente o la password sono vuoti, mostra un messaggio all'utente
                Toast.makeText(this, "Per favore, inserisci sia il nome utente che la password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Crea un'istanza di LoginManager e tenta il login
            val loginManager = LoginManager(this)

            if (loginManager.attemptLogin(username, password)) {
                // Se il login ha successo, salva lo stato di login e reindirizza all'activity BotMenuActivity
                sessionManager.createLoginSession(username)
                val intent = Intent(this, BotMenuActivity::class.java)
                startActivity(intent)
                finish() // Termina LoginActivity

            } else {
                // Se il login fallisce, mostra un messaggio all'utente
                Toast.makeText(this, "Login fallito. Riprova.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

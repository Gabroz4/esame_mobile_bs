package com.broccolistefanipss.sportstracker.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.broccolistefanipss.sportstracker.databinding.ActivityLoginBinding
import com.broccolistefanipss.sportstracker.manager.LoginManager
import com.broccolistefanipss.sportstracker.manager.SessionManager

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginManager: LoginManager
    private val sessionManager by lazy { SessionManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // check se utente è loggato
        if (sessionManager.isLoggedIn) {
            val intent = Intent(this, BotMenuActivity::class.java)
            startActivity(intent)
            finish()
        }

        loginManager = LoginManager(this)

        // bottone per login
        binding.loginButton.setOnClickListener {
            btnLoginOnClick()
        }

        // bottone per welcomeactivity
        binding.welcomeButton.setOnClickListener {
            btnWelcomeOnclick()
        }
    }

    private fun btnLoginOnClick() {
        val username = binding.usernameLogin.text.toString()
        val password = binding.passwordLogin.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Inserisci sia nome utente che password", Toast.LENGTH_SHORT).show()
            return
        }

        // tenta il login
        if (loginManager.attemptLogin(username, password)) {
            // se ha successo mette i dati in sessionManager
            sessionManager.setLogin(true)
            sessionManager.userName = username
            //e va a botMenuActivity
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

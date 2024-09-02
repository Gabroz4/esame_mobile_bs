package com.broccolistefanipss.esamedazero.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.broccolistefanipss.esamedazero.databinding.ActivityLoginBinding
import com.broccolistefanipss.esamedazero.manager.LoginManager
import com.broccolistefanipss.esamedazero.manager.SessionManager

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sessionManager = SessionManager(this)
        if (sessionManager.isLoggedIn) {
            val intent = Intent(this, BotMenuActivity::class.java)
            startActivity(intent)
            finish()
        }

        val usernameEditText: EditText = binding.usernameLogin
        val passwordEditText: EditText = binding.passwordLogin
        val loginButton: Button = binding.loginButton
        val welcomeButton: Button = binding.welcomeButton

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Per favore inserisci sia nome utente che password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loginManager = LoginManager(this)
            if (loginManager.attemptLogin(username, password)) {
                saveUsernameToSharedPreferences(binding.usernameLogin.toString())
                Log.d("Username vecchio:", username.toString())
                Log.d("Username nuovo:", binding.usernameLogin.toString())
                sessionManager.createLoginSession(username)
                val intent = Intent(this, BotMenuActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Login fallito, riprova", Toast.LENGTH_SHORT).show()
            }
        }

        welcomeButton.setOnClickListener {
            btnWelcome_onclick()
        }
    }
    private fun saveUsernameToSharedPreferences(username: String) {
        val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userName", username)
        editor.apply()

        Log.d("LoginActivity", "Username saved in SharedPreferences: $username")
    }
    private fun btnWelcome_onclick() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }
}
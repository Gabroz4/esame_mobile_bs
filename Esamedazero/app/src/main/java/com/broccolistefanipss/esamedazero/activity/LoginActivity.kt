package com.broccolistefanipss.esamedazero.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.broccolistefanipss.esamedazero.R
import com.broccolistefanipss.esamedazero.manager.LoginManager

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // Assicurati di avere questo layout.

        val usernameEditText: EditText = findViewById(R.id.usernameLogin)
        val passwordEditText: EditText = findViewById(R.id.passwordLogin)
        val loginButton: Button = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val loginManager = LoginManager(this)

            if (loginManager.attemptLogin(username, password)) {
                // login successo, prossima activity
                val intent = Intent(this, BotMenuActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // login fallito
                Toast.makeText(this, "Login fallito. Riprova.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
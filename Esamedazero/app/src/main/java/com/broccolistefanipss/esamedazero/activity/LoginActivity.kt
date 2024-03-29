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
        setContentView(R.layout.activity_login)

        val sessionManager = SessionManager(this)
        if (sessionManager.isLoggedIn) {
            val intent = Intent(this, BotMenuActivity::class.java)
            startActivity(intent)
            finish()
        }

        val usernameEditText: EditText = findViewById(R.id.usernameLogin)
        val passwordEditText: EditText = findViewById(R.id.passwordLogin)
        val loginButton: Button = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Per favore, inserisci sia il nome utente che la password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loginManager = LoginManager(this)

            if (loginManager.attemptLogin(username, password)) {
                sessionManager.createLoginSession(username)
                val intent = Intent(this, BotMenuActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Login fallito. Riprova.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

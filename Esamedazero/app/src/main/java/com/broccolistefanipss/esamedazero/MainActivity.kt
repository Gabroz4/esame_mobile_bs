package com.broccolistefanipss.esamedazero

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.broccolistefanipss.esamedazero.activity.BotMenuActivity
import com.broccolistefanipss.esamedazero.activity.WelcomeActivity
import com.broccolistefanipss.esamedazero.manager.SessionManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // applica splash
        installSplashScreen().apply {
        }

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Controlla se l'utente è già loggato
        val sessionManager = SessionManager(this)
        if (sessionManager.isLoggedIn) {
            // Reindirizza l'utente alla BotMenuActivity
            val intent = Intent(this, BotMenuActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // Avvia la WelcomeActivity
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish() // termina asttività, impedisce di tornare allo splash
        }
    }
}


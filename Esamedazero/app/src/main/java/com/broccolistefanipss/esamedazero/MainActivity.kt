package com.broccolistefanipss.esamedazero

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.broccolistefanipss.esamedazero.activity.WelcomeActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // applica splash
        installSplashScreen().apply {
        }

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        startActivity(Intent(this, WelcomeActivity::class.java))
        finish() // termina asttività, impedisce di tornare allo splash
    }
}

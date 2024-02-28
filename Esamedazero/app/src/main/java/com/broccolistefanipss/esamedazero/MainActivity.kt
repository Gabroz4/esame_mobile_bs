package com.broccolistefanipss.esamedazero

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.broccolistefanipss.esamedazero.activity.WelcomeActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // This line is crucial to initialize the new splash screen API and should be the first line in onCreate.
        installSplashScreen().apply {
            // If you need to keep the splash screen for a longer duration, use setKeepOnScreenCondition.
            // However, it's recommended to let the system determine the optimal time to display the splash based on your app's loading time.
        }

        super.onCreate(savedInstanceState)

        // Assuming `activity_main` is the layout for MainActivity after the splash screen.
        setContentView(R.layout.activity_main)

        // Intent to start WelcomeActivity, consider moving this to a more appropriate lifecycle callback if needed.
        // For example, you might want to move this intent into onResume or onStart to ensure the user interface is fully initialized.
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish() // Finish MainActivity to prevent the user from returning to the splash screen.
    }
}

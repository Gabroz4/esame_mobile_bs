// src/com/broccolistefanipss/sportstracker/WelcomeActivity.kt
package com.broccolistefanipss.sportstracker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.broccolistefanipss.sportstracker.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(getString(R.string.user_prefs), MODE_PRIVATE)

        if (!isWelcomeCompleted()) {
            setupWelcomeScreen()
        } else {
            navigateToMainActivity()
        }
    }

    private fun isWelcomeCompleted(): Boolean {
        return sharedPreferences.getBoolean(getString(R.string.key_welcome_completed), false)
    }

    private fun setupWelcomeScreen() {
        val editTextName: EditText = binding.editTextName
        val btnNext: Button = binding.btnNext

        btnNext.setOnClickListener {
            val userName = editTextName.text.toString().trim()

            if (userName.isNotEmpty()) {
                saveWelcomeCompletedFlag()
                saveUserName(userName)

                navigateToMainActivity()
            } else {
                saveUserName("user")
            }
        }
    }

    private fun saveWelcomeCompletedFlag() {
        sharedPreferences.edit().putBoolean(getString(R.string.key_welcome_completed), true).apply()
    }

    private fun saveUserName(userName: String) {
        sharedPreferences.edit().putString(getString(R.string.user_name), userName).apply()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Optional: Close the current activity
    }
}

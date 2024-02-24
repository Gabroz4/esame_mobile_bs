package com.broccolistefanipss.esamedazero.activity

import android.content.Intent
import android.widget.Button
import com.broccolistefanipss.esamedazero.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.broccolistefanipss.esamedazero.global.DB

class WelcomeActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val nextButton = findViewById<Button>(R.id.nextButton)

        val intent = Intent(this, BotMenuActivity::class.java)

        nextButton.setOnClickListener {
            startActivity(intent)
        }
    }
}
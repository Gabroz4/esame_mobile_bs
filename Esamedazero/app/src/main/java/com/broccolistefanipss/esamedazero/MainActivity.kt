package com.broccolistefanipss.esamedazero

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.broccolistefanipss.esamedazero.activity.BotMenuActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nextButton = findViewById<Button>(R.id.nextButton)

        val intent = Intent(this, BotMenuActivity::class.java)

        nextButton.setOnClickListener {
            startActivity(intent)
        }

    }
}
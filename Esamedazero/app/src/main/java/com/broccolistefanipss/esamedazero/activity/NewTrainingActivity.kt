package com.broccolistefanipss.esamedazero.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.broccolistefanipss.esamedazero.databinding.ActivityNewTrainingBinding

class NewTrainingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewTrainingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startStopButton.setOnClickListener {
            // Handle button click
        }
    }
}

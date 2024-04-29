package com.broccolistefanipss.esamedazero.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import java.time.Duration
import java.time.format.DateTimeFormatter
import androidx.core.util.TimeUtils.formatDuration
import androidx.appcompat.app.AppCompatActivity
import com.broccolistefanipss.esamedazero.databinding.ActivityNewTrainingBinding

class NewTrainingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewTrainingBinding

    private var isRunning = false
    private var startTime: Long = 0L  // Timestamp di inizio (millisecondi)
    private var elapsedTime: Long = 0L  // Tempo trascorso dall'inizio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startStopButton.setOnClickListener {
            if (!isRunning) {
                isRunning = true
                binding.startStopButton.text = "Stop"
                startTime = System.currentTimeMillis()
                startTimer() // Inizio del timer
            } else {
                isRunning = false
                binding.startStopButton.text = "Start"
                stopTimer()
                // Salva la sessione d'allenamento nel database
            }
        }
    }

    private val timerHandler = Handler(Looper.getMainLooper())
    private val timerRunnable = object : Runnable {
        override fun run() {
            updateTimer()
            timerHandler.postDelayed(this, 1000) // Richama ogni secondo
        }
    }

    private fun formatDuration(durationInMillis: Long): String {
        val duration = Duration.ofMillis(durationInMillis)
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        return duration.toString()
    }

    private fun startTimer() {
        timerHandler.postDelayed(timerRunnable, 0) // Avvio immediato del timer
    }

    private fun stopTimer() {
        timerHandler.removeCallbacks(timerRunnable)
    }

    private fun updateTimer() {
        if (isRunning) {
            val currentTime = System.currentTimeMillis()
            val newElapsedTime = currentTime - startTime + elapsedTime
            binding.timeTextView.text = formatDuration(newElapsedTime) // Mostra il tempo trascorso
        }
    }
}

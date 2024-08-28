package com.broccolistefanipss.esamedazero.activity

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.broccolistefanipss.esamedazero.R
import com.broccolistefanipss.esamedazero.databinding.ActivityNewTrainingBinding
import com.broccolistefanipss.esamedazero.global.DB
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.sqrt

class NewTrainingActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityNewTrainingBinding
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    private var isRunning = false
    private var startTime: Long = 0L
    private var elapsedTime: Long = 0L

    private var totalAcceleration: Double = 0.0
    private var calorieCount: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // Setup sensore accelerometro
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        binding.startStopButton.setOnClickListener {
            if (!isRunning) {
                isRunning = true
                binding.startStopButton.text = R.string.stop.toString()
                startTime = System.currentTimeMillis()
                startTimer()
                startAccelerometer()
            } else {
                isRunning = false
                binding.startStopButton.text = R.string.start.toString()
                stopTimer()
                stopAccelerometer()
                saveTrainingSession() // Salva la sessione d'allenamento nel database
                resetTrainingData() // Resetta i dati per una nuova sessione
            }
        }
    }

    private val timerHandler = Handler(Looper.getMainLooper())
    private val timerRunnable = object : Runnable {
        override fun run() {
            updateTimer()
            timerHandler.postDelayed(this, 1000)
        }
    }

    private fun formatDuration(durationInMillis: Long): String {
        val hours = durationInMillis / 3600000
        val minutes = (durationInMillis % 3600000) / 60000
        val seconds = (durationInMillis % 60000) / 1000
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun startTimer() {
        timerHandler.postDelayed(timerRunnable, 0)
    }

    private fun stopTimer() {
        if (isRunning) {
            val currentTime = System.currentTimeMillis()
            elapsedTime += currentTime - startTime // Aggiorna elapsedTime
        }
        timerHandler.removeCallbacks(timerRunnable)
    }

    private fun updateTimer() {
        if (isRunning) {
            val currentTime = System.currentTimeMillis()
            val newElapsedTime = currentTime - startTime + elapsedTime
            binding.timeTextView.text = formatDuration(newElapsedTime)
            binding.calorieTextView.text = String.format("Calories: %.2f", calorieCount)
        }
    }

    private fun startAccelerometer() {
        accelerometer?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    private fun stopAccelerometer() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (isRunning && event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            // Calcolo la magnitudo dell'accelerazione
            val accelerationMagnitude = sqrt((x * x + y * y + z * z).toDouble())

            // Aggiungi la magnitudo totale per l'uso nel calcolo delle calorie
            totalAcceleration += accelerationMagnitude

            // Calcola le calorie bruciate basandosi sull'accelerazione totale (stima basilare)
            calorieCount = totalAcceleration * 0.01 // Un fattore arbitrario per convertire l'accelerazione in calorie
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Non serve gestire i cambiamenti di accuratezza per questo caso
    }

    private fun saveTrainingSession() {
        val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)

        val userName = sharedPreferences.getString("userName", null) // Sostituisci con il nome dell'utente corrente
        val sessionDate = getCurrentDate() // Ottieni la data corrente in formato stringa
        //val duration = (elapsedTime / 1000).toInt() // Converti la durata in secondi
        val duration = elapsedTime.toInt()
        val trainingType = "corsa" // Sostituisci con il tipo di allenamento
        val burntCalories = calorieCount.toInt()

        // Inserisci la sessione nel database
        val db = DB(this)
        if (userName != null) {
            db.insertTrainingSession(userName, sessionDate, duration, trainingType, burntCalories)
        }
        Log.d("NewTrainingActivity", "Salvataggio sessione in corso: $userName, $sessionDate, $duration, $trainingType, $burntCalories")
    }

    private fun getCurrentDate(): String {
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return formatter.format(Date())
    }

    private fun resetTrainingData() {
        totalAcceleration = 0.0
        calorieCount = 0.0
        elapsedTime = 0L
    }
}

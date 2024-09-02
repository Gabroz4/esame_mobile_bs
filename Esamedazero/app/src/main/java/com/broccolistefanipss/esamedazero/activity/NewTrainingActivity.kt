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
import com.broccolistefanipss.esamedazero.manager.SharedPrefs
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
                startTraining()
            } else {
                stopTraining()
            }
        }

        binding.closeButton.setOnClickListener {
            closeTraining()
        }
    }

    private val timerHandler = Handler(Looper.getMainLooper())
    private val timerRunnable = object : Runnable {
        override fun run() {
            elapsedTime = updateTimer(); //per trasfoprmare in minuti --> updateTimer() / 60
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
            Log.d("NewTrainingActivity", "Elapsed time (ms): $elapsedTime")
        }
        timerHandler.removeCallbacks(timerRunnable)
    }

    private fun updateTimer(): Long {
        if (isRunning) {
            val currentTime = System.currentTimeMillis()
            val newElapsedTime = currentTime - startTime + 1
            Log.d("NewTrainingActivity", "Timer update: newElapsedTime (ms): $newElapsedTime")

            binding.timeTextView.text = formatDuration(newElapsedTime)
            binding.calorieTextView.text = String.format(Locale.getDefault(), "Calories: %.2f", calorieCount)
            return newElapsedTime
        }
        return 0;
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
            calorieCount = totalAcceleration * 0.01 // Fattore arbitrario per convertire l'accelerazione in calorie
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Non serve gestire i cambiamenti di accuratezza in questo caso
    }

    //private fun saveTrainingSession() {
    //    val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
//
    //    val userName = sharedPreferences.getString("userName", null)
    //    val sessionDate = getCurrentDate()
    //    val durationInSeconds = (elapsedTime / 1000).toInt() // Converti la durata in secondi
//
    //    Log.d("NewTrainingActivity", "Elapsed time in seconds: $durationInSeconds") // Aggiungi un log per il debug
//
    //    val trainingType = "corsa"
    //    val burntCalories = calorieCount.toInt()
    //    val trainingId = SharedPrefs.getInt()
//
    //    // Inserisci la sessione nel database
    //    val db = DB(this)
    //    if (userName != null) {
    //        db.insertTrainingSession(userName, sessionDate, durationInSeconds, trainingType, burntCalories)
    //    }
    //    Log.d("NewTrainingActivity", "Salvataggio sessione in corso: $userName, $sessionDate, $durationInSeconds, $trainingType, $burntCalories")
    //}

    private fun saveTrainingSession() {
        val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)

        val userName = sharedPreferences.getString("userName", null)
        val sessionDate = getCurrentDate()
        val durationInSeconds = (elapsedTime / 1000).toInt() // Converti la durata in secondi

        Log.d("NewTrainingActivity", "Elapsed time in seconds: $durationInSeconds") // Log per il debug

        val trainingType = "corsa"
        val burntCalories = calorieCount.toInt()

        // Inserisci la sessione nel database e ottieni l'ID della sessione appena inserita
        val db = DB(this)
        if (userName != null) {
            val sessionId = db.insertTrainingSession(userName, sessionDate, durationInSeconds, trainingType, burntCalories)
            Log.d("NewTrainingActivity", "Session ID: $sessionId, Salvataggio sessione in corso: $userName, $sessionDate, $durationInSeconds, $trainingType, $burntCalories")
        } else {
            Log.e("NewTrainingActivity", "Errore: userName non trovato")
        }
    }


    private fun getCurrentDate(): String {
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return formatter.format(Date())
    }

    private fun startTraining() {
        isRunning = true
        binding.startStopButton.text = getString(R.string.stop)
        startTime = System.currentTimeMillis()
        startTimer()
        startAccelerometer()
        binding.closeButton.isEnabled = false // Disabilita il bottone Close durante l'allenamento
    }

    private fun stopTraining() {
        isRunning = false
        binding.startStopButton.text = getString(R.string.start)
        stopTimer()
        stopAccelerometer()
        saveTrainingSession()
        binding.closeButton.isEnabled = true // Riabilita il bottone Close
    }

    private fun closeTraining() {
        if (isRunning) {
            stopTraining()
        }
        resetTrainingData()
        finish() // Chiude l'activity e torna alla precedente
    }

    private fun resetTrainingData() {
        totalAcceleration = 0.0
        calorieCount = 0.0
        elapsedTime = 0L
        binding.timeTextView.text = "00:00:00"
        binding.calorieTextView.text = "Calorie: 0.00"
    }
}
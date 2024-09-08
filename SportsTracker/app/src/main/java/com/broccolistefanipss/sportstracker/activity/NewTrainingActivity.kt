package com.broccolistefanipss.sportstracker.activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.broccolistefanipss.sportstracker.R
import com.broccolistefanipss.sportstracker.databinding.ActivityNewTrainingBinding
import com.broccolistefanipss.sportstracker.global.DB
import com.broccolistefanipss.sportstracker.model.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.sqrt

class NewTrainingActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityNewTrainingBinding
    private lateinit var sensorManager: SensorManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    private val locationList = mutableListOf<Location>()

    private var accelerometer: Sensor? = null

    private var isRunning = false
    private var startTime: Long = 0L
    private var elapsedTime: Long = 0L

    private var totalAcceleration: Double = 0.0
    private var calorieCount: Double = 0.0
    private lateinit var db: DB

    private val LOCATION_PERMISSION_REQUEST_CODE = 1001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityNewTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        db = DB(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000) // 5 secondi
            .setMinUpdateIntervalMillis(5000) // Intervallo minimo di aggiornamento: 5 secondi
            .setMinUpdateDistanceMeters(10f) // Aggiornamento ogni 10 metri
            .setWaitForAccurateLocation(false) // Se impostato a true, aspetta che la posizione sia precisa
            .build()

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

        // Inizialmente disabilita il pulsante di chiusura
        binding.closeButton.isEnabled = false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // I permessi sono stati concessi
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        // Avvia gli aggiornamenti della posizione
                        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                    }
                } else {
                    // I permessi sono stati negati
                    Toast.makeText(this, "Permesso di accesso alla posizione negato", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private val timerHandler = Handler(Looper.getMainLooper())
    private val timerRunnable = object : Runnable {
        override fun run() {
            elapsedTime = updateTimer()
            timerHandler.postDelayed(this, 1000)
        }
    }

    private fun startLocationUpdates() {
        //val locationRequest = LocationRequest.create().apply {
        //    interval = 10000  // Aggiornamenti ogni 10 secondi
        //    fastestInterval = 5000
        //    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        //}

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    updateLocation(location)
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Richiedi i permessi all'utente
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

// Se i permessi sono già stati concessi, inizia gli aggiornamenti della posizione
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

    }

    private fun printLocationData() {
        if (locationList.isNotEmpty()) {
            Log.d("NewTrainingActivity", "Location Data:")
            locationList.forEach { location ->
                Log.d("NewTrainingActivity", "Latitude: ${location.latitude}, Longitude: ${location.longitude}, Timestamp: ${location.timestamp}")
            }
        } else {
            Log.d("NewTrainingActivity", "No location data available.")
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun updateLocation(location: android.location.Location) {
        val newLocation = Location(
            latitude = location.latitude,
            longitude = location.longitude,
            timestamp = System.currentTimeMillis()
        )
        locationList.add(newLocation)
        Log.d("NewTrainingActivity", "Posizione aggiunta: $newLocation")
    }




    private fun formatDuration(durationInMillis: Long): String {
        val hours = durationInMillis / 3600000
        val minutes = (durationInMillis % 3600000) / 60000
        val seconds = (durationInMillis % 60000) / 1000
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun startTimer() {
        resetTrainingData()
        timerHandler.postDelayed(timerRunnable, 0)
    }

    private fun stopTimer() {
        if (isRunning) {
            val currentTime = System.currentTimeMillis()
            elapsedTime += currentTime - startTime
        }
        timerHandler.removeCallbacks(timerRunnable)
    }

    private fun updateTimer(): Long {
        if (isRunning) {
            val currentTime = System.currentTimeMillis()
            val newElapsedTime = currentTime - startTime + 1

            binding.timeTextView.text = formatDuration(newElapsedTime)
            binding.calorieTextView.text = String.format(Locale.getDefault(), "Calorie: %.2f", calorieCount)
            return newElapsedTime
        }
        return 0
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

            val accelerationMagnitude = sqrt((x * x + y * y + z * z).toDouble())
            totalAcceleration += accelerationMagnitude

            calorieCount = totalAcceleration * 0.01
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Non implementato
    }

    private fun saveTrainingSession() {
        val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("userName", null)
        val sessionDate = getCurrentDate()
        val durationInSeconds = (elapsedTime / 1000).toInt()

        val trainingType = "corsa"
        val burntCalories = calorieCount.toInt()

        val db = DB(this)
        if (userName != null) {
            val sessionId = db.insertTrainingSession(userName, sessionDate, durationInSeconds, trainingType, burntCalories)
            // Salva anche il percorso nel database
            for (location in locationList) {
                db.insertTrainingLocation(sessionId, location.latitude, location.longitude, location.timestamp)
            }
            Log.d("NewTrainingActivity", "Sessione salvata con percorso")
        } else {
            Log.e("NewTrainingActivity", "Errore: userName non trovato")
        }
    }


    //private fun saveTrainingSession() {
    //    val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
//
    //    val userName = sharedPreferences.getString("userName", null)
//
    //    val sessionDate = getCurrentDate()
    //    val durationInSeconds = (elapsedTime / 1000).toInt()
//
    //    Log.d("NewTrainingActivity", "Elapsed time in seconds: $durationInSeconds")
//
    //    val trainingType = "corsa"
    //    val burntCalories = calorieCount.toInt()
//
    //    val db = DB(this)
    //    if (userName != null) {
    //        val sessionId = db.insertTrainingSession(userName, sessionDate, durationInSeconds, trainingType, burntCalories)
    //        Log.d("NewTrainingActivity", "Session ID: $sessionId, Salvataggio sessione in corso: $userName, $sessionDate, $durationInSeconds, $trainingType, $burntCalories")
    //    } else {
    //        Log.e("NewTrainingActivity", "Errore: userName non trovato")
    //    }
    //}

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
        startLocationUpdates()  // Inizia il tracciamento della posizione
        binding.closeButton.isEnabled = false
        updateButtonColor()
    }


    private fun updateButtonColor() {
        if (binding.closeButton.isEnabled) {
            binding.closeButton.setBackgroundResource(R.drawable.button_style) // colore del bottone normale
        } else {
            binding.closeButton.setBackgroundResource(R.drawable.button_style_disabled) // colore del bottone disabilitato
        }
    }
    private fun stopTraining() {
        isRunning = false
        binding.startStopButton.text = getString(R.string.start)
        stopTimer()
        stopAccelerometer()
        stopLocationUpdates()  // Ferma il tracciamento della posizione
        saveTrainingSession()

        printLocationData()
        binding.closeButton.isEnabled = true
        updateButtonColor()
    }

    private fun closeTraining() {
        if (isRunning) {
            stopTraining()
        }
        resetTrainingData()
        finish()
    }

    private fun resetTrainingData() {
        totalAcceleration = 0.0
        calorieCount = 0.0
        elapsedTime = 0L
        binding.timeTextView.text = getString(R.string.reset_time)
        binding.calorieTextView.text = getString(R.string.reset_calories)
    }
}
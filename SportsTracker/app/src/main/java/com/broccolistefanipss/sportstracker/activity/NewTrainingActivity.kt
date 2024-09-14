package com.broccolistefanipss.sportstracker.activity

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.broccolistefanipss.sportstracker.R
import com.broccolistefanipss.sportstracker.databinding.ActivityNewTrainingBinding
import com.broccolistefanipss.sportstracker.global.DB
import com.broccolistefanipss.sportstracker.manager.SessionManager
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
    private lateinit var sessionManager: SessionManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var sportSelectSpinner: Spinner
    private lateinit var db: DB

    private val locationList = mutableListOf<Location>()
    private val timerHandler = Handler(Looper.getMainLooper())

    private var accelerometer: Sensor? = null

    private var isRunning = false
    private var startTime: Long = 0L
    private var elapsedTime: Long = 0L
    private var totalAcceleration: Double = 0.0
    private var calorieCount: Double = 0.0
    private var totalDistance: Float = 0.0F

    private val locationPermissionRequestCode = 1001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        db = DB(this)
        sessionManager = SessionManager(this)
        sportSelectSpinner = binding.sportSelectSpinner

        val sportOptions : Array<String> = resources.getStringArray(R.array.sports_array)
        sportSelectSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sportOptions)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000) // 5 secondi
            .setMinUpdateIntervalMillis(5000) // intervallo minimo di aggiornamento: 5 secondi
            .setMinUpdateDistanceMeters(10f) // aggiornamento ogni 10 metri
            .setWaitForAccurateLocation(false) // se impostato a true aspetta che la posizione sia precisa
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

        // inizialmente disabilita il pulsante di chiusura
        binding.closeButton.isEnabled = false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            locationPermissionRequestCode -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permessi concessi
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        // avvia gli aggiornamenti della posizione
                        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                    }
                } else {
                    // permessi negati
                    Toast.makeText(this, "Permesso di accesso alla posizione negato", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (isRunning && event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val accelerationMagnitude = sqrt((x * x + y * y + z * z).toDouble())
            totalAcceleration += accelerationMagnitude

            // calcola le calorie in base al tipo di sport selezionato e alla distanza percorsa
            calorieCount = calculateCalories(sportSelectSpinner.selectedItem.toString(), totalAcceleration, totalDistance)
        }
    }

    // funzione per calcolare le calorie tenendo conto della distanza
    private fun calculateCalories(sport: String, acceleration: Double, distance: Float): Double {
        val calorieBurnRate = when (sport) {
            "Corsa" -> 0.05 // fattore di calcolo per la corsa
            "Bicicletta" -> 0.03 // la bicicletta è più efficiente
            else -> 0.0
        }

        // calcola le calorie usando sia l'accelerazione che la distanza (calcolo finto)
        return ((calorieBurnRate * acceleration) + (distance * 0.1))/4
    }

    private val timerRunnable = object : Runnable {
        override fun run() {
            elapsedTime = updateTimer()
            timerHandler.postDelayed(this, 1000)
        }
    }

    private fun startLocationUpdates() {
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
            // richiedi i permessi
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                locationPermissionRequestCode
            )
            return
        }
        // se i permessi sono già stati concessi, inizia gli aggiornamenti della posizione
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun printLocationData() {
        if (locationList.isNotEmpty()) {
            Log.d("NewTrainingActivity", "Dati posizione:")
            locationList.forEach { location ->
                Log.d("NewTrainingActivity", "Latitudine: ${location.latitude}, Longitudine: ${location.longitude}, Timestamp: ${location.timestamp}")
            }
        } else {
            Log.d("NewTrainingActivity", "Nessun dato disponibile sulla posizione")
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

        if (locationList.isNotEmpty()) {
            // calcola la distanza dall'ultima posizione
            val lastLocation = locationList.last()
            val previousLocation = android.location.Location("").apply {
                latitude = lastLocation.latitude
                longitude = lastLocation.longitude
            }
            val distanceToLast = previousLocation.distanceTo(location)
            totalDistance += distanceToLast

            // calcola il tempo trascorso dall'ultima posizione
            val timeDifference = (newLocation.timestamp - lastLocation.timestamp) / 1000.0 // in secondi

            // evita divisioni per zero
            if (timeDifference > 0) {
                val currentSpeed = (distanceToLast / timeDifference) * 3.6 // km/h
                binding.speedTextView.text = String.format(Locale.getDefault(), "Velocità: %.2f km/h", currentSpeed)
            }
        }
        locationList.add(newLocation)
        Log.d("NewTrainingActivity", "Posizione aggiunta: $newLocation, Distanza: $totalDistance m")
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
            binding.calorieTextView.text = String.format(Locale.getDefault(), "Calorie: %.2f kcal", calorieCount)
            binding.distanzaTextView.text = String.format(Locale.getDefault(), "Distanza: %.2f m", totalDistance)
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

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Non implementato
    }

    private fun saveTrainingSession() {
        val userName = sessionManager.userName
        val sessionDate = getCurrentDate()
        val durationInSeconds = (elapsedTime / 1000).toInt()

        val trainingType = sportSelectSpinner.selectedItem.toString()
        val burntCalories = calorieCount.toInt()
        val distance = totalDistance

        val db = DB(this)
        if (userName != null) {
            val sessionId = db.insertTrainingSession(userName, sessionDate, durationInSeconds, distance, trainingType, burntCalories)
            // salva percorso nel db
            for (location in locationList) {
                db.insertTrainingLocation(sessionId, location.latitude, location.longitude, location.timestamp)
            }
            Log.d("NewTrainingActivity", "Sessione salvata con dati posizione")
        } else {
            Log.e("NewTrainingActivity", "Errore: username non trovato")
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
        startLocationUpdates()
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
        stopLocationUpdates()
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
        totalDistance = 0F
        binding.timeTextView.text = getString(R.string.reset_time)
        binding.calorieTextView.text = getString(R.string.reset_calories)
    }
}
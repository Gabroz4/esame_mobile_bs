package com.broccolistefanipss.sportstracker.activity_vms
//
//import android.app.Application
//import android.os.Handler
//import android.os.Looper
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.broccolistefanipss.sportstracker.global.DB
//import com.broccolistefanipss.sportstracker.manager.SessionManager
//import com.broccolistefanipss.sportstracker.model.Location
//import com.google.android.gms.location.*
//import java.text.SimpleDateFormat
//import java.util.*
//import kotlin.math.sqrt
//
//class NewTrainingViewModel(application: Application) : AndroidViewModel(application) {
//
//    private val db: DB = DB(getApplication())
//    private val sessionManager: SessionManager = SessionManager(getApplication())
//    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplication())
//
//    private val _isRunning = MutableLiveData<Boolean>()
//    val isRunning: LiveData<Boolean> = _isRunning
//
//    private val _elapsedTime = MutableLiveData<Long>()
//    val elapsedTime: LiveData<Long> = _elapsedTime
//
//    private val _calorieCount = MutableLiveData<Double>()
//    val calorieCount: LiveData<Double> = _calorieCount
//
//    private val _totalDistance = MutableLiveData<Float>()
//    val totalDistance: LiveData<Float> = _totalDistance
//
//    private val _currentSpeed = MutableLiveData<Double>()
//    val currentSpeed: LiveData<Double> = _currentSpeed
//
//    private val _shouldFinish = MutableLiveData<Boolean>()
//    val shouldFinish: LiveData<Boolean> = _shouldFinish
//
//    private val locationList = mutableListOf<Location>()
//    private val timerHandler = Handler(Looper.getMainLooper())
//
//    private var startTime: Long = 0L
//    private var totalAcceleration: Double = 0.0
//
//    private lateinit var locationCallback: LocationCallback
//    private lateinit var locationRequest: LocationRequest
//
//    init {
//        _isRunning.value = false
//        _elapsedTime.value = 0L
//        _calorieCount.value = 0.0
//        _totalDistance.value = 0F
//        _currentSpeed.value = 0.0
//
//        setupLocationRequest()
//    }
//
//    private fun setupLocationRequest() {
//        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
//            .setMinUpdateIntervalMillis(5000)
//            .setMinUpdateDistanceMeters(10f)
//            .setWaitForAccurateLocation(false)
//            .build()
//
//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                super.onLocationResult(locationResult)
//                for (location in locationResult.locations) {
//                    updateLocation(location)
//                }
//            }
//        }
//    }
//
//    fun startTraining(toString: String) {
//        _isRunning.value = true
//        startTime = System.currentTimeMillis()
//        startTimer()
//        startLocationUpdates()
//    }
//
//    fun stopTraining(toString: String) {
//        _isRunning.value = false
//        stopTimer()
//        stopLocationUpdates()
//        saveTrainingSession()
//    }
//
//    fun closeTraining() {
//        if (_isRunning.value == true) {
//            stopTraining(binding.sportSelectSpinner.selectedItem.toString())
//        }
//        resetTrainingData()
//        _shouldFinish.value = true
//    }
//
//    private fun startTimer() {
//        timerHandler.post(object : Runnable {
//            override fun run() {
//                updateTimer()
//                if (_isRunning.value == true) {
//                    timerHandler.postDelayed(this, 1000)
//                }
//            }
//        })
//    }
//
//    private fun stopTimer() {
//        timerHandler.removeCallbacksAndMessages(null)
//    }
//
//    private fun updateTimer() {
//        val currentTime = System.currentTimeMillis()
//        _elapsedTime.value = currentTime - startTime
//    }
//
//    private fun startLocationUpdates() {
//        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
//    }
//
//    private fun stopLocationUpdates() {
//        fusedLocationClient.removeLocationUpdates(locationCallback)
//    }
//
//    private fun updateLocation(location: android.location.Location) {
//        val newLocation = Location(
//            latitude = location.latitude,
//            longitude = location.longitude,
//            timestamp = System.currentTimeMillis()
//        )
//
//        if (locationList.isNotEmpty()) {
//            val lastLocation = locationList.last()
//            val previousLocation = android.location.Location("").apply {
//                latitude = lastLocation.latitude
//                longitude = lastLocation.longitude
//            }
//            val distanceToLast = previousLocation.distanceTo(location)
//            _totalDistance.value = (_totalDistance.value ?: 0f) + distanceToLast
//
//            val timeDifference = (newLocation.timestamp - lastLocation.timestamp) / 1000.0
//            if (timeDifference > 0) {
//                _currentSpeed.value = (distanceToLast / timeDifference) * 3.6
//            }
//        }
//        locationList.add(newLocation)
//    }
//
//    fun onAccelerometerChanged(x: Float, y: Float, z: Float) {
//        val accelerationMagnitude = sqrt((x * x + y * y + z * z).toDouble())
//        totalAcceleration += accelerationMagnitude
//        updateCalories()
//    }
//
//    private fun updateCalories() {
//        val sport = "Corsa" // Sostituire con il valore effettivo dallo Spinner
//        _calorieCount.value = calculateCalories(sport, totalAcceleration, _totalDistance.value ?: 0f)
//    }
//
//    private fun calculateCalories(sport: String, acceleration: Double, distance: Float): Double {
//        val calorieBurnRate = when (sport) {
//            "Corsa" -> 0.05
//            "Bicicletta" -> 0.03
//            else -> 0.0
//        }
//        return ((calorieBurnRate * acceleration) + (distance * 0.1)) / 8
//    }
//
//    private fun saveTrainingSession() {
//        val userName = sessionManager.userName
//        val sessionDate = getCurrentDate()
//        val durationInSeconds = (_elapsedTime.value ?: 0L) / 1000
//        val trainingType = "Corsa" // Sostituire con il valore effettivo dallo Spinner
//        val burntCalories = _calorieCount.value?.toInt() ?: 0
//        val distance = _totalDistance.value ?: 0f
//
//        if (userName != null) {
//            val sessionId = db.insertTrainingSession(userName, sessionDate, durationInSeconds.toInt(), distance, trainingType, burntCalories)
//            for (location in locationList) {
//                db.insertTrainingLocation(sessionId, location.latitude, location.longitude, location.timestamp)
//            }
//        }
//    }
//
//    private fun getCurrentDate(): String {
//        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
//        return formatter.format(Date())
//    }
//
//    private fun resetTrainingData() {
//        totalAcceleration = 0.0
//        _calorieCount.value = 0.0
//        _elapsedTime.value = 0L
//        _totalDistance.value = 0F
//        _currentSpeed.value = 0.0
//        locationList.clear()
//    }
//
//    fun onLocationPermissionGranted() {
//        startLocationUpdates()
//    }
//}
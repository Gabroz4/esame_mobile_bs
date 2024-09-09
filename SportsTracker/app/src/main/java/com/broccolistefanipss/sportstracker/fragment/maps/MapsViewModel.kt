package com.broccolistefanipss.sportstracker.fragment.maps

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.broccolistefanipss.sportstracker.global.DB
import com.broccolistefanipss.sportstracker.manager.SessionManager
import com.google.android.gms.maps.model.PolylineOptions
import kotlin.random.Random

class MapsViewModel(application: Application) : AndroidViewModel(application) {

    private val db: DB = DB(application.applicationContext)
    private val sessionManager: SessionManager = SessionManager(application.applicationContext)
    private val _userTrainings = MutableLiveData<List<PolylineOptions>>()
    val userTrainings: LiveData<List<PolylineOptions>> = _userTrainings

    fun loadAllUserTrainings() {
        val userName = sessionManager.userName
        val trainings = userName?.let { db.getAllTrainingsByUserId(it) }

        val polylineOptionsList = mutableListOf<PolylineOptions>()
        trainings?.forEach { training ->
            val locations = db.getLocationsByTrainingId(training.sessionId.toLong())
            if (locations.isNotEmpty()) {
                val color = getRandomColor()
                val polylineOptions = PolylineOptions()
                    .addAll(locations)
                    .width(8f)
                    .color(color)
                    .geodesic(true)
                polylineOptionsList.add(polylineOptions)
            }
        }

        _userTrainings.postValue(polylineOptionsList)
    }

    private fun getRandomColor(): Int {
        return Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
    }
}

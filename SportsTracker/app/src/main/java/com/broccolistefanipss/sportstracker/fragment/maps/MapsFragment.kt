package com.broccolistefanipss.sportstracker.fragment.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.broccolistefanipss.sportstracker.R
import com.broccolistefanipss.sportstracker.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.broccolistefanipss.sportstracker.activity.NewTrainingActivity
import com.broccolistefanipss.sportstracker.global.DB
import com.broccolistefanipss.sportstracker.manager.SessionManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.PolylineOptions
import kotlin.random.Random

class MapsFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap
    private lateinit var db: DB
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var sessionManager : SessionManager


    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                enableMyLocation()
                getLastKnownLocation()
            } else {
                Toast.makeText(requireContext(), "Il servizio di localizzazione richiede il permesso di accedere alla posizione", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        db = DB(requireContext())
        sessionManager = SessionManager(requireContext())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        _binding!!.addTrainingButton.setOnClickListener {
            val intent = Intent(context, NewTrainingActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun drawAllUserTrainings() {
        val userName = sessionManager.userName

        // Get all trainings for the user
        val trainings = userName?.let { db.getAllTrainingsByUserId(it) }

        val bounds = com.google.android.gms.maps.model.LatLngBounds.Builder()
        var hasValidTrainings = false

        trainings?.forEach { training ->
            val locations = db.getLocationsByTrainingId(training.sessionId.toLong())
            if (locations.isNotEmpty()) {
                hasValidTrainings = true
                val color = getRandomColor()
                val polylineOptions = PolylineOptions()
                    .addAll(locations)
                    .width(8f)
                    .color(color)
                    .geodesic(true)

                map.addPolyline(polylineOptions)

                // Add start and end markers
                map.addMarker(MarkerOptions().position(locations.first()).title("Start: ${training.sessionDate}"))
                map.addMarker(MarkerOptions().position(locations.last()).title("End: ${training.sessionDate}"))

                // Update bounds
                locations.forEach { bounds.include(it) }
            }
        }

        // Move camera to fit all trainings
        if (hasValidTrainings) {
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 100))
        } else {
            // Handle case where no valid trainings were found
            Toast.makeText(context, "No training routes found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getRandomColor(): Int {
        return Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
    }

    private fun checkLocationPermission() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) -> {
                enableMyLocation()
                getLastKnownLocation()
            }
            else -> {
                requestPermissionLauncher.launch(ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
        }
    }

    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val userLocation = LatLng(it.latitude, it.longitude)
                    map.addMarker(MarkerOptions().position(userLocation).title("La tua posizione"))
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12.0f))
                } ?: Toast.makeText(requireContext(), "Posizione non disponibile", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        checkLocationPermission()
        drawAllUserTrainings()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

//package com.broccolistefanipss.sports-tracker.fragment.maps
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import com.broccolistefanipss.sportstracker.R
//import com.broccolistefanipss.sportstracker.databinding.FragmentMapsBinding
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions
//import android.Manifest.permission.ACCESS_FINE_LOCATION
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.graphics.Color
//import android.location.Location
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.broccolistefanipss.sportstracker.activity.NewTrainingActivity
//import com.broccolistefanipss.sportstracker.global.DB
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.maps.model.PolylineOptions
//
//// TODO: ora configurato per disegnare solo un allenamento (che non gli viene mai passato) quindi giustamente non disegna nulla
//// TODO: modificare in modo che disegni tutti gli allenamenti di un utente -> vero parametro da passare
//
//class MapsFragment : Fragment(), OnMapReadyCallback {
//
//    private var _binding: FragmentMapsBinding? = null
//
//    private val binding get() = _binding!!
//    private lateinit var map: GoogleMap
//    private lateinit var db: DB
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//
//
//    private val requestPermissionLauncher =
//        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
//            if (isGranted) {
//                enableMyLocation()
//                getLastKnownLocation()
//            } else {
//                Toast.makeText(requireContext(), "Il servizio di localizzazione richiede il permesso di accedere alla posizione", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//        _binding = FragmentMapsBinding.inflate(inflater, container, false)
//        db = DB(requireContext())
//
//        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//
//        // Inizializza il fusedLocationClient
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
//
//        // Pulsante per lanciare NewTrainingActivity
//        _binding!!.addTrainingButton.setOnClickListener {
//            val intent = Intent(context, NewTrainingActivity::class.java)
//            startActivity(intent)
//        }
//
//        return binding.root
//    }
//
//    private fun drawTrainingPolyline(trainingId: Long) {
//        // Recupera le posizioni dal database
//
//        val locations = db.getLocationsByTrainingId(trainingId)
//
//        // Verifica che ci siano posizioni disponibili
//        if (locations.isNotEmpty()) {
//            // Crea un oggetto PolylineOptions per configurare la polyline
//            val polylineOptions = PolylineOptions()
//                .addAll(locations)
//                .width(8f)
//                .color(Color.BLUE)
//                .geodesic(true)
//
//            // Aggiungi la polyline sulla mappa
//            map.addPolyline(polylineOptions)
//
//            // Centra la mappa sulla prima posizione del percorso
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(locations[0], 15f))
//        } else {
//            Toast.makeText(context, "Nessuna posizione disponibile per questo allenamento", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    // Controlla il permesso e abilita la posizione
//    private fun checkLocationPermission() {
//        when (PackageManager.PERMISSION_GRANTED) {
//            ContextCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) -> {
//                enableMyLocation()
//                getLastKnownLocation()
//            }
//            else -> {
//                requestPermissionLauncher.launch(ACCESS_FINE_LOCATION)
//            }
//        }
//    }
//
//    private fun enableMyLocation() {
//        if (ActivityCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            map.isMyLocationEnabled = true
//        }
//    }
//
//    private fun getLastKnownLocation() {
//        if (ActivityCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
//                if (location != null) {
//                    val userLocation = LatLng(location.latitude, location.longitude)
//                    map.addMarker(MarkerOptions().position(userLocation).title("La tua posizione"))
//                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12.0f))
//                } else {
//                    Toast.makeText(requireContext(), "Posizione non disponibile", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//
//    override fun onMapReady(googleMap: GoogleMap) {
//        map = googleMap
//        checkLocationPermission()
//
//        val trainingId = arguments?.getLong("training_id") ?: return
//        drawTrainingPolyline(trainingId)
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}


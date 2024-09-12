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
import android.location.Location
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.broccolistefanipss.sportstracker.activity.NewTrainingActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLngBounds

class MapsFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var viewModel: MapsViewModel

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

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        viewModel = ViewModelProvider(this)[MapsViewModel::class.java]

        _binding!!.addTrainingButton.setOnClickListener {
            val intent = Intent(context, NewTrainingActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        checkLocationPermission()

        // osserviamo i dati del ViewModel
        viewModel.userTrainings.observe(viewLifecycleOwner) { polylineOptionsList ->
            val bounds = LatLngBounds.Builder()
            polylineOptionsList.forEach { polylineOptions ->
                map.addPolyline(polylineOptions)
                polylineOptions.points.forEach { bounds.include(it) }
            }

            if (polylineOptionsList.isNotEmpty()) {
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 100))
            }
        }
        viewModel.loadAllUserTrainings()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
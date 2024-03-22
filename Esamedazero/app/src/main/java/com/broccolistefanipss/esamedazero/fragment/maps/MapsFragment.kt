package com.broccolistefanipss.esamedazero.fragment.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.broccolistefanipss.esamedazero.R
import com.broccolistefanipss.esamedazero.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        // Initial location to focus on, for example, New York
        val location = LatLng(40.7128, -74.0060) // New York LatLng
        map.addMarker(MarkerOptions().position(location).title("Marker in New York"))
        map.moveCamera(CameraUpdateFactory.newLatLng(location))

        // Here, you can enable location tracking, record the user's route, etc.
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
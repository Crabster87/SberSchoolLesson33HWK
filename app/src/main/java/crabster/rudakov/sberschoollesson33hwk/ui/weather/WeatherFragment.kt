package crabster.rudakov.sberschoollesson33hwk.ui.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
import crabster.rudakov.sberschoollesson33hwk.data.model.Coordinate
import crabster.rudakov.sberschoollesson33hwk.databinding.FragmentWeatherBinding

class WeatherFragment : Fragment(){

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 42
    private lateinit var weatherFragmentSharedViewModel: WeatherFragmentSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)

        weatherFragmentSharedViewModel =
            ViewModelProvider(this)[WeatherFragmentSharedViewModel::class.java]

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        binding.startRideButton.setOnClickListener {
            getLastLocation()
        }
        return binding.root
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        getGeoCodingData(location)
                    }
                }
            } else {
                Toast
                    .makeText(requireActivity(), "Please turn on location", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getGeoCodingData(location: Location) {
        weatherFragmentSharedViewModel.setCoordinate(Coordinate(location.latitude, location.longitude))
        Log.d(
            "CURRENT COORDINATE IS ",
            "LAT ${weatherFragmentSharedViewModel.getCoordinate().value?.latitude} " +
                    "LONG ${weatherFragmentSharedViewModel.getCoordinate().value?.longitude}"
        )
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest.create()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()!!
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        @SuppressLint("SetTextI18n")
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            getGeoCodingData(mLastLocation)
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED)
            ) {
                getLastLocation()
            }
        }
    }

}
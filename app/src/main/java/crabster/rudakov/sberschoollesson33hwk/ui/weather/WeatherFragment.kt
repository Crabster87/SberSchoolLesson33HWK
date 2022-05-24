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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import crabster.rudakov.sberschoollesson33hwk.R
import crabster.rudakov.sberschoollesson33hwk.data.model.Coordinate
import crabster.rudakov.sberschoollesson33hwk.databinding.FragmentWeatherBinding
import crabster.rudakov.sberschoollesson33hwk.utils.Constants
import crabster.rudakov.sberschoollesson33hwk.utils.SnackBarReceiver
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 42

    private val weatherFragmentSharedViewModel by activityViewModels<WeatherFragmentSharedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        getWeatherInLocation()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        binding.getWeatherButton.setOnClickListener {
            getLastLocation()
        }
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
                SnackBarReceiver.show(_binding!!.root, getString(R.string.snackbar_msg_location))
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getGeoCodingData(location: Location) {
        weatherFragmentSharedViewModel.setCoordinate(
            Coordinate(
                location.latitude,
                location.longitude
            )
        )
        weatherFragmentSharedViewModel.requestLocalWeather()
        SnackBarReceiver.show(
            _binding!!.root,
            "LAT ${weatherFragmentSharedViewModel.getCoordinate().value?.latitude} " +
                    "LONG ${weatherFragmentSharedViewModel.getCoordinate().value?.longitude}"
        )
        getWeatherInLocation()
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

    @SuppressLint("SetTextI18n")
    private fun getWeatherInLocation() {
        weatherFragmentSharedViewModel.getLocalWeather().observe(viewLifecycleOwner) {
            binding.apply {
                textViewCurrentTime.text =
                    "Current time: ${convertCurrentData(it.currentParams.dt, it.timezone)}"
                textViewTimeZone.text = "Timezone: ${it.timezone}"

                textViewTemperature.text = "Temperature \n${it.currentParams.temp} C"
                textViewPressure.text = "Pressure \n${it.currentParams.pressure} hPa"
                textViewHumidity.text = "Humidity \n${it.currentParams.humidity} %"
                textViewUvi.text = "UV index \n${it.currentParams.uvi}"
                textViewClouds.text = "Cloudiness \n${it.currentParams.clouds} %"
                textViewWindSpeed.text = "Wind speed \n${it.currentParams.wind_speed} m/s"
                textViewMain.text = it.currentParams.currentConditions[0].main
                textViewDescription.text = it.currentParams.currentConditions[0].description
                textViewVisibility.text = "Visibility \n${it.currentParams.visibility} m"
            }
        }

        weatherFragmentSharedViewModel.getIcon().observe(viewLifecycleOwner) {
            Glide.with(this).load(it).into(binding.imageViewWeatherIcon)
        }

        weatherFragmentSharedViewModel.exception().observe(
            viewLifecycleOwner
        ) {
            SnackBarReceiver.show(_binding!!.root, it)
        }
    }

    @SuppressLint("NewApi")
    private fun convertCurrentData(millis: Long, timeZone: String): String {
        val formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)
        val instant = Instant.ofEpochMilli(millis * 1000)
        val date = LocalDateTime.ofInstant(instant, ZoneId.of(timeZone))
        return formatter.format(date)
    }

}
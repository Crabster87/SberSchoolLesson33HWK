package crabster.rudakov.sberschoollesson33hwk.ui.map

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import crabster.rudakov.sberschoollesson33hwk.R
import crabster.rudakov.sberschoollesson33hwk.databinding.FragmentMapBinding
import crabster.rudakov.sberschoollesson33hwk.ui.weather.WeatherFragmentSharedViewModel
import crabster.rudakov.sberschoollesson33hwk.utils.SnackBarReceiver
import java.util.*

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var weatherFragmentSharedViewModel: WeatherFragmentSharedViewModel
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        weatherFragmentSharedViewModel =
            ViewModelProvider(requireActivity())[WeatherFragmentSharedViewModel::class.java]
        getGeoCodingData()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(p0: GoogleMap) {
        weatherFragmentSharedViewModel.getCoordinate().observe(viewLifecycleOwner) {
            p0.apply {
                val coordinates = LatLng(it.latitude, it.longitude)
                addMarker(
                    MarkerOptions()
                        .position(coordinates)
                        .title(getString(R.string.map_marker))
                )
                moveCamera(CameraUpdateFactory.newLatLng(coordinates))
            }
        }
    }

    private fun getGeoCodingData() {
        weatherFragmentSharedViewModel.getCoordinate().observe(viewLifecycleOwner) {
            val geocoder = Geocoder(requireActivity(), Locale.getDefault())
            val list: List<Address> =
                geocoder.getFromLocation(it.latitude, it.longitude, 1)
            binding.apply {
                instanceLatitude.text = list[0].latitude.toString()
                instanceLongitude.text = list[0].longitude.toString()
                instanceCountry.text = list[0].countryName
                instanceLocality.text = list[0].locality
                instanceAddress.text = list[0].getAddressLine(0)
                instanceSubAdminArea.text = list[0].subAdminArea
            }

        }

        weatherFragmentSharedViewModel.exception().observe(
            viewLifecycleOwner
        ) {
            SnackBarReceiver.show(_binding!!.root, it)
        }
    }

}
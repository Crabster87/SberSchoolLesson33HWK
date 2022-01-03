package crabster.rudakov.sberschoollesson33hwk.ui.map

import android.os.Bundle
import android.util.Log
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
            ViewModelProvider(this)[WeatherFragmentSharedViewModel::class.java]
//        Log.d("MAP FRAGMENT REPORTS ",
//            "LAT ${weatherFragmentSharedViewModel.getCoordinate().value?.latitude} " + "LONG ${weatherFragmentSharedViewModel.getCoordinate().value?.longitude}")

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
//                Log.d("MAP FRAGMENT REPORTS ",
//                    "LAT ${it.latitude} " + "LONG ${it.longitude}")
//                clear()
                addMarker(
                    MarkerOptions()
                        .position(coordinates)
                        .title(getString(R.string.map_marker))
                )
                moveCamera(CameraUpdateFactory.newLatLng(coordinates))
            }
        }
    }

}
package crabster.rudakov.sberschoollesson33hwk.ui.map

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

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapViewModel: MapViewModel
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mapViewModel =
            ViewModelProvider(this)[MapViewModel::class.java]

        _binding = FragmentMapBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(p0: GoogleMap) {
        mapViewModel.coordinates().observe(viewLifecycleOwner) {
            p0.apply {
                val coordinates = LatLng(it.latitude, it.longitude)
                clear()
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
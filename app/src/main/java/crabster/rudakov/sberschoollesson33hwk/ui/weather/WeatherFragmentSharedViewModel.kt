package crabster.rudakov.sberschoollesson33hwk.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import crabster.rudakov.sberschoollesson33hwk.data.model.Coordinate

class WeatherFragmentSharedViewModel : ViewModel() {

    private val coordinate: MutableLiveData<Coordinate> = MutableLiveData<Coordinate>()

    fun getCoordinate(): LiveData<Coordinate> {
        return coordinate
    }

    fun setCoordinate(coordinate: Coordinate) {
        this.coordinate.value = coordinate
    }

}
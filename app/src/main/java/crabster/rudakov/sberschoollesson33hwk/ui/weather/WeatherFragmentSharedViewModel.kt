package crabster.rudakov.sberschoollesson33hwk.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import crabster.rudakov.sberschoollesson33hwk.data.model.Coordinate
import crabster.rudakov.sberschoollesson33hwk.data.model.LocalWeather
import crabster.rudakov.sberschoollesson33hwk.data.repository.WeatherRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class WeatherFragmentSharedViewModel : ViewModel() {

    private val coordinate = MutableLiveData<Coordinate>()
    private val localWeather = MutableLiveData<LocalWeather>()
    private var exception = MutableLiveData<String>()

    private var weatherRepository = WeatherRepository()
    private lateinit var disposable: Disposable

    fun getCoordinate(): LiveData<Coordinate> {
        return coordinate
    }

    fun setCoordinate(coordinate: Coordinate) {
        this.coordinate.value = coordinate
    }

    fun getLocalWeather(): LiveData<LocalWeather> {
        return localWeather
    }

    fun requestLocalWeather() {
        disposable = weatherRepository.getCurrentWeather(
            coordinate.value!!.latitude,
            coordinate.value!!.longitude
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                localWeather.value = it
            }, {
                exception.value = it.toString()
            })
    }

    fun exception(): LiveData<String> {
        return exception
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

}
package crabster.rudakov.sberschoollesson33hwk.ui.weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import crabster.rudakov.sberschoollesson33hwk.data.model.Coordinate
import crabster.rudakov.sberschoollesson33hwk.data.model.LocalWeather
import crabster.rudakov.sberschoollesson33hwk.data.repository.WeatherRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class WeatherFragmentSharedViewModel : ViewModel() {

    private val coordinate = MutableLiveData<Coordinate>()
    private val localWeather = MutableLiveData<LocalWeather>()
    private val iconUrl = MutableLiveData<String>()
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

    fun getIcon(): LiveData<String> {
        return iconUrl
    }

    fun requestLocalWeather() {
        disposable = Observable.interval(0,10, TimeUnit.SECONDS).timeInterval()
            .flatMap { weatherRepository.getCurrentWeather(
                coordinate.value!!.latitude,
                coordinate.value!!.longitude
            ) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                localWeather.value = it
                iconUrl.value =
                    "http://openweathermap.org/img/wn/${it.currentParams.currentConditions[0].icon}@2x.png"
                Log.d("LIVE DATA", "WEATHER OBJECT ${localWeather.value.toString()}")
                Log.d("LIVE DATA", "URL OBJECT ${iconUrl.value.toString()}")
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
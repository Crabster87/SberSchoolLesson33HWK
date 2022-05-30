package crabster.rudakov.sberschoollesson33hwk.data.repository

import crabster.rudakov.sberschoollesson33hwk.data.model.LocalWeather
import io.reactivex.Single

interface WeatherRepository {

    fun getCurrentWeather(latitude: Double, longitude: Double): Single<LocalWeather>

}
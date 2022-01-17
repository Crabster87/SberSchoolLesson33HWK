package crabster.rudakov.sberschoollesson33hwk.data.repository

import crabster.rudakov.sberschoollesson33hwk.utils.Constants
import crabster.rudakov.sberschoollesson33hwk.data.api.RetrofitClient
import crabster.rudakov.sberschoollesson33hwk.data.model.LocalWeather
import io.reactivex.Single

class WeatherRepository {

    fun getCurrentWeather(latitude: Double, longitude: Double): Single<LocalWeather> {
        return RetrofitClient.api.getWeather(
            latitude,
            longitude,
            Constants.EXCLUDED_PARAMS,
            Constants.API_KEY,
            Constants.UNIT_PARAMS
        )
    }

}
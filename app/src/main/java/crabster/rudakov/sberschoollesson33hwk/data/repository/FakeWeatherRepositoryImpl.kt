package crabster.rudakov.sberschoollesson33hwk.data.repository

import crabster.rudakov.sberschoollesson33hwk.data.api.RetrofitClient
import crabster.rudakov.sberschoollesson33hwk.data.model.LocalWeather
import crabster.rudakov.sberschoollesson33hwk.utils.Constants
import io.reactivex.Single

class FakeWeatherRepositoryImpl : WeatherRepository {

    override fun getCurrentWeather(latitude: Double, longitude: Double): Single<LocalWeather> =
        RetrofitClient.api.getWeather(
            latitude,
            longitude,
            Constants.EXCLUDED_PARAMS,
            Constants.API_KEY,
            Constants.UNIT_PARAMS
        )

}
package crabster.rudakov.sberschoollesson33hwk.data.api

import crabster.rudakov.sberschoollesson33hwk.data.model.LocalWeather
import crabster.rudakov.sberschoollesson33hwk.utils.Constants
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitServices {

    @GET(Constants.WEATHER_URL)
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String,
        @Query("appid") appid: String,
        @Query("units") units: String
    ): Observable<LocalWeather>

}
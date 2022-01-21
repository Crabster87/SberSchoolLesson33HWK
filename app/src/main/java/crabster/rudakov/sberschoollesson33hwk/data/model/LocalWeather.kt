package crabster.rudakov.sberschoollesson33hwk.data.model

import com.google.gson.annotations.SerializedName

data class LocalWeather(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @SerializedName("current")
    val currentParams: CurrentParams
)

data class CurrentParams(
    val dt: Long,
    val temp: Double,
    val pressure: Double,
    val humidity: Double,
    val uvi: Double,
    val clouds: Double,
    val wind_speed: Double,
    val visibility: Double,
    @SerializedName("weather")
    val currentConditions: List<CurrentConditions>
)

data class CurrentConditions(
    val main: String,
    val description: String,
    val icon: String
)
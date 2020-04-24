package hu.ait.weatherinformation.network

import hu.ait.weatherinformation.data.CityWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query



interface CityWeatherAPI{
    @GET("data/2.5/weather")
    fun getWeather(@Query("q") city: String,
                   @Query("units") units: String,
                   @Query("appid") appid: String): Call<CityWeather>
}
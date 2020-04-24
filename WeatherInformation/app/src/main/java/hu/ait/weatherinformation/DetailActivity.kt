package hu.ait.weatherinformation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import hu.ait.weatherinformation.data.CityWeather
import hu.ait.weatherinformation.network.CityWeatherAPI
import kotlinx.android.synthetic.main.activity_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class DetailActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        var cityName = intent.getStringExtra("KEY_DATA")
        tvWeather.text = cityName

        setTitle(cityName + getString(R.string.Weather_title))

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        val weatherAPI = retrofit.create(CityWeatherAPI::class.java)

        val weatherCall = weatherAPI.getWeather(
            cityName,
            "metric",
            "46c919c670a8e4826f1e2f215871223c"
        )

        weatherCall.enqueue(object : Callback<CityWeather> {
            override fun onFailure(call: Call<CityWeather>, t: Throwable) {
                tvWeather.text = t.message
            }

            override fun onResponse(call: Call<CityWeather>, response: Response<CityWeather>) {
                var weatherResult = response.body()
                tvWeather.text = weatherResult?.weather?.get(0)?.main.toString()
                if (tvWeather.text == "null") {
                    tvWeather.text = getString(R.string.city_not_found_error)
                } else {
                    tvDescription.text = weatherResult?.weather?.get(0)?.description.toString()
                    tvTemp.text = weatherResult?.main?.temp.toString()+getString(R.string.celsius)
                    tvPressure.text = weatherResult?.main?.pressure.toString()
                    tvHumidity.text = weatherResult?.main?.humidity.toString()
                    tvMinTemp.text = weatherResult?.main?.temp_min.toString()+getString(R.string.celsius)
                    tvMaxTemp.text = weatherResult?.main?.temp_max.toString()+getString(R.string.celsius)


                    Glide.with(this@DetailActivity).load(
                        ("https://openweathermap.org/img/w/" + weatherResult?.weather?.get(0)?.icon + ".png")
                    ).into(ivWeahter)

                    mapMoveTo(
                        weatherResult?.coord?.lat!!.toDouble(),
                        weatherResult?.coord?.lon!!.toDouble()
                    )
                }
            }
        })


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.fgmap) as SupportMapFragment?
        mapFragment!!.getMapAsync(this@DetailActivity)
    }


    override fun onMapReady(map: GoogleMap) {
        mMap = map
        val myCurrentLatitude = 0.0
        val myCurrentLongitude = 0.0
        val myCurrentLocation = CameraPosition.builder()
            .target(LatLng(myCurrentLatitude, myCurrentLongitude))
            .zoom(12f)
            .bearing(0f)
            .tilt(2f)
            .build()
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(myCurrentLocation))
        mMap.getUiSettings().setScrollGesturesEnabled(true)
    }

    private fun mapMoveTo(lat: Double, lng: Double) {
        val latLng = LatLng(lat, lng)
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        }

    }
}





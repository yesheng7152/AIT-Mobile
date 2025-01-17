package hu.ait.restaurantsharing

import android.graphics.Point
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var restaurantLatLng: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val cuisineArray = resources.getStringArray(R.array.cuisine)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val documentId = intent.getStringExtra("KEY")
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("posts").document(documentId)
        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null) {
                    tvRestaurantNameD.text = document.getString("restaurantName")
                    tvDishesD.text = document.getString("dishes")
                    tvDishesD.setMovementMethod(ScrollingMovementMethod())
                    tvPriceRangeD.text = document.getString("priceRange")
                    tvCuisineType.text = cuisineArray[document.get("cuisine").toString().toInt()]
                    rbIndicatorD.rating = document.get("rating").toString().toFloat()
                    if(document.getString("img1")!=""){
                        ivImg1D.visibility = View.VISIBLE
                        Glide.with(this).load(document.get("img1")).into(ivImg1D)
                    }else{
                        ivImg1D.visibility = View.GONE
                    }
                    restaurantLatLng = LatLng(document.get("lat").toString().toDouble()
                        ,document.get("lng").toString().toDouble())
                    var userMarker = MarkerOptions()
                    userMarker.position(restaurantLatLng)
                    userMarker.title(tvRestaurantNameD.text.toString())
                    mMap.addMarker(userMarker)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userMarker.position,16f))
                }
            }
        }
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

    }
}

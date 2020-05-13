package hu.ait.restaurantsharing

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val cuisineArray = resources.getStringArray(R.array.cuisine)


        val documentId = intent.getStringExtra("KEY")
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("posts").document(documentId)
        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null) {
                    tvRestaurantNameD.text = document.getString("restaurantName")
                    tvDishesD.text = document.getString("dishes")
                    tvPriceRangeD.text = document.getString("priceRange")
                    tvCuisineType.text = cuisineArray[document.get("cuisine").toString().toInt()]
                    rbIndicatorD.rating = document.get("rating").toString().toFloat()

                }
            }
        }
    }
}

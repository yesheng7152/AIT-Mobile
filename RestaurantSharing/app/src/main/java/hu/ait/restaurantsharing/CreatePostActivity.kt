package hu.ait.restaurantsharing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hu.ait.restaurantsharing.data.Post
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.post_row.*

class CreatePostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        spinnerSetUp()

        btnPost.setOnClickListener {
            sendClick(it)
            finish()
        }
    }
    fun spinnerSetUp(){
        var categoryAdapter = ArrayAdapter.createFromResource(this,
            R.array.cuisine,
            android.R.layout.simple_spinner_item
        )
        categoryAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item)
        spCuisine.adapter = categoryAdapter
        spCuisine.setSelection(0)
    }

    fun sendClick(v: View){
        uploadPost()
    }

    fun uploadPost() {
        var radioButton = findViewById<RadioButton>(rgButton.checkedRadioButtonId)
        val post = Post(
            FirebaseAuth.getInstance().currentUser!!.uid,
            FirebaseAuth.getInstance().currentUser!!.email!!,
            etRestaurant.text.toString(),
            spCuisine.selectedItemPosition,
            etDishes.text.toString(),
            rbRating.rating,
            radioButton.text.toString()
        )
        var postCollection = FirebaseFirestore.getInstance().collection("posts")
        postCollection.add(post).addOnSuccessListener {
            Toast.makeText(this@CreatePostActivity,"Post Uploade",
            Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(this@CreatePostActivity,
            "Error ${it.message}",Toast.LENGTH_LONG).show()
        }
    }
}

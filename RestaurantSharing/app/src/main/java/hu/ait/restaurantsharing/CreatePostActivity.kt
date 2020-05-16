package hu.ait.restaurantsharing

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils.isEmpty
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import hu.ait.restaurantsharing.data.Post
import kotlinx.android.synthetic.main.activity_create_post.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URLEncoder
import java.util.*
import kotlin.collections.ArrayList


class CreatePostActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
        private const val CAMERA_REQUEST_CODE_1 = 10021
        private const val GALLERY_REQUEST_CODE_1 = 10031
    }

    private lateinit var mMap: GoogleMap

    var uploadBitmap: Bitmap? = null
    private val imageList = ArrayList<Bitmap?>()
    var ResLatLng: LatLng= LatLng(0.0, 0.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        spinnerSetUp()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        btn_find.setOnClickListener{
            var location = et_location.text.toString()
            var userMarker = MarkerOptions()
            if (location != null && location != ""){
                val geocoder = Geocoder(this)
                val addressList = geocoder.getFromLocationName(location, 1)
                if(addressList!= null) {
                    for (address in addressList) {
                        ResLatLng = LatLng(address.latitude, address.longitude)
                        userMarker.position(ResLatLng)
                        userMarker.title(etRestaurant.text.toString())
                        mMap.addMarker(userMarker)
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(ResLatLng))
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(14f))

                    }
                }else{
                    Toast.makeText(this, "Location does not exist", Toast.LENGTH_LONG).show()
                }
            }else{
                et_location.setError("Please enter Restaurant Location")
            }
        }


        btnPost.setOnClickListener {
            sendClick(it)
            finish()
        }

        btnCancel.setOnClickListener{
            finish()
        }

        ivImg1.setOnClickListener {
            attachClick(it, CAMERA_REQUEST_CODE_1, GALLERY_REQUEST_CODE_1)
        }

        requestNeededPermiss()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    //Spinner
    fun spinnerSetUp() {
        var categoryAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.cuisine,
            android.R.layout.simple_spinner_item
        )
        categoryAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        spCuisine.adapter = categoryAdapter
        spCuisine.setSelection(0)
    }

    //Select image and uploading
    fun attachClick(v: View, code1: Int, code2: Int) {
        try {
            val pm = packageManager
            val hasPerm = pm.checkPermission(Manifest.permission.CAMERA, packageName)
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                val options =
                    arrayOf<CharSequence>("Take Photo", "Choose From Gallery", "Cancel")
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Select Option")
                builder.setItems(options,
                    DialogInterface.OnClickListener { dialog, item ->
                        if (options[item] == "Take Photo") {
                            dialog.dismiss()
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(intent, code1)
                        } else if (options[item] == "Choose From Gallery") {
                            dialog.dismiss()
                            val pickPhoto = Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                            startActivityForResult(pickPhoto, code2)
                        } else if (options[item] == "Cancel") {
                            dialog.dismiss()
                        }
                    })
                builder.show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun requestNeededPermiss() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.CAMERA
                )
            ) {
                Toast.makeText(this, "I need it for camera", Toast.LENGTH_LONG).show()
            }
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.CAMERA),
                PERMISSION_REQUEST_CODE
            )
        } else {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE_1 -> {
                    cameraBitmapGetData(data,ivImg1)
                }
                GALLERY_REQUEST_CODE_1 -> {
                    galleryBitmapGetData(data,ivImg1)
                }
            }
        }

    }

    private fun cameraBitmapGetData(data: Intent?,ivImg: ImageView) {
        uploadBitmap = data!!.extras!!.get("data") as Bitmap
        imageList.add(uploadBitmap)
        ivImg.setImageBitmap(uploadBitmap)
    }
    private fun galleryBitmapGetData(data: Intent?,ivImg: ImageView) {
        uploadBitmap = MediaStore.Images.Media.getBitmap(
                this.getContentResolver(), data!!.getData())
        imageList.add(uploadBitmap)
        ivImg.setImageBitmap(uploadBitmap)
    }


    fun sendClick(v: View) {
        if(uploadBitmap != null) {
            uploadPostWithImages(imageList)
        }else{
            uploadPost()
        }
    }

    fun uploadPost(img1: String=""){
        var radioButton = findViewById<RadioButton>(rgButton.checkedRadioButtonId)
        val post = Post(
            FirebaseAuth.getInstance().currentUser!!.uid,
            FirebaseAuth.getInstance().currentUser!!.email!!,
            etRestaurant.text.toString(),
            spCuisine.selectedItemPosition,
            etDishes.text.toString(),
            rbRating.rating,
            radioButton.text.toString(),
            img1,
            ResLatLng.latitude,
            ResLatLng.longitude
        )
        var postCollection = FirebaseFirestore.getInstance().collection("posts")
        postCollection.add(post)

    }

    private fun compressToBytes(uploadBitmap: Bitmap): ByteArray {
        val baos = ByteArrayOutputStream()
        uploadBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageInBytes = baos.toByteArray()
        return imageInBytes
    }

    private fun setPathForBytes(): StorageReference {
        val storageRef = FirebaseStorage.getInstance().getReference()
        val newImage = URLEncoder.encode(UUID.randomUUID().toString(), "UTF-8") + ".jpg"
        val newImagesRef = storageRef.child("images/$newImage")
        return newImagesRef
    }


    @Throws(Exception::class)
    private fun uploadPostWithImages(imageList: ArrayList<Bitmap?>) {
        for (image in imageList) {
            var newUploadBitmap = image as Bitmap
            val imageInBytes = compressToBytes(newUploadBitmap)
            val newImagesRef = setPathForBytes()

            newImagesRef.putBytes(imageInBytes)
                .addOnFailureListener {
                    Toast.makeText(this@CreatePostActivity, it.message, Toast.LENGTH_SHORT).show()
                }.addOnSuccessListener {
                    newImagesRef.downloadUrl.addOnCompleteListener(object : OnCompleteListener<Uri> {
                        override fun onComplete(task: Task<Uri>) {
                            var urlD = task.result.toString()
                            uploadPost(urlD)
                        }
                    })
                }
        }
    }
}








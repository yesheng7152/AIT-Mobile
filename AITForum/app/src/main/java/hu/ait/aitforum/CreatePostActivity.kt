package hu.ait.aitforum

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApiNotAvailableException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import hu.ait.aitforum.data.Post
import kotlinx.android.synthetic.main.activity_create_post.*
import java.io.ByteArrayOutputStream
import java.net.URLEncoder
import java.util.*

class CreatePostActivity : AppCompatActivity() {

    companion object{
        private const val PERMISSION_REQUEST_CODE = 1001
        private const val CAMERA_REQUEST_CODE = 1002
    }

    var uploadBitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        btnAttach.setOnClickListener{
            startActivityForResult(
                Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_CODE
            )
        }

        requestNeededPermission()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            uploadBitmap = data!!.extras!!.get("data") as Bitmap

            ivAttach.setImageBitmap(uploadBitmap)
            ivAttach.visibility = View.VISIBLE
        }
    }


    private fun requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CAMERA)) {
                Toast.makeText(this,
                    "I need it for camera", Toast.LENGTH_SHORT).show()
            }

            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.CAMERA),
                PERMISSION_REQUEST_CODE)
        } else {
            // we already have permission
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "CAMERA perm granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "CAMERA perm NOT granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun sendClick(v: View){
        if(uploadBitmap != null){
            try {
                uploadPostWithImage()
            }catch (e: java.lang.Exception){
                e.printStackTrace()
            }
        }else{
            upLoadPost()
        }
    }

    fun upLoadPost(imageUrl: String = ""){
        val post = Post(
            FirebaseAuth.getInstance().currentUser!!.uid,
            FirebaseAuth.getInstance().currentUser!!.email!!,
            etTitle.text.toString(),
            etBody.text.toString(),
            imageUrl
        )

        var postCollection = FirebaseFirestore.getInstance().collection(
            "post")
        postCollection.add(post).addOnSuccessListener {
            Toast.makeText(this@CreatePostActivity, "Post Saved", Toast.LENGTH_LONG).show()
            finish()  //close the current activity and go back to the previous
        }.addOnFailureListener{
            Toast.makeText(this@CreatePostActivity, "ERROR ${it.message}", Toast.LENGTH_LONG).show()
        }
    }

    @Throws(Exception::class)
    private fun uploadPostWithImage() {

        val baos = ByteArrayOutputStream()
        uploadBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageInBytes = baos.toByteArray()

        val storageRef = FirebaseStorage.getInstance().getReference()
        val newImage = URLEncoder.encode(UUID.randomUUID().toString(), "UTF-8") + ".jpg"
        val newImagesRef = storageRef.child("images/$newImage")

        newImagesRef.putBytes(imageInBytes)
            .addOnFailureListener {
                Toast.makeText(this@CreatePostActivity, it.message, Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener {
                newImagesRef.downloadUrl.addOnCompleteListener(object: OnCompleteListener<Uri> {
                    override fun onComplete(task: Task<Uri>) {
                        upLoadPost(task.result.toString())
                    }
                })
            }
    }
}

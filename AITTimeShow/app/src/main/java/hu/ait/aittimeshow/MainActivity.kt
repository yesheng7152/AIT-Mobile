package hu.ait.aittimeshow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnTime.setOnClickListener{
            //Log.d("KEY_TAG", "time button was clicked")

            var time =
                getString(R.string.text_date,etName.text.toString(),Date(System.currentTimeMillis()).toString())
                //"${etName.text.toString()}, time is: ${Date(System.currentTimeMillis()).toString()}"

            Toast.makeText(this,
                time,
                Toast.LENGTH_LONG).show()

            tvTime.text = time

            Snackbar.make(layoutMain, time, Snackbar.LENGTH_LONG).show()
        }

    }
}

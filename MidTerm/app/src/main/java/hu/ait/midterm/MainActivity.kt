package hu.ait.midterm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGotoRecording.setOnClickListener {
            if(etPin.text.toString() == "5738"){ //The hard coded pin
                startActivity(Intent(this, RecordingActivity::class.java))
            }else{
                etPin.setError(getString(R.string.pin_error_message))
            }
        }
    }
}

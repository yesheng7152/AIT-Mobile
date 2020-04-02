package hu.ait.mutiactivitydemo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val KEY_DATA = "KEY_DATA"
        const val REQ_ANSWER = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStart.setOnClickListener{
            var intentDetails = Intent()

            intentDetails.setClass(this, DetailActivity::class.java)

            //KEY_DATA is like a id for the etData string
            intentDetails.putExtra(KEY_DATA, etData.text.toString())

            MyDataManager.demo = etData.text.toString()

            startActivityForResult(intentDetails, REQ_ANSWER)
        }
    }

    //RequestCode = REQ_ANSWER, resultCode = REQ_cancle or accept
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_ANSWER) {
            if (resultCode == Activity.RESULT_OK) {
                var resp = data?.getStringExtra("KEY_RES")
                Toast.makeText(this, resp, Toast.LENGTH_LONG).show()
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "CANCELLED", Toast.LENGTH_LONG).show()
            }
        }
    }
}

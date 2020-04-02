package hu.ait.mutiactivitydemo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        //get the info from the reason(intent) that is started
        var data = intent.getStringExtra(MainActivity.KEY_DATA)
        tvData.text = data

        btnAccept.setOnClickListener{
            var intentResult = Intent()
            intentResult.putExtra("KEY_RES", "accept")
            setResult(Activity.RESULT_OK, intentResult)
            finish()
        }

        btnDecline.setOnClickListener{
            var intentResult = Intent()
            intentResult.putExtra("KEY_RES", "decline")
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
    override fun onBackPressed(){
        Toast.makeText(this, "You can not exit", Toast.LENGTH_LONG).show()
    }
}

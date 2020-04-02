package hu.ait.animationdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        btnAnimation.setOnClickListener {
//            var pushAnim = AnimationUtils.loadAnimation(this, R.anim.push_anim)
//
//            btnAnimation.startAnimation(pushAnim)
//        }
        var sendAnim = AnimationUtils.loadAnimation(this, R.anim.send_anim)

        btnAnimation.setOnClickListener {

            tvData.startAnimation(sendAnim)
        }
    }
}

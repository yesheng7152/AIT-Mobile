package hu.ait.multiactivitypractice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.TextUtils
import kotlinx.android.synthetic.main.activity_game.*
import java.lang.Exception
import java.util.*

class GameActivity : AppCompatActivity() {

    var generatedNum = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        btnGuess.setOnClickListener {
            try {


                if (!TextUtils.isEmpty(etNumber.text.toString())) {
                    var myNum = etNumber.text.toString().toInt()

                    if (myNum == generatedNum) {
                        tvResult.text = "Congratulations"
                        startActivity(
                            Intent(this,ResultActivity ::class.java ))
                        finish()
                    } else if (myNum < generatedNum) {
                        tvResult.text = "The number is higher"
                    } else if (myNum > generatedNum) {
                        tvResult.text = "The number is lower"
                    }
                } else {
                    etNumber.error = "This field can not be empty"
                }
            } catch (e: Exception) {
                etNumber.error = e.message
            }
        }
        //save the random number even after the rotation
        if (savedInstanceState != null && savedInstanceState.containsKey("KEY_RAND")) {
            generatedNum = savedInstanceState.getInt("KEY_RAND")
        } else {
            generateNumber()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("KEY_RAND", generatedNum)

        super.onSaveInstanceState(outState)
    }


    fun generateNumber() {
        var rand = Random(System.currentTimeMillis())
        generatedNum = rand.nextInt(3) //generates number between 0..99
    }
}


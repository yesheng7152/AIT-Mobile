package hu.ait.aitcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnPlus.setOnClickListener{
            try {
                var sum = "Result: ${(firstNum.text.toString()).toDouble() +
                        (secNum.text.toString()).toDouble()}"
                tvResult.text = sum
            }catch (e: Exception){
                firstNum.setError("This field can not be empty!")
            }
        }

        btnMinus.setOnClickListener{
            try {
                var difference = "Result: ${(firstNum.text.toString()).toDouble() -
                        (secNum.text.toString().toDouble())}"
                tvResult.text = difference
            }catch(e:Exception){
                secNum.setError("This field can not be empty!")
            }
        }
    }
}

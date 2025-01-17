package hu.ait.roomgradesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.ait.roomgradesdemo.data.AppDatabase
import hu.ait.roomgradesdemo.data.Grade
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSave.setOnClickListener {
            saveGrade(Grade(null,
            etName.text.toString(),
            etGrade.text.toString()))
        }

        btnQuery.setOnClickListener {
            queryGrades()
        }

        btnDeleteAll.setOnClickListener {
            Thread{
                AppDatabase.getInstance(this@MainActivity).gradeDao().deleteAllGrade()
            }.start()
        }
    }

    fun saveGrade(grade: Grade){
        Thread{
            AppDatabase.getInstance(this). gradeDao().insertGrades(grade)
        }.start()
    }

    fun queryGrades(){
        Thread{
            val grades = AppDatabase.getInstance(this).gradeDao().getAllGrades()

            runOnUiThread{
                tvResult.text = ""
                grades.forEach {
                    tvResult.append("Name: ${it.studentName}, grade: ${it.grade}\n")
                }
            }
        }.start()
    }
}

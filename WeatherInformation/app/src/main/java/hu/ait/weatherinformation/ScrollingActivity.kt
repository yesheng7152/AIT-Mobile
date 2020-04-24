package hu.ait.weatherinformation

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import hu.ait.weatherinformation.adapter.CityAdapter
import hu.ait.weatherinformation.data.City
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlinx.android.synthetic.main.city_row.*

class ScrollingActivity : AppCompatActivity(){

    lateinit var cityAdapter: CityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            showAddCityDialog()
        }

        initRecyclerView()
    }

    private fun initRecyclerView(){
        cityAdapter= CityAdapter(this)
        recyclerList.adapter=cityAdapter
    }


    fun showAddCityDialog() {
        var builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.Dialog_title))

        val etCityName = EditText(this)
        builder.setView(etCityName)

        builder.setPositiveButton(getString(R.string.OK)) { dialog, which ->
            if(etCityName.text.toString() == ""){
            }else {
                cityAdapter.addCity(
                    City(
                        etCityName.text.toString()
                    )
                )
            }
        }
        
        builder.setNegativeButton(getString(R.string.Cancel)){
            dialog, which ->  
        }
        

        builder.show()
    }


}

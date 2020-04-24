package hu.ait.weatherinformation.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import hu.ait.weatherinformation.DetailActivity
import hu.ait.weatherinformation.R
import hu.ait.weatherinformation.ScrollingActivity
import hu.ait.weatherinformation.data.City
import kotlinx.android.synthetic.main.city_row.view.*



class CityAdapter : RecyclerView.Adapter<CityAdapter.ViewHolder>{

    var cityNames = mutableListOf<City>()
    val context: Context

    constructor(context: Context) {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.city_row, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cityNames.size
    }

    public fun addCity(city: City){
        cityNames.add(city)
        notifyItemChanged(cityNames.lastIndex)
    }

    private fun deleteCity(position: Int){
        cityNames.removeAt(position)
        notifyItemRemoved(position)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTodo = cityNames[position]

        holder.tvCityName.text = currentTodo.cityName
        holder.tvCityName.setOnClickListener{
            var intentDetails = Intent(context, DetailActivity::class.java)
            intentDetails.putExtra("KEY_DATA", holder.tvCityName.text.toString())
            context.startActivity(intentDetails)
        }
        holder.btnClear.setOnClickListener {
            deleteCity(holder.adapterPosition)
        }
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val tvCityName = itemView.tvCityName
        val btnClear = itemView.btnClear
        val tvDetail = itemView.tvDetail
    }
}
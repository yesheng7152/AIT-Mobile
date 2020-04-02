package hu.ait.midterm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.ait.midterm.MainActivity
import hu.ait.midterm.R
import hu.ait.midterm.RecordingActivity
import hu.ait.midterm.data.Inputs
import kotlinx.android.synthetic.main.input_row.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder> {
    var inputItems = mutableListOf<Inputs>()
    val context: Context


    constructor(context: Context) {
        this.context = context
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.input_row, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return inputItems.size
    }

    public fun addInput(input: Inputs){
        inputItems.add(input)
        notifyItemInserted(inputItems.lastIndex)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentInput = inputItems[position]
        holder.tvType.text = currentInput.inputType
        holder.tvAmount.text = currentInput.inputAmount

        if (inputItems[position].income){
            holder.imvIcon.setImageResource(R.drawable.income)
        }else{
            holder.imvIcon.setImageResource(R.drawable.expense)
        }

        holder.btnDelete.setOnClickListener {
            (context as RecordingActivity).deleteInput(holder.adapterPosition)

        }
    }




    public fun deleteAll(){
        inputItems.removeAll(inputItems)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imvIcon = itemView.imvIcon
        val tvType = itemView.tvType
        val tvAmount = itemView.tvAmount
        val btnDelete = itemView.btnDelete
    }
}
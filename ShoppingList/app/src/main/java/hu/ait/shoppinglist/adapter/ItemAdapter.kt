package hu.ait.shoppinglist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.ait.shoppinglist.MainActivity
import hu.ait.shoppinglist.R
import hu.ait.shoppinglist.data.AppDatabase
import hu.ait.shoppinglist.data.Item
import kotlinx.android.synthetic.main.shop_row.view.*

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ViewHolder>{

    var listItem = mutableListOf<Item>()
    var context : Context

    constructor(context: Context, items: List<Item>){
        this.context = context
        listItem.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(
            R.layout.shop_row, parent,false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = listItem[position]

        if(listItem[holder.adapterPosition].itemCategory == 0){
            holder.ivIcon.setImageResource(R.drawable.food)
        }else if(listItem[holder.adapterPosition].itemCategory == 1){
            holder.ivIcon.setImageResource(R.drawable.eletronic)
        }else{
            holder.ivIcon.setImageResource(R.drawable.book)
        }
        holder.tvName.text = currentItem.itemName
        holder.cbDone.isChecked = currentItem.done
        holder.cbDone.setOnClickListener{
            listItem[holder.adapterPosition].done = holder.cbDone.isChecked
            Thread{
                AppDatabase.getInstance(context).itemDao()
                    .updateItem(listItem[holder.adapterPosition])
            }.start()
        }
        holder.btnDelete.setOnClickListener {
            deleteItem(holder.adapterPosition)
        }
        holder.btnEdit.setOnClickListener {
            (context as MainActivity).showEditShopDialog(
                listItem[holder.adapterPosition], holder.adapterPosition
            )
        }
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    public fun addItem(item: Item){
        listItem.add(item)
        notifyItemChanged(listItem.lastIndex)
    }

    public fun updateItem(item: Item, editIndex: Int){
        listItem.set(editIndex, item)
        notifyItemChanged(editIndex)
    }

    private fun deleteItem(position: Int){
        Thread{
            AppDatabase.getInstance(context).itemDao().deleteItem(
                listItem.get(position))
            (context as MainActivity).runOnUiThread {
                listItem.removeAt(position)
                notifyItemRemoved(position)
            }
        }.start()
    }


    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val ivIcon = itemView.ivIcon
        val tvName = itemView.tvName
        val cbDone = itemView.cbDone
        val btnDelete = itemView.btnDelete
        val btnEdit = itemView.btnEdit
    }
}
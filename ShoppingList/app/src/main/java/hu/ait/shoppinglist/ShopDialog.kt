package hu.ait.shoppinglist

import android.content.Context
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import hu.ait.shoppinglist.data.Item

class ShopDialog : DialogFragment(){

    interface ItemHandler{
        fun itemCreated(item: Item)
        fun itemUpdated(item: Item)
    }

    lateinit var itemHandler: ItemHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ItemHandler){
            itemHandler = context
        } else {
            throw RuntimeException(
                "The Activity is not implementing the TodoHandler interface.")
        }
    }

    lateinit var etItemName: EditText
    lateinit var cbItemDone: CheckBox
    lateinit var spItemCategory: Spinner

}
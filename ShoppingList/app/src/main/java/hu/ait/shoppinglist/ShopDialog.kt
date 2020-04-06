package hu.ait.shoppinglist

import android.app.AlertDialog
import android.app.Dialog
import android.content.ClipDescription
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import hu.ait.shoppinglist.data.Item
import kotlinx.android.synthetic.main.shop_dialog.view.*

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
                getString(R.string.itemhandler_runtime_exception))
        }
    }

    lateinit var etItemName: EditText
    lateinit var cbItemDone: CheckBox
    lateinit var spItemCategory: Spinner
    lateinit var etPrice: EditText
    lateinit var etDescription: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle(getString(R.string.create_a_new_item))
        val dialogView = requireActivity().layoutInflater.inflate(
            R.layout.shop_dialog, null
        )
        etItemName = dialogView.etName
        cbItemDone = dialogView.cbAlreadyDone
        spItemCategory = dialogView.spCategory
        etPrice = dialogView.etPrice
        etDescription = dialogView.etDescription

        var categoryAdapter = ArrayAdapter.createFromResource(
            context!!,
            R.array.categories,
            android.R.layout.simple_spinner_item
        )
        categoryAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item)
        spItemCategory.adapter = categoryAdapter
        spItemCategory.setSelection(0)
        dialogBuilder.setView(dialogView)

        val arguments = this.arguments

        if(arguments != null && arguments.containsKey(MainActivity.KEY_EDIT)){
            val listItem = arguments.getSerializable(MainActivity.KEY_EDIT) as Item
            etItemName.setText(listItem.itemName)
            cbItemDone.isChecked = listItem.done
            spItemCategory.setSelection(listItem.itemCategory)
            etPrice.setText(listItem.itemPrice)
            etDescription.setText(listItem.itemDescription)

            dialogBuilder.setTitle(getString(R.string.edit_item))
        }

        dialogBuilder.setPositiveButton(getString(R.string.add_item)){
            dialog, which ->
        }

        dialogBuilder.setNegativeButton(getString(R.string.cancel_item)){
            dialog, which ->
        }

        return dialogBuilder.create()

    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if(etItemName.text.isNotEmpty()){
                val arguments = this.arguments
                if(arguments != null && arguments.containsKey(MainActivity.KEY_EDIT)){
                    handleItemEdit()
                }else{
                    handleItemCreate()
                }
                dialog!!.dismiss()
            }else{
                etItemName.error = getString(R.string.item_name_error)
            }
        }
    }

    private fun handleItemCreate(){
        itemHandler.itemCreated(
            Item(
                null,
                spItemCategory.selectedItemPosition,
                etItemName.text.toString(),
                false,
                etDescription.text.toString(),
                etPrice.text.toString()
            )
        )
    }

    private fun handleItemEdit(){
        val itemToEdit = arguments?.getSerializable(
            MainActivity.KEY_EDIT) as Item
        itemToEdit.itemCategory = spItemCategory.selectedItemPosition
        itemToEdit.itemName = etItemName.text.toString()
        itemToEdit.done = cbItemDone.isChecked
        itemToEdit.itemDescription = etDescription.text.toString()
        itemToEdit.itemPrice = etPrice.text.toString()

        itemHandler.itemUpdated(itemToEdit)
    }

}
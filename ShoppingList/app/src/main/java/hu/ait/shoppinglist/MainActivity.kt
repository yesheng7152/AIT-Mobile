package hu.ait.shoppinglist

import android.app.ProgressDialog.show
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import hu.ait.shoppinglist.adapter.ItemAdapter
import hu.ait.shoppinglist.data.AppDatabase
import hu.ait.shoppinglist.data.Item
import kotlinx.android.synthetic.main.activity_main.*
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetSequence
import java.sql.Date

class MainActivity : AppCompatActivity(),ShopDialog.ItemHandler {

    lateinit var itemAdapter: ItemAdapter

    companion object {
        const val KEY_EDIT = "KEY_EDIT"
        const val PREF_NAME = "PREFTODO"
        const val KEY_STARTED = "KEY_STARTED"
        const val KEY_LAST_USED = "LAST_USED"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        initRecyclerView()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        if (!wasStartedBefore()) {
            Handler().post {
                MaterialTapTargetSequence().addPrompt(
                        MaterialTapTargetPrompt.Builder(this)
                            .setTarget(R.id.add_Item)
                            .setPrimaryText(getString(R.string.create_item))
                            .setSecondaryText(getString(R.string.click_to_create_new))
                            .create(), 4000
                    )
                    .addPrompt(
                        MaterialTapTargetPrompt.Builder(this)
                            .setTarget(R.id.delete_All)
                            .setPrimaryText(getString(R.string.delete_list))
                            .setSecondaryText(getString(R.string.click_to_delete_list))
                            .create(), 4000
                    ).show()
            }
        }
        saveStartInfo()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_Item) {
            showAddShopDialog()
        } else if (item.itemId == R.id.delete_All) {
            deleteAll()
        }
        return super.onOptionsItemSelected(item)
    }

    fun saveStartInfo(){
        var sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        var editor = sharedPref.edit()
        editor.putBoolean(KEY_STARTED, true)
        editor.putString(KEY_LAST_USED, Date(System.currentTimeMillis()).toString())
        editor.apply()
    }

    fun wasStartedBefore() : Boolean {
        var sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getBoolean(KEY_STARTED, false)
    }

    private fun initRecyclerView(){
        Thread{
            var itemList = AppDatabase.getInstance(this).itemDao().getAllItems()
            runOnUiThread{
                itemAdapter = ItemAdapter(this, itemList)
                recyclerList.adapter = itemAdapter
            }
        }.start()
    }

    fun showAddShopDialog(){
        ShopDialog().show(supportFragmentManager, "Dialog")
    }

    var editIndex: Int = -1
    public fun showEditShopDialog(itemToEdit: Item, index: Int){
        editIndex = index
        val editItemDialog = ShopDialog()
        val bundle = Bundle()
        bundle.putSerializable(KEY_EDIT, itemToEdit)
        editItemDialog.arguments = bundle
        editItemDialog.show(supportFragmentManager,"Edit Dialog")
    }

    fun saveItem(item: Item){
        Thread{
            item.itemId = AppDatabase.getInstance(this).itemDao().insertItem(item)
            runOnUiThread {
                itemAdapter.addItem(item)
            }
        }.start()
    }

    fun deleteAll(){
        Thread{
            AppDatabase.getInstance(this).itemDao().deleteAll()
            runOnUiThread {
                itemAdapter.removeAll()
            }
        }.start()
    }
    override fun itemCreated(item: Item){
        saveItem(item)
    }

    override fun itemUpdated(item: Item){
        Thread{
            AppDatabase.getInstance(this).itemDao().updateItem(item)
            runOnUiThread {
                itemAdapter.updateItem(item,editIndex)
            }
        }.start()
    }
















}

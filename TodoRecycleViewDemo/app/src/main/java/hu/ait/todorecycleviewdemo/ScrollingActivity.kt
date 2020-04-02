package hu.ait.todorecycleviewdemo

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import hu.ait.todorecycleviewdemo.adapter.TodoAdapter
import hu.ait.todorecycleviewdemo.data.AppDatabase
import hu.ait.todorecycleviewdemo.data.Todo
import hu.ait.todorecycleviewdemo.touch.TodoRecyclerTouchCallback
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlinx.android.synthetic.main.todo_row.*
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import java.sql.Date
import java.util.*

class ScrollingActivity : AppCompatActivity(), TodoDialog.TodoHandler {

    lateinit var todoAdapter: TodoAdapter

    companion object{
        const val KEY_EDIT = "KEY_EDIT"
        const val PREF_NAME = "PREFTODO"
        const val KEY_STARTED = "KEY_STARTED"
        const val KEY_LAST_USED = "LAST_USED"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)

        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            // create new item
            showAddTodoDialog()
        }


        initRecyclerView()

        if(!wasStartedBefore()) {
            //tutorial view set up
            MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.fab)
                .setPrimaryText("Create todo")
                .setSecondaryText("Click here to create new items").show()
        }
        saveStartInfo()
    }

    //sharePreference set up
    fun saveStartInfo(){
        var sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        var editor = sharedPref.edit()
        editor.putBoolean(KEY_STARTED, true)
        editor.putString(KEY_LAST_USED, Date(System.currentTimeMillis()).toString())
        editor.apply()
    }

    fun wasStartedBefore() : Boolean {
        var sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        var lastTime = sharedPref.getString(KEY_LAST_USED, "This is the first time")
        Toast.makeText(this, lastTime, Toast.LENGTH_LONG).show()

        return sharedPref.getBoolean(KEY_STARTED, false)
    }

    private fun initRecyclerView() {
        Thread {
            var todoList = AppDatabase.getInstance(this).todoDao().getAllTodos()

            runOnUiThread {
                todoAdapter = TodoAdapter(this, todoList)
                recyclerTodo.adapter = todoAdapter

                val touchCallbakList = TodoRecyclerTouchCallback(todoAdapter)
                val itemTouchHelper = ItemTouchHelper(touchCallbakList)
                itemTouchHelper.attachToRecyclerView(recyclerTodo)
            }
        }.start()
    }


    fun showAddTodoDialog() {
        TodoDialog().show(supportFragmentManager, "Dialog")
    }

    var editIndex: Int = -1
    public fun showEditTodoDialog(todoToEdit: Todo, index: Int){
        editIndex = index
        val editItemDialog = TodoDialog()
        val bundle = Bundle()
        bundle.putSerializable(KEY_EDIT,todoToEdit)
        editItemDialog.arguments = bundle

        editItemDialog.show(supportFragmentManager,"EDITDIALOG")
    }

    fun saveTodo(todo: Todo) {
        Thread {
            todo.todoId = AppDatabase.getInstance(this).todoDao().insertTodo(todo)

            runOnUiThread {
                todoAdapter.addTodo(todo)
            }
        }.start()
    }

    override fun todoCreated(todo: Todo) {
        saveTodo(todo)
    }

    override fun todoUpdated(todo: Todo) {
        Thread{
            AppDatabase.getInstance(this).todoDao().updateTodo(todo)

            runOnUiThread {
                todoAdapter.updateTodo(todo,editIndex)
            }
        }.start()
    }
}


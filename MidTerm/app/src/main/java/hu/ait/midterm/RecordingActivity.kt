package hu.ait.midterm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hu.ait.midterm.adapter.ListAdapter
import hu.ait.midterm.data.Inputs
import kotlinx.android.synthetic.main.activity_recording.*

class RecordingActivity : AppCompatActivity() {
    lateinit var listAdapter: ListAdapter
    var expense = "0"
    var income = "0"
    var balance = "0"
    var balanceText = balance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recording)

        btnSummary.setOnClickListener {

            val intent = Intent(this, SummaryActivity::class.java)
            intent.putExtra("Balance", balance)
            intent.putExtra("Expense", expense)
            intent.putExtra("Income", income)
            startActivity(intent)
        }

        btnDeleteAll.setOnClickListener {
            listAdapter.deleteAll()
            balance = "0"
            balanceText = getString(R.string.current_balance)+balance
            tvBalance.text= balanceText
            expense = "0"
            income = "0"
        }

        btnSave.setOnClickListener {
            try {
                if(etType.text.toString() == ""){
                    etType.setError(getString(R.string.type_error))
                }else {
                    if (etAmount.text.toString() == "") {
                        etAmount.setError(getString(R.string.amount_error))
                    }
                    if (isIncome()) {
                        income = calIncome(income)
                    } else {
                        expense = calExpense(expense)
                    }
                    balance = (income.toLong() - expense.toLong()).toString()
                    balanceText = getString(R.string.current_balance) + balance
                    tvBalance.text = balanceText

                    listAdapter.addInput(
                        Inputs(
                            etType.text.toString(),
                            etAmount.text.toString(),
                            isIncome()
                        )
                    )
                }
            } catch (e: Exception) {}
        }
        initRecyclerView()

    }

    private fun initRecyclerView(){
        listAdapter= ListAdapter(this)
        recyclerTodo.adapter = listAdapter
    }

    public fun calExpense(expense: String):String{
        var expenseSoFar = expense.toLong()
        if(!isIncome()){
            expenseSoFar+=etAmount.text.toString().toLong()
        }
        return expenseSoFar.toString()
    }

    public fun calIncome(income : String):String{
        var incomeSofar = income.toLong()
        if(isIncome()){
            incomeSofar+=etAmount.text.toString().toLong()
        }
        return incomeSofar.toString()
    }

    public fun isIncome() : Boolean {
        return tbType.isChecked
    }

    public fun deleteInput(position: Int){
        if(listAdapter.inputItems[position].income){
            income =
                (income.toLong()-listAdapter.inputItems[position].inputAmount.toLong()).toString()
        }else {
            expense =
                (expense.toLong() - listAdapter.inputItems[position].inputAmount.toLong()).toString()
        }
        listAdapter.inputItems.removeAt(position)
        listAdapter.notifyItemRemoved(position)
        balance = (income.toLong()-expense.toLong()).toString()
        balanceText = getString(R.string.current_balance)+balance
        tvBalance.text= balanceText
    }


}

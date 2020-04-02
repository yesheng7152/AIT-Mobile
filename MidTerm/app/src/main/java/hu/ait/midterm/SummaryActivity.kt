package hu.ait.midterm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_summary.*

class SummaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)
        ivBracket.setImageResource(R.drawable.summary)
        val income = getString(R.string.income) + getIntent().getStringExtra("Income")
        val expense = getString(R.string.expense) + getIntent().getStringExtra("Expense")
        val balance = getString(R.string.balance) + getIntent().getStringExtra("Balance")
        tvIncome.text = income
        tvExpense.text = expense
        tvSummaryBalance.text = balance

    }
}

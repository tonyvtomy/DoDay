package com.techwonders.doday.view.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.techwonders.doday.R
import com.techwonders.doday.databinding.ActivityFoodExpenseBinding
import com.techwonders.doday.model.FoodExpense
import com.techwonders.doday.model.Transaction
import com.techwonders.doday.utility.getDateTimeFromTimestamp
import com.techwonders.doday.view.adapter.FoodExpenseAdapter
import com.techwonders.doday.viewModel.FoodExpenseViewModel
import com.techwonders.doday.viewModel.TransactionViewModel
import java.util.*
import java.util.Calendar.PM

class FoodExpenseActivity : BaseActivity() {

    lateinit var binding: ActivityFoodExpenseBinding
    lateinit var foodExpenseViewModel: FoodExpenseViewModel
    lateinit var transactionViewModel: TransactionViewModel
    private var foodExpenseAdapter: FoodExpenseAdapter? = null
    var cal: Calendar = Calendar.getInstance()

    @SuppressLint("SetTextI18n")
    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            cal.set(Calendar.HOUR_OF_DAY, 11)
            cal.set(Calendar.MINUTE, 59)
            cal.set(Calendar.SECOND, 59)
            cal.set(Calendar.AM_PM, PM)
            addTransaction()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        title = getString(R.string.food_expense)
        foodExpenseViewModel = ViewModelProvider(this)[FoodExpenseViewModel::class.java]
        transactionViewModel = ViewModelProvider(this)[TransactionViewModel::class.java]
        addAdapters()
        addObservers()
        setTouchListeners()

        binding.tvErrorMsg.visibility = VISIBLE
        binding.rvFoodExpense.visibility = GONE
        binding.bottomBarTotal.visibility = GONE
    }

    private fun showDateDialog() {
        DatePickerDialog(
            this@FoodExpenseActivity,
            dateSetListener,
            // set DatePickerDialog to point to today's date when it loads up
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun addAdapters() {
        foodExpenseAdapter = FoodExpenseAdapter { pos ->
            val expense = foodExpenseAdapter?.getFoodExpense(pos)
            val editExpense = Intent(this, FoodExpenseEntryActivity::class.java)
            editExpense.putExtra("expense", expense)
            newDeliveryEntryLauncher.launch(editExpense)
        }
        binding.rvFoodExpense.layoutManager = LinearLayoutManager(this)
        binding.rvFoodExpense.adapter = foodExpenseAdapter
    }

    private fun addObservers() {
        foodExpenseViewModel.getAllFoodExpense()
            .observe(this@FoodExpenseActivity) { foodExpenses: List<FoodExpense> ->
                Log.d("FOOD_EXPENSE", "sortedNoteList observed ${foodExpenses.size}")
                foodExpenseAdapter?.updateFoodExpenseList(foodExpenses)
                setTotalExpense(foodExpenses)
            }
    }

    private fun setTotalExpense(foodExpenses: List<FoodExpense>) {
        val totalExpense = getTotalExpense(foodExpenses)

        if (foodExpenses.isNotEmpty()) {
            binding.tvErrorMsg.visibility = GONE
            binding.rvFoodExpense.visibility = VISIBLE
        } else {
            binding.tvErrorMsg.visibility = VISIBLE
            binding.rvFoodExpense.visibility = GONE
            binding.bottomBarTotal.visibility = GONE
        }

        if (totalExpense > 0) {
            binding.bottomBarTotal.visibility = VISIBLE
        }
        binding.tvTotalExp.text = getString(R.string.total_expense) + " ₹$totalExpense"
    }

    private fun getTotalExpense(foodExpenses: List<FoodExpense>): Int {
        var totalExpense = 0
        foodExpenses.forEach {
            totalExpense += it.getTotalExpense()
        }
        return totalExpense
    }

    private fun setTouchListeners() {
        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                foodExpenseAdapter?.let { deleteFoodExpense(it.getFoodExpense(viewHolder.adapterPosition)) }
            }
        }).attachToRecyclerView(binding.rvFoodExpense)
    }

    fun deleteFoodExpense(foodExpense: FoodExpense) {
        foodExpenseViewModel.delete(foodExpense)
        //add the snackBar and the undo button
        Snackbar.make(
            findViewById(R.id.viewNoteContainer),
            "Food Expense Deleted.",
            Snackbar.LENGTH_LONG
        )
            .setAction("UNDO") {
                foodExpenseViewModel.insert(foodExpense)
            }.show()
    }

    private var newDeliveryEntryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                val foodExpense = if (Build.VERSION.SDK_INT >= 33) {
                    data?.getParcelableExtra("expense", FoodExpense::class.java)
                } else {
                    data?.getParcelableExtra("expense")
                }

                if (foodExpense!!.id > 0) {
                    foodExpenseViewModel.update(foodExpense)
                    Toast.makeText(this, "Food expense updated", Toast.LENGTH_SHORT).show()
                } else {
                    foodExpenseViewModel.insert(foodExpense)
                    Toast.makeText(this, "Food expense added", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_food_expense, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.newExpense -> {
                newDeliveryEntryLauncher.launch(Intent(this, FoodExpenseEntryActivity::class.java))
            }
            R.id.addToTransaction -> {
                if (foodExpenseAdapter?.getAllFoodExpense()?.isNotEmpty() == true)
                    showDateDialog()
            }
            R.id.transactions -> {
                startActivity(Intent(this, TransactionActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addTransaction() {
        val totalExpense = getTotalExpense(foodExpenseAdapter?.getAllFoodExpense()!!)

        Log.d("FOOD_EXPENSE", "Date: ${getDateTimeFromTimestamp(cal.timeInMillis.toString())}")

        val transaction = Transaction()
        transaction.total = totalExpense
        transaction.timestamp = cal.timeInMillis.toString()
        transactionViewModel.insert(transaction)

        Toast.makeText(this, "Added to Transactions", Toast.LENGTH_SHORT).show()
    }
}

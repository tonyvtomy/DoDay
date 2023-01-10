package com.techwonders.doday.view.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.techwonders.doday.R
import com.techwonders.doday.databinding.ActivityFoodExpenseEntryBinding
import com.techwonders.doday.model.FoodExpense
import com.techwonders.doday.utility.*
import com.techwonders.doday.viewModel.FoodExpenseViewModel
import java.util.*

class FoodExpenseEntryActivity : BaseActivity() {

    private val TAG = "FoodEntryActivity"
    private lateinit var binding: ActivityFoodExpenseEntryBinding
    var cal: Calendar = Calendar.getInstance()
    var foodExpense = FoodExpense()
    lateinit var foodExpenseViewModel: FoodExpenseViewModel
    private lateinit var foodExpenseList: List<FoodExpense>

    @SuppressLint("SetTextI18n")
    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            foodExpense.timestamp = cal.timeInMillis.toString()
            binding.tvDate.text =
                "Date selected ${getDateFromTimestamp(cal.timeInMillis.toString())}"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodExpenseEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        init()
    }

    override fun onStart() {
        super.onStart()
        addObservers()
    }

    private fun init() {
        title = getString(R.string.new_food_expense)

        binding.tvReset.visibility = GONE
        foodExpenseList = arrayListOf()
        foodExpenseViewModel = ViewModelProvider(this)[FoodExpenseViewModel::class.java]

        val breakfast = MySharedPrefUtils(this).getInt(BREAKFAST_AMOUNT)
        val lunch = MySharedPrefUtils(this).getInt(LUNCH_AMOUNT)
        val dinner = MySharedPrefUtils(this).getInt(DINNER_AMOUNT)

        val editFoodExpense = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("expense", FoodExpense::class.java)
        } else {
            intent.getParcelableExtra("expense")
        }

        editFoodExpense?.let {
            title = getString(R.string.edit_food_expense)
            binding.tvReset.visibility = VISIBLE
            binding.btnSave.text = getString(R.string.update)

            foodExpense = editFoodExpense

            binding.etTitle.setText(foodExpense.title)
            binding.tvDate.text =
                "Date selected ${getDateFromTimestamp(foodExpense.timestamp)}"

            if (foodExpense.forBreakfast > 0) binding.chBreakfast.isChecked = true
            if (foodExpense.forLunch > 0) binding.chLunch.isChecked = true
            if (foodExpense.forDinner > 0) binding.chDinner.isChecked = true

        }

        val isSettingsSet = breakfast != -1 && lunch != -1 && dinner != -1

        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        binding.tvDate.setOnClickListener {
            DatePickerDialog(
                this@FoodExpenseEntryActivity,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.tvReset.setOnClickListener {
            foodExpense.forBreakfast = 0
            foodExpense.forLunch = 0
            foodExpense.forDinner = 0
            Toast.makeText(this, getString(R.string.values_reset), Toast.LENGTH_SHORT).show()
        }

        binding.btnSave.setOnClickListener {
            var tag = ""
            if (binding.chBreakfast.isChecked) {
                foodExpense.forBreakfast =
                    if (foodExpense.forBreakfast > 0) foodExpense.forBreakfast else breakfast
                tag += "B"
            } else {
                foodExpense.forBreakfast = 0
            }

            if (binding.chLunch.isChecked) {
                foodExpense.forLunch = if (foodExpense.forLunch > 0) foodExpense.forLunch else lunch
                tag += "L"
            } else {
                foodExpense.forLunch = 0
            }

            if (binding.chDinner.isChecked) {
                foodExpense.forDinner =
                    if (foodExpense.forDinner > 0) foodExpense.forDinner else dinner
                tag += "D"
            } else {
                foodExpense.forDinner = 0
            }

            if (!isSettingsSet) {
                Toast.makeText(this, getString(R.string.plz_set_exp_amount), Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (foodExpense.timestamp.isEmpty()) {
                Toast.makeText(this, getString(R.string.expense_date), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (foodExpenseExits(foodExpense.timestamp)) {
                Toast.makeText(this, getString(R.string.expense_entry_exits), Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            var title = binding.etTitle.text.trim().toString()
            if (title.isEmpty())
                title = foodExpense.getTotalExpense().let {
                    if (it > 0) getString(R.string.food_expense) else getString(R.string.no_food_expense)
                }

            foodExpense.title = title
            foodExpense.tag = tag

            Log.d("FOOD_EXPENSE", "Food Expense: $foodExpense")

            val dataIntent = Intent()
            dataIntent.putExtra("expense", foodExpense)
            setResult(RESULT_OK, dataIntent)
            finish()
        }

    }

    private fun addObservers() {
        foodExpenseViewModel.getAllFoodExpense()
            .observe(this@FoodExpenseEntryActivity) { foodExpenses: List<FoodExpense> ->
                Log.d("FOOD_EXPENSE", "sortedNoteList observed ${foodExpenses.size}")
                foodExpenseList = foodExpenses
            }
    }

    private fun foodExpenseExits(timestamp: String): Boolean {
        foodExpenseList.forEach {
            val isSameDay = isDateSameDay(it.timestamp, timestamp) && it.id != foodExpense.id
            if (isSameDay)
                return true
        }
        return false
    }

}

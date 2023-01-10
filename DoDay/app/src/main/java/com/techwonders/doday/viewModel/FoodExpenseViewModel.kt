package com.techwonders.doday.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.techwonders.doday.model.FoodExpense
import com.techwonders.doday.repository.FoodExpenseRepo

class FoodExpenseViewModel(application: Application) : AndroidViewModel(application) {
    private val foodExpenseRepo: FoodExpenseRepo
    private val foodExpenseList: LiveData<List<FoodExpense>>

    init {
        foodExpenseRepo = FoodExpenseRepo(application)
        foodExpenseList = foodExpenseRepo.getAllFoodExpense()
    }

    // Orders
    fun insert(foodExpense: FoodExpense?) = foodExpenseRepo.insertData(foodExpense)

    fun update(foodExpense: FoodExpense?) = foodExpenseRepo.updateData(foodExpense)

    fun delete(foodExpense: FoodExpense?) = foodExpenseRepo.deleteData(foodExpense)

    fun deleteAllFoodExpense() = foodExpenseRepo.deleteAllFoodExpense()

//    fun updateStatus(status: Int) = foodExpenseRepo.updateStatus(status)

    fun getAllFoodExpense() = foodExpenseList

}

package com.techwonders.doday.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.techwonders.doday.db.DoDayDatabase
import com.techwonders.doday.db.FoodExpenseDao
import com.techwonders.doday.model.FoodExpense

class FoodExpenseRepo(application: Application?) {
    private val foodExpenseDao: FoodExpenseDao
    private val foodExpenseList: LiveData<List<FoodExpense>>

    init {
        val doDayDatabase = DoDayDatabase.getInstance(application)
        foodExpenseDao = doDayDatabase.foodExpenseDao()
        foodExpenseList = foodExpenseDao.allFoodExpense
    }

    /// Food Expense
    fun insertData(foodExpense: FoodExpense?) {
        DoDayDatabase.databaseWriteExecutor.execute { foodExpenseDao.insert(foodExpense) }
    }

    fun updateData(foodExpense: FoodExpense?) {
        DoDayDatabase.databaseWriteExecutor.execute { foodExpenseDao.update(foodExpense) }
    }

    fun deleteData(foodExpense: FoodExpense?) {
        DoDayDatabase.databaseWriteExecutor.execute { foodExpenseDao.delete(foodExpense) }
    }

    fun deleteAllFoodExpense() {
        DoDayDatabase.databaseWriteExecutor.execute { foodExpenseDao.deleteAllFoodExpense() }
    }

    /*fun updateStatus(status: Int) {
        DoDayDatabase.databaseWriteExecutor.execute { foodExpenseDao.updateStatus(status) }
    }*/

    fun getAllFoodExpense() = foodExpenseList

}

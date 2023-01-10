package com.techwonders.doday.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.techwonders.doday.db.DoDayDatabase
import com.techwonders.doday.db.FoodExpenseDao
import com.techwonders.doday.db.TransactionDao
import com.techwonders.doday.model.Transaction

class TransactionRepo(application: Application?) {
    private val transactionDao: TransactionDao
    private val foodExpenseDao: FoodExpenseDao
    private val allTransactions: LiveData<List<Transaction>>

    init {
        val doDayDatabase = DoDayDatabase.getInstance(application)
        transactionDao = doDayDatabase.transactionDao()
        foodExpenseDao = doDayDatabase.foodExpenseDao()
        allTransactions = transactionDao.allTransaction
    }

    /// Transaction
    fun insertData(transaction: Transaction?) {
        DoDayDatabase.databaseWriteExecutor.execute {
            val primaryKey = transactionDao.insert(transaction)
            foodExpenseDao.updateStatus(0, primaryKey.toInt())
        }
    }

    fun updateData(transaction: Transaction?) {
        DoDayDatabase.databaseWriteExecutor.execute { transactionDao.update(transaction) }
    }

    fun deleteData(transaction: Transaction?) {
        DoDayDatabase.databaseWriteExecutor.execute { transactionDao.delete(transaction) }
        transaction?.id?.let {
            DoDayDatabase.databaseWriteExecutor.execute { foodExpenseDao.updateStatus(it, 0) }
        }
    }

    fun deleteAllTransactions() {
        DoDayDatabase.databaseWriteExecutor.execute { transactionDao.deleteAllTransactions() }
    }

    fun getAllTransactions() = allTransactions

}

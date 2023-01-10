package com.techwonders.doday.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.techwonders.doday.model.Transaction
import com.techwonders.doday.repository.TransactionRepo

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val transactionRepo: TransactionRepo
    private val allTransactions: LiveData<List<Transaction>>

    init {
        transactionRepo = TransactionRepo(application)
        allTransactions = transactionRepo.getAllTransactions()
    }

    // Transactions
    fun insert(transaction: Transaction?) = transactionRepo.insertData(transaction)

    fun update(transaction: Transaction?) = transactionRepo.updateData(transaction)

    fun delete(transaction: Transaction?){
        transactionRepo.deleteData(transaction)
    }

    fun deleteAllTransactions() = transactionRepo.deleteAllTransactions()

    fun getAllTransactions() = allTransactions

}

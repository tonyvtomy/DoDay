package com.techwonders.doday.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.techwonders.doday.R
import com.techwonders.doday.databinding.ActivityTransactionBinding
import com.techwonders.doday.model.Transaction
import com.techwonders.doday.utility.getDateFromTimestamp
import com.techwonders.doday.view.adapter.TransactionAdapter
import com.techwonders.doday.viewModel.TransactionViewModel

class TransactionActivity : BaseActivity() {

    lateinit var binding: ActivityTransactionBinding

    lateinit var transactionViewModel: TransactionViewModel
    private var transactionAdapter: TransactionAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        title = getString(R.string.transactions)
        transactionViewModel = ViewModelProvider(this)[TransactionViewModel::class.java]
        addAdapters()
        addObservers()
        setTouchListeners()

        binding.tvErrorMsg.visibility = View.VISIBLE
        binding.rvFoodExpense.visibility = View.GONE
        binding.bottomBarTotal.visibility = View.GONE
    }

    private fun addAdapters() {
        transactionAdapter = TransactionAdapter() { pos ->
            val transaction = transactionAdapter?.getTransaction(pos)
            val editTransaction = Intent(this, FoodExpenseEntryActivity::class.java)
            editTransaction.putExtra("transaction", transaction)
//            newDeliveryEntryLauncher.launch(editTransaction)
        }
        binding.rvFoodExpense.layoutManager = LinearLayoutManager(this)
        binding.rvFoodExpense.adapter = transactionAdapter
    }

    private fun addObservers() {
        transactionViewModel.getAllTransactions()
            .observe(this@TransactionActivity) { transactions: List<Transaction> ->
                Log.d("FOOD_EXPENSE", "sortedNoteList observed ${transactions.size}")
                transactionAdapter?.updateTransactionList(transactions)
                totalExpense(transactions)
            }
    }

    private fun totalExpense(transactions: List<Transaction>) {
        var totalExpense = 0
        transactions.forEach {
            totalExpense += it.total
        }

        if (transactions.isNotEmpty()) {
            binding.tvErrorMsg.visibility = View.GONE
            binding.rvFoodExpense.visibility = View.VISIBLE
        } else {
            binding.tvErrorMsg.visibility = View.VISIBLE
            binding.rvFoodExpense.visibility = View.GONE
            binding.bottomBarTotal.visibility = View.GONE
        }

        if (totalExpense > 0) {
            binding.bottomBarTotal.visibility = View.VISIBLE
        }
        binding.tvTotalExp.text = getString(R.string.total_expense) + " ₹$totalExpense"
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
                transactionAdapter?.let { deleteTransaction(it.getTransaction(viewHolder.adapterPosition)) }
            }
        }).attachToRecyclerView(binding.rvFoodExpense)
    }

    fun deleteTransaction(transaction: Transaction) {
        transactionViewModel.delete(transaction)
        //add the snackBar and the undo button
        Snackbar.make(
            findViewById(R.id.viewNoteContainer),
            "Transaction Deleted.",
            Snackbar.LENGTH_LONG
        )
            .setAction("UNDO") {
                transactionViewModel.insert(transaction)
            }.show()
    }

}

package com.techwonders.doday.view.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techwonders.doday.R
import com.techwonders.doday.databinding.SingleTransactionItemBinding
import com.techwonders.doday.listeners.OnItemClickListener
import com.techwonders.doday.model.Transaction
import com.techwonders.doday.utility.getDateFromTimestamp

class TransactionAdapter(private var onItemClickListener: OnItemClickListener?) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    private var transactionList: List<Transaction>? = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.single_transaction_item, parent, false)
        return TransactionViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = getTransaction(holder.adapterPosition)
        holder.binding.tvTitle.text = transaction.title
        holder.binding.tvDate.text = "${getDateFromTimestamp(transaction.timestamp)}"
        holder.binding.tvAmount.text = "₹${transaction.total}"

        holder.binding.root.setOnClickListener {
            if (onItemClickListener != null) onItemClickListener!!.onItemClick(
                position
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateTransactionList(newTransactionList: List<Transaction>?) {
        transactionList = arrayListOf()
        transactionList = newTransactionList
        Log.d("FOOD_EXPENSE", "updateFoodExpenseList ${transactionList?.size}")

        notifyDataSetChanged()
    }

    override fun getItemCount() = transactionList!!.size

    fun getTransaction(pos: Int) = transactionList!![pos]

    fun getAllTransaction(): List<Transaction>? = transactionList

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: SingleTransactionItemBinding

        init {
            binding = SingleTransactionItemBinding.bind(itemView)
        }
    }

}

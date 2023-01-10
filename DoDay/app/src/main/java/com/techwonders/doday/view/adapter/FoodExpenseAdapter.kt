package com.techwonders.doday.view.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techwonders.doday.R
import com.techwonders.doday.databinding.SingleFoodExpenseItemBinding
import com.techwonders.doday.listeners.OnItemClickListener
import com.techwonders.doday.model.FoodExpense
import com.techwonders.doday.utility.getDateFromTimestamp

class FoodExpenseAdapter(private var onItemClickListener: OnItemClickListener?) :
    RecyclerView.Adapter<FoodExpenseAdapter.FoodDeliveryViewHolder>() {

    private var foodExpenseList: List<FoodExpense>? = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodDeliveryViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.single_food_expense_item, parent, false)
        return FoodDeliveryViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FoodDeliveryViewHolder, position: Int) {
        val foodExpense = getFoodExpense(holder.adapterPosition)
        holder.binding.tvTitle.text = foodExpense.title
        holder.binding.tvDate.text = "${getDateFromTimestamp(foodExpense.timestamp)}"
        holder.binding.tvAmount.text = "₹${foodExpense.getTotalExpense()}"
        holder.binding.tvTag.text = "${foodExpense.tag.uppercase()}"

        holder.binding.root.setOnClickListener {
            if (onItemClickListener != null) onItemClickListener!!.onItemClick(
                position
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateFoodExpenseList(newOrdersList: List<FoodExpense>?) {
        foodExpenseList = arrayListOf()
        foodExpenseList = newOrdersList
        Log.d("FOOD_EXPENSE", "updateFoodExpenseList ${foodExpenseList?.size}")

        notifyDataSetChanged()
    }

    override fun getItemCount() = foodExpenseList!!.size

    fun getFoodExpense(pos: Int) = foodExpenseList!![pos]

    fun getAllFoodExpense(): List<FoodExpense>? = foodExpenseList

    class FoodDeliveryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: SingleFoodExpenseItemBinding

        init {
            binding = SingleFoodExpenseItemBinding.bind(itemView)
        }
    }

}

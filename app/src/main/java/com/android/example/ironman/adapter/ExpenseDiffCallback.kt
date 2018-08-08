package com.android.example.ironman.adapter

import android.support.v7.util.DiffUtil
import com.android.example.ironman.db.Expense

class ExpenseDiffCallback(private val mOldExpenseList: List<Expense>, private val mNewExpenseList: List<Expense>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = mOldExpenseList.size

    override fun getNewListSize(): Int = mNewExpenseList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            mOldExpenseList[oldItemPosition].id.equals(mNewExpenseList[newItemPosition].id)

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldExpense = mOldExpenseList[oldItemPosition]
        val newExpense = mNewExpenseList[newItemPosition]

        return oldExpense.money == newExpense.money &&
                oldExpense.catergory == newExpense.catergory &&
                oldExpense.description == newExpense.description &&
                oldExpense.date == newExpense.date &&
                oldExpense.time == newExpense.time
    }


}

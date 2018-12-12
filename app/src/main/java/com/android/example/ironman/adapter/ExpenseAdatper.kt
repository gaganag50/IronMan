package com.android.example.ironman.adapter

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.example.ironman.R
import com.android.example.ironman.db.Expense
import java.util.*


class ExpenseAdatper(
        val data: ArrayList<Expense>,
        private val clickListener: (Int) -> Unit,
        private val longClickListener: (Int) -> Unit
) : RecyclerView.Adapter<ExpenseAdatper.ExpenseViewHolder>() {
    val TAG: String = "ExpenseAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = li.inflate(R.layout.activity_list, parent, false)


        return ExpenseViewHolder(itemView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = data[position]
        Log.d(TAG, ": data ${data.toList()}")
        Log.d(TAG, ": position $position")

        holder.bind(expense, clickListener, longClickListener, position)

    }

    fun getItem(position: Int): Expense {
        return data[position]
    }

    fun getId(position: Int): Long? {
        return data[position].id
    }

    fun updateExpenseListItems(expenseList: List<Expense>) {
        val diffCallback = ExpenseDiffCallback(this.data, expenseList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.data.clear()
        this.data.addAll(expenseList)
        diffResult.dispatchUpdatesTo(this)
    }

    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(expense: Expense, clickListener: (Int) -> Unit, longClickListener: (Int) -> Unit, position: Int) {



            itemView.findViewById<TextView>(R.id.tvMoneySignUpper).text = "Rs"
            itemView.findViewById<TextView>(R.id.tvMoneySignLower).text = "Rs"
            itemView.findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.ic_launcher_background)
            itemView.findViewById<TextView>(R.id.tvCategory).text = expense.catergory
            itemView.findViewById<TextView>(R.id.tvDescription).text = expense.description
            itemView.findViewById<TextView>(R.id.tvTotalForDay).text = expense.money.toString()
            itemView.findViewById<TextView>(R.id.tvMoney).text = expense.money.toString()

            itemView.findViewById<TextView>(R.id.tvDate).text = expense.exclusiveDate
            itemView.findViewById<TextView>(R.id.tvMonth).text = expense.month
            itemView.findViewById<TextView>(R.id.tvTime).text = expense.time
            Log.d("Adapter", ": expense.time ${expense.time}")

            itemView.findViewById<TextView>(R.id.tvDay).text = expense.day


            itemView.setOnClickListener {
                clickListener(position)
            }
            itemView.setOnLongClickListener {
                longClickListener(position)
                true
            }

        }


    }


}






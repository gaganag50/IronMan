package com.android.example.ironman.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.example.ironman.R
import com.android.example.ironman.db.Expense
import java.util.*
import android.text.method.TextKeyListener.clear
import android.support.v7.util.DiffUtil




class ExpenseAdatper(
        val data: ArrayList<Expense>,
        private val clickListener: (Int) -> Unit,
        private val longClickListener: (Int) -> Unit
) : RecyclerView.Adapter<ExpenseAdatper.ExpenseViewHolder>() {
    val TAG: String = "ExpenseAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = li.inflate(R.layout.activity_list_item, parent, false)


        return ExpenseViewHolder(itemView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = data[position]
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


            itemView.findViewById<TextView>(R.id.tvtotal).text = expense.money.toString()
            itemView.findViewById<TextView>(R.id.tvCategory).text = expense.catergory
            itemView.findViewById<TextView>(R.id.tvNote).text = expense.description
            itemView.findViewById<TextView>(R.id.tvDate).text = expense.date
            itemView.findViewById<TextView>(R.id.tvTime).text = expense.time
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






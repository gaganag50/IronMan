package com.android.example.ironman

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.example.ironman.db.Expense

class ExpenseAdatper(val data: ArrayList<Expense>, val context: Context?
                     , val onTaskDelete: (task: Expense) -> Unit) : RecyclerView.Adapter<ExpenseAdatper.ExpenseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = li.inflate(R.layout.activity_list_item, parent, false)
        return ExpenseViewHolder(itemView)
    }

    override fun getItemCount(): Int = data.size


    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.itemView.setOnClickListener(null)
        holder.total.setText(data[position].money.toString())
        holder.category.setText(data[position].catergory)
        Log.d("MainAct", "onBindViewHolder")

        holder.itemView.setOnClickListener {
            val intent = Intent(context, EditActivity::class.java)
            val parse = Uri.parse(position.toString())
            intent.setData(parse)
            if (context != null) {
                startActivity(context, intent, null)
            }

        }

        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(holder.itemView.context)
                    .setTitle("Delete Task")
                    .setMessage("Do you really want to delete this task ? ")
                    .setPositiveButton(
                            "YES",
                            { _, _ -> onTaskDelete(data[position]) }
                    )
                    .setNegativeButton("NO", { _, _ -> Unit })
                    .show()
            true


        }
    }

    inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val total = itemView.findViewById<TextView>(R.id.tvtotal)
        val category = itemView.findViewById<TextView>(R.id.tvCategory)

    }


}

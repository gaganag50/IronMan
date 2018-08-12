package com.android.example.ironman.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.example.ironman.R
import com.android.example.ironman.db.Category
import com.android.example.ironman.ui.CategoryAddition
import kotlinx.android.synthetic.main.list_category.view.*


class CategoryAdapter(val context: CategoryAddition, val list: ArrayList<Category>) : RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = li.inflate(R.layout.list_category, parent, false)
        return CategoryHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bind(list[position])

    }

    inner class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(stringCategoryId: Category) {

            itemView.tvCat.text =stringCategoryId.category

        }

    }

}

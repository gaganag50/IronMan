package com.android.example.ironman.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ayalma.ir.expandablerecyclerview.ExpandableRecyclerView
import com.android.example.ironman.R
import com.android.example.ironman.db.Category
import com.android.example.ironman.ui.CategoryAddition
import com.android.example.ironman.ui.TwoString
import org.json.JSONObject


class PurchaseItemRecyclerViewAdapter(
        val context: CategoryAddition,
        val list: ArrayList<Category>
) :
        ExpandableRecyclerView.Adapter<
                PurchaseItemRecyclerViewAdapter.ChildViewHolder,
                ExpandableRecyclerView.SimpleGroupViewHolder,
                TwoString,

                String
                >() {
    val TAG: String = "Purchase"


    override fun getGroupItemCount(): Int {
        return list.size - 1
    }

    override fun getChildItemCount(i: Int): Int {
        Log.d(TAG, ": getChildItemCount i $i")

        return 5
    }

    override fun getGroupItem(i: Int): String {
        // this is the first thing to appear in the logcat
        // 0..10
        Log.d(TAG, ": getGroupItem i $i")
        return list[i].category
    }

    override fun getChildItem(group: Int, child: Int): TwoString {
        Log.d(TAG, ": getChildItem group $group child $child")

        Log.d(TAG, ": list[group].subCategory?.get(child) ${list[group].subCategory?.toList()}")

        val json = JSONObject(list[group].subCategory)
        val items = json.optJSONArray("uniqueArrays")

        var str_value: String? = null


        str_value = items.optString(child)  //<< jget value from jArray
        val substring = str_value.substring(0, 1)
        return TwoString(substring, str_value!!)
    }

    override fun onCreateGroupViewHolder(parent: ViewGroup): ExpandableRecyclerView.SimpleGroupViewHolder {
        return ExpandableRecyclerView.SimpleGroupViewHolder(parent.context)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.purchase_list_content, parent, false)
        return ChildViewHolder(rootView)
    }

    override fun onBindGroupViewHolder(holder: ExpandableRecyclerView.SimpleGroupViewHolder, group: Int) {
        super.onBindGroupViewHolder(holder, group)
        holder.text = getGroupItem(group)

    }

    override fun onBindChildViewHolder(holder: ChildViewHolder, group: Int, position: Int) {
        super.onBindChildViewHolder(holder, group, position)
        holder.name.text = getChildItem(group, position).firstCategory
        holder.name2.text = getChildItem(group, position).secondCategory
    }

    override fun getChildItemViewType(i: Int, i1: Int): Int {
        return 1
    }

    inner class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var name: TextView
        internal var name2: TextView


        init {
            name = itemView.findViewById<View>(R.id.tvImage) as TextView
            name2 = itemView.findViewById<View>(R.id.tvSubCategory) as TextView


        }
    }
}


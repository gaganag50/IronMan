package com.android.example.ironman.db

import com.orm.SugarRecord
import com.orm.dsl.Table


// you must have an empty constructor for Sugar to work fine in the database class

@Table
class Expense(var money: Int = 0, category: String = "", note: String = "", date: String? = null, time: String? = null) : SugarRecord() {

    var catergory: String? = category
    var description: String? = note
    var date: String? = null
    var time: String? = null


    init {
        when {
            date != null -> this.date = date
        }
        when {
            time != null -> this.time = time
        }
    }



    override fun toString(): String {
        return "Expense --> $id $money$catergory $description"
    }

}
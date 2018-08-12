package com.android.example.ironman.db

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id


// you must have an empty constructor for Sugar to work fine in the database class

@Entity
class Expense(
        @Id var id: Long = 0,
        var money: Int = 0,
        var catergory: String = "",
        var description: String = "",
        var date: String? = null,
        var time: String? = null
) {


    override fun toString(): String {
        return "Expense --> $id $money $catergory $description"
    }


}
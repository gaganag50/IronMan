package com.android.example.ironman.db

import com.orm.SugarRecord

// this is the form in which we are filling the table
class Expense() : SugarRecord<Expense>() {
    var id: Int? = 1
    var money: Int = 0
    var catergory: String? = null



    constructor(money: Int, category: String) : this() {
        this.money = money
        this.catergory = catergory
    }

}
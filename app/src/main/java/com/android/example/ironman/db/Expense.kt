package com.android.example.ironman.db

import com.orm.SugarRecord


class Expense : SugarRecord {
    var money: Int = 0
    var catergory: String? = null
    var description: String? = null
    var time: Long = 0L


    constructor() {}


    constructor(money: Int, category: String, note: String, time: Long) {

        this.money = money
        this.catergory = category
        this.description = note
        this.time = time
    }

}
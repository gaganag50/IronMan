package com.android.example.ironman.db

import com.orm.SugarRecord
import java.sql.Time
import java.util.*


class Expense : SugarRecord {
    var money: Int = 0
    var catergory: String? = null
    var description: String? = ""
    var date: String? = null

    var time: String? = null


    constructor() {}


    constructor(money: Int, category: String, note: String, date: String?, time: String?) {

        this.money = money
        this.catergory = category
        this.description = note
        if (date != null)
            this.date = date
        if (time != null)
            this.time = time
    }

}
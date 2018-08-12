package com.android.example.ironman.db

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id


@Entity
class Category(var category: String = "", var subCategory: String = "", @Id var id: Long = 0) {

    override fun toString(): String {
        return "\n $category"
    }


}
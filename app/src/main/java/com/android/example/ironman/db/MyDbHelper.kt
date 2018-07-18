package com.android.example.ironman.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


val DB_NAME = "EXPENSES.db"
val DB_VERSION = 1

class MyDbHelper(context: Context?) : SQLiteOpenHelper(
        context, DB_NAME, null, DB_VERSION
) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(Table.CMD_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}

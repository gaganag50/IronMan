package com.android.example.ironman.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase


// this is the name of the column heading in the table
object Columns {
    val ID: String = "id"
    val EXPENSE: String = "expense"
    val CATEGORY: String = "category"
}


class Table {
    companion object {
        val TABLE_NAME = "expenses"
        val CMD_CREATE_TABLE = """
        CREATE TABLE IF NOT EXISTS ${TABLE_NAME}
        (
        ${Columns.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${Columns.EXPENSE} INTEGER,
        ${Columns.CATEGORY} TEXT
        );
    """.trimIndent()

        fun addExpense(db: SQLiteDatabase, expense: Expense): Long {
            val row = ContentValues()
            row.put(Columns.EXPENSE, expense.money)
            row.put(Columns.CATEGORY, expense.catergory)
            return db.insert(TABLE_NAME, null, row)
        }


        fun updateExpense(db: SQLiteDatabase, expense: Expense): Int {
            val whereClause = "${Columns.ID}=?"
            val whereArgs = arrayOf(expense.id.toString())
            val row = ContentValues()
            row.put(Columns.EXPENSE, expense.money)
            row.put(Columns.CATEGORY, expense.catergory)
            return db.update(TABLE_NAME, row, whereClause, whereArgs)
        }

        fun getAll(db: SQLiteDatabase): ArrayList<Expense> {
            val query = db.query(TABLE_NAME, arrayOf(
                    Columns.ID, Columns.CATEGORY, Columns.EXPENSE
            ), null, null, null, null, null
            )
            val list = ArrayList<Expense>()

            while (query.moveToNext()) {
                list.add(Expense(
                        query.getInt(query.getColumnIndex(Columns.ID)),
                        query.getInt(query.getColumnIndex(Columns.EXPENSE)),
                        query.getString(query.getColumnIndex(Columns.CATEGORY))
                ))
            }
            query.close()
            return list
        }

        fun deleteExpense(db: SQLiteDatabase, expense: Expense) {
            val whereClause = "${Columns.ID}=?"
            val whereArgs = arrayOf(expense.id.toString())
            db.delete(TABLE_NAME, whereClause, whereArgs)

        }

    }


}



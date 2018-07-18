package com.android.example.ironman

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.android.example.ironman.db.Expense
import com.android.example.ironman.db.MyDbHelper
import com.android.example.ironman.db.Table
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var adapter: ExpenseAdatper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            startActivity(Intent(this@MainActivity, EditActivity::class.java))
        }

        val db = MyDbHelper(this).writableDatabase

        val data = Table.getAll(db)
        if (data.size != 0) {
            empty_view.visibility = View.GONE
        }


        // we havve  to fill the content in the database and the arraylist
        // first i am filling the content in the database
        // then i am fetching the data in the arraylist
        // then i am binind the data with the edittexts
        // how to bind the spinner value with the view


        // when displaying here , fetch the data from the database

        fun refereshList() {
            data.clear()
            data.addAll(Table.getAll(db))
            if (data.size != 0) {
                empty_view.visibility = View.GONE
            }
            Log.d("MainAct", "" + data.toList().toString())
            adapter.notifyDataSetChanged()

        }

        val onTaskDelete= {
            expense: Expense ->
            Table.deleteExpense(db, expense)
            refereshList()
        }
        adapter = ExpenseAdatper(data, this, onTaskDelete)


        list.layoutManager = LinearLayoutManager(this)

        list.adapter = adapter


        refereshList()


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // User clicked on a menu option in the app bar overflow menu
        when (item.itemId) {
        // Respond to a click on the "settings" menu option
            R.id.settings -> {
                settings()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun settings() = startActivity(Intent(this@MainActivity, SettingsActivity::class.java))


}

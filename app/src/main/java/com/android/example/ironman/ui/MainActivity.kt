package com.android.example.ironman.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.android.example.ironman.ExpenseAdatper
import com.android.example.ironman.R
import com.android.example.ironman.SettingsActivity
import com.android.example.ironman.db.Expense
import com.orm.SugarRecord
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            startActivity(Intent(this@MainActivity, EditActivity::class.java))
        }
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

    override fun onResume() {
        super.onResume()
        refereshList()
    }

    private fun refereshList() {

        val expenses = SugarRecord.listAll(Expense::class.java)
        val listOfExpenses = ArrayList<Expense>(expenses.toList())
        if (listOfExpenses.size > 0) {
            empty_view.visibility = View.INVISIBLE
        } else {
            empty_view.visibility = View.VISIBLE
        }
        rvList.layoutManager = LinearLayoutManager(this)
        val adapter = ExpenseAdatper(listOfExpenses)



        rvList.adapter = adapter
//            adapter.setOnCardClickListner(this)

    }

}


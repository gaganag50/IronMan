package com.android.example.ironman.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.android.example.ironman.R
import com.android.example.ironman.adapter.ExpenseAdatper
import com.android.example.ironman.db.Expense
import com.orm.SugarRecord
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v7.widget.DividerItemDecoration




class MainActivity : AppCompatActivity() {


    private val tag: String = "MainAct"
    private lateinit var adapter: ExpenseAdatper

    private var initialCount: Long = 0
    private lateinit var listOfExpenses: ArrayList<Expense>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            startActivity(Intent(this@MainActivity, EditActivity::class.java))
        }

        initialCount = SugarRecord.count<Expense>(Expense::class.java)




        if (initialCount >= 0) {


            listOfExpenses = ArrayList(SugarRecord.listAll<Expense>(Expense::class.java).toList())
            listOfExpenses.size
            showingEmptyView()
            adapter = ExpenseAdatper(
                    listOfExpenses,
                    { position: Int -> onItemClick(position) }
            ) { position: Int -> onLongItemClick(position) }

            rvList.layoutManager = LinearLayoutManager(this)
            rvList.addItemDecoration(DividerItemDecoration(rvList.context, DividerItemDecoration.VERTICAL))

            rvList.adapter = adapter

        }


    }

    private fun showingEmptyView() =
            when {
                listOfExpenses.isEmpty() -> {
                    empty_view.visibility = View.VISIBLE


                    Snackbar.make(rvList, "click the add button to add an expense", Snackbar.LENGTH_LONG).show()
                }
                else -> empty_view.visibility = View.INVISIBLE
            }




    override fun onResume() {
        super.onResume()

        val newCount = SugarRecord.count<Expense>(Expense::class.java)


        if (newCount > initialCount) {
            refreshList()
            initialCount = newCount

        }



    }

    private fun refreshList() {
        val list = ArrayList(SugarRecord.listAll<Expense>(Expense::class.java).toList())



        showingEmptyView()
        adapter.updateExpenseListItems(list)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                settings()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun settings() = startActivity(Intent(this@MainActivity, SettingsActivity::class.java))


    private fun onItemClick(position: Int) {

        val i = Intent(this@MainActivity, EditActivity::class.java)

        i.putExtra("ID", adapter.getId(position))

        startActivity(i)


    }

    private fun onLongItemClick(position: Int) {
        val ID = position
        showDeleteMessage(ID)

    }

    private fun showDeleteMessage(ID: Int) {

        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.delete_dialog_msg)
        builder.setPositiveButton(R.string.delete) { _, _ ->


            val expense = adapter.getItem(ID)
            refreshList()
            expense.delete()
            initialCount -= 1
            refreshList()
        }
        builder.setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog?.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }


}


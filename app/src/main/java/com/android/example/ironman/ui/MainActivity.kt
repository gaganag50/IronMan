package com.android.example.ironman.ui

import android.app.AlertDialog
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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


class MainActivity : AppCompatActivity(), RecyclerItemTouch.OnItemClickListener {


    val TAG: String = "MainAct"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            startActivity(Intent(this@MainActivity, EditActivity::class.java))
        }

        rvList.addOnItemTouchListener(RecyclerItemTouch(this@MainActivity, rvList, this))
        refereshList()


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

    override fun onResume() {
        super.onResume()
        refereshList()
    }

    private fun refereshList() {
        val count = SugarRecord.count<Expense>(Expense::class.java)
        Log.d(TAG, ": count $count")
        if (count != -1L) {


            val expenses = SugarRecord.listAll(Expense::class.java)
            val listOfExpenses = ArrayList<Expense>(expenses.toList())
            Log.d(TAG, ": listOfExpenses ${listOfExpenses.size}")

            if (listOfExpenses.size > 0) {
                empty_view.visibility = View.INVISIBLE
            } else {
                empty_view.visibility = View.VISIBLE
            }
            rvList.layoutManager = LinearLayoutManager(this)
            val adapter = ExpenseAdatper(listOfExpenses)




            rvList.adapter = adapter
            adapter.notifyDataSetChanged()


        }
    }

    override fun onItemClick(view: View, position: Int) {
        val i = Intent(this@MainActivity, EditActivity::class.java)
        val currentExpenseUri = ContentUris.withAppendedId(
                Uri.parse("content://com.android.example.ironman"), position.toLong())
        i.data = currentExpenseUri
        startActivity(i)


    }

    override fun onLongItemClick(view: View?, position: Int) {
        val ID = position + 1
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.delete_dialog_msg)
        builder.setPositiveButton(R.string.delete) { dialog, id ->
            Log.d(TAG, ": setPositiveButton called")
            val expense = SugarRecord.findById(Expense::class.java, ID.toLong())
            Log.d(TAG, ": expense $expense")

            expense?.delete()
            refereshList()
        }
        builder.setNegativeButton(R.string.cancel) { dialog, id ->

            dialog?.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
        Log.d(TAG, ": onLongClick Called")

    }

//    override fun onLongItemClick(view: View?, position: Int) {
//
//        AlertDialog.Builder(this@MainActivity)
//                .setMessage(R.string.delete_dialog_msg)
//                .setPositiveButton(R.string.delete) { dialog, id ->
//                    val expense = SugarRecord.findById(Expense::class.java, id + 1)
//                    expense.delete()
//
//
//                }
//                .setNegativeButton(R.string.cancel)
//                { dialog, _ ->
//
//                    dialog?.dismiss()
//                }
//
//                .create()
//                .show()
//    }

}


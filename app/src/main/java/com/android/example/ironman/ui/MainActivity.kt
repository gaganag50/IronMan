package com.android.example.ironman.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.android.example.ironman.App.App
import com.android.example.ironman.R
import com.android.example.ironman.adapter.ExpenseAdatper
import com.android.example.ironman.db.Expense
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {

        var boxStore = App.app?.boxStore
        var expenseBox = boxStore?.boxFor(Expense::class.java)

    }


    private val tag: String = "MainAct"
    private lateinit var adapter: ExpenseAdatper

    private var initialCount: Long? = 0
    private lateinit var listOfExpenses: ArrayList<Expense>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        fab.setOnClickListener { onFabClick() }

        initialCount = expenseBox?.count()




        if (initialCount!! >= 0) {
            val list = expenseBox?.all!!.toList()
            listOfExpenses = ArrayList(list)
            showingEmptyView()
            adapter = ExpenseAdatper(
                    listOfExpenses,
                    { position: Int -> onItemClick(position) }
            ) { position: Int -> onLongItemClick(position) }

            rvList.layoutManager = LinearLayoutManager(this)

            rvList.adapter = adapter

        }


        swipeToDelete()


    }

    private fun onFabClick() {
        startActivity(Intent(this@MainActivity, EditActivity::class.java))
    }

    private fun swipeToDelete() {
        // Handling swipe to delete
        val simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //Remove swiped item from list and notify the RecyclerView

                val position = viewHolder.adapterPosition

                val expense = adapter.getItem(position)
                showDeleteMessage(position, true)
                initialCount?.minus(1)


            }

        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(rvList)
    }

    private fun showDeleteMessageOnSwipe(expense: Expense, position: Int) {
        refreshList()
        expenseBox!!.put(expense)
        listOfExpenses.add(position, expense)

        Log.d(tag, ": expense $expense")

        refreshList()
        initialCount?.plus(1)
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

        val newCount = expenseBox!!.count()


        if (newCount > initialCount!!) {
            refreshList()
            initialCount = newCount

        }


    }

    private fun refreshList() {
        val list = ArrayList(expenseBox!!.all)
        Log.d(tag, ": list ${list.toList()}")

        adapter.updateExpenseListItems(list)
        adapter.notifyDataSetChanged()
        showingEmptyView()

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

    private fun showDeleteMessage(ID: Int, boolean: Boolean = false) {

        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.delete_dialog_msg)
        val expense = adapter.getItem(ID)
        builder.setPositiveButton(R.string.delete) { _, _ ->
            expenseBox!!.remove(expense)


            initialCount?.minus(1)
            refreshList()
            Snackbar.make(rvList, "Expense deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        showDeleteMessageOnSwipe(expense, ID)
                    }
                    .show()
        }
        builder.setNegativeButton(R.string.cancel) { dialog, _ ->

            dialog?.dismiss()
            if (boolean) {
                showDeleteMessageOnSwipe(expense, ID)
            }
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }


}


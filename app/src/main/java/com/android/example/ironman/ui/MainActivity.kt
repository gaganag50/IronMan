package com.android.example.ironman.ui

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.android.example.ironman.App.App
import com.android.example.ironman.R
import com.android.example.ironman.adapter.ExpenseAdatper
import com.android.example.ironman.db.Expense
import com.android.example.ironman.ui.EditActivity.Companion.extraForIntent
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    val tag: String = "MainAct"

    companion object {
        var boxStore = App.app?.boxStore
        var expenseBox = boxStore?.boxFor(Expense::class.java)
    }

    private lateinit var adapter: ExpenseAdatper

    private lateinit var listOfExpenses: ArrayList<Expense>


    /** for posting authentication attempts back to UI thread  */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.rvList)
        fab.setOnClickListener { onFabClick() }



        listOfExpenses = ArrayList(expenseBox!!.all.toList())
        showingEmptyView()

        adapter = ExpenseAdatper(
                listOfExpenses,
                { position: Int -> onItemClick(position) }
        ) { position: Int -> onLongItemClick(position, recyclerView) }

        rvList.layoutManager = LinearLayoutManager(this)

        rvList.adapter = adapter



        swipeToDelete(listOfExpenses)


    }

    private fun refreshList() {
        adapter.updateExpenseListItems(expenseBox!!.all.toList())
        showingEmptyView()

    }

    private fun showingEmptyView() {


        when {
            listOfExpenses.isEmpty() -> {
                empty_view.visibility = View.VISIBLE
            }
            else -> empty_view.visibility = View.INVISIBLE
        }
    }

    private fun onFabClick() {
        startActivity(Intent(this@MainActivity, EditActivity::class.java))
    }

    private fun swipeToDelete(listOfExpenses: ArrayList<Expense>) {
        // Handling swipe to delete
        val simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //Remove swiped item from list and notify the RecyclerView

                val adapterPosition = viewHolder.adapterPosition
                val exp = listOfExpenses[adapterPosition]
                expenseBox!!.remove(exp)
                listOfExpenses.removeAt(adapterPosition)
                rvList.scrollToPosition(adapterPosition)
                adapter.notifyItemRemoved(adapterPosition)

                Snackbar.make(rvList, "1 item removed", Snackbar.LENGTH_SHORT)
                        .setAction("UNDO") {
                            expenseBox!!.put(exp)
                            listOfExpenses.add(adapterPosition, exp)
                            adapter.notifyItemInserted(adapterPosition)


                        }.show()









                refreshList()


            }

        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(rvList)
    }


    override fun onResume() {
        refreshList()


        super.onResume()


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

        i.putExtra(extraForIntent, adapter.getId(position))





        startActivity(i)


    }

    private fun onLongItemClick(position: Int, recyclerView: RecyclerView) {


    }


}



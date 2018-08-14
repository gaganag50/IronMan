package com.android.example.ironman.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.example.ironman.R
import com.android.example.ironman.dateMonth.DateTimePicker
import com.android.example.ironman.db.Expense
import com.android.example.ironman.ui.MainActivity.Companion.expenseBox
import kotlinx.android.synthetic.main.activity_edit.*
import java.text.DateFormat
import java.util.*

class EditActivity : AppCompatActivity() {
    private val TAG: String = "EditAct"
    var idOfIncomingExpense: Long = 0
    var categoryOfExpense = "General"
    private var mExpenseHasChanged: Boolean = false
    private val mTouchListener = View.OnTouchListener { _, _ ->
        mExpenseHasChanged = true
        false
    }

    companion object {
        val extraForIntent = "ID"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)



        val dateTimePicker = DateTimePicker(this@EditActivity, findViewById(R.id.btnAttendanceDate)
                , findViewById(R.id.btnTime), fragmentManager)

        val stringArray = resources.getStringArray(R.array.array_category_options)
        idOfIncomingExpense = intent.getLongExtra(extraForIntent, 0)
        if (idOfIncomingExpense == 0L) {
            title = getString(R.string.editor_activity_title_add_expense)
            btnAttendanceDate.text = DateFormat.getDateInstance().format(Date(System.currentTimeMillis()))
            btnTime.text = DateFormat.getTimeInstance().format(Date(System.currentTimeMillis()))
            invalidateOptionsMenu()
            setupSpinner(0)

        } else {


            title = getString(R.string.editor_activity_title_edit_expense)
            val exp = expenseBox?.get(idOfIncomingExpense)

            exp?.let {
                val initialMoney = it.money
                val initialCategory = it.catergory
                val initialdescription = it.description
                val initialdate = it.date
                val initialtime = it.time

                Log.d(TAG, """
                    initialMoney $initialMoney
                    initialCategory $initialCategory
                    initialdescription $initialdescription
                    initialdate $initialdate
                    initialtime $initialtime
                """.trimIndent())


                setupSpinner(stringArray.indexOf(initialCategory))


                etTotal.setText(initialMoney.toString())
                etNote.setText(initialdescription)
                btnTime.text = initialtime
                btnAttendanceDate.text = initialdate
            }


        }




        btnAttendanceDate.setOnClickListener { dateTimePicker.displayDate() }
        btnTime.setOnClickListener { dateTimePicker.displayTime() }



        etTotal.setOnTouchListener(mTouchListener)
        spinner_category.setOnTouchListener(mTouchListener)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        fabSave.setOnClickListener {
            saveExpense()
            finish()
        }
        tvAddCategory.setOnClickListener {
            val i = Intent(this@EditActivity, CategoryAddition::class.java)
            startActivity(i)

        }


    }


    private fun setupSpinner(category: Int = 1) {
//        val indexOf = stringArray.indexOf(category)

        val categorySpinnerAdapter =
                ArrayAdapter.createFromResource(this,
                        R.array.array_category_options, android.R.layout.simple_spinner_item)

        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        spinner_category.adapter = categorySpinnerAdapter


        spinner_category.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val selection = parent?.getItemAtPosition(position) as String

                if (!TextUtils.isEmpty(selection)) {


                    categoryOfExpense = selection

                }

            }
        }


        spinner_category.setSelection(category)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        menuInflater.inflate(R.menu.menu_edit, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                saveExpense()
                finish()
                return true
            }
            R.id.action_delete -> {
                showDeleteConfirmationDialog()
                return true
            }

            android.R.id.home -> {
                when {
                    !mExpenseHasChanged -> NavUtils.navigateUpFromSameTask(this)
                    else -> {
                        val discardButtonClickListener = DialogInterface.OnClickListener { _, _ ->
                            NavUtils.navigateUpFromSameTask(this@EditActivity)
                        }
                        showUnsavedChangesDialog(discardButtonClickListener)

                    }
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveExpense() {

        when (idOfIncomingExpense) {
            0L -> {


                val total = etTotal.text.toString().trim()
                val note = etNote.text.toString().trim()

                if (TextUtils.isEmpty(total) && categoryOfExpense == getString(R.string.cat_general)) {
                    Toast.makeText(this, "please enter total money", Toast.LENGTH_SHORT).show()

                    return
                }

                val time = btnTime.text.toString()
                val toString = btnAttendanceDate.text.toString()

//                StringUtils.isEmpty(String str)


                val expense = Expense(
                        money = total.toInt(),
                        catergory = categoryOfExpense,
                        description = note,
                        date = toString,
                        time = time
                )
                val addExpense = expenseBox?.put(expense)


                if (addExpense == -1L) {

                    Toast.makeText(this, getString(R.string.editor_insert_expense_failed),
                            Toast.LENGTH_SHORT).show()
                } else {

                    Toast.makeText(this, getString(R.string.editor_insert_expense_successful),
                            Toast.LENGTH_SHORT).show()
                }
            }
            else -> {


                val updatedExpense = expenseBox!!.get(idOfIncomingExpense)




                updatedExpense?.let {
                    it.money = etTotal.text.toString().toInt()
                    it.description = etNote.text.toString()
                    it.date = btnAttendanceDate.text.toString()
                    it.time = btnTime.text.toString()

                    val rowsAffected = expenseBox?.put(it)


                    if (rowsAffected == 0L) {
                        Toast.makeText(this, getString(R.string.editor_update_expense_failed),
                                Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, getString(R.string.editor_update_expense_successful),
                                Toast.LENGTH_SHORT).show()
                    }
                }


            }
        }


    }


    override fun onBackPressed() {
        when {
            !mExpenseHasChanged || etTotal.text.trim().toString().isEmpty() -> {
                super.onBackPressed()
                return
            }
            else -> {
                val discardButtonClickListener = DialogInterface
                        .OnClickListener { _, _ ->
                            finish()
                        }

                showUnsavedChangesDialog(discardButtonClickListener)


            }
        }


    }

    private fun showDeleteConfirmationDialog() {


        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.delete_dialog_msg)
        builder.setPositiveButton(R.string.delete) { _, _ ->
            val ID = idOfIncomingExpense
            deletingExpense(ID)
        }
        builder.setNegativeButton(R.string.cancel) { dialog, _ ->

            dialog?.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun deletingExpense(id: Long) {

        Log.d(TAG, ":idOfIncomingExpense $id ")

        expenseBox!!.remove(id)




        finish()
    }

    private fun showUnsavedChangesDialog(
            discardButtonClickListener: android.content.DialogInterface.OnClickListener) {


        val builder = android.app.AlertDialog.Builder(this)
        builder.setMessage(R.string.unsaved_changes_dialog_msg)
        builder.setPositiveButton(R.string.discard, discardButtonClickListener)
        builder.setNegativeButton(R.string.keep_editing) { dialog, _ ->

            dialog?.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }


}


package com.android.example.ironman

import android.app.AlertDialog
import android.content.DialogInterface
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.example.ironman.db.Expense
import com.android.example.ironman.db.MyDbHelper
import com.android.example.ironman.db.Table
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_main.*

class EditActivity : AppCompatActivity() {
    private val TAG = "EditAct"
    var mCurrentExpenseUri: Uri? = null
    var Category = "Others"



    private var mExpenseHasChanged: Boolean = false


    private val mTouchListener = View.OnTouchListener { view, motionEvent ->
        mExpenseHasChanged = true
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        val db : SQLiteDatabase? = MyDbHelper(this).writableDatabase

        Log.d(TAG, "" + db)

        val intent = intent
        mCurrentExpenseUri = intent.data


        if (mCurrentExpenseUri == null) {
            title = getString(R.string.editor_activity_title_add_expense)
            invalidateOptionsMenu()

        } else {

            Log.v(TAG, "activity is being called")

            title = getString(R.string.editor_activity_title_edit_expense)




        }


        etTotal.setOnTouchListener(mTouchListener)
        spinner_category.setOnTouchListener(mTouchListener)
        setupSpinner()

    }


    fun setupSpinner() {

        val categorySpinnerAdapter =
                ArrayAdapter.createFromResource(this,
                        R.array.array_category_options, android.R.layout.simple_spinner_item)

        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        spinner_category.setAdapter(categorySpinnerAdapter)

        spinner_category.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {


                val selection = parent.getItemAtPosition(position) as String
                if (!TextUtils.isEmpty(selection)) {
                    Category = selection
                }
            }


            override fun onNothingSelected(
                    parent: AdapterView<*>
            ) {
                Toast.makeText(this@EditActivity, "Nothing selected", Toast.LENGTH_SHORT).show()

            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        menuInflater.inflate(R.menu.menu_edit, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // User clicked on a menu option in the app bar overflow menu
        when (item.itemId) {
            R.id.action_save -> {
                empty_view.visibility = View.INVISIBLE
                saveExpense()
                finish()
                return true
            }
            R.id.action_delete -> {
                showDeleteConfirmationDialog()
                return true
            }

            android.R.id.home -> {

                if (!mExpenseHasChanged) {
                    NavUtils.navigateUpFromSameTask(this@EditActivity)
                    return true
                }


                val discardButtonClickListener = DialogInterface.OnClickListener { dialogInterface, i ->
                    NavUtils.navigateUpFromSameTask(this@EditActivity)
                }

                showUnsavedChangesDialog(discardButtonClickListener)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveExpense() {

        val db : SQLiteDatabase? = MyDbHelper(this).writableDatabase

        if (mCurrentExpenseUri == null) {


            Log.v(TAG, "add Expense ")

            val total = etTotal.getText().toString().trim()
            Log.d("MainAct", total)
            Log.v(TAG, "all the edittext values are stored in strings ")

            if (mCurrentExpenseUri == null &&
                    TextUtils.isEmpty(total) && Category == getString(R.string.cat_others)) {
                Log.d("MainAct", "empty")
                Toast.makeText(this@EditActivity, "nothing changed" , Toast.LENGTH_SHORT).show()

                return
            }

            val addExpense = Table.addExpense(db!!, Expense(
                    null, total.toInt(), Category
            ))


            if (addExpense == -1L) {
                Toast.makeText(this, getString(R.string.editor_insert_expense_failed),
                        Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_expense_successful),
                        Toast.LENGTH_SHORT).show()
            }
        } else {


            Log.v(TAG, "edit expense ")
            Log.v(TAG, "content values is being instanciated")


            val money = etTotal.getText().toString().trim({ it <= ' ' })


            Log.v(TAG, "all the edittext values are stored in strings ")

            val id = java.lang.Long.valueOf(mCurrentExpenseUri!!.getLastPathSegment())


            //getContentResolver().update(HabitTable.CONTENT_URI,values,HabitTable.ID+"=?",new String[] {String.valueOf(id)}); //id is the id of the row you wan to update
            val rowsAffected = Table.updateExpense(db!!, Expense(
                    id.toInt(), money.toInt(), Category
            ))

            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_expense_failed),
                        Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.editor_update_expense_successful),
                        Toast.LENGTH_SHORT).show()
            }

        }





    }

    override fun onBackPressed() {
        if (!mExpenseHasChanged) {
            super.onBackPressed()
            return
        }


        val discardButtonClickListener = DialogInterface
                .OnClickListener { dialogInterface, i ->
                    finish()
                }

        showUnsavedChangesDialog(discardButtonClickListener)
    }

    private fun showDeleteConfirmationDialog() {

        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.delete_dialog_msg)
        builder.setPositiveButton(R.string.delete) { dialog, id ->

        }
        builder.setNegativeButton(R.string.cancel) { dialog, id ->

            dialog?.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun showUnsavedChangesDialog(
            discardButtonClickListener: android.content.DialogInterface.OnClickListener) {

        val builder = android.app.AlertDialog.Builder(this)
        builder.setMessage(R.string.unsaved_changes_dialog_msg)
        builder.setPositiveButton(R.string.discard, discardButtonClickListener)
        builder.setNegativeButton(R.string.keep_editing) { dialog, id ->

            dialog?.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }
}


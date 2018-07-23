package com.android.example.ironman.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.net.Uri
import android.os.Build
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
import com.android.example.ironman.R
import com.android.example.ironman.date.DatePickerFragment
import com.android.example.ironman.db.Expense
import com.orm.SugarRecord
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.activity_edit.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class EditActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    val TAG: String = "EditAct"


    var mCurrentExpenseUri: Uri? = null
    var Category = "Others"


    private var mExpenseHasChanged: Boolean = false


    private val mTouchListener = View.OnTouchListener { view, motionEvent ->
        mExpenseHasChanged = true
        false
    }


    fun getMonthName(month: Int): String {
        when (month + 1) {
            1 -> return "Jan"

            2 -> return "Feb"

            3 -> return "Mar"

            4 -> return "Apr"

            5 -> return "May"

            6 -> return "Jun"

            7 -> return "Jul"

            8 -> return "Aug"

            9 -> return "Sep"

            10 -> return "Oct"

            11 -> return "Nov"

            12 -> return "Dec"
        }
        return ""
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val month = getMonthName(monthOfYear)
        val date = "$month $dayOfMonth , $year"
        btnAttendanceDate.setText(date)
    }

    private fun getMonthNumber(monthName: String): Int {
        val date = SimpleDateFormat("MMM").parse(monthName)
        val cal = Calendar.getInstance()
        cal.time = date
        val getMonth = cal.get(Calendar.MONTH)
        return getMonth
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)


        val intent = intent
        mCurrentExpenseUri = intent.data


        if (mCurrentExpenseUri == null) {
            title = getString(R.string.editor_activity_title_add_expense)
            invalidateOptionsMenu()

        } else {
            val id = java.lang.Long.valueOf(mCurrentExpenseUri?.getLastPathSegment())
            Log.d(TAG, ": " + id)

            title = getString(R.string.editor_activity_title_edit_expense)
            val updatedExpense = SugarRecord.findById(Expense::class.java, id + 1)
            Log.d(TAG, ": " + updatedExpense)

            val initialMoney = updatedExpense?.money.toString()
            val initialCategory = updatedExpense?.catergory
            if (initialCategory != null) {
                setupSpinner(initialCategory)
            }
            etTotal.setText(initialMoney)

        }


//        btnTime.setOnClickListener {
//            // Initialize a new TimePickerFragment
//            val newFragment = TimePickerFragment(btnTime)
//            // Show the time picker dialog
//            newFragment.show(fragmentManager, "Time Picker")
//        }


//        val format2 = DateFormat.getDateTimeInstance().format(Date(expense.time))
//        itemView.findViewById<TextView>(R.id.tvTime).text = format2


        btnAttendanceDate.text = DateFormat.getDateInstance().format(Date(System.currentTimeMillis()))
        btnTime.text = DateFormat.getTimeInstance().format(Date(System.currentTimeMillis()))


        btnAttendanceDate.setOnClickListener {


            displayDate()


        }

        etTotal.setOnTouchListener(mTouchListener)
        spinner_category.setOnTouchListener(mTouchListener)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        setupSpinner()

    }

    private fun displayDate() {

        val now = Calendar.getInstance()
        val selectedDate = btnAttendanceDate.text.toString()
        val monthString = selectedDate.substring(0, 3)
        val dateString: String?
        val yearString: String?
        when {
            selectedDate.length == 12 -> {
                dateString = selectedDate.substring(4, 5)
                yearString = selectedDate.substring(8, 12)
            }
            else -> {
                dateString = selectedDate.substring(4, 6)
                yearString = selectedDate.substring(9, 13)

            }
        }


        val monthNumber = getMonthNumber(monthString)
        val dpd = DatePickerDialog.newInstance(
                this@EditActivity,
                yearString.toInt(),
                monthNumber,
                dateString.toInt()
        )

        dpd.show(fragmentManager, "Datepickerdialog")
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dpd.accentColor = resources.getColor(R.color.accent_material_light, theme)
        }
        dpd.setOkText(getString(R.string.okTextDatePicker))

        dpd.setHighlightedDays(arrayOf(now))
        DatePickerFragment(btnAttendanceDate)

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

            }
        })
    }


    fun setupSpinner(initialCategory: String) {

        // todo set the initial value of the spinner to the string

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

                if (!mExpenseHasChanged) {
                    NavUtils.navigateUpFromSameTask(this)
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


        if (mCurrentExpenseUri == null) {


            val total = etTotal.text.toString().trim()
            val note = etNote.text.toString().trim()

            if (mCurrentExpenseUri == null &&
                    TextUtils.isEmpty(total) && Category == getString(R.string.cat_others)) {

                return
            }


            val expense = Expense(
                    money = total.toInt(),
                    category = Category,
                    note = note,
                    time = System.currentTimeMillis()
            )
            val addExpense = expense.save()


            if (addExpense == -1L) {
                Log.d(TAG, ": expense not saved")

                Toast.makeText(this, getString(R.string.editor_insert_expense_failed),
                        Toast.LENGTH_SHORT).show()
            } else {
                Log.d(TAG, ": expense saved")

                Toast.makeText(this, getString(R.string.editor_insert_expense_successful),
                        Toast.LENGTH_SHORT).show()
            }
        } else {
            val id = java.lang.Long.valueOf(mCurrentExpenseUri?.getLastPathSegment())
            Log.d(TAG, "id: $id")

            val updatedExpense = SugarRecord.findById(Expense::class.java, id)
            updatedExpense.money = etTotal.text.toString().toInt()
            val rowsAffected = updatedExpense.update()
            Log.d(TAG, ": $rowsAffected")
            Log.d(TAG, """
                : ${updatedExpense.money} ${updatedExpense.catergory} ${updatedExpense.time}
                """.trimIndent())

            if (rowsAffected == 0L) {
                Toast.makeText(this, getString(R.string.editor_update_expense_failed),
                        Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.editor_update_expense_successful),
                        Toast.LENGTH_SHORT).show()
            }

        }


    }

    override fun onBackPressed() {
        if (!mExpenseHasChanged || etTotal.text.trim().toString().isEmpty()) {
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

//    fun setTime() {
//
//        val date = System.currentTimeMillis()
//
//        val sdf = SimpleDateFormat(" h:mm a")
//        val dateString = sdf.format(date)
//        textview.setText(dateString)
//
//        // for the dialog box
//
//
//    }
//
//    fun setDate() {
//        val time = System.currentTimeMillis()
//        val sdf = SimpleDateFormat("dd/MM/yy")
//        val timeString = sdf.format(time)
//        textview.setText(timeString)
//
//    }
//
//    fun setDay() {
//        val day = System.currentTimeMillis()
//        val sdf = SimpleDateFormat("EEEE")
//        val dayString = sdf.format(day)
//        textView.setText(dayString)
//    }


}


package com.android.example.ironman.ui

import android.app.AlertDialog
import android.content.DialogInterface
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
import com.android.example.ironman.db.Expense
import com.orm.SugarRecord
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.activity_edit.*
import java.text.DateFormat
import java.text.DateFormatSymbols
import java.util.*


class EditActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private val TAG: String = "EditAct"
    private var idOfIncomingExpense: Long? = null
    var categoryOfExpense = "General"
    private var mExpenseHasChanged: Boolean = false
    private val mTouchListener = View.OnTouchListener { _, _ ->
        mExpenseHasChanged = true
        false
    }

    private fun getMonthName(month: Int): String {
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
        val date = "$month $dayOfMonth, $year"
        btnAttendanceDate.text = date
    }

    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {
        val hourAMPM = getHourAMPM(hourOfDay)
        val ampm = getAMPM(hourOfDay)
        val date: String?
        date = when {
            minute < 10 && second < 10 -> "$hourAMPM:0$minute:0$second $ampm"
            minute > 10 && second < 10 -> "$hourAMPM:$minute:0$second $ampm"
            minute < 10 && second > 10 -> "$hourAMPM:0$minute:$second $ampm"
            else -> "$hourAMPM:$minute:$second $ampm"
        }
        btnTime.text = date
    }

    private fun getAMPM(hour: Int): String {
        return if (hour > 11) "PM" else "AM"
    }

    private fun getHourAMPM(hour: Int): Int {
        var modifiedHour = if (hour > 11) hour - 12 else hour
        if (modifiedHour == 0) {
            modifiedHour = 12
        }
        return modifiedHour
    }


    private fun getMonthNumber(month: String): Int {
        val dfs = DateFormatSymbols()
        val months = dfs.shortMonths


        for (i in 0..11) {
            if (months[i].equals(month, ignoreCase = true)) {
                return i // month index is zero-based as usual in old JDK pre 8!
            }
        }

        return -1 // no match
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        Log.d(TAG, ": onCreate")

        val stringArray = resources.getStringArray(R.array.array_category_options)



        idOfIncomingExpense = intent.getLongExtra("ID", 0)


        if (idOfIncomingExpense == 0L) {
            title = getString(R.string.editor_activity_title_add_expense)
            btnAttendanceDate.text = DateFormat.getDateInstance().format(Date(System.currentTimeMillis()))
            btnTime.text = DateFormat.getTimeInstance().format(Date(System.currentTimeMillis()))
            invalidateOptionsMenu()
            setupSpinner(0)

        } else {


            val exp = SugarRecord.findById<Expense>(Expense::class.java, idOfIncomingExpense)




            title = getString(R.string.editor_activity_title_edit_expense)

            val initialMoney = exp.money
            val initialCategory = exp.catergory
            val initialdescription = exp.description
            val initialdate = exp.date
            val initialtime = exp.time


            Log.d(TAG, ": initialMoney $initialMoney")
            Log.d(TAG, ": initialCategory $initialCategory")
            Log.d(TAG, ": initialdescription $initialdescription")
            Log.d(TAG, ": initialdate $initialdate")
            Log.d(TAG, ": initialtime $initialtime")






            val index = stringArray.indexOf(initialCategory)

            if (initialCategory != null) {
                setupSpinner(index)
            }

            etTotal.setText(initialMoney.toString())
            etNote.setText(initialdescription)
            btnTime.text = initialtime
            btnAttendanceDate.text = initialdate

        }




        btnAttendanceDate.setOnClickListener { displayDate() }
        btnTime.setOnClickListener { displayTime() }



        etTotal.setOnTouchListener(mTouchListener)
        spinner_category.setOnTouchListener(mTouchListener)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)


    }

    private fun getExpenseById(id: Long?): Expense? {
        return SugarRecord.findById<Expense>(Expense::class.java, id?.plus(1L))
    }

    private fun displayTime() {
        val selectedTime = btnTime.text.toString()
        val hourString: String?
        val minString: String?
        val secString: String?
        val settingTimeInAMPM: String?

        val length = selectedTime.length

        Log.d(TAG, ": length = $length selectedTime= $selectedTime")

        when {
            selectedTime.length > 10 -> {
                hourString = selectedTime.substring(0, 2)
                minString = selectedTime.substring(3, 5)
                secString = selectedTime.substring(6, 8)
                settingTimeInAMPM = selectedTime.substring(9)

            }
            else -> {
                hourString = selectedTime.substring(0, 1)
                minString = selectedTime.substring(2, 4)
                secString = selectedTime.substring(5, 7)
                settingTimeInAMPM = selectedTime.substring(8)

            }
        }


        Log.d(TAG, ": $hourString")
        Log.d(TAG, ": $minString")
        Log.d(TAG, ": $secString")
        Log.d(TAG, ": $settingTimeInAMPM")

        var hourInt = hourString.toInt()
        if (settingTimeInAMPM == getString(R.string.timeInPM)) {
            hourInt += 12
        }


        val dpd = TimePickerDialog.newInstance(
                this@EditActivity,
                hourInt,
                minString.toInt(),
                secString.toInt(),
                true

        )
        dpd.show(fragmentManager, "Datepickerdialog")


    }

    private fun displayDate() {

        val now = Calendar.getInstance()
        val selectedDate = btnAttendanceDate.text.toString()
        Log.d(TAG, ": $selectedDate")

        val monthString = selectedDate.substring(0, 3)
        val dateString: String?
        val yearString: String?
        when {
            selectedDate.length == 11 -> {
                dateString = selectedDate.substring(4, 5)
                yearString = selectedDate.substring(7, 11)
            }
            else -> {
                dateString = selectedDate.substring(4, 6)
                yearString = selectedDate.substring(8, 12)

            }
        }


        val monthNumber = getMonthNumber(monthString)
        Log.d(TAG, ": ${selectedDate.length}")

        Log.d(TAG, ": $yearString")
        Log.d(TAG, ": $monthNumber")
        Log.d(TAG, ": $dateString")


        val dpd = DatePickerDialog.newInstance(
                this@EditActivity,
                yearString.toInt(),
                monthNumber,
                dateString.toInt()
        )

        dpd.show(fragmentManager, "Datepickerdialog")
        dpd.version = DatePickerDialog.Version.VERSION_2


        dpd.setOkText(getString(R.string.okTextDatePicker))

        dpd.highlightedDays = arrayOf(now)


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

                if (idOfIncomingExpense == null &&
                        TextUtils.isEmpty(total) && categoryOfExpense == getString(R.string.cat_others)) {

                    return
                }

                val time = btnTime.text.toString()
                val toString = btnAttendanceDate.text.toString()






                val expense = Expense(
                        money = total.toInt(),
                        category = categoryOfExpense,
                        note = note,
                        date = toString,
                        time = time
                )
                val addExpense = expense.save()


                if (addExpense == -1L) {

                    Toast.makeText(this, getString(R.string.editor_insert_expense_failed),
                            Toast.LENGTH_SHORT).show()
                } else {

                    Toast.makeText(this, getString(R.string.editor_insert_expense_successful),
                            Toast.LENGTH_SHORT).show()
                }
            }
            else -> {


                val updatedExpense = SugarRecord.findById<Expense>(Expense::class.java, idOfIncomingExpense)




                updatedExpense?.let {
                    it.money = etTotal.text.toString().toInt()
                    it.description = etNote.text.toString()
                    it.date = btnAttendanceDate.text.toString()
                    it.time = btnTime.text.toString()

                    val rowsAffected = it.update()

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

    private fun deletingExpense(id: Long?) {
        val expense = getExpenseById(id)
        expense?.delete()
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


    override fun onPause() {
        Log.d(TAG, ": onPause")

        super.onPause()
    }

    override fun onDestroy() {
        Log.d(TAG, ": onDestroy")

        super.onDestroy()
    }


}


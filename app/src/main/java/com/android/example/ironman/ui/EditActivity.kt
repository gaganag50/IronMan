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
import com.android.example.ironman.db.Expense
import com.orm.SugarRecord
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.activity_edit.*
import java.text.DateFormat
import java.text.DateFormatSymbols
import java.util.*


class EditActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    val TAG: String = "EditAct"
    var mCurrentExpenseUri: Uri? = null
    var Category = "General"
    private var mExpenseHasChanged: Boolean = false
    private val mTouchListener = View.OnTouchListener { view, motionEvent ->
        mExpenseHasChanged = true
        false
    }
//    val stringArray = resources.getStringArray(R.array.array_category_options)

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
        val date = "$month $dayOfMonth, $year"
        btnAttendanceDate.setText(date)
    }

    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {
        val hourAMPM = getHourAMPM(hourOfDay)
        val ampm = getAMPM(hourOfDay)
        val date: String?
        if (minute < 10 && second < 10)
            date = "$hourAMPM:0$minute:0$second $ampm"
        else if (minute > 10 && second < 10)
            date = "$hourAMPM:$minute:0$second $ampm"
        else if (minute < 10 && second > 10)
            date = "$hourAMPM:0$minute:$second $ampm"
        else
            date = "$hourAMPM:$minute:$second $ampm"
        btnTime.setText(date)
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


        val intent = intent
        mCurrentExpenseUri = intent.data


        if (mCurrentExpenseUri == null) {
            title = getString(R.string.editor_activity_title_add_expense)
            btnAttendanceDate.text = DateFormat.getDateInstance().format(Date(System.currentTimeMillis()))
            btnTime.text = DateFormat.getTimeInstance().format(Date(System.currentTimeMillis()))
            invalidateOptionsMenu()
            setupSpinner(Category)

        } else {
            val id = java.lang.Long.valueOf(mCurrentExpenseUri?.getLastPathSegment())
            Log.d(TAG, ": " + id)

            title = getString(R.string.editor_activity_title_edit_expense)
            val updatedExpense = SugarRecord.findById(Expense::class.java, id + 1)
            Log.d(TAG, ": " + updatedExpense)

            val initialMoney = updatedExpense?.money.toString()
            val initialCategory = updatedExpense?.catergory
            val initialdescription = updatedExpense?.description
            val initialdate = updatedExpense?.date
            val initialtime = updatedExpense?.time
            Log.d(TAG, ": initialMoney $initialMoney")
            Log.d(TAG, ": initialCategory $initialCategory")
            Log.d(TAG, ": initialdescription $initialdescription")
            Log.d(TAG, ": initialdate $initialdate")
            Log.d(TAG, ": initialtime $initialtime")




            if (initialCategory != null) {
                setupSpinner(initialCategory)
            }

            etTotal.setText(initialMoney)
            etNote.setText(initialdescription)
            btnTime.setText(initialtime)
            btnAttendanceDate.setText(initialdate)

        }




        btnAttendanceDate.setOnClickListener { displayDate() }
        btnTime.setOnClickListener { displayTime() }



        etTotal.setOnTouchListener(mTouchListener)
        spinner_category.setOnTouchListener(mTouchListener)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)


    }

    private fun displayTime() {
        Log.d(TAG, ": displayTime Called")

        val selectedTime = btnTime.text.toString()
        val hourString: String?
        val minString: String?
        val secString: String?
        val AMPM: String?

        val length = selectedTime.length
        Log.d(TAG, ": $length")

        Log.d(TAG, ": $selectedTime")

        if (selectedTime.length > 10) {
            hourString = selectedTime.substring(0, 2)
            minString = selectedTime.substring(3, 5)
            secString = selectedTime.substring(6, 8)
            AMPM = selectedTime.substring(9)

        } else {
            hourString = selectedTime.substring(0, 1)
            minString = selectedTime.substring(2, 4)
            secString = selectedTime.substring(5, 7)
            AMPM = selectedTime.substring(8)

        }


        Log.d(TAG, ": $hourString")
        Log.d(TAG, ": $minString")
        Log.d(TAG, ": $secString")
        Log.d(TAG, ": $AMPM")

        var hourInt = hourString.toInt()
        if (AMPM.equals("PM")) {
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
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dpd.accentColor = resources.getColor(R.color.accent_material_light, theme)
        }
        dpd.setOkText(getString(R.string.okTextDatePicker))

        dpd.setHighlightedDays(arrayOf(now))


    }


    fun setupSpinner(category: String) {
//        val indexOf = stringArray.indexOf(category)

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

//        spinner_category.setSelection(indexOf)
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

            val time = btnTime.text.toString()
            val toString = btnAttendanceDate.text.toString()


            Log.d(TAG, ": btnAttendanceDate $toString")


            Log.d(TAG, ": time $time")


            val expense = Expense(
                    money = total.toInt(),
                    category = Category,
                    note = note,
                    date = toString,
                    time = time
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

            val updatedExpense = SugarRecord.findById(Expense::class.java, id + 1)
            updatedExpense.money = etTotal.text.toString().toInt()
            updatedExpense.description = etNote.text.toString()
            updatedExpense.date = btnAttendanceDate.text.toString()
            updatedExpense.time = btnTime.text.toString()

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


}


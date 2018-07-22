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
import com.android.example.ironman.date.DateTime
import com.android.example.ironman.date.TimePickerFragment
import com.android.example.ironman.db.Expense
import com.orm.SugarRecord
import kotlinx.android.synthetic.main.activity_edit.*
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*


class EditActivity : AppCompatActivity() {
    var mCurrentExpenseUri: Uri? = null
    var Category = "Others"
    val TAG: String = "EditAct"


    private var mExpenseHasChanged: Boolean = false


    private val mTouchListener = View.OnTouchListener { view, motionEvent ->
        mExpenseHasChanged = true
        false
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

        val datetime = DateTime()
        datetime.setTime(btnTime)

        datetime.setDate(btnAttendanceDate)
        btnTime.setOnClickListener {
            // Initialize a new TimePickerFragment
            val newFragment = TimePickerFragment(btnTime)
            // Show the time picker dialog
            newFragment.show(fragmentManager, "Time Picker")
        }
        btnAttendanceDate.setOnClickListener {
            val newFragment2 = DatePickerFragment(btnAttendanceDate)
            newFragment2.show(fragmentManager, "Date Picker")

        }

        etTotal.setOnTouchListener(mTouchListener)
        spinner_category.setOnTouchListener(mTouchListener)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
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

            val cal = Calendar.getInstance()
            val time = cal.getTime()    // Sun Jul 22 18:22:05 GMT+05:30 2018
            Toast.makeText(this@EditActivity, "" + time, Toast.LENGTH_SHORT).show()
            Log.d(TAG, ": $time")


            val originalString = "2010-07-14 09:00:02"
            val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(originalString)
            val newString = SimpleDateFormat("H:mm").format(date) // 9:00


            val date2: Date = Date() // your date
            val cal2 = Calendar.getInstance()
            cal.time = date2
            val year2 = cal.get(Calendar.YEAR)
            val month2 = cal.get(Calendar.MONTH)
            val day2 = cal.get(Calendar.DAY_OF_MONTH)
            Log.d(TAG, ":  $cal2 \n $year2 \n $month2  \n $day2")
            // java.util.GregorianCalendar[
            // time=1532263925751,areFieldsSet=true,lenient=true,zone=Asia/Calcutta,firstDayOfWeek=1,minimalDaysInFirstWeek=1,
            // ERA=1,YEAR=2018,MONTH=6,WEEK_OF_YEAR=30,WEEK_OF_MONTH=4,DAY_OF_MONTH=22,DAY_OF_YEAR=203,DAY_OF_WEEK=1,
            // DAY_OF_WEEK_IN_MONTH=4,AM_PM=1,HOUR=6,HOUR_OF_DAY=18,MINUTE=22,SECOND=5,MILLISECOND=751,ZONE_OFFSET=19800000,
            // DST_OFFSET=0] 2010 6 14


            // cal -->
            // year2 --> 2018
            // month2 --> 6
            // day2 --> 22


            // better
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val date3 = Date()
                val localDate = date3.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

                val year = localDate.year
                val month = localDate.monthValue
                val day = localDate.dayOfMonth
                Log.d(TAG, ": $localDate $year $month $day")

            }
            val simpleDateFormat = SimpleDateFormat("EEEEEE")
            Log.d(TAG, ": $simpleDateFormat")

            val time1 = cal.time
            Log.d(TAG, "time: $time1")

            val ti = SimpleDateFormat("EEEEEE").format(cal.getTime())
            Log.d(TAG, ": $ti")

            Toast.makeText(this@EditActivity, "" + ti, Toast.LENGTH_SHORT).show()

            val expense = Expense(
                    money = total.toInt(),
                    category = Category,
                    note = note,
                    time = System.currentTimeMillis()
            )
            val addExpense = expense.save()


            if (addExpense == -1L) {
                Toast.makeText(this, getString(R.string.editor_insert_expense_failed),
                        Toast.LENGTH_SHORT).show()
            } else {
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


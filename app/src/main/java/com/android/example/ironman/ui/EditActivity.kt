package com.android.example.ironman.ui

import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.android.example.ironman.R
import com.android.example.ironman.dateMonth.DateTimePicker
import com.android.example.ironman.dateMonth.showColorPickerDialog
import com.android.example.ironman.db.Expense
import com.android.example.ironman.ui.MainActivity.Companion.expenseBox
import kotlinx.android.synthetic.main.activity_edit.*
import me.priyesh.chroma.ColorMode
import java.text.DateFormat
import java.util.*

class EditActivity : AppCompatActivity() {
    private val KEY_COLOR = "extra_color"
    private val KEY_COLOR_MODE = "extra_color_mode"

    private val TAG: String = "EditAct"
    var idOfIncomingExpense: Long = 0
    var categoryOfExpense = "General"
    private var mExpenseHasChanged: Boolean = false
    private val mTouchListener = View.OnTouchListener { _, _ ->
        mExpenseHasChanged = true
        false
    }
    var mColor: Int? = null
    lateinit var colorMode: ColorMode

    companion object {
        val extraForIntent = "ID"
        val militaryTime = true

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)


        mColor = savedInstanceState?.getInt(KEY_COLOR)
                ?: ContextCompat.getColor(this, R.color.colorPrimary)

        colorMode = when {
            savedInstanceState != null -> ColorMode.valueOf(savedInstanceState.getString(KEY_COLOR_MODE))
            else -> ColorMode.RGB
        }

        val btnTime = btnTime


        val dateTimePicker = DateTimePicker(

                context = this@EditActivity,
                btnAttendanceDate = findViewById(R.id.btnDate),
                btnTime = findViewById(R.id.btnTime),
                btnDay = findViewById<Button>(R.id.btnAttendanceDay),
                fm = fragmentManager,
                text = tvColorChangeDateTimePicker.text,
                btnAttendanceMonth = findViewById<Button>(R.id.btnAttendanceMonth),
                btnAttendanceYear = findViewById<Button>(R.id.btnYear),
                btnExclusiveDate = findViewById<Button>(R.id.btnExclusiveDate)
        )

        val stringArray = resources.getStringArray(R.array.array_category_options)
        idOfIncomingExpense = intent.getLongExtra(extraForIntent, 0)


        when (idOfIncomingExpense) {
            0L -> addingExpense(btnTime)
            else -> updatingExpense(stringArray, btnTime)
        }



        btnDate.setOnClickListener { dateTimePicker.displayDate() }
        btnTime.setOnClickListener { dateTimePicker.displayTime(militaryTime) }

        tvColorChangeDateTimePicker.setOnClickListener {


            showColorPickerDialog.showColorPickerDialog(
                    this, color_mode_spinner,
                    findViewById<TextView>(R.id.tvColorChangeDateTimePicker),
                    supportFragmentManager,
                    mColor!!, colorMode


            )

        }





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

    fun updatingExpense(stringArray: Array<out String>?, btnTime: Button) {
        title = getString(R.string.editor_activity_title_edit_expense)
        val exp = expenseBox?.get(idOfIncomingExpense)

        exp?.let {
            val initialMoney = it.money
            val initialCategory = it.catergory
            val initialdescription = it.description
            val initialdate = it.date
            val initialtime = it.time
            val initialday = it.day
            val month = it.month
            val initialexclusiveDate = it.exclusiveDate

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
            btnDate.text = initialdate
            btnAttendanceDay.text = initialday
            btnAttendanceMonth.text = month
            btnExclusiveDate.text = initialexclusiveDate
        }
    }

    fun addingExpense(btnTime: Button) {
        title = getString(R.string.editor_activity_title_add_expense)
        btnDate.text = DateFormat.getDateInstance().format(Date(System.currentTimeMillis()))

        if (militaryTime) {
            btnTime.text = displayTimeIn24Format()
        } else
            btnTime.text = displayTimeIn12Format()


        val date = btnDate.text.toString()
        val month = date.substring(0, 3)
        val year = date.substring(date.length - 4)
        btnAttendanceMonth.text = month
        btnYear.text = year

        Log.d(TAG, ": btnDate.text  ${btnDate.text}")
        Log.d(TAG, ": btnTime.text ${btnTime.text}")


        invalidateOptionsMenu()
        setupSpinner(0)
    }

    private fun displayTimeIn12Format(): String {
        val selectedTime = DateFormat.getTimeInstance().format(Date(System.currentTimeMillis()))
        var hour: Int?
        val min: Int?
        val sec: Int?
        val AMPM: String? = selectedTime.substring(selectedTime.length - 2)
        when {
            selectedTime.length > 10 -> {
                hour = selectedTime.substring(0, 2).toInt()
                min = selectedTime.substring(3, 5).toInt()
                sec = selectedTime.substring(6, 8).toInt()


            }
            else -> {
                hour = selectedTime.substring(0, 1).toInt()
                min = selectedTime.substring(2, 4).toInt()
                sec = selectedTime.substring(5, 7).toInt()

            }
        }
        val hourString = String(CharArray(2 - hour.toString().length)).replace('\u0000', '0') + hour
        val minString = String(CharArray(2 - min.toString().length)).replace('\u0000', '0') + min
        val secString = String(CharArray(2 - sec.toString().length)).replace('\u0000', '0') + sec
        Log.d(TAG, ": hourString $hourString")
        Log.d(TAG, ": minString $minString")
        Log.d(TAG, ": secString $secString")

        return "$hourString:$minString:$secString$AMPM"

    }


    fun displayTimeIn24Format(): String {
        val selectedTime = DateFormat.getTimeInstance().format(Date(System.currentTimeMillis()))
        var hour: Int?
        val min: Int?
        val sec: Int?
        val settingTimeInAMPM = selectedTime.substring(selectedTime.length - 2)

        val length = selectedTime.length

        Log.d(ContentValues.TAG, ": length = $length selectedTime= $selectedTime")

        when {
            selectedTime.length > 10 -> {
                hour = selectedTime.substring(0, 2).toInt()
                min = selectedTime.substring(3, 5).toInt()
                sec = selectedTime.substring(6, 8).toInt()


            }
            else -> {
                hour = selectedTime.substring(0, 1).toInt()
                min = selectedTime.substring(2, 4).toInt()
                sec = selectedTime.substring(5, 7).toInt()

            }
        }


        Log.d(ContentValues.TAG, ": $hour")
        Log.d(ContentValues.TAG, ": $min")
        Log.d(ContentValues.TAG, ": $sec")
        Log.d(ContentValues.TAG, ": $settingTimeInAMPM")


        if (settingTimeInAMPM == resources.getString(R.string.timeInPM) && hour < 12) {
            hour += 12
        } else if ((settingTimeInAMPM == resources.getString(R.string.timeInAM) && hour == 12))
            hour -= 12

        val hourString = String(CharArray(2 - hour.toString().length)).replace('\u0000', '0') + hour
        val minString = String(CharArray(2 - min.toString().length)).replace('\u0000', '0') + min
        val secString = String(CharArray(2 - sec.toString().length)).replace('\u0000', '0') + sec
        Log.d(TAG, ": hourString $hourString")
        Log.d(TAG, ": minString $minString")
        Log.d(TAG, ": secString $secString")


        val time = "$hourString:$minString:$secString"
        Log.d(TAG, ": time $time")
        return time


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
                val toString = btnDate.text.toString()


//                StringUtils.isEmpty(String str)


                val expense = Expense(
                        money = total.toInt(),
                        catergory = categoryOfExpense,
                        description = note,
                        date = toString,
                        day = btnAttendanceDay.text as String?,
                        month = btnAttendanceMonth.text.toString(),
                        exclusiveDate = btnExclusiveDate.text.toString(),
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
                    it.date = btnDate.text.toString()
                    it.day = btnAttendanceDay.text.toString()
                    it.month = btnAttendanceMonth.text.toString()
                    it.time = btnTime.text.toString()
                    it.month = btnAttendanceMonth.text.toString()
                    it.day = btnAttendanceDay.text.toString()
                    it.exclusiveDate = btnExclusiveDate.text.toString()


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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_COLOR, mColor!!)
        outState.putString(KEY_COLOR_MODE, color_mode_spinner.getSelectedItem().toString())
        super.onSaveInstanceState(outState)
    }
}


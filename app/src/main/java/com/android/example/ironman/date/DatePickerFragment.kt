package  com.android.example.ironman.date

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_edit.*
import java.text.DateFormat
import java.util.*

@SuppressLint("ValidFragment")
class DatePickerFragment(val textView: TextView) : DialogFragment(), DatePickerDialog.OnDateSetListener {



    private lateinit var calendar: Calendar
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)


        return DatePickerDialog(
                activity,

                android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,
                this,
                year,
                month,
                day
        )
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = "You picked the following date: " + dayOfMonth + "/" + (month + 1) + "/" + year
        btnAttendanceDate.setText(date)
    }


    private fun formatDate(year: Int, month: Int, day: Int): String {
        calendar.set(year, month, day, 0, 0, 0)
        val chosenDate = calendar.time

        val df = DateFormat.getDateInstance(DateFormat.SHORT)
        return df.format(chosenDate)
    }
}
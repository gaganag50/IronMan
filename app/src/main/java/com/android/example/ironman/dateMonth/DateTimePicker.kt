package com.android.example.ironman.dateMonth

import android.app.FragmentManager
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Button
import com.android.example.ironman.R
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.util.*

class DateTimePicker(
        val context: Context,
        val btnAttendanceDate: Button,
        val btnTime: Button,
        val fm: FragmentManager
) : DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val month = DateMonth.getMonthName(monthOfYear)
        val date = "$month $dayOfMonth, $year"
        btnAttendanceDate.text = date

    }

    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {
        val hourAMPM = DateMonth.getHourAMPM(hourOfDay)
        val ampm = DateMonth.getAMPM(hourOfDay)
        val date: String?
        date = when {
            minute < 10 && second < 10 -> "$hourAMPM:0$minute:0$second $ampm"
            minute > 10 && second < 10 -> "$hourAMPM:$minute:0$second $ampm"
            minute < 10 && second > 10 -> "$hourAMPM:0$minute:$second $ampm"
            else -> "$hourAMPM:$minute:$second $ampm"
        }
        btnTime.text = date
    }


    fun displayTime() {
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
        if (settingTimeInAMPM == context.getString(R.string.timeInPM)) {
            hourInt += 12
        }


        val dpd = TimePickerDialog.newInstance(
                this,
                hourInt,
                minString.toInt(),
                secString.toInt(),
                true

        )


        dpd.show(fm, "Datepickerdialog")


    }

    fun displayDate() {

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


        val monthNumber = DateMonth.getMonthNumber(monthString)
        Log.d(TAG, ": ${selectedDate.length}")

        Log.d(TAG, ": $yearString")
        Log.d(TAG, ": $monthNumber")
        Log.d(TAG, ": $dateString")


        val dpd = DatePickerDialog.newInstance(
                this,
                yearString.toInt(),
                monthNumber,
                dateString.toInt()
        )

        dpd.show(fm, "Datepickerdialog")
        dpd.version = DatePickerDialog.Version.VERSION_2


        dpd.setOkText(context.getString(R.string.okTextDatePicker))

        dpd.highlightedDays = arrayOf(now)


    }

}
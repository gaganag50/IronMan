package com.android.example.ironman.dateMonth

import android.app.FragmentManager
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Button
import com.android.example.ironman.R
import com.android.example.ironman.dateMonth.Time.Companion.timeConversion
import com.android.example.ironman.ui.EditActivity.Companion.militaryTime
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.text.SimpleDateFormat
import java.util.*

class Time(val hour: String, val min: String, val sec: String) {
    companion object {


        fun timeConversion(width: Int, hour: Int, fill: Char, min: Int, sec: Int): Time {
            val hourString = String(CharArray(width - hour.toString().length)).replace('\u0000', fill) + hour
            val minString = String(CharArray(width - min.toString().length)).replace('\u0000', fill) + min
            val secString = String(CharArray(width - sec.toString().length)).replace('\u0000', fill) + sec

            val time: Time = Time(hourString, minString, secString)
            return time
        }
    }

}

class DateTimePicker(
        val context: Context,
        val btnAttendanceDate: Button,
        val btnTime: Button,
        val btnDay: Button,
        val fm: FragmentManager,
        val text: CharSequence,
        val btnAttendanceMonth: Button,
        val btnAttendanceYear: Button,
        val btnExclusiveDate: Button

) : DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    val days = arrayOf("", "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY")


    fun displayDay(date: String): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
        c.time = df.parse(date)
        val day = days[c.get(Calendar.DAY_OF_WEEK)]
        return day
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val month = DateMonth.getMonthName(monthOfYear)
        val date = "$month $dayOfMonth, $year"
        btnAttendanceDate.text = date
        btnDay.text = displayDay(btnAttendanceDate.toString())
        btnAttendanceMonth.text = month
        btnAttendanceYear.text = year.toString()
        btnExclusiveDate.text = dayOfMonth.toString()


    }


    override fun onTimeSet(view: TimePickerDialog?, hour: Int, min: Int, sec: Int) {
        val date: String
        val width = 2
        val fill = '0'
        val hourAMPM: Int

        if (!militaryTime) {
            hourAMPM = DateMonth.getHourAMPM(hour)
            val ampm = DateMonth.getAMPM(hour)
            val timeIn12format = Time.timeConversion(2, hourAMPM, '0', min, sec)
            btnTime.text = "${timeIn12format.hour}:${timeIn12format.min}:${timeIn12format.sec}:$ampm"

        } else {

            val timeIn24format = Time.timeConversion(2, hour, '0', min, sec)
            btnTime.text = "${timeIn24format.hour}:${timeIn24format.min}:${timeIn24format.sec}"
        }






    }


    fun displayTime(militaryTime: Boolean) {
        val selectedTime = btnTime.text.toString()
        var hour = selectedTime.substring(0, 2).toInt()
        val min = selectedTime.substring(3, 5).toInt()
        val sec = selectedTime.substring(6, 8).toInt()




        Log.d(TAG, ": $hour")
        Log.d(TAG, ": $min")
        Log.d(TAG, ": $sec")


        if (!militaryTime && selectedTime.substring(9) == context.getString(R.string.timeInPM) && hour < 12)
            hour += 12
        else if (!militaryTime && selectedTime.substring(9) == context.getString(R.string.timeInAM) && hour == 12)
            hour -= 12


        val dpd = TimePickerDialog.newInstance(
                this,
                hour,
                min.toInt(),
                sec.toInt(),
                militaryTime

        )
//        dpd.setAccentColor(text.toString())

        dpd.show(fm, "Datepickerdialog")
        dpd.version = TimePickerDialog.Version.VERSION_2


    }

    fun displayDate() {

        val now = Calendar.getInstance()
        val selectedDate = btnAttendanceDate.text.toString()
        Log.d(TAG, ": $selectedDate")

        val monthString = selectedDate.substring(0, 3)
        val date: Int?
        val year = selectedDate.substring(selectedDate.length - 4).toInt()
        when {
            selectedDate.length == 11 -> {
                date = selectedDate.substring(4, 5).toInt()
            }
            else -> {
                date = selectedDate.substring(4, 6).toInt()

            }
        }


        val monthNumber = DateMonth.getMonthNumber(monthString)
        Log.d(TAG, ": ${selectedDate.length}")

        Log.d(TAG, ": year $year")
        Log.d(TAG, ": monthNumber $monthNumber")
        Log.d(TAG, ": date $date")


        val dpd = DatePickerDialog.newInstance(
                this,
                year,
                monthNumber,
                date
        )




        dpd.show(fm, "Datepickerdialog")
        dpd.version = DatePickerDialog.Version.VERSION_2


        dpd.setOkText(context.getString(R.string.okTextDatePicker))

        dpd.highlightedDays = arrayOf(now)


    }

}
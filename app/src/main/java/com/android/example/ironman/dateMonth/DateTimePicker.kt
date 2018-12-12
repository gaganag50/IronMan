package com.android.example.ironman.dateMonth

import android.app.FragmentManager
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Button
import com.android.example.ironman.R
import com.android.example.ironman.ui.EditActivity.Companion.Date
import com.android.example.ironman.ui.EditActivity.Companion.Day
import com.android.example.ironman.ui.EditActivity.Companion.Month
import com.android.example.ironman.ui.EditActivity.Companion.Year
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
        val fm: FragmentManager,
        val text: CharSequence

) : DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    companion object {


        val days = arrayOf("", "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY")
    }

    fun displayDay(date: String, year: Int, monthOfYear: Int, dayOfMonth: Int, month1: String): String {
        val c = Calendar.getInstance()

        Log.d(TAG, ": date $date")
        Log.d(TAG, ": year $year")
        Log.d(TAG, ": monthOfYear $monthOfYear")
        Log.d(TAG, ": dayOfMonth $dayOfMonth")
        Log.d(TAG, ": month1 $month1")

        val Month = monthOfYear + 1

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

        val df = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
        Log.d(TAG, ": date $date")
        Log.d(TAG, ": sdf $sdf")


        val dateString = "16/08/2018"
        c.time = sdf.parse(dayOfMonth.toString() + "/" + Month + "/" + year)
        val day = days[c.get(Calendar.DAY_OF_WEEK)]
        return day
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val month = DateMonth.getMonthName(monthOfYear)
        val date = "$month $dayOfMonth, $year"
        btnAttendanceDate.text = date

        Day = displayDay(btnAttendanceDate.text.toString(), year, monthOfYear, dayOfMonth, month)
        Log.d(TAG, ": btnAttendanceDate ${btnAttendanceDate.text}")
        Month = month
        Year = year.toString()
        Date = dayOfMonth.toString()


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




        Log.d(TAG, "hour : $hour")
        Log.d(TAG, "min : $min")
        Log.d(TAG, "sec : $sec")


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
        Log.d(TAG, "selectedDate : $selectedDate")

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
        Log.d(TAG, "{selectedDate.length}: ${selectedDate.length}")

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
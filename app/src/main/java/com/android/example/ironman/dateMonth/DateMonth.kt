package com.android.example.ironman.dateMonth

import java.text.DateFormatSymbols


class DateMonth {
    companion object {

        fun getAMPM(hour: Int): String {
            return if (hour > 11) "PM" else "AM"
        }

        fun getHourAMPM(hour: Int): Int {
            var modifiedHour = if (hour > 11) hour - 12 else hour
            if (modifiedHour == 0) {
                modifiedHour = 12
            }
            return modifiedHour
        }


        fun getMonthNumber(month: String): Int {
            val dfs = DateFormatSymbols()
            val months = dfs.shortMonths


            for (i in 0..11) {
                if (months[i].equals(month, ignoreCase = true)) {
                    return i // month index is zero-based as usual in old JDK pre 8!
                }
            }

            return -1 // no match
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


    }

}
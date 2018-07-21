package com.android.example.ironman

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        layout_country_currency.setOnClickListener {
            val title = "Choose a country"
            val countryList = resources.getStringArray(R.array.countries_names)
            val country = countryList.indexOf("India")
            showAlertDialog(title, countryList, country)

        }




        creation_category.setOnClickListener {
            val title = "Select date format"
            val dateList = resources.getStringArray(R.array.date_formats)
            val date = dateList.indexOf("DD/MM/YYYY")
            showAlertDialog(title, dateList, date)
        }

        selection_week.setOnClickListener {
            val title = "Set first day of week"
            val dayList = resources.getStringArray(R.array.days)
            showAlertDialog(title, dayList, 0)

        }

        selection_year.setOnClickListener {
            val title = "First month of the year"
            val dayList = resources.getStringArray(R.array.months)
            showAlertDialog(title, dayList, 0)


        }
//        actionBar.setDisplayHomeAsUpEnabled(true)

        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        NavUtils.navigateUpFromSameTask(this)
        return true
    }

    private fun showAlertDialog(title: String, list: Array<String>, checkedItem: Int) {
        // setup the alert builder
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)

        // add a radio button list

        builder.setSingleChoiceItems(list, checkedItem, DialogInterface.OnClickListener { dialog, which ->
            // user checked an item
        })

        // add OK and Cancel buttons
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            // user clicked OK
        })
        builder.setNegativeButton("Cancel", null)

        // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }


}

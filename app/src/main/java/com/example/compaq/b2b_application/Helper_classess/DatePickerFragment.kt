package com.example.compaq.b2b_application.Helper_classess

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    val date: String
        get() {

            val cal = GregorianCalendar.getInstance()

            cal.add(Calendar.DAY_OF_YEAR, +30)
            val seven_days = cal.time

            val df = SimpleDateFormat("dd-MM-yyyy")


            return df.format(seven_days)
        }

    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(activity, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user

    }

    private fun date(year: Int, month: Int, day: Int) {

        Log.e("DATE", year.toString())
        Log.e("month", month.toString())
        Log.e("day", day.toString())

    }


}

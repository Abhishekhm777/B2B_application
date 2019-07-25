package com.example.compaq.b2b_application.Helper_classess

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.NumberPicker
import android.R
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager


class Number_picker_dialogue(private val context: Context) : NumberPicker.OnValueChangeListener{
    override fun onValueChange(p0: NumberPicker?, p1: Int, p2: Int) {

    }
    lateinit var myDialogue: Dialog
    lateinit var set: Button
    lateinit var  np: NumberPicker

    fun showPicker() {
        myDialogue = Dialog(context)
        myDialogue.setContentView(com.example.compaq.b2b_application.R.layout.number_picker_layout)
        myDialogue.setCanceledOnTouchOutside(true)
        myDialogue.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        np = myDialogue.findViewById(com.example.compaq.b2b_application.R.id.numberPicker1) as NumberPicker
        np.maxValue = 100 // max value 100
        np.minValue = 1   // min value 0
        np.wrapSelectorWheel = true
        myDialogue.show()
        set=myDialogue.findViewById(com.example.compaq.b2b_application.R.id.set)
    }
}

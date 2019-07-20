package com.example.compaq.b2b_application.Helper_classess

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.widget.Button

import com.example.compaq.b2b_application.R

class Back_alert_class(private val context: Context) {
    lateinit var myDialogue: Dialog
   lateinit var yes: Button
   lateinit var cancel: Button

    fun showAlert() {
        myDialogue = Dialog(context)
        myDialogue.setContentView(R.layout.back_alert_dialog_layout)
        myDialogue.setCanceledOnTouchOutside(false)
        yes = myDialogue.findViewById(R.id.yes)
        cancel = myDialogue.findViewById(R.id.cancel)

        myDialogue.window!!.attributes.windowAnimations = R.style.DialogAnimation
        myDialogue.show()
        cancel.setOnClickListener { myDialogue.dismiss() }


    }

}

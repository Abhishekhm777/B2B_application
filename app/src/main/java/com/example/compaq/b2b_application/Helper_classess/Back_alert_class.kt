package com.example.compaq.b2b_application.Helper_classess

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import com.example.compaq.b2b_application.R

class Back_alert_class(private val context: Context) {
    lateinit var myDialogue: Dialog
    lateinit var yes: Button
    lateinit var cancel: Button
    lateinit var msg: TextView
    lateinit var image: ImageView

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

    fun shoDeletAlert() {
        myDialogue = Dialog(context)
        myDialogue.setContentView(R.layout.back_alert_dialog_layout)
        myDialogue.setCanceledOnTouchOutside(false)
        yes = myDialogue.findViewById(R.id.yes)
        cancel = myDialogue.findViewById(R.id.cancel)
        msg = myDialogue.findViewById(R.id.popup_textview)
        msg.setText("Do you want to remove this item ?")
        myDialogue.show()
    }

    fun showPreview(drawable : Drawable) {
        myDialogue = Dialog(context)
        myDialogue.setContentView(R.layout.image_preiview_layout)
        image = myDialogue.findViewById(R.id.image_view)
        image.setImageDrawable(drawable);
        cancel=myDialogue.findViewById(R.id.cancel);
        cancel.setOnClickListener { myDialogue.dismiss() }
        myDialogue.show()
    }
}

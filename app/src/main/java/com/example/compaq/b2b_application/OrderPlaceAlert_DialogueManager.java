package com.example.compaq.b2b_application;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class OrderPlaceAlert_DialogueManager {
    public void showAlertDialog(final Context context, String title, String message,
                                Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setCanceledOnTouchOutside(false);
        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        if(status != null)
            // Setting alert dialog icon
            alertDialog.setIcon(R.drawable.clapping);

        // Setting OK Button


        alertDialog.setButton("Shop More", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Intent i = new Intent(context, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context. startActivity(i);
                /*Intent intent=new Intent(context, MainActivity.class);

                context.startActivity(intent);*/

            }
        });

        // Showing Alert Message
        if(!((Activity) context).isFinishing())
        {
            alertDialog.show();
            //show dialog
        }

    }
}

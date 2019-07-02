package com.example.compaq.b2b_application;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogManager {

    public AlertDialog alertDialog;
    public void showAlertDialog(Context context, String title, String message,
                                String status) {
         alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        if(status.equalsIgnoreCase("fail")) {
            // Setting alert dialog icon

            alertDialog.setIcon(R.drawable.fail);
            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }

        if(status==null){
            alertDialog.setButton("SEND", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }

        if(status.equalsIgnoreCase("internet")){

            alertDialog.setIcon(R.drawable.no_internet);

           alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }

        if(status.equalsIgnoreCase("yes")){
            alertDialog.setIcon(R.drawable.yes);

            alertDialog.setButton("REFRESH", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });

        }
        // Showing Alert Message
        if(!((Activity) context).isFinishing())
        {

            alertDialog.show();

            //show dialog
        }



    }

}

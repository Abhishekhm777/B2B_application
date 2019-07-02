package com.example.compaq.b2b_application.Model;

import android.graphics.Bitmap;

public class Image_Add_model {
    Bitmap bitmap;
    public Image_Add_model(Bitmap bitmap){
        this.bitmap=bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}

package com.example.compaq.b2b_application.Model;

import android.graphics.Bitmap;

public class Update_product_model {
    String id;
    String main_id;
    Bitmap bitmap;
    String image_id,pname;
    public Update_product_model(String id,String main_id){
        this.id=id;
        this.main_id=main_id;
    }
    public Update_product_model(String id, String main_id, Bitmap bitmap){
        this.id=id;
        this.main_id=main_id;
        this.bitmap=bitmap;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMain_id() {
        return main_id;
    }

    public void setMain_id(String main_id) {
        this.main_id = main_id;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}

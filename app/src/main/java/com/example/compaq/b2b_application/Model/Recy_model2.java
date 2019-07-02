package com.example.compaq.b2b_application.Model;

import android.view.View;
import android.widget.Button;

public
class Recy_model2 {
    public Button addcart_button;
    private String sku;
    private String name;
    private String imageId;
    private String id;
    public
    Recy_model2( String id,String imageId, String sku,String name) {


        this.id=id;
        this.imageId=imageId;
        this.sku=sku;
        this.name=name;
    }

    public Button getAddcart_button() {
        return addcart_button;
    }

    public void setAddcart_button(Button addcart_button) {
        this.addcart_button = addcart_button;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addcart_buttonOnClick(View v, int adapterPosition) {
    }

    public void imageV(View view, int adapterPosition) {
    }
}

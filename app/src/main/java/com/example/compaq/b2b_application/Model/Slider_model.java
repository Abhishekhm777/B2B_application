package com.example.compaq.b2b_application.Model;

import android.widget.ImageView;

public class Slider_model {
    String image_url;
    String id;
    String main_id;
    public Slider_model(String image_url,String id,String main_id){
        this.image_url=image_url;
        this.id=id;
        this.main_id=main_id;

    }

    public String getMain_id() {
        return main_id;
    }

    public void setMain_id(String main_id) {
        this.main_id = main_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

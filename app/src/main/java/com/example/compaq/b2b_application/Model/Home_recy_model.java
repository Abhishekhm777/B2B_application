package com.example.compaq.b2b_application.Model;

public class Home_recy_model {
    private int imageid;
    private String name;

    public
    Home_recy_model(int imageid, String name) {
        this.imageid = imageid;
        this.name = name;
    }

    public
    int getImageid() {
        return imageid;
    }

    public
    void setImageid(int imageid) {
        this.imageid = imageid;
    }

    public
    String getName() {
        return name;
    }

    public
    void setName(String name) {
        this.name = name;
    }
}

package com.example.compaq.b2b_application.Model;

public class Inner_Recy_model {

    String key="";
    String values="";

    public
    Inner_Recy_model(String key, String values) {
        this.key=key;
        this.values=values;
    }

    public
    String getKey() {
        return key;
    }

    public
    void setKey(String key) {
        this.key = key;
    }

    public
    String getValues() {
        return values;
    }

    public
    void setValues(String values) {
        this.values = values;
    }
}

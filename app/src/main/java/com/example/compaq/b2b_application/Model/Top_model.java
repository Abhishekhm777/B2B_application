package com.example.compaq.b2b_application.Model;

import org.json.JSONObject;

public class Top_model {
    String name;
    Boolean aBoolean;
    JSONObject jsonObject;

    public Top_model(String name, Boolean aBoolean, JSONObject jsonObject){
        this.name=name;
        this.aBoolean=aBoolean;
        this.jsonObject=jsonObject;
    }
    public  Top_model  (String name){
        this.name=name;

    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(Boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}

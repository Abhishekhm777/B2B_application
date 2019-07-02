package com.example.compaq.b2b_application.Model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonModel {
    private String name;
    private List<JsonModel> children;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<JsonModel> getChildren() {
        return children;
    }
    public void setChildren(List<JsonModel> children) {
        this.children = children;
    }
    public String toString(){
        return "name: "+name + ", children: {"+children+"}";
    }
}

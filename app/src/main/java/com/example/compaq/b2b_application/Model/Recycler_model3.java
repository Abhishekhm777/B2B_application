package com.example.compaq.b2b_application.Model;

import java.util.ArrayList;

public class Recycler_model3 {
    private String headings;
    ArrayList<Inner_Recy_model> arrayList;
    public
    Recycler_model3(String headings) {

        this.headings = headings;

    }

    public
    String getHeadings() {
        return headings;
    }

    public
    void setHeadings(String headings) {
        this.headings = headings;
    }

    public
    ArrayList<Inner_Recy_model> getArrayList() {
        return arrayList;
    }

    public
    void setArrayList(ArrayList<Inner_Recy_model> arrayList) {
        this.arrayList = arrayList;
    }
}

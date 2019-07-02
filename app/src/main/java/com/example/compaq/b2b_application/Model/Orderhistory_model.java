package com.example.compaq.b2b_application.Model;

import java.util.ArrayList;

public class Orderhistory_model {

    private String orderno;
    private String orderdate;
    public ArrayList<Order_history_inner_model> arrayList;
    public Orderhistory_model(String orderno,String orderdate) {

        this.orderno=orderno;
        this.orderdate=orderdate;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public ArrayList<Order_history_inner_model> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Order_history_inner_model> arrayList) {
        this.arrayList = arrayList;
    }
}

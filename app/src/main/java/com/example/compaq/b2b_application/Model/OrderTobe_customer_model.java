package com.example.compaq.b2b_application.Model;

public class OrderTobe_customer_model {
    String name;
    String order_no;
    public OrderTobe_customer_model(String name, String order_no){

       this.name=name;
       this.order_no=order_no;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }
}

package com.example.compaq.b2b_application.Model;

public class Seller_order_model {

    private String order_no;
    private String order_type;
    private String date;
    private String qty;
    private String cust_name;
    private String cust_no;
    private String image_id;
    public  Seller_order_model(String order_no,String order_type,String date,String qty,String cust_name,String cust_no){
        this.order_no=order_no;
        this.order_type=order_type;
        this.date=date;
        this.qty=qty;
        this.cust_name=cust_name;
        this.cust_no=cust_no;

    }
    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getCust_no() {
        return cust_no;
    }

    public void setCust_no(String cust_no) {
        this.cust_no = cust_no;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }
}

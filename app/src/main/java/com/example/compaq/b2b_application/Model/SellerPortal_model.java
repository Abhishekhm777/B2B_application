package com.example.compaq.b2b_application.Model;

public class SellerPortal_model {
    private String imageid;
    private String name;
    private String seller_id;
    private String wholeseller_no;

    public
    SellerPortal_model(String imageid, String name,String seller_id,String wholeseller_no) {
        this.imageid = imageid;
        this.name = name;
        this.seller_id=seller_id;
        this.wholeseller_no=wholeseller_no;
    }

    public
    String getImageid() {
        return imageid;
    }

    public
    void setImageid(String imageid) {
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

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getWholeseller_no() {
        return wholeseller_no;
    }

    public void setWholeseller_no(String wholeseller_no) {
        this.wholeseller_no = wholeseller_no;
    }
}

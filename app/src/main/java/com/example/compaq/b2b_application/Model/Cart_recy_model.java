package com.example.compaq.b2b_application.Model;

import android.view.View;
import android.widget.Button;

public class Cart_recy_model {


    private String  name;
    private String img_url;
    private String weight;
    private String id;
    private String qty;
    private String descript;
    private int del_id;
    private String purity;
    private  String size;
    private String length;
    private String expected;
    private String seller_name;
    public Cart_recy_model(String name, String img_url, String weight, String id, String qty,int del_id,String descript,String purity,String size,String length,String seller_name,String expected){
        this.img_url=img_url;
        this.weight=weight;
        this.id=id;
        this.qty=qty;
        this.del_id=del_id;
        this.descript=descript;
        this.size=size;
        this.purity=purity;
        this.length=length;
        this.seller_name=seller_name;
        this.expected=expected;
        this.name=name;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getDel_id() {
        return del_id;
    }

    public void setDel_id(int del_id) {
        this.del_id = del_id;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getPurity() {
        return purity;
    }

    public void setPurity(String purity) {
        this.purity = purity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }
}

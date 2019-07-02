package com.example.compaq.b2b_application.Model;

public class Check_out_Recyclemodel {
    private String  name;
    private String img_url;
    private String weight;
    private String id;
    private String qty;
    private int del_id;
    private String desc;
    private String purity;
    private  String size;
    private String length;

    public Check_out_Recyclemodel(String name, String img_url, String weight, String id, String qty, int del_id, String desc,String purity,String size,String length) {
        this.name=name;
        this.img_url=img_url;
        this.weight=weight;
        this.id=id;
        this.qty=qty;
        this.del_id=del_id;
        this.desc=desc;
        this.purity=purity;
        this.size=size;
        this.length=length;
    }


    public String getQty() {
        return qty;
    }

    public void setQty(String seal) {
        this.qty = seal;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
}



package com.example.compaq.b2b_application.Model;

public class Offline_order_model {
    String name,img_url,sku,weight,size,purity,quantity;

    public Offline_order_model(String name, String img_url,String sku,String weight,String size,String purity,String quantity) {
        this.name = name;
        this.img_url = img_url;
        this.sku=sku;
        this.weight=weight;
        this.size=size;
        this.purity=purity;
        this.quantity=quantity;

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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPurity() {
        return purity;
    }

    public void setPurity(String purity) {
        this.purity = purity;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}

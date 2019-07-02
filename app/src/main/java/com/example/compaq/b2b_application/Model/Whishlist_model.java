package com.example.compaq.b2b_application.Model;

public class Whishlist_model {
    String name,href,price,sku,id;

    public Whishlist_model(String name, String href,String sku,String id) {
        this.name = name;
        this.href = href;
        this.sku=sku;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

}

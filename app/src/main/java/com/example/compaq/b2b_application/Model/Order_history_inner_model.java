package com.example.compaq.b2b_application.Model;

public class Order_history_inner_model {
    private String  id;
    private String product;
    private String purity;
    private String shipped_to;
    private String weight;
    private String qty;
    private String status;
    private String image_url;
    private String delivered;
    private  String seller;
    private  String ordertype;
    public Order_history_inner_model(String id, String product, String purity, String shipped_to, String weight,String delivered, String qty, String status,String seller,String ordertype) {
        this.id=id;
        this.product=product;
        this.purity=purity;
        this.shipped_to=shipped_to;
        this.weight=weight;
        this.delivered=delivered;
        this.qty=qty;
        this.status=status;
        this.image_url=image_url;
        this.seller=seller;
        this.ordertype=ordertype;

    }

    public String getItem_name() {
        return id;
    }

    public void setItem_name(String item_name) {
        this.id = item_name;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getShipped_to() {
        return shipped_to;
    }

    public void setShipped_to(String shipped_to) {
        this.shipped_to = shipped_to;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPurity() {
        return purity;
    }

    public void setPurity(String purity) {
        this.purity = purity;
    }

    public String getDelivered() {
        return delivered;
    }

    public void setDelivered(String delivered) {
        this.delivered = delivered;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }
}

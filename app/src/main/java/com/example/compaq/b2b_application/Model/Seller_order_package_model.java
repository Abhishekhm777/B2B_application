package com.example.compaq.b2b_application.Model;

public class Seller_order_package_model {
    String product,qty,item_status,image_url,item_no,lenght,size,purity,weight,id, taskid,expected,product_id,pay_status ,cust_mobile;
    public Seller_order_package_model(String product, String qty, String item_status, String image_url, String item_no, String lenght, String size, String purity, String weight, String id, String taskid,String expected,String product_id,String pay_status,String cust_mobile){
        this.product=product;
        this.qty=qty;
        this.item_status=item_status;
        this.image_url=image_url;
        this.item_no=item_no;
        this.lenght=lenght;
        this.size=size;
        this.weight=weight;
        this.purity=purity;
        this.id=id;
        this.taskid=taskid;
        this.expected=expected;
        this.product_id=product_id;
        this.pay_status=pay_status;
        this.cust_mobile=cust_mobile;
    }

    public String getCust_mobile() {
        return cust_mobile;
    }

    public void setCust_mobile(String cust_mobile) {
        this.cust_mobile = cust_mobile;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getItem_status() {
        return item_status;
    }

    public void setItem_status(String item_status) {
        this.item_status = item_status;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getItem_no() {
        return item_no;
    }

    public void setItem_no(String item_no) {
        this.item_no = item_no;
    }

    public String getLenght() {
        return lenght;
    }

    public void setLenght(String lenght) {
        this.lenght = lenght;
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }
}


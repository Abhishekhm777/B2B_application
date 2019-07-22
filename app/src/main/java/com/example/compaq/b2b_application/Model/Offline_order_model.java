package com.example.compaq.b2b_application.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Offline_order_model implements Parcelable {
    String name,img_url,sku,weight,size,purity,quantity;
    String total_weight;
    String product_id;
    public Offline_order_model(String name, String img_url,String sku,String weight,String size,String purity,String quantity,String product_id,String total_weight) {
        this.name = name;
        this.img_url = img_url;
        this.sku=sku;
        this.weight=weight;
        this.size=size;
        this.purity=purity;
        this.quantity=quantity;
        this.product_id=product_id;
        this.total_weight=total_weight;

    }

    protected Offline_order_model(Parcel in) {
        name = in.readString();
        img_url = in.readString();
        sku = in.readString();
        weight = in.readString();
        size = in.readString();
        purity = in.readString();
        quantity = in.readString();
        total_weight=in.readString();
    }

    public static final Creator<Offline_order_model> CREATOR = new Creator<Offline_order_model>() {
        @Override
        public Offline_order_model createFromParcel(Parcel in) {
            return new Offline_order_model(in);
        }

        @Override
        public Offline_order_model[] newArray(int size) {
            return new Offline_order_model[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    public String getTotal_weight() {
        return total_weight;
    }

    public void setTotal_weight(String total_weight) {
        this.total_weight = total_weight;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public static Creator<Offline_order_model> getCREATOR() {
        return CREATOR;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(img_url);
        parcel.writeString(sku);
        parcel.writeString(weight);
        parcel.writeString(size);
        parcel.writeString(purity);
        parcel.writeString(quantity);
        parcel.writeString(total_weight);
    }


}

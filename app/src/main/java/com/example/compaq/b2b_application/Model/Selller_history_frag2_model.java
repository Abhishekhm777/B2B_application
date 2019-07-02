package com.example.compaq.b2b_application.Model;

import java.util.HashMap;

public class Selller_history_frag2_model {
    private String name;
    private String item_no;
    private String image_id;
    private String item_stat;
    private String qty;
    private String desc;
    private String puirty;
    private HashMap<String, String> images;




    public
    Selller_history_frag2_model(String name, String item_no,String image_id,String item_stat,String qty,String desc,String puirty) {
        this.name = name;
        this.item_no = item_no;
        this.image_id=image_id;
        this.item_stat=item_stat;
        this.qty=qty;
        this.desc=desc;
        this.puirty=puirty;
    }


    public String getPuirty() {
        return puirty;
    }

    public void setPuirty(String puirty) {
        this.puirty = puirty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItem_no() {
        return item_no;
    }

    public void setItem_no(String item_no) {
        this.item_no = item_no;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getItem_stat() {
        return item_stat;
    }

    public void setItem_stat(String item_stat) {
        this.item_stat = item_stat;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}

package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.compaq.b2b_application.Model.Offline_order_model;
import com.example.compaq.b2b_application.Model.OrderTobe_customer_model;
import com.example.compaq.b2b_application.Model.Seller_order_history;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Offline_order_Adapter extends RecyclerView.Adapter<Offline_order_Adapter.MyViewHolder> {
    private ArrayList<Offline_order_model> productlist;
    Context mContext;
    public Bundle bundle;
    private FragmentTransaction fragmentTransaction;
    public FragmentManager fragmentManager;
    private HashMap<String, OrderTobe_customer_model> details;
    public Offline_order_Adapter(Context applicationContext, ArrayList<Offline_order_model> productlist) {
        this.mContext=applicationContext;
        this.productlist=productlist;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.offline_order_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final com.example.compaq.b2b_application.Model.Offline_order_model listner=  productlist.get(position);
        holder.pr_name.setText("AHOOOO");

    }

    @Override
    public int getItemCount() {
        return productlist.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
       ImageView product_image;
       TextView pr_name,pro_sku,pro_size,pro_gwt,pro_qty,total_weight;

        public MyViewHolder(View itemView) {
            super(itemView);

            pr_name=(TextView)itemView.findViewById(R.id.name);
            pro_sku=(TextView)itemView.findViewById(R.id.sku);
            pro_size=(TextView)itemView.findViewById(R.id.size_length);
            pro_gwt=(TextView)itemView.findViewById(R.id.g_wt_gms_);
            pro_qty=(TextView)itemView.findViewById(R.id.qty);
            total_weight=(TextView)itemView.findViewById(R.id.total_g_wt_);
            product_image=(ImageView) itemView.findViewById(R.id.image);
        }
    }
}

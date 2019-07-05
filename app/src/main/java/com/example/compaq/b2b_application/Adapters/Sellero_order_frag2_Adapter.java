package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Model.Recy_model2;
import com.example.compaq.b2b_application.Model.Selller_history_frag2_model;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Sellero_order_frag2_Adapter extends RecyclerView.Adapter<Sellero_order_frag2_Adapter.MyViewHolder> {
    public FragmentActivity mCtx;
    private ArrayList<Selller_history_frag2_model> productlist;
    private CardView cardView;
    private View view;
    public FragmentManager fragmentManager;
    public FragmentTransaction fragmentTransaction;
    public ImageView imageV;
    public Recy_model2 recyModel2;
    public  Recy_model2 onClickListener;
    public Bundle bundle;
    public Context mContext;
    public SharedPreferences sharedPref;
  private HashMap<String, String> images;


    public Sellero_order_frag2_Adapter(FragmentActivity mCtx, ArrayList<Selller_history_frag2_model> productlist, HashMap<String, String> images) {
        this.productlist=productlist;
        this.mCtx=mCtx;
        this.images=images;

    }
    @NonNull
    @Override
    public Sellero_order_frag2_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mCtx).inflate(R.layout.seller_order_hist_frag2_card, parent, false);

        return new Sellero_order_frag2_Adapter.MyViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(@NonNull Sellero_order_frag2_Adapter.MyViewHolder holder, int position) {
        Selller_history_frag2_model listnet=(Selller_history_frag2_model)productlist.get(position);
        holder.oder_no.setText(listnet.getItem_no());
        holder.name.setText(listnet.getName());
        holder.item_status.setText(listnet.getItem_stat());
        holder.qty.setText(listnet.getQty());
        holder.dec.setText(listnet.getDesc());
        holder.puri.setText(listnet.getPuirty());
         String url=images.get(listnet.getName());
        Glide.with(mCtx).load(url).into(holder.imageV);


    }

    @Override
    public int getItemCount() {
        return productlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageV;
        TextView name,oder_no,item_status,qty,dec,puri;
        DisplayMetrics dm = mCtx.getResources().getDisplayMetrics();  //set width of the card
        int width = dm.widthPixels/1;
        CardView card;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageV=itemView.findViewById(R.id.imageview);
            oder_no=itemView.findViewById(R.id.check_orderno);
            name=itemView.findViewById(R.id.check_produc_name);
            item_status=itemView.findViewById(R.id.check_weight);
            qty=itemView.findViewById(R.id.check_quantity);
            puri=itemView.findViewById(R.id.purity);
            dec=itemView.findViewById(R.id.desc);


            card=(CardView)itemView.findViewById(R.id.card);
            card.getLayoutParams().width=width;
            card.requestLayout();
        }
    }
}

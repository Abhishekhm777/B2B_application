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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Helper_classess.DatePickerFragment;
import com.example.compaq.b2b_application.Model.Cart_recy_model;
import com.example.compaq.b2b_application.Model.Check_out_Recyclemodel;
import com.example.compaq.b2b_application.Model.Recy_model2;
import com.example.compaq.b2b_application.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Checkout_adapter  extends RecyclerView.Adapter<Checkout_adapter.MyViewHolder> {
    public FragmentActivity mCtx;
    private ArrayList<Check_out_Recyclemodel> productlist;
    private CardView cardView;
    private View view;
    public FragmentManager fragmentManager;
    public FragmentTransaction fragmentTransaction;
    public ImageView imageV;
    public Recy_model2 recyModel2;
    public  Recy_model2 onClickListener;
    public Bundle bundle;
    public Context mContext;
    public SharedPreferences pref;
    DatePickerFragment datePickerFragment=new DatePickerFragment();
    public Checkout_adapter(Context mContext, ArrayList<Check_out_Recyclemodel> productlist) {
        this.productlist=productlist;
        this.mContext=mContext;

    }

    @NonNull
    @Override
    public Checkout_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_cardlayout, parent, false);

            return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull Checkout_adapter.MyViewHolder holder, int position) {
        final com.example.compaq.b2b_application.Model.Check_out_Recyclemodel listner=  productlist.get(position);

        holder.d_qty.setText(listner.getQty());
        try {
            String url = listner.getImg_url();
            Glide.with(mContext).load(url).into(holder.imageV);
            Double total_weight = Double.parseDouble(listner.getQty()) * Double.parseDouble(listner.getWeight());
            String set_total=new DecimalFormat("#0.000").format(total_weight);
            holder.total_weight.setText(set_total);

              holder.date.setText(datePickerFragment.getDate());

        }
        catch (Exception e){
            e.printStackTrace();
        }
            try {
                String gross = new DecimalFormat("#0.000").format(Double.parseDouble(listner.getWeight()));
                holder.d_gweight.setText(gross);
                holder.d_product.setText(listner.getName());


            }
            catch (NumberFormatException e){
                e.printStackTrace();
            }
                }

    @Override
    public int getItemCount() {
        return productlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageV;
        TextView d_qty, pname,d_product,d_gweight,desc,total_weight,date,size,length;
        public MyViewHolder(View itemView){
            super(itemView);

            date=(TextView)itemView.findViewById(R.id.date);

            d_gweight=(TextView)itemView.findViewById(R.id.check_grpss_weight);

            d_product=(TextView)itemView.findViewById(R.id.check_produc_name);
            d_qty = (TextView) itemView.findViewById(R.id.check_quantity);
            imageV=(ImageView)itemView.findViewById(R.id.checkout_image);
            total_weight=(TextView) itemView.findViewById(R.id.totl_weight);

        }

    }
}

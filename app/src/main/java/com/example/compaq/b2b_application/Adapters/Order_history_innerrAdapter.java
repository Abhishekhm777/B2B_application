package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Main2Activity;
import com.example.compaq.b2b_application.Model.Inner_Recy_model;
import com.example.compaq.b2b_application.Model.Order_history_inner_model;
import com.example.compaq.b2b_application.Model.Orderhistory_model;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;

public class Order_history_innerrAdapter extends RecyclerView.Adapter<Order_history_innerrAdapter.ListnerViewHolder>{
    public ArrayList<Order_history_inner_model> details_list;

    private View view;
Context mContext;
    public
    Order_history_innerrAdapter(Context mContext, ArrayList<Order_history_inner_model> details_list) {
        this.mContext=mContext;
        this.details_list=details_list;
    }
    @NonNull
    @Override
    public Order_history_innerrAdapter.ListnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        view=inflater.inflate(R.layout.order_history_inner_cardlayout,parent,false);
        Order_history_innerrAdapter.ListnerViewHolder holder=new Order_history_innerrAdapter.ListnerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Order_history_innerrAdapter.ListnerViewHolder holder, int position) {
        Order_history_inner_model inner_recy_listner=(Order_history_inner_model)details_list.get(position);



        holder.item_name.setText(inner_recy_listner.getItem_name());
        holder.product.setText(inner_recy_listner.getProduct());

        holder.shipped_to.setText(inner_recy_listner.getShipped_to());
        holder.weight.setText(inner_recy_listner.getWeight());
        holder.qty.setText(inner_recy_listner.getQty());
        holder.status.setText(inner_recy_listner.getStatus());
        String url=  inner_recy_listner.getImage_url();
        Glide.with(mContext).load(url).into(holder.imageV);
    }

    @Override
    public int getItemCount() {
        return details_list.size();
    }

    public class ListnerViewHolder extends RecyclerView.ViewHolder {
        TextView item_name, product,sold_by,shipped_to,weight,qty,status;
        ImageView imageV;
        public ListnerViewHolder(View itemView) {
            super(itemView);
            item_name=(TextView)itemView.findViewById(R.id.set_itemname);
            product=(TextView)itemView.findViewById(R.id.set_product);
            sold_by =(TextView)itemView.findViewById(R.id.set_seller);
            shipped_to=(TextView)itemView.findViewById(R.id.set_shippedto);
            weight = (TextView) itemView.findViewById(R.id.set_itemwieght);
            qty=(TextView)itemView.findViewById(R.id.set_qty);
            status=(TextView)itemView.findViewById(R.id.set_status);
            imageV=itemView.findViewById(R.id.order_history_image);
        }
    }
}

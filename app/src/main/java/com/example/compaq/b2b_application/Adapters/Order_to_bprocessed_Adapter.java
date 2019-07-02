package com.example.compaq.b2b_application.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.compaq.b2b_application.Fragments.AddNewAddess;
import com.example.compaq.b2b_application.Fragments.Company_info_fragment;
import com.example.compaq.b2b_application.Fragments.Seller_order_package;
import com.example.compaq.b2b_application.Model.OrderTobe_customer_model;
import com.example.compaq.b2b_application.Model.SellerPortal_model;
import com.example.compaq.b2b_application.Model.Seller_order_history;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.Seller_Order_History;

import java.util.ArrayList;
import java.util.HashMap;

public class Order_to_bprocessed_Adapter extends RecyclerView.Adapter<Order_to_bprocessed_Adapter.MyViewHolder> implements Filterable {
    private ArrayList<Seller_order_history> productlist;
    private ArrayList<Seller_order_history> mFilteredList;
    Context mContext;
    public  Bundle bundle;
    private FragmentTransaction fragmentTransaction;
    public FragmentManager fragmentManager;
    private HashMap<String, OrderTobe_customer_model> details;
    public Order_to_bprocessed_Adapter(Context applicationContext, ArrayList<Seller_order_history> productlist, HashMap<String, OrderTobe_customer_model> details) {
        this.mContext=applicationContext;
        this.productlist=productlist;
        this.details=details;
       this. mFilteredList=productlist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_orderhis_card_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull Order_to_bprocessed_Adapter.MyViewHolder holder, int position) {
        final com.example.compaq.b2b_application.Model.Seller_order_history listner=  mFilteredList.get(position);
        final  OrderTobe_customer_model d_listner = details.get(listner.getOrder_no());
        holder.order_no.setText(listner.getOrder_no());
        /*holder.order_type.setText(listner.getOrder_type());*/
        if(listner.getOrder_type().equalsIgnoreCase("OFFLINE")){
            holder.customer_name.setText(listner.getCust_name());
            holder.customer_no.setText(listner.getCust_no());
            holder.order_type.setText(listner.getOrder_type());

        }
        else{
            try {

                holder.customer_name.setText(d_listner.getName());
                holder.customer_no.setText(d_listner.getOrder_no());
            }catch (Exception e){

            }
        }
     holder.quantity.setText(listner.getItems());
        holder.order_date.setText(listner.getDate());
            holder.order_type.setText(listner.getOrder_type());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!listner.getOrder_type().equalsIgnoreCase("OFFLINE")) {
                    try {
                        Seller_order_package myFragment = new Seller_order_package();
                        bundle = new Bundle();
                        bundle.putString("order_no", listner.getOrder_no());
                        bundle.putString("task_id", listner.getTask_id());
                        bundle.putString("cust_mobile", d_listner.getOrder_no());

                        myFragment.setArguments(bundle);
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        fragmentManager = activity.getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_layout, myFragment).addToBackStack(null);
                        fragmentTransaction.commit();
                    }catch (Exception e){
                       e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {


                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    mFilteredList = productlist;

                } else {

                    ArrayList<Seller_order_history> filteredList = new ArrayList<>();
                    ArrayList<OrderTobe_customer_model> filteredList1 = new ArrayList<>();

                    for (Seller_order_history androidVersion: productlist) {


                        if (androidVersion.getOrder_no().contains(charString.toString())) {


                            filteredList.add(androidVersion);

                        }
                        if(androidVersion.getCust_name().contains(charString)){
                            filteredList.add(androidVersion);
                        }
                    }

                    mFilteredList = filteredList;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Seller_order_history>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView order_no,order_date,order_type,quantity,customer_name,customer_no;
        CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);
            order_no=(TextView)itemView.findViewById(R.id.order_no);
            order_date=(TextView)itemView.findViewById(R.id.order_date);
            order_type=(TextView)itemView.findViewById(R.id.order_type);
            quantity=(TextView)itemView.findViewById(R.id.item_qty);
            customer_name=(TextView)itemView.findViewById(R.id.customer);
            customer_no=(TextView)itemView.findViewById(R.id.number);
            cardView=(CardView)itemView.findViewById(R.id.card_lay);
        }
    }
}

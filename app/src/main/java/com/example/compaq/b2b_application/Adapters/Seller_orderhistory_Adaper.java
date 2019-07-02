package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.example.compaq.b2b_application.Fragments.Seller_Orer_His_frag2;
import com.example.compaq.b2b_application.Fragments.Seller_order_package;
import com.example.compaq.b2b_application.Model.OrderTobe_customer_model;
import com.example.compaq.b2b_application.Model.Seller_order_model;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Seller_orderhistory_Adaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Seller_order_model> productlist;
    private Context mContext;
   private HashMap<String, OrderTobe_customer_model> details;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean     isLoadingAdded = false;
    public Bundle bundle;
    private FragmentTransaction fragmentTransaction;
    public FragmentManager fragmentManager;

    public Seller_orderhistory_Adaper(Context mContext, ArrayList<Seller_order_model> productlist, HashMap<String, OrderTobe_customer_model> details) {
        this.productlist=productlist;
        this.mContext=mContext;
        this.details=details;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(RecyclerView. ViewHolder holder, int position) {
        final com.example.compaq.b2b_application.Model.Seller_order_model listner=  productlist.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                MyViewHolder movieVH = (MyViewHolder) holder;
                if(listner.getOrder_type().equalsIgnoreCase("OFFLINE")){
                    movieVH.customer_name.setText(listner.getCust_name());
                    movieVH.customer_no.setText(listner.getCust_no());
                }
                else{

                    OrderTobe_customer_model d_listner=details.get(listner.getOrder_no());
                       if(d_listner!=null) {
                           movieVH.customer_name.setText(d_listner.getName());
                           movieVH.customer_no.setText(d_listner.getOrder_no());
                       }

                }
                movieVH.order_date.setText(listner.getDate());
                movieVH.order_no.setText(listner.getOrder_no());
                movieVH.order_type.setText(listner.getOrder_type());
                movieVH.quantity.setText(listner.getQty());
                movieVH.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Seller_Orer_His_frag2 myFragment = new Seller_Orer_His_frag2();
                        bundle = new Bundle();
                        bundle.putString("order_no", listner.getOrder_no());
                        myFragment.setArguments(bundle);
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        fragmentManager = activity.getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_layout, myFragment).addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });


                break;
            case LOADING:
//                Do nothing
                break;
        }

    }

    @NonNull
    private ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
       RecyclerView. ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.seller_orderhis_card_layout, parent, false);

        viewHolder = new MyViewHolder(v1);
        return viewHolder;
    }


    @Override
    public int getItemCount() {
        return productlist == null ? 0 : productlist.size();

    }
    @Override
    public int getItemViewType(int position) {
        Log.e("Boolean", String.valueOf((position == productlist.size() - 1 && isLoadingAdded)));
        Log.e("List", String.valueOf(productlist.size() - 1));
        Log.e("Position", String.valueOf(position));
        return (position == productlist.size() - 1&& isLoadingAdded) ? LOADING : ITEM;

    }

    public class MyViewHolder extends RecyclerView. ViewHolder {
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
    public class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }
    public void addLoadingFooter() {
        isLoadingAdded = true;
     /*   notifyItemInserted(productlist.size()+1);*/
        /*notifyDataSetChanged();*/


    }
    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = productlist.size();

          /*  productlist.remove(position);
            notifyItemRemoved(position);
*/


    }
}

package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Model.Cart_recy_model;
import com.example.compaq.b2b_application.Model.Order_history_inner_model;
import com.example.compaq.b2b_application.Model.Orderhistory_model;
import com.example.compaq.b2b_application.Model.Recy_model2;
import com.example.compaq.b2b_application.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.MainActivity.ip;
import static com.example.compaq.b2b_application.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.SessionManagement.PREF_NAME;

public class OrderHistory_adapter extends RecyclerView.Adapter<OrderHistory_adapter.ListnerViewHolder> {
    public FragmentActivity mCtx;
    private ArrayList<Orderhistory_model> productlist;
    public FragmentManager fragmentManager;
    public FragmentTransaction fragmentTransaction;
    public ImageView imageV;
    public Bundle bundle;
    private View view;
    public Context mContext;
    public SharedPreferences sharedPref;
    public   SharedPreferences.Editor myEditor;
    private String output;

    public OrderHistory_adapter(Context mContext, ArrayList<Orderhistory_model> productlist ) {
        this.productlist=productlist;
        this.mContext=mContext;
    }


    @NonNull
    @Override
    public OrderHistory_adapter.ListnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        view=inflater.inflate(R.layout.orderhistory_cradlayout,parent,false);
        OrderHistory_adapter.ListnerViewHolder holder=new OrderHistory_adapter.ListnerViewHolder(view);
        sharedPref =mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderHistory_adapter.ListnerViewHolder holder, int position) {
        final com.example.compaq.b2b_application.Model.Orderhistory_model listner=  productlist.get(position);

        holder.orderno.setText(listner.getOrderno());
        holder.orderdate.setText(listner.getOrderdate());

        ArrayList<Order_history_inner_model> arrayList=listner.getArrayList();


        Order_history_innerrAdapter order_history_innerrAdapter=new Order_history_innerrAdapter(mContext,arrayList);


        holder.innerRecyclerview.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        holder.innerRecyclerview.setHasFixedSize(true);
        holder.innerRecyclerview.setAdapter(order_history_innerrAdapter);

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  cancelOrder(holder.orderno,holder.getItemId(),);*/
                holder.cancel.setText("Cancelled");
            }
        });

    }

    @Override
    public int getItemCount() {
        return productlist.size();
    }

   /* public class MyViewHolder extends RecyclerView.ViewHolder {
        RecyclerView innerRecyclerview;
        TextView orderno,orderdate;
        public Button track,cancel;
        public MyViewHolder(View itemView) {
            super(itemView);

            track=(Button)itemView.findViewById(R.id.track_button) ;
            cancel=(Button)itemView.findViewById(R.id.cancel_button);

            orderno=(TextView)itemView.findViewById(R.id.Order_set);
            orderdate=(TextView)itemView.findViewById(R.id.date_set);

            innerRecyclerview=(RecyclerView)itemView.findViewById(R.id.orderhis_inner_recycler);
        }
    }*/

    public class ListnerViewHolder extends RecyclerView.ViewHolder {
        RecyclerView innerRecyclerview;
        TextView orderno,orderdate;
        public Button track,cancel;
        public ListnerViewHolder(View view) {
            super(view);
            track=(Button)itemView.findViewById(R.id.track_button) ;
            cancel=(Button)itemView.findViewById(R.id.cancel_button);

            orderno=(TextView)itemView.findViewById(R.id.Order_set);
            orderdate=(TextView)itemView.findViewById(R.id.date_set);

            innerRecyclerview=(RecyclerView)itemView.findViewById(R.id.orderhis_inner_recycler);
        }
    }
    public  void   cancelOrder(int id,String orderno,String productname){
       /* http://192.168.100.4:8040/gate/b2b/order/api/v1/item/cancel/220/OD980827460197?productName=undefined&customerMobile=+917411107119&customerEmail=neel@neel.com*/
        String url=ip+"gate/b2b/order/api/v1/item/cancel/"+id+"/"+orderno+"?productName="+productname+"&customerMobile=+91"+sharedPref.getString("mob",null)+"&customerEmail=n"+sharedPref.getString("mob",null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public  void onResponse(String response) {
                try {

                     if(response.equalsIgnoreCase("success")) {

                         BottomSheet.Builder builder1 = new BottomSheet.Builder(mCtx);
                         builder1.setTitle("Your Order cancelled successfully");
                         builder1.show();

                     }







                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                BottomSheet.Builder builder1 = new BottomSheet.Builder(mCtx);
                builder1.setTitle("Sorry!,could't send request.");
                builder1.show();
            }
        })

        {
            @Override
            public Map<String, String> getHeaders() {
                output=sharedPref.getString(ACCESS_TOKEN, null);

                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                params.put("Content-Type","application/json");
                return params;
            }
        };




        RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
        requestQueue.add(stringRequest);

    }

}
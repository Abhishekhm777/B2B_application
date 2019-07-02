package com.example.compaq.b2b_application.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Model.Update_product_model;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.MainActivity.ip;
import static com.example.compaq.b2b_application.SessionManagement.ACCESS_TOKEN;

public class Update_product_activity_ad  extends RecyclerView.Adapter<Update_product_activity_ad.MyViewHolder>{
    private ArrayList<Update_product_model> productlist;
    public Context mContext;
    private String imageUrl="https://server.mrkzevar.com/gate/b2b/catalog/api/v1/assets/image/";

    Dialog dialog;
    TextView ok,cancel,main_text;
    String output;
    SharedPreferences sharedPref;

    public Update_product_activity_ad(Context mContext, ArrayList<Update_product_model> productlist) {
        this.productlist=productlist;
        this.mContext=mContext;

    }
    @NonNull
    @Override
    public Update_product_activity_ad.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_recycler_cardlayout, parent, false);
        dialog = new Dialog(mContext);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.alert_popup);
        ok=(TextView)dialog.findViewById(R.id.ok);
        cancel=(TextView)dialog.findViewById(R.id.cancel);
        main_text=(TextView)dialog.findViewById(R.id.popup_textview);
        main_text.setText("Do you want to delete this image?");

        sharedPref=mContext.getSharedPreferences("USER_DETAILS",0);
        output=sharedPref.getString(ACCESS_TOKEN, null);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final Update_product_activity_ad.MyViewHolder holder, int position) {
        final com.example.compaq.b2b_application.Model.Update_product_model listner=  productlist.get(position);
        if(listner.getId().equalsIgnoreCase("null"))
        {
            holder.imageView.setImageBitmap(listner.getBitmap());
        }
        else {
            String url = imageUrl + listner.getId();
            Glide.with(mContext).load(url).into(holder.imageView);
        }

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        delete(listner.getId(),listner.getMain_id(),holder.getAdapterPosition(),view);
                        productlist.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        notifyItemRangeChanged(holder.getAdapterPosition(), productlist.size());
                        notifyDataSetChanged();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return productlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,button;
        CardView card;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.slider_image);
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();  //set width of the card
            int width = dm.widthPixels/2;
            button=(ImageView) itemView.findViewById(R.id.delete_btn);
            card=(CardView)itemView.findViewById(R.id.card_view);
            card.getLayoutParams().width=width;
            card.requestLayout();
        }
    }

    public  void delete(final String id, final String mainid, final int adapterPosition, final View view){
        String url = ip+"gate/b2b/catalog/api/v1/assets/secure/image/delete/"+mainid+"?fileIDs="+id;
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest objectRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    Toast.makeText(mContext, "Product image deleted", Toast.LENGTH_SHORT).show();



                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Snackbar.make(view, "Sorry! something went wrong", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "bearer "+output);
                params.put("Content-Type", "application/json");
                return params;
            }

        };

        requestQueue.add(objectRequest);

    }
}

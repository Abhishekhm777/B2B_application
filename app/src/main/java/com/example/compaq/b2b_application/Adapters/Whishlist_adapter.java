package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Main2Activity;
import com.example.compaq.b2b_application.MainActivity;
import com.example.compaq.b2b_application.Model.Whishlist_model;
import com.example.compaq.b2b_application.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Fragments.Fragment_2.item_clicked;
import static com.example.compaq.b2b_application.MainActivity.ip1;
import static com.example.compaq.b2b_application.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.SessionManagement.PREF_NAME;

public class Whishlist_adapter extends RecyclerView.Adapter<Whishlist_adapter.ViewHolder> {
    Context mContext;
    private ArrayList<Whishlist_model> wishList;
    SharedPreferences sharedPref;
    String output;
    public String  itemclicked="";
    public Bundle bundle;

    public Whishlist_adapter(Context mContext, ArrayList<Whishlist_model> wishList){
        this.mContext=mContext;
        this.wishList=wishList;


    }
    @NonNull
    @Override
    public Whishlist_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        sharedPref =mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);


        output=sharedPref.getString(ACCESS_TOKEN, null);
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.whishlist_card_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final Whishlist_adapter.ViewHolder holder, int position) {
        final com.example.compaq.b2b_application.Model.Whishlist_model listner=  wishList.get(position);


        Glide.with(mContext).load(listner.getHref()).into(holder.product_img);
        holder.name.setText(listner.getName());
        holder.sku.setText(listner.getSku());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*AlertDialog.Builder alertDialog=new AlertDialog.Builder(mContext);
                alertDialog  .setTitle("Alert");
                alertDialog .setMessage("Do you want to remove this item from the whishlist?");
                alertDialog  .setCancelable(false);
                alertDialog  .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                alertDialog  .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {
                                 wishList.remove(holder.getAdapterPosition());
                                 notifyItemRemoved(holder.getAdapterPosition());
                                 notifyItemRangeChanged(holder.getAdapterPosition(), wishList.size());
                                 removeFromWhish(listner.getSku());
                             }
                         }).show();
*/

                wishList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), wishList.size());
                removeFromWhish(listner.getSku());
            }
        });

        holder.product_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, listner.getName(),Toast.LENGTH_SHORT).show();
                String name= listner.getName();
                itemclicked= listner.getId();

                bundle=new Bundle();
                bundle.putString("item_name",name);
                bundle.putString("Item_Clicked",itemclicked);
                bundle.putString("lurl",item_clicked);
                Intent intent=new Intent(mContext, Main2Activity.class).putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return wishList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView product_img,delete;
        TextView name,sku;
        public ViewHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.wish_name);
            sku=(TextView)itemView.findViewById(R.id.wish_sku);
            product_img=(ImageView)itemView.findViewById(R.id.wish_image);
            delete=(ImageView)itemView.findViewById(R.id.delete_wish);
        }
    }

    public  void removeFromWhish(final String sku){

        String url = ip1+"/b2b/api/v1/user/wish-list/"+sharedPref.getString("userid","")+"/DL?wishList="+sku;
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {




                    Toast.makeText(mContext, "Removed from whishlist", Toast.LENGTH_SHORT).show();
                    MainActivity mainActivity=new MainActivity();
                    mainActivity.getinfo(mContext);

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

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

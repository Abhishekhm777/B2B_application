package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Activity.Main2Activity;
import com.example.compaq.b2b_application.Activity.MainActivity;
import com.example.compaq.b2b_application.Model.Whishlist_model;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Fragments.products_display_fragment.item_clicked;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;

public class Whishlist_adapter extends RecyclerView.Adapter<Whishlist_adapter.ViewHolder> {
    Context mContext;
    private ArrayList<Whishlist_model> wishList;
    SharedPreferences sharedPref;
    String output,cartid,userid,wholeseller,url;
    public String  itemclicked="";
    public Bundle bundle;
    public SharedPreferences.Editor myEditor;
    public int json_length=0;

    public Whishlist_adapter(Context mContext, ArrayList<Whishlist_model> wishList){
        this.mContext=mContext;
        this.wishList=wishList;


    }
    @NonNull
    @Override
    public Whishlist_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        sharedPref =mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        myEditor = sharedPref.edit();
        cartid = sharedPref.getString("cartid", "");
        userid = sharedPref.getString("userid", "");
        wholeseller=sharedPref.getString("Wholeseller_id", null);
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
        holder.move_toBag_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToBag(listner.getName(),listner.getSku(),listner.getId(),listner.getHref(),holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return wishList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView product_img,delete;
        Button move_toBag_btn;
        TextView name,sku;
        public ViewHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.wish_name);
            sku=(TextView)itemView.findViewById(R.id.wish_sku);
            product_img=(ImageView)itemView.findViewById(R.id.wish_image);
            delete=(ImageView)itemView.findViewById(R.id.delete_wish);
            move_toBag_btn=itemView.findViewById(R.id.move_to_bag_btn) ;
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

    /////////////Move To Bag////////////////
    public void moveToBag(String prod_name, final String sku, String id, String image_url, final int position) {

        JSONObject mainJasan= new JSONObject();
        if(cartid.equalsIgnoreCase("0")){
            url=ip+"gate/b2b/order/api/v1/cart/add";

            JSONObject json1= new JSONObject();
            final JSONArray items_jsonArray=new JSONArray();
            try {
                json1.put("product",sku);
                json1.put("name",prod_name);
                json1.put("productID",id);
                json1.put("quantity",1);
                json1.put("description","");

                json1.put("purity","22");

                json1.put("productImage",image_url);
                json1.put("seller",wholeseller);

                items_jsonArray.put(json1);
                mainJasan.put("customer",userid);
                mainJasan.put("items",items_jsonArray);
                Log.e("format",mainJasan.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Map<String, String> params = new HashMap<>();
            params.put("customer", userid);
            params.put("items", String.valueOf(items_jsonArray));

        }
        else {
            url = ip+"gate/b2b/order/api/v1/cart/update";


            JSONObject json1= new JSONObject();
            final JSONArray items_jsonArray=new JSONArray();
            try {
                json1.put("product",sku);
                json1.put("productID",id);
                json1.put("quantity",1);
                json1.put("description","");
                json1.put("purity","22");
                json1.put("productImage",image_url);
                json1.put("seller",wholeseller);

                items_jsonArray.put(json1);
                mainJasan.put("customer",userid);
                mainJasan.put("items",items_jsonArray);
                mainJasan.put("id",cartid);

                Log.e("format",mainJasan.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Map<String, String> params = new HashMap<>();
            params.put("customer", userid);
            params.put("items", String.valueOf(items_jsonArray));
            params.put("id", cartid);
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, mainJasan, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    removeFromWhish(sku);
                    wishList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, wishList.size());
                    String cartid=response.getString("id");

                    myEditor.putString("cartid",cartid);
                    myEditor.apply();
                    myEditor.commit();
                    JSONArray jsonArray=response.getJSONArray("items");

                    json_length= Integer.parseInt(sharedPref.getString("no_of_items",""));
                    myEditor.putString("no_of_items", String.valueOf(json_length+1)).apply();
                    myEditor.commit();
                   /* MainActivity mActivity= new MainActivity();
                    mActivity.setupBadge(mContext);
                    if(mContext.getClass().getSimpleName().equalsIgnoreCase("Main2Activity")){
                        Main2Activity main2Activity= new Main2Activity();
                        main2Activity.setupBadge(mContext);
                    }
                   Main2Activity main2Activity= new Main2Activity();
                    main2Activity.setupBadge(mContext);*/

                }

                // Go to next activity

                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", String.valueOf(error));

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headr = new HashMap<>();

                headr.put("Authorization","bearer "+output);
                headr.put("Content-Type", "application/json");
                return headr;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);
    }
}

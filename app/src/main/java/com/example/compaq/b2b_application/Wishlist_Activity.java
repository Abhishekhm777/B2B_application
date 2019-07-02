package com.example.compaq.b2b_application;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.Whishlist_adapter;
import com.example.compaq.b2b_application.Model.Whishlist_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.example.compaq.b2b_application.MainActivity.ip;
import static com.example.compaq.b2b_application.MainActivity.ip1;
import static com.example.compaq.b2b_application.MainActivity.wishlist2;
import static com.example.compaq.b2b_application.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.SessionManagement.PREF_NAME;

public class Wishlist_Activity extends AppCompatActivity {
    public static ArrayList<String> skulist;
    ArrayList<String>idlist;
    ArrayList<Whishlist_model>wishlist;
    private static final String PREF = "CountAppSharedPreference";
    SharedPreferences sharedPref;
    SharedPreferences.Editor myEditior;
    int pos=0,pos2=0;
    Toolbar toolbar;
    String href;
    RecyclerView wishlistRecycler;
    Whishlist_adapter adapter;
    public String output;
    private ImageView empty_imageview;
    Button emptybtn;
    TextView textView,empty_text_view1;
    ProgressBar progressBar;
    private SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_);

        session = new SessionManagement(getApplicationContext().getApplicationContext());
        skulist=new ArrayList<>();
        wishlist=new ArrayList<>();
        idlist=new ArrayList<>();
        progressBar=(ProgressBar)findViewById(R.id.progress);
        empty_imageview=(ImageView)findViewById(R.id.wish_empty) ;
        emptybtn=(Button)findViewById(R.id.wish_empty_btn);
        emptybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });
        textView=(TextView)findViewById(R.id.empty_wish_text) ;
        empty_text_view1=(TextView)findViewById(R.id.empty_wish_text1) ;
        toolbar=(Toolbar)findViewById(R.id.whishlist_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        sharedPref =getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        output=sharedPref.getString(ACCESS_TOKEN, null);
        wishlistRecycler=findViewById(R.id.whishlist_recycler);
        getinfo();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // click on 'up' button in the action bar, handle it here
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed()
    {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }
    public  void getinfo(){
        progressBar.setVisibility(View.VISIBLE);
        String url = ip1+"/b2b/api/v1/user/info";
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    //JsonObject jsonObject=new JsonObject(response);
                    JSONArray jsonArray=response.getJSONArray("wishList");
                    if(jsonArray.length()==0){
                        wishlistRecycler.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);


                        empty_text_view1.setVisibility(View.VISIBLE);
                        emptybtn.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);
                        empty_imageview.setImageResource(R.drawable.wishlist_icon);
                        empty_imageview.setVisibility(View.VISIBLE);

                    }

                    if(jsonArray.length()!=0)
                    {
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String productLink=jsonObject.getString("productLink");


                            skulist.add(productLink);
                            wishlist2.add(productLink);

                            if(i+1==jsonArray.length()){
                                fatchid();
                            }
                        }
                    }



                } catch (JSONException e) {

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
                params.put("Authorization", "bearer " + output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

        };

        RequestQueue rQueue = Volley.newRequestQueue(getApplicationContext());
        rQueue.add(objectRequest);

    }

    ///////////////////////////////////////////////////////////////////////////// fatch id///////////////////////////////////////////////////
    public  void fatchid(){
        String sku=skulist.get(pos);
        String url2 = ip+"gate/b2b/catalog/api/v1/product/productBySKU/"+sku+"";

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    //JsonObject jsonObject=new JsonObject(response);
                    String id=response.getString("id");

                    idlist.add(id);
                    ++pos;
                    if(pos!=skulist.size())
                    {


                        fatchid();
                    }
                    if(pos==skulist.size()) {
                        productInfo();
                    }




                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressBar.setVisibility(View.GONE);
                NetworkResponse response = volleyError.networkResponse;
                ++pos;
                if(pos!=skulist.size())
                {


                    fatchid();
                }

                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 404:
                            if(pos==skulist.size()) {
                                wishlistRecycler.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);


                                empty_text_view1.setVisibility(View.VISIBLE);
                                emptybtn.setVisibility(View.VISIBLE);
                                textView.setVisibility(View.VISIBLE);
                                empty_imageview.setImageResource(R.drawable.wishlist_icon);
                                empty_imageview.setVisibility(View.VISIBLE);
                            }
                            break;

                        case 401:

                            Toast.makeText(getApplicationContext(),"Session Expired!",Toast.LENGTH_SHORT).show();
                            /* new Reffressh_token().getToken(MainActivity.this);*/
                            session.logoutUser(getApplicationContext());

                    }
                }
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

        RequestQueue rQueue = Volley.newRequestQueue(getApplicationContext());
        rQueue.add(objectRequest);

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    public  void productInfo(){
        final String ids=idlist.get(pos2);
        String url3 = ip+"gate/b2b/catalog/api/v1/product/"+ids+"";
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url3, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                try {

                    JSONObject obj1 = response.getJSONObject("resourceSupport");
                    int k = 0;

                    String name=obj1.getString("name");
                    String sku1=obj1.getString("sku");
                    String id=obj1.getString("id");
                    JSONArray ja_data = obj1.getJSONArray("links");
                    for (int i = 0; i < ja_data.length(); i++) {
                        JSONObject jObj = ja_data.getJSONObject(i);
                        for (Iterator<String> iterator = jObj.keys(); iterator.hasNext(); ) {
                            String key = (String) iterator.next();

                            if (jObj.get(key).equals("imageURl") && k < 1) {
                                href = jObj.getString("href");
                                Log.d("HREF", href);
                                k++;

                            }
                        }
                    }

                    wishlist.add(new Whishlist_model(name,href,sku1,id));
                    ++pos2;
                    if(pos2!=idlist.size()){

                        productInfo();

                    }

                    if(pos2==idlist.size()){
                        Log.d("size...",wishlist.size()+"..."+idlist.size());

                        adapter=new Whishlist_adapter(getApplicationContext(),wishlist);

                        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                            @Override
                            public void onItemRangeRemoved(int positionStart, int itemCount) {
                                super.onItemRangeRemoved(positionStart, itemCount);

                                if(adapter.getItemCount()==0){
                                    wishlistRecycler.setVisibility(View.GONE);
                                    empty_text_view1.setVisibility(View.VISIBLE);
                                    textView.setVisibility(View.VISIBLE);
                                    emptybtn.setVisibility(View.VISIBLE);
                                    empty_imageview.setImageResource(R.drawable.wishlist_icon);
                                    empty_imageview.setVisibility(View.VISIBLE);

                                }

                            }

                            @Override
                            public void onChanged() {
                                super.onChanged();

                            }

                        });
                        wishlistRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        wishlistRecycler.setAdapter(adapter);

                    }
                    wishlistRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    wishlistRecycler.setAdapter(adapter);

                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                NetworkResponse response = volleyError.networkResponse;
                ++pos2;
                if(pos2!=idlist.size()){

                    productInfo();
                    return;


                }

                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 404:

                            wishlistRecycler.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);


                            empty_text_view1.setVisibility(View.VISIBLE);
                            emptybtn.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.VISIBLE);
                            empty_imageview.setImageResource(R.drawable.wishlist_icon);
                            empty_imageview.setVisibility(View.VISIBLE);

                            break;

                        case 401:

                            Toast.makeText(getApplicationContext(),"Session Expired!",Toast.LENGTH_SHORT).show();
                            /* new Reffressh_token().getToken(MainActivity.this);*/
                            session.logoutUser(getApplicationContext());

                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "bearer " +output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

        };

        RequestQueue rQueue = Volley.newRequestQueue(getApplicationContext());
        rQueue.add(objectRequest);

    }

    @Override
    public void onResume() {
        super.onResume();

    }
    @Override
    public void onStop() {
        super.onStop();

    }
}

package com.example.compaq.b2b_application.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.Sellero_order_frag2_Adapter;
import com.example.compaq.b2b_application.Model.Selller_history_frag2_model;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;


public class Seller_Orer_His_frag2 extends Fragment {
    public RecyclerView recyclerView;
    public SharedPreferences sharedPref;
    public ArrayList<Selller_history_frag2_model> productlist;
    String output;
    Bundle bundle;
    Sellero_order_frag2_Adapter sellero_order_frag2_adapter;
    HashMap<String,String> images=new HashMap<>();
    ArrayList<String> image_name=new ArrayList<>();
     private RelativeLayout progress_bar;
    public Seller_Orer_His_frag2() {
        // Required empty public constructor
    }
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

            view = inflater.inflate(R.layout.fragment_seller__orer__his_frag2, container, false);
            sharedPref = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            recyclerView = (RecyclerView) view.findViewById(R.id.sell_histry);
            productlist = new ArrayList<>();
            progress_bar=(RelativeLayout)view.findViewById(R.id.progress);
            progress_bar.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            recyclerView.setHasFixedSize(true);
            sharedPref = getActivity().getSharedPreferences("USER_DETAILS", 0);
            output = sharedPref.getString(ACCESS_TOKEN, null);
            bundle = this.getArguments();
            Seller_history(bundle.getString("order_no"));


        return view;
    }
    public void Seller_history(final String order_no) {
             String url=ip+"gate/b2b/order/api/v1/order/getorder/"+order_no;
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                               JSONObject jsonObject=new JSONObject(response);
                               JSONObject jsonObject1=jsonObject.getJSONObject("order");
                                JSONArray jsonArray=jsonObject1.getJSONArray("items");
                                for(int i=0;i<jsonArray.length();i++){
                                   JSONObject jsonObject2=jsonArray.getJSONObject(i);
                                    String name=jsonObject2.getString("product");
                                    if(name.equalsIgnoreCase("null")){
                                        name=jsonObject2.getString("customOrderImageID");
                                    }
                                   String item_no=jsonObject2.getString("itemNo");
                                    String image_id=jsonObject2.getString("productID");
                                    String status=jsonObject2.getString("itemStatus");
                                    String quantity=jsonObject2.getString("quantity");
                                    String purity=jsonObject2.getString("purity");

                                    String description=jsonObject2.getString("description");
                                    image_name.add(name);


                                    productlist.add(new Selller_history_frag2_model(name,item_no,image_id,status,quantity,description,purity));
                                }

                          /*  sellero_order_frag2_adapter   = new Sellero_order_frag2_Adapter(getActivity(), productlist);
                                recyclerView.setAdapter(sellero_order_frag2_adapter);*/
                          if(jsonObject1.getString("orderType").equalsIgnoreCase("ONLINECUSTOM_ORDER")){
                              custom_Image(image_name);
                              return;
                          }
                                Images(image_name);



                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if(error != null ){
                    switch(response.statusCode){
                        case 404:
                            Snackbar.make(view,"Sorry could'nt reach server",Snackbar.LENGTH_LONG).show();
                            break;

                        case 401:
                            Snackbar.make(view,"Session expiered",Snackbar.LENGTH_LONG).show();
                            break;
                        case 500:
                            Snackbar.make(view,"Sorry something went wrong",Snackbar.LENGTH_LONG).show();
                            break;



                    }
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        requestQueue.add(stringRequest);
    }
    private void Images(ArrayList<String> image_name){
        for(int i=0;i<image_name.size();i++){
            GetImage(image_name.get(i));
        }
    }
    private void custom_Image(ArrayList<String> image_name){
        for(int i=0;i<image_name.size();i++){
            progress_bar.setVisibility(View.GONE);
            images.put(image_name.get(i),"https://server.mrkzevar.com/gate/b2b/order/api/v1/image/get/"+image_name.get(i));
            sellero_order_frag2_adapter   = new Sellero_order_frag2_Adapter(getActivity(), productlist,images);
            recyclerView.setAdapter(sellero_order_frag2_adapter);
        }
    }

    public void GetImage(final String image_name) {
        String url=ip+"gate/b2b/order/api/v1/getProduct/"+image_name;
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONObject jsonObject1=jsonObject.getJSONObject("_links");
                            Object intervention = jsonObject1.get("imageURl");
                            if (intervention instanceof JSONArray) {

                                JSONArray img_arr = (JSONArray) intervention;
                                JSONObject img_obj = img_arr.getJSONObject(0);
                                images.put(image_name,img_obj.getString("href"));


                            } else if (intervention instanceof JSONObject) {
                                // It's an object
                                JSONObject img_link = (JSONObject) intervention;
                                images.put(image_name,img_link.getString("href"));

                            }



                            progress_bar.setVisibility(View.GONE);
                            sellero_order_frag2_adapter   = new Sellero_order_frag2_Adapter(getActivity(), productlist,images);
                            recyclerView.setAdapter(sellero_order_frag2_adapter);



                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if(error != null ){
                    switch(response.statusCode){
                        case 404:
                            Snackbar.make(view,"Sorry could'nt reach server",Snackbar.LENGTH_LONG).show();
                            break;

                        case 401:
                            Snackbar.make(view,"Session expiered",Snackbar.LENGTH_LONG).show();
                            break;
                        case 500:
                            Snackbar.make(view,"Sorry something went wrong",Snackbar.LENGTH_LONG).show();
                            break;


                    }
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        requestQueue.add(stringRequest);
    }

}

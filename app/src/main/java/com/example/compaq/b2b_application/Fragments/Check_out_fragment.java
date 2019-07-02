package com.example.compaq.b2b_application.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.Checkout_adapter;
import com.example.compaq.b2b_application.Model.Check_out_Recyclemodel;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.example.compaq.b2b_application.MainActivity.ip;
import static com.example.compaq.b2b_application.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.SessionManagement.PREF_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class Check_out_fragment extends Fragment {
  /*  public SharedPreferences pref;*/
    public SharedPreferences sharedPref;
    public String userid="";
    public    ArrayList<String>product_id;
    public String href="";
    public Check_out_Recyclemodel check_out_recyclemodel;
    public Checkout_adapter checkout_adapter;
    public ArrayList<Check_out_Recyclemodel> productlist;
    public RecyclerView recyclerView;
    public Check_out_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_check_out_fragment, container, false);


        sharedPref =getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);


        recyclerView = (RecyclerView)view.findViewById(R.id.checkout_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setHasFixedSize(true);
        productlist=new ArrayList<>();
        getItem_ids();
        return view;
    }

    public  void getItem_ids() {

       /* sharedPref = getActivity().getSharedPreferences("User_information", 0);*/
        userid = sharedPref.getString("userid", "");
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest jsonArrayRequest = new StringRequest(
                ip+"gate/b2b/order/api/v1/cart/customer/"+userid+"",
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        product_id=new ArrayList<>();


                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray ja_data = jsonObject.getJSONArray("items");
                            for (int i = 0; i < ja_data.length(); i++) {
                                JSONObject jsonObject1 = ja_data.getJSONObject(i);

                                String se=jsonObject1.getString("seal");
                                String we=jsonObject1.getString("netWeight");
                                String pid=jsonObject1.getString("productID");
                                int delete_ids= Integer.parseInt(jsonObject1.getString("id"));

                             /*   display_cart_details(pid,we,se,delete_ids);*/

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                sharedPref=getActivity().getSharedPreferences("USER_DETAILS",0);

                String output=sharedPref.getString(ACCESS_TOKEN, null);
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                return params;
            }

        };
        requestQueue.add(jsonArrayRequest);


    }


    //////////////// fetching cart details//////////////////////////

  /*  public void display_cart_details(final String id,final String weght,final String sealw,final int del_id) {



        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                ip+"/gate/b2b/catalog/api/v1/product/"+id + "",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            int k = 0;
                            JSONObject obj = new JSONObject(response);
                            JSONObject obj1 = obj.getJSONObject("resourceSupport");
                            String name = obj1.getString("name");
                            String product_id = obj1.getString("id");



                            JSONArray ja_data = obj1.getJSONArray("links");
                            for (int i = 0; i < ja_data.length(); i++) {
                                JSONObject jObj = ja_data.getJSONObject(i);
                                for (Iterator<String> iterator = jObj.keys(); iterator.hasNext(); ) {
                                    String key = (String) iterator.next();

                                    if (jObj.get(key).equals("imageURl") && k < 1) {
                                        href = jObj.getString("href");
                                        k++;
                                    }
                                }
                            }
                              *//*  JSONObject obj2 = obj.getJSONObject("priceandseller");
                                JSONObject jsonObject1 = obj2.getJSONObject("price");
                                String new_price = jsonObject1.getString("productFinalPrice");
                                String old_price = jsonObject1.getString("productPrice");*//*


                              *//*  total_price = (Math.round((total_price + (Double.parseDouble(new_price))) * 100D) / 100D);

                                Log.d("price", total_price + "");*//*



                            productlist.add(new Check_out_Recyclemodel(name, href, weght, product_id, sealw,del_id,"sfg"));


                            checkout_adapter = new Checkout_adapter(getContext(), productlist);
                            *//*recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));*//*
                            recyclerView.setAdapter(checkout_adapter);



                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //displaying the error in toast if occurrs
                makeText(getContext().getApplicationContext(), error.getMessage(), LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() {

                sharedPref=getActivity().getSharedPreferences("USER_DETAILS",0);

                String output=sharedPref.getString(ACCESS_TOKEN, null);
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        requestQueue.add(stringRequest);
    }

*/
}

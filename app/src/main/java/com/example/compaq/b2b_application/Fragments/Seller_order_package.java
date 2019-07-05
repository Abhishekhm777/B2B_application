package com.example.compaq.b2b_application.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.Seller_order_package_adapter;
import com.example.compaq.b2b_application.Model.Seller_order_package_model;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class Seller_order_package extends Fragment {

    public RecyclerView recyclerView;
    public ArrayList<Seller_order_package_model> productlist;
    Seller_order_package_adapter adapter;
    public SharedPreferences sharedPref;
    Bundle bundle;
  String output,image_url,order_no,cust_mobile_no;
  private  View view;
  private ArrayList<String> pro_name_array;
  private HashMap<String,String> image_map;
  private  int p=0;
  private String product,taskid;
  private  SearchView searchView;
  private AppBarLayout appBarLayout;
    public Seller_order_package() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view= inflater.inflate(R.layout.fragment_seller_order_package, container, false);
        sharedPref =getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sharedPref=getActivity().getSharedPreferences("USER_DETAILS",0);
        output=sharedPref.getString(ACCESS_TOKEN, null);

        appBarLayout=(AppBarLayout)getActivity().findViewById(R.id.appBarLayout) ;
        appBarLayout.setExpanded(true,true);

        searchView = (SearchView) getActivity().findViewById(R.id.sellers_search);
        searchView.setVisibility(View.GONE);
        recyclerView = (RecyclerView)view.findViewById(R.id.seller_package_recycler);
        productlist=new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setHasFixedSize(true);
        bundle = this.getArguments();
         order_no=bundle.getString("order_no");
        taskid=bundle.getString("task_id");
        cust_mobile_no=bundle.getString("cust_mobile");
        Order_package(order_no);
        return  view;
    }
   /////////////////////////////////////////



    ////////////////////////////////////////////////////////////////////////
    public void Order_package(String order_no) {
        pro_name_array=new ArrayList<>();

          String ui=ip+"gate/b2b/order/api/v1/item/allitems/"+sharedPref.getString("userid",null)+"/"+order_no;
          String url=ui.replaceAll("\\s", "");

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jObj = jsonArray.getJSONObject(i);

                                JSONObject jsonObject=jObj.getJSONObject("item");
                                 product=jsonObject.getString("product");
                                String qty=String.valueOf(jsonObject.getString("quantity"));
                                String itemStatus=jsonObject.getString("itemStatus");
                                String item_no=jsonObject.getString("itemNo");

                                String size=jsonObject.getString("size");
                                String purity=jsonObject.getString("purity");
                                String weight=jsonObject.getString("netWeight");
                                String order_weigt=jsonObject.getString("totalWeight");
                                String expected=jsonObject.getString("expectedDeliveryDate");

                                String product_id=jsonObject.getString("productID");
                                String pay_status=jsonObject.getString("paymentStatus");

                                String id=String.valueOf(jsonObject.getString("id"));
                                if(product.equalsIgnoreCase("null")){
                                    String image_url="https://server.mrkzevar.com/gate/b2b/order/api/v1/image/get/"+jsonObject.getString("customOrderImageID");
                                    productlist.add(new Seller_order_package_model(jsonObject.getString("name"),qty,itemStatus,image_url,item_no,order_weigt,size,purity,weight,id,taskid,expected,product_id,pay_status,cust_mobile_no));
                                    Log.e("IMAGE_UUU","NKDCDFCDV");

                                }
                                else {
                                    Log.e("ELSEEE","YHUGUU");
                                    pro_name_array.add(product);
                                    productlist.add(new Seller_order_package_model(product,qty,itemStatus,"null",item_no,order_weigt,size,purity,weight,id,taskid,expected,product_id,pay_status,cust_mobile_no));
                                }

                            }
                           if(pro_name_array.size()==0){
                               Log.e("tyytyty","CDDVD");
                               adapter = new Seller_order_package_adapter(getContext(), productlist, view, image_map);
                               recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                               recyclerView.setAdapter(adapter);
                           }

                           else {

                               Log.e("out","kjkj");
                               imageArray(pro_name_array);
                           }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //displaying the error in toast if occurrs


                NetworkResponse response = error.networkResponse;
                Log.e("Status" , String.valueOf(response.statusCode));
                if(response != null ){
                    switch(response.statusCode){
                        case 401:
                            BottomSheet.Builder builder1 = new BottomSheet.Builder(getActivity());
                            builder1.setTitle("Session expiered!!");
                            builder1.show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        requestQueue.add(stringRequest);
    }

    private void  imageArray(ArrayList<String> arrayList ){
        image_map=new HashMap<>();
        for(int i=0;i<arrayList.size();i++){
            images(arrayList.get(i));
        }
    }

    public void images(final String product) {

        String ui=ip+"gate/b2b/order/api/v1/getProduct/"+product;
        String url=ui.replaceAll("\\s", "");
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url
                ,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            ++p;

                            JSONObject jsonArray = new JSONObject(response);

                           JSONObject jsonObject=jsonArray.getJSONObject("_links");
                            Object intervention = jsonObject.get("imageURl");
                            if (intervention instanceof JSONArray) {
                                JSONArray jsonArray1=jsonObject.getJSONArray("imageURl");
                                JSONObject jsonObject2=jsonArray1.getJSONObject(0);
                                 image_url=jsonObject2.getString("href");
                                 image_map.put(product,image_url);

                            }
                            else if(intervention instanceof JSONObject) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("imageURl");
                                 image_url = jsonObject1.getString("href");
                                image_map.put(product,image_url);

                            }
                            if(pro_name_array.size()==p) {

                                adapter = new Seller_order_package_adapter(getContext(), productlist, view, image_map);
                                adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                                    @Override
                                    public void onItemRangeRemoved(int positionStart, int itemCount) {
                                        super.onItemRangeRemoved(positionStart, itemCount);


                                    }

                                    @Override
                                    public void onChanged() {
                                        super.onChanged();
                                        Log.e("CLESAREADS", "CLEARED");
                                        Order_package(order_no);
                                    }
                                });

                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                                recyclerView.setAdapter(adapter);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //displaying the error in toast if occurrs


                NetworkResponse response = error.networkResponse;
                Log.e("Status" , String.valueOf(response.statusCode));
                if(error != null ){
                    switch(response.statusCode){
                        case 401:
                            BottomSheet.Builder builder1 = new BottomSheet.Builder(getActivity());
                            builder1.setTitle("Session expiered!!");
                            builder1.show();


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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        requestQueue.add(stringRequest);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("CLEAR","CLEARED");
        try{
            adapter.someMethod();
        }
        catch (Exception e){

        }


    }
}

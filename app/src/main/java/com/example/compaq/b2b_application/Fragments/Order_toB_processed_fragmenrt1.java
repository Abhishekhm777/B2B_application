package com.example.compaq.b2b_application.Fragments;



import android.content.Context;

import android.content.SharedPreferences;




import android.os.Build;
import android.os.Bundle;


import android.support.annotation.RequiresApi;

import android.support.v4.app.Fragment;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.EndlessRecyclerViewScrollListener;
import com.example.compaq.b2b_application.Adapters.Order_to_bprocessed_Adapter;
import com.example.compaq.b2b_application.Helper_classess.AlertDialogManager;
import com.example.compaq.b2b_application.Model.OrderTobe_customer_model;
import com.example.compaq.b2b_application.Model.Seller_order_history;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.Helper_classess.SessionManagement;

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
public class Order_toB_processed_fragmenrt1 extends Fragment {
    public Toolbar toolbar;
    public static TextView pricetext_view;
    public RecyclerView recyclerView;
    public Order_to_bprocessed_Adapter order_to_bprocessed_adapter;
    public ArrayList<Seller_order_history> productlist;
    /* SharedPreferences pref;*/
    public Bundle bundle;
    public String userid="";
    public SharedPreferences sharedPref;
    public   SharedPreferences.Editor myEditor;
    private  HashMap<String,String> names;
    private  ArrayList<OrderTobe_customer_model> list=new ArrayList<>();
    private  HashMap<String,OrderTobe_customer_model> details;
    public JSONArray ja_data;
    AlertDialogManager alert = new AlertDialogManager();
    SessionManagement session;
    private String output,user_id;
    int number;
    private  RelativeLayout progress;
    private android.support.v7.widget.SearchView searchView;
    public Order_toB_processed_fragmenrt1() {
        // Required empty public constructor
    }
    public    View view1;
    public static Boolean aBoolean=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
          if(view1==null) {
              view1 = inflater.inflate(R.layout.fragment_company_info_fragment, container, false);
              progress = (RelativeLayout) view1.findViewById(R.id.progress);
              progress.setVisibility(View.VISIBLE);

              sharedPref = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

              recyclerView = (RecyclerView) view1.findViewById(R.id.order_to_b);


              recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
              recyclerView.setHasFixedSize(true);

              searchView = (SearchView) getActivity().findViewById(R.id.sellers_search);
              searchView.setFocusable(false);
              searchView.setFocusableInTouchMode(true);
              searchView.setSubmitButtonEnabled(true);
              sharedPref = getActivity().getSharedPreferences("USER_DETAILS", 0);

              output = sharedPref.getString(ACCESS_TOKEN, null);
              user_id = sharedPref.getString("userid", "");
              Seller_history();


              searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                  @Override
                  public boolean onQueryTextSubmit(String query) {
                      return false;
                  }

                  @Override
                  public boolean onQueryTextChange(String newText) {
                      order_to_bprocessed_adapter.getFilter().filter(newText);

                      return true;
                  }
              });
          }
            return view1;
        }

    public void Seller_history() {
        names=new HashMap<>();
        productlist = new ArrayList<>();
        details=new HashMap<>();
         number=0;
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                ip+"gate/b2b/order/api/v1/task/orders/shipping?userId="+user_id,

                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("REFRESHED","REFRESHED");
                          JSONArray jsonArray=new JSONArray(response);
                          for(int i=0;i<jsonArray.length();i++){
                              JSONObject jsonObject=jsonArray.getJSONObject(i);
                              String task_id=jsonObject.getString("taskId");
                                JSONObject order_ob=jsonObject.getJSONObject("order");

                                String order_no=order_ob.getString("orderNo");
                                String order_type=order_ob.getString("orderType");

                              String s1 = order_ob.getString("createdDate");
                              String[] order_date1 = s1.split("T");
                              String date = order_date1[0];
                                JSONArray items_array=order_ob.getJSONArray("items");
                                String items=String.valueOf(items_array.length());


                                    String cust_name=order_ob.getString("consigneeName");
                                    String cust_no=order_ob.getString("consigneeNumber");
                                    if(!order_type.equalsIgnoreCase("OFFLINE_SALES_ORDER")){
                                        names.put(order_no,order_ob.getString("customer"));
                                    }

                                    if(order_type.equalsIgnoreCase("ONLINE_SALES_ORDER")) {
                                        productlist.add(new Seller_order_history(order_no, "ONLINE", date, items, cust_name, cust_no,task_id));
                                    }
                                    if(order_type.equalsIgnoreCase("OFFLINE_SALES_ORDER")){
                                        productlist.add(new Seller_order_history(order_no, "OFFLINE", date, items, cust_name, cust_no,task_id));
                                    }
                              if(order_type.equalsIgnoreCase("ONLINECUSTOM_ORDER")){
                                  productlist.add(new Seller_order_history(order_no, "CUSTOM", date, items, cust_name, cust_no,task_id));
                              }

                          }
                            details(names);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //displaying the error in toast if occurrs


                NetworkResponse response = error.networkResponse;
                if(response != null ){
                    switch(response.statusCode){
                        case 404:
                            BottomSheet.Builder builder = new BottomSheet.Builder(getActivity());
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;

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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        requestQueue.add(stringRequest);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void  details( final  HashMap<String, String> nam){
        for ( Map.Entry<String, String> entry : nam.entrySet()) {
            String order = entry.getKey();
            String id = entry.getValue();
            customer_detail(id,order);
        }

    }

    public void customer_detail(String id,final  String no) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                ip+"gate/b2b/order/api/v1/getUser/"+id,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                           ++ number;
                            JSONObject jsonObject = new JSONObject(response);

                          OrderTobe_customer_model orderTobe_customer_model=new OrderTobe_customer_model(jsonObject.getString("firstName"),jsonObject.getString("mobileNumber"));

                            details.put(no,orderTobe_customer_model);


                            if(number==names.size()){
                                progress.setVisibility(View.GONE);
                                order_to_bprocessed_adapter = new Order_to_bprocessed_Adapter(getContext(), productlist,details);
                               LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                                 recyclerView.setLayoutManager(linearLayoutManager);
                                recyclerView.setAdapter(order_to_bprocessed_adapter);


                                recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener( linearLayoutManager) {

                                    @Override
                                    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {


                                    }

                                });

                            }

                        }

                         catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ++ number;
                //displaying the error in toast if occurrs
                NetworkResponse response = error.networkResponse;
                if(response != null ){
                    switch(response.statusCode){
                        case 404:
                            BottomSheet.Builder builder = new BottomSheet.Builder(getActivity());
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;

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

    public void updateText(String text){


    }

    @Override
    public void onResume() {
        super.onResume();
        searchView.setVisibility(View.VISIBLE);
    }
}

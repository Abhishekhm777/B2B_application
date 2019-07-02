package com.example.compaq.b2b_application.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.OrderHistory_adapter;
import com.example.compaq.b2b_application.Model.Order_history_inner_model;
import com.example.compaq.b2b_application.Model.Orderhistory_model;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.example.compaq.b2b_application.MainActivity.ip;
import static com.example.compaq.b2b_application.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.SessionManagement.PREF_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class Oder_History extends Fragment {

    public Toolbar toolbar;
    ArrayList<String> skulist;
    public RecyclerView recyclerView;
    public Orderhistory_model orderhistory_model;
    public OrderHistory_adapter orderHistory_adapter;
    public ArrayList<Orderhistory_model> productlist;
    public   ArrayList<Order_history_inner_model> details_list;

    public Bundle bundle;
    public String userid="";
    public SharedPreferences sharedPref;
    public   SharedPreferences.Editor myEditor;
    public    ArrayList<String>product_id;
    public String shippedto="";
    public String output="";
    public String dates;
    public String strDate;
    public String lastdate;
     int pos2=0,pos1=0;
public String imageurl;
    public JSONArray item_array;
    public JSONArray order_listarray;
    private Order_history_inner_model order_history_inner_model;
    private String product,netweight,qty;
    public ImageView no_order_image;
    Button no_order_button;
    TextView no_textview,order_text;

    public Oder_History() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_oder__history, container, false);


        no_order_image=(ImageView)view.findViewById(R.id.wish_empty);
        no_order_button=(Button)view.findViewById(R.id.wish_empty_btn);
        no_textview=(TextView)view.findViewById(R.id.empty_wish_text);
        order_text=(TextView)view.findViewById(R.id.empty_wish_text1);




        toolbar=(Toolbar)view.findViewById(R.id.order_hist_tooolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.inflateMenu(R.menu.bluedasrt);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });



        recyclerView = (RecyclerView)view.findViewById(R.id.order_hist_recycler);
        productlist=new ArrayList<>();
        details_list=new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setHasFixedSize(true);

        sharedPref =getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
       shippedto=sharedPref.getString("firstname", null);

       output=sharedPref.getString(ACCESS_TOKEN, null);


        Calendar cal = GregorianCalendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, +1);
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, +1);
        Date today=cal.getTime();
        Log.e("date1", String.valueOf(today));
        cal.add(Calendar.DAY_OF_YEAR, -30);
        Date seven_days = cal.getTime();

        Log.e("date1", String.valueOf(seven_days));
        SimpleDateFormat sdf;

        sdf = new SimpleDateFormat("yyyy-MM-dd");
         strDate = sdf.format(today);

         lastdate = sdf.format(seven_days);
        Log.e("date1,,after format", (strDate));

        Log.e("date1,,after format2", (lastdate));

        return  view;
    }


   /* public  void getItem_ids() {
        userid = sharedPref.getString("userid", "");
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Loading....");
        progressDialog.show();
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.GET,
                ip+"gate/b2b/order/api/v1/order/user/"+sharedPref.getString("userid",null)+"?fromDate="+lastdate+"&toDate="+strDate+"",

                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        product_id=new ArrayList<>();


                        try {
                            JSONObject jsonObject = new JSONObject(response);

                                JSONObject embedobbject = jsonObject.getJSONObject("_embedded");
                                order_listarray=embedobbject.getJSONArray("orderList");




                            for (int i = 0; i < order_listarray.length(); i++) {
                                ++pos1;
                                JSONObject jsonObject1 = order_listarray.getJSONObject(i);

                                String orderno=jsonObject1.getString("orderNo");

                                String order_status=jsonObject1.getString("orderStatus");

                                String s1=jsonObject1.getString("createdDate");

                                String[] order_date = s1.split("T");
                               String order_date1= order_date[0];
                               orderhistory_model= new Orderhistory_model(orderno,order_date1);
                                productlist.add(orderhistory_model);
                                 item_array=jsonObject1.getJSONArray("items");
                                details_list=new ArrayList<>();
                                skulist=new ArrayList<>();
                                for(int j=0;j<item_array.length();j++){
                                      ++pos2;
                                    JSONObject item_object=item_array.getJSONObject(j);

                                     product=item_object.getString("product");

                                     qty=item_object.getString("quantity");

                                     netweight=item_object.getString("netWeight");

                                  *//* display_cart_details(product,netweight, qty, order_status);*//*


                                  *//*  skulist.add(product);*//*
                                    *//*ArrayList<String> arr=   display_cart_details(product);*//*
                                       *//* if(imageurl==null){
                                            display_cart_details(product);
                                        }*//*


                                    order_history_inner_model = new Order_history_inner_model(product, "sku", "MALLINATH", sharedPref.getString("firstname", null), netweight, qty, order_status);
                                    details_list.add(order_history_inner_model);
                                    orderhistory_model.setArrayList(details_list);

                                }





                        }



                           *//* orderHistory_adapter = new OrderHistory_adapter(getContext(), productlist);
                            recyclerView.setAdapter(orderHistory_adapter);
                            recyclerView.setNestedScrollingEnabled(false);
                            orderhistory_model.setArrayList(details_list);*//*

                           if(pos1==order_listarray.length()) {
                               orderHistory_adapter = new OrderHistory_adapter(getContext(), productlist);
                               orderhistory_model.setArrayList(details_list);
                               recyclerView.setAdapter(orderHistory_adapter);
                               recyclerView.setNestedScrollingEnabled(false);
                           }


                        } catch (JSONException e) {
                            e.printStackTrace();

                                    no_order_image.setImageResource(R.drawable.no_order);
                            no_order_image.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() {


                String output=sharedPref.getString(ACCESS_TOKEN, null);
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                return params;
            }

        };
       *//* requestQueue.add(jsonArrayRequest);*//*

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonArrayRequest.setRetryPolicy(policy);
        RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }
    //////////////// fetching cart details//////////////////////////

    public void display_cart_details(final String product,final String netweight, final String qty, final String order_status) {

       *//* final String ids=skulist.get(pos2);
*//*
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                ip+"gate/b2b/order/api/v1/getProduct/"+product+"",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        try {
                            int k = 0;
                            JSONObject obj = new JSONObject(response);
                            JSONObject obj1 = obj.getJSONObject("_links");

                            JSONObject object=obj1.getJSONObject("imageURl");


                                 imageurl =object.getString("href");

                            Log.e("banthuu","yaaaaa");
                            Log.e("O",String.valueOf(pos1));


                           order_history_inner_model = new Order_history_inner_model(product, "sku", "MALLINATH", sharedPref.getString("firstname", null), netweight, qty, order_status);
                            details_list.add(order_history_inner_model);
                            orderhistory_model.setArrayList(details_list);





                               *//*  Log.e("OUTPUTTTT", String.valueOf(pos1));
                                 orderHistory_adapter = new OrderHistory_adapter(getContext(), productlist);
                                 orderhistory_model.setArrayList(details_list);
                                 recyclerView.setAdapter(orderHistory_adapter);
                                 recyclerView.setNestedScrollingEnabled(false);*//*


                               *//*orderhistory_model.setArrayList(details_list);*//*



                           *//* productlist.add(new Orderhistory_model(product,sku, "MALLINATH", shippedto, netweight, qty,order_status,image_url,orderno,order_date));


                            orderHistory_adapter = new OrderHistory_adapter(getContext(), productlist);
                            *//**//*recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));*//**//*
                            recyclerView.setAdapter(orderHistory_adapter);*//*

                          *//*  details_list.add(new Order_history_inner_model(product,sku,"MALLINATH",sharedPref.getString("firstname", null),"netwie","qty","status",imageurl));
                              ++pos1;
*//*
                          *//*  orderHistory_adapter = new OrderHistory_adapter(getContext(), productlist);
                            recyclerView.setAdapter(orderHistory_adapter);
                            recyclerView.setNestedScrollingEnabled(false);
                            orderhistory_model.setArrayList(details_list);
                            ++pos2;*//*
*//*
if(item_array.length()==pos1) {
    orderHistory_adapter = new OrderHistory_adapter(getContext(), productlist);
    recyclerView.setAdapter(orderHistory_adapter);
    recyclerView.setNestedScrollingEnabled(false);
    orderhistory_model.setArrayList(details_list);
}
*//*

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //displaying the error in toast if occurrs
                makeText(getContext(), error.getMessage(), LENGTH_SHORT).show();
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
        *//*RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        requestQueue.add(stringRequest);*//*

       int socketTimeout = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
        requestQueue1.add(stringRequest);


    }*/



    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }


}

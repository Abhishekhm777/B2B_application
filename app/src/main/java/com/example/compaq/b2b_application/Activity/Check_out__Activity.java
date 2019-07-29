package com.example.compaq.b2b_application.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.Checkout_adapter;
import com.example.compaq.b2b_application.Model.Check_out_Recyclemodel;
import com.example.compaq.b2b_application.Helper_classess.OrderPlaceAlert_DialogueManager;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.Helper_classess.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;

public class Check_out__Activity extends AppCompatActivity {
   /* public SharedPreferences pref;*/
    public SharedPreferences sharedPref;
    public   SharedPreferences.Editor myEditor;
    public String userid="";
    public JSONArray items;
    public String href="";
    public Check_out_Recyclemodel check_out_recyclemodel;
    public Checkout_adapter checkout_adapter;
    public ArrayList<Check_out_Recyclemodel> productlist;
    public RecyclerView recyclerView;
    public OrderPlaceAlert_DialogueManager alert = new OrderPlaceAlert_DialogueManager();
    Toolbar toolbar;
    public Button button;
    private SessionManagement session;
    private String manufacturename,manufacture_mob;
    public String cartid="";
    private  String output;
    private ProgressBar progressBar;
    private Dialog myDialog;
    private Dialog dialog;
    TextView details,shopmore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out__);
        toolbar=(Toolbar)findViewById(R.id.check_out_toolbar);
        toolbar.setTitle("Place Order");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new SessionManagement(getApplicationContext());
        progressBar=(ProgressBar)findViewById(R.id.progress);
        ////////////////////////////////////////////////////
        dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.congrats_layout);
        details=(TextView)dialog.findViewById(R.id.viewdetails) ;
        shopmore=(TextView)dialog.findViewById(R.id.continue_shopping) ;
        /* sharedPref =getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);*/
        sharedPref = getApplicationContext().getSharedPreferences("USER_DETAILS", 0);
        sharedPref=getSharedPreferences("USER_DETAILS",0);
         output=sharedPref.getString(ACCESS_TOKEN, null);
        myEditor = sharedPref.edit();
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.popup_layout);

        recyclerView = (RecyclerView)findViewById(R.id.checkout_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        recyclerView.setHasFixedSize(true);
        productlist=new ArrayList<>();
        getItem_ids();

        button=(Button)findViewById(R.id.place_orderbttn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(Check_out__Activity.this);
                alertDialog  .setTitle("Alert");
                alertDialog .setMessage("Do you want to Place this Order?");
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
                        progressBar.setVisibility(View.VISIBLE);
                        placeorder();
                    }
                }).show();
            }
        });
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
    public void getItem_ids() {
        /*  sharedPref = getSharedPreferences("User_information", 0);*/
        userid = sharedPref.getString("userid", "");
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonArrayRequest = new StringRequest(
                ip+"gate/b2b/order/api/v1/cart/customer/"+userid+"",
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        items=new JSONArray();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            cartid=jsonObject.getString("id");
                            JSONArray ja_data = jsonObject.getJSONArray("items");
                            for (int i = 0; i < ja_data.length(); i++) {
                                JSONObject jsonObject1 = ja_data.getJSONObject(i);

                                String desc = jsonObject1.getString("description");
                                String we = jsonObject1.getString("netWeight");
                                String pid = jsonObject1.getString("productID");
                                String produ = jsonObject1.getString("product");
                                String  manufacturename=jsonObject1.getString("manufactureName");
                                String  manufacture_mob=jsonObject1.getString("manufactureMobile");
                                String qty = jsonObject1.getString("quantity");
                                String purity=jsonObject1.getString("purity");
                                String size=jsonObject1.getString("size");
                                String length=jsonObject1.getString("length");
                                String image_url=jsonObject1.getString("productImage");
                                String name = jsonObject1.getString("name");
                                int delete_ids = Integer.parseInt(jsonObject1.getString("id"));

                                display_cart_details(pid, we, qty, delete_ids,desc,purity,size,length);
                                Thread.sleep(200);

                                JSONObject iem_objects = new JSONObject();
                                iem_objects.put("netWeight", we);
                                iem_objects.put("product", produ);
                                iem_objects.put("name", name);
                                iem_objects.put("productID", pid);
                                iem_objects.put("quantity", qty);
                                iem_objects.put("productImage", image_url);
                                iem_objects.put("paymentStatus", "PENDING");
                                iem_objects.put("expectedDeliveryDate", "");
                                iem_objects.put("seller", sharedPref.getString("Wholeseller_id", null));
                            /*   iem_objects.put("melting", "");
                                iem_objects.put("seal", "");
                                iem_objects.put("rate", "");
                                iem_objects.put("advance", "");*/
                                iem_objects.put("manufactureName", manufacturename);
                                iem_objects.put("manufactureMobile", manufacture_mob);
                                iem_objects.put("description", desc);
                                items.put(iem_objects);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        } catch (InterruptedException e) {
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

                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                return params;
            }
        };
        requestQueue.add(jsonArrayRequest);
    }
    //////////////// fetching cart details//////////////////////////
    public void display_cart_details(final String id,final String weght,final String qty,final int del_id,final String desc,final String purity,final  String size,final String length) {
        final StringRequest stringRequest = new StringRequest(Request.Method.GET,
                ip+"gate/b2b/catalog/api/v1/product/"+id+"?wholesaler="+userid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        button.setVisibility(View.VISIBLE);
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
                              /*  JSONObject obj2 = obj.getJSONObject("priceandseller");
                                JSONObject jsonObject1 = obj2.getJSONObject("price");
                                String new_price = jsonObject1.getString("productFinalPrice");
                                String old_price = jsonObject1.getString("productPrice");*/


                              /*  total_price = (Math.round((total_price + (Double.parseDouble(new_price))) * 100D) / 100D);

                                Log.d("price", total_price + "");*/


                            productlist.add(new Check_out_Recyclemodel(name, href, weght, product_id, qty,del_id,desc,purity,size,length));

                            checkout_adapter = new Checkout_adapter(getApplicationContext(), productlist);
                            /*recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));*/
                            recyclerView.setAdapter(checkout_adapter);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //displaying the error in toast if occurrs
                makeText(getApplicationContext(), error.getMessage(), LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() {

                sharedPref=getSharedPreferences("USER_DETAILS",0);

                String output=sharedPref.getString(ACCESS_TOKEN, null);
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public  void placeorder(){

            JSONObject place_object= new JSONObject();

            try {

                place_object.put("items",items);
                place_object.put("customer",userid);
                place_object.put("paymentStatus","PENDING");
                place_object.put("orderType","ONLINE_SALES_ORDER");
                place_object.put("consigneeName","");
                place_object.put("consigneeEmail","");
                place_object.put("consigneeNumber","");


            } catch (JSONException e) {
                e.printStackTrace();
            }
           /* Map<String, String> params = new HashMap<>();
            params.put("items", String.valueOf(items));
            params.put("customer", userid);
            params.put("paymentStatus", "PENDING");*/
               Log.e("placeeeee", String.valueOf(place_object));
            String url=ip+"gate/b2b/order/api/v1/order/add";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, place_object, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                            progressBar.setVisibility(View.GONE);
                    try {
                        response.getString("orderStatus");


                        clearCart(cartid);
                    }

                    // Go to next activity

                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
                    NetworkResponse response = error.networkResponse;


                    if (response != null && response.data != null) {
                        switch (response.statusCode) {
                            case 404:
                                BottomSheet.Builder builder = new BottomSheet.Builder(Check_out__Activity.this);
                                builder.setTitle("Sorry! could't reach server");
                                builder.show();
                                break;
                            case 400:
                                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                myDialog.show();
                                break;
                            case 401:
                                Toast.makeText(getApplicationContext(), "Session Expired!", Toast.LENGTH_SHORT).show();
                                session.logoutUser(Check_out__Activity.this);
                        }

                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headr = new HashMap<>();
                    sharedPref=getSharedPreferences("USER_DETAILS",0);

                    String output=sharedPref.getString(ACCESS_TOKEN, null);
                    headr.put("Authorization","bearer "+output);
                    headr.put("Content-Type", "application/json");
                    return headr;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(request);
        }

    public void clearCart(String id){

        String url=ip+"gate/b2b/order/api/v1/cart/delete/"+id;
        StringRequest stringRequest=new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public
            void onResponse(String response) {

                try {
                    sharedPref =getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                    myEditor = sharedPref.edit();
                    myEditor.putString("cartid","0");
                    myEditor.putString("no_of_items", "0");
                    myEditor.apply();
                    myEditor.commit();
                       try {
                           MainActivity mActivity = new MainActivity();
                           mActivity.setupBadge(getApplicationContext());
                       }
                       catch (NullPointerException e){
                           e.printStackTrace();
                       }
                   /* alert.showAlertDialog(Check_out__Activity.this, "Congratulations.", "Your order placed Successfully.", true);*/
                    dialog.show();
                    details.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent i = new Intent(getApplicationContext(), Orders_History_Activity.class);
                            startActivity(i);
                        }
                    });

                    shopmore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            getApplicationContext(). startActivity(i);
                            Check_out__Activity.this.finish();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public
            void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Could't Place your order.Try again.",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() {

                sharedPref=getApplicationContext().getSharedPreferences("USER_DETAILS",0);

                String output=sharedPref.getString(ACCESS_TOKEN, null);

                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);

                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}


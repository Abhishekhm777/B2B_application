package com.example.compaq.b2b_application.Activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.Cart_recycler_Adapter;
import com.example.compaq.b2b_application.Helper_classess.AlertDialogManager;
import com.example.compaq.b2b_application.Helper_classess.ConnectivityStatus;
import com.example.compaq.b2b_application.Model.Cart_recy_model;
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

import static android.widget.Toast.makeText;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;

public class Cart_Activity extends AppCompatActivity {
    public Toolbar toolbar;
    public static TextView pricetext_view;
    public RecyclerView recyclerView;
    public Cart_recy_model cart_recy_model;
    public Cart_recycler_Adapter cart_recycler_adapter;
    public ArrayList<Cart_recy_model> productlist;
   /* SharedPreferences pref;*/
    public Bundle bundle;
    public String userid="";
    public SharedPreferences sharedPref;
    public   SharedPreferences.Editor myEditor;
    public    ArrayList<String>product_id;
    public    ArrayList<String>seal_list;
    public    ArrayList<String>weigh_list;
    public    ArrayList<String>qty_list;
    public String prod_id="";
    public String href="";
    public String description="";
    public String wieght="";
    public String qty="";
    public JSONArray ja_data;
    public ImageView empty_imageview;
    public double total_price=0;
    AlertDialogManager alert = new AlertDialogManager();
    public Button check_out;
    ProgressBar progressBar;
    SessionManagement session;
   public String output;
    public CardView cardView;
    Button emptybtn,clear_bag;
    public TextView total_textview;
    private SwipeRefreshLayout swipeContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_);
        toolbar=(Toolbar)findViewById(R.id.cart_toolbar);
        toolbar.setTitle("My Bag");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       /* pricetext_view=(TextView)findViewById(R.id.total_price);*/
        session = new SessionManagement(getApplicationContext());
        progressBar=(ProgressBar)findViewById(R.id.progress);
        sharedPref =getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        myEditor = sharedPref.edit();

        cardView=(CardView)findViewById(R.id.cardvie);
        total_textview=(TextView)findViewById(R.id.total_weight);
        empty_imageview=findViewById(R.id.cart_emppty);
        /*clear_bag=(Button)findViewById(R.id.clear_bag);
        clear_bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               productlist.removeAll(productlist);
                cart_recycler_adapter.notifyDataSetChanged();
                cart_recycler_adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onItemRangeRemoved(int positionStart, int itemCount) {
                        super.onItemRangeRemoved(positionStart, itemCount);

                        if(cart_recycler_adapter.getItemCount()==0){
                            recyclerView.setVisibility(View.GONE);


                            emptybtn.setVisibility(View.VISIBLE);
                            empty_imageview.setImageResource(R.drawable.emptycart);
                            empty_imageview.setVisibility(View.VISIBLE);

                        }

                    }

                    @Override
                    public void onChanged() {
                        super.onChanged();

                    }

                });
            }
        });*/



        //////////////////////////////////////////////////////////////
     /*   swipeContainer = (SwipeRefreshLayout)findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                productlist.clear();
                getItem_ids();

            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);*/


  /////////////////////////////////////////////////////////////////////////////////////////////////




         emptybtn=findViewById(R.id.shop_nowbtn);
         emptybtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                onBackPressed();
                 Cart_Activity.this.finish();
             }
         });
        check_out=(Button)findViewById(R.id.check_out_btn);
        check_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Fragment checkfragment=new Check_out_fragment();
                FragmentManager fragmentManager=getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contaner, checkfragment).addToBackStack(null);;
                fragmentTransaction.commit();*/
                String noOfitems=sharedPref.getString("no_of_items","");

                if (noOfitems.equalsIgnoreCase("0"))
                {
                    Toast.makeText(getApplicationContext(),"Your Bag is empty!!",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent i = new Intent(getApplicationContext(), Check_out__Activity.class);
                    startActivity(i);
                }
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.cart_recycler);
        productlist=new ArrayList<>();
        seal_list=new ArrayList<>();
        weigh_list=new ArrayList<>();
        qty_list=new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);


        sharedPref=getSharedPreferences("USER_DETAILS",0);

         output=sharedPref.getString(ACCESS_TOKEN, null);

        getItem_ids();


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

    public  void getItem_ids() {

       /* sharedPref = getSharedPreferences("User_information", 0);*/

        userid = sharedPref.getString("userid", "");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        progressBar.setVisibility(View.VISIBLE);
        StringRequest jsonArrayRequest = new StringRequest(
                ip+"gate/b2b/order/api/v1/cart/customer/"+userid+"",
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        product_id=new ArrayList<>();


                        try {
                            JSONObject jsonObject = new JSONObject(response);

                             ja_data = jsonObject.getJSONArray("items");
                             Log.e("length...", String.valueOf(ja_data.length()));

                            if (ja_data.length()==0)
                            {

                                empty_imageview.setImageResource(R.drawable.emptycart);
                                emptybtn.setVisibility(View.VISIBLE);
                                cardView.setVisibility(View.GONE);
                                check_out.setVisibility(View.GONE);
                                return;
                            }
                            for (int i = 0; i < ja_data.length(); i++) {
                                JSONObject jsonObject1 = ja_data.getJSONObject(i);

                                String qty=jsonObject1.getString("quantity");
                                String we=jsonObject1.getString("netWeight");
                                String pid=jsonObject1.getString("productID");
                                String description=jsonObject1.getString("description");
                                String purity=jsonObject1.getString("purity");
                                String size=jsonObject1.getString("size");
                                String length=jsonObject1.getString("length");
                                String seller=jsonObject1.getString("seller");
                                String expected=jsonObject1.getString("expectedDeliveryDate");


                               int delete_ids= Integer.parseInt(jsonObject1.getString("id"));



                                   /* display_cart_details(seller,pid,we,qty,delete_ids,description,purity,size,length);*/
                                seller_name(seller,pid,we,qty,delete_ids,description,purity,size,length,expected);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                            empty_imageview.setImageResource(R.drawable.emptycart);


                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                progressBar.setVisibility(View.GONE);
                cardView.setVisibility(View.GONE);
                check_out.setVisibility(View.GONE);
                empty_imageview.setImageResource(R.drawable.emptycart);
                emptybtn.setVisibility(View.VISIBLE);

                NetworkResponse response = volleyError.networkResponse;


                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 401:
                            Toast.makeText(getApplicationContext(),"Session Expired!",Toast.LENGTH_SHORT).show();
                            session.logoutUser(MainActivity.class.cast(Cart_Activity.this));

                    }
                }
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

    ///////////////////////////////////////////seller//////////////////////////////////


    public void seller_name ( final String seller,final String id,final String weght,final String qty,final int del_id,final String description,final String purity,final  String size,final String length,final String expected){

        String url=ip1+"/b2b/api/v1/user/get/"+seller;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONObject jsonObject=jObj.getJSONObject("company");
                    String name=jsonObject.getString("name");

                    display_cart_details(id,weght,qty,del_id,description,purity,size,length,name,expected);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                params.put("Content-Type", "application/json");
                return params;
            }
        };




        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    //////////////// fetching cart details//////////////////////////

    public void display_cart_details(final String id,final String weght,final String qty,final int del_id,final String description,final String purity,final  String size,final String length,final  String seller_name,final String expected) {

        final ProgressDialog progressDialog=new ProgressDialog(Cart_Activity.this);
        progressDialog.setTitle("Loading....");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    ip+"gate/b2b/catalog/api/v1/product/"+id+"?wholesaler="+userid,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            progressDialog.dismiss();
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



                                productlist.add(new Cart_recy_model(name, href, weght, product_id, qty,del_id,description,purity,size,length,seller_name,expected));


                                cart_recycler_adapter = new Cart_recycler_Adapter(Cart_Activity.this, productlist,getSupportFragmentManager());
                                cart_recycler_adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                                    @Override
                                    public void onItemRangeRemoved(int positionStart, int itemCount) {
                                        super.onItemRangeRemoved(positionStart, itemCount);

                                        if(cart_recycler_adapter.getItemCount()==0){
                                            recyclerView.setVisibility(View.GONE);

                                            check_out.setVisibility(View.GONE);
                                            cardView.setVisibility(View.GONE);
                                            emptybtn.setVisibility(View.VISIBLE);
                                            empty_imageview.setImageResource(R.drawable.emptycart);
                                            empty_imageview.setVisibility(View.VISIBLE);

                                        }


                                    }

                                    @Override
                                    public void onChanged() {
                                        super.onChanged();

                                    }


                                    void checkEmpty() {



                                        empty_imageview.setImageResource(R.drawable.emptycart);
                                        empty_imageview.setVisibility(cart_recycler_adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                                    }

                                });
                                    /*recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));*/
                                    recyclerView.setAdapter(cart_recycler_adapter);
                                    check_out.setVisibility(View.VISIBLE);



                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }


                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //displaying the error in toast if occurrs

                    progressDialog.dismiss();
                    NetworkResponse response = error.networkResponse;
                    Log.e("Status" , String.valueOf(response.statusCode));
                    if(error != null ){
                        switch(response.statusCode){
                            case 404:
                                check_out.setVisibility(View.GONE);
                                BottomSheet.Builder builder = new BottomSheet.Builder(Cart_Activity.this);
                                builder.setTitle("Sorry! could't reach server");
                                builder.show();
                                break;

                            case 401:
                                BottomSheet.Builder builder1 = new BottomSheet.Builder(Cart_Activity.this);
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
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            requestQueue.add(stringRequest);
        }





    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(!ConnectivityStatus.isConnected(getApplicationContext())){
                alert.showAlertDialog(Cart_Activity.this, "No Internet!", "Please Check your internet Connection", "internet");
            }else {
                // connected

            }
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        getApplicationContext().registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    @Override
    protected void onPause() {
        super.onPause();
        getApplicationContext().registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


    }
public  void reloadData(){
    Intent intent = getIntent();
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    finish();
    startActivity(intent);


}


 public void  totoal_weight(String msg){

        total_textview.setText(msg+"gms");

    }

    }



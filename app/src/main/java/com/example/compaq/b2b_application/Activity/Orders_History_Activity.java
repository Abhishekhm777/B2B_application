package com.example.compaq.b2b_application.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.OrderHistoryAdapter;
import com.example.compaq.b2b_application.Model.OrderImageChild;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static android.widget.Toast.makeText;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;

public class Orders_History_Activity extends AppCompatActivity {
    private ExpandableListView expandableListView;
    public SharedPreferences sharedPref;
    private static String USER_ID, ACCESS_TOKEN, sku, mob, email;
    private ArrayList<String> sku_list;
    private ArrayList<String> seller_id;
    private ArrayList<Orderhistory_model> orderNumList;
    private Map<String, List<Order_history_inner_model>> orderChildMap;
    private Map<String, List<OrderImageChild>> imageChildMap;
    LinkedHashMap<String, String> sellerNameList;
    List<OrderImageChild> imagechildlist;
    OrderHistoryAdapter orderHistoryAdapter;
    public String strDate;
    public String lastdate;
    String imgUrl;
    int inn = 0;
    Dialog dialog;
    int inn2 = 0;
    String oder_type;
    public String output,user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders__history_);


        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.orderhistory_toolbar);
        setSupportActionBar(myChildToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order History");

        sharedPref = getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        sharedPref.getString(ACCESS_TOKEN, null);
        output = sharedPref.getString("acc_token", null);
        user_id=sharedPref.getString("userid", null);


        dialog = new Dialog(Orders_History_Activity.this);
        dialog.setContentView(R.layout.progress_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        expandableListView = (ExpandableListView) findViewById(R.id.order_expand);
        sellerNameList = new LinkedHashMap<>();
        orderNumList = new ArrayList<>();
        orderChildMap = new TreeMap<>();
        sku_list = new ArrayList<>();
        seller_id = new ArrayList<>();
        imageChildMap = new TreeMap<>();


        Calendar cal = GregorianCalendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, +1);
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, +1);
        Date today = cal.getTime();
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
        getOrderHistoryt();
    }


    public void getOrderHistoryt() {
        dialog.show();
        Log.d("url...",ip+"gate/b2b/order/api/v1/order/user/" + sharedPref.getString("userid", null) + "?fromDate=" + lastdate + "&toDate=" + strDate + "");
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                ip + "gate/b2b/order/api/v1/order/user/"+user_id+ "?fromDate=" + lastdate + "&toDate=" + strDate + "",

                new Response.Listener<String>() {
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject embedobbject = jsonObject.getJSONObject("_embedded");
                            JSONArray order_listarray = embedobbject.getJSONArray("orderList");


                            for (int i = 0; i < order_listarray.length(); i++) {

                                JSONObject jsonObject1 = order_listarray.getJSONObject(i);

                                String orderno = jsonObject1.getString("orderNo");

                                String type = jsonObject1.getString("orderType");
                                if (type.equalsIgnoreCase("ONLINECUSTOM_ORDER")) {

                                      oder_type="CUSTOM ORDER";
                                }
                                else {
                                     oder_type="REGULAR ORDER";
                                }

                                String s1 = jsonObject1.getString("createdDate");

                                String[] order_date = s1.split("T");
                                String order_date1 = order_date[0];

                                JSONArray item_array = jsonObject1.getJSONArray("items");
                                final List<Order_history_inner_model> childlist = new ArrayList<Order_history_inner_model>();

                                for (int j = 0; j < item_array.length(); j++) {

                                    JSONObject item_object = item_array.getJSONObject(j);

                                    String product = item_object.getString("product");
                                    String custom_name = item_object.getString("name");
                                    String seller = item_object.getString("seller");

                                    if(oder_type.equalsIgnoreCase("CUSTOM ORDER")){
                                        imagechildlist = new ArrayList<OrderImageChild>();
                                        imagechildlist.add(new OrderImageChild(custom_name, item_object.getString("productImage")));
                                        imageChildMap.put(custom_name, imagechildlist);

                                        String qty = item_object.getString("quantity");

                                        String netweight = item_object.getString("netWeight");

                                        String order_status = item_object.getString("itemStatus");
                                        String purity = item_object.getString("purity");
                                        String delivered = item_object.getString("totalWeight");
                                        childlist.add(new Order_history_inner_model(custom_name, custom_name, purity, sharedPref.getString("firstname", null), netweight, delivered, qty, order_status, seller,oder_type));
                                        if (seller_id.contains(seller) == false) {
                                            Log.e("Seller_in", seller);
                                            seller_id.add(seller);
                                        }
                                       continue;
                                    }
                                    else {


                                        if (sku_list.contains(product) == false) {
                                            sku_list.add(product);
                                        }
                                        if (seller_id.contains(seller) == false) {
                                            Log.e("Seller_in", seller);
                                            seller_id.add(seller);
                                        }
                                    }

                                    String qty = item_object.getString("quantity");

                                    String netweight = item_object.getString("netWeight");

                                    String order_status = item_object.getString("itemStatus");
                                    String purity = item_object.getString("purity");
                                    String delivered = item_object.getString("totalWeight");
                                    childlist.add(new Order_history_inner_model(product, product, purity, sharedPref.getString("firstname", null), netweight, delivered, qty, order_status, seller,oder_type));

                                }

                                orderNumList.add(new Orderhistory_model(orderno, order_date1));
                                orderChildMap.put(orderno, childlist);


                            }
                            getSellerName();
                            getImagess();

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
                return params;
            }

        };
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        requestQueue1.add(stringRequest);
    }


    public void getImagess() {
        imagechildlist = new ArrayList<OrderImageChild>();
        if (inn < sku_list.size()) {
            sku = sku_list.get(inn);
            Log.d("size11...", sku_list.size() + "   " + sku);
            imgUrl = ip + "gate/b2b/order/api/v1/getProduct/"+sku+"?wholesaler="+user_id;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, imgUrl
                    ,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject obj1 = new JSONObject(response);
                                String name = obj1.getString("name");
                                /*   imagechildlist  = new ArrayList<OrderImageChild>();*/
                                JSONObject links = obj1.getJSONObject("_links");
                                String img_url = "";
                                Object intervention = links.get("imageURl");
                                if (intervention instanceof JSONArray) {
                                    // It's an array
                                    JSONArray img_arr = (JSONArray) intervention;
                                    Log.d("json arr", "......................Array");
                                    JSONObject img_obj = img_arr.getJSONObject(0);
                                    img_url = img_obj.getString("href");
                                    Log.d("json arr11", img_url);
                                } else if (intervention instanceof JSONObject) {
                                    // It's an object
                                    JSONObject img_link = (JSONObject) intervention;
                                    Log.d("json obj", "..................Obj");
                                    img_url = img_link.getString("href");
                                    Log.d("json obj11", img_url);
                                }

                                Log.d("name.......", name + sku_list.get(inn));
                                imagechildlist.add(new OrderImageChild(name, img_url));
                                imageChildMap.put(sku_list.get(inn).toString(), imagechildlist);
                                inn++;

                                Log.d("array.......", sku_list.size() + "......." + imageChildMap.size());
                                if (sku_list.size() == imageChildMap.size()) {
                                    dialog.dismiss();
                                    Log.d("done.......", "......................total");
                                    orderHistoryAdapter = new OrderHistoryAdapter(getApplicationContext(), orderNumList, orderChildMap, imageChildMap, sellerNameList);
                                    expandableListView.setAdapter(orderHistoryAdapter);

                                }
                                getImagess();


                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //displaying the error in toast if occurrs
                            imagechildlist.add(new OrderImageChild("", ""));
                            imageChildMap.put(sku_list.get(inn).toString(), imagechildlist);
                            inn++;
                            getImagess();
                            if (sku_list.size() == imageChildMap.size()) {
                                dialog.dismiss();
                                Log.d("done.......", "......................total");
                                orderHistoryAdapter = new OrderHistoryAdapter(getApplicationContext(), orderNumList, orderChildMap, imageChildMap, sellerNameList);
                                expandableListView.setAdapter(orderHistoryAdapter);

                            }

                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "bearer " + output);
                    return params;
                }

            };


            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
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
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }

    }



    public void getSellerName() {
        if (inn2 < seller_id.size()) {
            String seller_Url = ip+"gate/b2b/order/api/v1/getUser/"+seller_id.get(inn2);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, seller_Url
                    ,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                String firstName = jsonObject.getString("firstName");
                                String lastName = jsonObject.getString("lastName");

                                sellerNameList.put(seller_id.get(inn2), firstName + " " + lastName);
                                inn2++;
                                getSellerName();

                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "bearer " + output);
                    return params;
                }

            };


            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}

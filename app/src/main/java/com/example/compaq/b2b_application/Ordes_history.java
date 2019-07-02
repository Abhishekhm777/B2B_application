package com.example.compaq.b2b_application;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.OrderHistoryAdapter;
import com.example.compaq.b2b_application.Model.OrderImageChild;
import com.example.compaq.b2b_application.Model.Order_history_inner_model;
import com.example.compaq.b2b_application.Model.Orderhistory_model;

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

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.example.compaq.b2b_application.MainActivity.ip;
import static com.example.compaq.b2b_application.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.SessionManagement.PREF_NAME;

public class Ordes_history extends AppCompatActivity {
    private ExpandableListView expandableListView;
    private String dates, tommrowdate;
    public  SharedPreferences sharedPref;
    SharedPreferences.Editor myEditior;

    private static String USER_ID, ACCESS_TOKEN, sku, mob, email;
    private ArrayList<String> sku_list;
    private ArrayList<String> seller_id;
    LinkedHashMap<String, String> sellerNameList;
    LinkedHashMap<String, String> trackMap;
    private ArrayList<Orderhistory_model> orderNumList;
    private Map<String, List<Order_history_inner_model>> orderChildMap;
    private Map<String, List<OrderImageChild>> imageChildMap;
    OrderHistoryAdapter orderHistoryAdapter;
    public String strDate;
    public String lastdate;
    String imgUrl;
    int inn = 0;
    int inn2 = 0;
    public String output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordes_history);


        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.orderhistory_toolbar);
        setSupportActionBar(myChildToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order History");

        sharedPref =getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        expandableListView = (ExpandableListView) findViewById(R.id.order_expand);

        orderNumList = new ArrayList<>();
        orderChildMap = new TreeMap<String, List<Order_history_inner_model>>();
        sku_list = new ArrayList<>();
        seller_id = new ArrayList<>();
        sellerNameList = new LinkedHashMap<>();
        imageChildMap = new TreeMap<>();
        trackMap = new LinkedHashMap<>();


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
       /* getOrderHistoryt();*/
    }

/*
    public void getOrderHistoryt() {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                ip+"gate/b2b/order/api/v1/order/user/"+ sharedPref.getString("userid",null)+"?fromDate="+lastdate + "&toDate=" + strDate + "",

                new Response.Listener<String>() {
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject embedobbject = jsonObject.getJSONObject("_embedded");
                            JSONArray order_listarray = embedobbject.getJSONArray("orderList");


                            for (int i = 0; i < order_listarray.length(); i++) {

                                JSONObject jsonObject1 = order_listarray.getJSONObject(i);

                                String orderno = jsonObject1.getString("orderNo");

                                String order_status = jsonObject1.getString("orderStatus");

                                String s1 = jsonObject1.getString("createdDate");

                                String[] order_date = s1.split("T");
                                String order_date1 = order_date[0];

                                JSONArray item_array = jsonObject1.getJSONArray("items");
                                final List<Order_history_inner_model> childlist = new ArrayList<Order_history_inner_model>();

                                for (int j = 0; j < item_array.length(); j++) {

                                    JSONObject item_object = item_array.getJSONObject(j);

                                    String product = item_object.getString("product");
                                    if (sku_list.contains(product) == false) {
                                        sku_list.add(product);
                                    }
                                    String qty = item_object.getString("quantity");

                                    String netweight = item_object.getString("netWeight");


                                    childlist.add(new Order_history_inner_model(product, "sku", "MALLINATH", sharedPref.getString("firstname", null), netweight, qty, order_status));

                                }

                                orderNumList.add(new Orderhistory_model(orderno, order_date1));
                                orderChildMap.put(orderno, childlist);


                            }

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

                output=sharedPref.getString(ACCESS_TOKEN, null);

                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "bearer "+output);
                return params;
            }

        };
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        requestQueue1.add(stringRequest);
    }


    public void getImagess() {

        if (inn < sku_list.size()) {
            sku = sku_list.get(inn);
            Log.d("size11...", sku_list.size() + "   " + sku);
            imgUrl = ip+"gate/b2b/order/api/v1/getProduct/"+ sku;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, imgUrl
                    ,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject obj1 = new JSONObject(response);
                                String name = obj1.getString("name");
                                List<OrderImageChild> imagechildlist = new ArrayList<OrderImageChild>();
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
                                getImagess();
                                Log.d("array.......", sku_list.size() + "......." + imageChildMap.size());
                                if (sku_list.size() == imageChildMap.size()) {
                                    Log.d("done.......", "......................total");
                                    orderHistoryAdapter = new OrderHistoryAdapter(getApplicationContext(), orderNumList, orderChildMap, imageChildMap);
                                    expandableListView.setAdapter(orderHistoryAdapter);

                                }


                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //displaying the error in toast if occurrs
                            makeText(getApplicationContext(), error.getMessage(), LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "bearer " + ACCESS_TOKEN);
                    return params;
                }

            };


            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }*/
}

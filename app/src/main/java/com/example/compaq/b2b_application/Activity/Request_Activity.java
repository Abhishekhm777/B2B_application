package com.example.compaq.b2b_application.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.Request_Adapter;
import com.example.compaq.b2b_application.Helper_classess.AlertDialogManager;
import com.example.compaq.b2b_application.Model.Request_model;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.Helper_classess.SessionManagement;

import org.json.JSONArray;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;

public class Request_Activity extends AppCompatActivity {
    public Toolbar toolbar;
    public RecyclerView recyclerView;
    public Request_Adapter request_adapter;
    public ArrayList<Request_model> productlist;
    /* SharedPreferences pref;*/
    public Bundle bundle;
    public String userid="";
    public SharedPreferences sharedPref;
    public   SharedPreferences.Editor myEditor;

    public String prod_id="";
    public String href="";
    public String description="";
    public String wieght="";
    public String qty="";
    private String output,wholseller_id;
    public JSONArray ja_data;
    public ImageView empty_imageview;
    public double total_price=0;
    AlertDialogManager alert = new AlertDialogManager();
    public Button check_out;
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_);

        toolbar= findViewById(R.id.cart_toolbar);
        toolbar.setTitle("Manage Requests");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPref =getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        recyclerView = findViewById(R.id.request_recycler);
        productlist=new ArrayList<>();

        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        recyclerView.setHasFixedSize(true);
        sharedPref=getSharedPreferences("USER_DETAILS",0);
        wholseller_id = sharedPref.getString("userid", null);

         output=sharedPref.getString(ACCESS_TOKEN, null);


        request();
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
    public void request() {


        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                ip1+"/b2b/api/v1/relation/get",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject obj = new JSONObject(response);
                            JSONObject obj1 = obj.getJSONObject("_embedded");

                            JSONArray ja_data = obj1.getJSONArray("relationList");
                            for (int i = 0; i < ja_data.length(); i++) {
                                JSONObject jObj = ja_data.getJSONObject(i);

                                String id=jObj.getString("id");
                                String stats=jObj.getString("status");
                                JSONObject setting=jObj.getJSONObject("setting");

                                JSONObject jsonObject=jObj.getJSONObject("_links");
                                JSONObject jsonObject1=jsonObject.getJSONObject("from_user");

                                String hrf=jsonObject1.getString("href");

                               String u_id=  hrf.substring(hrf.lastIndexOf('/') + 1);
                                if(!wholseller_id.equalsIgnoreCase(u_id)) {

                                    details_list(hrf, id, stats,setting.getString("id"));
                                }
                            }



                          /*  request_adapter = new Request_Adapter(getApplicationContext(), productlist);




                            request_adapter = new Request_Adapter(getApplicationContext(), productlist);
                            request_adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                                @Override
                                public void onItemRangeRemoved(int positionStart, int itemCount) {
                                    super.onItemRangeRemoved(positionStart, itemCount);

                                    if(request_adapter.getItemCount()==0){


                                    }


                                }

                                @Override
                                public void onChanged() {
                                    super.onChanged();

                                }


                                void checkEmpty() {



                                    empty_imageview.setImageResource(R.drawable.emptycart);
                                    empty_imageview.setVisibility(request_adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                                }

                            });
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                            recyclerView.setAdapter(request_adapter);
*/


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
                            BottomSheet.Builder builder = new BottomSheet.Builder(Request_Activity.this);
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;

                        case 401:
                            BottomSheet.Builder builder1 = new BottomSheet.Builder(Request_Activity.this);
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

    public void details_list(String url, final String id, final String status,final String settings) {


        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject obj = new JSONObject(response);

                            String company_name=obj.getString("companyName");

                            JSONObject jsonObject=obj.getJSONObject("_links");
                            JSONObject jsonObject1=jsonObject.getJSONObject("logo");

                            String logo=jsonObject1.getString("href");

                                productlist.add(new Request_model(company_name, id, status,logo,settings));


                            request_adapter = new Request_Adapter(Request_Activity.this, productlist);
                            request_adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                                @Override
                                public void onItemRangeRemoved(int positionStart, int itemCount) {
                                    super.onItemRangeRemoved(positionStart, itemCount);

                                    if(request_adapter.getItemCount()==0){
                                        recyclerView.setVisibility(View.GONE);



                                    }


                                }

                                @Override
                                public void onChanged() {
                                    super.onChanged();

                                }


                                void checkEmpty() {



                                    empty_imageview.setImageResource(R.drawable.emptycart);
                                    empty_imageview.setVisibility(request_adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                                }

                            });

                            recyclerView.setAdapter(request_adapter);
                            check_out.setVisibility(View.VISIBLE);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //displaying the error in toast if occurrs


                NetworkResponse response = error.networkResponse;

                if(error != null ){
                    switch(response.statusCode){
                        case 404:

                            BottomSheet.Builder builder = new BottomSheet.Builder(Request_Activity.this);
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;

                        case 401:
                            BottomSheet.Builder builder1 = new BottomSheet.Builder(Request_Activity.this);
                            builder1.setTitle("Session expiered!!");
                            builder1.show();


                    }
                }
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
    }
}

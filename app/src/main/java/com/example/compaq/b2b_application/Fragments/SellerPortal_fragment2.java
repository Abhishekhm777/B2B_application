package com.example.compaq.b2b_application.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.compaq.b2b_application.Adapters.Seller_portal_fragment1Adapter;
import com.example.compaq.b2b_application.Adapters.Seller_portal_fragment2Adapter;
import com.example.compaq.b2b_application.LoginActivity;
import com.example.compaq.b2b_application.Model.SellerPortal_model;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static com.example.compaq.b2b_application.MainActivity.ip1;
import static com.example.compaq.b2b_application.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.SessionManagement.PREF_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerPortal_fragment2 extends Fragment {

    public RecyclerView sellser_recycler;
    public SellerPortal_model sellerPortal_model;
    public Seller_portal_fragment2Adapter seller_portal_fragment2_adapter;
    public ArrayList<SellerPortal_model> productlist;
    public SharedPreferences sharedPref;
    public String output;
    public String seller_id,sellerurl;
    SharedPreferences.Editor myEditior;
    ProgressBar progressBar;
    private String wholse_mob;
    private String user;
    private String companyname,imageurl;
    TextView textView;
    int k=0;

    public SellerPortal_fragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_seller_portal_fragment2, container, false);

        productlist=new ArrayList<>();
        sellser_recycler = (RecyclerView)view.findViewById(R.id.seller_recycler2);
        sellser_recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));


        progressBar= view.findViewById(R.id.progress);
        sharedPref=getActivity().getApplicationContext().getSharedPreferences("USER_DETAILS",0);
         user=sharedPref.getString("userid",null);
         textView=(TextView)view.findViewById(R.id.wholesalers_text);
        textView.setVisibility(View.VISIBLE);



        return  view;
    }
    public void sellerLogo ( ){
        progressBar.setVisibility(View.VISIBLE);

        String url=ip1+"/b2b/api/v1/user/users?role=ROLE_WHOLESALER&product=Jewellery";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {



                    JSONObject jObj = new JSONObject(response);

                    JSONObject embedeobject=jObj.getJSONObject("_embedded");
                    JSONArray user_array=embedeobject.getJSONArray("userList");

                    for(int i=0;i<user_array.length();i++){

                        JSONObject object=user_array.getJSONObject(i);


                       seller_id= object.getString("id");
                        if(user.equalsIgnoreCase(seller_id)){
                            continue;
                        }

                        String seller_name=object.getString("firstName");


                         wholse_mob=object.getString("mobileNumber");


                        JSONObject comanyarray=object.getJSONObject("_links");

                        JSONObject urlobject=comanyarray.getJSONObject("company");

                         sellerurl=urlobject.getString("href");

                        checkhStatus(seller_id,wholse_mob,sellerurl);


                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                NetworkResponse response = error.networkResponse;


                if(response != null && response.data != null){
                    switch(response.statusCode){

                        case 404:
                            Snackbar.make(getView(), "Sorry! Could't login.Try agian after sometime", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            break;
                        case 400:

                            break;
                        case 500:

                            Snackbar.make(getView(), "Sorry! Could't reach server", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();

                    }
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() {



                output=sharedPref.getString(ACCESS_TOKEN, null);

                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                return params;
            }
        };




        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }



    public void getLogo (String url, final String seller_id , final String wholeseller_mob){


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject jObj = new JSONObject(response);

                     companyname=jObj.getString("name");
                    JSONObject comanyarray=jObj.getJSONObject("_links");


                        JSONObject urlobject = comanyarray.getJSONObject("logo");

                         imageurl = urlobject.getString("href");





                } catch (JSONException e) {
                    e.printStackTrace();
                }
               finally {
                    productlist.add(new SellerPortal_model(imageurl, companyname, seller_id, wholeseller_mob));


                    seller_portal_fragment2_adapter = new Seller_portal_fragment2Adapter(getActivity(), productlist,getActivity().getSupportFragmentManager());

                    seller_portal_fragment2_adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                        @Override
                        public void onItemRangeRemoved(int positionStart, int itemCount) {
                            super.onItemRangeRemoved(positionStart, itemCount);

                            if(seller_portal_fragment2_adapter.getItemCount()>0){


                               progressBar.setVisibility(View.GONE);
                                textView.setVisibility(View.GONE);
                                Log.e("yahoooo","SIZE");




                            }


                        }

                        @Override
                        public void onChanged() {
                            super.onChanged();

                            Log.e("yahoooo","change");
                            textView.setVisibility(View.VISIBLE);
                        }


                        void checkEmpty() {

                        }

                    });
                    /*recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));*/
                    sellser_recycler.setAdapter(seller_portal_fragment2_adapter);
                    if(seller_portal_fragment2_adapter.getItemCount()>0){


                        progressBar.setVisibility(View.GONE);
                        textView.setVisibility(View.GONE);
                        Log.e("yahoooo","SIZE");




                    }
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
                return params;
            }
        };




        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    RequestQueue requestQueue;
    public void checkhStatus (final String seller,final String whole,final String hrf){
        if(requestQueue==null){
            requestQueue = Volley.newRequestQueue(getContext());
        }

        progressBar.setVisibility(View.GONE);


        String url=ip1+"/b2b/api/v1/relation/check/"+user+"/"+seller;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);

                    if(jObj.getString("status").equalsIgnoreCase("ACCEPTED")) {

                        Log.e("frag2","ACCEPTEDDD");
                        getLogo(hrf, seller,whole);
                    }



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
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            productlist.clear();
            sellerLogo();





        }


    }



}

package com.example.compaq.b2b_application.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.Cart_recycler_Adapter;
import com.example.compaq.b2b_application.Adapters.Seller_portal_fragment1Adapter;
import com.example.compaq.b2b_application.LoginActivity;
import com.example.compaq.b2b_application.MainActivity;
import com.example.compaq.b2b_application.Model.Cart_recy_model;
import com.example.compaq.b2b_application.Model.SellerPortal_model;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.SellerPortal_Activity;
import com.example.compaq.b2b_application.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.widget.GridLayout.HORIZONTAL;
import static com.example.compaq.b2b_application.MainActivity.ip;
import static com.example.compaq.b2b_application.MainActivity.ip1;
import static com.example.compaq.b2b_application.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.SessionManagement.PREF_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerPortal_fragment1 extends Fragment {

    public RecyclerView sellser_recycler,sellser_recycler2;
    public SellerPortal_model sellerPortal_model;
    public Seller_portal_fragment1Adapter seller_portal_fragment1_adapter;
    public ArrayList<SellerPortal_model> productlist;
    public ArrayList<SellerPortal_model> productlist2;
    public SharedPreferences sharedPref;
    public String output;
    SessionManagement session;
    SharedPreferences.Editor myEditior;
/*
private String companyname;
*/
    private String imageurl;

    private String companyname2;
    private String imageurl2;
    TextView bset_manu;
    SearchView searchView;
Dialog dialog;
    public SellerPortal_fragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_seller_portal_fragment1, container, false);

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout. progress_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        session = new SessionManagement(getActivity().getApplicationContext());
        productlist=new ArrayList<>();
        productlist2=new ArrayList<>();
        sellser_recycler = (RecyclerView)view.findViewById(R.id.seller_recycler1);
        sellser_recycler2 = (RecyclerView)view.findViewById(R.id.seller_recycler122);
        sharedPref =getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        myEditior = sharedPref.edit();
        bset_manu=(TextView)view.findViewById(R.id.bestmanu_textview);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext().getApplicationContext(), 1, GridLayoutManager.HORIZONTAL, false);
        sellser_recycler.setLayoutManager(mGridLayoutManager);
        sellser_recycler.setItemAnimator(new DefaultItemAnimator());


        GridLayoutManager mGridLayoutManager1 = new GridLayoutManager(getContext().getApplicationContext(), 1, GridLayoutManager.HORIZONTAL, false);
        sellser_recycler2.setLayoutManager(mGridLayoutManager1);
        sellerLogo();

        String role=sharedPref.getString("role", null);
        if(role.equalsIgnoreCase("ROLE_WHOLESALER")){
            bset_manu.setVisibility(View.VISIBLE);
            manufacturer();
        }

        searchView=getActivity().findViewById(R.id.sellers_search);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.equalsIgnoreCase(null)) {
                    seller_portal_fragment1_adapter.getFilter().filter(newText);
                }

                return true;
            }
        });
   return  view;
    }

    public void sellerLogo ( ){
        dialog.show();

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

                       String  seller_id= object.getString("id");
                       String seller_name=object.getString("firstName");
                       String  wholseller_mob= object.getString("mobileNumber");


                      /* myEditior.putString("Seller_id",seller_id);
                       myEditior.putString("Seller_name",seller_name).apply();
                       myEditior.putString("Wholeseller_mob",wholseller_mob);
                       myEditior.apply();
                       myEditior.commit();*/


                       JSONObject comanyarray=object.getJSONObject("_links");

                       JSONObject urlobject=comanyarray.getJSONObject("company");

                       String sellerurl=urlobject.getString("href");

                       getLogo(sellerurl,seller_id,wholseller_mob);


                   }



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;


                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 404:
                            BottomSheet.Builder builder = new BottomSheet.Builder(getActivity());
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;

                        case 401:
                            Toast.makeText(getContext(),"Session Expired!",Toast.LENGTH_SHORT).show();
                            session.logoutUser(getContext());

                    }
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() {

                sharedPref=getActivity().getApplicationContext().getSharedPreferences("USER_DETAILS",0);

                 output=sharedPref.getString(ACCESS_TOKEN, null);

                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                return params;
            }
        };




        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }



    public void getLogo (String url, final String sellerid,final String wholseller_no ){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                String companyname = null;
                try {
                    JSONObject jObj = new JSONObject(response);

                    companyname = jObj.getString("name");
                   /* myEditior.putString("company",companyname);
                    myEditior.commit();*/


                    JSONObject comanyarray = jObj.getJSONObject("_links");

                    JSONObject urlobject = comanyarray.getJSONObject("logo");

                    imageurl = urlobject.getString("href");


                } catch (JSONException e) {

                    e.printStackTrace();
                } finally {

                    dialog.dismiss();

                    productlist.add(new SellerPortal_model(imageurl, companyname, sellerid, wholseller_no));


                    seller_portal_fragment1_adapter = new Seller_portal_fragment1Adapter(getActivity(), productlist, getFragmentManager());
                    /*recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));*/
                    sellser_recycler.setAdapter(seller_portal_fragment1_adapter);
                    /*    sellser_recycler2.setAdapter(seller_portal_fragment1_adapter);*/

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



        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    public void manufacturer ( ){

        String url=ip1+"/b2b/api/v1/user/users?role=ROLE_MANUFACTURER&product=Jewellery";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);

                    JSONObject embedeobject=jObj.getJSONObject("_embedded");
                    JSONArray user_array=embedeobject.getJSONArray("userList");

                    for(int i=0;i<user_array.length();i++){
                        JSONObject object=user_array.getJSONObject(i);

                        String  seller_id= object.getString("id");
                        String seller_name=object.getString("firstName");
                        String  wholseller_mob= object.getString("mobileNumber");


                      /* myEditior.putString("Seller_id",seller_id);
                       myEditior.putString("Seller_name",seller_name).apply();
                       myEditior.putString("Wholeseller_mob",wholseller_mob);
                       myEditior.apply();
                       myEditior.commit();*/


                        JSONObject comanyarray=object.getJSONObject("_links");

                        JSONObject urlobject=comanyarray.getJSONObject("company");

                        String sellerurl=urlobject.getString("href");

                        getManufacturer(sellerurl,seller_id,wholseller_mob);


                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;


                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 404:
                            BottomSheet.Builder builder = new BottomSheet.Builder(getActivity());
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;

                        case 401:
                            Toast.makeText(getContext(),"Session Expired!",Toast.LENGTH_SHORT).show();
                            session.logoutUser(getContext());

                    }
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() {

                sharedPref=getActivity().getApplicationContext().getSharedPreferences("USER_DETAILS",0);

                output=sharedPref.getString(ACCESS_TOKEN, null);

                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                return params;
            }
        };




        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public void getManufacturer (String url, final String sellerid,final String wholseller_no ){


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);

                    companyname2=jObj.getString("name");
                   /* myEditior.putString("company",companyname);
                    myEditior.commit();*/
                    JSONObject comanyarray=jObj.getJSONObject("_links");

                    JSONObject urlobject=comanyarray.getJSONObject("logo");

                    imageurl2=urlobject.getString("href");


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                finally {


                    productlist2.add(new SellerPortal_model(imageurl2, companyname2, sellerid, wholseller_no));


                    seller_portal_fragment1_adapter = new Seller_portal_fragment1Adapter(getActivity(), productlist2, getFragmentManager());
                    /*recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));*/

                        sellser_recycler2.setAdapter(seller_portal_fragment1_adapter);

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



        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }
}

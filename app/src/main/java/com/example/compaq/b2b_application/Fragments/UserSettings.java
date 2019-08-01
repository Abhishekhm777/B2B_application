package com.example.compaq.b2b_application.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;


public class UserSettings extends Fragment {

    private SharedPreferences sharedPref;
    private String Acess_Token,adharNo="",pan="",role="",password="",adharDocumentId="",panDocumentId="",email="",user_fname="",user_lname="", teli_phone="";
    private JSONObject company_details,websiteSetting;
    private JSONArray meltingSealing,userClass,address,wishlist;
    private int defaultAddressID=0,id=0;
    Boolean verified,verifyRequest;
    RecyclerView seal_recycler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user_settings, container, false);

        sharedPref =getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Acess_Token=sharedPref.getString(ACCESS_TOKEN, null);
        seal_recycler=(RecyclerView)view.findViewById(R.id.seal_recyclerview);
        final GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 1);
        seal_recycler.setLayoutManager(mGridLayoutManager);
        setHasOptionsMenu(true);
        userInformation();
        return view;
    }


    public void userInformation ( ){

        String url=ip1+"/b2b/api/v1/user/info";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    adharDocumentId=jObj.getString("adharDocumentId");
                    adharNo=jObj.getString("adharNo");
                    address=jObj.getJSONArray("address");
                    company_details=jObj.getJSONObject("company");
                    email=jObj.getString("email");
                    defaultAddressID=jObj.getInt("defaultAddressID");
                    user_fname=jObj.getString("firstName");
                    user_lname=jObj.getString("lastName");
                    teli_phone=jObj.getString("mobileNumber");
                    id=jObj.getInt("id");
                    meltingSealing=jObj.getJSONArray("meltingSealing");
                    pan=jObj.getString("pan");
                    panDocumentId=jObj.getString("panDocumentId");
                    password=jObj.getString("password");
                    role=jObj.getString("role");
                    userClass=jObj.getJSONArray("userClass");
                    verified=jObj.getBoolean("verified");
                    verifyRequest=jObj.getBoolean("verifyRequest");
                    websiteSetting=jObj.getJSONObject("websiteSetting");
                    wishlist=jObj.getJSONArray("wishList");

                    Log.d("userClass.....",userClass.toString());
                    if(meltingSealing.length()>0){
                        add_row(meltingSealing.length());
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
                params.put("Authorization","bearer "+Acess_Token);
                params.put("Content-Type", "application/json");
                return params;
            }
        };




        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
//////////////////////////////////////add row////////////////////////////////////////////////////////
    public  void add_row(int count){
        for(int i=0;i<count;i++) {
            try {
                JSONObject meltObj=meltingSealing.getJSONObject(i);




            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }



///////////////////////////////////////////////////edit menu///////////////////////////////////////////

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.edit_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("item...", item.getItemId() + "");
        switch (item.getItemId()) {

            case R.id.edit_icon:
               /* user_fname.setEnabled(true);
                user_fname.setClickable(true);
                user_fname.setFocusableInTouchMode(true);
                user_lname.setEnabled(true);
                user_lname.setClickable(true);
                user_lname.setFocusableInTouchMode(true);
                email.setEnabled(true);
                email.setClickable(true);
                email.setFocusableInTouchMode(true);
                teli_phone.setEnabled(true);
                teli_phone.setClickable(true);
                teli_phone.setFocusableInTouchMode(true);*/
                setHasOptionsMenu(false);

                return true;


        }
        return  false;
    }

}

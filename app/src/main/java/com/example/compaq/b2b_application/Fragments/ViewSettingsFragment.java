package com.example.compaq.b2b_application.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.Slider_adapterView;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;


public class ViewSettingsFragment extends Fragment {

    private SharedPreferences sharedPref;
    private String Acess_Token=null,slider_id=null;
    private JSONObject company_details,websiteSetting;
    ArrayList<String>slider_list;
    JSONArray slider_img_array;
    private int id=0;
    Slider_adapterView slider_adapterView;

    RecyclerView slider_recycler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_view_settings, container, false);
        sharedPref=getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Acess_Token=sharedPref.getString(ACCESS_TOKEN, null);
        slider_list=new ArrayList<>();

        slider_recycler=(RecyclerView)view.findViewById(R.id.slider_recyclerview);
        final GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        slider_recycler.setLayoutManager(mGridLayoutManager);
        userInformation();

        return  view;
    }


    ///////////////////////////////take info////////////////////////////////////
    public void userInformation ( ){

        String url=ip1+"/b2b/api/v1/user/info";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);

                    id=jObj.getInt("id");
                    getSlider();


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

    /////////////////////////////////////////////get Slider///////////////////////////////
    public void getSlider(){
        String url=ip1+"/b2b/catalog/api/v1/slide/get/"+id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject sliderObj = new JSONObject(response);
                    slider_id=sliderObj.getString("id");
                     slider_img_array =sliderObj.getJSONArray("imageGridFsID");
                    if(slider_img_array.length()!=0){
                        for (int i=0;i<slider_img_array.length();i++){
                            slider_list.add(slider_img_array.get(i).toString());
                        }

                    }
                    slider_adapterView=new Slider_adapterView(getActivity(),slider_list);
                    slider_recycler.setAdapter(slider_adapterView);



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



}

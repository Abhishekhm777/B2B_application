package com.example.compaq.b2b_application.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.Inner_RecyclerAdapter4;
import com.example.compaq.b2b_application.Adapters.RecyclerAdapter3;
import com.example.compaq.b2b_application.Model.Inner_Recy_model;
import com.example.compaq.b2b_application.Model.Recycler_model3;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class Varient_display_fragment extends Fragment {
private View view;
    private ArrayList<Recycler_model3> detProductlist;

    private Recycler_model3 main2_listner;
    private RecyclerAdapter3 main2_recycler_adapter;
    private Inner_Recy_model inner_recy_listner;
    public Inner_RecyclerAdapter4 inner_recycler_adapter;
    private ArrayList<Inner_Recy_model> details_list;
    private SharedPreferences sharedPref;
    private String sku_wishlilst;
    private String output,wholesaler,user_id,name;
    @BindView(R.id.main2recycler) RecyclerView main_recyclerView;

    public Varient_display_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_varient_display_fragment, container, false);
        ButterKnife.bind(this, view);
        sharedPref = getActivity().getSharedPreferences("USER_DETAILS", 0);
        wholesaler = sharedPref.getString("Wholeseller_id", null);
        output = sharedPref.getString(ACCESS_TOKEN, null);
        user_id = sharedPref.getString("userid", null);
        detProductlist = new ArrayList<>();
        Bundle bundle=getActivity().getIntent().getExtras();
        name= Objects.requireNonNull(bundle).getString("Item_Clicked");

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext().getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
        main_recyclerView.setLayoutManager(mGridLayoutManager);
        detailsRecycleData();
        return  view;
    }

    private void detailsRecycleData(){

        String Detail_URL_DATA=ip+"gate/b2b/catalog/api/v1/product/"+name+"?wholesaler="+wholesaler;

        StringRequest stringRequest=new StringRequest(Request.Method.GET,  Detail_URL_DATA, new Response.Listener<String>() {
            @Override
            public
            void onResponse(String response) {

                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONObject jp=jsonObj.getJSONObject("resourceSupport");
                    JSONArray ja_data = jp.getJSONArray("specification");

                    Log.d("OutPut", String.valueOf(ja_data.length()));
                    /* details_list=new ArrayList<>();*/
                    int length = ja_data.length();

                    for(int i=0; i<length; i++) {
                        JSONObject jObj = ja_data.getJSONObject(i);
                        String heading=(jObj.getString("heading"));
                        main2_listner=new Recycler_model3(heading);
                        detProductlist.add(main2_listner);

                        JSONArray attribute = jObj.getJSONArray ("attributes");
                        details_list=new ArrayList<>();
                        String key,values;

                        for(int j=0;j<attribute.length();j++) {
                            try {
                                JSONObject attributeobjects = attribute.getJSONObject(j);

                                key = (attributeobjects.getString("key"));
                                JSONArray attribute_values = attributeobjects.getJSONArray("values");

                                values = attribute_values.getString(0);
                                if (values.equalsIgnoreCase("")) {

                                    continue;
                                }
                                inner_recy_listner = new Inner_Recy_model(key, values);
                            }
                            catch (Exception e){
                                continue;
                            }



                            inner_recy_listner=new Inner_Recy_model(key,values);
                            details_list.add(inner_recy_listner);

                        }

                        main2_listner.setArrayList(details_list);
                    }


                    /*  main2_listner.setArrayList(details_list);*/

                    main2_recycler_adapter = new RecyclerAdapter3(getActivity(), detProductlist,0);
                    main_recyclerView.setAdapter(main2_recycler_adapter);
                    main_recyclerView.setNestedScrollingEnabled(false);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public
            void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization","bearer "+output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}

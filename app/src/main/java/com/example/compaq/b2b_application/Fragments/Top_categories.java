package com.example.compaq.b2b_application.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.Top_adapter;
import com.example.compaq.b2b_application.Model.Top_model;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip_cat;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class Top_categories extends Fragment {
private  View view;
private ListView listView;
private ArrayList<Top_model> productlist;
private Top_adapter top_adapter;
public SharedPreferences sharedPref;

    public Top_categories() {
        // Required empty public constructor
    }
    private Context context;
    private  Bundle bundle;

private  String output,wholseller_id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_top_categories, container, false);
        listView=(ListView)view.findViewById(R.id.top_category);
        context=this.getContext();
        sharedPref = getActivity().getSharedPreferences("USER_DETAILS", 0);
        wholseller_id = sharedPref.getString("userid", null);


        output = sharedPref.getString(ACCESS_TOKEN, null);

        prepareSubListData();

        return  view;
    }
    private void prepareSubListData ( ){
        productlist=new ArrayList<>();
        String url=ip_cat+"/category/byFirstLevelCategory/b2b/Jewellery?wholesaler="+wholseller_id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray ja_data = new JSONArray(response);
                    for(int i=0;i<ja_data.length();i++){
                        JSONObject jsonObject=ja_data.getJSONObject(i);
                        productlist.add(new Top_model(jsonObject.getString("name"),jsonObject.getBoolean("topCategory"),jsonObject));

                    }
                    Collections.sort(productlist, new Comparator<Top_model>() {
                        @Override
                        public int compare(Top_model top_model, Top_model t1) {
                            return top_model.getName().compareTo(t1.getName());
                        }
                    });

                    Collections.sort(productlist, new Comparator<Top_model>(){

                        @Override
                        public int compare(Top_model mall1, Top_model mall2){

                            boolean b1 = mall1.getaBoolean();
                            boolean b2 = mall2.getaBoolean();

                            return (b1 != b2) ? (b1) ? -1 : 1 : 0;
                        }
                    });
                    top_adapter=new Top_adapter(context,productlist,view,wholseller_id);
                    listView.setAdapter(top_adapter);

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
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

}

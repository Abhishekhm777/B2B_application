package com.example.compaq.b2b_application.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip_cat;


public class Select_CatalogueFragment extends Fragment {

    List<String> catalogueList;
    ListView listView;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_select__catalogue, container, false);
        listView=(ListView) view.findViewById(R.id.catalogue_list);

        catalogueList=new ArrayList<>();
       textView=(TextView) view.findViewById(R.id.catalog);



        find_catalogue();
       return  view;
    }
    public  void  find_catalogue(){
        String url=ip_cat+"/catalogue";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response.length()!=0){
                            try {
                                JSONArray jsonArray=response.getJSONArray("content");
                                if(jsonArray.length()!=0){
                                    for(int i=0;i<jsonArray.length();i++){
                                        JSONObject catObject=jsonArray.getJSONObject(i);
                                        String catName=catObject.getString("name");
                                       catalogueList.add(catName);
                                       if(i==jsonArray.length()-1){
                                           ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                                   android.R.layout.simple_expandable_list_item_1,catalogueList);
                                                    listView.setAdapter(adapter);
                                       }
                                    }

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                        else {
                            Toast.makeText(getContext(),"Please check password or reenter it",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(jsonObjectRequest);


    }


}

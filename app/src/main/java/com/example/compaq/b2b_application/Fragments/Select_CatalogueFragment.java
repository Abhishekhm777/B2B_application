package com.example.compaq.b2b_application.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.example.compaq.b2b_application.Activity.Sign_up_Activity;
import com.example.compaq.b2b_application.Adapters.CatalogueListAdapter;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip_cat;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;


public class Select_CatalogueFragment extends Fragment {

    ArrayList<String> catalogueList;
    ListView listView;
    CatalogueListAdapter catalogueListAdapter;
    SharedPreferences sharedPref;
    SharedPreferences.Editor myEditior;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_select__catalogue, container, false);
        listView=(ListView) view.findViewById(R.id.catalogue_list);
        catalogueList=new ArrayList<>();
        find_catalogue();
        sharedPref = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        myEditior = sharedPref.edit();

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               if(catalogueList.size()>0) {
                   String catname = catalogueList.get(i);
                   myEditior.putString("CATALOGUE", catname);
                   myEditior.commit();
                   myEditior.apply();

                   Log.d("catalog...", catname);
                   Sign_up_Activity.set_view(1);
               }
               else {
                   Toast.makeText(getContext(),"Something went wrong!",
                           Toast.LENGTH_SHORT).show();
               }
           }
       });
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
                                           Log.d("catalogue...",catalogueList.size()+"");
                                           catalogueListAdapter=new CatalogueListAdapter(getActivity(),catalogueList);
                                           listView.setAdapter(catalogueListAdapter);
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

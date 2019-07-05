package com.example.compaq.b2b_application.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.compaq.b2b_application.Adapters.Slider_adapter;
import com.example.compaq.b2b_application.Model.Slider_model;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;

import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class Slider_fragment extends Fragment {

    public ArrayList<Slider_model> productlist;
    public RecyclerView recyclerView;
    SharedPreferences pref;
    String output,user_id;
    public Slider_adapter adapter;
    public Slider_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_slider_fragment, container, false);
        pref=getActivity().getSharedPreferences("USER_DETAILS",0);

        output=pref.getString(ACCESS_TOKEN, null);
        user_id= pref.getString("userid", null);
        productlist=new ArrayList<>();
        recyclerView=(RecyclerView)view.findViewById(R.id.slider_recycler) ;
      /*  getSlider();*/
        return view;
    }
   /* public  void getSlider( ){


        String url = ip+"gate/b2b/catalog/api/v1/slide/get/"+user_id;
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonObject=response.getJSONObject("_links");
                    JSONArray jsonArray=jsonObject.getJSONArray("imageURl");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);


                        productlist.add(new Slider_model(jsonObject1.getString("href")));

                    }


                    adapter = new Slider_adapter(getActivity(), productlist);



                    recyclerView.setAdapter(adapter);



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
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "bearer " + output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

        };

        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(objectRequest);

    }*/
}

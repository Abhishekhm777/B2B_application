package com.example.compaq.b2b_application.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.Manage_Adapter;
import com.example.compaq.b2b_application.Model.Recy_model2;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Fragments.Fragment_2.URL_DATA;
import static com.example.compaq.b2b_application.MainActivity.ip_cat;
import static com.example.compaq.b2b_application.SessionManagement.ACCESS_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class Mange_card_fragment extends Fragment {
    public ArrayList<Recy_model2> productlist;
    RecyclerView recyclerView;
    String output,wholseller_id;
    public SharedPreferences sharedPref;
    public Manage_Adapter manage_adapter;
    private  Bundle bundle;
    SearchView searchView;
    public Mange_card_fragment() {
        // Required empty public constructor
    }
private  View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

            view = inflater.inflate(R.layout.fragment_mange_card_fragment, container, false);

            sharedPref = getActivity().getSharedPreferences("USER_DETAILS", 0);

            output = sharedPref.getString(ACCESS_TOKEN, null);
        searchView = (SearchView) getActivity().findViewById(R.id.sellers_search);
        searchView.setVisibility(View.GONE);

            recyclerView = (RecyclerView) view.findViewById(R.id.navrecycler);
            productlist = new ArrayList<>();
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recyclerView.setHasFixedSize(true);

            wholseller_id = sharedPref.getString("userid", null);

            bundle = this.getArguments();
            String text = bundle.getString("Searched");
            if(bundle.getString("Searched")!=null){

                String ty=ip_cat+"/searching/facets?queryText="+text+"&wholesaler="+wholseller_id+"&productType=REGULAR";
                String url= ty.replaceAll("\\s", "");

                loadRecycleData(url);
            }
            else {
               String text1= bundle.getString("clicked");
                String ty=ip_cat+"/product/all/category/Jewellery,"+text1+"?wholesaler="+wholseller_id+"&productType=REGULAR&showAll=true";
                String url= ty.replaceAll("\\s", "");
                loadRecycleData(url);
            }


        return view;
    }
    public void loadRecycleData(String url){
        Log.e("UUURRRLLL",url);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public
            void onResponse(String response) {

                try {

                    JSONObject jsonObj = new JSONObject(response);
                    JSONObject jp=jsonObj.getJSONObject("products");

                    /* JSONArray jsPriceArray = jsonObj.getJSONArray("productPrice");*/

                    JSONArray ja_data = jp.getJSONArray("content");
                    if(ja_data.length()==0){
                        Snackbar.make(getView(), "Sorry! No Products Available", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }


                    int length = ja_data.length();
                    for(int i=0; i<length; i++) {
                        JSONObject jObj = ja_data.getJSONObject(i);


                            /*JSONObject priceObject = jsPriceArray.getJSONObject(i);

                            String price = String.valueOf(priceObject.getDouble("productFinalPrice"));*/
                        String sku = jObj.getString("sku");


                        String id = (jObj.getString("id"));
                        String name = (jObj.getString("name"));

                        JSONArray image_arr = jObj.getJSONArray ("links");
                        JSONObject img_jObj = image_arr.getJSONObject(1);
                        String imageurl=img_jObj.getString("href");

                        Recy_model2 item=new Recy_model2(id,imageurl,sku,name);
                        productlist.add(item);



                    }


                    manage_adapter = new Manage_Adapter(getActivity(), productlist);



                    recyclerView.setAdapter(manage_adapter);



                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        },new Response.ErrorListener() {
            @Override
            public
            void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;


                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 404:
                            BottomSheet.Builder builder = new BottomSheet.Builder(getContext());
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;
                        case 400:
                            BottomSheet.Builder builder1 = new BottomSheet.Builder(getContext());
                            builder1.setTitle("Sorry! No Products Available");
                            builder1.show();
                            break;
                        case 417:

                            Snackbar.make(view, "Sorry! No Products Available", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                           /* BottomSheet.Builder builder2 = new BottomSheet.Builder(getContext());
                            builder2.setTitle("Sorry! No Products Available");
                            builder2.show();*/
                            break;


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
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }




}

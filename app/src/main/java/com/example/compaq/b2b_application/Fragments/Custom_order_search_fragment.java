package com.example.compaq.b2b_application.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.Custom_Order_search_Adapter;
import com.example.compaq.b2b_application.Adapters.Top_adapter;
import com.example.compaq.b2b_application.Model.Top_model;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class Custom_order_search_fragment extends Fragment {
private ListView listView;
private SearchView searchView;
private AppBarLayout appBarLayout;
private View view;
private SharedPreferences sharedPref;
private SharedPreferences.Editor editor;
private  String output,user_id,wholseller_id;
private ArrayList<Top_model> skus;
private ArrayList<Top_model> names;
    private ArrayList<String> ids;
private Custom_Order_search_Adapter top_adapter;
private RadioButton byName,byCategory;
private  Bundle bundle;
private String path;

    public Custom_order_search_fragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_custom_order_search_fragment, container, false);

            listView=view.findViewById(R.id.custom_search_listview);

            searchView =  getActivity().findViewById(R.id.custom_search);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if(!newText.equalsIgnoreCase("")){
                        getSuggestions(newText);
                    }
                    if (newText.equalsIgnoreCase("")){
                          names.clear();
                          top_adapter.notifyDataSetChanged();
                    }

                    return true;
                }
            });
            sharedPref = getActivity().getSharedPreferences("USER_DETAILS", 0);
            editor = sharedPref.edit();
            wholseller_id = sharedPref.getString("userid", null);
            output = sharedPref.getString(ACCESS_TOKEN, null);
            user_id = sharedPref.getString("userid", "");

            bundle = this.getArguments();
            if(bundle.getString("path")!=null){
                searchView.setVisibility(View.GONE);
                path=bundle.getString("path");

                String url =  ip+"gate/b2b/catalog/api/v1/product/all/category/"+path;
                String uri=null;
                uri = Uri.parse(url)
                        .buildUpon()
                        .appendQueryParameter("wholesaler",user_id)
                        .appendQueryParameter("productType","REGULAR")
                        .build().toString();
                Log.e("CAT",uri);
                getProduct(uri);

            }
            else {
                searchView.setIconified(false);
                searchView.requestFocusFromTouch();
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {


                    editor.putString("cust_id",ids.get(position)).apply();
                    editor.commit();
                    getActivity().finish();

                }
            });

        }
        return view;
    }
public  void getSuggestions(String text){
    String url = ip+"gate/b2b/catalog/api/v1/searching/facets";
    String uri=null;
    uri = Uri.parse(url)
            .buildUpon()
            .appendQueryParameter("queryText",text)
            .appendQueryParameter("wholesaler",user_id)
            .appendQueryParameter("productType","REGULAR")
            .build().toString();
    Log.e("JJIB",uri);
        getProduct(uri);
}




    public void getProduct( String text){

        StringRequest stringRequest=new StringRequest(Request.Method.GET, text, new Response.Listener<String>() {
            @Override
            public
            void onResponse(String response) {
                try {
                    skus = new ArrayList<>();
                    names = new ArrayList<>();
                    ids = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject pro_object = jsonObject.getJSONObject("products");
                    JSONArray jsonArray = pro_object.getJSONArray("content");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject content = jsonArray.getJSONObject(i);
                        names.add(new Top_model(content.getString("name"), content.getString("sku")));
                        ids.add(content.getString("id"));
                    }
                    top_adapter = new Custom_Order_search_Adapter(getActivity(), names, wholseller_id);
                    listView.setAdapter(top_adapter);

                } catch (Exception e) {
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

                            Snackbar.make(getView(), "Sorry! No Products Available", Snackbar.LENGTH_LONG)
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

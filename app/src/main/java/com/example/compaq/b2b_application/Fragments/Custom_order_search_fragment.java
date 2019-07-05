package com.example.compaq.b2b_application.Fragments;


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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
private  String output,user_id;
private ArrayList<String> suggestion_list;

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
            searchView=view.findViewById(R.id.custom_search);


            searchView =  view.findViewById(R.id.custom_search);
            searchView.setSubmitButtonEnabled(true);

            searchView.setIconified(false);
            searchView.requestFocusFromTouch();

            sharedPref = getActivity().getSharedPreferences("USER_DETAILS", 0);

            output = sharedPref.getString(ACCESS_TOKEN, null);
            user_id = sharedPref.getString("userid", "");

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

                    return true;
                }
            });





        }
        return view;
    }

    public void getSuggestions( String text){
        Log.e("JJIB","SS");
        suggestion_list=new ArrayList<>();
        String url = ip+"gate/b2b/catalog/api/v1/product/getsuggesions";
        String uri=null;
        uri = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter("text",text)
                .appendQueryParameter("user",user_id)
                .build().toString();
        Log.e("JJIB",uri);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public
            void onResponse(String response) {

                try {
                    JSONArray jsonArray=new JSONArray(response);
                    Log.e("JJIB",response);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        suggestion_list.add(jsonObject.getString("name"));

                        Log.e("JJIB","SS252525");
                    }



                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_list_item_1, android.R.id.text1, suggestion_list);


                    listView.setAdapter(adapter);
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

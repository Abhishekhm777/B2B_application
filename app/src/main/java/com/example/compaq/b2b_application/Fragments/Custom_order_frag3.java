package com.example.compaq.b2b_application.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Adapters.Customize_Oder_Adapter1;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.Helper_classess.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.Customize_Order.pager;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Fragments.products_display_fragment.URL_DATA;
import static com.example.compaq.b2b_application.Fragments.products_display_fragment.item_clicked;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip_cat;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class Custom_order_frag3 extends Fragment {

    Bundle bundle;
    public SharedPreferences sharedPref;
    public SharedPreferences.Editor myEditor;
    public List<String> listDataHeader = new ArrayList<String>();
    HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
    Customize_Oder_Adapter1 listAdapter;
    public String output, wholseller_id;
    public int position = 0;
    public String SUB_URL = "";
    public String sname = "";
    SessionManagement session;
    private Button place_button;
    public Bundle bundle2;
    private View view;
    public String original;
    int item = 0;
    HashMap<String, String> list_id = new HashMap<String, String>();
    String all;
   private EditText p_name,date,size,qty,qwt,melting,seal,descri;
    public Custom_order_frag3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(view==null) {
            view = inflater.inflate(R.layout.fragment_customize_order_frag2, container, false);

            place_button=(Button)view.findViewById(R.id.place_button);
            sharedPref = getContext().getSharedPreferences("USER_DETAILS", 0);
            myEditor = sharedPref.edit();
            output = sharedPref.getString(ACCESS_TOKEN, null);

            wholseller_id = sharedPref.getString("userid", null);
            p_name=view.findViewById(R.id.product_name);
            date=view.findViewById(R.id.date);
            size=view.findViewById(R.id.size);
            qty=view.findViewById(R.id.qty);
            qwt=view.findViewById(R.id.g_weight);
            melting=view.findViewById(R.id.melting);
            seal=view.findViewById(R.id.sea);
            descri=view.findViewById(R.id.descr);


            session = new SessionManagement(getActivity());
            listAdapter = new Customize_Oder_Adapter1(getActivity(), listDataHeader, listDataChild, list_id, getFragmentManager(), view);


            place_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pager.setCurrentItem(4);
                }
            });

        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String id=sharedPref.getString("cust_id", null);

        if(id!=null){

            getProduct(id);
        }

    }

    private RequestQueue requestQueue;
    private void prepareListData () {
        bundle=this.getArguments();
        item_clicked=bundle.getString("Item_Clicked");
        all=bundle.getString("All");
        original=item_clicked;

        if(requestQueue==null)
        {
            requestQueue = Volley.newRequestQueue(getActivity());

        }

        URL_DATA=ip_cat+"/category/byFirstLevelCategory/b2b/Jewellery,"+item_clicked+"?wholesaler="+wholseller_id;
        String  url=URL_DATA.replaceAll("\\s", "");


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{

                    JSONArray ja_data = new JSONArray(response);
                    int length = ja_data.length();

                    for (int i = 0; i < length; i++) {

                        ++item;

                        JSONObject jObj = ja_data.getJSONObject(i);

                        JSONArray parent_array=jObj.getJSONArray("parent");
                        String parent=parent_array.getString(0);


                        sname = (jObj.getString("name").replaceAll("\\s", ""));
                        listDataHeader.add(sname);
                        list_id.put(sname, jObj.getString("id"));

                        position = i;

                        SUB_URL = ip_cat+"/category/byFirstLevelCategory/b2b/"+parent+","+sname+"?wholesaler="+wholseller_id;
                        SUB_URL=SUB_URL.replaceAll("\\s", "");

                        prepareSubListData(i, sname);

                       /* if(item==length){
                            final List<String> submenu = new ArrayList<String>();

                            listDataChild.put(listDataHeader.get(item), submenu);
                        }*/


                    }


                } catch (JSONException e) {
                    e.printStackTrace();


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;

                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 404:
                            BottomSheet.Builder builder = new BottomSheet.Builder(getActivity());
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;

                        case 401:

                            Toast.makeText(getActivity(),"Session Expired!",Toast.LENGTH_SHORT).show();
                            /* new Reffressh_token().getToken(MainActivity.this);*/
                            session.logoutUser(getActivity());

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

        requestQueue.add(stringRequest);
    }


    private void prepareSubListData ( final int i, final String sname1){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, SUB_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONArray ja_data = new JSONArray(response);
                    /*if (ja_data.length() == 0) {
                        final List<String> submenu = new ArrayList<String>();
                        submenu.add("All  "+sname1);

                        listDataChild.put(listDataHeader.get(i), submenu);



                    } else {*/
                    final List<String> submenu = new ArrayList<String>();

                    for (int j = 0; j < ja_data.length(); j++) {

                        JSONObject jObj = ja_data.getJSONObject(j);

                        String subnames = jObj.getString("name");
                        list_id.put(subnames, jObj.getString("id"));
                        submenu.add(subnames);
                    }
                    listDataChild.put(listDataHeader.get(i), submenu);


                    // Header, Child data



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

    public void getProduct( String id){

        String url = ip+"gate/b2b/catalog/api/v1/product/"+id;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url   , new Response.Listener<String>() {
            @Override
            public
            void onResponse(String response) {
                try {

                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject pro_object=jsonObject.getJSONObject("resourceSupport");
                    p_name.setText(pro_object.getString("name"));
                    qwt.setText(pro_object.getString("grossWeight"));


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

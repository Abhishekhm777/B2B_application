package com.example.compaq.b2b_application.Fragments;


import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.Customize_Oder_Adapter1;
import com.example.compaq.b2b_application.Adapters.Manage_Adapter;
import com.example.compaq.b2b_application.Adapters.Manage_category_Adapter;
import com.example.compaq.b2b_application.Adapters.Update_product_recy_Adaptetr;
import com.example.compaq.b2b_application.Add_new_product_activity;
import com.example.compaq.b2b_application.Model.Recy_model2;
import com.example.compaq.b2b_application.Model.Update_product_model;
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
import java.util.List;
import java.util.Map;

import static com.example.compaq.b2b_application.Fragments.Fragment_2.URL_DATA;
import static com.example.compaq.b2b_application.MainActivity.ip_cat;
import static com.example.compaq.b2b_application.SessionManagement.ACCESS_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class Customize_order_frag1 extends Fragment {
    ExpandableListView expListView;
    public ArrayList<String> listDataHeader = new ArrayList<String>();
    HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
    HashMap<String, String> list_id = new HashMap<String, String>();
    Customize_Oder_Adapter1 listAdapter;
    public Manage_Adapter manage_adapter;
    private int lastExpandedPosition = -1;
    public SharedPreferences sharedPref;
    private Bundle bundle;
    SessionManagement session;
    public String SUB_URL = "";
    public String sname = "";
    private View view;
    public int position = 0;
    String output;
    String wholseller_id;
    private Mange_card_fragment mange_card_fragment;
    ProgressBar progressBar;
    Button button;
    private Spinner spinner, pro_spinner, status_spinner, prioriyt_spinner, spinner2;
    private FragmentTransaction fragmentTransaction;
    public FragmentManager fragmentManager;
    /*private String path="";*/
    private SearchView searchView,search_by_name;
    public ArrayList<Recy_model2> productlist;
    Manage_category_frag1.TextClicked mCallback;
    private    StringBuilder category_path = new StringBuilder();
    private  List  list=new ArrayList();

    Dialog dialog;
    private LinearLayout parentLinearLayout;
    private RelativeLayout newLayout;
    private String selected = "";
    RecyclerView recyclerView;
    TextView ok,cancel,main_text;
    HashMap<String, String> all_id = new HashMap<String, String>();
    public List<String> product;
    private Button clear,submit,add_product;
    private RadioButton byname,byCat;
    private LinearLayout cat_view,recy_view,second_search;


    public Customize_order_frag1() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragments_customize_order_frag1, container, false);
            listAdapter = new Customize_Oder_Adapter1(getActivity(), listDataHeader, listDataChild, list_id, getFragmentManager(), view);

            dialog = new Dialog(getContext());
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.alert_popup);
            ok=(TextView)dialog.findViewById(R.id.ok);
            cancel=(TextView)dialog.findViewById(R.id.cancel);
            main_text=(TextView)dialog.findViewById(R.id.popup_textview);
            cat_view=(LinearLayout)view.findViewById(R.id.category_view) ;
            recy_view=(LinearLayout)view.findViewById(R.id.recy_lay) ;

            byname=(RadioButton)view.findViewById(R.id.byname);
            byCat=(RadioButton)view.findViewById(R.id.bycat);
            byname.setChecked(true);
           byname.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                 if(b){
                    byCat.setChecked(false);
                    searchView.setVisibility(View.VISIBLE);
                     cat_view.setVisibility(View.GONE);
                     second_search.setVisibility(View.GONE);
                 }
                   try{
                       productlist.clear();
                       manage_adapter.notifyDataSetChanged();
                   } catch (Exception e){

                   }
               }
           });
            byCat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        byname.setChecked(false);
                        searchView.setVisibility(View.GONE);
                        cat_view.setVisibility(View.VISIBLE);
                        second_search.setVisibility(View.VISIBLE);
                    }
                    try{
                        productlist.clear();
                        manage_adapter.notifyDataSetChanged();
                    } catch (Exception e){

                    }
                }
            });


            recyclerView = (RecyclerView) view.findViewById(R.id.image_recycler);
            GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1, GridLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(mGridLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());


            searchView = (SearchView) view.findViewById(R.id.sellers_search);
            second_search = (LinearLayout) view.findViewById(R.id.search_second);
            search_by_name = (SearchView) view.findViewById(R.id.filter_name);
            search_by_name.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if(!newText.equalsIgnoreCase(null)) {
                        manage_adapter.getFilter().filter(newText);
                    }

                    return true;

                }
            });
            searchView.setSubmitButtonEnabled(true);
            searchView.setFocusable(false);
            searchView.setFocusableInTouchMode(true);
            clear=(Button)view.findViewById(R.id.clear) ;
            submit=(Button)view.findViewById(R.id.submit);
            add_product=(Button)view.findViewById(R.id.add_product);
            add_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getActivity(),Add_new_product_activity.class);
                    startActivity(intent,  ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                }
            });

            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  /*  parentLinearLayout.removeAllViews();*/
                    if(parentLinearLayout.getChildCount()>1) {
                        parentLinearLayout.removeViewAt(parentLinearLayout.getChildCount() - 1);
                        try {
                           /* path = path.substring(0, path.lastIndexOf(',', path.lastIndexOf(',')+1));*/


                                productlist.clear();
                                manage_adapter.notifyDataSetChanged();
                                recy_view.setVisibility(View.GONE);

                        }
                        catch (Exception e){

                        }
                    }
                   /* if(parentLinearLayout.getChildCount()==1){
                       *//* sp.setSelection(0);*//*
                    }*/
                }
            });

            submit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    recy_view.setVisibility(View.VISIBLE);
                    try{
                        productlist.clear();
                        manage_adapter.notifyDataSetChanged();

                    } catch (Exception e){

                    }

                     String path="Jewellery";
                    Log.e("CAT PATH",path);
                    int childcout= parentLinearLayout.getChildCount();
                    for(int i=0;i<childcout;i++){
                        View v = parentLinearLayout.getChildAt(i);
                        Spinner spinne = v.findViewById(R.id.dynamic_spinner);
                         if(spinne.getSelectedItemPosition()>0) {
                             path += ","+ spinne.getSelectedItem().toString();
                             Log.e("HILO", spinne.getSelectedItem().toString());
                             Log.e("HILO_", path);
                         }
                    }
                    by_category(path);
                }
            });

           searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
               @Override
               public boolean onQueryTextSubmit(String query) {
                   recy_view.setVisibility(View.VISIBLE);
                   String ty=ip_cat+"/searching/facets?queryText="+query+"&wholesaler="+wholseller_id+"&productType=REGULAR";
                   String url= ty.replaceAll("\\s", "");
                   loadRecycleData(url);
                   return false;
               }

               @Override
               public boolean onQueryTextChange(String newText) {
                 /*  String ty=ip_cat+"/searching/facets?queryText="+newText+"&wholesaler="+wholseller_id+"&productType=REGULAR";
                   String url= ty.replaceAll("\\s", "");
                   loadRecycleData(url);*/
                   return false;
               }
           });
           searchView.setOnCloseListener(new SearchView.OnCloseListener() {
               @Override
               public boolean onClose() {
                   productlist.clear();
                   manage_adapter.notifyDataSetChanged();
                   return false;
               }
           });


            sharedPref = getActivity().getSharedPreferences("USER_DETAILS", 0);

            session = new SessionManagement(getActivity().getApplicationContext());
            output = sharedPref.getString(ACCESS_TOKEN, null);

            mange_card_fragment = new Mange_card_fragment();
            progressBar = (ProgressBar) view.findViewById(R.id.progress);
            parentLinearLayout = (LinearLayout)view. findViewById(R.id.linearLayout);
            newLayout = (RelativeLayout) view.findViewById(R.id.new_layout);


            spinner = (Spinner) view.findViewById(R.id.country);
            pro_spinner = (Spinner)view. findViewById(R.id.product_type);
            status_spinner = (Spinner)view. findViewById(R.id.product_status);
            prioriyt_spinner = (Spinner)view. findViewById(R.id.priyority);
            wholseller_id = sharedPref.getString("userid", null);
           /* catalog();*/
            prepareListData("Jewellery");
            //expandAll();
            // Listview Group click listener

           /* spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (position == 0) {
                        return;
                    }
                   *//* newLayout.removeAllViews();*//*
                    selected = "";
                    String selectedItem = parent.getItemAtPosition(position).toString();


                   *//* if(path.length()==0){
                        path=selectedItem;
                    }else {
                        path=selectedItem+",";
                    }*//*

                    newLayout.setVisibility(View.VISIBLE);


                    prepareListData(selectedItem);


                } // to close the onItemSelected

                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/

        }

        return view;
    }


    public void loadRecycleData(String url){
        productlist = new ArrayList<>();

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

                    recyclerView.setAdapter(manage_adapter);
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
    /////////////////////////////////////////
    ///////////////////////////////////////catalog url/////////////////////////////////////

    private RequestQueue requestQueue1;

    private void catalog() {
        if (requestQueue1 == null) {
            requestQueue1 = Volley.newRequestQueue(getActivity());
        }

        URL_DATA = ip_cat + "/catalogue";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    product = new ArrayList<>();
                    product.add("Select Catalog");


                    JSONObject ja_data = new JSONObject(response);
                    JSONArray jsonArray = ja_data.getJSONArray("content");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        product.add(jsonObject.getString("name"));
                    }

                    final ArrayAdapter<String> country_adaper = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, product) {
                        @Override
                        public boolean isEnabled(int position) {
                            if (position == 0) {
                                // Disable the first item from Spinner
                                // First item will be use for hint
                                return false;
                            } else {


                                return true;
                            }
                        }


                    };
                    spinner.setAdapter(country_adaper);


                } catch (JSONException e) {
                    e.printStackTrace();


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 404:
                            BottomSheet.Builder builder = new BottomSheet.Builder(getContext());
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;

                    }
                }
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

        requestQueue1.add(stringRequest);
    }
//////////////////////////categories/////////////////////
private RequestQueue requestQueue;

    private void prepareListData(String item) {
        String qy="Jewellery";
        int childcout= parentLinearLayout.getChildCount();
        for(int i=0;i<childcout;i++){
            View v = parentLinearLayout.getChildAt(i);
            Spinner spinner = v.findViewById(R.id.dynamic_spinner);
            if(spinner.getSelectedItemPosition()>0){
                qy+=","+spinner.getSelectedItem().toString();
            }
            Log.e("QYYY",qy);
        }
       /* if (selected.equalsIgnoreCase("")) {
            selected = item;

        } else {
            selected = selected.concat("," + item);

        }*/
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity());

        }

        URL_DATA = ip_cat + "/category/byFirstLevelCategory/b2b/" + qy + "?wholesaler=" + wholseller_id;
        Log.e("SPI",URL_DATA);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<String> array = new ArrayList<>();
                    array.add("Select category");

                    JSONArray ja_data = new JSONArray(response);
                    if (ja_data.length() == 0) {

                        return;
                    }

                    for (int i = 0; i < ja_data.length(); i++) {
                        JSONObject jObj = ja_data.getJSONObject(i);


                        String sname = (jObj.getString("name").replaceAll("\\s", ""));

                        array.add(sname);


                        all_id.put(jObj.getString("name").replaceAll("\\s", ""), jObj.getString("id"));


                    }

                   /* spinn(array);*/
                  /*  dynamicSpinnerAdd(array);*/
                    onAddField(array);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Sidebar", "EXCEPETION");

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 404:
                            BottomSheet.Builder builder = new BottomSheet.Builder(getActivity());
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;

                    }
                }
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

        requestQueue.add(stringRequest);
    }
    ///////////////////////////////////////////
    public void spinn(ArrayList<String> string) {

        spinner2 = new Spinner(getActivity());
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, string) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(spinnerArrayAdapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                }

                /*    ((ViewManager)newLayout.getParent()).removeView(newLayout);*/

                String selectedItem = parent.getItemAtPosition(position).toString();
                category_path.append(selectedItem).append(",");

               /* path+=(","+selectedItem);*/

                prepareListData(selectedItem);
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        parentLinearLayout.addView(spinner2, parentLinearLayout.getChildCount());

    }

    public void dynamicSpinnerAdd(ArrayList<String> string){
        View v = parentLinearLayout.getChildAt(parentLinearLayout.getChildCount());
        Spinner spi = v.findViewById(R.id.dynamic_spinner);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, string) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spi.setAdapter(spinnerArrayAdapter);
        spi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                }

                /*    ((ViewManager)newLayout.getParent()).removeView(newLayout);*/

                String selectedItem = parent.getItemAtPosition(position).toString();

              /*  path+=(","+selectedItem);*/

                prepareListData(selectedItem);
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public  void refreshMethod(){
        /*listDataChild.clear();
        listDataHeader.clear();
        listAdapter.notifyDataSetChanged();*/
    }

    /////////////Delete//////////////////////////
    public void onDelete(View view) {
        parentLinearLayout.removeView(view);
    }

///////////////////////////////////////////////////////////////////////By Category Search
 public void by_category(  String text){
     productlist = new ArrayList<>();

    String ty=ip_cat+"/product/all/category/"+text+"?wholesaler="+wholseller_id+"&productType=REGULAR&showAll=true";
    String url= ty.replaceAll("\\s", "");


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

                recyclerView.setAdapter(manage_adapter);
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
                        Snackbar.make(getView(), "Sorry! Something went wrong", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        break;
                    case 400:
                        Snackbar.make(getView(), "Sorry! Something went wrong", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
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

    public void onAddField(ArrayList<String> string) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        final View rowView = inflater.inflate(R.layout.spinner_layout, null);


        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams.setMargins(0, 15, 0, 15);
        rowView.setLayoutParams(buttonLayoutParams);
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());

        final Spinner spi = rowView.findViewById(R.id.dynamic_spinner);


        ImageView button = (ImageView) rowView.findViewById(R.id.add_image);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle=new Bundle();
                bundle.putString("pro_id",all_id.get(spi.getSelectedItem().toString()));
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                Add_new_Category top_categories=new Add_new_Category();
                top_categories.setArguments(bundle);
                fragmentTransaction.replace(R.id.customize, top_categories).addToBackStack(null).commit();
            }
        });

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, string) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spi.setAdapter(spinnerArrayAdapter);
        spi.post(new Runnable() {
            public void run() {
                spi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            return;
                        }
                        /*    ((ViewManager)newLayout.getParent()).removeView(newLayout);*/
                        String selectedItem = parent.getItemAtPosition(position).toString();
                        /*  path+=(","+selectedItem);*/
                        parentLinearLayout.removeView(parentLinearLayout.getRootView());
                        newLayout.setVisibility(View.VISIBLE);
                        prepareListData(selectedItem);
                        try {

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } // to close the onItemSelected
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
      /*  Button button = (Button) rowView.findViewById(R.id.remove);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDelete(rowView);
            }
        });*/
    }
}

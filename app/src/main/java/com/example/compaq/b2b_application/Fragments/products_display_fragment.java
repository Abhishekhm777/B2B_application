package com.example.compaq.b2b_application.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.RecyclerAdapter2;
import com.example.compaq.b2b_application.Adapters.Recycler_Adapter2;
import com.example.compaq.b2b_application.Adapters.ViewpageAdapter1;
import com.example.compaq.b2b_application.Helper_classess.Bottom_sheet_dialog;
import com.example.compaq.b2b_application.Activity.Cart_Activity;
import com.example.compaq.b2b_application.Model.Recy_model2;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.Activity.Search_Activity;
import com.example.compaq.b2b_application.Helper_classess.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip_cat;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.LKEY_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public
class products_display_fragment extends Fragment implements Toolbar.OnMenuItemClickListener,View.OnClickListener, Bottom_sheet_dialog.BottomSheetListner, AdapterView.OnItemClickListener {
    private Map<Object, Object> params;
    public ArrayList<Recy_model2> productlist;
    public static String URL_DATA = "", create_urls = "";
    private DrawerLayout drawer;
    public RecyclerView recyclerView;
    public Recy_model2 recy_model2;
    public RecyclerAdapter2 recyclerAdapter2;
    public Bottom_sheet_dialog.BottomSheetListner mlistner;
    public Recycler_Adapter2 recycler_adapter2;
    public products_display_fragment nav_fragments;
    public static String item_clicked, parnt, class2;
    public Bundle bundle;
    Toolbar toolbar, toolbar1, main_toolbar;
    Fragment fragment;
    private Context context;
    public String ProductPrice = "";
    public String ProductFinalPrice = "";
    private ActionBarDrawerToggle mToggle;
    private DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    TextView textView;
    SessionManagement session;
    ProgressBar progressBar;
    CardView cardView;
    SharedPreferences pref;
    public View mImageBtn;
    private String output;
    private String user_no;
    private EditText otp;
    private String wholseller_id;
    ViewpageAdapter1 viewpageAdapter1;
    Dialog dialog, sortDialog;
    private ViewPager viewPager;
    Button submit, close;
    public EditText ed1, ed2, ed3, ed4, ed5, ed6;
    private SwipeRefreshLayout swipeContainer;
    RelativeLayout relativeLayout;
    LinearLayout sort_layout;
    ListView sortlist;
    ArrayAdapter sortadapter;
    Map<Object, Object> sortparams;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_fragment_2, container, false);

        session = new SessionManagement(getActivity().getApplicationContext());
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        bundle = this.getArguments();
        mlistner = this;
        class2 = "";
        Log.d("class...", class2);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.layout_buttom);

        dialog = new Dialog(getContext());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.pop_layout);
        submit = (Button) dialog.findViewById(R.id.submit);
        close = (Button) dialog.findViewById(R.id.close);

        ed1 = (EditText) dialog.findViewById(R.id.otpET1);
        ed2 = (EditText) dialog.findViewById(R.id.otpET2);
        ed3 = (EditText) dialog.findViewById(R.id.otpET3);
        ed4 = (EditText) dialog.findViewById(R.id.otpET4);
        ed5 = (EditText) dialog.findViewById(R.id.otpET5);

        ed1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    ed2.requestFocus();
                } else if (s.length() == 0) {
                    ed1.clearFocus();
                }
            }
        });
        ed2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    ed3.requestFocus();
                } else if (s.length() == 0) {
                    ed1.requestFocus();
                }
            }
        });

        ed3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    ed4.requestFocus();
                } else if (s.length() == 0) {
                    ed2.requestFocus();
                }
            }
        });


        ed4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    ed5.requestFocus();
                } else if (s.length() == 0) {
                    ed3.requestFocus();
                }
            }
        });

        ed5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    ed5.requestFocus();
                } else if (s.length() == 0) {
                    ed4.requestFocus();
                }
            }
        });

        TextView textView = (TextView) dialog.findViewById(R.id.number);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                Toast.makeText(getContext(), "REFRESGING ", Toast.LENGTH_LONG).show();
                productlist.clear();
                loadRecycleData();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.new_colour,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        sortparams = new LinkedHashMap<Object, Object>();
        //sort_layout=view.findViewById(R.id.sort_layout);
        sortadapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.sortOptions, android.R.layout.simple_list_item_1);
        sortDialog = new Dialog(getContext());
        sortDialog.setContentView(R.layout.sort_layout);
        sortlist = (ListView) sortDialog.findViewById(R.id.sort_list);
        sortlist.setAdapter(sortadapter);
        sortlist.setOnItemClickListener(this);




        pref = getActivity().getSharedPreferences("USER_DETAILS", 0);
        output = pref.getString(ACCESS_TOKEN, null);

        wholseller_id = pref.getString("Wholeseller_id", null);
        user_no = pref.getString("Wholeseller_mob", null);
        textView.setText("Contact :" + user_no);
        bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override

            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.filter) {
                    Bundle args = new Bundle();
                    args.putString("TeamName", item_clicked);

                    Bottom_sheet_dialog bottom_sheet_dialog = new Bottom_sheet_dialog(mlistner);
                    bottom_sheet_dialog.setArguments(args);
                    bottom_sheet_dialog.show(getFragmentManager(), bottom_sheet_dialog.getTag());

                    return true;
                }
                if (item.getItemId() == R.id.sort) {
                    sortDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    sortDialog.show();
                    return true;
                }
                if (item.getItemId() == R.id.premium) {
                    sendOtp();
                   /* LayoutInflater li = LayoutInflater.from(getContext());
                    View promptsView = li.inflate(R.layout.premium_dialogue_layout, null);
                    AlertDialog.Builder alertDialog=new AlertDialog.Builder(getContext());
                    alertDialog  .setTitle("PREMIUM PRODUCTS");
                    alertDialog .setMessage("TO VIEW PREMIUM PRODUCTS YOU NEED TO GET OTP FROM THE WHOLESALER");

                    alertDialog.setView(promptsView);
                    otp=(EditText) promptsView.findViewById(R.id.edittext);
                    TextView textView=(TextView)promptsView.findViewById(R.id.textView);
                    textView.setText("Contact :"+user_no);

                    alertDialog.setCancelable(false);
                    alertDialog  .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });


                    alertDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            chec_otp(otp.getText().toString());
                        }
                    });
                    AlertDialog alertDialoga = alertDialog.create();

                    alertDialoga.show();
*/

                    dialog.show();


                    return true;
                }
                return false;
            }
        });


        Log.e("CALLED", getActivity().getClass().getSimpleName());
        if (getActivity().getClass().getSimpleName().equalsIgnoreCase("MainActivity")) {
            toolbar = (getActivity()).findViewById(R.id.tool_bar);
            drawerLayout = (getActivity()).findViewById(R.id.drawer);
            toolbar.setOnMenuItemClickListener(this);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        drawerLayout.openDrawer(GravityCompat.START);
                    }
                }
            });

        }
        recyclerView = (RecyclerView) view.findViewById(R.id.navrecycler);
        productlist = new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setHasFixedSize(true);


        item_clicked = bundle.getString("Item_Clicked");
        class2 = bundle.getString("CLASS");
        Log.d(".........item click",item_clicked+class2);
       /* for (String key : bundle.keySet()) {
            if(key.equals("CLASS")) {
                class2 =bundle.getString(key);
            }
        }*/

        /* textView.append(item_clicked);*/

        if (class2.equals("FRAGMENT2")) {
            URL_DATA = create_url(ip + "gate/b2b/catalog/api/v1/product/all/category/Jewellery,"+item_clicked);
            Log.e("NEW URL ", URL_DATA + class2);
        }
        else if (class2.equals("SEARCH")) {
            URL_DATA = create_url(ip_cat + "/searching/facets");
            Log.d("url_data..", URL_DATA + class2);
        }
        else if(class2.equals("popular")){
            String classname=bundle.getString("CLASS");

            if(sortparams.isEmpty()==true) {
                sortparams.put(classname, "true");
            }
            else {
                sortparams.clear();
                sortparams.put(classname, "true");
            }
            URL_DATA = create_url(ip + "gate/b2b/catalog/api/v1/product/all/category/Jewellery,"+item_clicked);
            Log.d("url321....",URL_DATA);

        }
        else if(class2.equals("WeightLowToHigh")){
            String classname=bundle.getString("CLASS");

            if(sortparams.isEmpty()==true) {
                sortparams.put(classname, "true");
            }
            else {
                sortparams.clear();
                sortparams.put(classname, "true");
            }
            URL_DATA = create_url(ip + "gate/b2b/catalog/api/v1/product/all/category/Jewellery,"+item_clicked);
            Log.d("url321....",URL_DATA);

        }
        else if(class2.equals("WeightHighToLow")){
            String classname=bundle.getString("CLASS");

            if(sortparams.isEmpty()==true) {
                sortparams.put(classname, "true");
            }
            else {
                sortparams.clear();
                sortparams.put(classname, "true");
            }
            URL_DATA = create_url(ip + "gate/b2b/catalog/api/v1/product/all/category/Jewellery,"+item_clicked);
            Log.d("url321....",URL_DATA);

        }
        else if(class2.equals("newProducts")){
            String classname=bundle.getString("CLASS");

            if(sortparams.isEmpty()==true) {
                sortparams.put(classname, "true");
            }
            else {
                sortparams.clear();
                sortparams.put(classname, "true");
            }
            URL_DATA = create_url(ip + "gate/b2b/catalog/api/v1/product/all/category/Jewellery,"+item_clicked);
            Log.d("url321....",URL_DATA);

        }

        loadRecycleData();


        return view;
    }

    /////////////////////////////create url////////////////////////////////////////
    public String create_url(String val) {
        params = new LinkedHashMap<>();
        try {
            if (bundle.getString("CLASS") != null) {
                if(class2.equals("SEARCH")) {
                    params.put("queryText", item_clicked);
                }
                else if(class2.equals("popular")&&sortparams.isEmpty()==false){

                        params.putAll(sortparams);

                    }
                    else if(class2.equals("WeightLowToHigh") &&sortparams.isEmpty() == false) {

                            params.putAll(sortparams);

                    }
                else if(class2.equals("WeightHighToLow") &&sortparams.isEmpty() == false) {

                    params.putAll(sortparams);

                }
                else if(class2.equals("newProducts") &&sortparams.isEmpty() == false) {

                    params.putAll(sortparams);

                }



                params.put("wholesaler", wholseller_id);
                params.put("productType", "REGULAR");
                params.put("page", 0);
                params.put("pagesize", 20);
                create_urls = addQueryStringToUrlString(val, params);
                Log.d("params", params.toString());
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d("res.", create_urls);
        return create_urls;
    }

    String addQueryStringToUrlString(String url, final Map<Object, Object> parameters) throws UnsupportedEncodingException {
        if (parameters == null) {
            return url;
        }

        for (Map.Entry<Object, Object> parameter : parameters.entrySet()) {

            final String encodedKey = URLEncoder.encode(parameter.getKey().toString(), "UTF-8");
            final String encodedValue = URLEncoder.encode(parameter.getValue().toString(), "UTF-8");

            if (!url.contains("?")) {
                url += "?" + encodedKey + "=" + encodedValue;
            } else {
                url += "&" + encodedKey + "=" + encodedValue;
            }
        }

        return url;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.actionbar_icons, menu);
        super.onCreateOptionsMenu(menu, inflater);
       /* MenuItem mCartIconMenuItem = menu.findItem(R.id.cart);
        View actionView = MenuItemCompat.getActionView(mCartIconMenuItem);
       *//* View actionView = mCartIconMenuItem.getActionView();
         *//*

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Cart_Activity.class);
                startActivity(intent);

            }
        });

        // mCountTv.setText(String.valueOf(mCnt1));
*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.navrecycler:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity().getClass().getSimpleName().equalsIgnoreCase("MainActivity")) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }

                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (getActivity().getClass().getSimpleName().equalsIgnoreCase("MainActivity")) {
            toolbar.setNavigationIcon(R.drawable.drawer_toggle);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
   /* @Override
    public
    boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.search){
            Intent i = new Intent(getContext(),Search_Activity.class);
            startActivity(i);
            return true;
        }

        if (item.getItemId() == R.id.cart) {
            Intent i = new Intent(getContext(), Cart_Activity.class);
            startActivity(i);
            return true;
        }

            return super.onOptionsItemSelected(item);
    }*/

    public void loadRecycleData() {

       /*pref=getActivity().getSharedPreferences("USER_DETAILS",0);

        String output=pref.getString(ACCESS_TOKEN, null);
        Log.e("pref...........>>>>..","loadRecycleData................");
        Log.e("pref...........>>>>..",output);*/


       /* final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Loading....");
        progressDialog.show();*/
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                swipeContainer.setRefreshing(false);

                try {
                    progressBar.setVisibility(View.GONE);
                    JSONObject jsonObj = new JSONObject(response);
                    JSONObject jp = jsonObj.getJSONObject("products");

                    /* JSONArray jsPriceArray = jsonObj.getJSONArray("productPrice");*/

                    JSONArray ja_data = jp.getJSONArray("content");


                    int length = ja_data.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject jObj = ja_data.getJSONObject(i);


                            /*JSONObject priceObject = jsPriceArray.getJSONObject(i);

                            String price = String.valueOf(priceObject.getDouble("productFinalPrice"));*/
                        String sku = jObj.getString("sku");


                        String id = (jObj.getString("id"));
                        String name = (jObj.getString("name"));

                        JSONArray image_arr = jObj.getJSONArray("links");
                        JSONObject img_jObj = image_arr.getJSONObject(1);
                        String imageurl = img_jObj.getString("href");

                        Recy_model2 item = new Recy_model2(id, imageurl, sku, name);
                        productlist.add(item);


                    }


                    recycler_adapter2 = new Recycler_Adapter2(getActivity(), productlist);


                    recyclerView.setAdapter(recycler_adapter2);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                progressBar.setVisibility(View.GONE);

                NetworkResponse response = error.networkResponse;


                if (response != null && response.data != null) {
                    switch (response.statusCode) {
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
                        case 401:
                            Toast.makeText(getActivity().getApplicationContext(), "Session Expired!", Toast.LENGTH_SHORT).show();
                            session.logoutUser(getContext());

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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                //do sth here
                class2 = "";
                bundle.remove("");
                Intent i = new Intent(getActivity(), Search_Activity.class);
                startActivity(i);
                return true;
        }
        switch (item.getItemId()) {
            case R.id.cart:
                //do sth here
                Intent i = new Intent(getActivity(), Cart_Activity.class);
                startActivity(i);
                return true;
        }
        return false;
    }


    @Override
    public void onButtonClicked(String text) {

        productlist.clear();

        if (text.equalsIgnoreCase("clear")) {
            productlist.clear();
            URL_DATA = ip + "gate/b2b/catalog/api/v1/product/all/category/Jewellery," + item_clicked + "?wholesaler=" + wholseller_id + "&productType=REGULAR";
            loadRecycleData();
            return;
        }
        URL_DATA += text;

        loadRecycleData();

        Log.e("Applied", URL_DATA);
    }

    public void chec_otp(final String otp) {

        String url = ip1 + "/b2b/api/v1/user/checkOTP/" + otp + "?email=" + pref.getString(LKEY_NAME, null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {

                    if (response.equalsIgnoreCase("false")) {
                        BottomSheet.Builder builder1 = new BottomSheet.Builder(getContext());
                        builder1.setTitle("Wrong OTP");
                        builder1.show();
                    }
                    Log.e("RESULTTT", response.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                BottomSheet.Builder builder1 = new BottomSheet.Builder(getContext());
                builder1.setTitle("Sorry!,something went wrong");
                builder1.show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "bearer " + output);
                params.put("Content-Type", "application/json");
                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    public void sendOtp() {
        String url = ip1 + "/b2b/api/v1/user/otp/wholesaler/" + pref.getString("userid", null) + "?wholesaler=" + wholseller_id;
        Log.e("urrrrr", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("response", response);

                   /* LayoutInflater li = LayoutInflater.from(getContext());
                    View promptsView = li.inflate(R.layout.premium_dialogue_layout, null);
                    AlertDialog.Builder alertDialog=new AlertDialog.Builder(getContext());
                    alertDialog  .setTitle("PREMIUM PRODUCTS");
                    alertDialog .setMessage("TO VIEW PREMIUM PRODUCTS YOU NEED TO GET OTP FROM THE WHOLESALER");

                    alertDialog.setView(promptsView);
                    otp=(EditText) promptsView.findViewById(R.id.edittext);
                    TextView textView=(TextView)promptsView.findViewById(R.id.textView);
                    textView.setText("Contact :"+user_no);

                    alertDialog.setCancelable(false);
                    alertDialog  .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });


                    alertDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            chec_otp(otp.getText().toString());
                        }
                    });
                    AlertDialog alertDialoga = alertDialog.create();

                    alertDialoga.show();
*/

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                BottomSheet.Builder builder1 = new BottomSheet.Builder(getContext());
//                builder1.setTitle("Sorry!,something went wrong");
//                builder1.show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "bearer " + output);
                params.put("Content-Type", "application/json");
                return params;
            }
        };


        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

       /* RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);*/
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Fragment fragment_2 = new products_display_fragment();
        String val = (String)parent.getAdapter().getItem(position);
        Log.d("called", val);
        switch (val) {
            case "POPULARITY":



                if (bundle.getString("CLASS").equals("Search_Filter") || bundle.getString("CLASS").equals("Search_Activity")) {
                    sortparams.clear();
                    sortparams.put("popular", "true");
                    bundle.putString("Item_Clicked", item_clicked );
                    bundle.putString("CLASS", "Search_Filter");
                    bundle.putSerializable("SORT_VALUE", (Serializable) sortparams);


                } else {
                    bundle.putString("Item_Clicked", item_clicked );
                    bundle.putString("CLASS", "popular");
                   // bundle.putSerializable("FILTER_VALUE", (Serializable) filterparams);
                }


                fragment_2.setArguments(bundle);
                FragmentManager fManager1 = getActivity().getSupportFragmentManager();
                FragmentTransaction fTransaction1 = fManager1.beginTransaction();
                fTransaction1.detach(products_display_fragment.this);
                fTransaction1.attach(products_display_fragment.this);
                fTransaction1.commit();
                sortDialog.dismiss();
                break;

            case "WEIGHT-LOW TO HIGH":



                if (bundle.getString("CLASS").equals("Search_Filter") || bundle.getString("CLASS").equals("Search_Activity")) {
                    sortparams.clear();
                    sortparams.put("popular", "true");
                    bundle.putString("Item_Clicked", item_clicked );
                    bundle.putString("CLASS", "Search_Filter");
                    bundle.putSerializable("SORT_VALUE", (Serializable) sortparams);


                } else {
                    bundle.putString("Item_Clicked", item_clicked );
                    bundle.putString("CLASS", "WeightLowToHigh");
                    // bundle.putSerializable("FILTER_VALUE", (Serializable) filterparams);
                }


                fragment_2.setArguments(bundle);
                FragmentManager fManager2 = getActivity().getSupportFragmentManager();
                FragmentTransaction fTransaction2 = fManager2.beginTransaction();
                fTransaction2.detach(products_display_fragment.this);
                fTransaction2.attach(products_display_fragment.this);
                fTransaction2.commit();
                sortDialog.dismiss();
                break;
            case "WEIGHT-HIGH TO LOW":

                if (bundle.getString("CLASS").equals("Search_Filter") || bundle.getString("CLASS").equals("Search_Activity")) {
                    sortparams.clear();
                    sortparams.put("popular", "true");
                    bundle.putString("Item_Clicked", item_clicked );
                    bundle.putString("CLASS", "Search_Filter");
                    bundle.putSerializable("SORT_VALUE", (Serializable) sortparams);


                } else {
                    bundle.putString("Item_Clicked", item_clicked );
                    bundle.putString("CLASS", "WeightHighToLow");
                    // bundle.putSerializable("FILTER_VALUE", (Serializable) filterparams);
                }


                fragment_2.setArguments(bundle);
                FragmentManager fManager3 = getActivity().getSupportFragmentManager();
                FragmentTransaction fTransaction3 = fManager3.beginTransaction();
                fTransaction3.detach(products_display_fragment.this);
                fTransaction3.attach(products_display_fragment.this);
                fTransaction3.commit();
                sortDialog.dismiss();
                break;
            case "NEW PRODUCTS":

                if (bundle.getString("CLASS").equals("Search_Filter") || bundle.getString("CLASS").equals("Search_Activity")) {
                    sortparams.clear();
                    sortparams.put("NEW PRODUCT", "true");
                    bundle.putString("Item_Clicked", item_clicked );
                    bundle.putString("CLASS", "Search_Filter");
                    bundle.putSerializable("SORT_VALUE", (Serializable) sortparams);


                } else {
                    bundle.putString("Item_Clicked", item_clicked );
                    bundle.putString("CLASS", "newProducts");
                    // bundle.putSerializable("FILTER_VALUE", (Serializable) filterparams);
                }


                fragment_2.setArguments(bundle);
                FragmentManager fManager4 = getActivity().getSupportFragmentManager();
                FragmentTransaction fTransaction4 = fManager4.beginTransaction();
                fTransaction4.detach(products_display_fragment.this);
                fTransaction4.attach(products_display_fragment.this);
                fTransaction4.commit();
                sortDialog.dismiss();
                break;



        }

        }


    }


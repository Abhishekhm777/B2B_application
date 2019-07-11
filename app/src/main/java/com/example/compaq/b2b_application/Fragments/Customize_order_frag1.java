package com.example.compaq.b2b_application.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.Customize_Oder_Adapter1;
import com.example.compaq.b2b_application.Adapters.Manage_Adapter;
import com.example.compaq.b2b_application.Model.Recy_model2;
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
import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;

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
    String output,user_id;
    String wholseller_id;
    private Mange_card_fragment mange_card_fragment;
    ProgressBar progressBar;
    Button button;
    private Spinner spinner, pro_spinner, status_spinner, prioriyt_spinner, spinner2;
    private FragmentTransaction fragmentTransaction;
    public FragmentManager fragmentManager;
    /*private String path="";*/
    private SearchView searchView, search_by_name;
    public ArrayList<Recy_model2> productlist;
    Manage_category_frag1.TextClicked mCallback;
    private StringBuilder category_path = new StringBuilder();
    private List list = new ArrayList();

    Dialog dialog;
    private LinearLayout parentLinearLayout;
    private RelativeLayout newLayout;
    private String selected = "";
    RecyclerView recyclerView;
    private EditText cust_name, email, company_name,gstn,address;
    HashMap<String, String> all_id = new HashMap<String, String>();
    public List<String> product;
    private Button next, submit, add_product;
    private RadioButton byname, byCat;
    private LinearLayout cat_view, recy_view, second_search;

    public List<String> contact_array=new ArrayList<>();
    private AutoCompleteTextView autoCompleteTextView;
    private ImageView add_contact;

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

            sharedPref = getActivity().getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            output = sharedPref.getString(ACCESS_TOKEN, null);
            user_id = sharedPref.getString("userid", null);

            autoCompleteTextView=view.findViewById(R.id.cust_no);
            cust_name=view.findViewById(R.id.cust_name);
            gstn=view.findViewById(R.id.gstn);
            address=view.findViewById(R.id.address);
            email=view.findViewById(R.id.cust_email);
            company_name=view.findViewById(R.id.company_name);
            next=view.findViewById(R.id.next);
            next.setEnabled(false);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(contact_array.contains(autoCompleteTextView.getText().toString())){
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                          pager.setCurrentItem(1);
                    }
                    else {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        updateCustomer_Details();

                    }

                }
            });

            getContacts();
            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                        long id) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    next.setEnabled(true);
                    next.setText("NEXT");
                    next.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                    getUserDetail(autoCompleteTextView.getText().toString());

                }
            });

            cust_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {

                    if(!contact_array.contains(autoCompleteTextView.getText().toString())){
                        Log.e("YAHHH","SNKSSDSD");
                        next.setText("SAVE & NEXT");
                        next.setEnabled(true);
                        next.setBackgroundColor(getResources().getColor(R.color.skyBlue));
                    }
                    else {
                        getUserDetail(autoCompleteTextView.getText().toString());
                    }
                }
            });



           /* search_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    Custom_order_search_fragment add_contact_to_book=new Custom_order_search_fragment();
                    fragmentTransaction.replace(R.id.customize, add_contact_to_book).addToBackStack(null).commit();

                }
            });*/

        }

        return view;
    }
    /////////////////////get___Contacts////////////////
    public void getContacts( ){

        String url=ip+"uaa/b2b/contact-book/mobile-numbers/"+user_id;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public
            void onResponse(String response) {

                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        contact_array.add(jsonArray.getString(i));
                        Log.e("MOB",jsonArray.getString(i));
                    }
                    autoCompleteTextView.setAdapter(new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,contact_array));

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
    ///////////getUser_detailss///////////////


    public void getUserDetail(String mob ){

        String url = ip+"uaa/b2b/contact-book/get/mobile?";
        String uri=null;
        uri = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter("mobile",mob)
                .appendQueryParameter("user",user_id)
                .build().toString();
        StringRequest stringRequest=new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public
            void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    cust_name.setText(jsonObject.getString("name"));
                    email.setText(jsonObject.getString("email"));
                    company_name.setText(jsonObject.getString("firmName"));
                    gstn.setText(jsonObject.getString("gstNumber"));
                    address.setText(jsonObject.getString("address"));
                    next.setEnabled(true);
                    next.setBackgroundColor(getResources().getColor(R.color.skyBlue));

                }
                catch (Exception e) {
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

    public void updateCustomer_Details() {

        JSONObject mainJasan= new JSONObject();


        try {

            mainJasan.put("name",cust_name.getText().toString());
            mainJasan.put("mobileNumber",autoCompleteTextView.getText().toString());
            mainJasan.put("email",email.getText().toString());
            mainJasan.put("firmName",company_name.getText().toString());
            mainJasan.put("gstNumber",gstn.getText().toString());
            mainJasan.put("address",address.getText().toString());
            mainJasan.put("user",user_id);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = ip1+"/b2b/contact-book";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, mainJasan, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                Snackbar.make(view,"Contact Addedd Successfully",Snackbar.LENGTH_SHORT).show();
                pager.setCurrentItem(1);
/*
                Customize_order_frag1 customize_order_frag1 = (Customize_order_frag1)  getActivity().getSupportFragmentManager().findFragmentByTag("customize");

                customize_order_frag1.getContacts();
                (getActivity()).getSupportFragmentManager().popBackStackImmediate();*/

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Snackbar.make(view,"Something went wrong!!",Snackbar.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headr = new HashMap<>();
                headr.put("Authorization","bearer "+output);
                headr.put("Content-Type", "application/json");
                return headr;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }
}



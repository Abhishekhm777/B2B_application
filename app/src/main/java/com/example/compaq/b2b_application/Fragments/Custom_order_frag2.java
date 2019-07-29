package com.example.compaq.b2b_application.Fragments;


import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Activity.Add_new_product_activity;
import com.example.compaq.b2b_application.Activity.Custom_order_search_and_category_Activity;
import com.example.compaq.b2b_application.Activity.Customize_Order;
import com.example.compaq.b2b_application.Activity.Displaying_complete_product_details_Activity;
import com.example.compaq.b2b_application.Activity.Search_Activity;
import com.example.compaq.b2b_application.Adapters.Custom_Order_search_Adapter;
import com.example.compaq.b2b_application.Model.Top_model;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.compaq.b2b_application.Activity.Customize_Order.pager;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.editor;

/**
 * A simple {@link Fragment} subclass.
 */
public class Custom_order_frag2 extends Fragment {

    private SharedPreferences sharedPref;
    private String wholseller_id,output,image_url;
    private LinearLayout product_layout;
    private Bundle bundle;
    @BindView(R.id.byname) RadioButton byName;
    @BindView(R.id.byCategory) RadioButton byCategory;
    @BindView(R.id.add_new_product_btn) Button add_new;
    @BindView(R.id.confirm_butn) Button confirm_buttpn;
    @BindView(R.id.p_name)  TextView p_name;
    @BindView(R.id.category) TextView category;
    @BindView(R.id.sku) TextView sku;
    @BindView(R.id.selected_product) ImageView p_image_view;
    public Custom_order_frag2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_custom_order_frag2, container, false);
        ButterKnife.bind(this,view);

        sharedPref = getActivity().getSharedPreferences("USER_DETAILS", 0);

        product_layout=(LinearLayout)view.findViewById(R.id.product_layout);

        wholseller_id = sharedPref.getString("userid", null);
        output = sharedPref.getString(ACCESS_TOKEN, null);


        byName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    byCategory.setChecked(false);

                }
            }
        });
        byCategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    byName.setChecked(false);

                }
            }
        });

        byName.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                bundle=new Bundle();
                bundle.putString("byname","name");

                Intent intent=new Intent(getActivity().getApplicationContext(), Custom_order_search_and_category_Activity.class).putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            }
        });

        byCategory.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                bundle=new Bundle();
                bundle.putString("byname","cat");

                Intent intent=new Intent(getActivity().getApplicationContext(), Custom_order_search_and_category_Activity.class).putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            }
        });

        add_new.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

              /*  Intent intent=new Intent(getActivity(), Add_new_product_activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());*/
                ActivityCompat.startActivityForResult(getActivity(), new Intent(getActivity(), Add_new_product_activity.class), 0, null);
            }
        });

        confirm_buttpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(2);
            }
        });

          return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String id=sharedPref.getString("cust_id", null);
        product_layout.setVisibility(View.GONE);
        if(id!=null){
            getProduct(id);
        }
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);


    }


    RequestQueue requestQueue;
    public void getProduct( String id){
        if(requestQueue==null)
        {
            requestQueue = Volley.newRequestQueue(getActivity());
        }
        String url = ip+"gate/b2b/catalog/api/v1/product/"+id+"?wholesaler="+wholseller_id;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url   , new Response.Listener<String>() {
            @Override
            public
            void onResponse(String response) {
                try {
                    product_layout.setVisibility(View.VISIBLE);
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject pro_object=jsonObject.getJSONObject("resourceSupport");
                    p_name.setText(pro_object.getString("name"));
                    sku.setText(pro_object.getString("sku"));
                   /* purity.setText(pro_object.getString(""));*/
                    JSONArray cat_array=pro_object.getJSONArray("categoriesPath");
                    String string=cat_array.getString(0);
                    String output = string.replace(",", " >  ");
                    category.setText(output);
                    JSONArray jsonArray=pro_object.getJSONArray("imageGridFsID");
                    image_url=jsonArray.get(0).toString();

                    Glide.with(getActivity()).load("https://server.mrkzevar.com/gate/b2b/catalog/api/v1/assets/image/"+image_url).into(p_image_view);
                    editor.putString("cust_image_id",image_url).apply();
                    editor.commit();

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

                            Snackbar.make(getView(), "Sorry! could't reach server", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            break;
                        case 400:
                            Snackbar.make(getView(), "Sorry! No Products Available", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            break;
                        case 417:

                            Snackbar.make(getView(), "Sorry! No Products Available", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
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
        requestQueue.add(stringRequest);
    }
}

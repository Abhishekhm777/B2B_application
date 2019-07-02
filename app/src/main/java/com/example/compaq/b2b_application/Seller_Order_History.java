package com.example.compaq.b2b_application;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.Cart_recycler_Adapter;
import com.example.compaq.b2b_application.Adapters.EndlessRecyclerViewScrollListener;
import com.example.compaq.b2b_application.Adapters.Order_to_bprocessed_Adapter;
import com.example.compaq.b2b_application.Adapters.PaginationScrollListener;
import com.example.compaq.b2b_application.Adapters.Seller_orderhistory_Adaper;
import com.example.compaq.b2b_application.Fragments.Company_info_fragment;
import com.example.compaq.b2b_application.Fragments.Seller_Order_HIst_Frag1;
import com.example.compaq.b2b_application.Model.Cart_recy_model;
import com.example.compaq.b2b_application.Model.OrderTobe_customer_model;
import com.example.compaq.b2b_application.Model.Seller_order_history;
import com.example.compaq.b2b_application.Model.Seller_order_model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.example.compaq.b2b_application.MainActivity.custom_order;
import static com.example.compaq.b2b_application.MainActivity.ip;
import static com.example.compaq.b2b_application.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.SessionManagement.PREF_NAME;

public class Seller_Order_History extends AppCompatActivity {
    public Toolbar toolbar;
    public SharedPreferences sharedPref;
    private FragmentTransaction fragmentTransaction;
    public  FragmentManager fragmentManager;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller__order__history);
        toolbar=(Toolbar)findViewById(R.id.cart_toolbar);
        toolbar.setTitle("Order History");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.order_his_filter_layout);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, new Seller_Order_HIst_Frag1());
        fragmentTransaction.commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // click on 'up' button in the action bar, handle it here
                onBackPressed();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed()
    {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        }
        else {

            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter_menu_icon, menu);
        return super.onCreateOptionsMenu(menu);
    }

}

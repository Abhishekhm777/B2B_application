package com.example.compaq.b2b_application.Activity;

import android.content.SharedPreferences;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.compaq.b2b_application.Adapters.Order_to_bprocessed_Adapter;
import com.example.compaq.b2b_application.Helper_classess.AlertDialogManager;
import com.example.compaq.b2b_application.Fragments.Order_toB_processed_fragmenrt1;
import com.example.compaq.b2b_application.Model.Seller_order_history;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.Helper_classess.SessionManagement;

import org.json.JSONArray;

import java.util.ArrayList;

public class Order_to_bProsessed extends AppCompatActivity {
    public Toolbar toolbar;
    public static TextView pricetext_view;
    public RecyclerView recyclerView;
    public Order_to_bprocessed_Adapter order_to_bprocessed_adapter;
    public ArrayList<Seller_order_history> productlist;
    /* SharedPreferences pref;*/
    public Bundle bundle;
    public String userid="";
    public SharedPreferences sharedPref;
    public   SharedPreferences.Editor myEditor;

    private FragmentTransaction fragmentTransaction;
    public  FragmentManager fragmentManager;
    public JSONArray ja_data;
    public ImageView empty_imageview;
    public double total_price=0;
    AlertDialogManager alert = new AlertDialogManager();
    public Button check_out;
    ProgressBar progressBar;
    SessionManagement session;
    Button emptybtn,clear_bag;
    private AppBarLayout appBarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_to_b_prosessed);
        toolbar=(Toolbar)findViewById(R.id.cart_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appBarLayout=(AppBarLayout)findViewById(R.id.appBarLayout) ;

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, new Order_toB_processed_fragmenrt1(),"Company");
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

  /*  @Override
    public void sendText(String text) {
        Order_toB_processed_fragmenrt1 company_info_fragment=new Order_toB_processed_fragmenrt1();
        company_info_fragment.updateText(text);
    }*/
}

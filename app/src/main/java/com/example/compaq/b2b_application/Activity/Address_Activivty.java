package com.example.compaq.b2b_application.Activity;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.compaq.b2b_application.Adapters.Adress_adapter;
import com.example.compaq.b2b_application.Fragments.AddNewAddess;
import com.example.compaq.b2b_application.Fragments.MyAddress_fragment;
import com.example.compaq.b2b_application.Model.Address_model;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;

public class Address_Activivty extends AppCompatActivity implements AddNewAddess.OnGreenFragmentListener {
Toolbar toolbar;
 public  RecyclerView recyclerView;
   public ArrayList<Address_model> addressArray;
   public String type,line,city,state,country,zipcode,output;
  public   Adress_adapter adapter;
    private SharedPreferences sharedPref;
    private FragmentTransaction fragmentTransaction;
    public  FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address__activivty);

        toolbar=(Toolbar)findViewById(R.id.address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, new MyAddress_fragment());

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
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();



    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    public void messageFromGreenFragment( ) {
        MyAddress_fragment myAddress_fragment=new MyAddress_fragment();
        myAddress_fragment.youveGotMail();
    }
}

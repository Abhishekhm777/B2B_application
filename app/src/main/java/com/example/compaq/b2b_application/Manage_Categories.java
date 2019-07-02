package com.example.compaq.b2b_application;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.compaq.b2b_application.Fragments.Add_new_Category;
import com.example.compaq.b2b_application.Fragments.Manage_category_frag1;
import com.example.compaq.b2b_application.Fragments.Mange_exixisting_fragment;
import com.example.compaq.b2b_application.Fragments.Top_categories;

import java.util.ArrayList;

public class Manage_Categories extends AppCompatActivity {
    private FragmentTransaction fragmentTransaction;
    public FragmentManager fragmentManager;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage__categories);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("Manage Categories");

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.manage, new Manage_category_frag1(),"manage_frag1");
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


}

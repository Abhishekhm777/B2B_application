package com.example.compaq.b2b_application.Activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.compaq.b2b_application.Fragments.Custom_order_search_fragment;
import com.example.compaq.b2b_application.Fragments.Custom_serch_by_category_frag;
import com.example.compaq.b2b_application.R;

public class Custom_order_search_and_category_Activity extends AppCompatActivity {
    private FragmentTransaction fragmentTransaction;
    public FragmentManager fragmentManager;
    private Toolbar toolbar;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_order_search_and_category_);

        toolbar=(Toolbar)findViewById(R.id.search_tool);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle=getIntent().getExtras();
        key=bundle.getString("byname");
        if(key.equalsIgnoreCase("name")) {
            Custom_order_search_fragment custom_order_search_fragment=new Custom_order_search_fragment();
            Bundle bundle1 = new Bundle();
            bundle1.putString("path", null);
            custom_order_search_fragment.setArguments(bundle);
           getSupportFragmentManager().beginTransaction().replace(R.id.search_frame, custom_order_search_fragment,"customize_search").addToBackStack(null).commit();

        }
        if(key.equalsIgnoreCase("cat")){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.search_frame, new Custom_serch_by_category_frag(), "custom_by_cat");
            fragmentTransaction.commit();
        }


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
        super.onBackPressed();

        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        }
        else {

            super.onBackPressed();
        }
    }
}

package com.example.compaq.b2b_application.Activity;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.example.compaq.b2b_application.Fragments.Custom_order_search_fragment;
import com.example.compaq.b2b_application.Fragments.Custom_serch_by_category_frag;
import com.example.compaq.b2b_application.Fragments.Offline_order_search_fragment;
import com.example.compaq.b2b_application.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Offline_order extends AppCompatActivity {

    @Nullable @BindView(R.id.offline_tool) Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_order);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Offline_order_search_fragment custom_order_search_fragment=new Offline_order_search_fragment();
        getSupportFragmentManager().beginTransaction().add(R.id.offline_frame, custom_order_search_fragment,"customize_search").addToBackStack(null).commit();

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

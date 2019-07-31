package com.example.compaq.b2b_application.Activity;

import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.compaq.b2b_application.Fragments.Custom_order;
import com.example.compaq.b2b_application.Fragments.Offline_fragment1;
import com.example.compaq.b2b_application.Fragments.Offline_order_search_fragment;
import com.example.compaq.b2b_application.Helper_classess.Back_alert_class;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.databinding.ActivityBuyerCustomeOrderBinding;

import java.util.HashMap;

public class Buyer_Custome_Order extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityBuyerCustomeOrderBinding binding= DataBindingUtil.setContentView(this,R.layout.activity_buyer__custome__order);
        setSupportActionBar(binding.customtool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Custom_order offline_order_search_fragment=new Custom_order();
        getSupportFragmentManager().beginTransaction().add(R.id.offline_frame, offline_order_search_fragment,"custom_order").commit();
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
            final Back_alert_class back_alert_class=new Back_alert_class(this);
            back_alert_class.showAlert();
            back_alert_class.yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    back_alert_class.getMyDialogue().dismiss();
                   finish();
                }
            });
    }
}

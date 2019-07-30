package com.example.compaq.b2b_application.Activity;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.compaq.b2b_application.Fragments.Custom_order_search_fragment;
import com.example.compaq.b2b_application.Fragments.Custom_serch_by_category_frag;
import com.example.compaq.b2b_application.Fragments.Offline_fragment1;
import com.example.compaq.b2b_application.Fragments.Offline_order_search_fragment;
import com.example.compaq.b2b_application.Helper_classess.Back_alert_class;
import com.example.compaq.b2b_application.Model.Offline_order_model;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.databinding.ActivityOfflineOrderBinding;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Offline_order extends AppCompatActivity implements Offline_order_search_fragment.SubmitClicked{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityOfflineOrderBinding binding= DataBindingUtil.setContentView(this,R.layout.activity_offline_order);

        setSupportActionBar(binding.offlineTool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Offline_order_search_fragment offline_order_search_fragment=new Offline_order_search_fragment();
        getSupportFragmentManager().beginTransaction().add(R.id.offline_frame, offline_order_search_fragment,"customize_search").addToBackStack(null).commit();

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
            final Back_alert_class back_alert_class=new Back_alert_class(this);
            back_alert_class.showAlert();

            back_alert_class.getYes().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    back_alert_class.getMyDialogue().dismiss();
                    finish();

                }
            });


        }
    }

    @Override
    public void sendText(HashMap<String,String> text) {
        Offline_fragment1 frag = (Offline_fragment1)getSupportFragmentManager().findFragmentByTag("offline_frag1");
        frag.updateText(text);
    }



}

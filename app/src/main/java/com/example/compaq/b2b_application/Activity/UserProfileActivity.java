package com.example.compaq.b2b_application.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.compaq.b2b_application.Fragments.Personal_infoFragment;
import com.example.compaq.b2b_application.Fragments.UserProfileFragment;
import com.example.compaq.b2b_application.R;

public class UserProfileActivity extends AppCompatActivity {
    public Toolbar toolbar;
    public  static TextView titleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        toolbar=(Toolbar)findViewById(R.id.profile_tool);
        titleView=(TextView)findViewById(R.id.profile_title);
        titleView.append("USER PROFILE ");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Fragment user_fragment=new UserProfileFragment();
        FragmentManager fragmentManager=getSupportFragmentManager();
       // fragmentManager.popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.profile_activity_layout, user_fragment).commit();



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public  void onBackPressed()
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

package com.example.compaq.b2b_application.Activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.compaq.b2b_application.Adapters.Tablayout_adapter;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.Helper_classess.SessionManagement;

public class Sign_up_Activity extends AppCompatActivity {
    public Button button;
    public String username="";
   public String password="";
    public  String email="";
    public TabLayout tabLayout;
    public static  ViewPager viewPager;
    public Toolbar toolbar;
    Tablayout_adapter  adapter;
public SessionManagement session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_);

        toolbar=(Toolbar)findViewById(R.id.signUptool);
        TextView textView=(TextView)findViewById(R.id.signUptitle);
        textView.append("Sign Up   ");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tabLayout=(TabLayout)findViewById(R.id.tabs);
        viewPager=(ViewPager)findViewById(R.id.viewpager);


        tabLayout.addTab(tabLayout.newTab().setText("CATALOGUE"));
        tabLayout.addTab(tabLayout.newTab().setText("ROLE"));
        tabLayout.addTab(tabLayout.newTab().setText("USER INFO"));
        //tabLayout.addTab(tabLayout.newTab().setText("OTP"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

         adapter=new Tablayout_adapter(this,getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(0);




        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(viewPager.getCurrentItem()>tab.getPosition()) {


                    viewPager.setCurrentItem(tab.getPosition(), true);

                }
                tabLayout.getTabAt(viewPager.getCurrentItem()).select();
                final InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
      /*  session = new SessionManagement(getApplicationContext());
button=(Button)findViewById(R.id.reg_register);
button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        user_edittext=(TextInputEditText)findViewById(R.id.reg_username);
        passw_edittext=(TextInputEditText)findViewById(R.id.reg_password);
        email_edittext=(TextInputEditText)findViewById(R.id.reg_email);




       username = user_edittext.getText().toString();
        password = passw_edittext.getText().toString();
        Log.d("Sign.....up",username);
        email = email_edittext.getText().toString();
        session.creatSignupSession(username, password);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }
});*/

    }



    @Override
    public
    boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.bluedasrt, menu);
        return super.onCreateOptionsMenu(menu);
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


    public static void set_view(int i){


        viewPager.setCurrentItem(i,true);

    }
}

package com.example.compaq.b2b_application.Activity;

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
import android.widget.Button;
import android.widget.TextView;

import com.example.compaq.b2b_application.Adapters.Tablayout_adapter;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.Helper_classess.SessionManagement;

public class Sign_up_Activity extends AppCompatActivity {
    public TextInputEditText user_edittext,passw_edittext,email_edittext;
    public Button button;
    public String username="";
   public String password="";
    public  String email="";
    public TabLayout tabLayout;
    public static  ViewPager viewPager;
    public Toolbar toolbar;
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

        tabLayout.addTab(tabLayout.newTab().setText("RETAILER"));
        tabLayout.addTab(tabLayout.newTab().setText("WHOLESALER"));
        tabLayout.addTab(tabLayout.newTab().setText("MANUFACTURER"));
        tabLayout.addTab(tabLayout.newTab().setText("OTP"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final Tablayout_adapter  adapter=new Tablayout_adapter(this,getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(viewPager.getCurrentItem()>tab.getPosition()) {

                    viewPager.setCurrentItem(tab.getPosition(), true);
                }

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

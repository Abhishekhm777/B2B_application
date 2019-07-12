package com.example.compaq.b2b_application.Activity;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.badoualy.stepperindicator.StepperIndicator;
import com.example.compaq.b2b_application.Adapters.Stepper_Adapter;
import com.example.compaq.b2b_application.Adapters.Tablayout_adapter;
import com.example.compaq.b2b_application.Fragments.Customize_order_frag1;
import com.example.compaq.b2b_application.Fragments.Manage_category_frag1;
import com.example.compaq.b2b_application.R;

public class Customize_Order extends AppCompatActivity {
    private FragmentTransaction fragmentTransaction;
    public FragmentManager fragmentManager;
    private Toolbar toolbar;
    public static ViewPager pager;
    private SharedPreferences sharedPref;
    private Dialog  myDialogue;
    private SharedPreferences.Editor editor;
    private Button yes,cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize__order);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("Customize Order");

        sharedPref = this.getSharedPreferences("USER_DETAILS", 0);
        editor = sharedPref.edit();
        editor.putString("cust_id",null).apply();
        editor.commit();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myDialogue = new Dialog(this);
        myDialogue.setContentView(R.layout.back_alert_dialog_layout);
        myDialogue.setCanceledOnTouchOutside(false);
        yes=myDialogue.findViewById(R.id.yes);
        cancel=myDialogue.findViewById(R.id.cancel);

        final Stepper_Adapter adapter=new Stepper_Adapter(this,getSupportFragmentManager(),5);

        pager = findViewById(R.id.pager);
        assert pager != null;
        pager.setAdapter(adapter);
        pager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });

        /*   pager.setAdapter(new PagerAdapter(getSupportFragmentManager(),pager.getChildCount()));
         */
        final StepperIndicator indicator = findViewById(R.id.stepper_indicator);
        // We keep last page for a "finishing" page
        indicator.setViewPager(pager, true);

        indicator.addOnStepClickListener(new StepperIndicator.OnStepClickListener() {
            @Override
            public void onStepClicked(int step) {
                   /* pager.setCurrentItem(step, true);*/
             Log.e("Log",String.valueOf(pager.getCurrentItem()));
                Log.e("Log",String.valueOf(step));
                if(step<pager.getCurrentItem()&&pager.getCurrentItem()!=4){
                    pager.setCurrentItem(step, true);

                }


            }
        });


      /*
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.customize, new Customize_order_frag1(),"customize");
        fragmentTransaction.commit();*/
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // click on 'up' button in the action bar, handle it here
                myDialogue.show();
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialogue.dismiss();
                        onBackPressed();

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialogue.dismiss();
                    }
                });

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

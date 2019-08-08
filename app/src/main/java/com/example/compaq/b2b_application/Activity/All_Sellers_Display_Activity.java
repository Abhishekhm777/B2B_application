package com.example.compaq.b2b_application.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.example.compaq.b2b_application.Adapters.Seller_portal_fragment1Adapter;
import com.example.compaq.b2b_application.Adapters.Sellerport_acvtivityAdaper;
import com.example.compaq.b2b_application.Helper_classess.Bottom_moresheet;
import com.example.compaq.b2b_application.Helper_classess.Bottom_sheet_dialog;
import com.example.compaq.b2b_application.Model.SellerPortal_model;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;

import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;

public class All_Sellers_Display_Activity extends AppCompatActivity implements Bottom_moresheet.BottomSheetListner {
public Toolbar toolbar;
public RecyclerView sellser_recycler;
    public SellerPortal_model sellerPortal_model;
    public Seller_portal_fragment1Adapter seller_portal_fragment1_adapter;
    public ArrayList<SellerPortal_model> productlist;
    public SharedPreferences sharedPref;
    private ActionBarDrawerToggle mToggle;
    private DrawerLayout drawerLayout;
    public Bottom_sheet_dialog.BottomSheetListner mlistner;
    public TabLayout tabLayout;
    public  TextView textCartItemCount;
    public ScaleAnimation scaleAnimation;
    public ViewPager viewPager;
    private String cart_item_no;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_portal_);
        toolbar=(Toolbar)findViewById(R.id.sellerportal_toolbar);
        setSupportActionBar(toolbar);
        searchView=(SearchView)findViewById(R.id.sellers_search);

        sharedPref =getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
       toolbar.setNavigationIcon(R.drawable.ic_dehaze_black_24dp);
       toolbar.setNavigationOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Bottom_moresheet bottom_sheet_dialog=new Bottom_moresheet();

               bottom_sheet_dialog.show(getSupportFragmentManager(),bottom_sheet_dialog.getTag());
           }
       });
        tabLayout=(TabLayout)findViewById(R.id.seller_portal_tabs);
        viewPager=(ViewPager)findViewById(R.id.seller_activityadapter);
        tabLayout.addTab(tabLayout.newTab().setText("BEST SELLERS"));
        tabLayout.addTab(tabLayout.newTab().setText("MY CONNECTIONS"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final Sellerport_acvtivityAdaper adapter=new Sellerport_acvtivityAdaper(this,getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
    @Override
    public
    boolean onCreateOptionsMenu (Menu menu){
       /* MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.actionbar_icons, menu);
        return super.onCreateOptionsMenu(menu);*/
        getMenuInflater().inflate(R.menu.onlycarticon, menu);
        final MenuItem menuItem = menu.findItem(R.id.onlycart);
        View  actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
        setupBadge(getApplicationContext());
        /* cart_shared_preference = getApplicationContext().getSharedPreferences("CART_ITEMS", 0);*/
        cart_item_no=sharedPref.getString("no_of_items","");
        textCartItemCount.setText(cart_item_no);
        /* setupBadge();*/
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }
    public  void setupBadge(Context context) {
        scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.REVERSE, 0.7f, Animation.REVERSE, 0.7f);
        scaleAnimation.setDuration(1000);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);
        /*  cart_shared_preference = context.getSharedPreferences("CART_ITEMS", 0);*/
        sharedPref =context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        cart_item_no=sharedPref.getString("no_of_items","");
        if(cart_item_no.equalsIgnoreCase("0")){
            textCartItemCount.setVisibility(View.GONE);
        }
        if(Integer.parseInt(cart_item_no)>0){

            textCartItemCount.setVisibility(View.VISIBLE);
        }
        textCartItemCount.setText(cart_item_no);
        textCartItemCount.setAnimation(scaleAnimation);

    }
  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.getItemId() == R.id.more) {
            Bottom_moresheet bottom_sheet_dialog=new Bottom_moresheet();

            bottom_sheet_dialog.show(getSupportFragmentManager(),bottom_sheet_dialog.getTag());

            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // click on 'up' button in the action bar, handle it here
                onBackPressed();
                return true;
            case R.id.onlycart:
                Intent i = new Intent(this, Cart_Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onButtonClicked(String text) {

    }
    @Override
    protected void onResume() {
        super.onResume();

        invalidateOptionsMenu();


    }

    @Override
    protected void onPause() {
        super.onPause();



    }


}

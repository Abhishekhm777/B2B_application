package com.example.compaq.b2b_application.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.Inner_RecyclerAdapter4;
import com.example.compaq.b2b_application.Adapters.RecyclerAdapter3;
import com.example.compaq.b2b_application.Adapters.ViewpageAdapter2;
import com.example.compaq.b2b_application.Fragments.Custom_serch_by_category_frag;
import com.example.compaq.b2b_application.Fragments.DisplayingCompletProduct_fragment1;
import com.example.compaq.b2b_application.Fragments.Dropdown_fragment;
import com.example.compaq.b2b_application.Model.Inner_Recy_model;
import com.example.compaq.b2b_application.Model.Recycler_model3;
import com.example.compaq.b2b_application.Model.Viewpager2_model;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Activity.MainActivity.wishlist2;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;

public
class Displaying_complete_product_details_Activity extends AppCompatActivity implements Dropdown_fragment.SubmitClicked{
    private ViewPager viewPager;
    private ViewpageAdapter2 viewPageAdapter2;
    private Context mContext;
    private ArrayList<Viewpager2_model> productlist;
    private ArrayList<Recycler_model3> detProductlist;
    private RecyclerView main_recyclerView;
    private Recycler_model3 main2_listner;
    private RecyclerAdapter3 main2_recycler_adapter;
    private Inner_Recy_model inner_recy_listner;
    public Inner_RecyclerAdapter4 inner_recycler_adapter;
    private ArrayList<Inner_Recy_model> details_list;
    private String URL_DATA="";
    public String Detail_URL_DATA="";
    public Bundle bundle;
    public TextView textView,textView2,skutext;
    public RadioGroup radioGroup ;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor myEditor;
    private String name="";
    private String id="";
    private String item_name="";
    private String sku="";
    public String productPrice="";
    public String FinalProductPrice="";
    private   String d_url="";
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private ToggleButton toggleButton;
     private int json_length=0;
   /* public SharedPreferences cart_shared_preference;*/
   public TextView textCartItemCount;
    private TextView wish_items;
    private Dialog dialog;
    private String cart_item_no="";
    private View actionView;
    private ScaleAnimation scaleAnimation2;
    private String sku_wishlilst;
    private String output;
    /* public SharedPreferences.Editor cartEditor;*/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


         sharedPref =getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
         output=sharedPref.getString(ACCESS_TOKEN, null);
         myEditor = sharedPref.edit();

        Toolbar toolbar2 = (Toolbar) findViewById(R.id.new_toolbar);

        setSupportActionBar(toolbar2);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.product_display_frame, new DisplayingCompletProduct_fragment1(), "product_display");
        fragmentTransaction.commit();

    }


    @Override
    public boolean onCreateOptionsMenu (Menu menu){
       /* MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.actionbar_icons, menu);
        return super.onCreateOptionsMenu(menu);*/
        getMenuInflater().inflate(R.menu.main2_icons, menu);

        final MenuItem menuItem = menu.findItem(R.id.cart);
        final MenuItem wishitem = menu.findItem(R.id.heart);

       View  actionView = MenuItemCompat.getActionView(menuItem);
       View actionView1=MenuItemCompat.getActionView(wishitem);

        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        setupBadge(getApplicationContext());
       /* cart_shared_preference = getApplicationContext().getSharedPreferences("CART_ITEMS", 0);*/
        cart_item_no=sharedPref.getString("no_of_items","");
        textCartItemCount.setText(cart_item_no);
        /* setupBadge();*/
        actionView1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        onOptionsItemSelected(wishitem);
    }
});
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }
    public  void setupBadge(Context context) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.REVERSE, 0.7f, Animation.REVERSE, 0.7f);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // click on 'up' button in the action bar, handle it here
                onBackPressed();
                return true;
            case R.id.cart:
                Intent i = new Intent(this, Cart_Activity.class);
                startActivity(i);
                return true;
            case R.id.heart:
                //do sth here
                Intent ii = new Intent(this, Wishlist_Activity.class);
                startActivity(ii);
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

    @Override
    protected void onResume() {
        super.onResume();

        invalidateOptionsMenu();

    }

    @Override
    public void sendText() {
        invalidateOptionsMenu();
        Log.e("YEAAAAAAAAAAAA","FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
    }
}





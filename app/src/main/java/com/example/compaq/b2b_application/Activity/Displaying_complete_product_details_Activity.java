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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Activity.MainActivity.wishlist2;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;

public
class Displaying_complete_product_details_Activity extends AppCompatActivity {
    private DrawerLayout drawer;
    private ViewPager viewPager;
    private ViewpageAdapter2 viewPageAdapter2;
    private Context mContext;
    private ArrayList<Viewpager2_model> productlist;
    private ArrayList<Recycler_model3> detProductlist;
    private RecyclerView main_recyclerView;
    private Recycler_model3 main2_listner;
    private RecyclerAdapter3 main2_recycler_adapter;

    public RecyclerView innerRecyclerview;
    private Inner_Recy_model inner_recy_listner;
    public Inner_RecyclerAdapter4 inner_recycler_adapter;
    private ArrayList<Inner_Recy_model> details_list;
    private String URL_DATA="";
    public String Detail_URL_DATA="";
    public Bundle bundle;
    public TextView textView,textView2,skutext;
    public RadioGroup radioGroup ;
    private int page=0;
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
   private TextView textCartItemCount;
    private TextView wish_items;
    private Dialog dialog;
    private String cart_item_no="";
    private View actionView;
    private ScaleAnimation scaleAnimation2;
    private String sku_wishlilst;
    private String output;
    /* public SharedPreferences.Editor cartEditor;*/
    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        sharedPref =getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
         output=sharedPref.getString(ACCESS_TOKEN, null);

        myEditor = sharedPref.edit();

        Toolbar toolbar2 = (Toolbar) findViewById(R.id.new_toolbar);

      /*  toolbar2.inflateMenu(R.menu.actionbar_icons);*/






        setSupportActionBar(toolbar2);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        dialog = new Dialog(Displaying_complete_product_details_Activity.this);
        dialog.setContentView(R.layout. progress_layout);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        productlist=new ArrayList<>();
        detProductlist=new ArrayList<>();

        viewPager = (ViewPager)findViewById(R.id.viewpager2);
        TabLayout tabLayout=(TabLayout)findViewById(R.id.dot_tablayout);

        tabLayout.setupWithViewPager(viewPager, true);
        loadRecycleData();
///////////////////////////////////////////////////////////////////////////////////////////////////
        scaleAnimation2 = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        scaleAnimation2.setDuration(1000);
        BounceInterpolator  bounceInterpolator = new BounceInterpolator();
        scaleAnimation2.setInterpolator(bounceInterpolator);

        toggleButton=(ToggleButton)findViewById(R.id.button_favorite) ;

        if(wishlist2.contains(sku_wishlilst)){
            toggleButton.setChecked(true);
        }
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                compoundButton.startAnimation(scaleAnimation2);
                if(b){

                    addToWhishlist(sku_wishlilst);
                }
                if(!b){
                    removeFromWhish(sku_wishlilst);

                }
            }
        });


   ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Button addtocart=(Button)findViewById(R.id.addtocart);

        Button buynow=(Button)findViewById(R.id.buynow);

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* sharedPref=getApplicationContext().getSharedPreferences("User_information",0);*/

                String user_i =sharedPref.getString("userid","");

               /* userCart_details(user_i);*/

                Bundle bundle=getIntent().getExtras();
                name= Objects.requireNonNull(bundle).getString("Item_Clicked");
                id=bundle.getString("id");

                Bundle bundle1=new Bundle();
                bundle1.putString("item_name",name);
                bundle1.putString("Item_Clicked",id);
                Dropdown_fragment dropdown_fragment =new Dropdown_fragment();
                dropdown_fragment.setArguments(bundle);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, dropdown_fragment);

                fragmentTransaction.addToBackStack(null).commit();
            }
        });
//////////////////////////<<<<<<<<<<<<<<<<<<<<//////////////////////////////////////////

        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String noOfitems=sharedPref.getString("no_of_items","");
                if (noOfitems.equalsIgnoreCase("0"))
                {
                    Toast.makeText(getApplicationContext(),"Your cart is empty!!",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent i = new Intent(getApplicationContext(), Check_out__Activity.class);
                    startActivity(i);
                }
            }
        });
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

        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
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
   public void setupwishBadge(){

    wish_items.setText(cart_item_no);

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loadRecycleData(){
        Bundle bundle=getIntent().getExtras();
        name= Objects.requireNonNull(bundle).getString("Item_Clicked");
       id=bundle.getString("item_name");
       sku_wishlilst=bundle.getString("sku");
        item_name=bundle.getString("item_name");
        URL_DATA=ip+"gate/b2b/catalog/api/v1/product/"+name;
        Log.e("YAVDUUU",URL_DATA);
        dialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONObject jp=jsonObj.getJSONObject("resourceSupport");

                     sku=(jp.getString("sku"));

                    JSONArray ja_data = jp.getJSONArray("links");


                    Log.d("OutPut", String.valueOf(ja_data.length()));

                    int length = ja_data.length();
                    for(int i=0; i<length; i++) {
                        JSONObject jObj = ja_data.getJSONObject(i);

                        String sname=(jObj.getString("rel"));

                        if(sname.equals("imageURl"))
                        {
                            String sliderImageUrl=jObj.getString("href");

                            Viewpager2_model item=new Viewpager2_model(sliderImageUrl);

                            productlist.add(item);











                           /* for(int j=0;j<image_arr.length();j++)
                            {
                                JSONObject img_jObj = image_arr.getJSONObject(j);
                                String relid=(img_jObj.getString("rel"));
                                if(relid.equals("imageURl")){
                                    String sliderImageUrl=img_jObj.getString("href");
                                    d_url=(jObj.getString("id"));


                                    Viewpager2_model item=new Viewpager2_model(sliderImageUrl);

                                    productlist.add(item);

                                }
                            }*/

                        }


                       /* TextView pricetextView=(TextView)findViewById(R.id.priceTextviewdetail) ;
                        pricetextView.setText("₹ "+FinalProductPrice);

                        TextView discountTextView=(TextView)findViewById(R.id.priceTextviewdetail2);
                        discountTextView.setText("₹ "+productPrice);
                        discountTextView.setPaintFlags(discountTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
*/



                    }
                    detailsRecycleData();
                    viewPageAdapter2=new ViewpageAdapter2(Displaying_complete_product_details_Activity.this,productlist);



                    viewPager.setAdapter(viewPageAdapter2);

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public
            void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() {

                sharedPref=getSharedPreferences("USER_DETAILS",0);

                String output=sharedPref.getString(ACCESS_TOKEN, null);
                Log.d("ACESSSSSS>>>>>>>>TOKEN",output);
                Map<String, String> params = new HashMap<>();
                params.put("Authorization","bearer "+output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(Displaying_complete_product_details_Activity.this);
        requestQueue.add(stringRequest);
    }



    private void detailsRecycleData(){

        main_recyclerView=(RecyclerView)this.findViewById(R.id.main2recycler) ;
        main_recyclerView.setLayoutManager(new LinearLayoutManager(Displaying_complete_product_details_Activity.this,LinearLayoutManager.VERTICAL,false));

        main_recyclerView.setHasFixedSize(true);


        Log.d("URL %!!!... , . ,. , . ",d_url);





       String Detail_URL_DATA=ip+"gate/b2b/catalog/api/v1/product/"+name;


        StringRequest stringRequest=new StringRequest(Request.Method.GET,  Detail_URL_DATA, new Response.Listener<String>() {
            @Override
            public
            void onResponse(String response) {



                try {
                    JSONObject   jsonObj = new JSONObject(response);
                    JSONObject jp=jsonObj.getJSONObject("resourceSupport");
                    JSONArray ja_data = jp.getJSONArray("specification");


                    Log.d("OutPut", String.valueOf(ja_data.length()));
                   /* details_list=new ArrayList<>();*/
                    int length = ja_data.length();

                    for(int i=0; i<length; i++) {
                        JSONObject jObj = ja_data.getJSONObject(i);
                        String heading=(jObj.getString("heading"));
                        main2_listner=new Recycler_model3(heading);
                        detProductlist.add(main2_listner);

                        JSONArray attribute = jObj.getJSONArray ("attributes");
                        details_list=new ArrayList<>();
                        String key,values;

                        for(int j=0;j<attribute.length();j++) {
                            try {
                                JSONObject attributeobjects = attribute.getJSONObject(j);

                                 key = (attributeobjects.getString("key"));
                                JSONArray attribute_values = attributeobjects.getJSONArray("values");

                                 values = attribute_values.getString(0);
                                if (values.equalsIgnoreCase("")) {

                                    continue;
                                }
                                inner_recy_listner = new Inner_Recy_model(key, values);
                            }
                            catch (Exception e){
                                continue;
                            }

                            if (heading.equalsIgnoreCase("PRODUCT DETAILS") && j == 0) {
                                inner_recy_listner = new Inner_Recy_model("SKU", sku);
                                details_list.add(inner_recy_listner);


                                inner_recy_listner = new Inner_Recy_model("Product Name", item_name);
                                details_list.add(inner_recy_listner);


                            }




                                inner_recy_listner=new Inner_Recy_model(key,values);
                                details_list.add(inner_recy_listner);




                        }

                        main2_listner.setArrayList(details_list);
                    }


                  /*  main2_listner.setArrayList(details_list);*/
                          dialog.dismiss();
                    main2_recycler_adapter = new RecyclerAdapter3(Displaying_complete_product_details_Activity.this, detProductlist);
                    main_recyclerView.setAdapter(main2_recycler_adapter);
                    main_recyclerView.setNestedScrollingEnabled(false);


                } catch (JSONException e) {
                    e.printStackTrace();
                }







            }
        }, new Response.ErrorListener() {
            @Override
            public
            void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() {

                sharedPref=getSharedPreferences("USER_DETAILS",0);

                String output=sharedPref.getString(ACCESS_TOKEN, null);
                Log.d("ACESSSSSS>>>>>>>>TOKEN",output);
                Map<String, String> params = new HashMap<>();
                params.put("Authorization","bearer "+output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(Displaying_complete_product_details_Activity.this);
        requestQueue.add(stringRequest);
    }



    ///////////////////add to cart button pressed/////////////////////
    public void userCart_details(String us_id) {
        String url=ip+"gate/b2b/order/api/v1/cart/customer/"+us_id;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectss = new JsonObjectRequest (Request.Method.GET,url,null,

                new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {


                        try {


                            JSONArray jsonArray=response.getJSONArray("items");
                            json_length= jsonArray.length();
                            Log.e("Length....", String.valueOf(json_length));

                            /*cart_shared_preference = getApplicationContext().getSharedPreferences("CART_ITEMS", 0);
                            cartEditor= cart_shared_preference.edit();*/
                            myEditor.putString("no_of_items", String.valueOf(json_length)).apply();
                            myEditor.commit();
                           /* cart_shared_preference = getActivity().getSharedPreferences("CART_ITEMS", 0);
                            cartEditor.putString("no_of_items", String.valueOf(jsonArray.length()+1)).apply();
                            cartEditor.commit();*/
                            /*MainActivity mActivity= new MainActivity();
                            mActivity.setupBadge(getContext());
                            Displaying_complete_product_details_Activity displayingcompleteproductdetailsActivity= new Displaying_complete_product_details_Activity();
                            displayingcompleteproductdetailsActivity.setupBadge(getContext());*/


                            if (jsonArray.length() == 0 ) {
                                Bundle bundle=getIntent().getExtras();
                                name= Objects.requireNonNull(bundle).getString("Item_Clicked");
                                id=bundle.getString("id");

                                Bundle bundle1=new Bundle();
                                bundle1.putString("item_name",name);
                                bundle1.putString("Item_Clicked",id);
                                Dropdown_fragment dropdown_fragment =new Dropdown_fragment();
                                dropdown_fragment.setArguments(bundle);
                                fragmentManager = getSupportFragmentManager();
                                fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.main2, dropdown_fragment);
                                fragmentTransaction.addToBackStack(null).commit();
                            }
                            for(int i=0;i<jsonArray.length();i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String userCart_product_id = jsonObject1.getString("product");
                                Log.e("cart Alli irodu",userCart_product_id);

                                Log.e("Adding Alli irodu",item_name);
                                if(item_name.equalsIgnoreCase(userCart_product_id)) {
                                    Toast.makeText(getApplicationContext(), "Product is alreasy added to Cart ", Toast.LENGTH_SHORT).show();

                                    return;
                                }

                                if (i == jsonArray.length() - 1 ) {
                                    Bundle bundle=getIntent().getExtras();
                                    name= Objects.requireNonNull(bundle).getString("Item_Clicked");
                                    id=bundle.getString("id");

                                    Bundle bundle1=new Bundle();
                                    bundle1.putString("item_name",name);
                                    bundle1.putString("Item_Clicked",id);
                                    Dropdown_fragment dropdown_fragment =new Dropdown_fragment();
                                    dropdown_fragment.setArguments(bundle);
                                    fragmentManager = getSupportFragmentManager();
                                    fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.main2, dropdown_fragment);

                                    fragmentTransaction.addToBackStack(null).commit();
                                }
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Bundle bundle=getIntent().getExtras();
                name= Objects.requireNonNull(bundle).getString("Item_Clicked");
                id=bundle.getString("id");

                Bundle bundle1=new Bundle();
                bundle1.putString("item_name",name);
                bundle1.putString("Item_Clicked",id);
                Dropdown_fragment dropdown_fragment =new Dropdown_fragment();
                dropdown_fragment.setArguments(bundle);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main2, dropdown_fragment);

                fragmentTransaction.addToBackStack(null).commit();
                // ADD_CUSTOMER_CART();

            }
        }){
            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap< >();
                params.put("Authorization","bearer "+output);
                return params;
            }

        };
        requestQueue.add(jsonObjectss);


    }
    private void addToWhishlist(final String sku){

        String url = ip1+"/b2b/api/v1/user/wish-list/"+sharedPref.getString("userid","")+"/AD?wishList="+sku;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    //JsonObject jsonObject=new JsonObject(response);
                    String id=response.getString("id");

                    /*wishlist.add(sku);*/
                    Toast.makeText(getApplicationContext(), "Added to wishlist", Toast.LENGTH_SHORT).show();
                    wishlist2.add(sku);

                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "bearer "+output);
                params.put("Content-Type", "application/json");
                return params;
            }

        };

        requestQueue.add(objectRequest);

    }
    private void removeFromWhish(final String sku){

        String url = ip1+"/b2b/api/v1/user/wish-list/"+sharedPref.getString("userid","")+"/DL?wishList="+sku;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {



                    /* wishlist.remove(sku);*/
                    Toast.makeText(getApplicationContext(), "Removed from wishlist", Toast.LENGTH_SHORT).show();
                    wishlist2.remove(sku);

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "bearer "+output);
                params.put("Content-Type", "application/json");
                return params;
            }

        };

        requestQueue.add(objectRequest);

    }
    @Override
    protected void onResume() {
        super.onResume();

        invalidateOptionsMenu();


    }

}





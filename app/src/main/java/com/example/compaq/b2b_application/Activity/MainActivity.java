package com.example.compaq.b2b_application.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Fragments.GenericSearchFragment;
import com.example.compaq.b2b_application.Fragments.products_display_fragment;
import com.example.compaq.b2b_application.Helper_classess.AlertDialogManager;
import com.example.compaq.b2b_application.Helper_classess.FirebaseIDService;
import com.example.compaq.b2b_application.Fragments.Childrens_fragment;
import com.example.compaq.b2b_application.Fragments.HomeFragment_1;
import com.example.compaq.b2b_application.Helper_classess.ConnectivityStatus;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.Helper_classess.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.compaq.b2b_application.Fragments.products_display_fragment.URL_DATA;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;

public
class MainActivity extends AppCompatActivity {
  /* //local URLs
    public static String ip="http://192.168.1.21:8040/";
    public static String ip_cat="http://192.168.1.21:8009/";
    public static String ip1="http://192.168.1.21:8769/uaa";*/

   ///////////////Server Base URLS
    public static String ip="https://server.mrkzevar.com/";
    public static String ip_cat="https://server.mrkzevar.com/gate/b2b/catalog/api/v1";
    public static String ip1 ="https://server.mrkzevar.com/uaa";


    public  FragmentManager fragmentManager;
    private ActionBarDrawerToggle mToggle;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private FragmentTransaction fragmentTransaction;
    public android.support.v4.app.ActionBarDrawerToggle mDrawer;
    private MenuItem menuItem;
    public String SUB_URL = "";
    public String sname = "";
    public ArrayList<String> arrayList;
    public  ArrayList<String> sub_arraylist;

    public int position = 0;
    public static final ArrayList<String> wishlist2 = new ArrayList<>();

    public products_display_fragment productsdisplayfragment;
    FrameLayout actContent;
    com.example.compaq.b2b_application.Adapters.ExpandableListAdapter listAdapter;
    public ScaleAnimation scaleAnimation;
    ExpandableListView expListView;
    public List<String> listDataHeader = new ArrayList<>();
    HashMap<String, List<String>> listDataChild = new HashMap<>();
     public  SharedPreferences sharedPref;
    public   SharedPreferences.Editor myEditor;
    public  LinearLayout home;
    public  TextView textCartItemCount;
   NavigationView navigationView;
    private int lastExpandedPosition = -1;
   public int json_length=0;
   public String cart_item_no="";
    int mCartItemCount = 10;
    AlertDialogManager alert = new AlertDialogManager();
    // Session Manager Class
    SessionManagement session;

    // Button Logout
    Button btnLogout;
    BottomNavigationView bottomNavigationView;
    private ImageView header_image;
    private String parent_path;
    private String wholseller_id;
    private String output;
    private String user_id;
    private  Bundle bundle;


    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        getApplicationContext().registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        sharedPref=getSharedPreferences("USER_DETAILS",0);
        myEditor = sharedPref.edit();
       /* cart_shared_preference = getApplicationContext().getSharedPreferences("CART_ITEMS", 0);
        cartEditor=cart_shared_preference.edit();*/
        drawerLayout =  findViewById(R.id.drawer);
        getinfo(MainActivity.this);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        toolbar = findViewById(R.id.tool_bar);

/*
        mToggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();*/
        setSupportActionBar(toolbar);


       arrayList=new ArrayList<>();
       sub_arraylist=new ArrayList<>();


///////////////////////////////////////
        FirebaseIDService firebaseIDService=new FirebaseIDService();
        firebaseIDService.onTokenRefresh(MainActivity.this);

       ///////////////////////////////////////////////
/*

  wish_t=findViewById(R.id.whish_d);
  order_his_t=findViewById(R.id.order_his_d);
        account_t=findViewById(R.id.myaccount_d);
        seller_t=findViewById(R.id.seller_d);
        logout_t=findViewById(R.id.logout_d);
        custom_order=findViewById(R.id.custom_order);
        home=findViewById(R.id.home);

wish_t.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        drawerLayout.closeDrawers();
        Intent i = new Intent(getApplicationContext(), Wishlist_Activity.class);
        startActivity(i);
        */
/*fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainframe, new Whish_list_fragment()).addToBackStack(null).commit();*//*

    }
});
order_his_t.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        drawerLayout.closeDrawers();
       */
/* fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainframe, new Oder_History()).addToBackStack(null).commit();*//*

        Intent i = new Intent(getApplicationContext(), Orders_History_Activity.class);
        startActivity(i);
    }
});
account_t.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        drawerLayout.closeDrawers();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainframe, new Personal_info_fragment()).addToBackStack(null).commit();
    }
});
seller_t.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        drawerLayout.closeDrawers();
        MainActivity.this.finish();
        Intent i = new Intent(getApplicationContext(), Seller_Dashboard_Activity.class);
        startActivity(i);
    }
});

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                MainActivity.this.finish();
                Intent i = new Intent(getApplicationContext(), All_Sellers_Display_Activity.class);
                startActivity(i);
            }
        });
custom_order.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        drawerLayout.closeDrawers();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainframe, new Custom_order()).addToBackStack(null).commit();

    }
});



        logout_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                session.logoutUser(MainActivity.this);
            }
        });

*/


        //>>>>>>>>>>>>>Login Part.............>>>>>>>>>>>>>>>>>>>>>>>>//////
        // Session class instance
        session = new SessionManagement(getApplicationContext());
        output=sharedPref.getString(ACCESS_TOKEN, null);

       wholseller_id= sharedPref.getString("Wholeseller_id", null);


        /*session.checkLogin();
*/
      /*  userInformation();*/

        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */


        // get user data from session


        // name
        /*String name = user.get(SessionManagement.KEY_NAME);
                TextView headerText=(TextView)findViewById(R.id.name);
                headerText.append(name);*/
        // email


        // displaying user data
       /* lblName.setText(Html.fromHtml("Name: <b>" + name + "</b>"));
        lblEmail.setText(Html.fromHtml("Email: <b>" + email + "</b>"));



        Logout button click event
     */


//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>end>>>>>>>>>>>>>>>>>>>>>>>>>>>>>///



                if(getSupportActionBar()!=null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().show();
                }


            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.mainframe, new HomeFragment_1());

            fragmentTransaction.commit();
////////////////////////////////Bottom navigation/////////////////////
       /* bottomNavigationView=findViewById(R.id.bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.mainframe, new HomeFragment_1());

                    fragmentTransaction.commit();

                    return true;
                }
                if (item.getItemId() == R.id.more) {
                    fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainframe, new Profile_fragment());

                fragmentTransaction.commit();
                return true;
            }
                return false;
            }
        });

*/








            ///////////////////////Expandable_list View///////////////////////////////////



        expListView =  findViewById(R.id.navList);

            final View listHeaderView = getLayoutInflater().inflate(R.layout.header, null, false);

           /* expListView.addHeaderView(listHeaderView);*/

            TextView header=findViewById(R.id.header_name);
             header_image=findViewById(R.id.header_image) ;
            header_image.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View view) {
                    int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE);
                    if(ActivityCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                2000);
                    }
                    else {
                        startGallery();
                    }

                }

            });




      /*  sharedPref=getSharedPreferences("USER_DETAILS",0);*/
        String userfi=sharedPref.getString("firstname", null);
       Log.e("USERNAMEEE",userfi);
        header.setText(userfi);

///////////////////////Api call//////////////////////////////
        fetchingCatalog();



        listAdapter = new com.example.compaq.b2b_application.Adapters.ExpandableListAdapter(MainActivity.this, listDataHeader, listDataChild);


            //expandAll();
            // Listview Group click listener
            expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {
                    //  mDrawerLayout.closeDrawers();
                    // Toast.makeText(getApplicationContext(),
                    // "Group Clicked " + listDataHeader.get(groupPosition),
                    // Toast.LENGTH_SHORT).show();


                    return false;
                }
            });

            // Listview Group expanded listener
            expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                @Override
                public void onGroupExpand(int groupPosition) {
                    //  mDrawerLayout.closeDrawers();
                /*Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();*/

                    if (lastExpandedPosition != -1
                            && groupPosition != lastExpandedPosition) {
                        expListView.collapseGroup(lastExpandedPosition);
                     
                    }
                    lastExpandedPosition = groupPosition;
                }
            });

            // Listview Group collasped listener
            expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                @Override
                public void onGroupCollapse(int groupPosition) {


                }
            });

            // Listview on child click listener
            expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    drawerLayout.closeDrawers();
                    expListView.collapseGroup(groupPosition);


                    String mi;
                    String prnt;

                    if(childPosition==0){

                         mi=listDataHeader.get((groupPosition));
                         /*if(mi.equalsIgnoreCase("More")){
                             mi=listDataChild.get(
                                     listDataHeader.get(groupPosition)).get(
                                     childPosition);
                         }*/
                         Toast.makeText(getApplicationContext(),mi,Toast.LENGTH_SHORT).show();

                    }
                    else {

                        mi=listDataHeader.get((groupPosition));
                        prnt = listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition);
                      mi+=","+prnt;


                    }

                   /* if (mi.equalsIgnoreCase("SIGN OUT")) {
                        session.logoutUser(MainActivity.this);
                        return false;
                    }
                    if (mi.equalsIgnoreCase("ORDER HISTORY")) {
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.mainframe, new Oder_History()).addToBackStack(null).commit();
                        return false;
                    }

                    if (mi.equalsIgnoreCase("PERSONAL INFO")) {
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.mainframe, new Personal_info_fragment()).addToBackStack(null).commit();
                        return false;
                    }
                    if (mi.equalsIgnoreCase("WISHLIST")) {
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.mainframe, new Whish_list_fragment()).addToBackStack(null).commit();
                        return false;
                    }
                    if (mi.equalsIgnoreCase("COMPANY INFO")) {
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.mainframe, new Order_toB_processed_fragmenrt1()).addToBackStack(null).commit();
                        return false;
                    }*/
                    if (mi.equalsIgnoreCase("SELLER")) {
                        MainActivity.this.finish();
                        Intent i = new Intent(getApplicationContext(), All_Sellers_Display_Activity.class);
                        startActivity(i);
                        return false;

                    } else {
                        if(childPosition==0) {
                            bundle = new Bundle();

                            bundle.putString("Item_Clicked", mi);
                            bundle.putString("CLASS","FRAGMENT2");
                            productsdisplayfragment = new products_display_fragment();
                            productsdisplayfragment.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, productsdisplayfragment).addToBackStack(null).commit();
                            return false;
                        }

                        else{
                            bundle = new Bundle();
                            bundle.putString("Item_Clicked", mi);
                            bundle.putString("CLASS","FRAGMENT2");
                            bundle.putString("All",listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));

                            loadCatalogue(mi,bundle);
                        }

                        // TODO Auto-generated method stub
                       /* Toast.makeText(
                                getApplicationContext(),
                                listDataHeader.get(groupPosition)
                                        + " : "
                                        + listDataChild.get(
                                        listDataHeader.get(groupPosition)).get(
                                        childPosition), Toast.LENGTH_LONG)
                                .show();*/

                       /* productsdisplayfragment = new products_display_fragment();
                        productsdisplayfragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, productsdisplayfragment).addToBackStack(null).commit();
*/
                        return false;
                    }

                }
            });

        }
    public void loadCatalogue(final  String name,final Bundle bundle){


        final String url="https://server.mrkzevar.com/gate/b2b/catalog/api/v1/category/byFirstLevelCategory/b2b/Jewellery,"+name+"?wholesaler="+wholseller_id;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public
            void onResponse(String response) {

                try {

                    JSONArray jsonArray = new JSONArray(response);



                    if(jsonArray.length()==0){
                        productsdisplayfragment = new products_display_fragment();
                        productsdisplayfragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, productsdisplayfragment).addToBackStack(null).commit();
                    }

                 else {
                        Childrens_fragment childrens_fragment = new Childrens_fragment();
                        childrens_fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, childrens_fragment).addToBackStack(null).commit();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        },new Response.ErrorListener() {
            @Override
            public
            void onErrorResponse(VolleyError error) {




                NetworkResponse response = error.networkResponse;


                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 404:
                            BottomSheet.Builder builder = new BottomSheet.Builder(MainActivity.this);
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;
                        case 400:
                            BottomSheet.Builder builder1 = new BottomSheet.Builder(MainActivity.this);
                            builder1.setTitle("Sorry! No Products Available");
                            builder1.show();
                            break;
                        case 417:
                            BottomSheet.Builder builder2 = new BottomSheet.Builder(MainActivity.this);
                            builder2.setTitle("Sorry! No Products Available");
                            builder2.show();
                            break;
                        case 401:
                            Toast.makeText(getApplicationContext(),"Session Expired!",Toast.LENGTH_SHORT).show();
                            session.logoutUser(MainActivity.this);

                    }
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() {


                Map<String, String> params = new HashMap<>();
                params.put("Authorization","bearer "+output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }



    ///////////////////////////////////////////////////////////////////////////////
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
       /* expListView.setIndicatorBounds(expListView.getRight()- 80, expListView.getWidth());*/


    }
    // method to expand all groups
        private void expandAll () {
            int count = listAdapter.getGroupCount();
            for (int i = 0; i < count; i++) {
                expListView.expandGroup(i);
            }
        }


        /*
         * Preparing the list data
         */


////////////////////Fetching Catalog  and its categories/////////////////////////////
       private RequestQueue requestQueue;
       private void fetchingCatalog () {

            if(requestQueue==null)
            {
                 requestQueue = Volley.newRequestQueue(MainActivity.this);

            }

           URL_DATA=ip_cat+"/category/byFirstLevelCategory/b2b/Jewellery?wholesaler="+wholseller_id;

           StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{

                        JSONArray ja_data = new JSONArray(response);
                        int length = ja_data.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject jObj = ja_data.getJSONObject(i);

                            JSONArray parent_array=jObj.getJSONArray("parent");
                            String parent=parent_array.getString(0);


                                sname = (jObj.getString("name").replaceAll("\\s", ""));
                                /*.replaceAll("\\s", ""));*/
                            listDataHeader.add(sname);


                            position = i;

                            SUB_URL =   ip_cat+"/category/byFirstLevelCategory/b2b/"+parent+","+sname+"?wholesaler="+sharedPref.getString("Wholeseller_id", null);
                            Log.e("UUURRRLLL",SUB_URL);
                            fetchSubCategories(i, sname);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("Sidebar", "EXCEPETION");

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    NetworkResponse response = error.networkResponse;
                    if(response != null && response.data != null){
                        switch(response.statusCode){
                            case 404:
                                BottomSheet.Builder builder = new BottomSheet.Builder(MainActivity.this);
                                builder.setTitle("Sorry! could't reach server");
                                builder.show();
                                break;
                            case 401:
                              Toast.makeText(getApplicationContext(),"Session Expired!",Toast.LENGTH_SHORT).show();
                              /* new Reffressh_token().getToken(MainActivity.this);*/
                                session.logoutUser(MainActivity.this);
                        }
                    }
                }
            }){
                @Override
                public Map<String, String> getHeaders() {

                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization","bearer "+output);
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };

            requestQueue.add(stringRequest);
        }

        private void fetchSubCategories ( final int i, final String sname1){
            StringRequest stringRequest = new StringRequest(Request.Method.GET, SUB_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray ja_data = new JSONArray(response);

                        if (ja_data.length() == 0) {
                            final List<String> submenu = new ArrayList<>();
                            submenu.add("All  "+sname1);
                            listDataChild.put(listDataHeader.get(i), submenu);
                        } else {
                            final List<String> submenu = new ArrayList<>();
                            submenu.add("All  "+sname1);
                            for (int j = 0; j < ja_data.length(); j++) {
                                JSONObject jObj = ja_data.getJSONObject(j);

                                String subnames = jObj.getString("name");

                                submenu.add(subnames);
                            }
                            listDataChild.put(listDataHeader.get(i), submenu);
                        }
                        // Header, Child data
                        expListView.setAdapter(listAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

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
            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(stringRequest);
        }
//////////////////Opening Gallery On Clicking imageview On Drawer///////////////
    private void startGallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        cameraIntent.putExtra("crop", "true");
        cameraIntent.putExtra("aspectX", 100);
        cameraIntent.putExtra("aspectY", 100);
        cameraIntent.putExtra("outputX", 256);
        cameraIntent.putExtra("outputY", 356);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1000);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super method removed
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                Uri returnUri = data.getData();
                Bitmap bitmapImage ;
                Bitmap bitimage = null;
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), returnUri);
                    bitimage = getResizedBitmap(bitmapImage, 400);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                header_image.setImageBitmap(bitimage);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //////////////Resizing Images.///////////////////////////////////////
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
///////>>>>>>>>>>>>>Fetching user information<<<<<<<<<<<<<<<<<<<<<<<<<<<</////////
/*
public void userInformation ( ){

            String url="http://192.168.100.15:8769/uaa/b2b/api/v1/user/info";
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


        @Override
        public void onResponse(String response) {
            try {
                JSONObject jObj = new JSONObject(response);

                String userid=jObj.getString("id");
                Log.e("USER    id",userid);

                myEditor.putString("userid",userid).apply();

                String userfirstname=jObj.getString("firstName");
                Log.e("USER    name",userfirstname);
                myEditor.putString("firstname",userfirstname);

                myEditor.commit();
                myEditor.apply();
                cartInformation(userid);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    }){
        @Override
        public Map<String, String> getHeaders() {

            sharedPref=getSharedPreferences("USER_DETAILS",0);

            String output=sharedPref.getString(ACCESS_TOKEN, null);
            Log.d("ACESSSSSS>>>>>>>>TOKEN",output);
            Map<String, String> params = new HashMap<String, String>();
            params.put("Authorization","bearer "+output);
            params.put("Content-Type", "application/x-www-form-urlencoded");
            return params;
        }
    };




    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
    requestQueue.add(stringRequest);
}



    public void cartInformation (String id ){

        String url=ip+"gate/b2b/order/api/v1/cart/customer/"+id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray jsonArray=jObj.getJSONArray("items");
                    json_length= jsonArray.length();
                    Log.e("Length....", String.valueOf(json_length));


                    myEditor.putString("no_of_items", String.valueOf(json_length)).apply();
                    myEditor.commit();




                    String cartid=jObj.getString("id");
                    Log.e("Cart Id from Jason",cartid);
                    myEditor.putString("cartid",cartid).apply();
                    myEditor.commit();
                    myEditor.apply();

                    */
/*sharedPref=getSharedPreferences("User_information",0);*//*


                    String cart =sharedPref.getString("cartid","");
                    Log.e("Cart Id from SHARED",cart);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                myEditor.putString("cartid","0").apply();
                myEditor.commit();
                myEditor.apply();


            }
        }){
            @Override
            public Map<String, String> getHeaders() {

                sharedPref=getSharedPreferences("USER_DETAILS",0);

                String output=sharedPref.getString(ACCESS_TOKEN, null);

                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                params.put("Authorization","bearer "+output);
                return params;
            }
        };




        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

*/

        @Override
        public
        boolean onCreateOptionsMenu (Menu menu){
          /*  MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.actionbar_icons, menu);
*/
            scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
            scaleAnimation.setDuration(1000);
            BounceInterpolator bounceInterpolator = new BounceInterpolator();
            scaleAnimation.setInterpolator(bounceInterpolator);
            getMenuInflater().inflate(R.menu.actionbar_icons, menu);

            final MenuItem menuItem = menu.findItem(R.id.cart);

            View actionView = MenuItemCompat.getActionView(menuItem);
            actionView .setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            textCartItemCount =  actionView.findViewById(R.id.cart_badge);

            setupBadge(getApplicationContext());
            textCartItemCount.setAnimation(scaleAnimation);
          /*  cart_shared_preference = getApplicationContext().getSharedPreferences("CART_ITEMS", 0);*/
            sharedPref =getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.getItemId() == R.id.search) {
           // bundle.remove("CLASS");
            Fragment search=new GenericSearchFragment();
           getSupportFragmentManager().beginTransaction().replace(R.id.mainframe,search).addToBackStack(null).commit();
          //  Intent i = new Intent(this, Search_Activity.class);
           // startActivity(i);
            return true;
        }
        if (item.getItemId() == R.id.cart) {
            Intent i = new Intent(this, Cart_Activity.class);
            startActivity(i);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    /*private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }*/
/*

        public
        boolean onOptionsItemSelected (MenuItem item){

            if (mToggle.onOptionsItemSelected(item)) {
                return true;
            }
            if (item.getItemId() == R.id.search) {
                Intent i = new Intent(this, Search_Activity.class);
                startActivity(i);
                return true;
            }
            if (item.getItemId() == R.id.cart) {
                Intent i = new Intent(this, Cart_Activity.class);
                startActivity(i);
                return true;

            }

            return super.onOptionsItemSelected(item);
        }
*/

        public
        void setDrawerLocked ( boolean enabled){
            if (enabled) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }


        }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(!ConnectivityStatus.isConnected(getApplicationContext())){
                alert.showAlertDialog(MainActivity.this, "No Internet!", "Please Check your internet Connection", "internet");
            }
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        mToggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        setSupportActionBar(toolbar);
        getApplicationContext().registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


    }

    @Override
    protected void onPause() {
        super.onPause();
        getApplicationContext().registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


    }

   /* public  void getinfo(Context context){
wishlist2.clear();
        String url =ip1+"/b2b/api/v1/user/info";
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    //JsonObject jsonObject=new JsonObject(response);
                    JSONArray jsonArray=response.getJSONArray("wishList");
                    if(jsonArray.length()==0){
                        return;

                    }

                    if(jsonArray.length()!=0)
                    {
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String productLink=jsonObject.getString("productLink");


                            wishlist2.add(productLink);


                        }
                    }



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
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "bearer "+output);
                params.put("Content-Type", "application/json");
                return params;
            }

        };

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(objectRequest);

    }
*/

    public  void getinfo(Context context){
        wishlist2.clear();
        String url = ip1+"/b2b/api/v1/user/info";
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    //JsonObject jsonObject=new JsonObject(response);
                    JSONArray jsonArray=response.getJSONArray("wishList");
                    if(jsonArray.length()==0){
                        return;

                    }

                    if(jsonArray.length()!=0)
                    {
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String productLink=jsonObject.getString("productLink");



                            wishlist2.add(productLink);


                        }
                    }



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
                params.put("Authorization", "bearer " + output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

        };

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(objectRequest);

    }
}


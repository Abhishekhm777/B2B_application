package com.example.compaq.b2b_application.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Activity.Cart_Activity;
import com.example.compaq.b2b_application.Activity.Seller_Dashboard_Activity;
import com.example.compaq.b2b_application.Adapters.RecyclerAdapter1;
import com.example.compaq.b2b_application.Adapters.RecyclerItemClickListener;
import com.example.compaq.b2b_application.Adapters.ViewpageAdapter1;
import com.example.compaq.b2b_application.Model.Home_recy_model;
import com.example.compaq.b2b_application.Model.Viewpager_model1;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.Activity.All_Sellers_Display_Activity;
import com.example.compaq.b2b_application.Helper_classess.SessionManagement;
import com.volcaniccoder.bottomify.BottomifyNavigationView;
import com.volcaniccoder.bottomify.OnNavigationItemChangeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment_1 extends Fragment implements Toolbar.OnMenuItemClickListener{
    private ViewPager viewPager;
    private ViewpageAdapter1 viewPageAdapter1;
    RecyclerView recyclerView;
    public  Home_recy_model home_recy_model;
    RecyclerAdapter1 adapter;
    ArrayList<Home_recy_model> productList;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private CardView cardView;
    private Context context;
    ProgressBar progressBar;
    Fragment fragment_2;
    SharedPreferences pref;
    private Toolbar toolbar;
    private String output;
    BottomNavigationView bottomNavigationView;
    private SessionManagement session;
    private  ArrayList<Viewpager_model1> slider_array ;
    private String user_id;
    private DrawerLayout drawerLayout;
    private SwipeRefreshLayout swipeContainer;
    public  HomeFragment_1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home_fragment_1, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        slider_array = new ArrayList<>();
        progressBar=(ProgressBar)view.findViewById(R.id.progress);
        session = new SessionManagement(getActivity().getApplicationContext());
       /* viewPager.setAdapter(viewPageAdapter1);*/

      /*  bottomNavigationView=(BottomNavigationView)view.findViewById(R.id.bottom) ;*/
        TabLayout tabLayout=(TabLayout)view.findViewById(R.id.dot_tablayout);

        tabLayout.setupWithViewPager(viewPager, true);
        pref=getActivity().getSharedPreferences("USER_DETAILS",0);

         output=pref.getString(ACCESS_TOKEN, null);
        user_id= pref.getString("Wholeseller_id", null);
        productList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setHasFixedSize(true);
        getSlider( );
        loadRecycleData();



        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                  slider_array.clear();
                getSlider();


            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.new_colour,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

      /*  productList.add(new Home_recy_model(R.drawable.belt, "Belt"));

        productList.add(new Home_recy_model(R.drawable.kada, "Kada"));

        productList.add(new Home_recy_model(R.drawable.parinda, "Parinda"));

        productList.add(new Home_recy_model(R.drawable.necklace, "Necklace"));*/

       /* productList.add(new Home_recy_model(R.drawable.mangalsuthra, "Mangalsutra"));

        productList.add(new Home_recy_model(R.drawable.home_image6, "Necklace"));

        productList.add(new Home_recy_model(R.drawable.home_image7, "Pendants"));

        productList.add(new Home_recy_model(R.drawable.home_image8, "Phunchi"));
*/
       /* adapter = new RecyclerAdapter1(getActivity(), productList);
        recyclerView.setAdapter(adapter);*/



/*
bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.home){
            getActivity().finish();
            Intent i = new Intent(getActivity(), All_Sellers_Display_Activity.class);
            startActivity(i);
        }
        return true;
    }
});
*/
        BottomifyNavigationView bottomify = (BottomifyNavigationView)view.findViewById(R.id.bottomify_nav);
        bottomify.setOnNavigationItemChangedListener(new OnNavigationItemChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onNavigationItemChanged( BottomifyNavigationView.NavigationItem navigationItem) {
                if(navigationItem.getPosition()==0){

                        getActivity().finish();
                        Intent i = new Intent(getActivity(), All_Sellers_Display_Activity.class);
                        startActivity(i);
                }
                if(navigationItem.getPosition()==1){
                    getActivity().finish();
                    Intent i = new Intent(getActivity(), Seller_Dashboard_Activity.class);
                    startActivity(i);
                }
                if(navigationItem.getPosition()==2){
                    fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.mainframe, new Personal_info_fragment()).addToBackStack(null).commit();
                }

                if(navigationItem.getPosition()==3){
                    fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.mainframe, new Profile_fragment()).addToBackStack(null).commit();
                }
            }
        });






        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        Home_recy_model  home_recy_model =  productList.get(position);
                        String name= home_recy_model.getName();
                        Bundle bundle=new Bundle();
                        bundle.putString("Item_Clicked",name);
                        bundle.putString("CLASS","FRAGMENT2");
                        Toast.makeText(getContext(), name,Toast.LENGTH_SHORT).show();
                        toolbar = (Toolbar)getActivity().findViewById(R.id.tool_bar);
                        fragment_2=new products_display_fragment();
                        fragment_2.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframe,fragment_2).addToBackStack(null).commit();

                    }
                })
        );


        if (getActivity().getClass().getSimpleName().equalsIgnoreCase("MainActivity")) {
            toolbar = (getActivity()).findViewById(R.id.tool_bar);
            drawerLayout = (getActivity()).findViewById(R.id.drawer);
            toolbar.setOnMenuItemClickListener(this);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        drawerLayout.openDrawer(GravityCompat.START);
                    }
                }
            });

        }




        return  view;
    }





    public void loadRecycleData(){

       /*pref=getActivity().getSharedPreferences("USER_DETAILS",0);

        String output=pref.getString(ACCESS_TOKEN, null);
        Log.e("pref...........>>>>..","loadRecycleData................");
        Log.e("pref...........>>>>..",output);*/


       /* final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Loading....");
        progressDialog.show();*/
       String url="";
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public
            void onResponse(String response) {




                try {
                    progressBar.setVisibility(View.GONE);
                    JSONObject jsonObj = new JSONObject(response);
                    JSONObject jp=jsonObj.getJSONObject("products");

                    /* JSONArray jsPriceArray = jsonObj.getJSONArray("productPrice");*/

                    JSONArray ja_data = jp.getJSONArray("content");


                    int length = ja_data.length();
                    for(int i=0; i<length; i++) {
                        JSONObject jObj = ja_data.getJSONObject(i);


                            /*JSONObject priceObject = jsPriceArray.getJSONObject(i);

                            String price = String.valueOf(priceObject.getDouble("productFinalPrice"));*/
                        String sku = jObj.getString("sku");


                        String id = (jObj.getString("id"));
                        String name = (jObj.getString("name"));

                        JSONArray image_arr = jObj.getJSONArray ("links");
                        JSONObject img_jObj = image_arr.getJSONObject(1);
                        String imageurl=img_jObj.getString("href");



                        productList.add(new Home_recy_model(R.drawable.necklace, "Necklace"));

                    }
                    adapter = new RecyclerAdapter1(getActivity(), productList);
                    recyclerView.setAdapter(adapter);









                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        },new Response.ErrorListener() {
            @Override
            public
            void onErrorResponse(VolleyError error) {


                progressBar.setVisibility(View.GONE);

                NetworkResponse response = error.networkResponse;


                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 404:
                            BottomSheet.Builder builder = new BottomSheet.Builder(getContext());
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;
                        case 400:
                            BottomSheet.Builder builder1 = new BottomSheet.Builder(getContext());
                            builder1.setTitle("Sorry! No Products Available");
                            builder1.show();
                            break;
                        case 417:
                            BottomSheet.Builder builder2 = new BottomSheet.Builder(getContext());
                            builder2.setTitle("Sorry! No Products Available");
                            builder2.show();
                            break;
                        case 401:
                            Toast.makeText(getActivity().getApplicationContext(),"Session Expired!",Toast.LENGTH_SHORT).show();
                            session.logoutUser(getContext());

                    }
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    public  void getSlider( ){


        String url = ip+"gate/b2b/catalog/api/v1/slide/get/"+user_id;
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                swipeContainer.setRefreshing(false);
                try {
                   JSONObject jsonObject=response.getJSONObject("_links");
                    Object intervention = jsonObject.get("imageURl");
                    if(intervention instanceof  JSONArray) {

                        JSONArray jsonArray = (JSONArray) intervention;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            Viewpager_model1 viewpagerModel1 = new Viewpager_model1(jsonObject1.getString("href"));
                            slider_array.add(viewpagerModel1);
                        }
                    }


                    else {
                        JSONObject jsonObject1=(JSONObject)intervention;

                        Viewpager_model1 viewpagerModel1 = new Viewpager_model1(jsonObject1.getString("href"));
                        slider_array.add(viewpagerModel1);

                    }
                    viewPageAdapter1=new ViewpageAdapter1(getActivity(),slider_array);
                    viewPager.setAdapter(viewPageAdapter1);



                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                NetworkResponse response = volleyError.networkResponse;
                swipeContainer.setRefreshing(false);

                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 404:

                            break;
                        case 400:
                            BottomSheet.Builder builder1 = new BottomSheet.Builder(getContext());
                            builder1.setTitle("Sorry! No Products Available");
                            builder1.show();
                            break;
                        case 417:
                            BottomSheet.Builder builder2 = new BottomSheet.Builder(getContext());
                            builder2.setTitle("Sorry! No Products Available");
                            builder2.show();
                            break;
                        case 401:
                            Toast.makeText(getActivity().getApplicationContext(),"Session Expired!",Toast.LENGTH_SHORT).show();
                            session.logoutUser(getContext());

                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "bearer " + output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue.add(objectRequest);

    }



    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:

                     if(isVisible()) {
                         Fragment search = new GenericSearchFragment();
                         FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                         FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                         fragmentTransaction.replace(R.id.mainframe, search).addToBackStack(null).commit();
                     }

                return true;
        }

        return false;
    }


}

package com.example.compaq.b2b_application.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Fragments.products_display_fragment;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;

public
class Search_Activity extends AppCompatActivity {
    android.widget.ArrayAdapter<String> adapter;
    public  java.util.ArrayList<String> array=new java.util.ArrayList<String>();
   public ListView liv;
   public static SearchView searchView;
     public Context mContext;
    private FragmentTransaction fragmentTransaction;
    public static FragmentManager fragmentManager;
   public Toolbar toolbar;
    public SharedPreferences sharedPref;
    Fragment fragment_2;
    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_);

        liv=(android.widget.ListView)findViewById(R.id.list_view_container);
        /* array.addAll(java.util.Arrays.asList(getResources().getStringArray(R.array.array_country)));*/
        toolbar=(Toolbar)findViewById(R.id.search_toolbar);
        toolbar.setTitle("Search Your Item");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPref =getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        searchView=(SearchView)findViewById(R.id.search);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        final EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("search...","clickeed..");

                String items= searchEditText.getText().toString();

                Bundle bundle=new Bundle();
                bundle.putString("Item_Clicked",items);
                bundle.putString("CLASS","SEARCH");
                fragment_2=new products_display_fragment();
                fragment_2.setArguments(bundle);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                // ((LinearLayout)findViewById(R.id.frame2)).removeAllViews();
                fragmentTransaction.replace(R.id.frame2,fragment_2).addToBackStack(null).commit();


                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(adapter!=null){
                    adapter.getFilter().filter(newText);
                }
                else{
                    //prepareListData(newText);
                }
                return true;
            }
        });
        liv.setClickable(true);
        liv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               String items= adapter.getItem(position);

                Bundle bundle=new Bundle();
                bundle.putString("Item_Clicked",items);
                bundle.putString("CLASS","SEARCH");
                fragment_2=new products_display_fragment();
                fragment_2.setArguments(bundle);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
               /* ((LinearLayout)findViewById(R.id.frame2)).removeAllViews();*/

                fragmentTransaction.replace(R.id.frame2,fragment_2).addToBackStack(null).commit();



            }
        });

    }
    private void prepareListData(String text) {

        Log.d("Sidebar","Entered");


      String  URL_DATA = ip+"gate/b2b/catalog/api/v1/searching/suggestion/"+text;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public
            void onResponse(String response) {

                try {
                    JSONArray ja_data = new JSONArray(response);
                    int length = ja_data.length();
                    for ( int i = 0; i < length; i++) {
                        JSONObject jObj = ja_data.getJSONObject(i);

                        String sname = (jObj.getString("id"));
                        array.add(sname);

                    }

                    adapter=new android.widget.ArrayAdapter<String>(Search_Activity.this,android.R.layout.simple_list_item_1,array);
                    liv.setAdapter(adapter);


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

                String output=sharedPref.getString(ACCESS_TOKEN, null);

                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                params.put("Content-Type", "application/json");
                return params;
            }
        };;
        RequestQueue requestQueue = Volley.newRequestQueue(Search_Activity.this);
        requestQueue.add(stringRequest);
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

}



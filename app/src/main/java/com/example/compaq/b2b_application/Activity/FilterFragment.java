package com.example.compaq.b2b_application.Activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;

import com.android.volley.NetworkResponse;
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
import org.michaelbel.bottomsheet.BottomSheet;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip_cat;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.pref;

import com.example.compaq.b2b_application.R;


public class FilterFragment extends Fragment {
    public ExpandableListView expListView;
    com.example.compaq.b2b_application.Adapters.expand_listview2 listAdapter;
    public List<String> listDataHeader = new ArrayList<String>();
    HashMap<String, List<String>> listDataChild ;
    public SharedPreferences sharedPref;

    public static String result,item_clicks ;
    public ArrayList<String> children;
    public ArrayList<String> selection;
    public ArrayList<String> urls;
    private Button clear_all,apply;
    private Map<Object, Object> params;
    private LinkedHashMap<Object,Object>filterparams;
    private LinkedHashMap<Object,Object>sortparams;
    List<String>selected_list;
    Bundle bundle;
    JSONArray filter_jsonArray;
    private  static  final String base_url=ip+"gate/b2b/catalog/api/v1/product/all/category/Jewellery,";
    private static final String search_url=ip_cat + "/searching/facets";
    private  static  String final_url="" ,wholesaler_id="";
    String Classes=" ";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_filter, container, false);

        listDataChild = new HashMap<String, List<String>>();
        sortparams=new LinkedHashMap<>();
        filterparams=new LinkedHashMap<>();
        children = new ArrayList<String>();
        selection = new ArrayList<String>();
        urls = new ArrayList<String>();
        expListView = view.findViewById(R.id.expand);
        sharedPref = getContext().getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        bundle = this.getArguments();

        // result = bundle.getString("TeamName");
        item_clicks=bundle.getString("Item_Clicked");

        wholesaler_id=pref.getString("Wholeseller_id", null);

        sortparams= (LinkedHashMap<Object, Object>) bundle.getSerializable("SORT");
        Classes=bundle.getString("CLASS");

        if(Classes.equals("SEARCH") ||Classes.equals("WITH_SEARCH")){
            final_url= create_url(search_url);
        }
        else {
            final_url= create_url(base_url);
        }

        filter_jsonArray=new JSONArray();

        loadRecycleData();

        clear_all=(Button)view.findViewById(R.id.clear);
        apply=(Button)view.findViewById(R.id.applay);



        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                JSONObject jsonObjectfinal=new JSONObject();
                try {
                    if (filter_jsonArray.length()> 0){
                        jsonObjectfinal.put("filter", filter_jsonArray);
                        filterparams.put("filterReader",jsonObjectfinal.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Bundle bundle = new Bundle();
                bundle.putString("Item_Clicked",item_clicks);

                if(Classes.equals("SEARCH") ||Classes.equals("WITH_SEARCH")){
                    bundle.putString("CLASS","WITH_SEARCH");
                }
                else {
                    bundle.putString("CLASS","FILTER");
                }
                bundle.putSerializable("SORT",(Serializable) sortparams);
                bundle.putSerializable("FILTER_VALUE", (Serializable) filterparams);
                Log.d("FILTER_VALUE",filterparams.size()+"");



                Fragment fragment_2 = new products_display_fragment() ;
                fragment_2.setArguments(bundle);
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                fragmentManager.popBackStackImmediate(0,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction fTransaction1 =fragmentManager.beginTransaction();
                fTransaction1.replace(R.id.mainframe, fragment_2);
                fTransaction1.addToBackStack(null);
                fTransaction1.commit();

                // dismiss();


            }
        });
        clear_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //dismiss();

            }
        });








        listAdapter = new com.example.compaq.b2b_application.Adapters.expand_listview2(getActivity(), listDataHeader, listDataChild,selection);
        expListView.setAdapter(listAdapter);


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
                selected_list=new ArrayList<>();
                CheckedTextView checkbox = (CheckedTextView)v.findViewById(R.id.check_box_child);
                checkbox.toggle();
                String parent_view=parent.getAdapter().getItem(groupPosition).toString();
                if(checkbox.isChecked()) {
                    // add child category to parent's selection list

                    Log.d("parent..",parent.getAdapter().getItem(groupPosition).toString());
                    if(checkbox.getText().toString().equals("Yes")) {
                        create_Data(parent_view, "true");
                    }
                    else {
                        create_Data(parent_view, checkbox.getText().toString());
                    }


                }
                else {
                    // remove child category from parent's selection list

                    remove_data(parent_view,checkbox.getText().toString());
                }

                return true;
            }

        });

        return  view;
    }
    public void loadRecycleData(){

        String  URL_DATA=final_url;
        Log.d("URL....",URL_DATA);


        StringRequest stringRequest=new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public
            void onResponse(String response) {

                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jsonArray=jsonObj.optJSONArray("facets");
                    if(jsonArray!=null) {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String key = jsonObject.getString("facetKey");
                      /*if(key.equalsIgnoreCase("newLaunch"))
                      {
                          key="SORT";
                      }*/
                     /* if(key.equalsIgnoreCase("PRIORITY")){
                          continue;
                      }*/
                            listDataHeader.add(key);
                            JSONArray jsonArray1 = jsonObject.getJSONArray("values");
                            final List<String> submenu = new ArrayList<String>();
                            for (int j = 0; j < jsonArray1.length(); j++) {
                                if (jsonArray1.getString(j).equalsIgnoreCase("null") || jsonArray1.getString(j).equalsIgnoreCase("")) {
                                    continue;
                                }
                                if (jsonArray1.getString(j).equalsIgnoreCase("true")) {
                                    submenu.add("Yes");
                                    continue;
                                }
                                submenu.add(jsonArray1.getString(j));
                            }
                            listDataChild.put(listDataHeader.get(i), submenu);
                            expListView.setAdapter(listAdapter);

                        }
                    }
                    else {
                      JSONArray filterArray=jsonObj.getJSONArray("filters");
                        listDataHeader.add("category");
                        final List<String> submenu = new ArrayList<String>();
                        for (int i = 0; i < filterArray.length(); i++) {
                            JSONObject jsonObject = filterArray.getJSONObject(i);
                            String values = jsonObject.getString("filterValue");
                            submenu.add(values);
                         if(i+1==filterArray.length()){
                             listDataChild.put(listDataHeader.get(0), submenu);
                             expListView.setAdapter(listAdapter);
                         }
                        }
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
                            BottomSheet.Builder builder = new BottomSheet.Builder(getContext());
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            // dismiss();
                            break;
                        case 400:
                            BottomSheet.Builder builder1 = new BottomSheet.Builder(getContext());
                            builder1.setTitle("Sorry! No Products Available");
                            builder1.show();
                            // dismiss();
                            break;
                        case 417:
                            BottomSheet.Builder builder2 = new BottomSheet.Builder(getContext());
                            builder2.setTitle("Sorry! No Products Available");
                            builder2.show();
                            //dismiss();
                            break;


                    }
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() {

                String output=sharedPref.getString(ACCESS_TOKEN, null);
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

///////////////////////////////////////////////create data///////////////////////////////////////

    public void create_Data(String facetKeys, String values) {

        try {
            if (filter_jsonArray.length() > 0) {
                boolean b1 = false;
               /* for (Object key : listDataChild.keySet()) {
                    Object value = listDataChild.get(key);
                    Log.d("Key..",key.toString()+value.toString());


                }
            }*/
                for (int n = 0; n < filter_jsonArray.length(); n++) {
                    JSONObject object = filter_jsonArray.getJSONObject(n);
                    if (object.get("facetKey").equals(facetKeys)) {
                        b1=true;
                        JSONArray ja = object.getJSONArray("values");
                        ja.put(values);
                        Log.d("size111...",filter_jsonArray.length()+""+b1+"..."+ja.toString());
                        n=filter_jsonArray.length();
                    }
                    else if(n==filter_jsonArray.length()-1 &&b1==false) {
                        JSONObject jsonObject1=new JSONObject();
                        JSONArray jsonArrays1=new JSONArray();
                        jsonArrays1.put(values);
                        jsonObject1.put("facetKey", facetKeys);
                        jsonObject1.put("values", jsonArrays1);
                        Log.d("size222...",filter_jsonArray.length()+""+b1+"..."+jsonArrays1.toString());
                        filter_jsonArray.put(jsonObject1);
                        n=filter_jsonArray.length();
                    }
                }
                // do some stuff....
            }
            else {
                JSONObject jsonObject1=new JSONObject();
                JSONArray jsonArrays1=new JSONArray();
                jsonArrays1.put(values);
                jsonObject1.put("facetKey", facetKeys);
                jsonObject1.put("values", jsonArrays1);
                filter_jsonArray.put(jsonObject1);
                Log.d("size333...",filter_jsonArray.length()+""+"..."+jsonArrays1.toString());
            }




        }  catch(Exception e){
            e.printStackTrace();


        }


    }
//////////////////////////////////////remove from filter//////////////////////////////////////

    public void  remove_data(String facetKeys,Object values){
        try {
            for (int n = 0; n < filter_jsonArray.length(); n++) {
                JSONObject jsobject = filter_jsonArray.getJSONObject(n);
                if (jsobject.get("facetKey").equals(facetKeys)) {

                    JSONArray ja = jsobject.getJSONArray("values");
                    if(ja.length()>1 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                        for(int j=0;j<ja.length();j++) {
                            if(ja.get(j).equals(values)){
                                ja.remove(j);
                            }
                        }
                    }
                    else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            filter_jsonArray.remove(n);
                            if(filter_jsonArray.length()==0){
                                params.remove("filterReader");
                                filterparams.remove("filterReader");
                            }

                        }
                    }
                    n = filter_jsonArray.length();
                }
            }
        }catch (Exception e1){
            e1.printStackTrace();
        }
    }


    /////////////////////////////create url////////////////////////////////////////
    public String create_url(String val) {
        params = new LinkedHashMap<>();
        String create_urls="";

        try {
            if (bundle.getString("CLASS") != null) {
                if(Classes.equals("SEARCH") ||Classes.equals("WITH_SEARCH")) {
                    params.put("queryText", item_clicks);
                    if(sortparams!=null){
                        params.putAll(sortparams);
                    }
                }

                else if(Classes.equals("FILTER")&&sortparams!=null) {
                    val=val+item_clicks;
                    params.putAll(sortparams);

                }
                if (filterparams.isEmpty()==false) {
                    params.putAll(filterparams);
                }



                params.put("wholesaler",wholesaler_id );
                params.put("productType", "REGULAR");
                params.put("page", 0);
                params.put("pagesize", 20);
                create_urls = addQueryStringToUrlString(val, params);
                Log.d("params", params.toString());
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d("res.", create_urls);
        return create_urls;
    }

    String addQueryStringToUrlString(String url, final Map<Object, Object> parameters) throws UnsupportedEncodingException {
        if (parameters == null) {
            return url;
        }

        for (Map.Entry<Object, Object> parameter : parameters.entrySet()) {

            final String encodedKey = URLEncoder.encode(parameter.getKey().toString(), "UTF-8");
            final String encodedValue = URLEncoder.encode(parameter.getValue().toString(), "UTF-8");

            if (!url.contains("?")) {
                url += "?" + encodedKey + "=" + encodedValue;
            } else {
                url += "&" + encodedKey + "=" + encodedValue;
            }
        }

        return url;
    }
}

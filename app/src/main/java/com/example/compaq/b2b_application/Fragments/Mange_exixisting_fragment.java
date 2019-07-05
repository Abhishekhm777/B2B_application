package com.example.compaq.b2b_application.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.Manage_Adapter;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.Helper_classess.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.compaq.b2b_application.Fragments.products_display_fragment.URL_DATA;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip_cat;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class Mange_exixisting_fragment extends Fragment {
    private ExpandableListView expListView;
    private final List<String> listDataHeader = new ArrayList<>();
    private final  HashMap<String, List<String>> listDataChild = new HashMap<>();
    private com.example.compaq.b2b_application.Adapters.ExpandableListAdapter listAdapter;
    public Manage_Adapter manage_adapter;
    private int lastExpandedPosition = -1;
    private SharedPreferences sharedPref;
    private  Bundle bundle;
    private  SessionManagement session;
    private String SUB_URL = "";
    private String sname = "";
    private View view;
    private int position = 0;
    private  String output;
    private String  wholseller_id;
    private  Mange_card_fragment mange_card_fragment;
    private  SearchView searchView;
    private  ProgressBar progressBar;
    private FragmentActivity activity;
    public Mange_exixisting_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(view==null) {
            view = inflater.inflate(R.layout.fragment_mange_exixisting_fragment, container, false);

            expListView =  view.findViewById(R.id.navList);
            listAdapter = new com.example.compaq.b2b_application.Adapters.ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

            sharedPref = getActivity().getSharedPreferences("USER_DETAILS", 0);

            session = new SessionManagement(getActivity().getApplicationContext());
            output = sharedPref.getString(ACCESS_TOKEN, null);

            activity = this.getActivity();

            mange_card_fragment = new Mange_card_fragment();
            progressBar=view.findViewById(R.id.progress);
            progressBar.setVisibility(View.VISIBLE);

            wholseller_id = sharedPref.getString("userid", null);
            searchView =  getActivity().findViewById(R.id.sellers_search);
            searchView.setVisibility(View.VISIBLE);
            searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {


                    return false;
                }
            });

            searchView.setSubmitButtonEnabled(true);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    //Do something here

                    bundle = new Bundle();

                    bundle.putString("Searched", query);

                    mange_card_fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, mange_card_fragment).addToBackStack(null).commit();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });


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

                    if (listDataChild.get(listDataHeader.get(groupPosition)).size() == 0) {

                       /* Toast.makeText(getActivity(),
                                item_clicked + "," + listDataHeader.get(groupPosition) + " Expanded",
                                Toast.LENGTH_SHORT).show();*/
                        bundle = new Bundle();
                        bundle.putString("clicked", listDataHeader.get(groupPosition));
                        mange_card_fragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, mange_card_fragment).addToBackStack(null).commit();

                    }
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

                    expListView.collapseGroup(groupPosition);


                    String mi;
                    String prnt ;

                    if (childPosition == 0) {

                        mi = listDataHeader.get((groupPosition));
                         /*if(mi.equalsIgnoreCase("More")){
                             mi=listDataChild.get(
                                     listDataHeader.get(groupPosition)).get(
                                     childPosition);
                         }*/


                    } else {

                        mi = listDataHeader.get((groupPosition));
                        prnt = listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition);
                        mi += "," + prnt;


                    }

                    if (childPosition == 0) {
                       /* bundle = new Bundle();

                        bundle.putString("Item_Clicked", mi);
                        productsdisplayfragment = new products_display_fragment();
                        productsdisplayfragment.setArguments(bundle);
                      getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, productsdisplayfragment).addToBackStack(null).commit();*/

                        bundle = new Bundle();

                        bundle.putString("clicked", mi);
                        mange_card_fragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, mange_card_fragment).addToBackStack(null).commit();

                        return false;
                    } else {
                        bundle = new Bundle();
                        bundle.putString("Item_Clicked", mi);
                        bundle.putString("All", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));

                        loadCatalogue(mi, bundle,listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));
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
            });
            prepareListData();
        }
        return view;
    }
    public void loadCatalogue(final  String name,final Bundle bundle,final  String nnn) {
        final String url="https://server.mrkzevar.com/gate/b2b/catalog/api/v1/category/byFirstLevelCategory/b2b/Jewellery,"+name+"?wholesaler="+wholseller_id;
        String ur = url.replaceAll("\\s", "");
        Log.e("DDDVFDVDVD",ur);

        StringRequest stringRequest=new StringRequest(Request.Method.GET, ur, new Response.Listener<String>() {
            @Override
            public
            void onResponse(String response) {
                progressBar.setVisibility(View.GONE);

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if(jsonArray.length()==0){
                     Bundle   bundle = new Bundle();
                        bundle.putString("clicked", name);
                        mange_card_fragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, mange_card_fragment).addToBackStack(null).commit();
                    }

                    else {
                        Manage_exixsting_frag2 manage_exixsting_frag2 = new Manage_exixsting_frag2();
                        manage_exixsting_frag2.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, manage_exixsting_frag2).addToBackStack(null).commit();
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
                            BottomSheet.Builder builder = new BottomSheet.Builder(activity);
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;
                        case 400:
                            BottomSheet.Builder builder1 = new BottomSheet.Builder(activity);
                            builder1.setTitle("Sorry! No Products Available");
                            builder1.show();
                            break;
                        case 417:
                            BottomSheet.Builder builder2 = new BottomSheet.Builder(activity);
                            builder2.setTitle("Sorry! No Products Available");
                            builder2.show();
                            break;
                        case 401:
                            Toast.makeText(getActivity(),"Session Expired!",Toast.LENGTH_SHORT).show();
                            session.logoutUser(activity);

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
        RequestQueue requestQueue= Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }





    private RequestQueue requestQueue;
    private void prepareListData () {

        if(requestQueue==null)
        {
            requestQueue = Volley.newRequestQueue(activity);

        }

        URL_DATA=ip_cat+"/category/byFirstLevelCategory/b2b/Jewellery?wholesaler="+wholseller_id;
        Log.d("URL_DATA",URL_DATA);

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
                        sname = (jObj.getString("name"));
                        /*.replaceAll("\\s", ""));*/
                        listDataHeader.add(sname);
                        position = i;

                        SUB_URL =   ip_cat+"/category/byFirstLevelCategory/b2b/"+parent+","+sname+"?wholesaler="+wholseller_id;
                        SUB_URL  = SUB_URL.replaceAll("\\s", "");

                        Log.e("UUURRRLLL",SUB_URL);

                        prepareSubListData(i, sname);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Sidebar11", "EXCEPETION");
                }

             

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;

                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 404:
                            BottomSheet.Builder builder = new BottomSheet.Builder(activity);
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;

                        case 401:
                            Toast.makeText(activity.getApplicationContext(),"Session Expired!",Toast.LENGTH_SHORT).show();
                            /* new Reffressh_token().getToken(MainActivity.this);*/
                            session.logoutUser(activity);
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


    private void prepareSubListData ( final int i, final String sname1){
        progressBar.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, SUB_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONArray ja_data = new JSONArray(response);



                        final List<String> submenu = new ArrayList<>();

                        for (int j = 0; j < ja_data.length(); j++) {
                            if(j==0){
                                submenu.add("All  "+sname1);
                            }

                            JSONObject jObj = ja_data.getJSONObject(j);

                            String subnames = jObj.getString("name");

                            submenu.add(subnames);
                        }
                        listDataChild.put(listDataHeader.get(i), submenu);



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

                Map<String, String> params = new HashMap<>();
                params.put("Authorization","bearer "+output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };




        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        searchView.setVisibility(View.VISIBLE);
    }
}

package com.example.compaq.b2b_application.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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


import static com.example.compaq.b2b_application.Fragments.products_display_fragment.item_clicked;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip_cat;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class Manage_exixsting_frag2 extends Fragment {

    ExpandableListView expListView;
    Bundle bundle;
    public SharedPreferences sharedPref;
    public   SharedPreferences.Editor myEditor;
    public List<String> listDataHeader = new ArrayList<String>();
    HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
    com.example.compaq.b2b_application.Adapters.ExpandableListAdapter listAdapter;
    public String output,wholseller_id;
    public int position = 0;
    public String SUB_URL = "";
    public String sname = "";
    SessionManagement session;
    products_display_fragment productsdisplayfragment;
    public Bundle bundle2;
    private View view;
    public String original,urldata;
    int item=0;
    String all;

    public Manage_exixsting_frag2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            if(view==null) {
                view = inflater.inflate(R.layout.fragment_manage_exixsting_frag2, container, false);


                sharedPref = getContext().getSharedPreferences("USER_DETAILS", 0);
                myEditor = sharedPref.edit();
                output = sharedPref.getString(ACCESS_TOKEN, null);

                wholseller_id = sharedPref.getString("userid", null);
                expListView = (ExpandableListView) view.findViewById(R.id.childrens);

                session = new SessionManagement(getActivity());


                prepareListData();

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


                        if (groupPosition == listDataHeader.size() - 1) {
                            bundle = new Bundle();

                            bundle.putString("Searched", listDataHeader.get(groupPosition));
                            Mange_card_fragment mange_card_fragment = new Mange_card_fragment();
                            mange_card_fragment.setArguments(bundle);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, mange_card_fragment).addToBackStack(null).commit();
                            return;
                        }

                        if (listDataChild.get(listDataHeader.get(groupPosition)).size() == 0) {

                       /* Toast.makeText(getActivity(),
                                item_clicked + "," + listDataHeader.get(groupPosition) + " Expanded",
                                Toast.LENGTH_SHORT).show();*/


                            bundle = new Bundle();

                            bundle.putString("Searched", listDataHeader.get(groupPosition));
                            Mange_card_fragment mange_card_fragment = new Mange_card_fragment();
                            mange_card_fragment.setArguments(bundle);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, mange_card_fragment).addToBackStack(null).commit();

                            return;
                        }




              /*  if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;*/
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
                        String prnt = "";

                        if (childPosition == 0) {
/*
                        mi = listDataHeader.get((groupPosition));
                         if(mi.equalsIgnoreCase("More")){
                             mi=listDataChild.get(
                                     listDataHeader.get(groupPosition)).get(
                                     childPosition);
                         }*/


                            bundle2 = new Bundle();

                            bundle2.putString("Item_Clicked", item_clicked + "," + listDataHeader.get(groupPosition));
                            bundle2.putString("CLASS","FRAGMENT2");
                            productsdisplayfragment = new products_display_fragment();

                            productsdisplayfragment.setArguments(bundle2);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, productsdisplayfragment).addToBackStack(null).commit();


                        } else {

                       /* mi = listDataHeader.get((groupPosition));
                        prnt = listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition);
                        mi += "," + prnt;
                        Log.e("AFTER CONCATING", mi);*/


                            bundle2 = new Bundle();
                            bundle2.putString("CLASS","FRAGMENT2");
                            bundle2.putString("Item_Clicked", item_clicked + "," + listDataHeader.get(groupPosition) + "," + listDataChild.get(
                                    listDataHeader.get(groupPosition)).get(
                                    childPosition));
                            productsdisplayfragment = new products_display_fragment();
                            productsdisplayfragment.setArguments(bundle2);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, productsdisplayfragment).addToBackStack(null).commit();

                        }

                        return false;
                    }


                });


            }
        return  view;
    }
    private RequestQueue requestQueue;
    private void prepareListData () {
        bundle=this.getArguments();
        item_clicked=bundle.getString("Item_Clicked");
        all=bundle.getString("All");
        original=item_clicked;

        if(requestQueue==null)
        {
            requestQueue = Volley.newRequestQueue(getActivity());

        }

        urldata=ip_cat+"/category/byFirstLevelCategory/b2b/Jewellery,"+item_clicked+"?wholesaler="+wholseller_id;
        urldata=urldata.replaceAll("\\s", "");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urldata, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray ja_data = new JSONArray(response);
                    int length = ja_data.length();

                    for (int i = 0; i < length; i++) {

                        ++item;

                        JSONObject jObj = ja_data.getJSONObject(i);

                        JSONArray parent_array=jObj.getJSONArray("parent");
                        String parent=parent_array.getString(0);


                        sname = (jObj.getString("name").replaceAll("\\s", ""));
                        listDataHeader.add(sname);

                        position = i;

                        SUB_URL =   ip_cat+"/category/byFirstLevelCategory/b2b/"+parent+","+sname+"?wholesaler="+wholseller_id;
                        SUB_URL=SUB_URL.replaceAll("\\s", "");
                        prepareSubListData(i, sname);

                        if(item==length){

                            final List<String> submenu = new ArrayList<String>();
                            listDataHeader.add("All "+all);
                            listDataChild.put(listDataHeader.get(item), submenu);


                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;

                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 404:
                            BottomSheet.Builder builder = new BottomSheet.Builder(getActivity());
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;

                        case 401:

                            Toast.makeText(getActivity(),"Session Expired!",Toast.LENGTH_SHORT).show();
                            /* new Reffressh_token().getToken(MainActivity.this);*/
                            session.logoutUser(getActivity());

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

        requestQueue.add(stringRequest);
    }


    private void prepareSubListData ( final int i, final String sname1){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, SUB_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONArray ja_data = new JSONArray(response);
                    /*if (ja_data.length() == 0) {
                        final List<String> submenu = new ArrayList<String>();
                        submenu.add("All  "+sname1);

                        listDataChild.put(listDataHeader.get(i), submenu);



                    } else {*/
                    final List<String> submenu = new ArrayList<String>();

                    for (int j = 0; j < ja_data.length(); j++) {
                        submenu.add("All  " + sname1);
                        JSONObject jObj = ja_data.getJSONObject(j);

                        String subnames = jObj.getString("name");

                        submenu.add(subnames);
                    }
                    listDataChild.put(listDataHeader.get(i), submenu);


                    listAdapter = new com.example.compaq.b2b_application.Adapters.ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
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

                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


}

package com.example.compaq.b2b_application.Fragments;


import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.Manage_Adapter;
import com.example.compaq.b2b_application.Adapters.Manage_category_Adapter;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.Helper_classess.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.example.compaq.b2b_application.Activity.MainActivity.ip_cat;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class Manage_category_frag1 extends Fragment {
    ExpandableListView expListView;
    public ArrayList<String> listDataHeader = new ArrayList<String>();
    HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
    HashMap<String, String> list_id = new HashMap<String, String>();
    Manage_category_Adapter listAdapter;
    public Manage_Adapter manage_adapter;
    private int lastExpandedPosition = -1;
    public SharedPreferences sharedPref;
    private Bundle bundle;
    SessionManagement session;
    public String SUB_URL = "";
    public String sname = "";
    private View view;
    public int position = 0;
    private String output,urldata;
    String wholseller_id;
    private Mange_card_fragment mange_card_fragment;
    ProgressBar progressBar;
    Button button;
    private FragmentTransaction fragmentTransaction;
    public FragmentManager fragmentManager;
    private Button path;

    TextClicked mCallback;
    Dialog dialog;
    TextView ok,cancel,main_text;

    public Manage_category_frag1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_manage_category_frag1, container, false);

            expListView = (ExpandableListView) view.findViewById(R.id.navList);
            listAdapter = new Manage_category_Adapter(getActivity(), listDataHeader, listDataChild, list_id, getFragmentManager(), view);
            path = (Button) view.findViewById(R.id.path);
            dialog = new Dialog(getContext());
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.alert_popup);
            ok=(TextView)dialog.findViewById(R.id.ok);
            cancel=(TextView)dialog.findViewById(R.id.cancel);
            main_text=(TextView)dialog.findViewById(R.id.popup_textview);
            path.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   main_text.setText("Do you want to Add new Category to Jewellery ?");
                   dialog.show();
                   ok.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                            dialog.dismiss();
                           Bundle bundle=new Bundle();
                           bundle.putString("pro_id","Jewellery");

                           fragmentManager = getActivity().getSupportFragmentManager();
                           fragmentTransaction = fragmentManager.beginTransaction();
                           Add_new_Category top_categories=new Add_new_Category();
                           top_categories.setArguments(bundle);
                           fragmentTransaction.replace(R.id.manage, top_categories).addToBackStack(null).commit();

                       }
                   });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
            });


            sharedPref = getActivity().getSharedPreferences("USER_DETAILS", 0);

            session = new SessionManagement(getActivity().getApplicationContext());
            output = sharedPref.getString(ACCESS_TOKEN, null);
            button = (Button) view.findViewById(R.id.top);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    Top_categories top_categories = new Top_categories();
                    top_categories.setArguments(bundle);
                    fragmentTransaction.replace(R.id.manage, top_categories).addToBackStack(null).commit();

                }
            });

            mange_card_fragment = new Mange_card_fragment();
            progressBar = (ProgressBar) view.findViewById(R.id.progress);
            progressBar.setVisibility(View.VISIBLE);

            wholseller_id = sharedPref.getString("userid", null);
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


                        return;
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

                    Log.e("DNDJFNDDF","CLICKED");
                    String mi;
                    String prnt = "";



                        mi = listDataHeader.get((groupPosition));
                        prnt = listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition);
                        mi += "," + prnt;



                        bundle = new Bundle();
                        bundle.putString("Item_Clicked", mi);
                        loadCatalogue(mi, bundle,listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));

                    return false;


                }
            });
            prepareListData();
        }
        return view;
    }

    public interface TextClicked {
        public void sendText(ArrayList list);
    }

    public void loadCatalogue(final String name, final Bundle bundle, final String nnn) {


        final String ty = ip_cat + "/category/byFirstLevelCategory/b2b/Jewellery,"+name+"?wholesaler="+wholseller_id;
        String url=ty.replaceAll("\\s", "");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray jsonArray = new JSONArray(response);


                    if (jsonArray.length() == 0) {

                        Snackbar.make(getView(),nnn +"  has no subcategory",Snackbar.LENGTH_LONG).show();
                    } else {
                        Manage_category_subfragment manage_category_subfragment = new Manage_category_subfragment();
                        manage_category_subfragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.manage, manage_category_subfragment).addToBackStack(null).commit();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                NetworkResponse response = error.networkResponse;


                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 404:
                            BottomSheet.Builder builder = new BottomSheet.Builder(getActivity());
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;
                        case 400:
                            BottomSheet.Builder builder1 = new BottomSheet.Builder(getActivity());
                            builder1.setTitle("Sorry! No Products Available");
                            builder1.show();
                            break;
                        case 417:
                            BottomSheet.Builder builder2 = new BottomSheet.Builder(getActivity());
                            builder2.setTitle("Sorry! No Products Available");
                            builder2.show();
                            break;
                        case 401:
                            Toast.makeText(getActivity(), "Session Expired!", Toast.LENGTH_SHORT).show();
                            session.logoutUser(getActivity());

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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    private RequestQueue requestQueue;

    private void prepareListData() {

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity());

        }

        urldata = ip_cat + "/category/byFirstLevelCategory/b2b/Jewellery?wholesaler="+wholseller_id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urldata, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray ja_data = new JSONArray(response);
                    int length = ja_data.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject jObj = ja_data.getJSONObject(i);

                        JSONArray parent_array = jObj.getJSONArray("parent");
                        String parent = parent_array.getString(0);


                        sname = (jObj.getString("name"));
                        /*.replaceAll("\\s", ""));*/
                        listDataHeader.add(sname);

                        list_id.put(sname, jObj.getString("id"));


                        position = i;

                        SUB_URL = ip_cat + "/category/byFirstLevelCategory/b2b/"+parent+","+sname+"?wholesaler="+wholseller_id;
                        SUB_URL=SUB_URL.replaceAll("\\s", "");
                        String su=URLEncoder.encode(SUB_URL,"UTF-8");
                        Log.e("GTGT",su);
                        prepareSubListData(i, sname);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 404:
                            BottomSheet.Builder builder = new BottomSheet.Builder(getActivity());
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;

                        case 401:

                            Toast.makeText(getActivity().getApplicationContext(), "Session Expired!", Toast.LENGTH_SHORT).show();
                            /* new Reffressh_token().getToken(MainActivity.this);*/
                            session.logoutUser(getActivity());

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

        requestQueue.add(stringRequest);
    }

RequestQueue requestQueue1;
    private void prepareSubListData(final int i, final String sname1) {
        if (requestQueue1 == null) {
            requestQueue1 = Volley.newRequestQueue(getActivity());

        }
        progressBar.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, SUB_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONArray ja_data = new JSONArray(response);


                    final List<String> submenu = new ArrayList<String>();

                    for (int j = 0; j < ja_data.length(); j++) {

                        JSONObject jObj = ja_data.getJSONObject(j);

                        String subnames = jObj.getString("name");
                        list_id.put(subnames, jObj.getString("id"));
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
        }) {
            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "bearer " + output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };


        requestQueue1.add(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    public  void refreshMethod(){
        listDataChild.clear();
        listDataHeader.clear();

        listAdapter.notifyDataSetChanged();
        prepareListData();
    }
}

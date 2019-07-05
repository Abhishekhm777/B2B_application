package com.example.compaq.b2b_application.Fragments;


import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.Add_category_Adapter;
import com.example.compaq.b2b_application.Adapters.Manage_Adapter;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.Helper_classess.SessionManagement;

import org.json.JSONArray;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Fragments.products_display_fragment.URL_DATA;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip_cat;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class Add_new_Category extends Fragment {
    ExpandableListView expListView;
    public ArrayList<String> listDataHeader = new ArrayList<String>();
    HashMap<String, ArrayList<String>> listDataChild = new HashMap<String, ArrayList<String>>();
    HashMap<String, String> values = new HashMap<String, String>();
    private ArrayList<String> filters=new ArrayList<>();
    private ArrayList<String> varients=new ArrayList<>();
    Add_category_Adapter listAdapter;
    public Manage_Adapter manage_adapter;
    private int lastExpandedPosition = -1;
    public SharedPreferences sharedPref;
    private  Bundle bundle;
    SessionManagement session;
    public String SUB_URL = "";
    public String sname = "";
    private View view;
    public int position = 0;
    String output;
    String    wholseller_id;
    private  Mange_card_fragment mange_card_fragment;
    ProgressBar progressBar;
    Button sub_buton,add_specification;
    private FragmentTransaction fragmentTransaction;
    public FragmentManager fragmentManager;
    private TextView path;
    private EditText name;

    public Add_new_Category() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmen
        if(view==null) {
            view= inflater.inflate(R.layout.fragment_add_new__category, container, false);

            expListView = (ExpandableListView) view.findViewById(R.id.expandable_list);

            listAdapter = new Add_category_Adapter(getActivity(), listDataHeader, listDataChild,values,filters,varients);
            path=(TextView)view.findViewById(R.id.cat_path);

            sharedPref = getActivity().getSharedPreferences("USER_DETAILS", 0);

            session = new SessionManagement(getActivity().getApplicationContext());
            output = sharedPref.getString(ACCESS_TOKEN, null);
            name=(EditText)view.findViewById(R.id.name);
            add_specification=(Button)view.findViewById(R.id.add_specification);
            sub_buton=(Button)view.findViewById(R.id.submit_button);
            sub_buton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
            if(!name.getText().toString().equalsIgnoreCase("")) {
                listAdapter.add_Category(name.getText().toString(), path.getText().toString(), view);
            }else {
                Snackbar.make(view, "Please provide Category name", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

            }
                }
            });

            /*progressBar=(ProgressBar)view.findViewById(R.id.progress);
            progressBar.setVisibility(View.VISIBLE);*/

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
                    setListViewHeight(parent, groupPosition);

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

                    expListView.collapseGroup(groupPosition);


                    String mi;
                    String prnt = "";

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


                        return false;
                    } else {


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

            add_specification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listDataHeader.add("");
                    final ArrayList<String> submenu = new ArrayList<>();
                    submenu.add("");
                    listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), submenu);
                    listAdapter.notifyDataSetChanged();
                }
            });
        }
        bundle=this.getArguments();
        try {
            String id = bundle.getString("pro_id");
            if (id.equalsIgnoreCase("Jewellery")) {
                main();
            } else {
                specification(id);
            }
        }
        catch (Exception e){

        }
        return view;
    }

    private RequestQueue requestQueue;

    private void specification(String id) {

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        }
        URL_DATA = ip_cat + "/category/" + id + "?wholesaler="+ wholseller_id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                         JSONObject jsonObject=new JSONObject(response);
                         JSONArray jsonArray=jsonObject.getJSONArray("categoriesPath");
                         JSONArray fil_array=jsonObject.getJSONArray("filterAttributes");
                         for(int k=0;k<fil_array.length();k++){
                             filters.add(fil_array.getString(k));
                         }
                    JSONArray varient=jsonObject.getJSONArray("variantFields");
                    for(int k=0;k<varient.length();k++){
                        varients.add(varient.getString(k));
                    }
                         path.setText(jsonArray.getString(0));
                         JSONArray jsonArray1=jsonObject.getJSONArray("specificationHeading");
                         for(int i=0;i<jsonArray1.length();i++){
                             JSONObject jsonObject1=jsonArray1.getJSONObject(i);
                             listDataHeader.add(jsonObject1.getString("headingName"));
                             JSONArray jsonArray2=jsonObject1.getJSONArray("keyes");
                             ArrayList<String> submenu=new ArrayList<>();
                             for(int j=0;j<jsonArray2.length();j++){
                                 JSONObject jsonObject2=jsonArray2.getJSONObject(j);
                                 submenu.add(jsonObject2.getString("key"));
                                 JSONArray value_arr=jsonObject2.getJSONArray("value");

                                 values.put(jsonObject2.getString("key"),value_arr.getString(0));
                             }
                             listDataChild.put(listDataHeader.get(i),submenu);
                         }


                         expListView.setAdapter(listAdapter);
                    listAdapter.registerDataSetObserver(new DataSetObserver() {
                        @Override
                        public void onChanged() {
                            super.onChanged();
                            setPrevious(expListView, listDataHeader.size());
                        }
                    });

                    setPrevious(expListView, 0);

                } catch (Exception e) {
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


    private void main( ) {

        path.setText("Jewellery");
        listDataHeader.add("Metal Details");
        ArrayList<String> submenu=new ArrayList<>();
        submenu.add("Metal");
        submenu.add("Purity (Kt)");
        submenu.add("Net Weight (gms)");
        submenu.add("Color");
        submenu.add("Gross Weight (gms)");
        values.put("Metal","Gold");
        values.put("Purity (Kt)","22");
        listDataChild.put("Metal Details",submenu);

        listDataHeader.add("Product Details");
        ArrayList<String> submenu1=new ArrayList<>();
        submenu1.add("Brand");
        submenu1.add("Size");
        submenu1.add("Length");
        submenu1.add("Available Weights (gms)");
        listDataChild.put("Product Details",submenu1);

        listDataHeader.add("Tag Names");
        ArrayList<String> submenu2=new ArrayList<>();
        submenu2.add("Tags");
        listDataChild.put("Tag Names",submenu2);

        listDataHeader.add("Certifications");
        ArrayList<String> submenu3=new ArrayList<>();
        submenu3.add("Certifications");
        listDataChild.put("Certifications",submenu3);
        expListView.setAdapter(listAdapter);
        listAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                setPrevious(expListView, listDataHeader.size());
            }
        });

    }
    private void setListViewHeight(ExpandableListView listView, int group) {
        BaseExpandableListAdapter listAdapter = (BaseExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);


            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
                //Add Divider Height
                totalHeight += listView.getDividerHeight() * (listAdapter.getChildrenCount(i) - 1);
            }
        }
        //Add Divider Height
        totalHeight += listView.getDividerHeight() * (listAdapter.getGroupCount() - 1);

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    private void setPrevious(ExpandableListView listView, int group) {
        BaseExpandableListAdapter listAdapter = (BaseExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                /* || ((!listView.isGroupExpanded(i)) && (i == group))*/) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
                //Add Divider Height
                totalHeight += listView.getDividerHeight() * (listAdapter.getChildrenCount(i) - 1);
            }
        }
        //Add Divider Height
        totalHeight += listView.getDividerHeight() * (listAdapter.getGroupCount() - 1);

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


}

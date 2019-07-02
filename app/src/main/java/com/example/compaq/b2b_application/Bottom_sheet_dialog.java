package com.example.compaq.b2b_application;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.Recycler_Adapter2;
import com.example.compaq.b2b_application.Fragments.Company_info_fragment;
import com.example.compaq.b2b_application.Fragments.Fragment_2;
import com.example.compaq.b2b_application.Fragments.Oder_History;
import com.example.compaq.b2b_application.Fragments.Personal_info_fragment;
import com.example.compaq.b2b_application.Fragments.Whish_list_fragment;
import com.example.compaq.b2b_application.Model.Recy_model2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.compaq.b2b_application.Fragments.Fragment_2.item_clicked;
import static com.example.compaq.b2b_application.MainActivity.ip;
import static com.example.compaq.b2b_application.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.SessionManagement.PREF_NAME;
import static com.example.compaq.b2b_application.SessionManagement.pref;

@SuppressLint("ValidFragment")
public class Bottom_sheet_dialog extends BottomSheetDialogFragment {
    public ExpandableListView expListView;
    com.example.compaq.b2b_application.Adapters.expand_listview2 listAdapter;
    public List<String> listDataHeader = new ArrayList<String>();
    HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
    public SharedPreferences sharedPref;
    public BottomSheetListner mlistner;
    public String result;
    public ArrayList<String> children;
    public ArrayList<String> selection;
    public ArrayList<String> urls;
    private Button clear_all,apply;

    public Bottom_sheet_dialog(BottomSheetListner mlistner) {
       this.mlistner=mlistner;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);

        children = new ArrayList<String>();
        selection = new ArrayList<String>();
        urls = new ArrayList<String>();
        expListView = view.findViewById(R.id.expand);
        sharedPref = getContext().getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Bundle bundle = this.getArguments();
        result = bundle.getString("TeamName");
        loadRecycleData();

        clear_all=(Button)view.findViewById(R.id.clear);
        apply=(Button)view.findViewById(R.id.applay);



        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s="";
                s="&filterReader=%7B%22filter%22:%5B";
                for(int i=0;i<urls.size();i++){

                s+=urls.get(i);
                }


                mlistner.onButtonClicked( s);
                dismiss();

            }
        });
        clear_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlistner.onButtonClicked("clear");
                dismiss();
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

                CheckedTextView checkbox = (CheckedTextView)v.findViewById(R.id.check_box_child);
                checkbox.toggle();
                if(checkbox.isChecked()) {
                    // add child category to parent's selection list
                   String fltr="filterReader=%7B%22filter%22:%5B";

                             String url="%7B%22facetKey%22:%22"+listDataHeader.get(groupPosition)+"%22,%22values%22:%5B%22"+checkbox.getText().toString()+"%22%5D%7D%5D%7D";

                             urls.add(url);
                    selection.add(checkbox.getText().toString());

                }
                else {
                    // remove child category from parent's selection list
                    selection.remove(checkbox.getText().toString());
                }
                String mi = listDataChild.get(
                        listDataHeader.get(groupPosition)).get(
                        childPosition);


                return true;
            }

        });
return view;
    }

    public interface BottomSheetListner{
        void  onButtonClicked(String text);
    }

  /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mlistner=(BottomSheetListner)context;
        }
       catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"Must impliment BottomSheetListner");

       }
    }*/

    public void loadRecycleData(){


      String  URL_DATA=ip+"gate/b2b/catalog/api/v1/product/all/category/Jewellery,"+result+"?wholesaler="+pref.getString("Wholeseller_id", null)+"&productType=REGULAR";


        StringRequest stringRequest=new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public
            void onResponse(String response) {

                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jsonArray=jsonObj.getJSONArray("facets");

                  for(int i=0;i<jsonArray.length();i++){
                      JSONObject jsonObject=jsonArray.getJSONObject(i);

                      String key=jsonObject.getString("facetKey");
                      if(key.equalsIgnoreCase("NEWLAUNCH"))
                      {
                          key="SORT";
                      }
                      if(key.equalsIgnoreCase("PRIORITY")){
                          continue;
                      }
                        listDataHeader.add(key);
                      JSONArray jsonArray1=jsonObject.getJSONArray("values");
                      final List<String> submenu = new ArrayList<String>();
                      for(int j=0;j<jsonArray1.length();j++){
                          if(jsonArray1.getString(j).equalsIgnoreCase("null")||jsonArray1.getString(j).equalsIgnoreCase("")){
                              continue;
                          }
                          if(jsonArray1.getString(j).equalsIgnoreCase("true")){
                              submenu.add("New Launch");
                              continue;
                          }
                          submenu.add(jsonArray1.getString(j));
                      }
                      listDataChild.put(listDataHeader.get(i), submenu);
                      expListView.setAdapter(listAdapter);
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
                            dismiss();
                            break;
                        case 400:
                            BottomSheet.Builder builder1 = new BottomSheet.Builder(getContext());
                            builder1.setTitle("Sorry! No Products Available");
                            builder1.show();
                            dismiss();
                            break;
                        case 417:
                            BottomSheet.Builder builder2 = new BottomSheet.Builder(getContext());
                            builder2.setTitle("Sorry! No Products Available");
                            builder2.show();
                            dismiss();
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

}
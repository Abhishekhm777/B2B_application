package com.example.compaq.b2b_application.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Adapters.UpdateFragment_expandablelistview;
import com.example.compaq.b2b_application.Adapters.Update_product_recy_Adaptetr;
import com.example.compaq.b2b_application.Model.Update_product_model;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.Activity.Update_product_Activity;
import com.example.compaq.b2b_application.Activity.Varient_Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip_cat;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class Update_product_fragment extends Fragment {
    private ExpandableListView expandableListView;
    private List<String> listDataHeader = new ArrayList<String>();
    HashMap<String, ArrayList<String>> listDataChild = new HashMap<String, ArrayList<String>>();
    HashMap<String, ArrayList<String>> value_map = new HashMap<String, ArrayList<String>>();
    private  Bundle bundle;
    private  TextView name,sku,manufa_name,manu_fact_mob,no_of_days;
    private String output,wholseller_id;
    private SharedPreferences sharedPref;
    private ImageView imageView;
    private String imageUrl="https://server.mrkzevar.com/gate/b2b/catalog/api/v1/assets/image/";
    private  UpdateFragment_expandablelistview updateFragment_expandablelistview;
    private  RecyclerView recyclerView;
    private ArrayList<Update_product_model> productlist;
    private Update_product_recy_Adaptetr update_product_recy_adaptetr;
    private Button update,varient;
    private String pro_id,user_id;
    private ViewGroup rootView;

    public Update_product_fragment() {
        // Required empty public constructor
    }
View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_update_product_fragment, container, false);
        name=(TextView)view.findViewById(R.id.name);
        sku=(TextView)view.findViewById(R.id.sku);
        manu_fact_mob=(TextView)view.findViewById(R.id.manuf_mob);
        manufa_name=(TextView)view.findViewById(R.id.manufact_name);
        no_of_days=(TextView)view.findViewById(R.id.delevery_days);

        imageView=(ImageView) view.findViewById(R.id.image1);
        update=(Button)view.findViewById(R.id.update);
        varient=(Button)view.findViewById(R.id.varient);

        rootView = (ViewGroup) view.findViewById(R.id.stone_layout);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("pro_id",pro_id);
                Intent intent=new Intent(getActivity(), Update_product_Activity.class).putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });
        varient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("pro_id",pro_id);
                Intent intent=new Intent(getActivity(), Varient_Activity.class).putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });

        sharedPref=getActivity().getSharedPreferences("USER_DETAILS",0);
        output=sharedPref.getString(ACCESS_TOKEN, null);
        user_id = sharedPref.getString("userid", "");

        recyclerView = (RecyclerView) view.findViewById(R.id.image_recycler);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        expandableListView = (ExpandableListView) view.findViewById(R.id.expandable_list);
        expandableListView.setVisibility(View.GONE);
        expandableListView.setDivider(null);
        expandableListView.setChildDivider(null);
        bundle=this.getArguments();
         pro_id=bundle.getString("image_id");
      /*  loadRecycleData(pro_id);*/
        updateFragment_expandablelistview = new UpdateFragment_expandablelistview(getActivity(), listDataHeader, listDataChild,value_map);

        return view;
    }

    //////////////////////////Add field/////////////////////////////////////////////
    public void onAddField() {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View rowView = inflater.inflate(R.layout.stone_deatails, null);

        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams.setMargins(25, 15, 25, 15);
        rowView.setLayoutParams(buttonLayoutParams);
        rootView.addView(rowView, rootView.getChildCount());


        Button button = (Button) rowView.findViewById(R.id.remove);
       button.setVisibility(View.GONE);

    }
    /////////////Delete////////////////////////////////
    public void onDelete(View view) {
        rootView.removeView(view);
    }

///////////////////////////////////////////////////////////////////////





    //////////////////////////////////////////////main data////////////////////////////////////
   /* public void loadRecycleData(final  String id){

        String url=ip_cat+"/product/"+id+"?wholesaler="+user_id;

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public
            void onResponse(String response) {

                try {

                    JSONObject jsonObj = new JSONObject(response);
                        JSONObject jp=jsonObj.getJSONObject("resourceSupport");

                    name.setText(jp.getString("name"));
                    sku.setText(jp.getString("sku"));
                    manu_fact_mob.setText(jp.getString("manufactureMobile"));
                    manufa_name.setText(jp.getString("manufactureName"));
                    no_of_days.setText(jp.getString("requiredDayesToDeliver"));

                    JSONArray image_arr=jp.getJSONArray("imageGridFsID");

                    Glide.with(getActivity()).load(imageUrl+image_arr.get(0)).into(imageView);
                    productlist=new ArrayList<>();
                    for(int k=0;k<image_arr.length();k++){
                        productlist.add(new Update_product_model(image_arr.getString(k),id));
                    }
                    update_product_recy_adaptetr = new Update_product_recy_Adaptetr(getActivity().getApplicationContext(), productlist);
                    recyclerView.setAdapter(update_product_recy_adaptetr);
                    try{
                        JSONArray jsonArray = jp.getJSONArray("specification");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (jsonObject.getString("heading").equalsIgnoreCase("Stone Details")) {
                                onAddField();

                                View v = rootView.getChildAt( rootView.getChildCount()-1);
                                EditText heading = v.findViewById(R.id.heading);
                                EditText colour = v.findViewById(R.id.colour);
                                EditText shape = v.findViewById(R.id.shape);
                                EditText no_stone = v.findViewById(R.id.no_stone);
                                EditText clarity = v.findViewById(R.id.clarity);
                                EditText weight = v.findViewById(R.id.weight);
                                EditText type = v.findViewById(R.id.typpe);

                                JSONArray jsonArray1 = jsonObject.getJSONArray("attributes");

                                for (int j = 0; j < jsonArray1.length(); j++) {
                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                    String k=jsonObject1.getString("key");
                                    JSONArray jsonArray2 = jsonObject1.getJSONArray("values");
                                    if(k.equalsIgnoreCase("Stone Name")){
                                        heading.setText(jsonArray2.get(0).toString());
                                    }
                                    if(k.equalsIgnoreCase("color")){
                                        colour.setText(jsonArray2.get(0).toString());
                                    }
                                    if(k.equalsIgnoreCase("Shape")){
                                        shape.setText(jsonArray2.get(0).toString());
                                    }
                                    if(k.equalsIgnoreCase("Number of Stones")){
                                        no_stone.setText(jsonArray2.get(0).toString());
                                    }
                                    if(k.equalsIgnoreCase("Clarity")){
                                        clarity.setText(jsonArray2.get(0).toString());
                                    }
                                    if(k.equalsIgnoreCase("Weight")){
                                        weight.setText(jsonArray2.get(0).toString());
                                    }
                                    if(k.equalsIgnoreCase("Setting Type")){
                                        type.setText(jsonArray2.get(0).toString());
                                    }
                                }

                                continue;
                            }

                            if(jsonObject.getString("heading")!=null) {
                                expandableListView.setVisibility(View.VISIBLE);

                                listDataHeader.add(jsonObject.getString("heading"));
                            }
                            JSONArray jsonArray1 = jsonObject.getJSONArray("attributes");
                            final ArrayList<String> submenu = new ArrayList<>();
                            final ArrayList<String> value = new ArrayList<>();
                            for (int j = 0; j < jsonArray1.length(); j++) {
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                  if(jsonObject1.getString("key")!="") {
                                      submenu.add(jsonObject1.getString("key"));
                                  }
                                JSONArray jsonArray2 = jsonObject1.getJSONArray("values");

                                     value.add(jsonArray2.getString(0));

                            }
                            listDataChild.put(listDataHeader.get(i), submenu);
                            value_map.put(listDataHeader.get(i), value);
                        }
                        updateFragment_expandablelistview.registerDataSetObserver(new DataSetObserver() {
                            @Override
                            public void onChanged() {
                                super.onChanged();
                                setPrevious(expandableListView, listDataHeader.size());
                            }
                        });
                    }catch (Exception e){

                    }

                    expandableListView.setAdapter(updateFragment_expandablelistview);
                    setPrevious(expandableListView, 0);
                    expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                        @Override
                        public boolean onGroupClick(ExpandableListView parent, View v,
                                                    int groupPosition, long id) {

                                setListViewHeight(parent, groupPosition);

                            return false;
                        }
                    });

                    expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                            return false;
                        }
                    });



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (IndexOutOfBoundsException e){
                    updateFragment_expandablelistview.notifyDataSetChanged();
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
                            break;
                        case 400:
                            BottomSheet.Builder builder1 = new BottomSheet.Builder(getContext());
                            builder1.setTitle("Sorry! No Products Available");
                            builder1.show();
                            break;
                        case 417:

                            Snackbar.make(getView(), "Sorry! No Products Available", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                           *//* BottomSheet.Builder builder2 = new BottomSheet.Builder(getContext());
                            builder2.setTitle("Sorry! No Products Available");
                            builder2.show();*//*
                            break;


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
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
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
                *//* || ((!listView.isGroupExpanded(i)) && (i == group))*//*) {
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
*/
}

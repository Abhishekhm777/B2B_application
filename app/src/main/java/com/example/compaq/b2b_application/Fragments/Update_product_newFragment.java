package com.example.compaq.b2b_application.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Activity.Update_product_Activity;
import com.example.compaq.b2b_application.Activity.Varient_Activity;
import com.example.compaq.b2b_application.Adapters.RecyclerAdapter3;
import com.example.compaq.b2b_application.Adapters.Update_product_recy_Adaptetr;
import com.example.compaq.b2b_application.Adapters.Varients_Adapter;
import com.example.compaq.b2b_application.Helper_classess.SessionManagement;
import com.example.compaq.b2b_application.Model.Inner_Recy_model;
import com.example.compaq.b2b_application.Model.Recycler_model3;
import com.example.compaq.b2b_application.Model.Update_product_model;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip_cat;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class Update_product_newFragment extends Fragment implements Update_product_recy_Adaptetr.IProcessFilter {
private View view;
private SharedPreferences sharedPref;

private String userid,output,pro_id;
private ArrayList<Update_product_model> image_ids;
private ArrayList<String> varents_list;
private Update_product_recy_Adaptetr update_product_recy_adaptetr;
private Varients_Adapter varients_adapter;
private Bundle bundle;
private SessionManagement session;
private Activity activity;
private Recycler_model3 main2_listner;
private ArrayList<Recycler_model3> detProductlist;
private ArrayList<Inner_Recy_model> details_list;
private ArrayList<String> duplicate;
private Inner_Recy_model inner_recy_listner;
private  RecyclerAdapter3 main2_recycler_adapter;
private String sku,name,manufacture_name,manufacture_mob,days;
@BindView(R.id.image_recycler)RecyclerView imageRecycler;
@BindView(R.id.detail_recycler)RecyclerView detail_recycler;
@BindView(R.id.update_btn) Button update_btn;
@BindView(R.id.addVarient_btn) Button add_varient_btn;



    public Update_product_newFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_update_product_new, container, false);
        ButterKnife.bind(this,view);
        activity=getActivity();



        sharedPref =getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        userid = sharedPref.getString("userid", "");
        output=sharedPref.getString(ACCESS_TOKEN, null);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1, GridLayoutManager.HORIZONTAL, false);
        imageRecycler.setLayoutManager(mGridLayoutManager);
        imageRecycler.setItemAnimator(new DefaultItemAnimator());

        detail_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        detail_recycler.setHasFixedSize(true);


        bundle=this.getArguments();
        pro_id=bundle.getString("image_id");

        detProductlist=new ArrayList<>();



        loadProductsData(pro_id,this);
/*
        update_product_recy_adaptetr = new Update_product_recy_Adaptetr(getActivity().getApplicationContext(), image_ids,this);
*/

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("pro_id",pro_id);
                Intent intent=new Intent(getActivity(), Update_product_Activity.class).putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });

        add_varient_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("pro_id",pro_id);
                Intent intent=new Intent(getActivity(), Varient_Activity.class).putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });
        return  view;
    }

    // TODO: Rename method, update argument and hook method into UI event


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
///////////////////Load images int recycler////////////////////////
    public void loadProductsData(final String id,final Update_product_newFragment update_product_newFragment){

        detail_recycler.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));

        detail_recycler.setHasFixedSize(true);
        duplicate=new ArrayList<>();
        image_ids=new ArrayList<>();
        String url=ip_cat+"/product/"+id+"?wholesaler="+userid;
        Log.e("URL",url);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public
            void onResponse(String response) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONObject jp=jsonObj.getJSONObject("resourceSupport");
                    String id=jp.getString("id");
                    String custum_of=jp.getString("customOf");
                    if(id.equalsIgnoreCase(custum_of)){
                        add_varient_btn.setEnabled(true);
                        add_varient_btn.setBackgroundColor(getResources().getColor(R.color.Blue_colour));
                    }
                    sku=jp.getString("sku");
                    name=jp.getString("name");
                    manufacture_name=jp.getString("manufactureName");
                    manufacture_mob=jp.getString("manufactureMobile");
                    days=jp.getString("requiredDayesToDeliver");
                    JSONArray image_arr=jp.getJSONArray("imageGridFsID");
                    JSONArray links_array=jp.getJSONArray("links");
                   /* for(int k=0;k<image_arr.length();k++){
                        image_ids.add((image_arr.getString(k)));
                    }*/
                  /* image_ids.add(image_arr.getString(0),id);*/
                    duplicate.add((image_arr.getString(0)));
                   image_ids.add(new Update_product_model(image_arr.getString(0),id));
                    update_product_recy_adaptetr = new Update_product_recy_Adaptetr(getActivity().getApplicationContext(), image_ids,update_product_newFragment);

                   /* update_product_recy_adaptetr = new Update_product_recy_Adaptetr(getActivity().getApplicationContext(), image_ids);
                    imageRecycler.setAdapter(update_product_recy_adaptetr);*/
                    for(int i=0;i<links_array.length();i++){
                        JSONObject jsonObject=links_array.getJSONObject(i);
                       if(jsonObject.getString("rel").equalsIgnoreCase("customOF")){
                           String custom_url=jsonObject.getString("href");
                           fetchVarients(custom_url);
                       }
                    }
                    try{
                        JSONArray jsonArray = jp.getJSONArray("specification");

                        main2_listner=new Recycler_model3("Primary Details");
                        detProductlist.add(main2_listner);
                        details_list=new ArrayList<>();
                        inner_recy_listner = new Inner_Recy_model("Product Name", name);
                        details_list.add(inner_recy_listner);
                        inner_recy_listner = new Inner_Recy_model("SKU", sku);
                        details_list.add(inner_recy_listner);
                        inner_recy_listner = new Inner_Recy_model("Manufacturer Name", manufacture_name);
                        details_list.add(inner_recy_listner);
                        inner_recy_listner = new Inner_Recy_model("Manufacturer Phone", manufacture_mob);
                        details_list.add(inner_recy_listner);
                        inner_recy_listner = new Inner_Recy_model("Req. Days to Deliver", days);
                        details_list.add(inner_recy_listner);
                        main2_listner.setArrayList(details_list);
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String heading=(jsonObject.getString("heading"));
                            main2_listner=new Recycler_model3(heading);
                            detProductlist.add(main2_listner);
                            JSONArray attribute = jsonObject.getJSONArray ("attributes");
                            details_list=new ArrayList<>();
                            String key,values;
                            for(int j=0;j<attribute.length();j++) {
                                try {
                                    JSONObject attributeobjects = attribute.getJSONObject(j);
                                    key = (attributeobjects.getString("key"));
                                    JSONArray attribute_values = attributeobjects.getJSONArray("values");
                                    values = attribute_values.getString(0);
                                    if (values.equalsIgnoreCase("")) {
                                        continue;
                                    }
                                    inner_recy_listner = new Inner_Recy_model(key, values);
                                }
                                catch (Exception e){
                                    continue;
                                }
                               /* if (heading.equalsIgnoreCase("PRODUCT DETAILS") && j == 0) {
                                    inner_recy_listner = new Inner_Recy_model("Product Name", name);
                                    details_list.add(inner_recy_listner);
                                    inner_recy_listner = new Inner_Recy_model("SKU", sku);
                                    details_list.add(inner_recy_listner);
                                }*/
                                inner_recy_listner=new Inner_Recy_model(key,values);
                                details_list.add(inner_recy_listner);
                            }
                            main2_listner.setArrayList(details_list);
                        }
                        Log.d("URL %!!!... , . ,. , . ","Banthuuuuu");
                        main2_recycler_adapter = new RecyclerAdapter3(activity, detProductlist,1);
                        detail_recycler .setAdapter(main2_recycler_adapter);
                        detail_recycler.setNestedScrollingEnabled(false);
                    }catch (Exception e){
                       e.printStackTrace();
                       Log.e("Exception in","detailssss");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
        RequestQueue requestQueue= Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    ////////////////get Varients images///////////////////////

    public void fetchVarients(String url){

        StringRequest stringRequest=new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                           for(int i=0;i<jsonArray.length();i++){
                             JSONObject jsonObject=jsonArray.getJSONObject(i);
                             JSONArray jsonArray1=jsonObject.getJSONArray("imageGridFsID");
                              String id=jsonObject.getString("id");

                                   /*  image_ids.add(jsonArray1.getString(0));*/
                               if(duplicate.contains(jsonArray1.getString(0))){
                                   continue;
                               }
                               else {
                                   image_ids.add(new Update_product_model(jsonArray1.getString(0),id));
                               }


                           }
/*
                       update_product_recy_adaptetr = new Update_product_recy_Adaptetr(getActivity().getApplicationContext(), image_ids,this);
*/


                    imageRecycler.setAdapter(update_product_recy_adaptetr);
                  /*  varients_adapter=new Varients_Adapter(getActivity().getApplicationContext(),varents_list);
                    varient_recycler.setAdapter(varients_adapter);*/

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },
    new Response.ErrorListener() {
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
                        case 401:
                            session.logoutUser(getActivity());
                           /* BottomSheet.Builder builder2 = new BottomSheet.Builder(getContext());
                            builder2.setTitle("Sorry! No Products Available");
                            builder2.show();*/
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
        RequestQueue requestQueue= Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }



    @Override
    public void onProcessFilter(String id) {
         Log.e("Result",id);
         image_ids.clear();
         update_product_recy_adaptetr.notifyDataSetChanged();
     details_list.clear();
     detProductlist.clear();
     main2_recycler_adapter.notifyDataSetChanged();

     loadProductsData(id, this);

    }
}

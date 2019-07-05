package com.example.compaq.b2b_application.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.Adress_adapter;
import com.example.compaq.b2b_application.Model.Address_model;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAddress_fragment extends Fragment {

    Toolbar toolbar;
    public  RecyclerView recyclerView;
    public ArrayList<Address_model> addressArray;
    public String type,line,city,state,country,zipcode,output,id;
    public Adress_adapter adapter;
    private SharedPreferences sharedPref;
    private FragmentTransaction fragmentTransaction;
    public FragmentManager fragmentManager;
    public  Button emptybtn,addnew,selsct;
    ImageView empty_imageview;
TextView text1,text2,title;
    public MyAddress_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_my_address_fragment, container, false);



        toolbar=(Toolbar)view.findViewById(R.id.address_toolbar);

        title=(TextView)view.findViewById(R.id.zoom);

             addnew=(Button)view.findViewById(R.id.add_new) ;
             addnew.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     fragmentManager = getActivity().getSupportFragmentManager();
                     fragmentTransaction = fragmentManager.beginTransaction();
                     fragmentTransaction.replace(R.id.frame_layout, new AddNewAddess()).addToBackStack(null);

                     fragmentTransaction.commit();
                 }
             });
       
        emptybtn=(Button)view.findViewById(R.id.add_button) ;
        emptybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, new AddNewAddess()).addToBackStack(null);

                fragmentTransaction.commit();
            }
        });

        text1=(TextView)view.findViewById(R.id.text1);
        text2=(TextView)view.findViewById(R.id.text2);

        empty_imageview=(ImageView)view.findViewById(R.id.empty_adress) ;




        recyclerView=(RecyclerView)view.findViewById(R.id.address_recycler);
        addressArray=new ArrayList<>();

        sharedPref =getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        output=sharedPref.getString(ACCESS_TOKEN, null);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setHasFixedSize(true);


        userInformation();
        return view;
    }
    public void userInformation ( ){

        String url=ip1+"/b2b/api/v1/user/info";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);

                    String name=jObj.getString("firstName");
                    String lastname=jObj.getString("lastName");
                    String mob=jObj.getString("mobileNumber");


                    JSONArray jsonArray=jObj.getJSONArray("address");
                    if(jsonArray.length()==0){
                        recyclerView.setVisibility(View.GONE);
                        addnew.setVisibility(View.GONE);
                        /*selsct.setVisibility(View.GONE);*/


                        emptybtn.setVisibility(View.VISIBLE);
                        empty_imageview.setImageResource(R.drawable.address_add);
                        empty_imageview.setVisibility(View.VISIBLE);
                        text1.setVisibility(View.VISIBLE);
                        text2.setVisibility(View.VISIBLE);
                        return;
                    }
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        type=jsonObject.getString("addressType");
                        line=jsonObject.getString("line");
                        city=jsonObject.getString("city");
                        country=jsonObject.getString("country");
                        zipcode=jsonObject.getString("zipcode");
                        state=jsonObject.getString("state");
                        id=jsonObject.getString("id");

                        addressArray.add(new Address_model(name,lastname,type,mob,line,city,state,country,zipcode,id));
                    }




                    adapter=new Adress_adapter(getContext(),addressArray);

                    adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                        @Override
                        public void onItemRangeRemoved(int positionStart, int itemCount) {
                            super.onItemRangeRemoved(positionStart, itemCount);

                            if(adapter.getItemCount()==0){
                                recyclerView.setVisibility(View.GONE);
                                addnew.setVisibility(View.GONE);
                              /*  selsct.setVisibility(View.GONE);*/


                                emptybtn.setVisibility(View.VISIBLE);
                                empty_imageview.setImageResource(R.drawable.address_add);
                                empty_imageview.setVisibility(View.VISIBLE);
                                text1.setVisibility(View.VISIBLE);
                                text2.setVisibility(View.VISIBLE);


                            }


                        }

                        @Override
                        public void onChanged() {
                            super.onChanged();

                        }


                        void checkEmpty() {



                            empty_imageview.setImageResource(R.drawable.emptycart);
                            empty_imageview.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                        }

                    });
                    recyclerView.setAdapter(adapter);

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




        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    @Override
    public void onResume() {
        super.onResume();

        title=(TextView)getActivity().findViewById(R.id.zoom);
        title.setText("MY ADDRESS");

    }

    @Override
    public void onPause() {
        super.onPause();


    }

    public void youveGotMail( ) {
       /*addressArray.clear();
       userInformation();*/

    }

}

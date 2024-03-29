package com.example.compaq.b2b_application.Fragments;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.compaq.b2b_application.Adapters.Offline_order_Adapter;
import com.example.compaq.b2b_application.Helper_classess.Back_alert_class;
import com.example.compaq.b2b_application.Model.Offline_order_model;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Offline_fragment1 extends Fragment {
private  View view;
 private  SearchView searchView;
 private ArrayList<Offline_order_model> productlist;
 private Offline_order_Adapter offlineOrderAdapter;
 private  Toolbar toolbar;
 private FragmentTransaction fragmentTransaction;
 public FragmentManager fragmentManager;
 @BindView(R.id.offline_recycler) RecyclerView recyclerView;
 @BindView(R.id.offline_toolbar)Toolbar thisToolbar;
 @BindView(R.id.add_button) ImageView add_button;
 @BindView(R.id.place_button) Button place_button;
 public Context context;
 private  Bundle bundle;

 private  TextView total;


    public Offline_fragment1() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setView(inflater.inflate(R.layout.fragment_offline_fragment1, container, false));
        ButterKnife.bind(this, getView());
        searchView=getActivity().findViewById(R.id.custom_search);
        toolbar=getActivity().findViewById(R.id.offline_tool);
        total= getView().findViewById(R.id.total);

        context=getActivity().getApplicationContext();

        thisToolbar.setNavigationIcon(R.drawable.back_btn);
        thisToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Back_alert_class back_alert_class=new Back_alert_class(getContext());
                back_alert_class.showAlert();
                back_alert_class.getYes().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                          back_alert_class.getMyDialogue().dismiss();
                          getActivity().finish();
                    }
                });

            }
        });

          add_button.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {

                  Offline_order_search_fragment offline_order_search_fragment = new Offline_order_search_fragment();
                  getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.offline_frame, offline_order_search_fragment, "customize_search").addToBackStack(null).commit();
              }
          });

          place_button.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {

                  if(productlist.size()>0) {
                      Bundle bundle = new Bundle();
                      bundle.putParcelableArrayList("arraylist", productlist);
                      Offline_order_customerdetail offline_order_customerdetail = new Offline_order_customerdetail();
                      offline_order_customerdetail.setArguments(bundle);
                      fragmentManager = getActivity().getSupportFragmentManager();
                      fragmentTransaction = fragmentManager.beginTransaction();
                      fragmentTransaction.replace(R.id.offline_frame, offline_order_customerdetail, "offline_order_customerdetail").addToBackStack(null).commit();
                  }
                  else {
                      Snackbar.make(getView(), "Please add some items ", Snackbar.LENGTH_SHORT)
                              .setAction("Action", null).show();
                  }


              }
          });

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setHasFixedSize(true);
        bundle=this.getArguments();
        productlist= bundle.getParcelableArrayList("arraylist");
        offlineOrderAdapter = new Offline_order_Adapter(getContext(), productlist, getView());
        recyclerView.setAdapter(offlineOrderAdapter);
        return getView();
    }

    @Override
    public void onResume() {
        super.onResume();
       toolbar.setVisibility(View.GONE);

       /* searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("THINDDD","DBBUIDVDV");
                Offline_order_search_fragment offline_order_search_fragment = new Offline_order_search_fragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.offline_frame, offline_order_search_fragment, "customize_search").addToBackStack(null).commit();
            }
        });*/

    }
    @Override
    public void onPause() {
        super.onPause();
    }
    public void updateText(HashMap<String,String> text){
       Log.e("yaaaaaaaaaaa",  text.get("name"));

        productlist.add(new Offline_order_model(text.get("name"), text.get("url"),text.get("sku"),text.get("gwt"),text.get("size"),text.get("purity") ,"1",text.get("pro_id"),"0"));
        offlineOrderAdapter.notifyDataSetChanged();
    }
    @Nullable
    @Override
    public View getView() {
        return view;
    }
    public void setView(View view) {
        this.view = view;
    }
}

package com.example.compaq.b2b_application.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.compaq.b2b_application.Adapters.Offline_order_Adapter;
import com.example.compaq.b2b_application.Adapters.Order_to_bprocessed_Adapter;
import com.example.compaq.b2b_application.Model.Offline_order_model;
import com.example.compaq.b2b_application.Model.Seller_order_history;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;

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
 @BindView(R.id.offline_recycler) RecyclerView recyclerView;
    public Offline_fragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_offline_fragment1, container, false);
        ButterKnife.bind(this,view);
        searchView=getActivity().findViewById(R.id.custom_search);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setHasFixedSize(true);
        productlist=new ArrayList<>();
        productlist.add(new Offline_order_model("","","","","","","" ));
        productlist.add(new Offline_order_model("","","","","","","" ));

        offlineOrderAdapter = new Offline_order_Adapter(getContext(), productlist);

        recyclerView.setAdapter(offlineOrderAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
      searchView.setVisibility(View.GONE);
       /* searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("THINDDD","DBBUIDVDV");
                Offline_order_search_fragment offline_order_search_fragment = new Offline_order_search_fragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.offline_frame, offline_order_search_fragment, "customize_search").addToBackStack(null).commit();
            }
        });*/
    }
}

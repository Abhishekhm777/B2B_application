package com.example.compaq.b2b_application.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.compaq.b2b_application.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Offline_fragment1 extends Fragment {
private  View view;
 private  SearchView searchView;
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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        searchView.setVisibility(View.GONE);
    }
}

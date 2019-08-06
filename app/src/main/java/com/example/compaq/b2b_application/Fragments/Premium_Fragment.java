package com.example.compaq.b2b_application.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.compaq.b2b_application.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Premium_Fragment extends Fragment {


    public Premium_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_premium_, container, false);
    }

}

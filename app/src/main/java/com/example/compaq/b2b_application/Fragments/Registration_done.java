package com.example.compaq.b2b_application.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.compaq.b2b_application.Activity.LoginActivity;
import com.example.compaq.b2b_application.R;


public class Registration_done extends Fragment {
    ImageView imageView;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_registration_done, container, false);
        imageView=(ImageView)view.findViewById(R.id.done_img);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


}

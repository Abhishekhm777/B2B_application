package com.example.compaq.b2b_application.Fragments;


import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.example.compaq.b2b_application.Activity.Add_new_product_activity;
import com.example.compaq.b2b_application.Activity.Customize_Order;
import com.example.compaq.b2b_application.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Custom_order_frag2 extends Fragment {

    private RadioButton byName,byCategory;
    private Button add_new;
    public Custom_order_frag2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_custom_order_frag2, container, false);

        byName=view.findViewById(R.id.byname);
        byCategory=view.findViewById(R.id.byCategory);
        add_new=view.findViewById(R.id.add_new_product_btn);
        byName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    byCategory.setChecked(false);
                }
            }
        });
        byCategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    byName.setChecked(false);

                }
            }
        });

        add_new.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

              /*  Intent intent=new Intent(getActivity(), Add_new_product_activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());*/
                ActivityCompat.startActivityForResult(getActivity(), new Intent(getActivity(), Add_new_product_activity.class), 0, null);
            }
        });

          return  view;
    }

}

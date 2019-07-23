package com.example.compaq.b2b_application.Helper_classess;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.compaq.b2b_application.Activity.Customize_Order;
import com.example.compaq.b2b_application.Activity.LoginActivity;
import com.example.compaq.b2b_application.Activity.Offline_order;
import com.example.compaq.b2b_application.Activity.Splash_Activity;
import com.example.compaq.b2b_application.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Order_Placed_Splashfragment extends Fragment {


    public Order_Placed_Splashfragment() {
        // Required empty public constructor
    }
    private  View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_order__placed__splashfragment, container, false);

        Thread splash=new Thread()
        {
            public void run()
            {
                try{
                    sleep(700);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    Intent login = new Intent(getActivity(), Offline_order.class);
                    getActivity().startActivity(login);
                    getActivity().finish();
                }
            }
        };
        splash.start();
    return  view;
    }

}

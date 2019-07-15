package com.example.compaq.b2b_application.Fragments;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.example.compaq.b2b_application.Activity.Customize_Order;
import com.example.compaq.b2b_application.Activity.LoginActivity;
import com.example.compaq.b2b_application.Activity.Splash_Activity;
import com.example.compaq.b2b_application.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.os.SystemClock.sleep;
import static com.example.compaq.b2b_application.Activity.Customize_Order.pager;

/**
 * A simple {@link Fragment} subclass.
 */
public class Custom_order_placed_frag extends Fragment {

@BindView(R.id.finish_button)Button button;
@BindView(R.id.place_text) TextView textView;

    public Custom_order_placed_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_custom_order_placed_frag, container, false);
        ButterKnife.bind(this,view);

         button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
               /*  getActivity().finish();*/

                 Intent login = new Intent(getActivity(), Customize_Order.class);
                 getActivity().startActivity(login);
                 getActivity().finish();
             }
         });


        textView.startAnimation(shakeError());

/*
        Handler uiHandler = new Handler();
        uiHandler.post(new Runnable()
        {
            @Override
            public void run()
            {

                try{
                    sleep(2500);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    pager.setCurrentItem(3);
                }
            }
        });*/

        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(400);
        shake.setInterpolator(new CycleInterpolator(8));
        return shake;
    }
}

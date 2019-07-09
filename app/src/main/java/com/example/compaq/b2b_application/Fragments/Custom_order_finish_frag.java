package com.example.compaq.b2b_application.Fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.compaq.b2b_application.R;

import static android.os.SystemClock.sleep;
import static com.example.compaq.b2b_application.Activity.Customize_Order.pager;

/**
 * A simple {@link Fragment} subclass.
 */
public class Custom_order_finish_frag extends Fragment {

   View view;
    public Custom_order_finish_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_custom_order_finish_frag, container, false);


        Handler uiHandler = new Handler();
        uiHandler.post(new Runnable()
        {
            @Override
            public void run()
            {

                try{
                    sleep(1000);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    pager.setCurrentItem(4);
                }
            }
        });
        return  view;
    }

}

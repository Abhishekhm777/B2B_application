package com.example.compaq.b2b_application.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.compaq.b2b_application.Activity.Add_new_product_activity;
import com.example.compaq.b2b_application.Activity.Customize_Order;
import com.example.compaq.b2b_application.R;

import java.util.Timer;

import static android.os.SystemClock.sleep;
import static com.example.compaq.b2b_application.Activity.Customize_Order.pager;

/**
 * A simple {@link Fragment} subclass.
 */
public class Custom_order_finish_frag extends Fragment {

   View view;
   private Button button;
    public Custom_order_finish_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_custom_order_finish_frag, container, false);
        button=view.findViewById(R.id.finish_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent login = new Intent(getActivity(), Customize_Order.class);
                getActivity().startActivity(login);
                getActivity().finish();
            }
        });

        return  view;
    }


    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            Log.e("RERERERER","GFBDJKVDFVD");


          /*  try{
                sleep(1500);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally {
                pager.setCurrentItem(4);
            }*/
        }
    }

}

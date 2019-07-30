package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.compaq.b2b_application.Fragments.Custom_order_finish_frag;
import com.example.compaq.b2b_application.Fragments.Custom_order_frag2;
import com.example.compaq.b2b_application.Fragments.Custom_order_placed_frag;
import com.example.compaq.b2b_application.Fragments.Customize_order_frag1;
import com.example.compaq.b2b_application.Fragments.Custom_order_frag3;


public class Stepper_Adapter extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs;

    public Stepper_Adapter(Context myContext, FragmentManager fm, int totalTabs) {
        super(fm);
        this.myContext = myContext;
        this.totalTabs = totalTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Customize_order_frag1 frag1 = new Customize_order_frag1();
                return frag1;
            case 1:
                Custom_order_frag2 frag2 = new Custom_order_frag2();
                return frag2;

            case 2:
                Fragment frag3 = new Custom_order_frag3();
                return frag3;

            case 3:
                Custom_order_finish_frag frag4 = new Custom_order_finish_frag();
                return frag4;

            case 4:
                Custom_order_placed_frag wholesellerregister_fragment3 = new Custom_order_placed_frag();
                return wholesellerregister_fragment3;


            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}

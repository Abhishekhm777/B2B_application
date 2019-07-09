package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.compaq.b2b_application.Fragments.Customize_order_frag1;
import com.example.compaq.b2b_application.Fragments.Customize_order_frag2;


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
                Customize_order_frag1 retailerregister_fragment = new Customize_order_frag1();
                return retailerregister_fragment;
            case 1:
                Customize_order_frag2 wholesellerregister_fragment = new Customize_order_frag2();
                return wholesellerregister_fragment;

            case 2:
                Customize_order_frag1 wholesellerregister_fragment1 = new Customize_order_frag1();
                return wholesellerregister_fragment1;

            case 3:
                Customize_order_frag2 wholesellerregister_fragment2 = new Customize_order_frag2();
                return wholesellerregister_fragment2;

            case 4:
                Customize_order_frag2 wholesellerregister_fragment3 = new Customize_order_frag2();
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

package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.compaq.b2b_application.Fragments.AllSellers_display_fragment;
import com.example.compaq.b2b_application.Fragments.MyConnections_fragment;

public class Sellerport_acvtivityAdaper extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs;

    public Sellerport_acvtivityAdaper(Context myContext, FragmentManager fm, int totalTabs) {
        super(fm);
        this.myContext = myContext;
        this.totalTabs = totalTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                AllSellers_display_fragment register_fragment = new AllSellers_display_fragment();
                return register_fragment;
            case 1:
                MyConnections_fragment seller_fragment = new MyConnections_fragment();
                return seller_fragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}

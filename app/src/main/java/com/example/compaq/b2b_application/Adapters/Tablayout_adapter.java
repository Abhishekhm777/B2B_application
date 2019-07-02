package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.compaq.b2b_application.Fragments.Manufacturer_fragment;
import com.example.compaq.b2b_application.Fragments.Register_fragment;
import com.example.compaq.b2b_application.Fragments.Seller_fragment;

public class Tablayout_adapter extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs;

    public Tablayout_adapter(Context myContext,FragmentManager fm,int totalTabs) {
        super(fm);
        this.myContext = myContext;
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Register_fragment register_fragment = new Register_fragment();
                return register_fragment;
            case 1:
                Seller_fragment seller_fragment = new Seller_fragment();
                return seller_fragment;

            case 2:
                Manufacturer_fragment manufacturer_fragment = new Manufacturer_fragment();
                return manufacturer_fragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}

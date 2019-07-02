package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.compaq.b2b_application.Fragments.Register_fragment;
import com.example.compaq.b2b_application.Fragments.SellerPortal_fragment1;
import com.example.compaq.b2b_application.Fragments.SellerPortal_fragment2;
import com.example.compaq.b2b_application.Fragments.Seller_fragment;

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
                SellerPortal_fragment1 register_fragment = new SellerPortal_fragment1();
                return register_fragment;
            case 1:
                SellerPortal_fragment2 seller_fragment = new SellerPortal_fragment2();
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

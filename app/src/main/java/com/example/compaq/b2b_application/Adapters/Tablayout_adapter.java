package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.compaq.b2b_application.Fragments.Manufacturer_fragment;
import com.example.compaq.b2b_application.Fragments.Retailer_register_fragment;
import com.example.compaq.b2b_application.Fragments.Select_CatalogueFragment;
import com.example.compaq.b2b_application.Fragments.Wholeseller_register_fragment;

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
                /*Retailer_register_fragment retailerregister_fragment = new Retailer_register_fragment();
                return retailerregister_fragment;*/
                Select_CatalogueFragment select_catalogueFragment=new Select_CatalogueFragment();
                return select_catalogueFragment;
            case 1:
                Wholeseller_register_fragment wholesellerregister_fragment = new Wholeseller_register_fragment();
                return wholesellerregister_fragment;

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

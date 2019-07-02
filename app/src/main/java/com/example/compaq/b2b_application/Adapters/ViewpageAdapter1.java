package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Fragments.HomeFragment_1;
import com.example.compaq.b2b_application.Main2Activity;
import com.example.compaq.b2b_application.MainActivity;
import com.example.compaq.b2b_application.Model.Viewpager2_model;
import com.example.compaq.b2b_application.Model.Viewpager_model1;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;
import java.util.List;

public
class ViewpageAdapter1 extends PagerAdapter{


   /* public Integer[] arr={R.drawable.home_page_image1,R.drawable.home_page_image2,R.drawable.home_page_image3};*/



    public FragmentActivity context;
    public LayoutInflater layoutInflater;
    public ArrayList<Viewpager_model1> slider_array;



    public
    ViewpageAdapter1(FragmentActivity context, ArrayList<Viewpager_model1> slider_array) {
        this.context=context;
        this.slider_array=slider_array;
    }

    @Override
    public int getCount() {
        return slider_array.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view==(LinearLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        /* layoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);*/
       /* View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_card_layout, parent, false);*/
        layoutInflater=(LayoutInflater)context.getLayoutInflater();
        layoutInflater.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view=layoutInflater.inflate(R.layout.viewpager1_custome_layout,container,false);
        ImageView imageView=(ImageView)item_view.findViewById(R.id.imageview);

        Viewpager_model1 utils=(Viewpager_model1)slider_array.get(position);
        Glide.with(context).load(utils.getSliderImageUrl()).into(imageView);

        container.addView(item_view);
        return item_view;

        /*ViewPager viewPager=(ViewPager) container;
        viewPager.addView(item_view,0);
        return item_view;*/
    }

    public void destroyItem(ViewGroup container,int position,Object object){
        container.removeView((View) object);


        ViewPager vp=(ViewPager)container;
        View view=(View)object;
        vp.removeView(view);

       /* ViewPager vp=(ViewPager)container;
        View view=(View)object;
        vp.removeView(view);*/
    }
}

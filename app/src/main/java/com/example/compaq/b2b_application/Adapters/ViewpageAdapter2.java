package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Activity.Displaying_complete_product_details_Activity;
import com.example.compaq.b2b_application.Activity.ImageOpening_activity;
import com.example.compaq.b2b_application.Model.Viewpager2_model;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;
import java.util.List;

public
class ViewpageAdapter2  extends PagerAdapter {

    public Displaying_complete_product_details_Activity displayingcompleteproductdetailsActivity;
    public LayoutInflater layoutInflater;
    private List<Viewpager2_model> productlist;



    public
    ViewpageAdapter2(Displaying_complete_product_details_Activity displayingcompleteproductdetailsActivity, ArrayList<Viewpager2_model> productlist) {
        this.displayingcompleteproductdetailsActivity = displayingcompleteproductdetailsActivity;
        this.productlist=productlist;
    }
    @Override
    public
    boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view==(LinearLayout)object);
    }
    @NonNull
    @Override
    public
    Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater=(LayoutInflater) displayingcompleteproductdetailsActivity.getLayoutInflater();
        layoutInflater.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view=layoutInflater.inflate(R.layout.viewpager2_custom_layout,container,false);
        ImageView imageView=(ImageView)item_view.findViewById(R.id.customeimage2);


        Viewpager2_model utils=(Viewpager2_model) productlist.get(position);
        final String url=  utils.getSliderImageUrl();

        Glide.with(displayingcompleteproductdetailsActivity).load(url).into(imageView);


        container.addView(item_view);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {

                //Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(displayingcompleteproductdetailsActivity, ImageOpening_activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("URL",url);
                displayingcompleteproductdetailsActivity.startActivity(intent);

            }
        });
        return item_view;
    }

    public void destroyItem(ViewGroup container,int position,Object object){
        container.removeView((View) object);


        ViewPager vp=(ViewPager)container;
        View view=(View)object;
        vp.removeView(view);
    }
    @Override
    public
    int getCount() {
        return productlist.size();

    }


}

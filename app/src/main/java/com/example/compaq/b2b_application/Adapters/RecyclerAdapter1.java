package com.example.compaq.b2b_application.Adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Model.Home_recy_model;
import com.example.compaq.b2b_application.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter1 extends RecyclerView.Adapter<RecyclerAdapter1.ListnerViewHolder> {
    private FragmentActivity mCtx;
    private ArrayList<Home_recy_model> productList;
    private CardView cardView;
    private View view;
    public FragmentManager fragmentManager;
    public FragmentTransaction fragmentTransaction;

    public
    RecyclerAdapter1(FragmentActivity mCtx, ArrayList<Home_recy_model> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @NonNull
    @Override
    public
    RecyclerAdapter1.ListnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(mCtx);

        view=inflater.inflate(R.layout.home_recy_cardlayout,null);
        ListnerViewHolder holder=new ListnerViewHolder(view);
        return holder;
    }

    @Override
    public
    void onBindViewHolder(@NonNull RecyclerAdapter1.ListnerViewHolder holder, int position) {
        Home_recy_model home_recy_model =(Home_recy_model)productList.get(position);
        Glide.with(mCtx).load(mCtx.getResources().getDrawable(home_recy_model.getImageid()))

                .into(holder.imageView);
/*
        Picasso.with(mCtx).load(String.valueOf(mCtx.getResources().getDrawable(home_recy_model.getImageid()))).fit().centerCrop().into(holder.imageView);
*/

      /*  holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(home_recy_model.getImageid()));*/
        holder.textView.setText(home_recy_model.getName());


    }

    @Override
    public
    int getItemCount() {
        return productList.size();
    }

    public
    class ListnerViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView, imageView1;
        TextView textView, textView1;
        public
        ListnerViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.image1);
            textView = (TextView) itemView.findViewById(R.id.text1);
        }
    }
}

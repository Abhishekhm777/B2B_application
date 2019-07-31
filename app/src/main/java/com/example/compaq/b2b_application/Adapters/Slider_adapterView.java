package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Model.Slider_model;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;

public class Slider_adapterView extends RecyclerView.Adapter<Slider_adapterView.MyViewHolder> {
    ArrayList<String>slider_list;
    Context context;
    public Slider_adapterView(Context context,ArrayList<String> slider_list){
        this.context=context;
        this.slider_list=slider_list;
    }


    @NonNull
    @Override
    public Slider_adapterView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull Slider_adapterView.MyViewHolder holder, int i) {

        String url=slider_list.get(i);
        Glide.with(context).load(url).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return slider_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.iv_card);
        }
    }
}

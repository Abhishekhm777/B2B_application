package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;

public class Varients_Adapter extends RecyclerView.Adapter<Varients_Adapter.MyViewHolder> {
    private ArrayList<String> productlist;
    public Context mContext;
    private String imageUrl="https://server.mrkzevar.com/gate/b2b/catalog/api/v1/assets/image/";

    public Varients_Adapter(Context mContext, ArrayList<String> productlist) {
        this.productlist=productlist;
        this.mContext=mContext;

    }
    @NonNull
    @Override
    public Varients_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.varient_card_layout, viewGroup , false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Varients_Adapter.MyViewHolder holder, int i) {


        String url=imageUrl+productlist.get(i);
        Glide.with(mContext).load(url).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return productlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CardView card;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.slider_image);
          /*  DisplayMetrics dm = mContext.getResources().getDisplayMetrics();  //set width of the card
            int width = dm.widthPixels/2;
            card=(CardView)itemView.findViewById(R.id.card_view);
            card.getLayoutParams().width=width;
            card.requestLayout();*/

        }
    }
}

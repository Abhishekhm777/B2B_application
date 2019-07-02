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
import com.example.compaq.b2b_application.Model.Update_product_model;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;

public class Update_product_recy_Adaptetr  extends RecyclerView.Adapter<Update_product_recy_Adaptetr.MyViewHolder> {
    private ArrayList<Update_product_model> productlist;
    public Context mContext;
    private String imageUrl="https://server.mrkzevar.com/gate/b2b/catalog/api/v1/assets/image/";

    public Update_product_recy_Adaptetr(Context mContext, ArrayList<Update_product_model> productlist) {
        this.productlist=productlist;
        this.mContext=mContext;

    }
    @NonNull
    @Override
    public Update_product_recy_Adaptetr.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_recycler_cardlayout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Update_product_recy_Adaptetr.MyViewHolder holder, int position) {
        final com.example.compaq.b2b_application.Model.Update_product_model listner=  productlist.get(position);
          String url=imageUrl+listner.getId();
        Glide.with(mContext).load(url).into(holder.imageView);
        holder.button.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return productlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,button;
        CardView card;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.slider_image);
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();  //set width of the card
            int width = dm.widthPixels/2;
            button=(ImageView) itemView.findViewById(R.id.delete_btn);
            card=(CardView)itemView.findViewById(R.id.card_view);
            card.getLayoutParams().width=width;
            card.requestLayout();
        }
    }
}

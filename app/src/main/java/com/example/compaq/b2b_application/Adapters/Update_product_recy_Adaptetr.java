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
    ArrayList<Update_product_model> productlist;
    public Context mContext;
    private String imageUrl="https://server.mrkzevar.com/gate/b2b/catalog/api/v1/assets/image/";

    public interface IProcessFilter {
        void onProcessFilter(String string1);
    }

    private IProcessFilter mCallback;


    public Update_product_recy_Adaptetr(Context mContext, ArrayList<Update_product_model> productlist, IProcessFilter callback) {
        this.productlist=productlist;
        this.mContext=mContext;
        mCallback=  callback;

    }
    @NonNull
    @Override
    public Update_product_recy_Adaptetr.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.update_pro_main_image_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final Update_product_recy_Adaptetr.MyViewHolder holder, final int position) {

        final com.example.compaq.b2b_application.Model.Update_product_model listner=  productlist.get(position);
          String url=imageUrl+listner.getId();
        Glide.with(mContext).load(url).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(position>0) {
                mCallback.onProcessFilter(listner.getMain_id());
            }
            }
        });

    }

    @Override
    public int getItemCount() {
        return productlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CardView card;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.slider_image);
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();  //set width of the card
            int width = (int) (dm.widthPixels/1.5);

            card=(CardView)itemView.findViewById(R.id.card_view);
            card.getLayoutParams().width=width;
            card.requestLayout();
        }
    }


}

package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.compaq.b2b_application.Model.Cart_recy_model;
import com.example.compaq.b2b_application.Model.Home_recy_model;
import com.example.compaq.b2b_application.Model.Image_Add_model;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;
public class Image_Add_Adapter extends RecyclerView.Adapter<Image_Add_Adapter.MyViewHolder> {
    private ArrayList<Image_Add_model> productlist;
    public Context mContext;

    public Image_Add_Adapter(Context mContext, ArrayList<Image_Add_model> productlist) {
        this.productlist=productlist;
        this.mContext=mContext;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_recycler_cardlayout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final Image_Add_Adapter.MyViewHolder holder, int position) {

        final com.example.compaq.b2b_application.Model.Image_Add_model listner=  productlist.get(position);
        holder.imageView.setImageBitmap(listner.getBitmap());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productlist.remove(holder.getAdapterPosition());
              notifyItemRemoved(holder.getAdapterPosition());

            }
        });

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
            button=(ImageView) itemView.findViewById(R.id.delete_btn);
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();  //set width of the card
            int width = dm.widthPixels/2;

            card=(CardView)itemView.findViewById(R.id.card_view);
            card.getLayoutParams().width=width;
            card.requestLayout();
        }
    }
}

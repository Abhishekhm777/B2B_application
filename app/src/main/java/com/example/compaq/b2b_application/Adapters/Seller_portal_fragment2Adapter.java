package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Activity.All_Sellers_Display_Activity;
import com.example.compaq.b2b_application.Helper_classess.AlertDialogManager;
import com.example.compaq.b2b_application.Activity.MainActivity;
import com.example.compaq.b2b_application.Model.Recy_model2;
import com.example.compaq.b2b_application.Model.SellerPortal_model;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;

import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;

public class Seller_portal_fragment2Adapter extends RecyclerView.Adapter<Seller_portal_fragment2Adapter.MyViewHolder> {
    public FragmentActivity mCtx;
    private ArrayList<SellerPortal_model> productlist;
    private CardView cardView;
    private View view;
    public FragmentManager fragmentManager;
    public FragmentTransaction fragmentTransaction;
    public ImageView imageV;
    public Recy_model2 recyModel2;
    public  Recy_model2 onClickListener;
    public Bundle bundle;
    public Context mContext;
    public SharedPreferences sharedPref;
    SharedPreferences.Editor myEditior;
    AlertDialogManager alert = new AlertDialogManager();
    public Seller_portal_fragment2Adapter(FragmentActivity mCtx, ArrayList<SellerPortal_model> productlist,FragmentManager fragmentManager ) {
        this.productlist=productlist;
        this.mCtx=mCtx;
        this.fragmentManager=fragmentManager;
    }
    @NonNull
    @Override
    public Seller_portal_fragment2Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mCtx).inflate(R.layout.seller_card_layout, parent, false);
        sharedPref =mCtx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        myEditior = sharedPref.edit();
        return new Seller_portal_fragment2Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final SellerPortal_model sellerPortal_model=productlist.get(position);
        holder.seller_name.setText(sellerPortal_model.getName());
        Glide.with(mCtx).load(sellerPortal_model.getImageid()).into(holder.imageV);
        holder.viewprofile.setVisibility(View.GONE);
        holder.sendrequest.setVisibility(View.GONE);


        holder.imageV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myEditior.putString("Wholeseller_mob",sellerPortal_model.getWholeseller_no());
                myEditior.putString("Wholeseller_id",sellerPortal_model.getSeller_id());
                myEditior.apply();
                myEditior.commit();
                Intent intent=new Intent(mCtx, MainActivity.class);
                ((All_Sellers_Display_Activity)mCtx).finish();
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mCtx.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return productlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageV;
        TextView seller_name,viewprofile,sendrequest;
        DisplayMetrics dm = mCtx.getResources().getDisplayMetrics();  //set width of the card
        int width = dm.widthPixels/2;
        CardView card;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageV=itemView.findViewById(R.id.sellerimage);
            seller_name=itemView.findViewById(R.id.sellername);
            viewprofile=itemView.findViewById(R.id.view_profile);
            sendrequest=itemView.findViewById(R.id.send_request);

            card=(CardView)itemView.findViewById(R.id.sellercard_view);
            card.getLayoutParams().width=width;
            card.requestLayout();
        }
    }
}

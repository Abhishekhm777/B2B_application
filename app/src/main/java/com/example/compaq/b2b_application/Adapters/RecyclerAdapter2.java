package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Model.Recy_model2;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;

public class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerAdapter2.ListnerViewHolder> {
    public FragmentActivity mCtx;
    private ArrayList<Recy_model2> productlist;
    private CardView cardView;
    private View view;
    public FragmentManager fragmentManager;
    public FragmentTransaction fragmentTransaction;
    public ImageView imageV;
    public Recy_model2 recyModel2;
    public  Recy_model2 onClickListener;


    public
    RecyclerAdapter2(FragmentActivity mCtx, ArrayList<Recy_model2> productlist) {
        this.mCtx=mCtx;
        this.productlist=productlist;

    }





    @NonNull
    @Override
    public
    RecyclerAdapter2.ListnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(mCtx);

        view=inflater.inflate(R.layout.fragment2_cardlayout,null);
        RecyclerAdapter2.ListnerViewHolder holder=new RecyclerAdapter2.ListnerViewHolder(view);





        return holder;
    }

    @Override
    public
    void onBindViewHolder(@NonNull RecyclerAdapter2.ListnerViewHolder holder, int position) {

        com.example.compaq.b2b_application.Model.Recy_model2 listner= (com.example.compaq.b2b_application.Model.Recy_model2) productlist.get(position);

       /* holder.textView1.setText(listner.getSku());*/

        holder.textView.setText(listner.getName());
        String url=  listner.getImageId();
        Glide.with(mCtx).load(url).into(holder.imageV);

    }



    @Override
    public
    int getItemCount() {
        return productlist.size();
    }

    public
   static class ListnerViewHolder extends RecyclerView.ViewHolder  {
        ImageView imageV;
        TextView textView, textView1,textView2;
       public  Button addcart_button;
        public
        ListnerViewHolder(View view) {
            super(view);
            /*textView1=(TextView)itemView.findViewById(R.id.idtextview);*/
            textView = (TextView) itemView.findViewById(R.id.navtext);
            imageV=(ImageView)itemView.findViewById(R.id.navimage);
            addcart_button=(Button)itemView.findViewById(R.id.cart_button) ;




        }

    }


}



package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.compaq.b2b_application.Model.Inner_Recy_model;
import com.example.compaq.b2b_application.Model.Recycler_model3;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;

public class Varients_buyer_adapter extends RecyclerView.Adapter<Varients_buyer_adapter.ListnerViewHolder> {
    public Context context;
    public ArrayList<Recycler_model3> detProductlist;
    private View view;
    private int value;
    private FragmentManager fragmentManager;

    public Varients_buyer_adapter(Context context, ArrayList<Recycler_model3> detProductlist, int value,FragmentManager fragmentManager) {
        this.context = context;
        this.detProductlist=detProductlist;
        this.value=value;
        this.fragmentManager=fragmentManager;

    }
    @NonNull
    @Override
    public Varients_buyer_adapter.ListnerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(viewGroup.getContext());
       /* if(value==0) {
            view = inflater.inflate(R.layout.main2cardlayout, parent, false);
            Log.e("Value","0");
        }
        else {
            view = inflater.inflate(R.layout.update_product_main_layout, parent, false);
            Log.e("Value","1");
        }*/
        view = inflater.inflate(R.layout.buyer_varients_layout, viewGroup, false);
        Varients_buyer_adapter.ListnerViewHolder holder=new Varients_buyer_adapter.ListnerViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Varients_buyer_adapter.ListnerViewHolder holder, int i) {
        Recycler_model3 main2_listner=(Recycler_model3)detProductlist.get(i);

        ArrayList<Inner_Recy_model> arrayList=main2_listner.getArrayList();
        Varients_inner_Adapter inner_recycler_adapter=new Varients_inner_Adapter(context,arrayList,holder.cardView,fragmentManager);
/*
        holder.innerRecyclerview.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
*/
        holder.innerRecyclerview.setLayoutManager(new GridLayoutManager(context.getApplicationContext(), 2, GridLayoutManager.VERTICAL, false));
        holder.innerRecyclerview.setHasFixedSize(true);
        holder.innerRecyclerview.setAdapter(inner_recycler_adapter);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cardView.setBackgroundResource(R.drawable.background_design);
                Log.e("DVDV","DVDVDVDVD");
            }
        });
    }

    @Override
    public int getItemCount() {
        return detProductlist.size();
    }

    public class ListnerViewHolder extends RecyclerView.ViewHolder {
        RecyclerView innerRecyclerview;
        Inner_RecyclerAdapter4 inner_recycler_adapter;
        CardView cardView;

        public ListnerViewHolder(@NonNull View itemView) {
            super(itemView);
            innerRecyclerview=(RecyclerView)itemView.findViewById(R.id.recyclewview2);
            cardView=itemView.findViewById(R.id.card_view);

        }
    }
}

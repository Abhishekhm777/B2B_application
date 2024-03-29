package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.compaq.b2b_application.Model.Inner_Recy_model;
import com.example.compaq.b2b_application.Model.Recycler_model3;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;

public class RecyclerAdapter3 extends RecyclerView.Adapter<RecyclerAdapter3.ListnerViewHolder>{
    public Context context;
    public ArrayList<Recycler_model3> detProductlist;
    private View view;
    private int value;


    public RecyclerAdapter3(Context context, ArrayList<Recycler_model3> detProductlist, int value) {
        this.context = context;
        this.detProductlist=detProductlist;
        this.value=value;

    }


    @NonNull
    @Override
    public RecyclerAdapter3.ListnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
       /* if(value==0) {
            view = inflater.inflate(R.layout.main2cardlayout, parent, false);
            Log.e("Value","0");
        }
        else {
            view = inflater.inflate(R.layout.update_product_main_layout, parent, false);
            Log.e("Value","1");
        }*/
        view = inflater.inflate(R.layout.update_product_main_layout, parent, false);
        RecyclerAdapter3.ListnerViewHolder holder=new RecyclerAdapter3.ListnerViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter3.ListnerViewHolder holder, int position) {
        Recycler_model3 main2_listner=(Recycler_model3)detProductlist.get(position);

        holder.textView.setText(main2_listner.getHeadings());
        ArrayList<Inner_Recy_model> arrayList=main2_listner.getArrayList();
        Inner_RecyclerAdapter4 inner_recycler_adapter=new Inner_RecyclerAdapter4(context,arrayList,1, holder.cardView);
/*
        holder.innerRecyclerview.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
*/
        holder.innerRecyclerview.setLayoutManager(new GridLayoutManager(context.getApplicationContext(), 1, GridLayoutManager.VERTICAL, false));
        holder.innerRecyclerview.setHasFixedSize(true);
        holder.innerRecyclerview.setAdapter(inner_recycler_adapter);
    }



    @Override
    public int getItemCount() {
        return detProductlist.size();
    }

    public class ListnerViewHolder extends RecyclerView.ViewHolder {
        RecyclerView innerRecyclerview;
        Inner_RecyclerAdapter4 inner_recycler_adapter;
        TextView textView,textView1;
        CardView cardView;
        public ListnerViewHolder(View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.heading_textview);
            innerRecyclerview=(RecyclerView)itemView.findViewById(R.id.recyclewview2);
        }


    }
}

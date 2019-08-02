package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.compaq.b2b_application.Activity.Displaying_complete_product_details_Activity;
import com.example.compaq.b2b_application.Model.Inner_Recy_model;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;

public class Inner_RecyclerAdapter4 extends RecyclerView.Adapter<Inner_RecyclerAdapter4.ListnerViewHolder>{
    public Context context;
    public ArrayList<Inner_Recy_model> details_list;
    private View view;
    private int value;
    public
    Inner_RecyclerAdapter4(Context context, ArrayList<Inner_Recy_model> details_list,int value) {
        this.context = context;
        this.details_list=details_list;
        this.value=value;
    }
    @NonNull
    @Override
    public Inner_RecyclerAdapter4.ListnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());

            view = inflater.inflate(R.layout.inner_recycler_card, parent, false);


        Inner_RecyclerAdapter4.ListnerViewHolder holder=new Inner_RecyclerAdapter4.ListnerViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Inner_RecyclerAdapter4.ListnerViewHolder holder, int position) {
        Inner_Recy_model inner_recy_listner=(Inner_Recy_model)details_list.get(position);
        holder.textView1.setText(inner_recy_listner.getKey());
        holder.textView2.setText(inner_recy_listner.getValues());

    }

    @Override
    public int getItemCount() {
        return details_list.size();
    }

    public class ListnerViewHolder extends RecyclerView.ViewHolder {
        TextView textView1,textView2;
        public ListnerViewHolder(View itemView) {
            super(itemView);
            textView1=(TextView)itemView.findViewById(R.id.keytextview);
            textView2=(TextView)itemView.findViewById(R.id.valuetextview);

        }
    }
}

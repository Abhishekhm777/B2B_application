package com.example.compaq.b2b_application.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.compaq.b2b_application.Model.Top_model;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Custom_Order_search_Adapter  extends BaseAdapter {
    private LayoutInflater inflater;
    private Context _context;
    private ArrayList<Top_model> names;
    private String wholesaler;

    public Custom_Order_search_Adapter(Context context, ArrayList<Top_model> names,String wholesaler) throws NullPointerException{

        super();
        inflater = LayoutInflater.from(context);
        this._context = context;
        this.names = names;
       this.wholesaler=wholesaler;

    }
    @Override
    public int getCount() {
            return names.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {

            view=  inflater.inflate(R.layout.custom_search_layout, viewGroup,false);

        }

        final Top_model   currentMovie = names.get(i);
        TextView textView=(TextView)view.findViewById(R.id.names);
        textView.setText(currentMovie.getPname());
        TextView textView2=(TextView)view.findViewById(R.id.skus);
        textView2.setText (currentMovie.getPsku());


        return view;
    }
}

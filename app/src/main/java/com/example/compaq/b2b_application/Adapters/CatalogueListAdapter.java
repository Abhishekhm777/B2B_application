package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.compaq.b2b_application.R;

import java.util.ArrayList;

public class CatalogueListAdapter extends BaseAdapter {
    Context context;
    ArrayList<String>listItem;
    private static LayoutInflater inflater=null;

    public  CatalogueListAdapter(Context context,ArrayList<String>listItem){
        this.context=context;
        this.listItem=listItem;
        inflater = LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        return listItem.size();
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
        View vi=view;
        if (vi == null) {

            vi=  inflater.inflate(R.layout.category_listview, viewGroup,false);
        }
        TextView textView=(TextView) vi.findViewById(R.id.catalog_name);
        textView.setText(listItem.get(i));
        return vi;
    }
}

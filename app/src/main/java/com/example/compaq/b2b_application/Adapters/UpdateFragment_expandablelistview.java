package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.compaq.b2b_application.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpdateFragment_expandablelistview extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, ArrayList<String>> _listDataChild;
    private HashMap<String, ArrayList<String>> value_map;

    private LayoutInflater inflater;
    public UpdateFragment_expandablelistview(Context context, List<String> listDataHeader,
                                             HashMap<String, ArrayList<String>> listChildData, HashMap<String, ArrayList<String>> value_map) {

        super();

        inflater = LayoutInflater.from(context);
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.value_map = value_map;

    }
    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return this._listDataChild.get(this._listDataHeader.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return this._listDataHeader.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return this._listDataChild.get(this._listDataHeader.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

            final String childText = (String) getGroup(i);

            if (view == null) {

                view = inflater.inflate(R.layout.update_expand_header, viewGroup, false);

            }

        final TextView txtListChild = (TextView) view
                .findViewById(R.id.header);
        txtListChild.setText(childText);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final String childText = (String) getChild(i, i1);
        final String keys = (String) getKey(i, i1);
        if (view == null) {

            view=  inflater.inflate(R.layout.update_expand_child, viewGroup,false);
        }
        final TextView key = (TextView) view
                .findViewById(R.id.key);
        final TextView value = (TextView) view
                .findViewById(R.id.value);
        key.setText(childText);
        value.setText(keys);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
    public Object getKey(int i, int i1) {
        return this.value_map.get(this._listDataHeader.get(i)).get(i1);
    }
}

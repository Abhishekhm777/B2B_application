package com.example.compaq.b2b_application.Adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.compaq.b2b_application.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class expand_listview2 extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public ArrayList<String> selection;
    public expand_listview2(Context context, List<String> listDataHeader,
                            HashMap<String, List<String>> listChildData, ArrayList<String> selection) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.selection=selection;
    }
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public
    View getChildView(int groupPosition, final int childPosition,
                      boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_list_item_layout, null);

        }

        CheckedTextView txtListChild = (CheckedTextView) convertView
                .findViewById(R.id.check_box_child);

        txtListChild.setText(childText);

        if(selection.contains(txtListChild.getText().toString())) {
            txtListChild.setChecked(true);
        }
        else {
            txtListChild.setChecked(false);
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandabale_grouplayout, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.expand_id);
        //  lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        ImageView imageView = convertView.findViewById(R.id.header_indicator);
if(isExpanded) {

    imageView.setImageResource(R.drawable.ic_expand_less_black_24dp);
    ObjectAnimator animator =   ObjectAnimator.ofFloat(imageView,"rotation",0,180f);
    animator.setDuration(400);
    animator.start();

}
else {

    imageView.setImageResource(R.drawable.ic_expand_more_black_24dp);
    ObjectAnimator animator =   ObjectAnimator.ofFloat(groupPosition,"rotation",0,180f);
    animator.setDuration(400);
    animator.start();
}
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}

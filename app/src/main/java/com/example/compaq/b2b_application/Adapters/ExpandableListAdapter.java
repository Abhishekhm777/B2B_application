package com.example.compaq.b2b_application.Adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.compaq.b2b_application.R;

import java.util.HashMap;
import java.util.List;
public
class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
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
            convertView = infalInflater.inflate(R.layout.list_item, null);

        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
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
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        //  lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

       /* if ( getChildrenCount( groupPosition ) == 0 ) {
            indicator.setVisibility( View.INVISIBLE );
        } else {
            indicator.setVisibility( View.VISIBLE );
            indicator.setImageResource( isExpanded ? R.drawable.list_group_expanded : R.drawable.list_group_closed );
        }*/
        ImageView imageView = convertView.findViewById(R.id.header_indicator);
        if(isExpanded) {
            lblListHeader.setTypeface(lblListHeader.getTypeface(), Typeface.BOLD);
            imageView.setImageResource(R.drawable.ic_expand_more_black_24dp);
            ObjectAnimator animator =   ObjectAnimator.ofFloat(imageView,"rotation",0,360);
            animator.setDuration(400);
            animator.start();

        }
        else {
            lblListHeader.setTypeface(null, Typeface.NORMAL);

/*
            imageView.setImageResource(R.drawable.ic_expand_more_black_24dp);
*/

           /* ObjectAnimator animator =   ObjectAnimator.ofFloat(groupPosition,"rotation",0,180f);
            animator.setDuration(400);
            animator.start();*/
        }

        try {
            if (getChildrenCount(groupPosition) == 0) {
                imageView.setVisibility(View.INVISIBLE);
            } else {
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(isExpanded ? R.drawable.ic_expand_more_black_24dp : R.drawable.ic_expand_less_black_24dp);
            }
        }
        catch (NullPointerException e){
            e.printStackTrace();
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

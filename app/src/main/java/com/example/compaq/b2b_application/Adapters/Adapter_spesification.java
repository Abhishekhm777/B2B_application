package com.example.compaq.b2b_application.Adapters;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.compaq.b2b_application.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Adapter_spesification extends BaseExpandableListAdapter  {
    private Context _context;
    private ArrayList<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, ArrayList<String>> _listDataChild;
    private  Dialog dialog;
    private LayoutInflater inflater;
    public    HashMap<String ,String> values=new HashMap<>();

    public Adapter_spesification(Context context, ArrayList<String> listDataHeader,
                                 HashMap<String, ArrayList<String>> listChildData) throws NullPointerException{

       super();
        inflater = LayoutInflater.from(context);
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.header_edit_layout);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) throws NullPointerException {

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
      return  i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return  i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final  int i, boolean b, View view, final ViewGroup viewGroup) {
        final String headerTitle = (String) getGroup(i);
        if (view == null) {

            view=  inflater.inflate(R.layout.add_cat_header, viewGroup,false);
        }
       /* TextView lblListHeader = (TextView) view
                .findViewById(R.id.lblListHeader);
        //  lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);*/
       final EditText  lblListHeader = (EditText) view
                .findViewById(R.id.edit);
        lblListHeader.setText(headerTitle);

        lblListHeader.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                dialog.show();
                final EditText editText=dialog.findViewById(R.id.popup_textview);
                TextView ok=dialog.findViewById(R.id.ok);
                TextView cancel=dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            ArrayList<String> hhh=_listDataChild.get(headerTitle);
                            _listDataChild.remove(lblListHeader);
                            notifyDataSetChanged();
                            _listDataHeader.set(i, editText.getText().toString());
                            _listDataChild.put(editText.getText().toString(),hhh);
                            notifyDataSetChanged();

                        }
                        catch (Exception e){

                        }

                      dialog.dismiss();
                    }
                });
                editText.setText(headerTitle);
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager)_context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);


                return false;
            }
        });


        ImageView button=(ImageView) view.findViewById(R.id.del);
        ImageView add=(ImageView) view.findViewById(R.id.add_child);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _listDataChild.get(_listDataHeader.get(i)).add("");
                notifyDataSetChanged();

            }
        });
        if(b){
            lblListHeader.setTypeface(null, Typeface.BOLD);
            add.setVisibility(View.VISIBLE);
        }
        else {
            add.setVisibility(View.GONE);
            lblListHeader.setTypeface(null, Typeface.NORMAL);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               _listDataHeader.remove(i);
               notifyDataSetChanged();

            }
        });

        ImageView imageView = view.findViewById(R.id.header_indicator);
        if(b) {
            lblListHeader.setTypeface(lblListHeader.getTypeface(), Typeface.BOLD);
            imageView.setImageResource(R.drawable.ic_expand_more_black_24dp);
            ObjectAnimator animator =   ObjectAnimator.ofFloat(imageView,"rotation",0,360);
            animator.setDuration(400);
            animator.start();

        }
        else {
            lblListHeader.setTypeface(null, Typeface.NORMAL);
        }
        try {

            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(b ? R.drawable.ic_expand_more_black_24dp : R.drawable.ic_expand_less_black_24dp);

        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, final ViewGroup viewGroup) {
        try {
            final String childText = (String) getChild(i, i1);

            if (view == null) {

                view = inflater.inflate(R.layout.specify_child_item, viewGroup, false);

            }

       /* TextView txtListChild = (TextView) view
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);*/

            final EditText txtListChild = (EditText) view
                    .findViewById(R.id.key);
            txtListChild.setText(childText);

            final EditText editext = (EditText) view
                    .findViewById(R.id.value);
            editext.setText( values.get(_listDataChild.get(_listDataHeader.get(i)).get(i1)));
            try {
                txtListChild.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        try {

                            _listDataChild.get(_listDataHeader.get(i)).set(i1, txtListChild.getText().toString());

                        }
                        catch (Exception e){

                        }


                    }
                });
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }

            editext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onFocusChange(View view, boolean b) {
                    try {
                        values.put(_listDataChild.get(_listDataHeader.get(i)).get(i1), editext.getText().toString());

                    }
                    catch (Exception e){

                    }


                }
            });


            ImageView im = (ImageView) view.findViewById(R.id.delete_c);
            im.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    txtListChild.clearFocus();
                    editext.clearFocus();

                    values.remove( _listDataChild.get(
                            _listDataHeader.get(i)).get(
                            i1));
                    _listDataChild.get(_listDataHeader.get(i)).remove(_listDataChild.get(
                            _listDataHeader.get(i)).get(
                            i1));

                    notifyDataSetChanged();
                    try {
                        Log.e("CLICKED", _listDataChild.get(
                                _listDataHeader.get(i)).get(
                                i1));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            });
        }
        catch (Exception e){

        }

            return view;

    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public HashMap<String, String> getValuemap() {
        return values;
    }
}

package com.example.compaq.b2b_application.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.compaq.b2b_application.Model.Top_model;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;

import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;

public class User_class_Adapter extends BaseAdapter {
    private Context _context;
    private ArrayList<Top_model> _listDataHeader;
    private  ArrayList<String> user_assigned_list;
    // header titles
    // child data in format of header title, child title
    Dialog dialog;
    TextView ok,cancel,main_text;
    public SharedPreferences sharedPref;
    private   String output,wholseller_id;
    private LayoutInflater inflater;
    View view;
    private  ArrayList<String> check_list=new ArrayList<>();



    public User_class_Adapter(Context context, ArrayList<Top_model> listDataHeader,ArrayList<String> user_assigned_list) {
        super();
        inflater = LayoutInflater.from(context);
        this._context = context;
        this._listDataHeader = listDataHeader;
        this.view=view;
        this.wholseller_id=wholseller_id;
        this.user_assigned_list=user_assigned_list;
        dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.alert_popup);
        ok=(TextView)dialog.findViewById(R.id.ok);
        cancel=(TextView)dialog.findViewById(R.id.cancel);
        main_text=(TextView)dialog.findViewById(R.id.popup_textview);
        sharedPref=_context.getSharedPreferences("USER_DETAILS",0);
        output=sharedPref.getString(ACCESS_TOKEN, null);
    }

    public User_class_Adapter(Context context, ArrayList<Top_model> listDataHeader) {
        super();
        inflater = LayoutInflater.from(context);
        this._context = context;
        this._listDataHeader = listDataHeader;
        this.view=view;
        this.wholseller_id=wholseller_id;
        this.user_assigned_list=user_assigned_list;
        dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.alert_popup);
        ok=(TextView)dialog.findViewById(R.id.ok);
        cancel=(TextView)dialog.findViewById(R.id.cancel);
        main_text=(TextView)dialog.findViewById(R.id.popup_textview);
        sharedPref=_context.getSharedPreferences("USER_DETAILS",0);
        output=sharedPref.getString(ACCESS_TOKEN, null);
    }

    @Override
    public int getCount() {
        return _listDataHeader.size();
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

            view=  inflater.inflate(R.layout.top_layout, viewGroup,false);

        }
        final Top_model   currentMovie = _listDataHeader.get(i);

        TextView textView=(TextView)view.findViewById(R.id.text_view);
        textView.setText(currentMovie.getName());

        final CheckBox checkBox=(CheckBox)view.findViewById(R.id.top_check);

               if(user_assigned_list!=null){
                        if (user_assigned_list.contains(currentMovie.getName())) {
                            checkBox.setChecked(true);
                            if (!check_list.contains(currentMovie.getName())) {
                                check_list.add(currentMovie.getName());
                            }
                        }
            }


        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if(checkBox.isChecked()){
                     if(!check_list.contains(currentMovie.getName())) {
                         check_list.add(currentMovie.getName());
                         if(user_assigned_list!=null) {
                             user_assigned_list.remove(currentMovie.getName());
                         }
                         notifyDataSetChanged();
                     }
                 }
                 if(!checkBox.isChecked()){
                     check_list.remove(currentMovie.getName());
                     if(user_assigned_list!=null) {
                         user_assigned_list.remove(currentMovie.getName());
                     }
                     notifyDataSetChanged();
                 }
            }
        });

        return  view;
    }
    public ArrayList  getValuemap() {
        return check_list;
    }

}

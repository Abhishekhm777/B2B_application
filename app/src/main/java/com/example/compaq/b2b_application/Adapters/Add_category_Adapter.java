package com.example.compaq.b2b_application.Adapters;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Customize_Order;
import com.example.compaq.b2b_application.Fragments.Add_new_Category;
import com.example.compaq.b2b_application.Fragments.Customize_order_frag1;
import com.example.compaq.b2b_application.Fragments.Manage_category_frag1;
import com.example.compaq.b2b_application.Manage_Categories;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.compaq.b2b_application.Fragments.Fragment_2.URL_DATA;
import static com.example.compaq.b2b_application.MainActivity.ip_cat;
import static com.example.compaq.b2b_application.SessionManagement.ACCESS_TOKEN;

public class Add_category_Adapter extends BaseExpandableListAdapter {
    private Context _context;
    private ArrayList<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, ArrayList<String>> _listDataChild;
    private LayoutInflater inflater;
    private HashMap<String, String> value_map;
    private ArrayList<String> filters;
    private ArrayList<String> varients;
    public SharedPreferences sharedPref;
    private   String output,wholseller_id;
    private Dialog dialog;
    public FragmentManager fragmentManager;

    public Add_category_Adapter(Context context, ArrayList<String> listDataHeader,
                                HashMap<String, ArrayList<String>> listChildData, HashMap<String, String> value_map, ArrayList<String> filters, ArrayList<String> varients) {

        super();
        inflater = LayoutInflater.from(context);
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.value_map=value_map;
        this.filters=filters;
        this.varients=varients;
        dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.header_edit_layout);

        sharedPref=_context.getSharedPreferences("USER_DETAILS",0);

         output=sharedPref.getString(ACCESS_TOKEN, null);
        wholseller_id = sharedPref.getString("userid", null);

    }
    @Override
    public int getGroupCount() {
        return _listDataHeader.size();
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
    public View getGroupView(final  int i, boolean b, View view, ViewGroup viewGroup) {
       final String headerTitle = (String) getGroup(i);
        if (view == null) {

            view=  inflater.inflate(R.layout.add_cat_header, viewGroup,false);
        }


        final EditText lblListHeader = (EditText) view
                .findViewById(R.id.edit);
        //  lblListHeader.setTypeface(null, Typeface.BOLD);
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
            add.setVisibility(View.VISIBLE);
        }
        else {
            add.setVisibility(View.GONE);
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
    public View getChildView(final  int i, final  int i1, final  boolean b, View view, ViewGroup viewGroup) {
        try {
            final String childText = (String) getChild(i, i1);

            if (view == null) {

                view = inflater.inflate(R.layout.add_cat_child, viewGroup, false);

            }

            final CheckBox fil_check=(CheckBox)view.findViewById(R.id.filter);
            final CheckBox var_check=(CheckBox)view.findViewById(R.id.varient);


                fil_check.setChecked(filters.contains(childText));
                var_check.setChecked(varients.contains(childText));

               fil_check.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {

                       if (filters.contains(childText)) {
                           filters.remove(childText);
                       }
                       else {
                           filters.add(childText);

                       }
                   }
               });
               var_check.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       if(varients.contains(childText)){
                           varients.remove(childText);
                       }
                       else {
                           varients.add(childText);
                       }
                   }
               });

            final EditText txtListChild = (EditText) view
                    .findViewById(R.id.key);
            txtListChild.setText(childText);

            final EditText editext = (EditText) view
                    .findViewById(R.id.value);
            editext.setText( value_map.get(childText));
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
                        value_map.put(_listDataChild.get(_listDataHeader.get(i)).get(i1),editext.getText().toString());

                    }
                    catch (Exception e){

                    }
                }
            });
            ImageView imageView = (ImageView) view.findViewById(R.id.delete_child);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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

    public void add_Category(String name,String path,final View view) {
        JSONObject main_object= new JSONObject();
        try {
            main_object.put("name",name);
            JSONArray pare_array=new JSONArray();
            pare_array.put(path);
            main_object.put("parent",pare_array);
            main_object.put("wholesaler",wholseller_id);

            JSONArray cat_path=new JSONArray();

            cat_path.put(path.concat(","+name));

            main_object.put("categoriesPath",cat_path);

            JSONArray jsonArray=new JSONArray();
            for(int i=0;i<_listDataHeader.size();i++){
                JSONObject spec_head=new JSONObject();
                spec_head.put("headingName",_listDataHeader.get(i));
                JSONArray spec_key_array=new JSONArray();

                for(int k=0;k<_listDataChild.get(_listDataHeader.get(i)).size();k++){
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("key",_listDataChild.get(_listDataHeader.get(i)).get(k));
                    jsonObject.put("varient",varients.contains(_listDataChild.get(_listDataHeader.get(i)).get(k)));
                    jsonObject.put("filterable",filters.contains(_listDataChild.get(_listDataHeader.get(i)).get(k)));

                    JSONArray val_arr=new JSONArray();
                     val_arr.put(value_map.get(_listDataChild.get(_listDataHeader.get(i)).get(k)));
                     jsonObject.put("value",val_arr);

                    spec_key_array.put(jsonObject);
                }
                spec_head.put("keyes",spec_key_array);
                jsonArray.put(spec_head);
            }
            main_object.put("specificationHeading",jsonArray);

             Log.e("OBJECT  SC SC ",main_object.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }


        URL_DATA = ip_cat + "/category/secure/add?wholesaler="+wholseller_id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_DATA, main_object, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    Snackbar.make(view, "Category Addedd Successfully", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                   if(_context instanceof Manage_Categories) {
                       Manage_category_frag1 f = (Manage_category_frag1) ((AppCompatActivity) _context).getSupportFragmentManager().findFragmentByTag("manage_frag1");
                       f.refreshMethod();
                       ((AppCompatActivity) _context).getSupportFragmentManager().popBackStackImmediate();
                   }
                   if(_context instanceof Customize_Order){
                       Customize_order_frag1 f = (Customize_order_frag1) ((AppCompatActivity) _context).getSupportFragmentManager().findFragmentByTag("customize");
                       f.refreshMethod();
                       ((AppCompatActivity) _context).getSupportFragmentManager().popBackStackImmediate();
                   }

                }

                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headr = new HashMap<>();
                headr.put("Authorization","bearer "+output);
                headr.put("Content-Type", "application/json");
                return headr;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(_context);
        queue.add(request);
    }


}

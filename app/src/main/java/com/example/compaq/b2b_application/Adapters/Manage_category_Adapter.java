package com.example.compaq.b2b_application.Adapters;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Fragments.Add_new_Category;
import com.example.compaq.b2b_application.Fragments.Edit_Category_frag;
import com.example.compaq.b2b_application.R;

import org.michaelbel.bottomsheet.BottomSheet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;


public class Manage_category_Adapter extends BaseExpandableListAdapter   {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private HashMap<String, String> list_id;

    private LayoutInflater inflater;
    Dialog dialog;
    TextView ok,cancel,main_text;
    public SharedPreferences sharedPref;
    String wholseller_id;
    private FragmentTransaction fragmentTransaction;
    public FragmentManager fragmentManager;
    private  View view;

    public Manage_category_Adapter(Context context, List<String> listDataHeader,
                                   HashMap<String, List<String>> listChildData, HashMap<String, String> list_id,FragmentManager fragmentManager, View view) {

        super();
        inflater = LayoutInflater.from(context);
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.list_id=list_id;
        this.fragmentManager=fragmentManager;
        this.view=view;

        dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.alert_popup);
        ok=(TextView)dialog.findViewById(R.id.ok);
        cancel=(TextView)dialog.findViewById(R.id.cancel);
        main_text=(TextView)dialog.findViewById(R.id.popup_textview);

        sharedPref =_context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        wholseller_id = sharedPref.getString("userid", null);
    }

    @Override
    public int getGroupCount() {
        return  this._listDataHeader.size();
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
    public View getGroupView( final  int i, boolean b, View view, ViewGroup viewGroup) {
        final String headerTitle = (String) getGroup(i);
        if (view == null) {

            view=  inflater.inflate(R.layout.manage_category_header, viewGroup,false);
        }
       /* TextView lblListHeader = (TextView) view
                .findViewById(R.id.lblListHeader);
        //  lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);*/
        final TextView lblListHeader = (TextView) view
                .findViewById(R.id.header);
        //  lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        /*lblListHeader.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                lblListHeader.setFocusableInTouchMode(true);
                return false;
            }
        });*/
        ImageView button=(ImageView) view.findViewById(R.id.del_hedr);
        ImageView add=(ImageView) view.findViewById(R.id.add_header);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final Dialog d;
                d = new Dialog(_context);
                d.setContentView(R.layout.option_layouttt);
                d.show();
             LinearLayout add=(LinearLayout) d.findViewById(R.id.add);
                LinearLayout edit=(LinearLayout) d.findViewById(R.id.edit);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                        Bundle bundle=new Bundle();
                        bundle.putString("pro_id",list_id.get(headerTitle));
                        fragmentTransaction = fragmentManager.beginTransaction();
                        Add_new_Category top_categories=new Add_new_Category();
                        top_categories.setArguments(bundle);
                        fragmentTransaction.replace(R.id.manage, top_categories).addToBackStack(null).commit();
                    }
                });
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                        Bundle bundle=new Bundle();
                        bundle.putString("pro_id",list_id.get(headerTitle));
                        fragmentTransaction = fragmentManager.beginTransaction();
                        Edit_Category_frag edit_category_frag=new Edit_Category_frag();
                        edit_category_frag.setArguments(bundle);
                        fragmentTransaction.replace(R.id.manage, edit_category_frag).addToBackStack(null).commit();
                    }
                });


            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_text.setText("Do you want to Delete "+headerTitle+" Category ?");
                dialog.show();
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        _listDataHeader.remove(i);
                        notifyDataSetChanged();
                        delete_cate( list_id.get(headerTitle),i);

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


            }
        });


        ImageView imageView = view.findViewById(R.id.header_indicator);
        if(b) {

            add.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);


            lblListHeader.setTypeface(lblListHeader.getTypeface(), Typeface.BOLD);
            imageView.setImageResource(R.drawable.ic_expand_more_black_24dp);
            ObjectAnimator animator =   ObjectAnimator.ofFloat(imageView,"rotation",0,360);
            animator.setDuration(400);
            animator.start();

        }
        else {

            add.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
            lblListHeader.setTypeface(null, Typeface.NORMAL);
        }
        try {
            if (getChildrenCount(i) == 0) {
                imageView.setVisibility(View.INVISIBLE);
            } else {
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(b ? R.drawable.ic_expand_more_black_24dp : R.drawable.ic_expand_less_black_24dp);
            }
        } catch (NullPointerException e){
            e.printStackTrace();
        }


        return view;


}

    @Override
    public View getChildView(final int i, final int i1, final boolean b,  View view,final ViewGroup viewGroup) {
        try {
            final String childText = (String) getChild(i, i1);

            if (view == null) {

                view = inflater.inflate(R.layout.manage_category_child, viewGroup, false);

            }


            final TextView txtListChild = (TextView) view
                    .findViewById(R.id.child);
            txtListChild.setText(childText);

            ImageView button=(ImageView) view.findViewById(R.id.child_del);
            ImageView add=(ImageView) view.findViewById(R.id.add_child);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog d;
                    d = new Dialog(_context);
                    d.setContentView(R.layout.option_layouttt);
                    d.show();
                    LinearLayout add=(LinearLayout) d.findViewById(R.id.add);
                    LinearLayout edit=(LinearLayout) d.findViewById(R.id.edit);
                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            d.dismiss();
                            Bundle bundle=new Bundle();
                            bundle.putString("pro_id",list_id.get(childText));
                            fragmentTransaction = fragmentManager.beginTransaction();
                            Add_new_Category top_categories=new Add_new_Category();
                            top_categories.setArguments(bundle);
                            fragmentTransaction.replace(R.id.manage, top_categories).addToBackStack(null).commit();
                        }
                    });
                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            d.dismiss();
                            Bundle bundle=new Bundle();
                            bundle.putString("pro_id",list_id.get(childText));
                            fragmentTransaction = fragmentManager.beginTransaction();
                            Edit_Category_frag edit_category_frag=new Edit_Category_frag();
                            edit_category_frag.setArguments(bundle);
                            fragmentTransaction.replace(R.id.manage, edit_category_frag).addToBackStack(null).commit();
                        }
                    });

                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    main_text.setText("Do you want to Delete "+childText+" Category ?");
                    dialog.show();
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            _listDataChild.get(_listDataHeader.get(i)).remove(i1);
                            notifyDataSetChanged();
                            delete_cate( list_id.get(childText),i);

                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

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

    public void delete_cate(String id,final int i){

        String url=ip+"gate/b2b/catalog/api/v1/category/secure/delete/"+id+"?wholesaler="+wholseller_id;

        StringRequest stringRequest=new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public
            void onResponse(String response) {

                try {

                    Snackbar.make(view, "Category Deleted Successfully", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();




                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        },new Response.ErrorListener() {
            @Override
            public
            void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;


                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 404:
                            BottomSheet.Builder builder = new BottomSheet.Builder(_context);
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;
                        case 400:
                            BottomSheet.Builder builder1 = new BottomSheet.Builder(_context);
                            builder1.setTitle("Sorry! No Products Available");
                            builder1.show();
                            break;
                        case 417:

                            Snackbar.make(view, "Sorry! No Products Available", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                           /* BottomSheet.Builder builder2 = new BottomSheet.Builder(getContext());
                            builder2.setTitle("Sorry! No Products Available");
                            builder2.show();*/
                            break;


                    }
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() {

                String output=sharedPref.getString(ACCESS_TOKEN, null);
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(_context);
        requestQueue.add(stringRequest);
    }
}

package com.example.compaq.b2b_application.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Model.Top_model;
import com.example.compaq.b2b_application.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import static com.example.compaq.b2b_application.Activity.MainActivity.ip_cat;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;

public class Top_adapter extends BaseAdapter {
    private Context _context;
    private ArrayList<Top_model> _listDataHeader; // header titles
    // child data in format of header title, child title
    Dialog dialog;
    TextView ok,cancel,main_text;
    public SharedPreferences sharedPref;
    private   String output,wholseller_id,urldata;
    private LayoutInflater inflater;
    View view;
    ArrayList<Boolean> check_list=new ArrayList<>();


    public Top_adapter(Context context, ArrayList<Top_model> listDataHeader,View view,String wholseller_id) {

        super();
        inflater = LayoutInflater.from(context);
        this._context = context;
        this._listDataHeader = listDataHeader;
        this.view=view;
        this.wholseller_id=wholseller_id;
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
        checkBox.setChecked(currentMovie.getaBoolean());
        if(currentMovie.getaBoolean()==true) {
            check_list.add(true);
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!currentMovie.getaBoolean()) {
                    if (check_list.size() <= 4) {
                        main_text.setText("Do you want to add " + currentMovie.getName() + " as a Top category ?");
                        dialog.show();
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();

                                add_topFive(currentMovie.getJsonObject(), true, currentMovie);

                            }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                checkBox.setChecked(false);
                            }
                        });

                    }
                    else {
                        checkBox.setChecked(false);
                        Snackbar.make(view, "Top Category  Cannot be more than Five", Snackbar.LENGTH_LONG )
                                .setAction("Action", null).show();
                    }
                }
                if(currentMovie.getaBoolean()){
                    main_text.setText("Do you want to remove "+ currentMovie.getName() +" from Top category ?");
                    dialog.show();
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            add_topFive(currentMovie.getJsonObject(),false, currentMovie);
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            checkBox.setChecked(true);
                        }
                    });

                }
            }

        });

        return view;
    }
    public void add_topFive(JSONObject jsonObject, final Boolean b, final Top_model currentMovie) {
            jsonObject.remove("topCategory");
        try {
            jsonObject.put("topCategory",b);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        urldata = ip_cat + "/category/secure/update?wholesaler="+wholseller_id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urldata,jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    if(b==true){
                        check_list.add(b);
                        synchronized(check_list){
                            check_list.notify();
                        }
                    }
                    if(b==false) {
                        check_list.remove(true);
                        synchronized(check_list){
                            check_list.notify();
                        }
                    }
                    currentMovie.setaBoolean(b);
                    Snackbar.make(view, "Top Category  Updated Successfully", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();

                }

                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                Log.e("ERERERER", String.valueOf(response.statusCode));

                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 404:
                            BottomSheet.Builder builder = new BottomSheet.Builder(_context);
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;

                    }
                }

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

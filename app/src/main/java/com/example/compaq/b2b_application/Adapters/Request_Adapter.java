package com.example.compaq.b2b_application.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Model.Request_model;
import com.example.compaq.b2b_application.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;

public class Request_Adapter extends RecyclerView.Adapter<Request_Adapter.MyViewHolder> {
   public Context context;
    ArrayList<Request_model> productlist;
    public SharedPreferences sharedPref;
    private   String output;
    private Boolean mIsSpinnerFirstCall = true;
    private int pos=-1;
    Dialog dialog;
    ImageView canel;
    ArrayList<String> spin_calls;
     String item;

    public Request_Adapter(Context context, ArrayList<Request_model> productlist) {
        this.context=context;
        this.productlist=productlist;
        sharedPref=context.getSharedPreferences("USER_DETAILS",0);

        output=sharedPref.getString(ACCESS_TOKEN, null);

        dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.settings_layout);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_layout, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final Request_Adapter.MyViewHolder holder, int position) {
        final com.example.compaq.b2b_application.Model.Request_model listner=  productlist.get(position);
        holder.companyname.setText(listner.getCompanyname());
          final String logo_url=listner.getLogo();

        ArrayList<String> country=new ArrayList<>();
        country.add("ACCEPTED");
        country.add("PENDING");
        country.add("REJECTED");
        country.add("BLOCKED");
        final ArrayAdapter<String> country_adaper = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, country) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return true;
                } else {
                    return true;
                }
            }


        };
        holder.spinner.setAdapter(country_adaper);
        int spinnerPosition = country_adaper.getPosition(listner.getStatus());

        holder.spinner.setSelection(spinnerPosition);



        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
              pos++;
                if(!mIsSpinnerFirstCall&&productlist.size()!=pos) {

                    return;
                }
                if(mIsSpinnerFirstCall) {
                    if (adapterView.getSelectedItem().toString().equalsIgnoreCase("ACCEPTED")) {
                        ((TextView) view).setTextColor(Color.GREEN);
                        holder.linearLayout.setVisibility(View.VISIBLE);
                    }
                    if (adapterView.getSelectedItem().toString().equalsIgnoreCase("REJECTED")) {
                        ((TextView) view).setTextColor(Color.RED);
                        holder.linearLayout.setVisibility(View.GONE);
                    }
                    if (adapterView.getSelectedItem().toString().equalsIgnoreCase("PENDING")) {
                        ((TextView) view).setTextColor(Color.BLUE);
                        holder.linearLayout.setVisibility(View.GONE);
                    }
                    if (adapterView.getSelectedItem().toString().equalsIgnoreCase("BLOCKED")) {
                        ((TextView) view).setTextColor(Color.RED);
                        holder.linearLayout.setVisibility(View.GONE);
                    }
                }
                if(pos>=productlist.size()) {
                  change_request(listner.getId(),adapterView.getSelectedItem().toString(),view);


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
   holder.linearLayout.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
               relation(listner.getSettings());





       }
   });
try {
    Glide.with(context).load(logo_url).into(holder.imageView);
}catch (Exception e){

}
    }

    @Override
    public int getItemCount() {
        return productlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView companyname;
        ImageView imageView;
        Spinner spinner;
        LinearLayout linearLayout;
        ImageView canel;
        public MyViewHolder(View itemView) {
            super(itemView);
            companyname=(TextView)itemView.findViewById(R.id.comp_name);
            imageView=(ImageView)itemView.findViewById(R.id.logo_image);
            spinner=(Spinner)itemView.findViewById(R.id.spinn);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.setings_layout);

        }
    }

    public void change_request( final String id, final String status,final View view) {
             String url=ip1+"/b2b/api/v1/relation/update/"+id+"?relationStatus="+status;
        StringRequest stringRequest = new StringRequest(Method.PUT,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            if(response.equalsIgnoreCase("success")) {
                                Snackbar.make(view, "Status  Updated Successfully", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //displaying the error in toast if occurrs


                NetworkResponse response = error.networkResponse;

                if(error != null ){
                    switch(response.statusCode){
                        case 404:


                            break;

                        case 401:



                    }
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        requestQueue.add(stringRequest);
    }

    public void relation( final String id)  {

        String url=ip1+"/b2b/api/v1/relation/relation/setting/"+id;
        Log.e("URL",url);
        StringRequest stringRequest = new StringRequest(Method.GET,

                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            spin_calls=new ArrayList<>();
                            spin_calls.add("A");
                            spin_calls.add("B");
                            spin_calls.add("C");
                             JSONObject jsonObject=new JSONObject(response);
                             Log.e("OBJDDV",jsonObject.toString());
                            dialog.show();
                            canel = (ImageView) dialog.findViewById(R.id.cancel);


                            canel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                            final EditText editText=(EditText)dialog.findViewById(R.id.disc);
                            final EditText weigt=(EditText)dialog.findViewById(R.id.weight);
                            final CheckBox rate=(CheckBox)dialog.findViewById(R.id.rate);
                            final CheckBox advance=(CheckBox)dialog.findViewById(R.id.advance);
                            final Button save=(Button) dialog.findViewById(R.id.save);

                            Spinner spinner=(Spinner) dialog.findViewById(R.id.spinn);
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, spin_calls) {
                                @Override
                                public boolean isEnabled(int position) {
                                    if (position == 0) {
                                        // Disable the first item from Spinner
                                        // First item will be use for hint
                                        return true;
                                    } else {


                                        return true;
                                    }
                                }
                            };
                            spinner.setAdapter(spinnerArrayAdapter);
                            spinner.setSelection(  spinnerArrayAdapter.getPosition(jsonObject.getString("userClass")));
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                       item=adapterView.getSelectedItem().toString();

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                           try {
                               rate.setChecked(jsonObject.getBoolean("rateCut"));
                               advance.setChecked(jsonObject.getBoolean("advance"));
                           }
                           catch (Exception e){

                           }
                            editText.setText(jsonObject.getString("discountPercentage"));
                            weigt.setText(jsonObject.getString("minWeight"));

                            save.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    settings_update(id,weigt.getText().toString(),editText.getText().toString(),item,advance.isChecked(),rate.isChecked(),view);
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //displaying the error in toast if occurrs


                NetworkResponse response = error.networkResponse;

                if(error != null ){
                    switch(response.statusCode){
                        case 404:


                            break;

                        case 401:



                    }
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        requestQueue.add(stringRequest);
    }


    public  void settings_update(final String id, final String netweight,final String disc,final  String userClass,final Boolean advance,final Boolean rate,final  View view){
        JSONObject jsonObject = new JSONObject();
        try {

     jsonObject.put("id", id);
     jsonObject.put("userClass", userClass);
     jsonObject.put("advance", advance);
     jsonObject.put("block", null);
     jsonObject.put("rateCut", rate);
     jsonObject.put("minWeight", netweight);
     jsonObject.put("discountPercentage", disc);


 }
 catch (Exception e){

 }
        String url = ip1+"/b2b/api/v1/relation/setting/update";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    Snackbar.make(view, "Success ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();



                } catch (Exception e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "bearer "+output);
                params.put("Content-Type", "application/json");
                return params;
            }

        };

        requestQueue.add(objectRequest);

    }
}

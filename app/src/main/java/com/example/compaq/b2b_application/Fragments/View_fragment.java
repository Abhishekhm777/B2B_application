package com.example.compaq.b2b_application.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.MainActivity.ip1;
import static com.example.compaq.b2b_application.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.SessionManagement.PREF_NAME;
import static com.example.compaq.b2b_application.SessionManagement.pref;

/**
 * A simple {@link Fragment} subclass.
 */
public class View_fragment extends Fragment {
private TextView firstname,lastname,role,adhar,pan,companyname,state,city,country,line,zipcode,cin,gstin;
public SharedPreferences sharedPref;
String output;
Bundle bundle;
Button button;
    public View_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_view_fragment, container, false);

        sharedPref =getActivity().getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        output=sharedPref.getString(ACCESS_TOKEN, null);
        firstname=(TextView)view.findViewById(R.id.first_text);
        lastname=(TextView)view.findViewById(R.id.last_text);
        /*state=(TextView)view.findViewById(R.id.state);
        city=(TextView)view.findViewById(R.id.city);
        country=(TextView)view.findViewById(R.id.country);
        line=(TextView)view.findViewById(R.id.line);
        zipcode=(TextView)view.findViewById(R.id.zipcode);*/
        cin=(TextView)view.findViewById(R.id.cin);
        gstin=(TextView)view.findViewById(R.id.gstn);



       /* role=(TextView)view.findViewById(R.id.role_text);
        adhar=(TextView)view.findViewById(R.id.adhar_text);*/
        pan=(TextView)view.findViewById(R.id.pan_text);

        companyname=(TextView)view.findViewById(R.id.company_text);
        button=view.findViewById(R.id.close_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
bundle=this.getArguments();
String sellerid=bundle.getString("seller_id");
        sellerLogo(sellerid);
        return  view;
    }
    public void sellerLogo ( String sellerid){

        String url=ip1+"/b2b/api/v1/user/get/"+sellerid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);

                    firstname.setText(jObj.getString("firstName"));
                    lastname.setText(jObj.getString("lastName"));
                    /* email.setText(jObj.getString("email"));
                     mobileno.setText(jObj.getString("mobileNumber"));*/
                  /*  adhar.setText(jObj.getString("adharNo"));*/
                   /* role.setText(jObj.getString("role"));*/
                    pan.setText(jObj.getString("pan"));


                      JSONObject jsonObject=jObj.getJSONObject("company");
                   companyname.setText(jsonObject.getString("name"));
                        gstin.setText(jsonObject.getString("gstin"));
                        cin.setText(jsonObject.getString("cin"));





                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() {



                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                return params;
            }
        };




        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}

package com.example.compaq.b2b_application.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class Add_contact_to_book extends Fragment {
private View view;
private EditText customer_no,customer_name,customer_email,customer_gstin,customer_address,cust_company;
private  Button save;
private SharedPreferences sharedPref;
private  String output,user_id;

    public Add_contact_to_book() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(view==null) {
            view= inflater.inflate(R.layout.fragment_add_contact_to_book, container, false);
            sharedPref = getActivity().getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            output = sharedPref.getString(ACCESS_TOKEN, null);
            user_id = sharedPref.getString("userid", null);

            customer_no=view.findViewById(R.id.cust_no);
            customer_name=view.findViewById(R.id.cust_name);
            customer_email=view.findViewById(R.id.cust_email);
            customer_gstin=view.findViewById(R.id.gstin);
            customer_address=view.findViewById(R.id.address);
            cust_company=view.findViewById(R.id.cust_company);

            save=view.findViewById(R.id.save);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(customer_no.getText().toString()!=""&&customer_name.getText().toString()!="") {
                        updateCustomer_Details();
                    }
                    else {
                        Snackbar.make(view,"Customer Mobile No.& Name is Mandatory",Snackbar.LENGTH_SHORT).show();
                    }
                }
            });



        }
        return  view;
    }

    public void updateCustomer_Details() {

        JSONObject mainJasan= new JSONObject();


        try {

            mainJasan.put("name",customer_name.getText().toString());
            mainJasan.put("mobileNumber",customer_no.getText().toString());
            mainJasan.put("email",customer_email.getText().toString());
            mainJasan.put("firmName",cust_company.getText().toString());
            mainJasan.put("gstNumber",customer_gstin.getText().toString());
            mainJasan.put("address",customer_address.getText().toString());
            mainJasan.put("user",user_id);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = ip1+"/b2b/contact-book";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, mainJasan, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Snackbar.make(view,"Contact Addedd Successfully",Snackbar.LENGTH_SHORT).show();

                Customize_order_frag1 customize_order_frag1 = (Customize_order_frag1)  getActivity().getSupportFragmentManager().findFragmentByTag("customize");

                customize_order_frag1.getContacts();
                (getActivity()).getSupportFragmentManager().popBackStackImmediate();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Snackbar.make(view,"Something went wrong!!",Snackbar.LENGTH_SHORT).show();
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
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }
}

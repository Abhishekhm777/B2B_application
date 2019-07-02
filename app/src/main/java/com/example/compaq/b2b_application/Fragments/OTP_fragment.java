package com.example.compaq.b2b_application.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;

import static com.example.compaq.b2b_application.MainActivity.ip1;
import static com.example.compaq.b2b_application.SessionManagement.PREF_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class OTP_fragment extends Fragment {

    EditText editText;
    Button submit,resend;
    SharedPreferences sharedPref;
    SharedPreferences.Editor myEditior;

    public OTP_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_otp_fragment, container, false);

        sharedPref =getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        myEditior = sharedPref.edit();

        String url = sharedPref.getString("RESEND_OTP",null) ;
        Log.e("OOOOOOTTTTTPPP",url);
       /* editText=(EditText)view.findViewById(R.id.otp_edittext);
        submit=(Button)view.findViewById(R.id.submit_otp);
        resend=(Button)view.findViewById(R.id.resend_otp);*/


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpurl = ip1+"/api/v1/user/checkOTP/" + editText.getText().toString() +"?email=" + sharedPref.getString("PASSWORD_ID", null);
                Log.d("res...", otpurl);
                RequestQueue rqueue= Volley.newRequestQueue(getActivity());
                StringRequest stringRequest=new StringRequest(Request.Method.GET, otpurl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("true")){
                            Log.d("otp sucess..",response);
                            FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.forgpass_layout, new Password_reset());
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        else{
                            editText.setError("Incorrect OTP");

                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                rqueue.add(stringRequest);


            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");

                String url = sharedPref.getString("RESEND_OTP",null) ;
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("res2...", response.toString());


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

                requestQueue.add(stringRequest);


            }
        });

        return view;
    }

}
/*
public class GenericTextWatcher implements TextWatcher
{
    private View view;
    private GenericTextWatcher(View view)
    {
        this.view = view;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // TODO Auto-generated method stub
        String text = editable.toString();
        switch(view.getId())
        {

            case R.id.editText1:
                if(text.length()==1)
                    et2.requestFocus();
                break;
            case R.id.editText2:
                if(text.length()==1)
                    et3.requestFocus();
                else if(text.length()==0)
                    et1.requestFocus();
                break;
            case R.id.editText3:
                if(text.length()==1)
                    et4.requestFocus();
                else if(text.length()==0)
                    et2.requestFocus();
                break;
            case R.id.editText4:
                if(text.length()==0)
                    et3.requestFocus();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
    }
}*/

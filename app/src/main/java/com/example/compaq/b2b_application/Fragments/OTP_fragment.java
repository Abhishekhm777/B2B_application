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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.R;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class OTP_fragment extends Fragment {

    EditText editText, otpText1, otpText2, otpText3, otpText4, otpText5, otpText6;
    Button submit, resend;
    SharedPreferences sharedPref;
    SharedPreferences.Editor myEditior;
    String calling="";

    public OTP_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_otp_fragment, container, false);

        sharedPref = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        myEditior = sharedPref.edit();

        calling=sharedPref.getString("CLASS_TYPE",null);
        /*otpText1=(EditText)view.findViewById(R.id.otpET1) ;
        otpText2=(EditText)view.findViewById(R.id.otpET2) ;
        otpText3=(EditText)view.findViewById(R.id.otpET3) ;
        otpText4=(EditText)view.findViewById(R.id.otpET4) ;
        otpText5=(EditText)view.findViewById(R.id.otpET5) ;
        otpText6=(EditText)view.findViewById(R.id.otpET6) ;*/
        String url = sharedPref.getString("RESEND_OTP", null);
        submit = (Button) view.findViewById(R.id.submit_otp);
        resend = (Button) view.findViewById(R.id.resend_otp);
        Log.e("OOOOOOTTTTTPPP", url);
         editText=(EditText)view.findViewById((R.id.otp_edit));

        //text_add();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String otp = otpText1.getText().toString() + otpText2.getText().toString() + otpText3.getText().toString() + otpText4.getText().toString() + otpText5.getText().toString() + otpText6.getText().toString();
                String otp=editText.getText().toString();
                if (otp.length() == 6) {
                    String otpurl = ip1 + "/b2b/api/v1/user/checkOTP/" + otp + "?email=" + sharedPref.getString("PASSWORD_ID", null);
                    submit_otp(otpurl);
                }
                else {
                 Toast toast= Toast.makeText(getActivity(),"!Enter valid OTP",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                    toast.show();
                }

            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 editText.setText("");
                /*otpText1.setText("");
                otpText1.setText("");
                otpText1.setText("");
                otpText1.setText("");
                otpText1.setText("");*/


                String url = sharedPref.getString("RESEND_OTP", null);
                Log.d("urls..", url);
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("res2...", response.toString());
                                Toast toast= Toast.makeText(getActivity(),"OTP  resend sucessfully",Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                                toast.show();

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

    //////////////////////////////////////submit otp////////////////////////////////////////////////////
    public void submit_otp(String otpurl) {
        Log.d("res...", otpurl);
        RequestQueue rqueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, otpurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("otp sucess..", response);
                if (response.equals("true")) {
                    Log.d("otp sucess..", response);
                    if(calling.equals("FORGOT")) {
                        transaction();
                    }
                    else if(calling.equals("REGISTER")){
                        
                    }

                } else {
                    // editText.setError("Incorrect OTP");
                    Toast toast=Toast.makeText(getActivity(), "wrong OTP!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                    toast.show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        rqueue.add(stringRequest);

    }
    public  void transaction(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // fragmentTransaction.replace(R.id.forgpass_layout, new Password_reset());
        fragmentTransaction.replace(R.id.fpassword, new Password_reset());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}




/*//////////////////////////////////////////////text change listener///////////////////////////////////////////
    public void  text_add(){
        otpText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==1)
                {
                    otpText2.setEnabled(true);
                    otpText2.requestFocus();

                }
                else if(s.length()==0)
                {
                    otpText1.clearFocus();
                    otpText2.setEnabled(false);
                }
            }
        });


        otpText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==1)
                {
                    otpText3.setEnabled(true);
                    otpText3.requestFocus();
                }
                else if(s.length()==0)
                {

                    otpText1.requestFocus();
                    otpText3.setEnabled(false);
                }
            }
        });

        otpText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==1)
                {
                    otpText4.setEnabled(true);
                    otpText4.requestFocus();
                }
                else if(s.length()==0)
                {
                    otpText2.requestFocus();
                    otpText4.setEnabled(false);
                }
            }
        });

        otpText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==1)
                {
                    otpText5.setEnabled(true);
                    otpText5.requestFocus();
                }
                else if(s.length()==0)
                {
                    otpText3.requestFocus();
                    otpText5.setEnabled(false);
                }
            }
        });

        otpText5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==1)
                {
                    otpText6.setEnabled(true);
                    otpText6.requestFocus();
                }
                else if(s.length()==0)
                {
                    otpText4.requestFocus();
                    otpText6.setEnabled(false);
                }
            }
        });

        otpText6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==1)
                {
                    otpText6.clearFocus();
                }
                else if(s.length()==0)
                {
                    otpText5.requestFocus();
                    otpText6.setEnabled(false);
                }
            }
        });
    }*/




   /* public class GenericTextWatcher implements TextWatcher  {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch (view.getId()) {

                case R.id.otpET1:
                    if (text.length() == 1)
                        otpText1.requestFocus();
                    break;
                case R.id.otpET2:
                    if (text.length() == 1)
                        et3.requestFocus();
                    else if (text.length() == 0)
                        et1.requestFocus();
                    break;
                case R.id.otpET3:
                    if (text.length() == 1)
                        et4.requestFocus();
                    else if (text.length() == 0)
                        et2.requestFocus();
                    break;
                case R.id.otpET4:
                    if (text.length() == 0)
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


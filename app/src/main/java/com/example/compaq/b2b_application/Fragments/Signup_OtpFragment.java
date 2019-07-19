package com.example.compaq.b2b_application.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Activity.LoginActivity;
import com.example.compaq.b2b_application.Model.SignupModel;
import com.example.compaq.b2b_application.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;


public class Signup_OtpFragment extends Fragment {
    EditText otpText, otpText1, otpText2, otpText3, otpText4, otpText5, otpText6;
    Button submit;
    TextView resend;
    SharedPreferences sharedPref;
    SharedPreferences.Editor myEditior;
    String calling="",role="",product="",logo="";
    android.support.v7.widget.Toolbar toolbar;
    SignupModel signupModel;
    ArrayList<SignupModel>signuplist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_signup__otp, container, false);

        sharedPref = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        myEditior = sharedPref.edit();
        Bundle bundle=this.getArguments();
        role=sharedPref.getString("ROLE","");
        product=sharedPref.getString("CATALOGUE","");
        otpText=(EditText)view.findViewById(R.id.otp_register);
        submit=(Button)view.findViewById(R.id.submit_res_otp);
        resend=(TextView)view.findViewById(R.id.text_resend);
        signuplist=new ArrayList<>();
        signuplist.addAll((ArrayList<SignupModel>) bundle.getSerializable("Data"));
        signupModel= (SignupModel) signuplist.get(0);

        logo="35f05e0e-56fb-4676-9819-381da000696b";

        /*toolbar = (android.support.v7.widget.Toolbar)view.findViewById(R.id.fpassword2_toolbar);
        toolbar.setNavigationIcon(R.drawable.left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    (getActivity()).getSupportFragmentManager().popBackStack();
                }

            }
        });*/

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp=otpText.getText().toString();
                if (otpText.getText().toString().length() == 6) {
                    String otpurl = ip1 + "/b2b/api/v1/user/checkOTP/" + otp + "?email=" + sharedPref.getString("PASSWORD_ID", null);
                    Log.d("res...", otpurl);
                    RequestQueue rqueue = Volley.newRequestQueue(getActivity());
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, otpurl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("true")) {
                                Log.d("otp " + "sucess..", response);
                                /*FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.register_layout, new Reset_password());
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();*/
                                submitData();
                            } else {
                                otpText.setError("Incorrect OTP");
                                otpText.setText("");

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    rqueue.add(stringRequest);


                } else {
                    Toast.makeText(getActivity(), "Invalid OTP!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = sharedPref.getString("RESEND_OTP",null) ;
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getActivity(),"OTP is resend sucessfully..", Toast.LENGTH_SHORT).show();


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

    public void submitData(){
        final JSONObject postparams = new JSONObject();
        final JSONObject comoanyParams = new JSONObject();
        //ArrayList<JSONObject> regiter_infolist=new ArrayList<>();
        try {
            comoanyParams.put("name",signupModel.getCompany());
            comoanyParams.put("product",product);
            comoanyParams.put("gstin",signupModel.getGst());
            comoanyParams.put("logoImageId",signupModel.getLogoId());
            postparams.put("company",comoanyParams);
            postparams.put("role","ROLE_"+role);
            postparams.put("gstin",signupModel.getGst());
            postparams.put("firstName",signupModel.getPerson());
            postparams.put("email",signupModel.getEmail());
            postparams.put("mobileNumber","+91"+signupModel.getMobile());
            postparams.put("password", signupModel.getPassword());

            //regiter_infolist.add(postparams);
            Log.d("json obj",postparams.toString());
            postrequest(postparams);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /////////////////////////////////////////////////send request//////////////////////////////////////////
    public void postrequest (final JSONObject jsonObject)
    {
        String url=ip1+"/b2b/api/v1/user/save";

//    Log.d("response Params",postparamslist.get(0).toString());

        StringRequest req = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responsess",response.toString());

                        Toast.makeText(getActivity(),"Sucessfully Register.",Toast.LENGTH_LONG);
                        Intent intent =new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("response123",error.toString());

                    }
                })
        {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return jsonObject.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

        };
        // Add the request to the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(req);

        req.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 25000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 25000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                Log.e("volly error",error.getMessage().toString());
            }
        });
    }





}

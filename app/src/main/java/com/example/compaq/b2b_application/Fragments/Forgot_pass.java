package com.example.compaq.b2b_application.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class Forgot_pass extends Fragment  {
    public Toolbar toolbar;
    public Spinner spinner;
    public TextView getSelect;
    public EditText editText;
    public Button button;
    public String url,url2;
    SharedPreferences sharedPref;
    SharedPreferences.Editor myEditior;
    int setposition;
    public Forgot_pass() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_pass, container, false);

        sharedPref =getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        myEditior = sharedPref.edit();


        toolbar = (Toolbar) view.findViewById(R.id.forgot_toolbar);
        getSelect = (TextView) view.findViewById(R.id.setoption);
        editText = (EditText)view.findViewById(R.id.type);
        button = (Button) view.findViewById(R.id.submit);
        https://server.mrkzevar.com/uaa/b2b/api/v1/user/userExist?emailOrMobile=abhishekhm777@gmail.com
         button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (setposition == 1 && !editText.getText().toString().equals(null)) {
            url = ip1+"/b2b/api/v1/user/userExist?emailOrMobile=%2B91"+editText.getText().toString();

        } else if (setposition == 2 && !editText.getText().toString().equals(null)) {
            url = ip1+"/b2b/api/v1/user/userExist?emailOrMobile="+editText.getText().toString();

        }
        if (!editText.getText().toString().equals(null)) {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            final ProgressDialog progressDialog=new ProgressDialog(getContext());
            progressDialog.setTitle("Loading....");
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (response.equals("true")) {

                                sendOTP(setposition,editText.getText().toString());


                            } else if (setposition == 2 &&response.equals("false") ) {
                                editText.setError("Email is not Registered");
                            } else if (setposition == 1&&response.equals("false")) {
                                editText.setError("Mobile No.is not registered");
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            requestQueue.add(stringRequest);
        }
        else {
            editText.setError("field can't be empty");

        }
    }
});

        ArrayList<String>  list = new ArrayList<>();
        list.add("Select");
        list.add("Mobile");
        list.add("Email");
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, list) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }


        };
        spinner = (Spinner) view.findViewById(R.id.fspinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2) {
                    setposition=position;
                    getSelect.setVisibility(TextView.VISIBLE);
                    getSelect.setText("Email");
                    editText.setText("");
                    editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    editText.setVisibility(EditText.VISIBLE);


                } else if (position == 1) {
                    setposition=position;
                    getSelect.setText("Mobile");
                    getSelect.setVisibility(TextView.VISIBLE);
                    editText.setText("");
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    editText.setVisibility(EditText.VISIBLE);

                } else {
                    setposition=0;
                    editText.setText("");
                    getSelect.setVisibility(TextView.GONE);
                    editText.setVisibility(EditText.GONE);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }
    public  void sendOTP(int position,String userid){
        if(position==2) {
            url2 = ip1+"/b2b/api/v1/user/userExist?emailOrMobile="+userid;
            myEditior.putString("RESEND_OTP",url2);
            Log.d("mob", url2.toString());
        }
        else{
            url2=ip1+"/b2b/api/v1/user/forgotPassword/mobile?mobile=%2B91"+userid;
            Log.d("mob", url2.toString());
            myEditior.putString("RESEND_OTP",url2);


        }
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("res3", response.toString());
                        if (!response.equals(null)) {
                            myEditior.putString("PASSWORD_ID",response);
                            myEditior.commit();
                            myEditior.apply();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.forgpass_layout, new OTP_fragment());
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(stringRequest);





    }
}

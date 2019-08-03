package com.example.compaq.b2b_application.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Activity.LoginActivity;
import com.example.compaq.b2b_application.Activity.UserProfileActivity;
import com.example.compaq.b2b_application.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class Password_reset extends Fragment {
    EditText password, confirmpassword;
    Button button;
    SharedPreferences sharedPref;
    SharedPreferences.Editor myEditior;
    android.support.v7.widget.Toolbar toolbar;
    Bundle bundle;

    public Password_reset() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_password_reset, container, false);
         bundle=this.getArguments();

        sharedPref = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        myEditior = sharedPref.edit();

        password = (EditText) view.findViewById(R.id.new_password);
        confirmpassword = (EditText) view.findViewById(R.id.confirm_password);


        button = (Button) view.findViewById(R.id.submit_password);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!password.getText().toString().equals(null) && !confirmpassword.getText().toString().equals(null) && password.getText().toString().equals(confirmpassword.getText().toString())) {
                    if (password.getText().toString().equals(confirmpassword.getText().toString())) {
                        int pword = password.getText().length();
                        if (password.getText().toString().length() < 6 && password.getText().toString().replaceAll("[0-9]", "").length() == pword ) {
                            password.setError("Invalid password. Password must be at least 6 characters long, and contain a number.");
                        } else {
                            String url = ip1 + "/b2b/api/v1/user/resetPassword";
                            Log.d("reset_pass", url);

                            try {
                                JSONObject jsonObject=new JSONObject();
                                jsonObject.put("email",sharedPref.getString("PASSWORD_ID", null));
                                jsonObject.put("password",password.getText().toString());
                                submit_password(url,jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    } else {
                        button.setError("password not matching");
                    }
                } else if (!password.getText().toString().equals(null) && !confirmpassword.getText().toString().equals(null) && !password.getText().toString().equals(confirmpassword.getText().toString())) {
                   Toast toast= Toast.makeText(getContext(), "password miss matched ", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                    toast.show();

                } else if (password.getText().toString().equals(null) && !confirmpassword.getText().toString().equals(null)) {
                    password.setError("This Field is Required");
                } else if (!password.getText().toString().equals(null) && confirmpassword.getText().toString().equals(null)) {
                    confirmpassword.setError("This Field is Required");
                } else {
                    password.setError("This Field is Required");
                    confirmpassword.setError("This Field is Required");
                }


            }
        });


        return view;

    }

    /////////////////////////////////submit password////////////////////////////////////////////
    private  void  submit_password(String url,JSONObject jsonObject){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.length() !=0) {
                            Toast.makeText(getContext(), "Password reset successfully",
                                    Toast.LENGTH_SHORT).show();

                                returnbackLogin();




                        } else {
                           Toast toast= Toast.makeText(getContext(), "Please check password or reenter it",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                            toast.show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    public void returnbackLogin(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.putExtra("val", 2);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public  void returnProfile(){
        Fragment userProfileFragment=new UserProfileFragment();
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.profile_activity_layout, userProfileFragment).commit();
        UserProfileActivity.titleView.setText("UPDATE PASSWORD");
    }
}



package com.example.compaq.b2b_application.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Activity.LoginActivity;
import com.example.compaq.b2b_application.Helper_classess.AlertDialogManager;
import com.example.compaq.b2b_application.Helper_classess.SessionManagement;
import com.example.compaq.b2b_application.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;


public class ChangePasswordFragment extends Fragment {
    private static String pass = "", email = "";
    Button button;
    EditText old_text;
    public SharedPreferences sharedPref;
    public SharedPreferences.Editor myEditor;
    AlertDialogManager alert = new AlertDialogManager();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        sharedPref = getActivity().getSharedPreferences("USER_DETAILS", 0);
        myEditor = sharedPref.edit();

        button = (Button) view.findViewById(R.id.password_btn);
        old_text = (EditText) view.findViewById(R.id.user_oldPassword);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass = old_text.getText().toString();
                email = sharedPref.getString("PASSWORD_ID", "");

                getToken();


            }
        });

        return view;
    }

    public void getToken() {

        String url = ip1 + "/b2b/oauth/token";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObj = new JSONObject(response);

                    String accesstoken = jsonObj.getString("access_token");
                    Log.d("access", response.toString());
                    transaction();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                NetworkResponse response = volleyError.networkResponse;


                if (response != null && response.data != null) {
                    switch (response.statusCode) {

                            case 404:

                                Snackbar.make(getActivity().getCurrentFocus(), "Sorry! Could't process.Try agian after sometime", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                break;
                            case 400:


                                alert.showAlertDialog(getActivity(), "Login failed..", "Password is incorrect.", "fail");
                                break;
                            case 401:

                                Toast.makeText(getActivity(),"Session Expired!",Toast.LENGTH_SHORT).show();


                        }


                }


            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", email);
                params.put("grant_type", "password");
                params.put("password", pass);
                params.put("client_id", "ordofy-android");
                params.put("client_secret", "ordofy593a-android");
                return params;
            }

        };

        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(stringRequest);
    }

    public  void transaction(){
        Bundle bundle=new Bundle();
        bundle.putString("CALL","ChangePasswordFragment");
        Fragment fragment=new Password_reset();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // fragmentTransaction.replace(R.id.forgpass_layout, new Password_reset());
        fragmentTransaction.replace(R.id.profile_activity_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}

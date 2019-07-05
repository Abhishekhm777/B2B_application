package com.example.compaq.b2b_application.Helper_classess;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;

public class Reffressh_token {
    SharedPreferences.Editor editor;
    Context context;
SharedPreferences sharedPref;
    private String accesstoken;



    public void getToken(final Context context ){
        Log.e("REFRESH ","New Class");
        sharedPref =context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        String url=ip1+"/b2b/oauth/token";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){

            @Override
            public void onResponse(String response){

                try {
                    JSONObject jsonObj = new JSONObject(response);

                    accesstoken =jsonObj.getString("access_token");
                    editor.putString(ACCESS_TOKEN,accesstoken);
                    Log.e("REFRESH ",accesstoken);
                    Toast.makeText(context, "Logged in",
                            Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("REFRESH ","ERROR");
            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
               String ref= sharedPref.getString("REFRESH_TOKEN",null);
               Log.e("refereefrefef",ref);
                params.put("grant_type", "refresh_token");
                params.put("refresh_token", sharedPref.getString("REFRESH_TOKEN",null));

                return params;
            }

        };

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(stringRequest);


    }
}

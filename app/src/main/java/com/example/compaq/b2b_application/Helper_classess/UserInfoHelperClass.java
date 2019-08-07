package com.example.compaq.b2b_application.Helper_classess;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Activity.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;

public class UserInfoHelperClass {
private Context context;
private String token;
private String user_id;
private SharedPreferences sharedPref;
private SharedPreferences.Editor myEditior;
private String bag_items;
private SessionManagement session;
public UserInfoHelperClass(Context context,String token,String user_id){
     this.context=context;
     this.token=token;
     this.user_id=user_id;

    sharedPref =context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    myEditior = sharedPref.edit();
    session = new SessionManagement(context);
 }

 public void cartInfomation( ){
        String url=ip+"gate/b2b/order/api/v1/cart/customer/"+user_id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray jsonArray=jObj.getJSONArray("items");
                    bag_items= String.valueOf(jsonArray.length());

                  String  cartid=jObj.getString("id");
                    Log.e("Cart Id from Jason",bag_items);

                    myEditior.putString("no_of_items", bag_items);

                    myEditior.putString("cartid",cartid);
                    myEditior.commit();
                    myEditior.apply();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 404:
                            myEditior.putString("cartid","0");
                            myEditior.commit();
                            myEditior.apply();
                            break;
                        case 401:
                            session.logoutUser(context);

                            break;
                    }
                }


            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+token);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}

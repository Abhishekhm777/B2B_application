package com.example.compaq.b2b_application.Helper_classess;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;

public class FirebaseIDService extends FirebaseInstanceIdService {
Context context;
    private static final String TAG = "FirebaseIDService";
    public SharedPreferences sharedPref;
    public void onTokenRefresh(Context context) {
        this.context=context;
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Refreshed token: " + refreshedToken);
        sharedPref=context.getSharedPreferences("USER_DETAILS",0);
        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {

            JSONObject mainJasan= new JSONObject();

                try {
                    mainJasan.put("deviceToken",token);
                    mainJasan.put("email",sharedPref.getString("email", null));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String url="http://10.0.0.3:8766/uaa/b2b/pushnotification/saveAndroidDeviceToken";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, mainJasan, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    try {
                        Log.e(TAG, "response : " + response.toString());
                    }

                    // Go to next activity

                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error", String.valueOf(error));

                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headr = new HashMap<>();
                    String output=sharedPref.getString(ACCESS_TOKEN, null);
                    headr.put("Authorization","bearer "+output);
                    headr.put("Content-Type", "application/json");
                    return headr;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
            queue.add(request);
        }
    }



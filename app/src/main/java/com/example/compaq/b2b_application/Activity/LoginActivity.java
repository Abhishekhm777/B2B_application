package com.example.compaq.b2b_application.Activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Helper_classess.AlertDialogManager;
import com.example.compaq.b2b_application.Helper_classess.ConnectivityStatus;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.Helper_classess.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;


public class LoginActivity extends AppCompatActivity {
    // Email, password edittext
    EditText txtUsername, txtPassword;
    private FragmentTransaction fragmentTransaction;
    public FragmentManager fragmentManager;
    // login button
    Button btnLogin,btn_Skip,btn_signup;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    // Session Manager Class
    SessionManagement session;
    MainActivity activity;
    public  String  accesstoken="";
    public String username="";
    public String password="";
    public String userid="";
    public String userfirstname="";
    public String userlastname="";
    public String email="";
    public String mobileNo="";
       public Dialog dialog;
    public int json_length;
    public String cartid="";
    private String role,product;
    private String refreshtoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //check internet status for the
        getApplicationContext().registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout. progress_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        // Session Manager
        session = new SessionManagement(getApplicationContext());
        session.checkLogin(LoginActivity.this);
        // Email, Password input text
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

       /* Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
*/

        // Login button
        btnLogin = (Button) findViewById(R.id.btnLogin);

   /*     btn_signup = (Button) findViewById(R.id.signUp);*/

        TextView signuptext=(TextView)findViewById(R.id.signuptext) ;
        TextView forgot_pass=(TextView)findViewById(R.id.forgot_textview);
        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Forgot_pass forgotPass=new Forgot_pass();

                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.login_activitylayout, forgotPass);

                fragmentTransaction.addToBackStack(null).commit();*/

                Intent i = new Intent(getApplicationContext(), Forgot_password.class);
                startActivity(i);


            }
        });



        signuptext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Sign_up_Activity.class);
                startActivity(i);

            }
        });
        /*btn_Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });*/

        // Login button click event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get username, password from EditText
                String user_enterd = txtUsername.getText().toString();
                 password = txtPassword.getText().toString();

                if(user_enterd.contains("@")){
                    username=user_enterd;

                }else {
                    username="+91"+user_enterd;

                }


                // Check if username, password is filled
                if(username.trim().length() > 0 && password.trim().length() > 0){
                    // For testing puspose username, password is checked with sample data
                    // username = test
                    // password = test

                  /*  new SendPostRequest().execute();*/


                    getToken();
                  /*  if(accesstoken!="invalid_grant"){

                        // Creating user login session
                        // For testing i am stroing name, email as follow
                        // Use user real data
                        session.createLoginSession("username", "password",accesstoken);

                        // Staring MainActivity


                       Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();

                    }else{
                        // username / password doesn't match
                        alert.showAlertDialog(LoginActivity.this, "Login failed..", "Username/Password is incorrect.....", false);
                    }*/
                }else{
                    // user didn't entered username or password
                    // Show alert asking him to enter the details
                    alert.showAlertDialog(LoginActivity.this, "Login failed..", "Please enter username and password...", "fail");
                }

            }
        });

    }


   /* public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL(" https://server.mrkzevar.com/uaa/b2b/oauth/token"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("username", username);
                postDataParams.put("grant_type", "password");
                postDataParams.put("password", password);
                postDataParams.put("client_id", "acme");
                postDataParams.put("client_secret", "acmesecret");
                Log.e("params", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5000 *//* milliseconds *//*);
                conn.setConnectTimeout(5000 *//* milliseconds *//*);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }
                 sb.toString();
                    JSONObject jsonObject=new JSONObject(sb.toString());
                    accesstoken=jsonObject.getString("access_token");
                    in.close();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    getApplicationContext().startActivity(i);
                    LoginActivity.this.finish();


                    return sb.toString();
                } else {
                    alert.showAlertDialog(LoginActivity.this, "Login failed..", "Please enter username and password...", false);
                    return new String("false : " + responseCode);

                }
            } catch (Exception e) {

                return "wrong";
            }

        }

        @Override
        protected  void onPostExecute(String result) {

if(result.equalsIgnoreCase("wrong"))
{
    alert.showAlertDialog(LoginActivity.this, "Login failed..", "Username/Password is incorrect.....", false);
}
else {
    session.createLoginSession(username, password,accesstoken);
    Toast.makeText(getApplicationContext(), "Logged in",
            Toast.LENGTH_LONG).show();
}
}
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        Log.d("JSON OBJECT",result.toString());
        return result.toString();
    }*/


    public void getToken( ){
        dialog.show();
        String url=ip1+"/b2b/oauth/token";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){

            @Override
            public void onResponse(String response){

                try {
                    JSONObject   jsonObj = new JSONObject(response);

                    accesstoken =jsonObj.getString("access_token");
                    refreshtoken=jsonObj.getString("refresh_token");
                    userInformation();

                   /* session.createLoginSession(username, password,accesstoken);

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(i);
                  LoginActivity.this.finish();*/



                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Logged in",
                            Toast.LENGTH_LONG).show();

                    /*String userlastname=jsonObj.getString("lastName");
                    pref.edit().putString(USER_LASTNAME,userlastname).apply();

                    String user_email =jsonObj.getString("email");
                    pref.edit().putString(USER_EMAIL,user_email).apply();

                    String usermobile=jsonObj.getString("mobileNumber");
                    pref.edit().putString(USER_MOBILE,usermobile).apply();*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }

               /* try {
                    JSONArray address=response.getJSONArray("address";
                    if(address.length()!=0) {
                        addresslist=new ArrayList<>();
                        //address_recycler.setVisibility(View.VISIBLE);
                        String default_id = response.getString("defaultAddressID";
                        if(default_id!=null)
                        {
                            myEditior.putString("default_id",default_id).apply();
                        }
                        else{
                            myEditior.putString("default_id","no".apply();
                        }
                        Log.d("default",default_id.toString());
                        String mob=response.getString("mobileNumber";
                        String name = response.getString("firstName" + " " + response.getString("lastName";
                        for(int i=0;i<address.length();i++){
                            JSONObject jsonObject=address.getJSONObject;
                            String zipcode=jsonObject.getString("zipcode";
                            String address_id=jsonObject.getString("id";
                            String street=jsonObject.getString("line";
                            String city=jsonObject.getString("city";
                            String state=jsonObject.getString("state";
                            String country=jsonObject.getString("country";
                            String total_address=street+","+city+","+state+","+country+" "+zipcode;
                            Address_Model address_model=new Address_Model(address_id,name,total_address,mob);
                            addresslist.add(address_model);
                        }
                        if(address.length()==addresslist.size()) {
                            addressAdapter = new AddressAdapter(getContext(),addresslist);
                            address_recycler.setAdapter(addressAdapter);
                            address_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
                            Log.d("........","done";
                        }

                    }
                    else{
                        //address_recycler.setVisibility(View.GONE);
                    }




                }

                catch (JSONException e){

                    e.printStackTrace();
                }
*/
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                NetworkResponse response = volleyError.networkResponse;


                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 404:
                            dialog.dismiss();
                            Snackbar.make(getCurrentFocus(), "Sorry! Could't login.Try agian after sometime", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            break;
                        case 400:
                            dialog.dismiss();

                             alert.showAlertDialog(LoginActivity.this, "Login failed..", "Username/Password is incorrect.....", "fail");
                            break;
                        case 401:
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Session Expired!",Toast.LENGTH_SHORT).show();
                            session.logoutUser(getApplicationContext());

                    }
                }


            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("grant_type", "password");
                params.put("password", password);
                params.put("client_id", "ordofy-android");
                params.put("client_secret", "ordofy593a-android");
                return params;
            }

        };

        RequestQueue rQueue = Volley.newRequestQueue(getApplicationContext());
        rQueue.add(stringRequest);


    }





    public void userInformation ( ){

        String url=ip1+"/b2b/api/v1/user/info";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);

                     userid=jObj.getString("id");
                    Log.e("USER    id",userid);

                     userlastname=jObj.getString("lastName");
                      userfirstname=jObj.getString("firstName");
                    email=jObj.getString("email");
                    mobileNo=jObj.getString("mobileNumber");
                    role=jObj.getString("role");
                    Log.e("Role",role);

                    Log.e("USER    name",userfirstname);

                    cartInformation(userid);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() {


                Log.d("ACESSSSSS>>>>>>>>TOKEN",accesstoken);
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+accesstoken);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };




        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }



    public void cartInformation (String id ){

        String url=ip+"gate/b2b/order/api/v1/cart/customer/"+id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray jsonArray=jObj.getJSONArray("items");
                    json_length= jsonArray.length();
                    Log.e("Length....", String.valueOf(json_length));


                    /*editor.putString("no_of_items", String.valueOf(json_length)).apply();
                    editor.commit();
*/



                     cartid=jObj.getString("id");
                    Log.e("Cart Id from Jason",cartid);
                   /* editor.putString("cartid",cartid).apply();
                    editor.commit();
                    editor.apply();
*/
                    /*sharedPref=getSharedPreferences("User_information",0);*/

                   /* String cart =pref.getString("cartid","");*/
                   /* Log.e("Cart Id from SHARED",cart);*/




                    session.createLoginSession(username, password,accesstoken,userid,userfirstname,String.valueOf(json_length),cartid,userlastname,email,mobileNo,role,refreshtoken);

                    if(role.equalsIgnoreCase("ROLE_WHOLESALER")){
                        Intent i = new Intent(getApplicationContext(), Seller_Dashboard_Activity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(i);
                        LoginActivity.this.finish();

                    }
                    if(role.equalsIgnoreCase("ROLE_RETAILER")) {
                        Intent i = new Intent(getApplicationContext(), All_Sellers_Display_Activity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(i);
                        LoginActivity.this.finish();
                    }
                    if(role.equalsIgnoreCase("ROLE_MANUFACTURER")) {
                        Intent i = new Intent(getApplicationContext(), Seller_Dashboard_Activity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(i);
                        LoginActivity.this.finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               /* editor.putString("cartid","0").apply();
                editor.commit();
                editor.apply();*/

                session.createLoginSession(username, password,accesstoken,userid,userfirstname,String.valueOf(json_length),"0",userlastname,email,mobileNo,role,refreshtoken);

                if(role.equalsIgnoreCase("ROLE_WHOLESALER")){
                    Intent i = new Intent(getApplicationContext(), Seller_Dashboard_Activity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(i);
                    LoginActivity.this.finish();

                }
                if(role.equalsIgnoreCase("ROLE_RETAILER")) {
                    Intent i = new Intent(getApplicationContext(), All_Sellers_Display_Activity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(i);
                    LoginActivity.this.finish();
                }
                if(role.equalsIgnoreCase("ROLE_MANUFACTURER")) {
                    Intent i = new Intent(getApplicationContext(), Seller_Dashboard_Activity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(i);
                    LoginActivity.this.finish();
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() {



                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                params.put("Authorization","bearer "+accesstoken);
                return params;
            }
        };




        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }

////////////////////////Internate Status Checking//////////////////////////////////
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(!ConnectivityStatus.isConnected(getApplicationContext())){

                alert.showAlertDialog(LoginActivity.this, "No Internet!", "Please Check your internet Connection", "internet");
            }else {
                // connected

            }
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        getApplicationContext().registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    @Override
    protected void onPause() {
        super.onPause();
        getApplicationContext().unregisterReceiver(receiver);

    }


}

package com.example.compaq.b2b_application.Helper_classess;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.compaq.b2b_application.Activity.Customize_Order;
import com.example.compaq.b2b_application.Activity.LoginActivity;
import com.example.compaq.b2b_application.Activity.MainActivity;
import com.example.compaq.b2b_application.Activity.All_Sellers_Display_Activity;
import com.example.compaq.b2b_application.Activity.Offline_order;
import com.example.compaq.b2b_application.Activity.Seller_Dashboard_Activity;

import java.util.HashMap;


public class SessionManagement  {
    // Shared Preferences

    public int  json_length=0;
    public static  SharedPreferences pref;


    // Editor for Shared preferences
  public static   SharedPreferences.Editor editor;
    MainActivity activity;
    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    public static final String PREF_NAME = "USER_DETAILS";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_PASS = "pass";

    public static final String USERNAME_FIRSTNAME = "userfirstname";
    public static final String ACCESS_TOKEN = "access";
    public static final String LKEY_NAME = "lname";
    public static final String LKEY_PASS = "lpass";

String role;

    /*ublic static final String USERID = "id";
    public static final String USER_MOBILE = "usermobile";
    public static final String USER_FIRSTNAME = "userfirstname";
    public static final String USER_LASTNAME = "userlastname";
    public static final String USER_EMAIL = "user_email";*/
    // Constructor
    public SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void creatSignupSession(String name, String pass){
        // Storing login value as TRUE
        Log.d("Sesion....",name);
        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_PASS, pass);

        // commit changes
        editor.commit();
    }

    public void createLoginSession(String lname, String lpass,String access,String userid,String userfirstname,String length,String cartid,String lasname,String email,String mobile,String role,String refresh){



        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(LKEY_NAME, lname);

        // Storing email in pref
        editor.putString(LKEY_PASS, lpass);
        editor.putString(ACCESS_TOKEN,access);
        editor.putString("userid",userid);
        editor.putString("acc_token",access);
        editor.putString("firstname",userfirstname);
        editor.putString("role",role);
        editor.putString("no_of_items", length);
        editor.putString("REFRESH_TOKEN",refresh);
        editor.putString("lastname",lasname);
        editor.putString("email",email);
        editor.putString("mobile",mobile);
        editor.putString("cartid",cartid);


        // commit changes
        editor.commit();

    }




    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */

    public void checkLogin(Context context){
        // Check login status




        role=pref.getString("role", null);

        if(this.isLoggedIn()){
            // user is  logged in redirect him to main Activity
           ((LoginActivity)context).finish();
if(role.equalsIgnoreCase("ROLE_WHOLESALER")) {
    Intent i = new Intent(_context, Seller_Dashboard_Activity.class);
    // Closing all the Activities
    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

    // Add new Flag to start new Activity
    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    // Staring Login Activity
    _context.startActivity(i);
}

      if(role.equalsIgnoreCase("ROLE_RETAILER")) {
                Intent i = new Intent(_context, All_Sellers_Display_Activity.class);
                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // Staring Login Activity
                _context.startActivity(i);
            }

            if(role.equalsIgnoreCase("ROLE_MANUFACTURER")) {
                Intent i = new Intent(_context, Seller_Dashboard_Activity.class);
                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // Staring Login Activity
                _context.startActivity(i);
            }
        }
    }

    /**
     * Get stored session data
     * */

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        Log.d("userP...........",pref.getString(KEY_NAME,null));
        // user email id
        user.put(KEY_PASS, pref.getString(KEY_PASS, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(Context context) throws ClassCastException{
        // Clearing all data from Shared Preferences
       editor.clear();
        editor.commit();

        try {
            ((Customize_Order) context).finish();
        }
        catch (Exception e){

        }
        try {
            ((Offline_order) context).finish();
        }
        catch (Exception e){

        }

            try {
                ((MainActivity) context).finish();
            }
            catch (Exception e){

            }
            try{
                ((All_Sellers_Display_Activity) context).finish();


            }

            catch (Exception e){

            }
        try{
            ((Seller_Dashboard_Activity) context).finish();

        }

        catch (Exception e){

        }


        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);

    }



    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN,false);
    }


   /* public void getAddressDetails(final String aces){
        String url="https://server.mrkzevar.com/uaa/b2b/api/v1/user/info";
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){

            @Override
            public void onResponse(String response){

                try {
                    JSONObject   jsonObj = new JSONObject(response);

                    String userid=jsonObj.getString("id");

                    userfirstname=jsonObj.getString("firstName");
                    editor.putString(USERID,userfirstname);
                    Log.e("RETURNINGG",userfirstname);




                    *//*String userlastname=jsonObj.getString("lastName");
                    pref.edit().putString(USER_LASTNAME,userlastname).apply();

                    String user_email =jsonObj.getString("email");
                    pref.edit().putString(USER_EMAIL,user_email).apply();

                    String usermobile=jsonObj.getString("mobileNumber");
                    pref.edit().putString(USER_MOBILE,usermobile).apply();*//*


                    editor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

               *//* try {
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
*//*
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+aces);
                params.put("Content-Type", "application/json");
                return params;
            }

        };

        RequestQueue rQueue = Volley.newRequestQueue(_context);
        rQueue.add(stringRequest);





    }
*/



/*

    public void userInformation ( ){

        String url="http://192.168.100.15:8769/uaa/b2b/api/v1/user/info";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);

                    String userid=jObj.getString("id");
                    Log.e("USER    id",userid);

                    editor.putString("userid",userid).apply();

                    String  userfirstname=jObj.getString("firstName");
                    Log.e("USER    name",userfirstname);
                    editor.putString("firstname",userfirstname);
                    editor.putString(USERNAME_FIRSTNAME,userfirstname);
                    editor.commit();
                    editor.apply();
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

                pref=_context.getSharedPreferences("USER_DETAILS",0);

                String output=pref.getString(ACCESS_TOKEN, null);
                Log.d("ACESSSSSS>>>>>>>>TOKEN",output);
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };




        RequestQueue requestQueue = Volley.newRequestQueue(_context);
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


                    editor.putString("no_of_items", String.valueOf(json_length)).apply();
                    editor.commit();




                    String cartid=jObj.getString("id");
                    Log.e("Cart Id from Jason",cartid);
                    editor.putString("cartid",cartid).apply();
                    editor.commit();
                    editor.apply();

                    */
/*sharedPref=getSharedPreferences("User_information",0);*//*


                    String cart =pref.getString("cartid","");
                    Log.e("Cart Id from SHARED",cart);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                editor.putString("cartid","0").apply();
                editor.commit();
                editor.apply();


            }
        }){
            @Override
            public Map<String, String> getHeaders() {

                pref=_context.getSharedPreferences("USER_DETAILS",0);

                String output=pref.getString(ACCESS_TOKEN, null);

                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                params.put("Authorization","bearer "+output);
                return params;
            }
        };




        RequestQueue requestQueue = Volley.newRequestQueue(_context);
        requestQueue.add(stringRequest);
    }


*/

}

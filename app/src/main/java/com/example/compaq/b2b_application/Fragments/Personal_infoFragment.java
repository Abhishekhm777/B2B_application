package com.example.compaq.b2b_application.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Activity.UserProfileActivity;
import com.example.compaq.b2b_application.R;
import com.rilixtech.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;


public class Personal_infoFragment extends Fragment {

    public EditText user_fname,user_lname,email,teli_phone;
    private SharedPreferences sharedPref;
    private String Acess_Token,adharNo="",pan="",role="",password="",adharDocumentId="",panDocumentId="";
    private JSONObject company_details,websiteSetting;
    private JSONArray meltingSealing,userClass;
    private int defaultAddressID=0,id=0;
     private CountryCodePicker ccp2;
    Button button;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_personal_info, container, false);
        user_fname=(EditText)view.findViewById(R.id.user_first_name);
        user_lname=(EditText)view.findViewById(R.id.user_last_name);
        email=(EditText)view.findViewById(R.id.edit_user_email);
        teli_phone=(EditText)view.findViewById(R.id.edit_user_mobile);
        button=(Button)view.findViewById(R.id.change_profile_btn);
        ccp2 = (CountryCodePicker)view.findViewById(R.id.ccp_2);

        sharedPref =getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Acess_Token=sharedPref.getString(ACCESS_TOKEN, null);
        setHasOptionsMenu(true);
        userInformation();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             updateUsers();
            }
        });

        return  view;
    }

    public void userInformation ( ){

        String url=ip1+"/b2b/api/v1/user/info";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    user_fname.setText(jObj.getString("firstName"));
                    user_lname.setText(jObj.getString("lastName"));
                    email.setText(jObj.getString("email"));
                    teli_phone.setText(jObj.getString("mobileNumber").replace("+91",""));
                    id=jObj.getInt("id");
                    adharNo=jObj.getString("adharNo");
                    pan=jObj.getString("pan");
                    role=jObj.getString("role");
                    password=jObj.getString("password");
                    adharDocumentId=jObj.getString("adharDocumentId");
                    panDocumentId=jObj.getString("panDocumentId");
                    defaultAddressID=jObj.getInt("defaultAddressID");
                    company_details=jObj.getJSONObject("company");
                    websiteSetting=jObj.getJSONObject("websiteSetting");
                    meltingSealing=jObj.getJSONArray("meltingSealing");
                    userClass=jObj.getJSONArray("userClass");
                    Log.d("company.....",company_details.toString());
                    Log.d("userClass.....",userClass.toString());


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



                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+Acess_Token);
                params.put("Content-Type", "application/json");
                return params;
            }
        };




        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

/////////////////////////////////////////////////////////////
public void updateUsers() {

    JSONObject mainJasan= new JSONObject();

    String   url=ip+"uaa/b2b/api/v1/user/update";

    JSONObject json1= new JSONObject();


    try {


        mainJasan.put("id",id);
        mainJasan.put("firstName",user_fname.getText().toString());
        mainJasan.put("lastName",user_lname.getText().toString());
        mainJasan.put("email",email.getText().toString());
        mainJasan.put("mobileNumber","+"+ccp2.getSelectedCountryCode()+teli_phone.getText().toString());
        mainJasan.put("adharNo",adharNo);
        mainJasan.put("pan",pan);
        mainJasan.put("defaultAddressID",defaultAddressID);
        mainJasan.put("role",role);
        mainJasan.put("password",password);
        mainJasan.put("adharDocumentId",adharDocumentId);
        mainJasan.put("panDocumentId",panDocumentId);
        mainJasan.put("meltingSealing",meltingSealing);
        mainJasan.put("userClass",userClass);
        mainJasan.put("websiteSetting",websiteSetting);
        mainJasan.put("company",company_details);
        Toast toast= Toast.makeText(getActivity(), "Profile Updated Sucessfully", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();





    } catch (JSONException e) {
        e.printStackTrace();
    }

    JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, mainJasan, new Response.Listener<JSONObject>() {

        @Override
        public void onResponse(JSONObject response) {

            try {
                Log.d("response...",response.toString());
                user_fname.setEnabled(false);
                user_fname.setClickable(false);
                user_fname.setFocusableInTouchMode(false);
                user_lname.setEnabled(false);
                user_lname.setClickable(false);
                user_lname.setFocusableInTouchMode(false);
                email.setEnabled(false);
                email.setClickable(false);
                email.setFocusableInTouchMode(false);
                teli_phone.setEnabled(false);
                teli_phone.setClickable(false);
                teli_phone.setFocusableInTouchMode(false);
                setHasOptionsMenu(true);


            }

            // Go to next activity

            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {

        }
    }) {
        @Override
        public Map<String, String> getHeaders() {
            HashMap<String, String> headr = new HashMap<>();

            headr.put("Authorization","bearer "+Acess_Token);
            headr.put("Content-Type", "application/json");
            return headr;
        }
    };
    RequestQueue queue = Volley.newRequestQueue(getContext());
    queue.add(request);
}






    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.edit_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("item...", item.getItemId() + "");
        switch (item.getItemId()) {

            case R.id.edit_icon:
               user_fname.setEnabled(true);
               user_fname.setClickable(true);
               user_fname.setFocusableInTouchMode(true);
                user_lname.setEnabled(true);
                user_lname.setClickable(true);
                user_lname.setFocusableInTouchMode(true);
                email.setEnabled(true);
                email.setClickable(true);
                email.setFocusableInTouchMode(true);
                teli_phone.setEnabled(true);
                teli_phone.setClickable(true);
                teli_phone.setFocusableInTouchMode(true);
                setHasOptionsMenu(false);

                return true;


        }
        return  false;
    }


}

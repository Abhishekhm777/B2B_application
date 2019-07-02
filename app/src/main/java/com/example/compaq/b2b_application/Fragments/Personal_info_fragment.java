package com.example.compaq.b2b_application.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.LoginActivity;
import com.example.compaq.b2b_application.Main2Activity;
import com.example.compaq.b2b_application.MainActivity;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.Slider_activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.MainActivity.ip;
import static com.example.compaq.b2b_application.MainActivity.ip1;
import static com.example.compaq.b2b_application.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.SessionManagement.PREF_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class Personal_info_fragment extends Fragment  {

public EditText firstname,lastname,email,mobile,adhar_no,pan_no,comapny_name,gstin_no,cin_no,description,fax;
public Button save,cancel,add_slider_btn;
    public SharedPreferences sharedPref;
    Toolbar toolbar;
public TextView edittextt,changepass;
    private String output;
ImageView imageView;
Dialog dialog;
    private TextView dialog_text;

    public Personal_info_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        sharedPref =getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_personal_info_fragment, container, false);


        toolbar=(Toolbar)view.findViewById(R.id.profile_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.popup_layout);
        dialog_text=dialog.findViewById(R.id.popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        imageView=view.findViewById(R.id.logo_image);
        edittextt=view.findViewById(R.id.mytextview1);
        toolbar.inflateMenu(R.menu.bluedasrt);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });


         add_slider_btn=(Button)view.findViewById(R.id.slidder_add_btn) ;
         add_slider_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 Intent intent=new Intent(getContext(), Slider_activity.class);
                 startActivity(intent);
             }
         });
        firstname=view.findViewById(R.id.first_name);
        lastname=view.findViewById(R.id.last_name);
        email=view.findViewById(R.id.email);
        mobile=view.findViewById(R.id.telephone);

        comapny_name=view.findViewById(R.id.company_name);
        gstin_no=view.findViewById(R.id.gstin_no);
        cin_no=view.findViewById(R.id.cin_no);
        description=view.findViewById(R.id.description);
        fax=view.findViewById(R.id.fax);
        adhar_no=view.findViewById(R.id.adhar);
        pan_no=view.findViewById(R.id.pan);
        cancel=view.findViewById(R.id.cancell);
cancel.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
});
        save=view.findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edittextt.getText().equals("Edit Now")){
                    updateUsers();
                    return;
                }
                dialog_text.setText("No changes made");
                dialog.show();
            }
        });



        changepass=view.findViewById(R.id.mytextview2);

       /* firstname.setText(sharedPref.getString("firstname", null));
        lastname.setText(sharedPref.getString("lastname", null));
        email.setText(sharedPref.getString("email", null));
        mobile.setText(sharedPref.getString("mobile", null));*/
        userInformation( );

        edittextt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               edittextt.setText("Edit Now");
                firstname.setFocusableInTouchMode(true);
                lastname.setFocusableInTouchMode(true);
                email.setFocusableInTouchMode(true);
                mobile.setFocusableInTouchMode(true);
                adhar_no.setFocusableInTouchMode(true);
                pan_no.setFocusableInTouchMode(true);
                comapny_name.setFocusableInTouchMode(true);
                gstin_no.setFocusableInTouchMode(true);
                cin_no.setFocusableInTouchMode(true);
                description.setFocusableInTouchMode(true);
                fax.setFocusableInTouchMode(true);


            }
        });
       return  view;
    }
    @Override
    public void onResume() {
        super.onResume();
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }
        catch (NullPointerException e){

        }
    }
    @Override
    public void onStop() {
        super.onStop();
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        }
        catch (NullPointerException e){

        }
    }


    public void userInformation ( ){

        String url=ip1+"/b2b/api/v1/user/info";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);

                    firstname.setText(jObj.getString("firstName"));
                    lastname.setText(jObj.getString("lastName"));
                    email.setText(jObj.getString("email"));
                    mobile.setText(jObj.getString("mobileNumber"));




                          JSONObject compan_obje=jObj.getJSONObject("company");
                          comapny_name.setText(compan_obje.getString("name"));
                          gstin_no.setText(compan_obje.getString("gstin"));
                    cin_no.setText(compan_obje.getString("cin"));
                    fax.setText(compan_obje.getString("fax"));
                    description.setText(compan_obje.getString("description"));
                    adhar_no.setText(jObj.getString("adharNo"));
                    pan_no.setText(jObj.getString("pan"));



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

                output=sharedPref.getString(ACCESS_TOKEN, null);

                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                params.put("Content-Type", "application/json");
                return params;
            }
        };




        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    public void updateUsers() {

        JSONObject mainJasan= new JSONObject();

         String   url=ip+"uaa/b2b/api/v1/user/update";


            JSONObject json1= new JSONObject();

            try {
                json1.put("logoImageId","");
                json1.put("name",comapny_name.getText().toString());
                json1.put("product","Jewellery");
                json1.put("cinDocumentId","");
                json1.put("gstDocumentId","");
                json1.put("gstin",gstin_no.getText().toString());
                json1.put("cin",cin_no.getText().toString());
                json1.put("id",sharedPref.getString("userid",null));

                mainJasan.put("company",json1);
                mainJasan.put("firstName",firstname.getText().toString());
                mainJasan.put("lastName",lastname.getText().toString());
                mainJasan.put("email",email.getText().toString());
                mainJasan.put("mobileNumber",mobile.getText().toString());
                mainJasan.put("adharNo",adhar_no.getText().toString());
                mainJasan.put("pan",pan_no.getText().toString());
                mainJasan.put("defaultAddressID","");
                mainJasan.put("role",sharedPref.getString("role",null));
                mainJasan.put("password","");
                mainJasan.put("id",sharedPref.getString("userid",null));
                mainJasan.put("adharDocumentId","");
                mainJasan.put("panDocumentId","");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, mainJasan, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {


                    dialog_text.setText("Profile Updated Successfully");

                    dialog.show();
                    userInformation();


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

                headr.put("Authorization","bearer "+output);
                headr.put("Content-Type", "application/json");
                return headr;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}

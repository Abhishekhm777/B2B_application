package com.example.compaq.b2b_application.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Activity.Sign_up_Activity;
import com.example.compaq.b2b_application.Model.SignupModel;
import com.example.compaq.b2b_application.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;


public class signup_Fragment extends Fragment {
    public EditText user_name,email,teli_phone,password,gstin_e,company_name;
    public  String firstname_t,email_t,phone_t,password_t,gstin_t,company;
    CheckBox checkBox;
    public Button sign_upbutton,upload_logo,gstn_button;
    ImageView imageView;

    Toolbar toolbar;
    private static  int count=1;
    private static String id="";
    SignupModel signupModel;
    private int PICK_IMAGE_REQUEST = 1;
    private EditText otp;
    private AlertDialog alertDialoga;
    private Object logo_image;
    Dialog dialog;
    EditText mobile_otp,email_otp;
    Button mobile_btn,email_btn;
    // Inflate the layout for this fragment
    SharedPreferences sharedPref;
    SharedPreferences.Editor myEditior;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


       View view= inflater.inflate(R.layout.fragment_signup_, container, false);

        sharedPref = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        myEditior = sharedPref.edit();
        company_name=(EditText)view.findViewById(R.id.edit_company_name);
        gstin_e=(EditText)view.findViewById(R.id.edit_GSTIN);
        gstn_button=(Button)view.findViewById(R.id.upload_gst);
        user_name=(EditText)view.findViewById(R.id.user_name);
        email=(EditText)view.findViewById(R.id.edit_email);
        teli_phone=(EditText)view.findViewById(R.id.edit_mobile);
        password=(EditText)view.findViewById(R.id.edit_password);
        imageView=(ImageView)view.findViewById(R.id.profile_logo);

        upload_logo=(Button)view.findViewById(R.id.upload_logBtn);
        sign_upbutton=(Button)view.findViewById(R.id.signup_process);
        checkBox=(CheckBox)view.findViewById(R.id.seller_checkbox);



        dialog = new Dialog(getContext());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.verification_popup);
        email_otp=(EditText)dialog.findViewById(R.id.email_otp) ;
        mobile_otp=(EditText)dialog.findViewById(R.id.mobile_otp);

        mobile_btn=(Button)dialog.findViewById(R.id.mobile_button);
        email_btn=(Button)dialog.findViewById(R.id.email_btn) ;

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE);
                if(ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            2000);
                }
                else {
                    startGallery();
                }

            }
        });
        email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  chec_otp(email_otp.getText().toString(),email_btn,email_otp);
            }
        });

        mobile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //chec_otp(mobile_otp.getText().toString(),mobile_btn,mobile_otp);

            }
        });

        gstn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickImageIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickImageIntent.setType("image/*");
                pickImageIntent.putExtra("aspectX", 1);
                pickImageIntent.putExtra("aspectY", 1);
                pickImageIntent.putExtra("scale", true);
                pickImageIntent.putExtra("outputFormat",
                        Bitmap.CompressFormat.JPEG.toString());
                startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
            }
        });




        /////////////////////////////////////////////////////////////////////////////

        sign_upbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 company=company_name.getText().toString();
                firstname_t = user_name.getText().toString();
                email_t = email.getText().toString();
                phone_t = teli_phone.getText().toString();
                password_t = password.getText().toString();
                gstin_t=gstin_e.getText().toString();

                if (TextUtils.isEmpty(company.trim())) {
                    company_name.setError("Company name is required");
                    return;
                }

                if (TextUtils.isEmpty(firstname_t.trim())) {
                    user_name.setError("User name is required");
                    return;
                }

                if (TextUtils.isEmpty(email_t.trim())) {
                    email.setError("Enter valid email");
                    return;
                }

                if (TextUtils.isEmpty(phone_t.trim())) {
                    teli_phone.setError("Enter the 10digit Mobile No.");
                    return;
                }
                if (TextUtils.isEmpty(password_t.trim())) {
                    password.setError("Invalid password.Password must be at least 6 characters long and contain a number");
                    return;
                }
                if(!checkBox.isChecked()){
                    BottomSheet.Builder builder1 = new BottomSheet.Builder(getContext());
                    builder1.setTitle("Please accept Terms and conditions");
                    builder1.show();
                    return;
                }else{
                    signupModel=new SignupModel(company,gstin_t,firstname_t,email_t,phone_t,password_t);
                    dialog.show();
                    check();
                   // send_otp(phone_t,email_t);

                }

            }


        });
        return  view;

    }

    //////////////////////////check email or phone/////////////////////////////////////
    public  void check(){
    if(count==2){
        id="%2B91"+phone_t;
    }
    String checkurl=ip1+"/b2b/api/v1/user/userExist?emailOrMobile="+id;
        Log.d("checkUrl",checkurl);
    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
    StringRequest stringRequest = new StringRequest(Request.Method.GET,checkurl,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    /*8481903248*/
                    Log.d("res3", response.toString());
                    if(response.equals("true")&&count==1){
                        Toast.makeText(getActivity(),"Mail already exist change the Mail id!",Toast.LENGTH_SHORT).show();
                    }
                    else if(response.equals("true")&&count==2){
                        Toast.makeText(getActivity(),"Mail already exist change the Mail id!",Toast.LENGTH_SHORT).show();
                    }
                    else if (response.equals("false")&&count==1){
                        count=2;
                        check();
                        return;
                    }
                    else if(response.equals("false")&&count==2) {
                        myEditior.putString("CLASS_TYPE", "REGISTER");
                        myEditior.commit();
                        myEditior.apply();



                        /*final JSONObject postparams = new JSONObject();
                        ArrayList<JSONObject>regiter_infolist=new ArrayList<>();
                        String resUrl=Domain.domainName()+"/api/v1/user/otp?mobileNumber=%2B91"+mobileNumber+"&email="+email+"&verification=true";
                        try {
                            postparams.put("email",email);
                            postparams.put("firstName",firstName);
                            postparams.put("lastName", lastName);
                            postparams.put("mobileNumber","+91"+mobileNumber);
                            postparams.put("password", password);
                            postparams.put("role","ROLE_CUSTOMER");
                            regiter_infolist.add(postparams);
                            Log.d("json obj",postparams.toString());
                            getotp(postparams,regiter_infolist,resUrl);*/
                       // Sign_up_Activity.set_view(3);
                    }
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {

            NetworkResponse response = volleyError.networkResponse;
            if(response != null && response.data != null) {
                switch (response.statusCode) {

                    case 500:
                        volleyError.printStackTrace();
                }

            }
        }
    });

        requestQueue.add(stringRequest);
}


    //////////////////Opening Gallery On Clicking imageview On Drawer///////////////
    private void startGallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        cameraIntent.putExtra("crop", "true");
        cameraIntent.putExtra("aspectX", 100);
        cameraIntent.putExtra("aspectY", 100);
        cameraIntent.putExtra("outputX", 256);
        cameraIntent.putExtra("outputY", 356);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1000);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super method removed
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                Uri returnUri = data.getData();
                Bitmap bitmapImage ;
                Bitmap bitimage = null;
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
                    bitimage = getResizedBitmap(bitmapImage, 400);
                    imageView.setImageBitmap(bitimage);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //////////////Resizing Images.///////////////////////////////////////
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}

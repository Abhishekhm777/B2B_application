package com.example.compaq.b2b_application.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Activity.Sign_up_Activity;
import com.example.compaq.b2b_application.Helper_classess.AppHelper;
import com.example.compaq.b2b_application.Helper_classess.VolleyMultipartRequest;
import com.example.compaq.b2b_application.Helper_classess.VolleySingleton;
import com.example.compaq.b2b_application.Model.SignupModel;
import com.example.compaq.b2b_application.R;
import com.rilixtech.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.media.MediaRecorder.VideoSource.CAMERA;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Fragments.Custom_order.PICK_IMAGE;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;


public class signup_Fragment extends Fragment {
    public EditText user_name,email,teli_phone,password,gstin_e,company_name,confirm_password;
    public  String firstname_t,email_t,phone_t,password_t,gstin_t,company,confirm_password_t;
    CheckBox checkBox;
    public Button sign_upbutton,upload_logo,gstn_button;
    ImageView imageView;
    private static String imageid="",gstid="";
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
    Bundle bundle;
    ArrayList<SignupModel>signupModelArrayList;
    private int GALLERY = 1, CAMERA = 2;
   // TextView gst_file;
    CountryCodePicker ccp;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


         View view= inflater.inflate(R.layout.fragment_signup_, container, false);

        sharedPref = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        myEditior = sharedPref.edit();
        bundle=new Bundle();
        signupModelArrayList= new ArrayList<>();
        company_name=(EditText)view.findViewById(R.id.edit_company_name);
        gstin_e=(EditText)view.findViewById(R.id.edit_GSTIN);
        //gstn_button=(Button)view.findViewById(R.id.upload_gst);
        user_name=(EditText)view.findViewById(R.id.user_name);
        email=(EditText)view.findViewById(R.id.edit_email);
        teli_phone=(EditText)view.findViewById(R.id.edit_mobile);
        password=(EditText)view.findViewById(R.id.edit_password);
        confirm_password=(EditText)view.findViewById(R.id.confir_password) ;
        imageView=(ImageView)view.findViewById(R.id.profile_logo);
       // gst_file=(TextView)view.findViewById(R.id.gst_file);

        upload_logo=(Button)view.findViewById(R.id.upload_logBtn);
        sign_upbutton=(Button)view.findViewById(R.id.signup_process);
        checkBox=(CheckBox)view.findViewById(R.id.seller_checkbox);
        ccp = (CountryCodePicker)view.findViewById(R.id.ccp);



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

                showPictureDialog(1000);

            }
        });


        /*gstn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               *//* Intent pickImageIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickImageIntent.setType("image/*");
                pickImageIntent.putExtra("aspectX", 1);
                pickImageIntent.putExtra("aspectY", 1);
                pickImageIntent.putExtra("scale", true);
                pickImageIntent.putExtra("outputFormat",
                        Bitmap.CompressFormat.JPEG.toString());
                startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);*//*
               showPictureDialog(5000);

            }
        });
*/



        /////////////////////////////////////////////////////////////////////////////

        sign_upbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 company=company_name.getText().toString();
                firstname_t = user_name.getText().toString();
                email_t = email.getText().toString();
                phone_t = teli_phone.getText().toString();
                password_t = password.getText().toString();
                confirm_password_t=confirm_password.getText().toString();
                gstin_t=gstin_e.getText().toString();

                Log.d("emial....",ccp.getSelectedCountryCode());

                if (TextUtils.isEmpty(company.trim())) {
                    company_name.setError("Company name is required");
                    return;
                }

               else if (TextUtils.isEmpty(firstname_t.trim())) {
                    user_name.setError("User name is required");
                    return;
                }

               else if (TextUtils.isEmpty(email_t.trim())) {
                    email.setError("Enter valid email");
                    return;
                }

               else if (TextUtils.isEmpty(phone_t.trim())) {
                    teli_phone.setError("Enter the 10digit Mobile No.");
                    return;
                }
               else if ( password_t.length()<5 ||TextUtils.isEmpty(password_t.trim())) {

                    password.setError("Invalid password.Password must be at least 6 characters long and contain a number");
                    return;
                }
               else if ( !password_t.equals(confirm_password_t)) {
                   confirm_password.setError("Password mismatched");
                    return;
                }
               /* if(!checkBox.isChecked()){
                    BottomSheet.Builder builder1 = new BottomSheet.Builder(getContext());
                    builder1.setTitle("Please accept Terms and conditions");
                    builder1.show();
                    return;
                }*/else{
                    try {
                        if(imageid==""|| imageid==null){
                            imageid="35f05e0e-56fb-4676-9819-381da000696b";
                        }
                        id=email_t;
                        phone_t=ccp.getSelectedCountryCode()+phone_t;
                       signupModel=new SignupModel(imageid,company,gstin_t,firstname_t,email_t,phone_t,password_t);
                        signupModelArrayList.add(signupModel);
                     bundle.putSerializable("Data",(Serializable) signupModelArrayList);
                        check();

                   // send_otp(phone_t,email_t);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

            }


        });
        return  view;

    }

    ///////////////////////////////////////////////////////////////////////////////////
    private void showPictureDialog(final int code){
        android.app.AlertDialog.Builder pictureDialog = new android.app.AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                " Gallery",
                " Camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if(code==1000) {
                                    choosePhotoFromGallary(1020);
                                }
                                else if(code==5000){
                                    choosePhotoFromGallary(5020);
                                }
                                break;
                            case 1:
                                if(code==1000) {
                                    takePhotoFromCamera(1010);
                                }
                                else if(code==5000){
                                    takePhotoFromCamera(5010);
                                }

                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallary(int code) {

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
            startGallery(code);
        }
    }

    private void takePhotoFromCamera(int code) {
        /*Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);*/
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if(ActivityCompat.checkSelfPermission(getActivity(),

                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(
                    new String[]{Manifest.permission.CAMERA},
                    2000);
        }
        int permissionChec2k = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(ActivityCompat.checkSelfPermission(getActivity(),

                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    2000);
        }
        else {
            Log.d("clode...",code+"");
           startCamera(code);
        }
    }




    //////////////////Opening Gallery On Clicking imageview On Drawer///////////////
    private void startGallery(int code) {
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        cameraIntent.putExtra("crop", "true");
        cameraIntent.putExtra("aspectX", 100);
        cameraIntent.putExtra("aspectY", 100);
        cameraIntent.putExtra("outputX", 256);
        cameraIntent.putExtra("outputY", 356);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            if(code==1020) {
                startActivityForResult(cameraIntent, code);
            }
            else if(code==5020){
                startActivityForResult(cameraIntent, code);
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////
    private void startCamera(int code){
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, code);
       /* if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, code);
        }*/

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super method removed
        Log.d("requestcode.....",requestCode+"");
        switch (requestCode) {
            case 1020:
        if (resultCode == RESULT_OK) {

                Uri returnUri = data.getData();
                Bitmap bitmapImage ;
                Bitmap bitimage = null;
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
                    bitimage = getResizedBitmap(bitmapImage, 400);

                    imageView.setImageBitmap(bitimage);
                    imagePick( requestCode,returnUri);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
           break;
            case 5020:
                if (resultCode == RESULT_OK) {

                   // Uri returnUri = data.getData();
                    Bitmap bitmapImage ;
                    Bitmap bitimage = null;
                    try {
                      //  bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
                        //bitimage = getResizedBitmap(bitmapImage, 400);
                        Uri returnUri = data.getData();
                        imagePick(requestCode, returnUri);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 1010:
                if (resultCode == RESULT_OK) {


                    Bitmap bitmapImage ;
                    Bitmap bitimage = null;
                    try {
                        bitmapImage =(Bitmap) data.getExtras().get("data");
                        bitimage = getResizedBitmap(bitmapImage, 400);

                        imageView.setImageBitmap(bitimage);
                        Upload_image(requestCode,bitimage);
                       // imagePick( requestCode,returnUri);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 5010:
                    if (resultCode == RESULT_OK) {


                        Bitmap bitmapImage ;
                       // Bitmap bitimage = null;
                        try {
                            bitmapImage =(Bitmap) data.getExtras().get("data");
                            Bitmap bitimage = getResizedBitmap(bitmapImage, 400);

                            //Upload_image(requestCode,bitimage);
                            Uri returnUri = getImageUri(getContext(),bitimage);
                            imagePick( requestCode,returnUri);



                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;

           /* else if(requestCode == 1200){
                Uri returnUri = data.getData();
                Bitmap bitmapImage ;
                Bitmap bitimage = null;
                try {

                    bitmapImage =(Bitmap) data.getExtras().get("data");
                    bitimage = getResizedBitmap(bitmapImage, 400);
                    imageView.setImageBitmap(bitimage);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }*/
        }



        super.onActivityResult(requestCode, resultCode, data);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                inImage, "Title", null);
        return Uri.parse(path);
    }

    //////////////////////////////////////////image pick///////////////////////////////
    public  void imagePick(int code,Uri returnUri){
        try {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(returnUri, filePathColumn, null, null, null);

            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);


            Bitmap bitmap = Bitmap.createScaledBitmap((BitmapFactory.decodeFile(picturePath)), 150, 150, true);
            /*if(code==5020 ||code==5010) {
                File f = new File(picturePath);
                String imageName = f.getName();
                Log.d("image name....", imageName);
                gst_file.setText(imageName);
            }*/


            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);

            byte[] imageInByte = byteArrayOutputStream.toByteArray();
            long lengthbmp = imageInByte.length;
            Log.d("Image Size", String.valueOf(lengthbmp));


            Upload_image(code,bitmap);

        }
        catch (Exception e){

        }

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


///////////////////////get image id to load//////////////////////////////////////////////////////////

    public void Upload_image(final int code, final Bitmap bitmap) {


        String url = ip1+"/b2b/api/v1/user/image/save";
        Log.d("urls..",url);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                try {
                    if(code==1020 ||code==1010) {
                        imageid = resultResponse;
                        Log.i("Unexpected", resultResponse);
                    }
                   /* else if(code==5020||code==5010){
                      gstid=resultResponse;
                        Log.i("Unexpected", resultResponse);
                    }*/


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("exception");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView

                params.put("file", new DataPart("file_avatar.jpg", AppHelper.getFileDataFromDrawable(getActivity(),bitmap), "image/jpeg"));

                return params;
            }
        };


        VolleySingleton.getInstance(getActivity()).addToRequestQueue(multipartRequest);
    }




    //////////////////////////check email or phone/////////////////////////////////////
    public  void check(){
        if(count==1) {
            id = email_t;
        }
    else if(count==2){
        id="%2B"+phone_t;
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
                        Toast.makeText(getActivity(),"Mobile number already exist. change the number!",Toast.LENGTH_SHORT).show();
                    }
                    else if (response.equals("false")&&count==1){
                        count=2;
                        check();
                        return;
                    }
                    else if(response.equals("false")&&count==2) {
                        String url=ip1+"/b2b/api/v1/user/otp?mobileNumber=%2B"+phone_t+"&email="+email_t+"&verification=true";
                        myEditior.putString("PASSWORD_ID",signupModel.getEmail());
                        myEditior.putString("RESEND_OTP",url);
                        myEditior.commit();
                        myEditior.apply();
                        count=1;

                        getotp(url);




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


    public void getotp (String resUrl)
    {


        Log.d("response Params",resUrl);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest req = new StringRequest(Request.Method.GET,resUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //8481903248
                        Log.d("response",response);
                        Fragment signOtp = new Signup_OtpFragment();
                        signOtp.setArguments(bundle);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.register_layout, signOtp).addToBackStack(null).commit();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("response123",error.toString());
                        NetworkResponse response = error.networkResponse;
                        if(response != null && response.data != null) {
                            switch (response.statusCode) {

                                case 500:
                                    error.printStackTrace();
                            }

                        }

                    }



        });
        requestQueue.add(req);
    }





}

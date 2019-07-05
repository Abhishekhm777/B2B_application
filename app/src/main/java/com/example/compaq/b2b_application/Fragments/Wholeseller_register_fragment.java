package com.example.compaq.b2b_application.Fragments;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Helper_classess.AppHelper;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.Helper_classess.VolleyMultipartRequest;
import com.example.compaq.b2b_application.Helper_classess.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.example.compaq.b2b_application.Fragments.Custom_order.PICK_IMAGE;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;

/**
 * A simple {@link Fragment} subclass.
 */
public class Wholeseller_register_fragment extends Fragment {
    public EditText first_name,last_name,email,teli_phone,pin_e,password,pan_e,gstin_e,cin_e,adhar_e,brands_e,area_e,country_e,state_e,city_e;
    public  String firstname_t,lastname_t,email_t,phone_t,password_t,pan_t,gstin_t,cin_t,adhar_t,brands_t;
    CheckBox checkBox;
    public Button sign_upbutton,upload_logo,adhar_button,checkpinbtn,pan_button,cin_button,gstn_button;
    SharedPreferences pref;
    Toolbar toolbar;
    public Spinner spinner,spinner_country,spinner_state;
    public List<String> list;
    public List<String> country;
    public List<String> state;
    private int PICK_IMAGE_REQUEST = 1;
    private EditText otp;
    private AlertDialog alertDialoga;
    private Object logo_image;
    Dialog dialog;
    EditText mobile_otp,email_otp;
    Button mobile_btn,email_btn;
    public Wholeseller_register_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_seller_fragment, container, false);



        sign_upbutton=(Button)view.findViewById(R.id.signup);
        upload_logo=(Button)view.findViewById(R.id.upload_logBtn);
        pan_button=(Button)view.findViewById(R.id.pan_button);
        cin_button=(Button)view.findViewById(R.id.cin_button);
        gstn_button=(Button)view.findViewById(R.id.gstin_button);


        checkBox=(CheckBox)view.findViewById(R.id.seller_checkbox);
        adhar_button=(Button) view.findViewById(R.id.adhar_btn_selle);
        checkpinbtn=(Button)view.findViewById(R.id.check_pin) ;
        checkpinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pin_e.getText().toString()!="") {
                    address_fetch(pin_e.getText().toString());
                }
            }
        });

        first_name=(EditText)view.findViewById(R.id.first_name);
        last_name=(EditText)view.findViewById(R.id.last_name);
        email=(EditText)view.findViewById(R.id.email);
        teli_phone=(EditText)view.findViewById(R.id.telephone);
        password=(EditText)view.findViewById(R.id.password);
        adhar_e=(EditText)view.findViewById(R.id.adhar_field);
       /* brands_e=(EditText)view.findViewById(R.id.brands);*/
        cin_e=(EditText)view.findViewById(R.id.cin);
        gstin_e=(EditText)view.findViewById(R.id.gstin);
        pan_e=(EditText)view.findViewById(R.id.pan);
        area_e=(EditText)view.findViewById(R.id.area_field);
        country_e=(EditText)view.findViewById(R.id.country_field);
        state_e=(EditText)view.findViewById(R.id.state_field);
        city_e=(EditText)view.findViewById(R.id.city_field);
        pin_e=(EditText)view.findViewById(R.id.pincode_field);




        dialog = new Dialog(getContext());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.verification_popup);
        email_otp=(EditText)dialog.findViewById(R.id.email_otp) ;
        mobile_otp=(EditText)dialog.findViewById(R.id.mobile_otp);

        mobile_btn=(Button)dialog.findViewById(R.id.mobile_button);
        email_btn=(Button)dialog.findViewById(R.id.email_btn) ;


        email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chec_otp(email_otp.getText().toString(),email_btn,email_otp);
            }
        });

        mobile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chec_otp(mobile_otp.getText().toString(),mobile_btn,mobile_otp);

            }
        });


       /* spinner_country=(Spinner)view.findViewById(R.id.contry_spinner);
        spinner_state=(Spinner)view.findViewById(R.id.state_spinner);
        country = new ArrayList<>();
        country.add("Select Country");
        country.add("India");



        final ArrayAdapter<String> country_adaper = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, country) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }


        };
        spinner_country.setAdapter(country_adaper);



        state = new ArrayList<>();
        state.add("Select State");
        state.add("Andhra Pradesh");
        state.add("Arunachal Pradesh");
        state.add("Assam");
        state.add("Bihar");
        state.add("Chhattisgarh");
        state.add("Goa");
        state.add("Gujarat");
        state.add("Haryana");
        state.add("Himachal Pradesh");
        state.add("Jammu and Kashmir");
        state.add("Jharkhand");
        state.add("Karnataka");
        state.add("Kerala");
        state.add("Madhya Pradesh");
        state.add("Maharashtra");
        state.add("Meghalaya");
        state.add("Nagaland");
        state.add("Odisha");
        state.add("Punjab");
        state.add("Tamil Nadu");
        state.add("Telangana");
        state.add("Tripura");
        state.add("Uttarakhand  ");
        state.add("Delhi");
        state.add("Puducherry");







        final ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, state) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }


        };
        spinner_state.setAdapter(adapter_state);*/

        spinner=(Spinner)view.findViewById(R.id.spinner_field);
        list = new ArrayList<>();
        list.add("Select Product");
        list.add("Jewellery");



        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, list) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }


        };
        spinner.setAdapter(adapter);

        //////////////////////////////////////////

        upload_logo.setOnClickListener(new View.OnClickListener() {
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

        pan_button.setOnClickListener(new View.OnClickListener() {
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

        cin_button.setOnClickListener(new View.OnClickListener() {
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

        adhar_button.setOnClickListener(new View.OnClickListener() {
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

                firstname_t = first_name.getText().toString();
                lastname_t = last_name.getText().toString();
                email_t = email.getText().toString();
                phone_t = teli_phone.getText().toString();
                password_t = password.getText().toString();
              /*  brands_t=brands_e.getText().toString();*/
                adhar_t=adhar_e.getText().toString();
                gstin_t=gstin_e.getText().toString();
                cin_t=cin_e.getText().toString();
                pan_t=pan_e.getText().toString();

                if (TextUtils.isEmpty(firstname_t.trim())) {
                    first_name.setError("First Name Is Essential");
                    return;
                }
                if (TextUtils.isEmpty(gstin_t.trim())) {
                    gstin_e.setError("Last Name Is Essential");
                    return;
                }
                if (TextUtils.isEmpty(email_t.trim())) {
                    email.setError("Enter valid email");
                    return;
                }
                if (TextUtils.isEmpty(pan_t.trim())) {
                    pan_e.setError("Enter the 10digit Mobile No.");
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

                    dialog.show();
                    send_otp(phone_t,email_t);

                }

            }


        });


            return view;
    }
    public  void register_user(){

        JSONObject register_object= new JSONObject();
        JSONObject company_object=new JSONObject();
        try {
            JSONArray brands_array=new JSONArray();

            JSONObject brands_object=new JSONObject();
            brands_object.put("brandLogoId","not available");
            brands_object.put("name",brands_t);

            brands_array.put(brands_object);

            company_object.put("brands",brands_array);
            company_object.put("cin",cin_t);
            company_object.put("gstin",gstin_t);
            company_object.put("name","");
            company_object.put("product","Jewellery");

            register_object.put("company",company_object);

            register_object.put("email",email_t);
            register_object.put("firmName","");
            register_object.put("pan",pan_t);
            register_object.put("adharNo",adhar_t);
            register_object.put("firstName",firstname_t);
            register_object.put("lastName",lastname_t);
            register_object.put("mobileNumber",phone_t);
            register_object.put("password",password_t);
            register_object.put("role",sign_upbutton.getHint());

          /*  LayoutInflater li = LayoutInflater.from(getContext());
            View promptsView = li.inflate(R.layout.premium_dialogue_layout, null);
            final AlertDialog.Builder alertDialog=new AlertDialog.Builder(getContext());
            alertDialog  .setTitle("SIGN UP");
            alertDialog .setMessage("Please enter the OTP that has sent to your registered Mob.No.");

            alertDialog.setView(promptsView);
            otp=(EditText) promptsView.findViewById(R.id.edittext);


*//*
                        alertDialog.setCancelable(false);
                        alertDialog  .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });*//*


            alertDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    chec_otp(otp.getText().toString());
                }
            });
            alertDialoga = alertDialog.create();

            alertDialoga.show();
*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("placeeeee", String.valueOf(register_object));

        String url="http://192.168.100.4:8766/uaa/b2b/api/v1/user/save";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, register_object, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    String status=response.toString();






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
                headr.put("Content-Type", "application/json");
                return headr;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext().getApplicationContext());
        queue.add(request);
    }
    public void chec_otp (final String otp,final Button button,final EditText editText){
        String url=ip1+"/b2b/api/v1/user/otp/verification/"+otp+"?email="+email_t;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {

                    if(response.equalsIgnoreCase("false")){
                        editText.setError("Wrong otp");
                        Snackbar.make(getView(), "Wrong OTP", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                    if(response.equalsIgnoreCase("true")){
                        button.setText("VERIFIED");
                        button.getBackground().setAlpha(100);
                        button.setClickable(false);

                    }
                    if(email_btn.getText().toString().equalsIgnoreCase("VERIFIED")&&mobile_btn.getText().toString().equalsIgnoreCase("VERIFIED")){

                       /* Snackbar.make(getView(), "Registered Succet", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();*/
                        dialog.dismiss();
                        register_user();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                BottomSheet.Builder builder1 = new BottomSheet.Builder(getContext());
                builder1.setTitle("Sorry!,something went wrong");
                builder1.show();
            }
        })

        {
            @Override
            public Map<String, String> getHeaders() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                return params;
            }
        };




        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.e("LOGO",encodedImage);
        return encodedImage;

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1212:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String uriString = uri.toString();
                    File myFile = new File(uriString);
                    String path = myFile.getAbsolutePath();
                    String displayName = null;

                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                adhar_button.setText(displayName);
                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uriString.startsWith("file://")) {
                        displayName = myFile.getName();
                        adhar_button.setText(displayName);
                    }

                }
                break;
        }
        if (requestCode == PICK_IMAGE) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);

                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);


                Bitmap bitmap = Bitmap.createScaledBitmap((BitmapFactory.decodeFile(picturePath)), 800, 800, true);


                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);

                byte[] imageInByte = byteArrayOutputStream.toByteArray();
                long lengthbmp = imageInByte.length;
                Log.d("Image Size", String.valueOf(lengthbmp));


                Upload_image(bitmap);
                upload_logo.setText("Replace image");

            }
            catch (Exception e){

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void address_fetch(String pin){

        String URL_DATA="http://postalpincode.in/api/pincode/"+pin;


        StringRequest stringRequest=new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public
            void onResponse(String response) {

                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray adress_array = jsonObj.getJSONArray("PostOffice");
                    JSONObject addres_object = adress_array.getJSONObject(0);
                    String areaname = (addres_object.getString("Name"));
                    String state = (addres_object.getString("State"));
                    String country = (addres_object.getString("Country"));
                    String district = (addres_object.getString("District"));


                    area_e.setText(areaname);
                    city_e.setText(district);
                    state_e.setText(state);
                    country_e.setText(country);


                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public
            void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    public void Upload_image(final Bitmap bitmap) {


        String url = ip1+"/b2b/api/v1/user/image/save";
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                try {


                    Log.i("Unexpected", resultResponse);

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
    public void send_otp (String mob,String email ){
        String url=ip1+"/b2b/api/v1/user/otp?mobileNumber="+mob+"&email="+email+"&verification=true";
        Log.e("Send CHECK",url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try{

                    if(response.equalsIgnoreCase("sucess")) {
                        Snackbar.make(getView(), "OTP has been Sent", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                    Log.e("RESPONSE",response);

                } catch (Exception e) {
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

                return params;
            }
        };




        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}

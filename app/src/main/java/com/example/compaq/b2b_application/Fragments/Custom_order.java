package com.example.compaq.b2b_application.Fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Helper_classess.AppHelper;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.Helper_classess.VolleyMultipartRequest;
import com.example.compaq.b2b_application.Helper_classess.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class Custom_order extends Fragment {
    public SharedPreferences sharedPref;
    Toolbar toolbar;
    EditText name, qty, weight,purity, melting, rate, advance, desc,seal,branch,email,firmname,gst_no,cust_name;

   AutoCompleteTextView mobile;
    Spinner spinner;
    Button button, image_button;
    TextView conform,cancel,msg;
    ImageView imageView;
    Uri selectedImageUri;
    public List<String> spin_aray;
    private String selectedPath;
    private String fileName;
    private String output,seller_id,customer_id;
    ImageView dialogue_image,close;
    public LinearLayout gallery,camera,detail;
    private FrameLayout linearLayout;
    private RelativeLayout relativeLayout;
    Dialog myDialogue,options_dialog;
    private String image_id;
    final int CAMERA_PIC_REQUEST = 1337;
    public static final int PICK_IMAGE = 1;
    private TextView expect;
    private int mYear,mMonth,mDay;
    Bundle bundle;
    String image_url;
    public List<String> m_array=new ArrayList<>();

    public Custom_order() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_custom_order, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.custom_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.inflateMenu(R.menu.bluedasrt);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
        expect=(TextView)view.findViewById(R.id.expected_date);
        expect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String dateString = String.format("%02d-%02d-%02d", year,  monthOfYear+1, dayOfMonth);
                                expect.setText(dateString);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        myDialogue = new Dialog(getContext());
        myDialogue.setContentView(R.layout.alert_popup);
        conform=(TextView) myDialogue.findViewById(R.id.ok);
        cancel=(TextView) myDialogue.findViewById(R.id.cancel);
        msg=(TextView) myDialogue.findViewById(R.id.popup_textview);
        msg.setText("Do you want to Place this order ?");


        linearLayout=(FrameLayout) view.findViewById(R.id.image_layout);
        relativeLayout=(RelativeLayout) view.findViewById(R.id.button_lay);
        options_dialog=new Dialog(getContext());

        options_dialog.setContentView(R.layout.photo_options_layout);
         gallery=(LinearLayout)options_dialog.findViewById(R.id.gallery);
         camera=(LinearLayout)options_dialog.findViewById(R.id.camera);

         detail=(LinearLayout)view.findViewById(R.id.detail);
        imageView = (ImageView) view.findViewById(R.id.imageview);
        bundle=this.getArguments();

            image_url = bundle.getString("url");
            image_id = bundle.getString("image_id");



        Glide.with(this).load(image_url).into(imageView);
        relativeLayout.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialogue.dismiss();
            }
        });

        conform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               place_order();
               myDialogue.dismiss();
            }
        });
        button=(Button)view.findViewById(R.id.place_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!TextUtils.isEmpty(name.getText().toString()) && !TextUtils.isEmpty(qty.getText().toString()) && !TextUtils.isEmpty(weight.getText().toString())&&linearLayout.isShown()) {


                   myDialogue.show();

                }
                else {
                    Snackbar.make(getView(),"Please fill Mandatory feilds",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        sharedPref = getActivity().getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        output = sharedPref.getString(ACCESS_TOKEN, null);
        customer_id = sharedPref.getString("userid", null);
        seller_id = sharedPref.getString("Wholeseller_id", null);


        name = (EditText) view.findViewById(R.id.name);
        name.setText(bundle.getString("name"));
        qty = (EditText) view.findViewById(R.id.qty);
        weight = (EditText) view.findViewById(R.id.weight);
      /*  purity = (EditText) view.findViewById(R.id.purity);*/
        melting = (EditText) view.findViewById(R.id.melting);
        desc = (EditText) view.findViewById(R.id.desc);
        getContacts();

        firmname = (EditText) view.findViewById(R.id.firm);
        email = (EditText) view.findViewById(R.id.cust_email);
        mobile = (AutoCompleteTextView) view.findViewById(R.id.cust_no);
        cust_name= (EditText) view.findViewById(R.id.cust_name);

        mobile.setAdapter(new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,m_array));

        image_button = (Button) view.findViewById(R.id.upload_logBtn);
        image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                options_dialog.show();
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        options_dialog.dismiss();
                        cameraOpen();
                    }
                });
                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        options_dialog.dismiss();
                        gaallery_open();
                    }
                });

            }
        });
        mobile.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                getUserDetail(mobile.getText().toString());
                detail.setVisibility(View.VISIBLE);

            }
        });

        spinner = (Spinner) view.findViewById(R.id.spinner);
        spin_aray = new ArrayList<>();
        spin_aray.add("Select seal");
        spin_aray.add("MRK");
        spin_aray.add("GB");
        spin_aray.add("GK");
        spin_aray.add("GM");
        spin_aray.add("MG");
        spin_aray.add("GP");
        spin_aray.add("GH");

        final ArrayAdapter<String> country_adaper = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, spin_aray) {
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
        spinner.setAdapter(country_adaper);


        return view;
    }


    public void cameraOpen(){

        try {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
        }
        catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    public void gaallery_open()
    {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // ******** code for crop image
        i.putExtra("crop", "true");
        i.putExtra("aspectX", 100);
        i.putExtra("aspectY", 100);
        i.putExtra("outputX", 256);
        i.putExtra("outputY", 356);

        try {

            i.putExtra("return-data", true);
            startActivityForResult(
                    Intent.createChooser(i, "Select Picture"), PICK_IMAGE);
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    private void startGallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        cameraIntent.setType("image/*");
    /*    if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1000);
        }*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {

if(data!=null) {
    if (requestCode == CAMERA_PIC_REQUEST) {
        try {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            Bitmap bitmap = Bitmap.createScaledBitmap(image, 800, 800, true);
            imageView.setImageBitmap(bitmap);
            relativeLayout.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            Upload_image();

        }
      catch (NullPointerException e){

      }

    } if(requestCode==PICK_IMAGE) {
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


    imageView.setImageBitmap(bitmap);
    relativeLayout.setVisibility(View.GONE);
    linearLayout.setVisibility(View.VISIBLE);

    Upload_image();

    /* Upload_image(byteArrayOutputStream);*/


    String fileNameSegments[] = picturePath.split("/");
    fileName = fileNameSegments[fileNameSegments.length - 1];
    Log.e("FILE", fileName);
    /* Upload_image(fileName);*/

           /* Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);

                Bitmap lastBitmap = null;
                lastBitmap = bitmap;
                //encoding image to string
                String image = getStringImage(lastBitmap);
                Log.d("image", image);
                //passing the image to volley
                Upload_image(image);
            }
 catch (IOException e) {
                e.printStackTrace();
            }*/


    /*cursor.close();*/
}
catch (NullPointerException e){

}

    } /*else {
            Toast.makeText(getActivity(), "Try Again!!", Toast.LENGTH_SHORT).show();
        }*/
    super.onActivityResult(requestCode, resultCode, data);
}
    }

    /* public void Upload_image(final String image) {

         String url = "https://server.mrkzevar.com/gate/b2b/zuul/order/api/v1/image/save";
         StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

             @Override
             public void onResponse(String response) {

                 try {


                 } catch (Exception e) {
                     e.printStackTrace();
                 }


             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError volleyError) {
                 NetworkResponse networkResponse = volleyError.networkResponse;

                 String errorMessage = "Unknown error";
                 if (networkResponse == null) {
                     if (volleyError.getClass().equals(TimeoutError.class)) {
                         errorMessage = "Request timeout";
                     } else if (volleyError.getClass().equals(NoConnectionError.class)) {
                         errorMessage = "Failed to connect server";
                     }
                 } else {
                     String result = new String(networkResponse.data);
                     try {
                         JSONObject response = new JSONObject(result);

                         String message = response.getString("message");
                         Log.e("Error Status", message);
                         String excepton = response.getString("exception");
                         Log.e("Error exception", excepton);
                         String status = response.getString("status");

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
             }


         })

         {
             @Override
             public Map<String, String> getParams() {


                 Map<String, String> params = new HashMap<String, String>();
                 params.put("Authorization", "bearer "+output);
                 params.put("Content-Type", "application/x-www-form-urlencoded");
                 params.put("file", image);

                 return params;
             }
             @Override
             public Map<String, String> getHeaders() throws AuthFailureError {
                 final HashMap<String, String> headers = new HashMap<>();
                 headers.put("Authorization", "bearer "+output);
                 headers.put("Content-Type", "application/x-www-form-urlencoded");
                 headers.put("file", image);
                 return headers;
             }
             @Override
             public byte[] getBody() {
                 Map<String,String> params = new HashMap<>();
                 params.put("file", image);
                 String postBody = createPostBody(params);

                 return postBody.getBytes();
             }
         };

         RequestQueue rQueue = Volley.newRequestQueue(getContext());
         rQueue.add(stringRequest);


     }*/


    private String createPostBody(Map<String, String> params) {
        StringBuilder sbPost = new StringBuilder();
        for (String key : params.keySet()) {
            if (params.get(key) != null) {
                sbPost.append("\r\n" + "--" + "WebKitFormBoundaryQU7YiLs7VviFs152" + "\r\n");
                sbPost.append("Content-Disposition: form-data; name=\"" + key + "\"" + "\r\n\r\n");
                sbPost.append(params.get(key));
            }
        }

        return sbPost.toString();
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }

    public void Upload_image() {
        // loading or check internet connection or something...
        // ... then
        String url = ip+"gate/b2b/zuul/order/api/v1/image/save";
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                try {

                     image_id=resultResponse;
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
                params.put("Authorization", "bearer "+output);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView

                params.put("file", new DataPart("file_avatar.jpg", AppHelper.getFileDataFromDrawable(getActivity(),imageView.getDrawable()), "image/jpeg"));

                return params;
            }


        };


        VolleySingleton.getInstance(getActivity()).addToRequestQueue(multipartRequest);
    }

 /*  public  void Upload_image(final ByteArrayOutputStream byteArrayOutputStream) {
       String url = "https://server.mrkzevar.com/gate/b2b/zuul/order/api/v1/image/save";
       SimpleMultiPartRequest multipartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
           @Override
           public void onResponse(NetworkResponse response) {
               String resultResponse = new String(response.data);
               // parse success output
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

                       String excepton = response.getString("exception");
                       Log.e("Error exception", excepton);
                       String status = response.getString("status");
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
               error.printStackTrace();
           }
       }) {
           @Override
           protected Map<String, String> getParams() {
               Map<String, String> params = new HashMap<>();
               params.put("Authorization", "bearer " + output);
               params.put("Content-Type", "application/x-www-form-urlencoded");
               return params;
           }
       };





       RequestQueue rQueue = Volley.newRequestQueue(getActivity());
       rQueue.add(multipartRequest);
   }

*/

    public void place_order() {

        JSONObject mainJasan= new JSONObject();
         String  url=ip+"gate/b2b/order/api/v1/order/add";
            JSONObject json1= new JSONObject();
            final JSONArray items_jsonArray=new JSONArray();
            try {
                json1.put("name",name.getText().toString());
                json1.put("quantity",qty.getText());
                json1.put("paymentStatus","PENDING");
                json1.put("seller",seller_id);
               /* json1.put("seal",seal1.getText());*/
                json1.put("melting",melting.getText().toString());
                json1.put("customOrderImageID",image_id);
             /*   json1.put("netWeight",weight1.getText());*/
                json1.put("totweight",weight.getText().toString());


                items_jsonArray.put(json1);
                mainJasan.put("items",items_jsonArray);
                mainJasan.put("customer",customer_id);
                mainJasan.put("paymentStatus","PENDING");
                mainJasan.put("orderType","ONLINECUSTOM_ORDER");
                mainJasan.put("items",items_jsonArray);

                Log.e("OBJECT Status", json1.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
         /*   Map<String, String> params = new HashMap<>();
            params.put("customer", userid);
            params.put("items", String.valueOf(items_jsonArray));*/

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, mainJasan, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {



                    Snackbar.make(getView(), "Your Custom Order Placed Successfully !", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                    save_contact();
                   /* Fragment currentFragment = getFragmentManager().findFragmentByTag("custom_order");
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.detach(currentFragment);
                    fragmentTransaction.attach(currentFragment).addToBackStack(null);
                    fragmentTransaction.commit();*/



                   /* if(getActivity().getClass().getSimpleName().equalsIgnoreCase("Main2Activity")){
                        Main2Activity main2Activity= new Main2Activity();
                        main2Activity.setupBadge(getContext());
                    }
                  *//* Main2Activity main2Activity= new Main2Activity();
                    main2Activity.setupBadge(getContext());*//*
                    Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }*/
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
                Toast.makeText(getContext(), "Please select SEAL", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headr = new HashMap<>();
                sharedPref=getActivity().getSharedPreferences("USER_DETAILS",0);

                String output=sharedPref.getString(ACCESS_TOKEN, null);
                headr.put("Authorization","bearer "+output);
                headr.put("Content-Type", "application/json");
                return headr;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

    public void save_contact() {

        JSONObject mainJasan= new JSONObject();
        String  url=ip+"uaa/b2b/contact-book";


        JSONObject json1= new JSONObject();
        try {
            json1.put("address","");
            json1.put("branch",branch.getText().toString());
            json1.put("email",email.getText().toString());
            json1.put("firmName",firmname.getText().toString());
            json1.put("gstNumber",gst_no.getText().toString());
            json1.put("mobileNumber",mobile.getText().toString());
            json1.put("name",name.getText().toString());
            json1.put("user",customer_id);


            Log.e("OBJECT Status", json1.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
         /*   Map<String, String> params = new HashMap<>();
            params.put("customer", userid);
            params.put("items", String.valueOf(items_jsonArray));*/

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, mainJasan, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                 /*   Snackbar.make(getView(), "Your Custom Order Placed Successfully !", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    getActivity().getSupportFragmentManager().popBackStack();*/
                   /* Fragment currentFragment = getFragmentManager().findFragmentByTag("custom_order");
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.detach(currentFragment);
                    fragmentTransaction.attach(currentFragment).addToBackStack(null);
                    fragmentTransaction.commit();*/



                   /* if(getActivity().getClass().getSimpleName().equalsIgnoreCase("Main2Activity")){
                        Main2Activity main2Activity= new Main2Activity();
                        main2Activity.setupBadge(getContext());
                    }
                  *//* Main2Activity main2Activity= new Main2Activity();
                    main2Activity.setupBadge(getContext());*//*
                    Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }*/
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
                Toast.makeText(getContext(), "Please select SEAL", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headr = new HashMap<>();
                sharedPref=getActivity().getSharedPreferences("USER_DETAILS",0);

                String output=sharedPref.getString(ACCESS_TOKEN, null);
                headr.put("Authorization","bearer "+output);
                headr.put("Content-Type", "application/json");
                return headr;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

    public void getUserDetail(String mob ){

        String url=ip+"uaa/b2b/contact-book/get/mobile?mobile="+mob+"&user="+customer_id;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public
            void onResponse(String response) {

                try {
                   JSONObject jsonObject=new JSONObject(response);
                   cust_name.setText(jsonObject.getString("name"));
                    email.setText(jsonObject.getString("email"));
                    firmname.setText(jsonObject.getString("firmName"));

                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        },new Response.ErrorListener() {
            @Override
            public
            void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;


                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 404:
                            BottomSheet.Builder builder = new BottomSheet.Builder(getContext());
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;
                        case 400:
                            BottomSheet.Builder builder1 = new BottomSheet.Builder(getContext());
                            builder1.setTitle("Sorry! No Products Available");
                            builder1.show();
                            break;
                        case 417:

                            Snackbar.make(getView(), "Sorry! No Products Available", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                           /* BottomSheet.Builder builder2 = new BottomSheet.Builder(getContext());
                            builder2.setTitle("Sorry! No Products Available");
                            builder2.show();*/
                            break;
                    }
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    //////////////////////////get All phone Numbers from contactBook/////////////////

    public void getContacts( ){

        String url=ip+"uaa/b2b/contact-book/mobile-numbers";
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public
            void onResponse(String response) {

                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        m_array.add(jsonArray.getString(i));
                    }
                    Log.e("RERERER",String.valueOf(jsonArray.length()));

                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        },new Response.ErrorListener() {
            @Override
            public
            void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;


                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 404:
                            BottomSheet.Builder builder = new BottomSheet.Builder(getContext());
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;
                        case 400:
                            BottomSheet.Builder builder1 = new BottomSheet.Builder(getContext());
                            builder1.setTitle("Sorry! No Products Available");
                            builder1.show();
                            break;
                        case 417:

                            Snackbar.make(getView(), "Sorry! No Products Available", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                           /* BottomSheet.Builder builder2 = new BottomSheet.Builder(getContext());
                            builder2.setTitle("Sorry! No Products Available");
                            builder2.show();*/
                            break;
                    }
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}



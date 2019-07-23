package com.example.compaq.b2b_application.Fragments;


import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.compaq.b2b_application.Adapters.Customize_Oder_Adapter1;
import com.example.compaq.b2b_application.Helper_classess.AppHelper;
import com.example.compaq.b2b_application.Helper_classess.VolleyMultipartRequest;
import com.example.compaq.b2b_application.Helper_classess.VolleySingleton;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.Helper_classess.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.compaq.b2b_application.Activity.Add_new_product_activity.PICK_IMAGE;
import static com.example.compaq.b2b_application.Activity.Customize_Order.pager;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip;

import static com.example.compaq.b2b_application.Fragments.products_display_fragment.item_clicked;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip_cat;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class Custom_order_frag3 extends Fragment {

    Bundle bundle;
    public SharedPreferences sharedPref;
    public SharedPreferences.Editor myEditor;
    public List<String> listDataHeader = new ArrayList<String>();
    HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
    Customize_Oder_Adapter1 listAdapter;
    public String output, wholseller_id;
    public int position = 0;
    public String SUB_URL = "";
    public String sname = "";
    SessionManagement session;
    private Button place_button,upload_reference;
    public Bundle bundle2;
    private View view;
    public String original,urldata;
    int item = 0;
    HashMap<String, String> list_id = new HashMap<String, String>();
    String all;
    private  final int PERMISSION_REQUEST_CAMERA = 0;
    final int CAMERA_PIC_REQUEST = 1337;
    private Dialog  options_dialog,myDialogue;
    private LinearLayout gallery, camera;
    private TextView yes,cancel,msg;
   private String reference_image_id;
   @Nullable @BindView(R.id.product_name)EditText p_name;
    @Nullable @BindView(R.id.date)EditText date;
    @Nullable @BindView(R.id.size)EditText size;
    @Nullable @BindView(R.id.qty)EditText qty;
    @Nullable @BindView(R.id.g_weight)EditText qwt;
    @Nullable @BindView(R.id.melting)EditText melting;
    @Nullable @BindView(R.id.sea)EditText seal;
    @Nullable @BindView(R.id.descr)EditText descri;
    @Nullable @BindView(R.id.gwt_text)TextView qty_textview;
    @Nullable @BindView(R.id.dynamic_spec)TextView dynamic_spec;
    @Nullable @BindView(R.id.reference_image)ImageView reference_image;


    public Custom_order_frag3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(view==null) {
            view = inflater.inflate(R.layout.fragment_customize_order_frag2, container, false);
            ButterKnife.bind(this,view);

            place_button=(Button)view.findViewById(R.id.place_button);
            upload_reference=(Button)view.findViewById(R.id.reference_image_btn);
            sharedPref = getContext().getSharedPreferences("USER_DETAILS", 0);
            myEditor = sharedPref.edit();
            output = sharedPref.getString(ACCESS_TOKEN, null);

            myDialogue = new Dialog(getContext());
            myDialogue.setContentView(R.layout.back_alert_dialog_layout);
            myDialogue.setCanceledOnTouchOutside(false);
            yes=myDialogue.findViewById(R.id.yes);
            cancel=myDialogue.findViewById(R.id.cancel);
            msg=(TextView) myDialogue.findViewById(R.id.popup_textview);
            msg.setText("           Do you wish to place this order?              ");
            myDialogue.getWindow()
                    .getAttributes().windowAnimations = R.style.DialogAnimation;

            wholseller_id = sharedPref.getString("userid", null);

            options_dialog = new Dialog(getActivity());
            options_dialog.setContentView(R.layout.photo_options_layout);
            gallery = (LinearLayout) options_dialog.findViewById(R.id.gallery);
            camera = (LinearLayout) options_dialog.findViewById(R.id.camera);
            options_dialog.getWindow()
                    .getAttributes().windowAnimations = R.style.DialogAnimation;

            session = new SessionManagement(getActivity());
            listAdapter = new Customize_Oder_Adapter1(getActivity(), listDataHeader, listDataChild, list_id, getFragmentManager(), view);
            place_button.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View view) {

                    if (TextUtils.isEmpty(qwt.getText().toString().trim())) {
                        qty_textview.requestFocus();
                        qty_textview.startAnimation(shakeError());

                    }
                    else {
                        myDialogue.show();
                       /* pager.setCurrentItem(4);*/
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                myDialogue.dismiss();
                            }
                        });

                        yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                               /* place_order();*/

                                placeOrder();
                                myDialogue.dismiss();
                            }
                        });
                    }

                }
            });



            upload_reference.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (upload_reference.getText().toString().equalsIgnoreCase("UPLOAD REFERENCE IMAGE")) {
                        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CAMERA)
                                == PackageManager.PERMISSION_DENIED) {
                            requestCameraPermission();
                        }
                        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_DENIED) {
                            permission();
                        } else {
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
                    }
                    if(upload_reference.getText().toString().equalsIgnoreCase("DISCARD")){
                        reference_image.setVisibility(View.GONE);
                        upload_reference.setText("UPLOAD REFERENCE IMAGE");
                    }
                }

            });

        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String id=sharedPref.getString("cust_id", null);

        if(id!=null){

            getProduct(id);
        }

    }

    private RequestQueue requestQueue;
    private void prepareListData () {
        bundle=this.getArguments();
        item_clicked=bundle.getString("Item_Clicked");
        all=bundle.getString("All");
        original=item_clicked;

        if(requestQueue==null)
        {
            requestQueue = Volley.newRequestQueue(getActivity());
        }

        urldata=ip_cat+"/category/byFirstLevelCategory/b2b/Jewellery,"+item_clicked+"?wholesaler="+wholseller_id;
        String  url=urldata.replaceAll("\\s", "");


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{

                    JSONArray ja_data = new JSONArray(response);
                    int length = ja_data.length();

                    for (int i = 0; i < length; i++) {

                        ++item;

                        JSONObject jObj = ja_data.getJSONObject(i);

                        JSONArray parent_array=jObj.getJSONArray("parent");
                        String parent=parent_array.getString(0);


                        sname = (jObj.getString("name").replaceAll("\\s", ""));
                        listDataHeader.add(sname);
                        list_id.put(sname, jObj.getString("id"));

                        position = i;

                        SUB_URL = ip_cat+"/category/byFirstLevelCategory/b2b/"+parent+","+sname+"?wholesaler="+wholseller_id;
                        SUB_URL=SUB_URL.replaceAll("\\s", "");

                        prepareSubListData(i, sname);

                       /* if(item==length){
                            final List<String> submenu = new ArrayList<String>();

                            listDataChild.put(listDataHeader.get(item), submenu);
                        }*/


                    }


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
                            BottomSheet.Builder builder = new BottomSheet.Builder(getActivity());
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;

                        case 401:

                            Toast.makeText(getActivity(),"Session Expired!",Toast.LENGTH_SHORT).show();
                            /* new Reffressh_token().getToken(MainActivity.this);*/
                            session.logoutUser(getActivity());

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

        requestQueue.add(stringRequest);
    }


    private void prepareSubListData ( final int i, final String sname1){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, SUB_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONArray ja_data = new JSONArray(response);
                    /*if (ja_data.length() == 0) {
                        final List<String> submenu = new ArrayList<String>();
                        submenu.add("All  "+sname1);

                        listDataChild.put(listDataHeader.get(i), submenu);



                    } else {*/
                    final List<String> submenu = new ArrayList<String>();

                    for (int j = 0; j < ja_data.length(); j++) {

                        JSONObject jObj = ja_data.getJSONObject(j);

                        String subnames = jObj.getString("name");
                        list_id.put(subnames, jObj.getString("id"));
                        submenu.add(subnames);
                    }
                    listDataChild.put(listDataHeader.get(i), submenu);


                    // Header, Child data



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
                params.put("Authorization","bearer "+output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };




        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public void getProduct( String id){

        String url = ip+"gate/b2b/catalog/api/v1/product/"+id;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url   , new Response.Listener<String>() {
            @Override
            public
            void onResponse(String response) {
                try {

                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject pro_object=jsonObject.getJSONObject("resourceSupport");
                    p_name.setText(pro_object.getString("name"));

                    Calendar cal = GregorianCalendar.getInstance();
                  try {
                      cal.add(Calendar.DAY_OF_YEAR, +pro_object.getInt("requiredDayesToDeliver"));
                      Date seven_days = cal.getTime();

                      SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                      String formattedDate = df.format(seven_days);

                      date.setText(formattedDate);
                  }
                  catch (Exception e){
                      e.printStackTrace();
                      date.setText("");
                  }




                   JSONArray spec_arra=pro_object.getJSONArray("specification");
                   for(int i=0;i<spec_arra.length();i++){
                       JSONObject jsonObject1=spec_arra.getJSONObject(i);
                       if(jsonObject1.getString("heading").equalsIgnoreCase("Product Details")){

                           JSONArray jsonArray=jsonObject1.getJSONArray("attributes");
                           for(int j=0;j<jsonArray.length();j++){
                               JSONObject jsonObject2=jsonArray.getJSONObject(j);
                               if(jsonObject2.getString("key").equalsIgnoreCase("Gross Weight (gms)")){
                                   JSONArray jsonArray1=jsonObject2.getJSONArray("values");
                                   qwt.setText(jsonArray1.getString(0));
                               }
                               if(jsonObject2.getString("key").equalsIgnoreCase("Size"))
                               {
                                   JSONArray jsonArray1=jsonObject2.getJSONArray("values");
                                   dynamic_spec.setText("Size");
                                   size.setText(jsonArray1.getString(0));
                               }
                               if(jsonObject2.getString("key").equalsIgnoreCase("Length"))
                               {
                                   JSONArray jsonArray1=jsonObject2.getJSONArray("values");
                                   dynamic_spec.setText("Length");
                                   size.setText(jsonArray1.getString(0));
                               }
                           }
                       }
                   }
                    qty.setText("1");


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
    ////////////////////////permission //////////////////////////
    private void requestCameraPermission() {

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CAMERA);

    }
    private void permission() {



        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CAMERA);

    }
    public void cameraOpen() {

        try {
/*
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            final String dir =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/Folder/";
            File newdir = new File(dir);
            newdir.mkdirs();
            String file = dir+ DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString()+".jpg";
            File newfile = new File(file);
            try {
                newfile.createNewFile();
            } catch (IOException e) {}

            Uri outputFileUri = Uri.fromFile(newfile);

            filePaths.add(outputFileUri);*/

            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);



            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }


    ///////////////////
    public void gaallery_open() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // ******** code for crop image
        i.putExtra("crop", false);
        i.putExtra("aspectX", 800);
        i.putExtra("aspectY", 800);
        i.putExtra("outputX", 800);
        i.putExtra("outputY", 800);


        try {

            i.putExtra("return-data", true);
            startActivityForResult(
                    Intent.createChooser(i, "Select Picture"), PICK_IMAGE);
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
        }


    }

    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(600);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {

        if(data!=null) {
            if (requestCode == CAMERA_PIC_REQUEST) {
                try {
                    Bitmap image = (Bitmap) data.getExtras().get("data");
                    Bitmap bitmap = Bitmap.createScaledBitmap(image, 800, 800, true);
                    reference_image.setVisibility(View.VISIBLE);
                    upload_reference.setText("DISCARD");
                    reference_image.setImageBitmap(bitmap);
                       uploadReferenceImage();


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
                    reference_image.setVisibility(View.VISIBLE);
                    upload_reference.setText("DISCARD");
                    reference_image.setImageBitmap(bitmap);
                    Log.e("DVDVDV","bitmap");



                    uploadReferenceImage();

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
                    Log.e("DVDVDV","ILLLE");
                        e.printStackTrace();
                }

            } /*else {
            Toast.makeText(getActivity(), "Try Again!!", Toast.LENGTH_SHORT).show();
        }*/
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void placeOrder() {

        String image_id=sharedPref.getString("cust_image_id", null);
       String  customer_id = sharedPref.getString("userid", null);

        JSONObject mainJasan= new JSONObject();
        String  url=ip+"gate/b2b/order/api/v1/order/add";
        JSONObject json1= new JSONObject();
        final JSONArray items_jsonArray=new JSONArray();
        try {
            json1.put("name",p_name.getText().toString());
            json1.put("quantity",qty.getText().toString());
            json1.put("paymentStatus","PENDING");
            json1.put("seller",wholseller_id);
            json1.put("seal",seal.getText().toString());
            json1.put("expectedDeliveryDate",date.getText().toString());
            json1.put("grossWeight",qwt.getText().toString());
            json1.put("melting",melting.getText().toString());
            json1.put("productImage","https://server.mrkzevar.com/gate/b2b/catalog/api/v1/assets/image/"+image_id);
               json1.put("netWeight",qwt.getText().toString());
           /* json1.put("totweight",weight.getText().toString());*/


            items_jsonArray.put(json1);
            mainJasan.put("items",items_jsonArray);
            mainJasan.put("customer",customer_id);
            mainJasan.put("paymentStatus","PENDING");
            mainJasan.put("orderType","ONLINECUSTOM_ORDER");


            Log.e("OBJECT Status", mainJasan.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
            Map<String, String> params = new HashMap<>();
            params.put("customer", customer_id);
            params.put("items", String.valueOf(items_jsonArray));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, mainJasan, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                     pager.setCurrentItem(4);


                 /*   Snackbar.make(getView(), "Your Custom Order Placed Successfully !", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();*/

                }

                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;


                if(response != null && response.data != null){
                    switch(response.statusCode) {
                        case 404:

                            Snackbar.make(getView(), "Sorry! could't reach server", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            break;
                        case 400:

                            Snackbar.make(getView(), "Sorry! No Products Available", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            break;
                        case 417:

                            Snackbar.make(getView(), "Sorry! Something went wrong", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            break;
                    }
                }
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

    public void uploadReferenceImage() {
        // loading or check internet connection or something...
        // ... then
        String url = ip+"image/save";
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                try {

                    reference_image_id=resultResponse;
                    Log.e("Unexpected", resultResponse);

                    Log.e("RESULTT", response.toString());

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
                params.put("Content-Type", "image/png");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView

                params.put("file", new VolleyMultipartRequest.DataPart("id", AppHelper.getFileDataFromDrawable(getActivity(),reference_image.getDrawable()), "image/jpeg"));

                return params;
            }


        };


        VolleySingleton.getInstance(getActivity()).addToRequestQueue(multipartRequest);
    }
}

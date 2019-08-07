package com.example.compaq.b2b_application.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
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
import android.os.Environment;
import android.print.pdf.PrintedPdfDocument;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Helper_classess.AppHelper;
import com.example.compaq.b2b_application.Helper_classess.VolleyMultipartRequest;
import com.example.compaq.b2b_application.Helper_classess.VolleySingleton;
import com.example.compaq.b2b_application.R;
import com.rilixtech.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.android.volley.VolleyLog.TAG;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;


public class Company_profile_Fragment extends Fragment {

    public EditText company_name,gst_text,cin_text,description;
    Button gst_button,cin_button,update;
    private JSONObject company_details,websiteSetting;
    private JSONArray meltingSealing,userClass,wishList,brands,address;
    private int defaultAddressID=0,id=0,com_id=0,storeCount=0;
    TextView  logo_text;
    ArrayList<String>logo_list;
    ImageView cancle_imgbtn;





    private String Acess_Token,adharNo="",pan="",role="",password="",adharDocumentId="",panDocumentId="",user_fname="",user_lname=""
            ,email="",teli_phone="",cin="",cinDocumentId="",createdOn=null,com_description="",fax=null,fcrn=null,fcrnDocumentId=null,fllpin=null,
             fllpinDocumentId=null,gstDocumentId="",gstin="",llpin=null,llpinDocumentId=null,logoImageId="",com_name="",product="",supportEmail=null,supportMobile=null
            ,termsFileId="",updatedOn=null,gst_image_type="IMAGE",cin_image_type="IMAGE";

    Dialog img_dialog;
    ImageView imageView,logo_img;
    private SharedPreferences sharedPref;
    private Boolean verified=false,verifyRequest=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_company_profile_, container, false);
        sharedPref =getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        logo_list=new ArrayList<>();
        Acess_Token=sharedPref.getString(ACCESS_TOKEN, null);
        company_name=(EditText)view.findViewById(R.id.company_nam);
        gst_text=(EditText)view.findViewById(R.id.gst_text);
        cin_text=(EditText)view.findViewById(R.id.edit_cin);
        description=(EditText)view.findViewById(R.id.edit_Description);
        gst_button=(Button)view.findViewById(R.id.upload_gst_btn);
        cin_button=(Button)view.findViewById(R.id.upload_cin_btn);
        logo_img=(ImageView)view.findViewById(R.id.company_logo);

        logo_text=(TextView)view.findViewById(R.id.logo_text);
        logo_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog(3000);
            }
        });

        logo_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imageid="35f05e0e-56fb-4676-9819-381da000696b";
                Log.d("drawable..",logo_img.getDrawable().toString());
                if(!logoImageId.equals(null) && !logoImageId.equals("")){
                    img_dialog = new Dialog(getContext());
                    img_dialog.setContentView(R.layout.doc_inage);
                    imageView = (ImageView) img_dialog.findViewById(R.id.doc_image);
                    String getHref = ip1 + "/b2b/api/v1/user/image/get/" + logoImageId + "";
                    Log.d("href...", getHref);
                    Glide.with(getActivity().getApplicationContext()).load(getHref).into(imageView);
                    img_dialog.show();
                    cancle_imgbtn=(ImageView)img_dialog.findViewById(R.id.cancle_img) ;
                    cancle_imgbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(img_dialog.isShowing()==true){
                                img_dialog.dismiss();
                            }

                        }
                    });
                }
                else {
                    showPictureDialog(3000);
                }

            }
        });
        gst_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!gstDocumentId.equals(null) && !gstDocumentId.equals("")) {

                    try {


                        if (gst_image_type.equals("PDF")) {
                            String getHref2 = ip1 + "/b2b/api/v1/user/image/getfile/" + gstDocumentId + "";
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getHref2));
                            startActivity(browserIntent);
                        } else {
                            img_dialog = new Dialog(getContext());
                            img_dialog.setContentView(R.layout.doc_inage);
                            imageView = (ImageView) img_dialog.findViewById(R.id.doc_image);
                            String getHref = ip1 + "/b2b/api/v1/user/image/get/" + gstDocumentId + "";
                            Log.d("href...", getHref);
                            Glide.with(getActivity().getApplicationContext()).load(getHref).into(imageView);
                            img_dialog.show();
                            cancle_imgbtn=(ImageView)img_dialog.findViewById(R.id.cancle_img) ;
                            cancle_imgbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(img_dialog.isShowing()==true){
                                        img_dialog.dismiss();
                                    }

                                }
                            });
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else {
                    showPictureDialog(1000);
                }

            }
        });
        cin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!cinDocumentId.equals(null) && !cinDocumentId.equals("")) {
                    try {

                        if (cin_image_type.equals("PDF")) {
                            String getHref2 = ip1 + "/b2b/api/v1/user/image/getfile/" + cinDocumentId + "";
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getHref2));
                            startActivity(browserIntent);
                        } else {
                            img_dialog = new Dialog(getContext());
                            img_dialog.setContentView(R.layout.doc_inage);
                            imageView = (ImageView) img_dialog.findViewById(R.id.doc_image);
                            String getHref1 = ip1 + "/b2b/api/v1/user/image/get/" + cinDocumentId + "";
                            Log.d("href...", getHref1);
                            Glide.with(getActivity().getApplicationContext()).load(getHref1).into(imageView);
                            img_dialog.show();
                            cancle_imgbtn=(ImageView)img_dialog.findViewById(R.id.cancle_img) ;
                            cancle_imgbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(img_dialog.isShowing()==true){
                                        img_dialog.dismiss();
                                    }

                                }
                            });
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
                else {
                    showPictureDialog(5000);
                }



            }
        });



        update=(Button)view.findViewById(R.id.company_save_process);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(logo_list.size()>1) {
                    for (int i = 0; i < logo_list.size() - 1; i++) {
                        if(!logo_list.get(i).equals("")&& !logo_list.get(i).equals(null)) {
                            deletelogo(logo_list.get(i));
                        }
                        if (i + 1 == logo_list.size() - 1) {
                            updatecompany();
                        }
                    }
                }
                else {
                    updatecompany();
                }

            }
        });

        setHasOptionsMenu(true);
        userInformation();
        return  view;
    }

///////////////////////////////////////////////////get user info/////////////////////////////////////////
    public void userInformation ( ){

        String url=ip1+"/b2b/api/v1/user/info";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.d("response..",jObj.toString());
                    address=jObj.getJSONArray("address");
                    user_fname=jObj.getString("firstName");
                    user_lname=jObj.getString("lastName");
                    email=jObj.getString("email");
                    teli_phone=jObj.getString("mobileNumber");
                    id=jObj.getInt("id");
                    adharNo=jObj.getString("adharNo");
                    pan=jObj.getString("pan");
                    role=jObj.getString("role");
                    password=jObj.getString("password");
                    adharDocumentId=jObj.getString("adharDocumentId");
                    panDocumentId=jObj.getString("panDocumentId");
                    defaultAddressID=jObj.getInt("defaultAddressID");
                    websiteSetting=jObj.getJSONObject("websiteSetting");
                    meltingSealing=jObj.getJSONArray("meltingSealing");
                    userClass=jObj.getJSONArray("userClass");
                    wishList=jObj.getJSONArray("wishList");
                    verified=jObj.getBoolean("verified");
                    verifyRequest=jObj.getBoolean("verifyRequest");
                    company_details=jObj.getJSONObject("company");

                    Log.d("company.....",company_details.toString());
                    brands=company_details.getJSONArray("brands");
                    cin=company_details.getString("cin");
                    cinDocumentId=company_details.getString("cinDocumentId");
                    createdOn=company_details.getString("createdOn");
                    com_description=company_details.getString("description");
                    fax=company_details.getString("fax");
                    fcrn=company_details.getString("fcrn");
                    fcrnDocumentId=company_details.getString("fcrnDocumentId");
                    fllpin=company_details.getString("fllpin");
                    fllpinDocumentId=company_details.getString("fllpinDocumentId");
                    gstin=company_details.getString("gstin");
                    gstDocumentId=company_details.getString("gstDocumentId");
                    com_id=company_details.getInt("id");
                    llpin=company_details.getString("llpin");
                    llpinDocumentId=company_details.getString("llpinDocumentId");
                    logoImageId=company_details.getString("logoImageId");
                    com_name=company_details.getString("name");
                    product=company_details.getString("product");
                    storeCount=company_details.getInt("storeCount");
                    supportEmail=company_details.getString("supportEmail");
                    supportMobile=company_details.getString("supportMobile");
                    termsFileId=company_details.getString("termsFileId");
                    updatedOn=company_details.getString("updatedOn");


                    company_name.setText(com_name);
                    gst_text.setText(gstin);
                    cin_text.setText(cin);
                    description.setText(com_description);

                    if(!logoImageId.equals("") && !logoImageId.equals(null)){
                        String getHref = ip1 + "/b2b/api/v1/user/image/get/" + logoImageId + "";
                        Log.d("href...", getHref);
                        Glide.with(getActivity().getApplicationContext()).load(getHref).into(logo_img);
                        logo_list.add(logoImageId);
                    }else {
                        Bitmap icon = BitmapFactory.decodeResource(getActivity().getResources(),
                                R.drawable.yourlogo);
                        Upload_image(0000,icon);
                    }
                    if(!gstDocumentId.equals("")&&!gstDocumentId.equals(null)){
                        imageData("GST",gstDocumentId);
                        gst_button.setText("view gstin");
                    }
                    if(!cinDocumentId.equals("")&&!cinDocumentId.equals(null)){
                        imageData("CIN",cinDocumentId);
                        cin_button.setText("view cin");
                    }




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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////
    private void showPictureDialog(final int code){
        android.app.AlertDialog.Builder pictureDialog = new android.app.AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Option");


        String[] pictureDialogItems = {
                " Gallery",
                " Camera",
                " Document"};
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
                                else if(code==3000){
                                    choosePhotoFromGallary(3020);
                                }
                                break;
                            case 1:
                                if(code==1000) {
                                    takePhotoFromCamera(1010);
                                }
                                else if(code==5000){
                                    takePhotoFromCamera(5010);
                                }
                                else if(code==3000){
                                    takePhotoFromCamera(3010);
                                }
                                break;
                            case 3:
                                if(code==1000) {
                                  showFileChooser(1030);
                                }
                                else if(code==1000){
                                    showFileChooser(5030);
                                }

                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void showFileChooser(int resCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"), resCode);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getActivity(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
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
        cameraIntent.setType("image/*");
        cameraIntent.setAction(Intent.ACTION_GET_CONTENT);


        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            if(code==1020) {
                startActivityForResult(Intent.createChooser( cameraIntent, "Select File"),code);
            }
            else if(code==5020){
                startActivityForResult(Intent.createChooser( cameraIntent, "Select File"),code);
            }
            else if(code==3020){
                startActivityForResult(Intent.createChooser( cameraIntent, "Select File"),code);
            }
        }
        /*Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        cameraIntent.putExtra("crop", "true");
        cameraIntent.putExtra("aspectX", 100);
        cameraIntent.putExtra("aspectY", 100);
        cameraIntent.putExtra("outputX", 256);
        cameraIntent.putExtra("outputY", 356);
        cameraIntent.setType("image/*");*/
       /* if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            if(code==1020) {
                startActivityForResult(cameraIntent, code);
            }
            else if(code==5020){
                startActivityForResult(cameraIntent, code);
            }
            else if(code==3020){
                startActivityForResult(cameraIntent, code);
            }
        }*/
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
                    Bitmap bm=null;
                    Bitmap bitimage = null;
                    if (data != null) {
                        try {
                            bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                            bitimage = getResizedBitmap(bm, 1024);
                            //logo_img.setImageBitmap(bitimage);
                            Upload_image(requestCode,bitimage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                  /*  Uri returnUri = data.getData();
                    Bitmap bitmapImage ;
                    Bitmap bitimage = null;
                    try {
                        bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
                        bitimage = getResizedBitmap(bitmapImage, 400);

                        //imageView.setImageBitmap(bitimage);
                        imagePick( requestCode,returnUri);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                }
                break;
            case 5020:
                if (resultCode == RESULT_OK) {
                    Bitmap bm=null;
                    Bitmap bitimage = null;
                    if (data != null) {
                        try {
                            bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                            bitimage = getResizedBitmap(bm, 1024);
                            //logo_img.setImageBitmap(bitimage);
                            Upload_image(requestCode,bitimage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    /* Uri returnUri = data.getData();
                    Bitmap bitmapImage ;
                    Bitmap bitimage = null;
                    try {
                        bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
                        bitimage = getResizedBitmap(bitmapImage, 400);

                        imagePick(requestCode, returnUri);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                }
                break;

            case 3020:
                if (resultCode == RESULT_OK) {
                    Bitmap bm=null;
                    Bitmap bitimage = null;
                    if (data != null) {
                        try {
                            bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                            bitimage = getResizedBitmap(bm, 150);
                            logo_img.setImageBitmap(bitimage);
                            Upload_image(requestCode,bitimage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }



                   /* Uri returnUri = data.getData();
                    Bitmap bitmapImage ;
                    Bitmap bitimage = null;
                    try {
                        bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
                        bitimage = getResizedBitmap(bitmapImage, 400);

                        logo_img.setImageBitmap(bitimage);
                        imagePick( requestCode,returnUri);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*/
                }

                break;
            case 1010:
                if (resultCode == RESULT_OK) {


                    Bitmap bitmapImage ;

                    try {
                        bitmapImage =(Bitmap) data.getExtras().get("data");
                        Bitmap bitimage = getResizedBitmap(bitmapImage, 1024);

                        //imageView.setImageBitmap(bitimage);
                       // Upload_image(requestCode,bitimage);
                        // imagePick( requestCode,returnUri);
                        Uri returnUri = getImageUri(getContext(),bitimage);
                        imagePick( requestCode,returnUri);



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
                        Bitmap bitimage = getResizedBitmap(bitmapImage, 1024);

                        //Upload_image(requestCode,bitimage);
                        Uri returnUri = getImageUri(getContext(),bitimage);
                        imagePick( requestCode,returnUri);



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 3010:
                if (resultCode == RESULT_OK) {


                    Bitmap bitmapImage ;
                     Bitmap bitimage = null;
                    try {
                        bitmapImage =(Bitmap) data.getExtras().get("data");
                        bitimage = getResizedBitmap(bitmapImage, 150);

                        Upload_image(requestCode,bitimage);
                        logo_img.setImageBitmap(bitimage);
                       // Uri returnUri = getImageUri(getContext(),bitimage);
                        //imagePick( requestCode,returnUri);



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            case 5030:
                if (resultCode == RESULT_OK) {

                    Uri uri = data.getData();
                    File myFile = new File(uri.toString());
                    String path = myFile.getAbsolutePath();


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
        inImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
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


            Bitmap bitmap = Bitmap.createScaledBitmap((BitmapFactory.decodeFile(picturePath)), 1024, 1024, true);
            /*if(code==5020 ||code==5010) {
                File f = new File(picturePath);
                String imageName = f.getName();
                Log.d("image name....", imageName);
                gst_file.setText(imageName);
            }*/

/*
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);

            byte[] imageInByte = byteArrayOutputStream.toByteArray();
            long lengthbmp = imageInByte.length;
            Log.d("Image Size", String.valueOf(lengthbmp));*/


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
                        gstDocumentId = resultResponse;
                        Log.i("Unexpected", resultResponse);
                        gst_button.setText("view gstin");
                    }
                    else if(code==5020||code==5010){
                       cinDocumentId=resultResponse;
                        Log.i("Unexpected", resultResponse);
                        cin_button.setText("view cin");
                    }
                    else if(code==3020 || code==3010){
                        logoImageId=resultResponse;
                        Log.i("Unexpected", resultResponse);
                        logo_list.add(resultResponse);
                        Toast.makeText(getActivity(), "photo uploaded.."+resultResponse,
                                Toast.LENGTH_SHORT).show();

                    }
                    else if(code==0000){

                        logoImageId=resultResponse;
                        logo_list.add(resultResponse);
                        String getHref = ip1 + "/b2b/api/v1/user/image/get/" + logoImageId + "";
                        Log.d("href...", getHref);
                        Glide.with(getActivity().getApplicationContext()).load(getHref).into(logo_img);
                    }


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
                Toast.makeText(getActivity(), "photo uploaded.."+errorMessage,
                        Toast.LENGTH_LONG).show();
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
///////////////////////////////////////////////////////delete unwanted logo////////////////////////////////
public void deletelogo(final String img_id){
    String url=ip1+"/b2b/api/v1/user/image/delete/"+img_id+"";
    StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {


        @Override
        public void onResponse(String response) {
            try {
                logo_list.remove(img_id);

                Log.d("response..",response.toString());


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
            params.put("Authorization","bearer "+Acess_Token);
            params.put("Content-Type", "application/json");
            return params;
        }
    };

    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
    requestQueue.add(stringRequest);


}



    /////////////////////////////////////////////////company data post///////////////////////////////////////

    public void updatecompany() {

        JSONObject mainJasan= new JSONObject();

        String   url=ip+"uaa/b2b/api/v1/user/update";

        JSONObject json1= new JSONObject();


        try {

            mainJasan.put("address",address);
            mainJasan.put("adharDocumentId",adharDocumentId);
            mainJasan.put("adharNo",adharNo);
            mainJasan.put("defaultAddressID",defaultAddressID);
            mainJasan.put("email",email);
            mainJasan.put("firstName",user_fname);
            mainJasan.put("lastName",user_lname);
            mainJasan.put("id",id);
            mainJasan.put("meltingSealing",meltingSealing);
            mainJasan.put("mobileNumber",teli_phone);
            mainJasan.put("pan",pan);
            mainJasan.put("panDocumentId",panDocumentId);
            mainJasan.put("password",password);
            mainJasan.put("role",role);
            mainJasan.put("userClass",userClass);
            mainJasan.put("company",company_details);
            mainJasan.put("verified",verified);
            mainJasan.put("verifyRequest",verifyRequest);
            mainJasan.put("websiteSetting",websiteSetting);
            mainJasan.put("wishList",wishList);

            json1.put("cin",cin_text.getText().toString());
            json1.put("cinDocumentId",cinDocumentId);
            json1.put("brands",brands);
            json1.put("createdOn",createdOn);
            json1.put("description",description.getText().toString());
            json1.put("fax",fax);
            json1.put("fcrn",fcrn);
            json1.put("fcrnDocumentId",fcrnDocumentId);
            json1.put("fllpin",fllpin);
            json1.put("fllpinDocumentId",fllpinDocumentId);
            json1.put("gstDocumentId",gstDocumentId);
            json1.put("gstin",gstin);
            json1.put("id",id);
            json1.put("llpin",llpin);
            json1.put("llpinDocumentId",llpinDocumentId);
            json1.put("logoImageId",logoImageId);
            json1.put("name",company_name.getText().toString());
            json1.put("product",product);
            json1.put("storeCount",storeCount);
            json1.put("supportEmail",supportEmail);
            json1.put("supportMobile",supportMobile);
            json1.put("termsFileId",termsFileId);
            json1.put("updatedOn",updatedOn);
            mainJasan.put("company",json1);

            Log.d("response..",mainJasan.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, mainJasan, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    Log.d("response...",response.toString());
                    company_name.setEnabled(false);
                    company_name.setClickable(false);
                    company_name.setFocusableInTouchMode(false);

                    cin_text.setEnabled(false);
                    cin_text.setClickable(false);
                    cin_text.setFocusableInTouchMode(false);
                    description.setEnabled(false);
                    description.setClickable(false);
                    description.setFocusableInTouchMode(false);
                    setHasOptionsMenu(true);
                    Toast toast= Toast.makeText(getActivity(), " Company Information Updated Sucessfully", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();

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

//////////////////////////////////////////////////////image of gst ////////////////////////////////////////
    public void imageData(final String call, String img_id){
        String url=ip1+"/b2b/api/v1/user/image/imageDetail/"+img_id+"";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject imgObj = new JSONObject(response);
                    Log.d("response..",imgObj.toString());
                    String type=imgObj.getString("fileType");
                    if(call.equals("GST")&&type.equals("application/pdf")){
                      gst_image_type="PDF";
                    }
                    else if (call.equals("CIN")&&type.equals("application/pdf")){
                        cin_image_type="PDF";
                    }






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


    ////////////////////////////////////edit button show and hide in toolber/////////////////////////////////////////
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
                company_name.setEnabled(true);
                company_name.setClickable(true);
                company_name.setFocusableInTouchMode(true);
                if(cin.equals(null)|| cin.equals("")) {
                    cin_text.setEnabled(true);
                    cin_text.setClickable(true);
                    cin_text.setFocusableInTouchMode(true);
                }

                description.setEnabled(true);
                description.setClickable(true);
                description.setFocusableInTouchMode(true);
                setHasOptionsMenu(false);

                return true;


        }
        return  false;
    }
}

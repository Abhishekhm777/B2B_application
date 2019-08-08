package com.example.compaq.b2b_application.Fragments;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
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
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

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
import com.example.compaq.b2b_application.Adapters.Slider_adapterView;
import com.example.compaq.b2b_application.Helper_classess.AppHelper;
import com.example.compaq.b2b_application.Helper_classess.VolleyMultipartRequest;
import com.example.compaq.b2b_application.Helper_classess.VolleySingleton;
import com.example.compaq.b2b_application.R;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Fragments.Custom_order.PICK_IMAGE;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;


public class ViewSettingsFragment extends Fragment {

    private SharedPreferences sharedPref;
    private String Acess_Token=null,slider_id=null,added_id="";
    ArrayList<String>slider_list,slider_id_array;
    ImageButton addButton;
    JSONArray idArray;
    public  final int PICK_IMAGES = 1;
    private int id=0;
    Slider_adapterView slider_adapterView;

    RecyclerView slider_recycler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_view_settings, container, false);
        sharedPref=getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Acess_Token=sharedPref.getString(ACCESS_TOKEN, null);
        slider_list=new ArrayList<>();
        slider_id_array=new ArrayList<>();
        //idArray=new JSONArray();

        slider_recycler=(RecyclerView)view.findViewById(R.id.slider_recyclerview);
        addButton=(ImageButton)view.findViewById(R.id.add_slider_btn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    gaallery_open();
                }

            }
        });

        final GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 1);
        slider_recycler.setLayoutManager(mGridLayoutManager);
        userInformation();

        return  view;
    }

    ////////////////////////////////////image add into slider//////////////////////////
    public void gaallery_open() {

        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // ******** code for crop image
       /* i.putExtra("crop", "true");
        i.putExtra("aspectX", 800);
        i.putExtra("aspectY", 800);
        i.putExtra("outputX", 800);
        i.putExtra("outputY", 800);*/
        i.setType("*/*");

        try {

            i.putExtra("return-data", true);
            startActivityForResult(
                    Intent.createChooser(i, "Select Picture"), PICK_IMAGES);
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if(requestCode==PICK_IMAGES) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);

                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);


                Bitmap bitmap = Bitmap.createScaledBitmap((BitmapFactory.decodeFile(picturePath)), 800, 800, true);


                String fileNameSegments[] = picturePath.split("/");
                String fileName = fileNameSegments[fileNameSegments.length - 1];
                Log.e("FILE", fileName);
                Upload_image(bitmap, fileName);


            } catch (NullPointerException e) {

            }
        }
             /*else {
            Toast.makeText(getActivity(), "Try Again!!", Toast.LENGTH_SHORT).show();
        }*/
        super.onActivityResult(requestCode, resultCode, data);
    }

    ////////////////////////////////////////////
    public void Upload_image(final Bitmap bitmap,final String file_name) {


        String url = "https://server.mrkzevar.com/gate/b2b/catalog/api/v1/assets/secure/add";
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);

                try {

                    added_id=resultResponse;



                    Log.i("Unexpected1", added_id);

                    String n= added_id.substring(2,added_id.length()-2);
                    idArray.put(n);
                    update_image(idArray);
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
                Log.e("TOKEN",Acess_Token);
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "bearer "+Acess_Token);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView

                params.put("file", new DataPart(file_name, AppHelper.getFileDataFromDrawable(getActivity(),bitmap), "image/png"));

                return params;
            }
        };


        VolleySingleton.getInstance(getContext()).addToRequestQueue(multipartRequest);
    }
//////////////////////////////////////////////////update image////////////////////

    public void update_image(JSONArray image_array) {



        JSONObject mainJasan= new JSONObject();

        try {
            mainJasan.put("id",slider_id);
            mainJasan.put("wholesaler",id);
            mainJasan.put("imageGridFsID", image_array);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("image",mainJasan.toString());
        String url=ip+"gate/b2b/catalog/api/v1/slide/secure/update";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, mainJasan, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {


                    slider_list.clear();
                    slider_id_array.clear();

                    getSlider();


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


    ///////////////////////////////take info////////////////////////////////////
    public void userInformation ( ){

        String url=ip1+"/b2b/api/v1/user/info";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);

                    id=jObj.getInt("id");
                    getSlider();


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

    /////////////////////////////////////////////get Slider///////////////////////////////
    public void getSlider(){
        String url=ip+"gate/b2b/catalog/api/v1/slide/get/"+id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject sliderObj = new JSONObject(response);
                    slider_id=sliderObj.getString("id");

                    idArray=sliderObj.getJSONArray("imageGridFsID");
                    if(idArray.length()>0){
                        JSONObject linkObj=sliderObj.getJSONObject("_links");
                        if (linkObj.has("imageURl")) {

                            JSONObject dataObject = linkObj.optJSONObject("imageURl");

                            if (dataObject != null) {
                                slider_list.add(dataObject.getString("href"));
                                slider_id_array.add(idArray.getString(0));



                            } else {

                                //JSONArray array = json.optJSONArray("imageURl");
                                JSONArray slider_img_array =linkObj.optJSONArray("imageURl");
                                for (int i=0;i<slider_img_array.length();i++){
                                    JSONObject jsonObject=slider_img_array.getJSONObject(i);
                                    slider_list.add(jsonObject.getString("href"));
                                    slider_id_array.add(idArray.getString(i));

                                }
                                //Do things with array
                            }
                        }



                    }
                    slider_adapterView=new Slider_adapterView (getActivity(),slider_list,slider_id,slider_id_array);
                    slider_recycler.setAdapter(slider_adapterView);
                    Log.d("slider..",slider_list.size()+"");



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



}

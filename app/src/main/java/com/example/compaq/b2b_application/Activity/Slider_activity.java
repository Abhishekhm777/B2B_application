package com.example.compaq.b2b_application.Activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.Slider_adapter;
import com.example.compaq.b2b_application.Helper_classess.AppHelper;
import com.example.compaq.b2b_application.Model.Slider_model;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.Helper_classess.VolleyMultipartRequest;
import com.example.compaq.b2b_application.Helper_classess.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Fragments.Custom_order.PICK_IMAGE;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;

public class Slider_activity extends AppCompatActivity {
    public ArrayList<Slider_model> productlist;
    public RecyclerView recyclerView;
    SharedPreferences pref;
    String output, user_id,new_image_id,hosa_image;
    Button slider_add;
    public Slider_adapter adapter;
 JSONArray image_array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pref = getSharedPreferences("USER_DETAILS", 0);
        output = pref.getString(ACCESS_TOKEN, null);
        user_id = pref.getString("userid", null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        slider_add = (Button) findViewById(R.id.slider_add_button);
        slider_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gaallery_open();
            }
        });



        productlist = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.slider_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setHasFixedSize(true);
        getSlider();

    }

    public void getSlider() {


        String url = ip + "gate/b2b/catalog/api/v1/slide/get/" + user_id;
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                      new_image_id=response.getString("id");
                      image_array=response.getJSONArray("imageGridFsID");

                    JSONObject jsonObject = response.getJSONObject("_links");


                    Object intervention = jsonObject.get("imageURl");
                    if(intervention instanceof  JSONArray) {

                        JSONArray jsonArray= (JSONArray)intervention;

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                            productlist.add(new Slider_model(jsonObject1.getString("href"), image_array.get(i).toString(), new_image_id));

                        }
                    }
                    else {
                        JSONObject jsonObject1=(JSONObject)intervention;

                        productlist.add(new Slider_model(jsonObject1.getString("href"), image_array.get(0).toString(), new_image_id));

                    }


                    adapter = new Slider_adapter(Slider_activity.this, productlist);


                    recyclerView.setAdapter(adapter);


                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {


            }

        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "bearer " + output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

        };

        RequestQueue rQueue = Volley.newRequestQueue(getApplicationContext());
        rQueue.add(objectRequest);

    }

    public void gaallery_open() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // ******** code for crop image
        i.putExtra("crop", "true");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {

        try {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getApplicationContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);

            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);


            Bitmap bitmap = Bitmap.createScaledBitmap((BitmapFactory.decodeFile(picturePath)), 800, 800, false);


            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);

            byte[] imageInByte = byteArrayOutputStream.toByteArray();
            long lengthbmp = imageInByte.length;
            Log.d("Image Size", String.valueOf(lengthbmp));



            String fileNameSegments[] = picturePath.split("/");
            String fileName = fileNameSegments[fileNameSegments.length - 1];
            Log.e("FILE", fileName);
            Upload_image(bitmap,fileName);



            /* Upload_image(byteArrayOutputStream);*/


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
        } catch (NullPointerException e) {

        }

             /*else {
            Toast.makeText(getActivity(), "Try Again!!", Toast.LENGTH_SHORT).show();
        }*/
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void Upload_image(final Bitmap bitmap,final String file_name) {


        String url = "https://server.mrkzevar.com/gate/b2b/catalog/api/v1/assets/secure/add";
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);

                try {

                     hosa_image=resultResponse;



                    Log.i("Unexpected1", hosa_image);

                    String n= hosa_image.substring(2,hosa_image.length()-2);
                    image_array.put(n);
                    update_image(image_array);
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
               Log.e("TOKEN",output);
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "bearer "+output);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView

                params.put("file", new DataPart(file_name, AppHelper.getFileDataFromDrawable(getApplicationContext(),bitmap), "image/png"));

                return params;
            }
        };


        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(multipartRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // click on 'up' button in the action bar, handle it here
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed()
    {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        }
        else {

            super.onBackPressed();
        }
    }
    public void update_image(JSONArray image_array) {



        JSONObject mainJasan= new JSONObject();

        try {
            mainJasan.put("id",new_image_id);
            mainJasan.put("wholesaler",user_id);
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


                    productlist.clear();
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
                headr.put("Authorization","bearer "+output);
                headr.put("Content-Type", "application/json");
                return headr;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
}



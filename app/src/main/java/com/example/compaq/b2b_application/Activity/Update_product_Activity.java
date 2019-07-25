package com.example.compaq.b2b_application.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.Adapter_spesification;
import com.example.compaq.b2b_application.Adapters.Update_product_activity_ad;
import com.example.compaq.b2b_application.Adapters.User_class_Adapter;
import com.example.compaq.b2b_application.Helper_classess.AppHelper;
import com.example.compaq.b2b_application.Model.Top_model;
import com.example.compaq.b2b_application.Model.Update_product_model;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.Helper_classess.VolleyMultipartRequest;
import com.example.compaq.b2b_application.Helper_classess.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import timber.log.Timber;


import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip_cat;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;

public class Update_product_Activity extends AppCompatActivity {
    Toolbar toolbar;
    public List<String> product;
    public List<String> status;
    public List<String> type;
    public List<String> priority;
    private Spinner spinner, pro_spinner, status_spinner, prioriyt_spinner, spinner2;
    private LinearLayout parentLinearLayout;
    private LinearLayout newLayout;
    private LinearLayout catalog_layout;
    public SharedPreferences sharedPref;
    View view;
    private String selected = "";
    Dialog  options_dialog;
    public LinearLayout gallery, camera;
    private  String wholseller_id;
    String session;
    public static String output;
    Button image_upload, vedio_upload, add_stonedetails, add_spesification, submit_button;
    final int CAMERA_PIC_REQUEST = 1337;
    public static final int PICK_IMAGE = 1;
    RecyclerView recyclerView;
    public ArrayList<Update_product_model> productlist;
    public Update_product_activity_ad image_add_adapter;
    private ExpandableListView expandableListView;
    public ArrayList<String> listDataHeader = new ArrayList<String>();
    HashMap<String, ArrayList<String>> listDataChild = new HashMap<String, ArrayList<String>>();
    HashMap<String, String> all_id = new HashMap<String, String>();
    Adapter_spesification expandable_spcification_adapter;
    private Button reselect;
    LinearLayout button_layout, expand_layout;
    private static final int SELECT_VIDEO = 3;
    private ViewGroup rootView;
    byte[] fi;
    String fileName,picturePath;
    private final String twoHyphens = "--";
    private final String lineEnd = "\r\n";
    private final String boundary = "apiclient-" + System.currentTimeMillis();
    private EditText name, sku, description,delivery_days,manu_name,manu_no;
    private CheckBox showable, newlounch;
    private JSONArray cat_path = new JSONArray();
    private JSONArray image_array;
    private ArrayList<Uri> filePaths=new ArrayList<>();
    private    StringBuilder category_path = new StringBuilder();
    private  String realPath,urldata;
    private  static View window;
    private  static Context mcontext;
    private static Activity activity ;
    public    HashMap<String ,String> val_map;
    public    HashMap<String ,ArrayList<String>> stone_map=new HashMap<>();
    private  static ProgressBar progressBar;
    private   TextView ccat_path,user_class;
    private  ArrayAdapter<String> ty;
    private ArrayAdapter<String> pri;
    private ArrayAdapter<String> status_ada;
    private static String product_id;
    private Dialog dialog,myDialogue;
    private TextView ok,cancel,main_text,myd_ok,myd_cancel;
    private static final int PERMISSION_REQUEST_CAMERA = 0;
    private ListView listView;
    private ArrayList<Top_model> list_data=new ArrayList<>();
    private ArrayList<String> user_assigned_list=new ArrayList<>();
    private User_class_Adapter user_class_adapter;
    private String version;
    private   JSONArray userclassArray=new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product_);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("Update product");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //////////////////////////////////////////item fetching////////////////////////////////////
        name = (EditText) findViewById(R.id.name);
        sku = (EditText) findViewById(R.id.sku);
        description = (EditText) findViewById(R.id.description);
        delivery_days = (EditText) findViewById(R.id.delevery_days);
        manu_name = (EditText) findViewById(R.id.manufact_name);
        manu_no = (EditText) findViewById(R.id.manuf_mob);

        myDialogue=new Dialog(this);
        myDialogue.setContentView(R.layout.user_classes_layout);
        listView=(ListView)myDialogue.findViewById(R.id.user_layout_list);
        myd_ok=(TextView)myDialogue.findViewById(R.id.ok);
        myd_cancel=(TextView)myDialogue.findViewById(R.id.cancel);

        myd_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myDialogue.dismiss();
                ArrayList arrayList=user_class_adapter.getValuemap();
                Log.e("SIZE of An list",String.valueOf(arrayList.size()));

                if(arrayList.size()>0) {
                    StringBuilder string = new StringBuilder();
                    string.append(arrayList.get(0).toString());
                    for (int i = 1; i < arrayList.size(); i++) {
                        string.append(" , " + arrayList.get(i).toString());
                    }
                    user_class.setText(string);
                }
                else {
                    user_class.setText("");
                }
            }
        });

        myd_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialogue.dismiss();

            }
        });

        ccat_path = (TextView) findViewById(R.id.cat_path);
        user_class = (TextView) findViewById(R.id.user_classes);
        user_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialogue.show();
            }

        });


        progressBar=(ProgressBar)findViewById(R.id.prgLoading);
        showable = (CheckBox) findViewById(R.id.showable);
        newlounch = (CheckBox) findViewById(R.id.newlaunch);
        mcontext=this;
        activity=this;

        sharedPref = getSharedPreferences("USER_DETAILS", 0);
        output = sharedPref.getString(ACCESS_TOKEN, null);
        wholseller_id = sharedPref.getString("userid", null);
        spinner = (Spinner) findViewById(R.id.country);
        pro_spinner = (Spinner) findViewById(R.id.product_type);
        status_spinner = (Spinner) findViewById(R.id.product_status);
        prioriyt_spinner = (Spinner) findViewById(R.id.priyority);

        ///////////////////////////////////////////dynamic view/////////////////////////////////////////////////////

        rootView = (ViewGroup) findViewById(R.id.stone_layout);


        recyclerView = (RecyclerView) findViewById(R.id.image_recycler);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getApplicationContext().getApplicationContext(), 1, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        expandableListView = (ExpandableListView) findViewById(R.id.expandable_list);


        button_layout = (LinearLayout) findViewById(R.id.buttons);
        expand_layout = (LinearLayout) findViewById(R.id.expand_layout);

        parentLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        newLayout = (LinearLayout) findViewById(R.id.new_layout);
        catalog_layout = (LinearLayout) findViewById(R.id.catalog_layout);



        Bundle bundle=getIntent().getExtras();
        product_id= bundle.getString("pro_id");
////////////////////////////////////////////////////////
        userInfo();
        loadRecycleData( product_id );


        reselect = (Button) findViewById(R.id.reselect);
        reselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reselect.getText().toString().equalsIgnoreCase("Change Category")) {
                    catalog_layout.setVisibility(View.VISIBLE);
                    catalog();
                }
                if(reselect.getText().toString().equalsIgnoreCase("Cancel")){
                    catalog_layout.setVisibility(View.GONE);
                    newLayout.removeAllViews();
                    reselect.setText("Change Category");
                }
            }
        });
        productlist = new ArrayList<>();
        options_dialog = new Dialog(this);
        options_dialog.setContentView(R.layout.photo_options_layout);
        gallery = (LinearLayout) options_dialog.findViewById(R.id.gallery);
        camera = (LinearLayout) options_dialog.findViewById(R.id.camera);

        image_upload = (Button) findViewById(R.id.image_button);
        vedio_upload = (Button) findViewById(R.id.vdo_btn);
        add_stonedetails = (Button) findViewById(R.id.add_stone);
        add_spesification = (Button) findViewById(R.id.add_specification);
        submit_button = (Button) findViewById(R.id.submit);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*  Upload_image();*/
                        dialog = new Dialog(Update_product_Activity.this);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setContentView(R.layout.alert_popup);
                        ok=(TextView)dialog.findViewById(R.id.ok);
                        cancel=(TextView)dialog.findViewById(R.id.cancel);
                        main_text=(TextView)dialog.findViewById(R.id.popup_textview);
                        main_text.setText("Do you want to Update product "+name.getText().toString());
                        dialog.show();
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                if(category_path.length()!=0) {
                                    cat_path.put(category_path.deleteCharAt(category_path.length() - 1).toString());
                                }
                                else {
                                   cat_path.put(ccat_path.getText().toString());
                                }
                                uploadImagesToServer();
                            }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }
        });


        add_spesification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listDataHeader.add("");
                final ArrayList<String> submenu = new ArrayList<>();
                submenu.add("");
                listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), submenu);
                expandable_spcification_adapter.notifyDataSetChanged();
            }
        });
        add_stonedetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddField();

            }
        });

        vedio_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_PICK);

                startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);

            }
        });
        image_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
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
                else{
                    requestCameraPermission();
                }
            }

        });
        priority = new ArrayList<>();
        priority.add("MODERATE");
        priority.add("LOW");
        priority.add("HIGH");
       pri = new ArrayAdapter<String>(Update_product_Activity.this, R.layout.support_simple_spinner_dropdown_item, priority) {
            @Override
            public boolean isEnabled(int position) {
                return true;
            }
        };
        prioriyt_spinner.setAdapter(pri);


        type = new ArrayList<>();
        type.add("REGULAR");
        type.add("PREMIUM");
         ty = new ArrayAdapter<String>(Update_product_Activity.this, R.layout.support_simple_spinner_dropdown_item, type) {
            @Override
            public boolean isEnabled(int position) {
                return true;
            }
        };

        pro_spinner.setAdapter(ty);

        status = new ArrayList<>();
        status.add("ACTIVE");
        status.add("SUSPENDED");
        status.add("CANCELLED");

         status_ada = new ArrayAdapter<String>(Update_product_Activity.this, R.layout.support_simple_spinner_dropdown_item, status) {
            @Override
            public boolean isEnabled(int position) {
                return true;
            }


        };
        status_spinner.setAdapter(status_ada);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                    return;
                }

                newLayout.removeAllViews();
                selected = "";
                String selectedItem = parent.getItemAtPosition(position).toString();

                category_path.append(selectedItem).append(",");
                prepareListData(selectedItem);


            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        expandable_spcification_adapter = new Adapter_spesification(Update_product_Activity.this, listDataHeader, listDataChild);
        val_map = expandable_spcification_adapter.getValuemap();
    }

    ////////////////////////permission //////////////////////////
    private void requestCameraPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with cda button to request the missing permission.

            // Request the permission
            ActivityCompat.requestPermissions(Update_product_Activity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CAMERA);



        } else {

            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////
    public void onAddField() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View rowView = inflater.inflate(R.layout.stone_deatails, null);


        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams.setMargins(0, 15, 0, 15);
        rowView.setLayoutParams(buttonLayoutParams);
        rootView.addView(rowView, rootView.getChildCount());


        Button button = (Button) rowView.findViewById(R.id.remove);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDelete(rowView);
            }
        });


    }

    public void onDelete(View view) {
        rootView.removeView(view);
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
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void spinn(ArrayList<String> string) {

        spinner2 = new Spinner(Update_product_Activity.this);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, string) {
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
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(spinnerArrayAdapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                }

                /*    ((ViewManager)newLayout.getParent()).removeView(newLayout);*/


                String selectedItem = parent.getItemAtPosition(position).toString();
                category_path.append(selectedItem).append(",");

                prepareListData(selectedItem);
           /*     specification(all_id.get(selectedItem));*/

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        newLayout.addView(spinner2, newLayout.getChildCount());
    }

    private RequestQueue requestQueue;

    private void prepareListData(String item) {
        if (selected.equalsIgnoreCase("")) {
            selected = item;

        } else {
            selected = selected.concat("," + item);

        }
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());

        }

        urldata = ip_cat + "/category/byFirstLevelCategory/b2b/" + selected + "?wholesaler=" + wholseller_id;



        StringRequest stringRequest = new StringRequest(Request.Method.GET, urldata, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<String> array = new ArrayList<>();
                    array.add("Select category");

                    JSONArray ja_data = new JSONArray(response);
                    if (ja_data.length() == 0) {

                        return;
                    }

                    for (int i = 0; i < ja_data.length(); i++) {
                        JSONObject jObj = ja_data.getJSONObject(i);


                        String sname = (jObj.getString("name").replaceAll("\\s", ""));

                        array.add(sname);

                        all_id.put(jObj.getString("name").replaceAll("\\s", ""), jObj.getString("id"));


                    }

                    spinn(array);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Sidebar", "EXCEPETION");

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 404:
                            BottomSheet.Builder builder = new BottomSheet.Builder(getApplicationContext());
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;

                    }
                }
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

        requestQueue.add(stringRequest);
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


    public void gaallery_open() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // ******** code for crop image
        i.putExtra("crop", "true");
        i.putExtra("aspectX", 800);
        i.putExtra("aspectY", 800);
        i.putExtra("outputX", 800);
        i.putExtra("outputY", 800);

//////////////////////////////////////////////////////////////////////////////////
      /* to compress images
       Bitmap photo = decodeSampledBitmapFromFile(filePath, DESIRED_WIDTH,
                DESIRED_HEIGHT);

        FileOutputStream out = new FileOutputStream(filePath);
        photo.compress(Bitmap.CompressFormat.JPEG, 100, out);

        public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth,
        int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }*/
/////////////////////////////////////////////////////////////////////////////////////////////////////
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

        if (data != null) {
            if (requestCode == CAMERA_PIC_REQUEST) {
                try {

                    Bitmap image = (Bitmap) data.getExtras().get("data");
                    Bitmap bitmap = Bitmap.createScaledBitmap(image, 800, 800, true);
                    /* imageView.setImageBitmap(bitmap);*/
                  /*  productlist.add(new Image_Add_model(bitmap));
                    image_add_adapter = new Image_Add_Adapter(Update_product_Activity.this, productlist);
                    recyclerView.setAdapter(image_add_adapter);
*/
                    fi = AppHelper.getFileDataFromDrawable(getApplicationContext(), bitmap);
                    Upload_image(fi);
                   /* Uri uri=bitmapToUriConverter(bitmap);
                    filePaths.add(uri);*/



                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
           /* if (resultCode == RESULT_OK) {

                if (requestCode == SELECT_VIDEO) {
                    System.out.println("SELECT_VIDEO");
                    Uri selectedVideoUri = data.getData();
                   String selectedPath = getPath(selectedVideoUri);
                    System.out.println("SELECT_VIDEO Path : " + selectedPath);

                   *//* uploadVideo(selectedPath);*//*
                }
            }*/

            if (requestCode == PICK_IMAGE) {
                try {
                    Uri selectedImage = data.getData();
                    Log.e("Your Error Message", selectedImage.toString());
                    filePaths.add(selectedImage);
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getApplicationContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);

                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    /* filePaths.add(picturePath);
                     */

                    Bitmap bitmap = Bitmap.createScaledBitmap((BitmapFactory.decodeFile(picturePath)), 800, 800, false);

                    String fileNameSegments[] = picturePath.split("/");
                    fileName = fileNameSegments[fileNameSegments.length - 1];
                    Log.e("FILE", fileName);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);

                    byte[] imageInByte = byteArrayOutputStream.toByteArray();
                    long lengthbmp = imageInByte.length;
                    Log.d("Image Size", String.valueOf(lengthbmp));


                    /* imageView.setImageBitmap(bitmap);*/

                    fi = AppHelper.getFileDataFromDrawable(getApplicationContext(), bitmap);
                    Upload_image(fi);

/*
                    productlist.add(new Image_Add_model(bitmap));
                    image_add_adapter = new Image_Add_Adapter(Update_product_Activity.this, productlist);
                    image_add_adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                        @Override
                        public void onItemRangeRemoved(int positionStart, int itemCount) {
                            super.onItemRangeRemoved(positionStart, itemCount);

                            filePaths.remove(positionStart);
                            Log.e("SIZEEEEE", String.valueOf(filePaths.size()));
                        }
                    });
                    recyclerView.setAdapter(image_add_adapter);*/


                } catch (NullPointerException e) {

                }

            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    ////////////////////////////////////////////////////////vedio////////////

    private String getPath(Uri uri) {

        String[] projection = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION};

        String[] thumbColumns = { MediaStore.Images.Media.DATA};

        Cursor thumbCursor = getContentResolver().query(uri, thumbColumns, null
                , null, null);

        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor!=null) {
            cursor.moveToFirst();

            String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            int fileSize = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
            long duration = TimeUnit.MILLISECONDS.toSeconds(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)));


            //some extra potentially useful data to help with filtering if necessary
            System.out.println("size: " + fileSize);
            System.out.println("path: " + filePath);
            System.out.println("duration: " + duration);


            return filePath;
        }
        else {

            Log.e("Camera PAth",realPath);
            return realPath;

        }
    }


    ////////////////////////////////////specification/////////////////////////////////////////////////
  /*  private void specification(String item) {

        button_layout.setVisibility(View.VISIBLE);
        expand_layout.setVisibility(View.VISIBLE);
        listDataChild.clear();
        listDataHeader.clear();
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        URL_DATA = ip_cat + "/category/" + item;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<String> array = new ArrayList<>();
                    array.add("Select category");

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("specificationHeading");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        if (jsonObject1.getString("headingName").equalsIgnoreCase("Stone Details")) {
                            continue;
                        }
                        listDataHeader.add(jsonObject1.getString("headingName"));
                        JSONArray jsonArray1 = jsonObject1.getJSONArray("keyes");
                        final ArrayList<String> submenu = new ArrayList<>();
                        for (int j = 0; j < jsonArray1.length(); j++) {
                            JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                            submenu.add(jsonObject2.getString("key"));
                        }

                        listDataChild.put(listDataHeader.get(i), submenu);
                    }

                    expandable_spcification_adapter.registerDataSetObserver(new DataSetObserver() {
                        @Override
                        public void onChanged() {
                            super.onChanged();
                            setPrevious(expandableListView, listDataHeader.size());
                        }
                    });


                    expandableListView.setAdapter(expandable_spcification_adapter);



                    setPrevious(expandableListView, 0);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Sidebar", "EXCEPETION");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 404:
                            BottomSheet.Builder builder = new BottomSheet.Builder(Update_product_Activity.this);
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;

                    }
                }
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

        requestQueue.add(stringRequest);


        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

                setListViewHeight(parent, groupPosition);
                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                return false;
            }
        });
    }*/

    private void setListViewHeight(ExpandableListView listView, int group) {
        BaseExpandableListAdapter listAdapter = (BaseExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);


            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
                //Add Divider Height
                totalHeight += listView.getDividerHeight() * (listAdapter.getChildrenCount(i) - 1);
            }
        }
        //Add Divider Height
        totalHeight += listView.getDividerHeight() * (listAdapter.getGroupCount() - 1);

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    private void setPrevious(ExpandableListView listView, int group) {
        BaseExpandableListAdapter listAdapter = (BaseExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                /* || ((!listView.isGroupExpanded(i)) && (i == group))*/) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
                //Add Divider Height
                totalHeight += listView.getDividerHeight() * (listAdapter.getChildrenCount(i) - 1);
            }
        }
        //Add Divider Height
        totalHeight += listView.getDividerHeight() * (listAdapter.getGroupCount() - 1);

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    ///////////////////////////////////////catalog url/////////////////////////////////////

    private RequestQueue requestQueue1;

    private void catalog() {
        if (requestQueue1 == null) {
            requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        }
        urldata = ip_cat + "/catalogue";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, urldata, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    product = new ArrayList<>();
                    product.add("Select Catalog");
                    reselect.setText("Cancel");


                    JSONObject ja_data = new JSONObject(response);
                    JSONArray jsonArray = ja_data.getJSONArray("content");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        product.add(jsonObject.getString("name"));
                    }

                    final ArrayAdapter<String> country_adaper = new ArrayAdapter<String>(Update_product_Activity.this, R.layout.support_simple_spinner_dropdown_item, product) {
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
                } catch (JSONException e) {
                    e.printStackTrace();


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 404:
                            BottomSheet.Builder builder = new BottomSheet.Builder(Update_product_Activity.this);
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;

                    }
                }
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

        requestQueue1.add(stringRequest);
    }
    private void buildTextPart(DataOutputStream dataOutputStream, String parameterName, String parameterValue) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + parameterName + "\"" + lineEnd);
        //dataOutputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
        dataOutputStream.writeBytes(lineEnd);
        dataOutputStream.writeBytes(parameterValue + lineEnd);
    }


    private void uploadImagesToServer() {

        progressBar.setVisibility(View.VISIBLE);
        window=getWindow().getCurrentFocus();

        int childcout= rootView.getChildCount();
        if(childcout>0) {
            ArrayList<String> nam = new ArrayList<>();
            ArrayList<String> color = new ArrayList<>();
            ArrayList<String> shap = new ArrayList<>();
            ArrayList<String> n_stone = new ArrayList<>();
            ArrayList<String> cla = new ArrayList<>();
            ArrayList<String> we = new ArrayList<>();
            ArrayList<String> typ = new ArrayList<>();
            ArrayList<String> vlues = new ArrayList<>();
            vlues.add("Stone Name");
            vlues.add("color");
            vlues.add("Shape");
            vlues.add("Number of Stones");
            vlues.add("Clarity");
            vlues.add("color");
            vlues.add("Weight");
            vlues.add("Setting Type");
            listDataHeader.add("Stone Details");
            expandable_spcification_adapter.notifyDataSetChanged();
            listDataChild.put("Stone Details", vlues);
            expandable_spcification_adapter.notifyDataSetChanged();
            for (int i = 0; i < childcout; i++) {
                View v = rootView.getChildAt(i);
                EditText heading = v.findViewById(R.id.heading);

                EditText colour = v.findViewById(R.id.colour);
                EditText shape = v.findViewById(R.id.shape);
                EditText no_stone = v.findViewById(R.id.no_stone);
                EditText clarity = v.findViewById(R.id.clarity);
                EditText weight = v.findViewById(R.id.weight);
                EditText type = v.findViewById(R.id.typpe);

                nam.add(heading.getText().toString());
                color.add(colour.getText().toString());
                shap.add(shape.getText().toString());
                n_stone.add(no_stone.getText().toString());
                cla.add(clarity.getText().toString());
                we.add(weight.getText().toString());
                typ.add(type.getText().toString());

            }
            stone_map.put("Stone Name", nam);
            stone_map.put("color", color);
            stone_map.put("Shape", shap);
            stone_map.put("Number of Stones", n_stone);
            stone_map.put("Clarity", cla);
            stone_map.put("Weight", we);
            stone_map.put("Setting Type", typ);
            expandable_spcification_adapter.notifyDataSetChanged();
        }

        JSONObject   jsonObject = new JSONObject();

        try {
            jsonObject.put("id", product_id);
            jsonObject.put("name", name.getText().toString());
            jsonObject.put("sku", sku.getText().toString());
            jsonObject.put("description", description.getText().toString());
            jsonObject.put("brand", "");
            jsonObject.put("gender", "");
            jsonObject.put("imageGridFsID", image_array);
            jsonObject.put("productType", pro_spinner.getSelectedItem().toString());
            jsonObject.put("priority", prioriyt_spinner.getSelectedItem().toString());
            jsonObject.put("productStatus", status_spinner.getSelectedItem().toString());
            jsonObject.put("showable", showable.isChecked());
            jsonObject.put("newLaunch", newlounch.isChecked());
            jsonObject.put("wholesaler", wholseller_id);
            jsonObject.put("categoriesPath", cat_path);
            jsonObject.put("version", version);
            jsonObject.put("requiredDayesToDeliver", delivery_days.getText().toString());
            JSONArray jsonArray=new JSONArray();
            for(int i=0;i<listDataHeader.size();i++){
                JSONObject heading_object=new JSONObject();
                heading_object.put("heading",listDataHeader.get(i));

                JSONArray attribute_array=new JSONArray();
                ArrayList<String> att_aaray=listDataChild.get(listDataHeader.get(i));

                for(int j=0;j<att_aaray.size();j++){
                    JSONArray value_array=new JSONArray();
                    JSONObject att_object=new JSONObject();
                    att_object.put("key",att_aaray.get(j));
                    if( listDataHeader.get(i).equalsIgnoreCase("Stone Details")){
                        for(int k=0;k<stone_map.get(att_aaray.get(j)).size();k++){
                            value_array.put(stone_map.get(att_aaray.get(j)).get(k));
                        }
                        att_object.put("values",value_array);
                        attribute_array.put(att_object);
                        continue;
                    }
                    value_array.put( val_map.get(att_aaray.get(j)));

                    att_object.put("values",value_array);
                    attribute_array.put(att_object);
                }
                heading_object.put("attributes",attribute_array);
                jsonArray.put(heading_object);
            }
            jsonObject.put("specification",jsonArray);
            jsonObject.put("userAccess",userclassArray);
            Timber.d(String.valueOf(user_assigned_list.size()));
        } catch (Exception e) {
            e.printStackTrace();
        }
/*
        Retrofit retrofit = NetworkClient.getRetrofitClient(Add_new_product_activity.this);

        UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);*/
        //Create a file object using file path
        ApiService service = ApiClient.createService(ApiService.class);

        List<MultipartBody.Part> parts = new ArrayList<>();

        for (int i = 0; i < filePaths.size(); i++) {
            parts.add(prepareFilePart("images", filePaths.get(i)));
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(jsonObject.toString()));
        Log.e("YYYYYAYAYYAYA",jsonObject.toString());
        Call call = service.update(body,"bearer "+output);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, retrofit2.Response response) {
            }
            @Override
            public void onFailure(Call call, Throwable t) {
            }
        });
    }
    public interface ApiService {
        @PUT("gate/b2b/zuul/catalog/api/v1/product/secure/update")
        Call<ResponseBody> update(@Body RequestBody requestBody, @Header("Authorization") String auth);
        /*  Call  event_store(@Body RequestBody file, @Part("product") RequestBody requestBody);*/
    }
    public static class ApiClient   {
        public static final String API_BASE_URL = ip;

        private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request request = chain.request();
                        okhttp3.Response response = chain.proceed(request);

                        // todo deal with the issues the way you need to
                        if (response.code()==409){
                            Snackbar.make(window, "SKU already exist", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                            activity.runOnUiThread(new Runnable() {
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        }
                        if (response.code()==200){
                            activity.runOnUiThread(new Runnable() {
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(mcontext,"Product Updated Successfully",Toast.LENGTH_LONG).show();

                                }
                            });
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            Log.e("ERROR ME", jsonObject.toString());
                        }
                        catch (Exception e){

                        }
                        return response;
                    }
                });


        private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create());

        public static ApiService createService(Class<ApiService> serviceClass)
        {
            Retrofit retrofit = builder.client(httpClient.build()).build();
            return retrofit.create(serviceClass);
        }
    }

    public Uri bitmapToUriConverter(Bitmap mBitmap) {
        Uri uri = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 100, 100);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, 200, 200,
                    true);
            File file = new File(Update_product_Activity.this.getFilesDir(), "Image"
                    +".jpeg");
            FileOutputStream out = Update_product_Activity.this.openFileOutput(file.getName(),Context.MODE_PRIVATE);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //get absolute path
            realPath = file.getAbsolutePath();
            File f = new File(realPath,"image_from camera");
            uri = Uri.fromFile(f);
            Log.e("Your Error Message", uri.toString());

        } catch (Exception e) {
            Log.e("Your Error Message", e.getMessage());
        }
        return uri;
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    ///////////////////////////////Doneee//////////////
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {

        File file = new File(getPath(fileUri));
        String mimeType= URLConnection.guessContentTypeFromName(file.getName());
        RequestBody requestBody = RequestBody.create(MediaType.parse(mimeType), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestBody);
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    public void loadRecycleData(final String id){
       final StringBuilder stringBuilder=new StringBuilder();
              image_array=new JSONArray();
        String url=ip_cat+"/product/"+id;
        Log.e("UUURRRRL",url);

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject1=jsonObject.getJSONObject("resourceSupport");
                    version=jsonObject1.getString("version");
                    name.setText(jsonObject1.getString("name"));
                    sku.setText(jsonObject1.getString("sku"));
                    description.setText(jsonObject1.getString("description"));
                    JSONArray jsonArray=jsonObject1.getJSONArray("categoriesPath");
                    ccat_path.setText(jsonArray.getString(0));
                    delivery_days.setText(jsonObject1.getString("requiredDayesToDeliver"));
                    manu_name.setText(jsonObject1.getString("manufactureName"));
                    manu_no.setText(jsonObject1.getString("manufactureMobile"));
                    JSONArray usrer_acces=jsonObject1.getJSONArray("userAccess");
                    for(int i=0;i<usrer_acces.length();i++){
                        stringBuilder.append(usrer_acces.getString(i));
                        user_assigned_list.add((usrer_acces.getString(i)));
                        userclassArray.put(usrer_acces.getString(i));
                    }
                    user_class.setText(stringBuilder.toString());
                    user_class_adapter=new User_class_Adapter(getApplicationContext(),list_data,user_assigned_list);
                    listView.setAdapter(user_class_adapter);
                    Log.e("USER",stringBuilder.toString());
                    pro_spinner.setSelection(ty.getPosition(jsonObject1.getString("productType")));
                    prioriyt_spinner.setSelection(pri.getPosition(jsonObject1.getString("priority")));
                    status_spinner.setSelection(status_ada.getPosition(jsonObject1.getString("productStatus")));
                    showable.setChecked(jsonObject1.getBoolean("showable"));
                    newlounch.setChecked(jsonObject1.getBoolean("newLaunch"));
                    try {
                        JSONArray jsonArray1 = jsonObject1.getJSONArray("specification");
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                            if(jsonObject2.getString("heading").equalsIgnoreCase("Stone Details")){
                                onAddField();
                                View v = rootView.getChildAt( rootView.getChildCount()-1);
                                EditText heading = v.findViewById(R.id.heading);
                                EditText colour = v.findViewById(R.id.colour);
                                EditText shape = v.findViewById(R.id.shape);
                                EditText no_stone = v.findViewById(R.id.no_stone);
                                EditText clarity = v.findViewById(R.id.clarity);
                                EditText weight = v.findViewById(R.id.weight);
                                EditText type = v.findViewById(R.id.typpe);
                                JSONArray jsonArray11 = jsonObject2.getJSONArray("attributes");
                                for (int j = 0; j < jsonArray11.length(); j++) {
                                    JSONObject jsonObject11 = jsonArray11.getJSONObject(j);
                                    String k = jsonObject11.getString("key");
                                    JSONArray jsonArray2 = jsonObject11.getJSONArray("values");
                                    if (k.equalsIgnoreCase("Stone Name")) {
                                        heading.setText(jsonArray2.get(0).toString());
                                    }
                                    if (k.equalsIgnoreCase("color")) {
                                        colour.setText(jsonArray2.get(0).toString());
                                    }
                                    if (k.equalsIgnoreCase("Shape")) {
                                        shape.setText(jsonArray2.get(0).toString());
                                    }
                                    if (k.equalsIgnoreCase("Number of Stones")) {
                                        no_stone.setText(jsonArray2.get(0).toString());
                                    }
                                    if (k.equalsIgnoreCase("Clarity")) {
                                        clarity.setText(jsonArray2.get(0).toString());
                                    }
                                    if (k.equalsIgnoreCase("Weight")) {
                                        weight.setText(jsonArray2.get(0).toString());
                                    }
                                    if (k.equalsIgnoreCase("Setting Type")) {
                                        type.setText(jsonArray2.get(0).toString());
                                    }
                                }
                                continue;
                            }
                            listDataHeader.add(jsonObject2.getString("heading"));
                            JSONArray att_tr = jsonObject2.getJSONArray("attributes");
                            ArrayList<String> submenu = new ArrayList<>();
                            for (int k = 0; k < att_tr.length(); k++) {
                                JSONObject jsonObject3 = att_tr.getJSONObject(k);
                                submenu.add(jsonObject3.getString("key"));
                                JSONArray jsonArray2 = jsonObject3.getJSONArray("values");
                                val_map.put(jsonObject3.getString("key"), jsonArray2.getString(0));
                            }
                            listDataChild.put(listDataHeader.get(i), submenu);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    JSONArray jsonArray2=jsonObject1.getJSONArray("imageGridFsID");
                   for(int i=0;i<jsonArray2.length();i++){
                       productlist.add(new Update_product_model(jsonArray2.getString(i),id));
                       image_array.put(jsonArray2.getString(i));
                   }
                    image_add_adapter = new Update_product_activity_ad(Update_product_Activity.this, productlist);
                   image_add_adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                       @TargetApi(Build.VERSION_CODES.KITKAT)
                       @Override
                       public void onItemRangeRemoved(int positionStart, int itemCount) {
                           super.onItemRangeRemoved(positionStart, itemCount);
                         image_array.remove(positionStart);
                       }
                   });
                    recyclerView.setAdapter(image_add_adapter);
                    expandable_spcification_adapter.registerDataSetObserver(new DataSetObserver() {
                        @Override
                        public void onChanged() {
                            super.onChanged();
                            setPrevious(expandableListView, listDataHeader.size());
                        }
                    });
                    expandableListView.setAdapter(expandable_spcification_adapter);
                    setPrevious(expandableListView, 0);

                    expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                        @Override
                        public boolean onGroupClick(ExpandableListView parent, View v,
                                                    int groupPosition, long id) {

                            if (listDataChild.get(listDataHeader.get(groupPosition)).size() == 0) {


                            }
                            else {
                                setListViewHeight(parent, groupPosition);
                            }
                            return false;
                        }
                    });

                    expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                            return false;
                        }
                    });

                    expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                        @Override
                        public void onGroupExpand(int i) {
                            if (listDataChild.get(listDataHeader.get(i)).size() == 0) {

                                return;
                            }
                        }
                    });
                }
                catch (Exception e){
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
                            BottomSheet.Builder builder = new BottomSheet.Builder(Update_product_Activity.this);
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;
                        case 400:
                            BottomSheet.Builder builder1 = new BottomSheet.Builder(Update_product_Activity.this);
                            builder1.setTitle("Sorry! No Products Available");
                            builder1.show();
                            break;
                        case 417:

                            Snackbar.make(getWindow().getCurrentFocus(), "Sorry! No Products Available", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                           /* BottomSheet.Builder builder2 = new BottomSheet.Builder(getContext());
                            builder2.setTitle("Sorry! No Products Available");
                            builder2.show();*/
                            break;
                        case 500:
                        Snackbar.make(getWindow().getCurrentFocus(), "Sorry! something went wrong", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

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
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
  public void Upload_image( final  byte[] fi) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = ip + "gate/b2b/zuul/catalog/api/v1/assets/secure/image/"+product_id+"?file";
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                try {

                    Log.i("Unexpected", resultResponse);
                    Snackbar.make(getWindow().getCurrentFocus(), "New Image Added Successfully", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                      productlist.clear();
                    image_add_adapter.notifyDataSetChanged();
                      loadRecycleData(product_id);
                      listDataChild.clear();
                      expandable_spcification_adapter.notifyDataSetChanged();
                      listDataHeader.clear();
                      expandable_spcification_adapter.notifyDataSetChanged();


                  /*  Intent login = new Intent(mcontext, Update_product_Activity.class);
                    mcontext.startActivity(login);
                    activity.finish();*/

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
                        try {
                            String status = response.getString("exception");
                            Log.e("Error Status", status);
                        } catch (Exception e) {
                        }
                        String message = response.getString("message");


                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            Snackbar.make(getWindow().getCurrentFocus(), "Sorry! something went wrong", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            errorMessage = message + " Something is getting wrong";
                        } else if (networkResponse.statusCode == 409) {
                            Snackbar.make(getWindow().getCurrentFocus(), "SKU already exist", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
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
                params.put("Authorization", "bearer " + output);
                return params;
            }

           /* @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("product", jsonObject.toString());

                return params;
            }*/

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("file", new DataPart(fileName, fi, "image/jpeg"));

                return params;
            }

          /*  @Override
            public byte[] getBody() throws AuthFailureError {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos);

                try {

                    // populate text payload
                    Map<String, String> params = getParams();

                    if (params != null && params.size() > 0) {
                        textParse(dos, params, getParamsEncoding());
                    }

                    Map<String, DataPart> data = getByteData();
                    if (data != null && data.size() > 0) {
                        dataParse(dos, data);
                    }


                    // close multipart form data after text and file data
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    return bos.toByteArray();
                } catch (Exception e) {
                    Log.e("Illli","ERRRORRR");
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public String getBodyContentType() {
                return super.getBodyContentType();
            }*/
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(multipartRequest);
    }

    public void userInfo(  ){
        String url = ip+"uaa/b2b/api/v1/user/info";
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public
            void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("userClass");
                    for(int i=0;i<jsonArray.length();i++){
                        list_data.add(new Top_model(jsonArray.getString(i)));
                    }
                    /*user_class_adapter=new User_class_Adapter(getApplicationContext(),list_data);
                    listView.setAdapter(user_class_adapter);*/


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

                        case 417:

                            Snackbar.make(getCurrentFocus(), "Sorry! No Products Available", Snackbar.LENGTH_LONG)
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
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}


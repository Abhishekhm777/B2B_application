package com.example.compaq.b2b_application.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.Adapter_spesification;
import com.example.compaq.b2b_application.Adapters.Image_Add_Adapter;
import com.example.compaq.b2b_application.Adapters.User_class_Adapter;
import com.example.compaq.b2b_application.Fragments.Custom_order;
import com.example.compaq.b2b_application.Fragments.Customize_order_frag1;
import com.example.compaq.b2b_application.Helper_classess.AppHelper;
import com.example.compaq.b2b_application.Model.Image_Add_model;
import com.example.compaq.b2b_application.Model.Top_model;
import com.example.compaq.b2b_application.R;

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
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip_cat;
import static com.example.compaq.b2b_application.Fragments.products_display_fragment.URL_DATA;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;

public class Add_new_product_activity extends AppCompatActivity {
    Toolbar toolbar;
    public List<String> product;
    public List<String> status;
    public List<String> type;
    public List<String> priority;
    private Spinner spinner, pro_spinner, status_spinner, prioriyt_spinner, spinner2;
    private LinearLayout parentLinearLayout;
    private LinearLayout newLayout;
    public SharedPreferences sharedPref;
    View view;
    private String selected = "";
    Dialog myDialogue, options_dialog;
    public LinearLayout gallery, camera;
    String wholseller_id;
    String session;
    public static String output;
    Button image_upload, vedio_upload, add_stonedetails, add_spesification, submit_button;
    final int CAMERA_PIC_REQUEST = 1337;
    public static final int PICK_IMAGE = 1;
    RecyclerView recyclerView;
    public ArrayList<Image_Add_model> productlist;
    public Image_Add_Adapter image_add_adapter;
    private ExpandableListView expandableListView;
    public ArrayList<String> listDataHeader = new ArrayList<>();
    HashMap<String, ArrayList<String>> listDataChild = new HashMap<>();

    HashMap<String, String> all_id = new HashMap<>();

    Adapter_spesification expandable_spcification_adapter;
    private  TextView reselect,userclass_textView,ok,cancel;
    LinearLayout button_layout, expand_layout;
    private static final int SELECT_VIDEO = 3;
    private ViewGroup rootView;
    byte[] fi;
    String fileName,picturePath;
    private JSONObject jsonObject;
    private final String twoHyphens = "--";
    private final String lineEnd = "\r\n";
    private final String boundary = "apiclient-" + System.currentTimeMillis();
    private EditText name, sku, description,no_ofdelevery,man_name,manu_no;
    private CheckBox showable, newlounch;
    private JSONArray cat_path = new JSONArray();
    ArrayList<Uri> filePaths=new ArrayList<>();
    private    StringBuilder category_path = new StringBuilder();
    private  String realPath;
    private static final int PERMISSION_REQUEST_CAMERA = 0;
    private  static View window;
    private  static Context mcontext;

    private static  Activity activity ;
    private static String parent_name ;
    public    HashMap<String ,String> val_map;
    public    HashMap<String ,ArrayList<String>> stone_map=new HashMap<>();
    private  static  ProgressBar progressBar;
    private ListView listView;
    private ArrayList<Top_model> list_data=new ArrayList<>();
    private User_class_Adapter user_class_adapter;
    private String  user_Catalog="";


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product_activity);
        toolbar =  findViewById(R.id.tool_bar);
        toolbar.setTitle("Add new product");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //////////////////////////////////////////item fetching////////////////////////////////////
        name =  findViewById(R.id.name);
        sku =  findViewById(R.id.sku);
        description =  findViewById(R.id.description);
        man_name =  findViewById(R.id.manufact_name);
        manu_no =  findViewById(R.id.manuf_mob);
        no_ofdelevery =  findViewById(R.id.delevery_days);
        progressBar=findViewById(R.id.prgLoading);

        showable =  findViewById(R.id.showable);
        showable.setChecked(true);
        newlounch =  findViewById(R.id.newlaunch);
        mcontext=this;
        activity=this;

        if (getCallingActivity() != null) {
            parent_name=getCallingActivity().getClassName();

        }

        ///////////////////////////////////////////dynamic view/////////////////////////////////////////////////////

        rootView =  findViewById(R.id.stone_layout);
        recyclerView =  findViewById(R.id.image_recycler);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getApplicationContext().getApplicationContext(), 1, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        expandableListView =  findViewById(R.id.expandable_list);
        button_layout =  findViewById(R.id.buttons);
        expand_layout = findViewById(R.id.expand_layout);
        parentLinearLayout = findViewById(R.id.linearLayout);
        newLayout =  findViewById(R.id.new_layout);
        userclass_textView =  findViewById(R.id.user_classes);
        userclass_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialogue.show();
            }
        });
        reselect =  findViewById(R.id.reselect);
        reselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reselect.getText().toString().equalsIgnoreCase("Reselect Path")) {
                    category_path=new StringBuilder();
                    newLayout.removeAllViews();
                    spinner.setSelection(0);
                    cat_path = new JSONArray();
                    listDataChild.clear();
                    listDataHeader.clear();
                    button_layout.setVisibility(View.GONE);
                    expand_layout.setVisibility(View.GONE);
                }
            }
        });
        productlist = new ArrayList<>();
        myDialogue=new Dialog(this);
        myDialogue.setContentView(R.layout.user_classes_layout);
        ok=myDialogue.findViewById(R.id.ok);
        cancel=myDialogue.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialogue.dismiss();
            }
        });
         ok.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 myDialogue.dismiss();
                 ArrayList arrayList=user_class_adapter.getValuemap();
                 Log.e("LAY LIST",String.valueOf(arrayList.size()));
                 if(arrayList.size()>0) {
                     StringBuilder string = new StringBuilder();
                     string.append(arrayList.get(0).toString());


                     for (int i = 1; i < arrayList.size(); i++) {
                         string.append(" , " + arrayList.get(i).toString());
                     }
                     userclass_textView.setText(string);
                 }
                 else {
                     userclass_textView.setText("");
                 }
             }
         });
        listView=myDialogue.findViewById(R.id.user_layout_list);

       /* list_data.add(new Top_model("User-Class A"));
        list_data.add(new Top_model("User-Class B"));
        list_data.add(new Top_model("User-Class C"));*/

        user_class_adapter=new User_class_Adapter(this,list_data);
        listView.setAdapter(user_class_adapter);

        options_dialog = new Dialog(this);
        options_dialog.setContentView(R.layout.photo_options_layout);
        gallery =  options_dialog.findViewById(R.id.gallery);
        camera =  options_dialog.findViewById(R.id.camera);

        image_upload =  findViewById(R.id.image_button);
        vedio_upload =  findViewById(R.id.vdo_btn);
        add_stonedetails = findViewById(R.id.add_stone);
        add_spesification = findViewById(R.id.add_specification);
        submit_button =  findViewById(R.id.submit);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  Upload_image();*/
               if(!name.getText().toString().equalsIgnoreCase("")&&!sku.getText().toString().equalsIgnoreCase("")) {

                       cat_path.put(category_path.deleteCharAt(category_path.length() - 1).toString());
                       val_map = expandable_spcification_adapter.getValuemap();
                       uploadImagesToServer();


               }
               else {
                   if(name.getText().toString().equalsIgnoreCase("")){
                       name.setError("Please provide Product name");
                   }
                   if(sku.getText().toString().equalsIgnoreCase("")){
                       sku.setError("Please provide Product SKU");
                   }

                   Snackbar.make(view, "Please Fill Mandatory Fields", Snackbar.LENGTH_SHORT)
                           .setAction("Action", null).show();
               }

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
                        == PackageManager.PERMISSION_DENIED) {
                    requestCameraPermission();
                }
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED) {
                   permission();
                }

                else {
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

        });

        sharedPref = getSharedPreferences("USER_DETAILS", 0);
        output = sharedPref.getString(ACCESS_TOKEN, null);
        wholseller_id = sharedPref.getString("userid", null);
        spinner = (Spinner) findViewById(R.id.country);
        pro_spinner = (Spinner) findViewById(R.id.product_type);
        status_spinner = (Spinner) findViewById(R.id.product_status);
        prioriyt_spinner = (Spinner) findViewById(R.id.priyority);

        catalog();


        priority = new ArrayList<>();
        priority.add("MODERATE");
        priority.add("LOW");
        priority.add("HIGH");
        final ArrayAdapter<String> pri = new ArrayAdapter<String>(Add_new_product_activity.this, R.layout.support_simple_spinner_dropdown_item, priority) {
            @Override
            public boolean isEnabled(int position) {
                return true;
            }
        };
        prioriyt_spinner.setAdapter(pri);


        type = new ArrayList<>();
        type.add("REGULAR");
        type.add("PREMIUM");
        final ArrayAdapter<String> ty = new ArrayAdapter<String>(Add_new_product_activity.this, R.layout.support_simple_spinner_dropdown_item, type) {
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

        final ArrayAdapter<String> status_ada = new ArrayAdapter<String>(Add_new_product_activity.this, R.layout.support_simple_spinner_dropdown_item, status) {
            @Override
            public boolean isEnabled(int position) {
                return true;
            }


        };
        status_spinner.setAdapter(status_ada);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    reselect.setText("Select Path");
                    return;
                }
                newLayout.removeAllViews();
                selected = "";
                String selectedItem = user_Catalog+","+parent.getItemAtPosition(position).toString();

                category_path.append(selectedItem).append(",");

                prepareListData(selectedItem);
                try {
                    specification(all_id.get(parent.getItemAtPosition(position).toString()));


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        expandable_spcification_adapter = new Adapter_spesification(Add_new_product_activity.this, listDataHeader, listDataChild);

    }
////////////////////////permission //////////////////////////
private void requestCameraPermission() {

    ActivityCompat.requestPermissions(Add_new_product_activity.this,
            new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);

        ActivityCompat.requestPermissions(Add_new_product_activity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CAMERA);

}
    private void permission() {
        ActivityCompat.requestPermissions(Add_new_product_activity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CAMERA);
    }


//////////////////////////Add field/////////////////////////////////////////////
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
/////////////Delete//////////////////////////
    public void onDelete(View view) {
        rootView.removeView(view);
    }

///////////////////////////////////////////////////////////////////////

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

        spinner2 = new Spinner(Add_new_product_activity.this);
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

                reselect.setText("Reselect Path");
                String selectedItem = parent.getItemAtPosition(position).toString();
                category_path.append(selectedItem).append(",");

                prepareListData(selectedItem);
                try {
                    specification(all_id.get(selectedItem));


                } catch (Exception e) {
                    e.printStackTrace();
                }

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
        URL_DATA = ip_cat + "/category/byFirstLevelCategory/b2b/" + selected + "?wholesaler=" + wholseller_id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
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
                      /*  if(jObj.getString("requiredDayesToDeliver")!="null") {
                            no_ofdelevery.setText(jObj.getString("requiredDayesToDeliver"));
                        }*/
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
        i.putExtra("crop", false);
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
                    Bitmap bitmap = Bitmap.createScaledBitmap(image, 800, 800, false);
                    /* imageView.setImageBitmap(bitmap);*/
                    productlist.add(new Image_Add_model(bitmap));
                    image_add_adapter = new Image_Add_Adapter(Add_new_product_activity.this, productlist);
                    recyclerView.setAdapter(image_add_adapter);

                    Uri uri=bitmapToUriConverter(bitmap);
                    filePaths.add(uri);



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


                    productlist.add(new Image_Add_model(bitmap));
                    image_add_adapter = new Image_Add_Adapter(Add_new_product_activity.this, productlist);
                    image_add_adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                        @Override
                        public void onItemRangeRemoved(int positionStart, int itemCount) {
                            super.onItemRangeRemoved(positionStart, itemCount);

                           filePaths.remove(positionStart);
                           Log.e("SIZEEEEE", String.valueOf(filePaths.size()));
                        }
                    });
                    recyclerView.setAdapter(image_add_adapter);


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
    private void specification(String item) throws  Exception{

        button_layout.setVisibility(View.VISIBLE);
        expand_layout.setVisibility(View.VISIBLE);
        listDataChild.clear();
        listDataHeader.clear();
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        URL_DATA = ip_cat + "/category/" + item;
        Log.e("LOGTTF",URL_DATA);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<String> array = new ArrayList<>();
                    array.add("Select category");

                    JSONObject jsonObject = new JSONObject(response);
                    no_ofdelevery.setText(jsonObject.getString("requiredDayesToDeliver"));
                    JSONArray jsonArray = jsonObject.getJSONArray("specificationHeading");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        if (jsonObject1.getString("headingName").equalsIgnoreCase("Stone Details")) {
                            continue;
                        }
                        if(jsonObject1.getString("headingName")!=null) {
                            listDataHeader.add(jsonObject1.getString("headingName"));
                        }

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
                }catch (IndexOutOfBoundsException e){
                    expandable_spcification_adapter.notifyDataSetChanged();
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
                            BottomSheet.Builder builder = new BottomSheet.Builder(Add_new_product_activity.this);
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
                EditText editText=v.findViewById(R.id.edit);
                editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                return false;
            }
        });
    }

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
        URL_DATA = ip +"uaa/b2b/api/v1/user/info";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject ja_data = new JSONObject(response);
                    JSONArray jsonArray=ja_data.getJSONArray("userClass");
                    if(jsonArray.length()>0){
                        for(int i=0;i<jsonArray.length();i++)
                        list_data.add(new Top_model(jsonArray.getString(i)));
                    }

                    JSONObject user_object=ja_data.getJSONObject("company");
                    user_Catalog=user_object.getString("product");
                    getCategories(user_object.getString("product"));

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

        requestQueue1.add(stringRequest);
    }
   ////////////////////////
   private RequestQueue requestQueue2;
    private void getCategories(String catalogue){
        if (requestQueue2 == null) {
            requestQueue2 = Volley.newRequestQueue(getApplicationContext());
        }
        URL_DATA = ip_cat + "/category/byFirstLevelCategory/b2b/" + catalogue + "?wholesaler=" + wholseller_id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
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
                      /*  if(jObj.getString("requiredDayesToDeliver")!="null") {
                            no_ofdelevery.setText(jObj.getString("requiredDayesToDeliver"));
                        }*/
                        String sname = (jObj.getString("name").replaceAll("\\s", ""));
                        array.add(sname);
                        all_id.put(jObj.getString("name").replaceAll("\\s", ""), jObj.getString("id"));
                    }


                    final ArrayAdapter<String> country_adaper = new ArrayAdapter<String>(Add_new_product_activity.this, R.layout.support_simple_spinner_dropdown_item, array) {
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

        requestQueue2.add(stringRequest);

    }

   /* public void Upload_image() {
        try {
            jsonObject = new JSONObject();
            jsonObject.put("name", name.getText().toString());
            jsonObject.put("sku", sku.getText().toString());
            jsonObject.put("description", description.getText().toString());
            jsonObject.put("brand", "");
            jsonObject.put("gender", "");
            jsonObject.put("productType", pro_spinner.getSelectedItem().toString());
            jsonObject.put("priority", prioriyt_spinner.getSelectedItem().toString());
            jsonObject.put("productStatus", status_spinner.getSelectedItem().toString());
            jsonObject.put("showable", showable.isChecked());
            jsonObject.put("newLaunch", newlounch.isChecked());
            jsonObject.put("catalogue", spinner.getSelectedItem().toString());
            jsonObject.put("category", "ABC");
            jsonObject.put("wholesaler", wholseller_id);

            jsonObject.put("categoriesPath", cat_path);


        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = ip + "gate/b2b/zuul/catalog/api/v1/product/secure/add";
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

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("product", jsonObject.toString());

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("images", new DataPart(fileName, fi, "image/jpeg"));


                return params;
            }

         *//*   @Override
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
            }*//*
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(multipartRequest);
    }*/

   /* private void textParse(DataOutputStream dataOutputStream, Map<String, String> params, String encoding) throws IOException {
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                buildTextPart(dataOutputStream, entry.getKey(), entry.getValue());
            }
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + encoding, uee);
        }
    }
*/
    private void buildTextPart(DataOutputStream dataOutputStream, String parameterName, String parameterValue) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + parameterName + "\"" + lineEnd);
        //dataOutputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
        dataOutputStream.writeBytes(lineEnd);
        dataOutputStream.writeBytes(parameterValue + lineEnd);
    }

  /*  private void buildDataPart(DataOutputStream dataOutputStream, VolleyMultipartRequest.DataPart dataFile, String inputName) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" +
                inputName + "\"; filename=\"" + dataFile.getFileName() + "\"" + lineEnd);
        if (dataFile.getType() != null && !dataFile.getType().trim().isEmpty()) {
            dataOutputStream.writeBytes("Content-Type: " + dataFile.getType() + lineEnd);
        }
        dataOutputStream.writeBytes(lineEnd);

        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(dataFile.getContent());
        int bytesAvailable = fileInputStream.available();

        int maxBufferSize = 1024 * 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        dataOutputStream.writeBytes(lineEnd);
    }
*/
    private void uploadImagesToServer() {


        JSONArray user_access=new JSONArray();
        if(user_class_adapter.getValuemap().size()>0) {
            for (int i = 0; i < user_class_adapter.getValuemap().size(); i++) {
                user_access.put(user_class_adapter.getValuemap().get(i).toString());
            }
        }

        progressBar.setVisibility(View.VISIBLE);
        window=getWindow().getCurrentFocus();


       /* Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.166/~snow/UploadImage/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // create list of file parts (photo, video, ...)
        List<MultipartBody.Part> parts = new ArrayList<>();
        // create upload service client
        ApiService service = retrofit.create(ApiService.class);
        if (arrayList != null) {
            // create part for file (photo, video, ...)
            for (int i = 0; i < arrayList.size(); i++) {
                parts.add(prepareFilePart("image" + i, arrayList.get(i)));
            }
        }
        // create a map of data to pass along
        RequestBody description = createPartFromString("www.androidlearning.com");
        RequestBody size = createPartFromString("" + parts.size());
        // finally, execute the request
        Call<ResponseBody> call = service.uploadMultiple(description, size, parts);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });
*/

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
          listDataChild.put("Stone Details", vlues);
          for (int i = 0; i < childcout; i++) {
              View v = rootView.getChildAt(i);
              EditText heading = v.findViewById(R.id.heading);

              EditText colour = v.findViewById(R.id.colour);
              EditText shape = v.findViewById(R.id.shape);
              EditText no_stone = v.findViewById(R.id.no_stone);
              EditText clarity = v.findViewById(R.id.clarity);
              EditText weight = v.findViewById(R.id.weight);
              EditText type = v.findViewById(R.id.typpe);
              try {
                  nam.add(heading.getText().toString());
                  color.add(colour.getText().toString());
                  shap.add(shape.getText().toString());
                  n_stone.add(no_stone.getText().toString());
                  cla.add(clarity.getText().toString());
                  we.add(weight.getText().toString());
                  typ.add(type.getText().toString());
              } catch (Exception e) {
                  e.printStackTrace();
              }

          }
          stone_map.put("Stone Name", nam);
          stone_map.put("color", color);
          stone_map.put("Shape", shap);
          stone_map.put("Number of Stones", n_stone);
          stone_map.put("Clarity", cla);
          stone_map.put("Weight", we);
          stone_map.put("Setting Type", typ);
      }

        try {
            jsonObject = new JSONObject();
            jsonObject.put("name", name.getText().toString());
            jsonObject.put("sku", sku.getText().toString());
            jsonObject.put("description", description.getText().toString());
            jsonObject.put("brand", "");
            jsonObject.put("gender", "");
            jsonObject.put("productType", pro_spinner.getSelectedItem().toString());
            jsonObject.put("priority", prioriyt_spinner.getSelectedItem().toString());
            jsonObject.put("productStatus", status_spinner.getSelectedItem().toString());
            jsonObject.put("showable", showable.isChecked());
            jsonObject.put("newLaunch", newlounch.isChecked());
            jsonObject.put("catalogue", spinner.getSelectedItem().toString());
            jsonObject.put("wholesaler", wholseller_id);
            jsonObject.put("categoriesPath", cat_path);
            jsonObject.put("manufactureName", man_name.getText().toString());
            jsonObject.put("manufactureMobile", manu_no.getText().toString());
            jsonObject.put("requiredDayesToDeliver", no_ofdelevery.getText().toString());
            jsonObject.put("userAccess",user_access);

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

        } catch (Exception e) {
            e.printStackTrace();
        }
/*
        Retrofit retrofit = NetworkClient.getRetrofitClient(Add_new_product_activity.this);

        UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);*/
        //Create a file object using file path
        ApiService service = ApiClient.createService(ApiService.class);
      /*  File file = new File(picturePath);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("images", file.getName(), fileReqBody);*/
       ////////////////////////////////////////////////////////////////////

        List<MultipartBody.Part> parts = new ArrayList<>();


    for (int i = 0; i < filePaths.size(); i++) {
        parts.add(prepareFilePart("images", filePaths.get(i)));
    }

    /////////////////////////////////////////////////////////////

        /*MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);*/
       /* builder.addFormDataPart("images",file.getName(),RequestBody.create(MediaType.parse("image/*"), file));*/

      /*  MultipartBody.Part requestBody = MultipartBody.Part.createFormData("images","", (builder.build()));*/

       /* for (int i = 0; i <filePaths.size() ; i++) {
            File file1 = new File(filePaths.get(i));
            RequestBody requestImage = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
              builder.addFormDataPart(file1.getName(), String.valueOf(requestImage));


        }

        MultipartBody.Part requestBody = MultipartBody.Part.createFormData("images","", (builder.build()));*/

        //Create request body with text description and text media type
        RequestBody description = RequestBody.create(MediaType.parse("product"), jsonObject.toString());
     /*   RequestBody description2 = RequestBody.create(MediaType.parse("images"),part.toString());*/


      /*  Call call = uploadAPIs.uploadImage(builder, description);*/
        Call  call = service.uploadImage(parts,description,"bearer "+output);
        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call call, retrofit2.Response response) {


            }

            @Override
            public void onFailure(Call call, Throwable t) {



            }

        });
    }



   /* public  interface UploadAPIs  {
        @Headers({"Authorization: bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NTczNzY0ODYsInVzZXJfbmFtZSI6ImFrYXNoMUBnbWFpbC5jb20iLCJhdXRob3JpdGllcyI6WyJST0xFX1dIT0xFU0FMRVIiXSwianRpIjoiNzYzNzVmZDAtNmEyYy00NzEyLWI3N2MtMmIxMTgyNzc0ZWUyIiwiY2xpZW50X2lkIjoib3Jkb2Z5LXdlYiIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdfQ.W_xns7fW_Tj-XLe9sJKZccaTk5bVW7krqZyjntDHgDt0oDd-y7Tup_ifIy_PS_z1BXwZ3AD2WrBjEI4IAZBlnI8YXW3l7e6BDHFOPmWqwUgAs4HeodA77fz5omEAmmlmYiyMVB7vHAYm8lAtsQow9ImSfCQaFdoUlntB3iXBsuMbLlehGUNiAceaz0b_qQ6A5AWE6ZDeH347d4D5OqpR99Rv4dDUtUE7lUGMtpFz35vt44mFNG5Y9T3UDTXdfzEy8IjqF4pv9HOUxBdFodccA2wshNR1crEpwukscmxWyz8ppVlxjPNI3l48716uGwRUKzhTvG5ILKIvF5ZlXgBmPA"})
        @Multipart
        @POST("gate/b2b/zuul/catalog/api/v1/product/secure/add")
        Call<ResponseBody> uploadImage(@Body RequestBody file, @Part("product") RequestBody requestBody);
    }*/

/*



    static class NetworkClient  {
        private static final String BASE_URL = ip;
        private static Retrofit retrofit;

        public static Retrofit getRetrofitClient(Context context) {
            if (retrofit == null) {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .addInterceptor(new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(Chain chain) throws IOException {
                                okhttp3.Request request = chain.request();
                                okhttp3.Response response = chain.proceed(request);

                                //

                                try {
                                    JSONObject jsonObject = new JSONObject(response.body().string());

                                    Log.e("ERROR MESSAGE", jsonObject.toString());

                                }
                                catch (Exception e){

                                }

                                return response;
                            }
                        })
                        .build();
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }
    }
*/

    public interface ApiService {

        @Multipart
        @POST("gate/b2b/zuul/catalog/api/v1/product/secure/add")
        Call<ResponseBody> uploadImage(@Part List<MultipartBody.Part> body, @Part("product") RequestBody requestBody,@Header("Authorization") String auth);
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



                        if (response.code()==409){

                             activity.runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                     Snackbar.make(window, "SKU already exist", Snackbar.LENGTH_SHORT)
                                             .setAction("Action", null).show();
                                 }
                             });

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
                                    Toast.makeText(mcontext,"Product Added Successfully",Toast.LENGTH_LONG).show();


                                    if(parent_name.equalsIgnoreCase("com.example.compaq.b2b_application.Activity.Customize_Order")){
                                       /* Customize_order_frag1 f = (Customize_order_frag1) ((AppCompatActivity) mcontext).getSupportFragmentManager().findFragmentByTag("customize");*/
                                        /*   f.refreshMethod();*/
                                        ((AppCompatActivity) mcontext).getSupportFragmentManager().popBackStackImmediate();
                                          Log.e("YAHOOOOOO","VDVDVDV");
                                    }

                                }
                            });
                            if(!parent_name.equalsIgnoreCase("com.example.compaq.b2b_application.Activity.Customize_Order")) {
                                Intent login = new Intent(mcontext, Add_new_product_activity.class);
                                mcontext.startActivity(login);
                                activity.finish();
                            }
                        }

                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            Log.e("ERROR MESSAGE", jsonObject.toString());

                        }
                        catch (Exception e){
                           e.printStackTrace();
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
            options.inSampleSize = calculateInSampleSize(options, 800, 800);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, 800, 800,
                    false);
            File file = new File(Add_new_product_activity.this.getFilesDir(), "Image"
                    +".jpeg");
            FileOutputStream out = Add_new_product_activity.this.openFileOutput(file.getName(),Context.MODE_PRIVATE);
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

    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {

            File file = new File(getPath(fileUri));
        String mimeType= URLConnection.guessContentTypeFromName(file.getName());
        RequestBody requestBody = RequestBody.create(MediaType.parse(mimeType), file);
            Log.e("CSCDCDC", String.valueOf(MediaType.parse(mimeType)));

            return MultipartBody.Part.createFormData(partName, file.getName(), requestBody);
        }

}

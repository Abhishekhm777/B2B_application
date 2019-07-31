package com.example.compaq.b2b_application.Fragments;


import android.app.Activity;
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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.badoualy.stepperindicator.StepperIndicator;
import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Adapters.Customize_Oder_Adapter1;
import com.example.compaq.b2b_application.Adapters.Manage_Adapter;
import com.example.compaq.b2b_application.Helper_classess.AppHelper;
import com.example.compaq.b2b_application.Helper_classess.Back_alert_class;
import com.example.compaq.b2b_application.Helper_classess.SessionManagement;
import com.example.compaq.b2b_application.Model.Recy_model2;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.Helper_classess.VolleyMultipartRequest;
import com.example.compaq.b2b_application.Helper_classess.VolleySingleton;

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
import timber.log.Timber;

import static com.example.compaq.b2b_application.Activity.Customize_Order.pager;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.editor;

/**
 * A simple {@link Fragment} subclass.
 */
public class Custom_order extends Fragment {
    ExpandableListView expListView;
    public ArrayList<String> listDataHeader = new ArrayList<String>();
    HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
    HashMap<String, String> list_id = new HashMap<String, String>();
    Customize_Oder_Adapter1 listAdapter;
    public Manage_Adapter manage_adapter;
    private int lastExpandedPosition = -1;
    public SharedPreferences sharedPref;
    private Bundle bundle;
    SessionManagement session;
    public String SUB_URL = "";
    public String sname = "";
    private View view;
    public int position = 0;
    String output,user_id;
    String wholseller_id;
    Button button;
    public FragmentManager fragmentManager;
    /*private String path="";*/
    private SearchView searchView, search_by_name;
    public ArrayList<Recy_model2> productlist;
    Manage_category_frag1.TextClicked mCallback;
    private StringBuilder category_path = new StringBuilder();
    private List list = new ArrayList();
    public static final int PICK_IMAGE = 1;
    Dialog dialog;
    private LinearLayout parentLinearLayout;
    private RelativeLayout newLayout;
    private String imageurl ;
    RecyclerView recyclerView;
    HashMap<String, String> all_id = new HashMap<String, String>();
    public List<String> product;
    private SharedPreferences.Editor myEditor;
    public List<String> contact_array=new ArrayList<>();
    @BindView(R.id.cust_no)AutoCompleteTextView autoCompleteTextView;
    @BindView(R.id.customer_nu)TextView custname;
    @BindView(R.id.name) EditText name;
    @BindView(R.id.grosswt) EditText qrosswt;
    @BindView(R.id.address) EditText address;
    @BindView(R.id.email) EditText email;
    @BindView(R.id.company_name) EditText company_name;
    @BindView(R.id.next) Button next;
    @BindView(R.id.image)ImageView imageView;

    @BindView(R.id.qty) EditText qty;
    @BindView(R.id.size) EditText length;
    @BindView(R.id.seal) EditText seal;
    @BindView(R.id.melting) EditText melting;
    @BindView(R.id.p_name) EditText pro_name;
    @BindView(R.id.date) EditText days;



    private Activity activity;
    public Custom_order() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_custom_order, container, false);
        ButterKnife.bind(this,view);
        activity=getActivity();

        sharedPref = getActivity().getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        myEditor = sharedPref.edit();
        output = sharedPref.getString(ACCESS_TOKEN, null);
        user_id = sharedPref.getString("userid", null);
        itemDetails();
        getContacts();
        next.setEnabled(false);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValidPhoneNumber(autoCompleteTextView.getText().toString())&&! TextUtils.isEmpty(name.getText().toString().trim())) {
                    if (contact_array.contains(autoCompleteTextView.getText().toString())) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        editor.putString("cust_name",name.getText().toString());
                        editor.putString("cust_email",email.getText().toString());
                        editor.putString("cust_mobile",autoCompleteTextView.getText().toString()).apply();
                        editor.commit();

                        final Back_alert_class back_alert_class=new Back_alert_class(getActivity());
                        back_alert_class.confirmAlert();
                        back_alert_class.yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                back_alert_class.getMyDialogue().dismiss();
                                placeOrder();
                            }
                        });


                    } else {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        updateCustomer_Details();

                    }
                }
                else{
                    if(TextUtils.isEmpty(name.getText().toString().trim())){
                        custname.startAnimation(shakeError());
                    }
                    else{
                        custname.startAnimation(shakeError());
                    }
                }
            }

        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                next.setEnabled(true);
                next.setText("NEXT");
                next.setBackgroundColor(getResources().getColor(R.color.Text));
                getUserDetail(autoCompleteTextView.getText().toString());

            }
        });

        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (isValidPhoneNumber(autoCompleteTextView.getText().toString())&&name.getText().toString()!="") {

                    if (!contact_array.contains(autoCompleteTextView.getText().toString())) {

                        next.setText("SAVE & NEXT");
                        next.setEnabled(true);
                        next.setBackgroundColor(getResources().getColor(R.color.Text));
                    } else {
                        getUserDetail(autoCompleteTextView.getText().toString());
                    }
                }
                else {
                    custname.startAnimation(shakeError());
                }
            }
        });



        return  view;
    }

    public static final boolean isValidPhoneNumber(CharSequence target) {
        if (target.length()!=10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }



    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(600);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }
    /////////////////////get___Contacts////////////////
    public void getContacts( ){

        String url=ip+"uaa/b2b/contact-book/mobile-numbers/"+user_id;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public
            void onResponse(String response) {

                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        contact_array.add(jsonArray.getString(i));
                        Log.e("MOB",jsonArray.getString(i));
                    }
                    autoCompleteTextView.setAdapter(new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,contact_array));

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


                        case 401:

                            session.logoutUser(getActivity());
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
        RequestQueue requestQueue= Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }
    ///////////getUser_detailss///////////////


    public void getUserDetail(String mob ){

        String url = ip+"uaa/b2b/contact-book/get/mobile?";
        String uri=null;
        uri = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter("mobile",mob)
                .appendQueryParameter("user",user_id)
                .build().toString();
        StringRequest stringRequest=new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public
            void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    name.setText(jsonObject.getString("name"));
                    email.setText(jsonObject.getString("email"));
                    company_name.setText(jsonObject.getString("firmName"));
                    address.setText(jsonObject.getString("address"));
                    next.setEnabled(true);
                    next.setBackgroundColor(getResources().getColor(R.color.Text));

                }
                catch (Exception e) {
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

                        case 401:

                            session.logoutUser(getActivity());
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

    public void updateCustomer_Details() {

        JSONObject mainJasan= new JSONObject();
        try {

            mainJasan.put("name",name.getText().toString());
            mainJasan.put("mobileNumber",autoCompleteTextView.getText().toString());
            mainJasan.put("email",email.getText().toString());
            mainJasan.put("firmName",company_name.getText().toString());

            mainJasan.put("address",address.getText().toString());
            mainJasan.put("user",user_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = ip1+"/b2b/contact-book";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, mainJasan, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Snackbar.make(view,"Contact Addedd Successfully",Snackbar.LENGTH_SHORT).show();
                getContacts();

                editor.putString("cust_name",name.getText().toString());
                editor.putString("cust_email",email.getText().toString());
                editor.putString("cust_mobile",autoCompleteTextView.getText().toString()).apply();
                editor.commit();

                final Back_alert_class back_alert_class=new Back_alert_class(getActivity());
                back_alert_class.confirmAlert();
                back_alert_class.yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        back_alert_class.getMyDialogue().dismiss();
                        placeOrder();
                    }
                });
/*
                Customize_order_frag1 customize_order_frag1 = (Customize_order_frag1)  getActivity().getSupportFragmentManager().findFragmentByTag("customize");

                customize_order_frag1.getContacts();
                (getActivity()).getSupportFragmentManager().popBackStackImmediate();*/

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Snackbar.make(view,"Something went wrong!!",Snackbar.LENGTH_SHORT).show();

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
        RequestQueue queue = Volley.newRequestQueue(activity);
        queue.add(request);
    }

    public void itemDetails() {
        Bundle bundle=getActivity().getIntent().getExtras();
       String item_clicked = bundle.getString("product_id");

        String url = ip+"gate/b2b/catalog/api/v1/product/"+item_clicked+"?wholesaler="+user_id;
       Log.e("UUURRRRLL",url);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jp = jsonObject.getJSONObject("resourceSupport");
                    pro_name.setText(jp.getString("name"));
                    qty.setText("1");
                    JSONArray jsonArray1=jp.getJSONArray("links");
                    JSONObject jsonObject1=jsonArray1.getJSONObject(1);
                    imageurl=jsonObject1.getString("href");
                    Glide.with(getActivity()).load(imageurl).into(imageView);


                    Calendar cal = GregorianCalendar.getInstance();
                    try {
                        cal.add(Calendar.DAY_OF_YEAR, +jp.getInt("requiredDayesToDeliver"));
                        Date seven_days = cal.getTime();

                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        String formattedDate = df.format(seven_days);

                        days.setText(formattedDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                        days.setText("");
                    }



                    JSONArray jsonArray = jp.getJSONArray("specification");
                    try {
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject spesi_object=jsonArray.getJSONObject(i);
                            String heading=spesi_object.getString("heading");
                            if(heading.equalsIgnoreCase("Product Details")){
                                JSONArray attribute_aar=spesi_object.getJSONArray("attributes");
                                for(int j=0;j<attribute_aar.length();j++){
                                    JSONObject att_object=attribute_aar.getJSONObject(j);
                                    if( att_object.getString("key").equalsIgnoreCase("Gross Weight (gms)")){
                                        JSONArray avil_arra=att_object.getJSONArray("values");
                                        qrosswt.setText(avil_arra.get(0).toString());

                                    }

                                    if( att_object.getString("key").equalsIgnoreCase("Size")){
                                        JSONArray avil_arra=att_object.getJSONArray("values");
                                        length.setText(avil_arra.get(0).toString());

                                    }
                                }
                            }
                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "bearer " + output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void placeOrder() {



        String consign_name = sharedPref.getString("cust_name", null);
        String conignemail = sharedPref.getString("cust_email", null);
        String consignmob = sharedPref.getString("cust_mobile", null);

        JSONObject mainJasan = new JSONObject();
        String url = ip + "gate/b2b/order/api/v1/order/add";
        JSONObject json1 = new JSONObject();
        final JSONArray items_jsonArray = new JSONArray();
        try {
            json1.put("name", pro_name.getText().toString());
            json1.put("quantity", qty.getText().toString());
            json1.put("paymentStatus", "PENDING");
            json1.put("seller", user_id);
            json1.put("seal", seal.getText().toString());
            json1.put("expectedDeliveryDate", days.getText().toString());
            json1.put("grossWeight", qrosswt.getText().toString());
            json1.put("melting", melting.getText().toString());
            json1.put("productImage",imageurl);

            /* json1.put("totweight",weight.getText().toString());*/


            items_jsonArray.put(json1);
            mainJasan.put("items", items_jsonArray);

            mainJasan.put("paymentStatus", "PENDING");
            mainJasan.put("orderType", "ONLINECUSTOM_ORDER");
            mainJasan.put("consigneeName", consign_name);
            mainJasan.put("consigneeEmail", conignemail);
            mainJasan.put("consigneeNumber", consignmob);

            Log.e("OBJECT Status", mainJasan.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Map<String, String> params = new HashMap<>();
        params.put("customer", user_id);
        params.put("items", String.valueOf(items_jsonArray));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, mainJasan, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {



                    Snackbar.make(getView(), "Your Custom Order Placed Successfully !", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    activity.finish();

                } catch (Exception e) {
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
                headr.put("Authorization", "bearer " + output);
                headr.put("Content-Type", "application/json");
                return headr;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}



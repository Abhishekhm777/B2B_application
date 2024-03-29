package com.example.compaq.b2b_application.Fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.Customize_Oder_Adapter1;
import com.example.compaq.b2b_application.Adapters.Manage_Adapter;
import com.example.compaq.b2b_application.Helper_classess.Order_Placed_Splashfragment;
import com.example.compaq.b2b_application.Helper_classess.SessionManagement;
import com.example.compaq.b2b_application.Model.Offline_order_model;
import com.example.compaq.b2b_application.Model.Recy_model2;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.io.UnsupportedEncodingException;
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

import static com.example.compaq.b2b_application.Activity.Customize_Order.pager;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class Offline_order_customerdetail extends Fragment {
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
    public String output,user_id;
    public  String wholseller_id;
    Button button;
    public FragmentManager fragmentManager;
    public ArrayList<Offline_order_model> productlist;
    Manage_category_frag1.TextClicked mCallback;
    private StringBuilder category_path = new StringBuilder();
    private List list = new ArrayList();
    Dialog dialog;
    private String selected = "";
    RecyclerView recyclerView;
    HashMap<String, String> all_id = new HashMap<String, String>();
    public List<String> product;
    public List<String> contact_array=new ArrayList<>();
    @BindView(R.id.cust_no)
    AutoCompleteTextView autoCompleteTextView;
    private ImageView add_contact;
    @BindView(R.id.customer_nu)
    TextView cust_textview;
    @BindView(R.id.customer_na)TextView cust_name_textv;
    @BindView(R.id.cust_name)
    EditText cust_name;
    @BindView(R.id.gstn) EditText gstn;
    @BindView(R.id.address) EditText address;
    @BindView(R.id.cust_email) EditText email;
    @BindView(R.id.company_name) EditText company_name;
    @BindView(R.id.next) Button next;
    @BindView(R.id.offline_toolbar1) Toolbar toolbar;
    private Activity activity;
    private Dialog  myDialogue;
    public   JSONObject json1;
    public    JSONArray items_jsonArray;


    public Offline_order_customerdetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_offline_order_customerdetail, container, false);
            ButterKnife.bind(this,view);

            myDialogue = new Dialog(getContext());
            myDialogue.setContentView(R.layout.back_alert_dialog_layout);
            myDialogue.setCanceledOnTouchOutside(false);
           TextView yes=myDialogue.findViewById(R.id.yes);
           yes.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   myDialogue.dismiss();
                   setItemDetailsForOrder();


               }
           });
           TextView cancel=myDialogue.findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myDialogue.dismiss();
                }
            });
           TextView msg=(TextView) myDialogue.findViewById(R.id.popup_textview);
            msg.setText("           Do you wish to place this order?              ");
            myDialogue.getWindow()
                    .getAttributes().windowAnimations = R.style.DialogAnimation;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                toolbar.setNavigationIcon(R.drawable.back_btn);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    }
                });
            }
            activity=getActivity();
            sharedPref = getActivity().getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            output = sharedPref.getString(ACCESS_TOKEN, null);
            user_id = sharedPref.getString("userid", null);

            bundle=this.getArguments();
            productlist= bundle.getParcelableArrayList("arraylist");


            getContacts();



            next.setEnabled(false);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (isValidPhoneNumber(autoCompleteTextView.getText().toString())&&! TextUtils.isEmpty(cust_name.getText().toString().trim())) {
                        if (contact_array.contains(autoCompleteTextView.getText().toString())) {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                           myDialogue.show();


                        } else {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                          /*  updateCustomer_Details();*/

                        }
                    }
                    else{
                        if(TextUtils.isEmpty(cust_name.getText().toString().trim())){
                            cust_name_textv.startAnimation(shakeError());
                        }
                        else{
                            cust_textview.startAnimation(shakeError());
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
                    next.setText("PLACE ORDER");
                    next.setBackgroundColor(getResources().getColor(R.color.Text));
                    getUserDetail(autoCompleteTextView.getText().toString());

                }
            });

            cust_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (isValidPhoneNumber(autoCompleteTextView.getText().toString())&&cust_name.getText().toString()!="") {

                        if (!contact_array.contains(autoCompleteTextView.getText().toString())) {

                            next.setText("SAVE & NEXT");
                            next.setEnabled(true);
                            next.setBackgroundColor(getResources().getColor(R.color.Text));
                        } else {
                            getUserDetail(autoCompleteTextView.getText().toString());
                        }
                    }
                    else {
                        cust_textview.startAnimation(shakeError());
                    }
                }
            });




        }

        return view;

    }

    /////////////Validation/////////
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
                    cust_name.setText(jsonObject.getString("name"));
                    email.setText(jsonObject.getString("email"));
                    company_name.setText(jsonObject.getString("firmName"));
                    gstn.setText(jsonObject.getString("gstNumber"));
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

            mainJasan.put("name",cust_name.getText().toString());
            mainJasan.put("mobileNumber",autoCompleteTextView.getText().toString());
            mainJasan.put("email",email.getText().toString());
            mainJasan.put("firmName",company_name.getText().toString());
            mainJasan.put("gstNumber",gstn.getText().toString());
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
                pager.setCurrentItem(1);
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

    private void setItemDetailsForOrder(){
        Log.e("SIZEEEE",String.valueOf(productlist.size()));

        items_jsonArray=new JSONArray();
         String formattedDate="";

        Calendar cal = GregorianCalendar.getInstance();
        try {
            cal.add(Calendar.DAY_OF_YEAR, +30);
            Date seven_days = cal.getTime();

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            formattedDate= df.format(seven_days);


        }
        catch (Exception e){
            e.printStackTrace();

        }
        for(int i=0;i<productlist.size();i++){
            json1= new JSONObject();

            Offline_order_model offline_order_model=productlist.get(i);
          try {

              json1.put("name", offline_order_model.getName());
              json1.put("product", offline_order_model.getSku());
              json1.put("quantity", offline_order_model.getQuantity());
              json1.put("size", offline_order_model.getSize());
              json1.put("paymentStatus", "PAID");
              json1.put("productID", offline_order_model.getProduct_id());
              json1.put("seller", user_id);
              json1.put("expectedDeliveryDate",formattedDate);
              json1.put("grossWeight", offline_order_model.getWeight());
              json1.put("netWeight", offline_order_model.getWeight());
              json1.put("productImage", offline_order_model.getImg_url());

          }
          catch (JSONException e){
              e.printStackTrace();
          }
            items_jsonArray.put(json1);

        }

              placeOrder();
    }


    public void placeOrder() {

        String  customer_id = sharedPref.getString("userid", null);

        JSONObject mainJasan= new JSONObject();
        String  url=ip+"gate/b2b/order/api/v1/order/add";
        try {

            mainJasan.put("items",items_jsonArray);

            mainJasan.put("paymentStatus","PENDING");
            mainJasan.put("orderType","OFFLINE_SALES_ORDER");
            mainJasan.put("consigneeName",cust_name.getText().toString());
            mainJasan.put("consigneeEmail",email.getText().toString());
            mainJasan.put("consigneeNumber",autoCompleteTextView.getText().toString());
            mainJasan.put("consigneeGstNumber",gstn.getText().toString());


            Log.e("OBJECT Status", mainJasan.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Map<String, String> params = new HashMap<>();
        params.put("items", String.valueOf(items_jsonArray));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, mainJasan, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {


                   Order_Placed_Splashfragment order_placed_splashfragment = new Order_Placed_Splashfragment();
                   getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.offline_frame, order_placed_splashfragment, "splash").commit();

                  /*  Snackbar.make(getView(), "Your Offline Order Placed Successfully !", Snackbar.LENGTH_LONG)
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

                    String responseBody = null;
                    try {
                        responseBody = new String(response.data, "utf-8");
                        JSONObject data = new JSONObject(responseBody);

                        Log.e("ERERERERERER",responseBody);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


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
}

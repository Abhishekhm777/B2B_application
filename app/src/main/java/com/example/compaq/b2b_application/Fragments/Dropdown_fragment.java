package com.example.compaq.b2b_application.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Activity.Displaying_complete_product_details_Activity;
import com.example.compaq.b2b_application.Activity.MainActivity;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class Dropdown_fragment extends Fragment  {

    public static FragmentManager fragmentManager;
    public static TextView displayInteger;
    public static int minteger = 1;
    public Spinner spinner;
    private FragmentTransaction fragmentTransaction;
    public List<String> list;
   /* SharedPreferences pref;*/
    public Bundle bundle;
    public String item_clicked = "";
    public String item_id = "";
    public double item_wieght;
    public TextView  quantity_edittext, product_textview,descriptio;
    public Button cancelbtn, submitbtn;
    EditText weight_edittext,purity,size,length;
    public SharedPreferences sharedPref;
    public SharedPreferences.Editor myEditor;
    public String cartid, userid, seal, qty, weig,purity_t,size_t,length_t = "";
    public String user_cartid_de="";
  /* public SharedPreferences.Editor myEditor1;*/
    public int json_length=0;
    private static final String cartitems = "CART_ITEMS";
    private String output;
   /* SharedPreferences cart_shared_preference;
    SharedPreferences.Editor cartEditor;
*/
    String url="";
    private  String imageurl="";
    public Dropdown_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_dropdown_fragment, container, false);

        sharedPref =getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        myEditor = sharedPref.edit();
        sharedPref=getActivity().getSharedPreferences("USER_DETAILS",0);
         output=sharedPref.getString(ACCESS_TOKEN, null);

       /* cart_shared_preference = getActivity().getSharedPreferences(cartitems, Context.MODE_PRIVATE);
        cartEditor = cart_shared_preference.edit();
*/

       /* descriptio= (TextView) view.findViewById(R.id.desc_text);*/
        product_textview = (TextView) view.findViewById(R.id.product_id);
        cancelbtn = (Button) view.findViewById(R.id.cancel);
        submitbtn = (Button) view.findViewById(R.id.submit);
        quantity_edittext = (TextView) view.findViewById(R.id.result);
        weight_edittext = (EditText) view.findViewById(R.id.weight);
       /* length = (EditText) view.findViewById(R.id.length);
        size = (EditText) view.findViewById(R.id.size);
        purity = (EditText) view.findViewById(R.id.purity);*/

      /*  total_weight=(EditText)view.findViewById(R.id.totalweight);*/

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               minteger=1;
                getActivity().onBackPressed();
            }
        });
        displayInteger = (TextView) view.findViewById(R.id.result);
        Button button = (Button) view.findViewById(R.id.increas);
        Button button2 = (Button) view.findViewById(R.id.decrease);

        button.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                minteger = minteger + 1;
                display(minteger);
try {
    String d = weight_edittext.getText().toString();
    Double va = Double.parseDouble(d);
    /*weight_edittext.setText(String.valueOf(Math.abs(item_wieght + va)));*/
}
catch (Exception e){
    e.printStackTrace();
}

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minteger > 1) {
                    minteger = minteger - 1;
                    display(minteger);
                       try {
                           String d = weight_edittext.getText().toString();
                           Double va = Double.parseDouble(d);
                          /* weight_edittext.setText(String.valueOf(Math.abs(va - item_wieght)));*/
                       }
                       catch (Exception e){
                           e.printStackTrace();
                    }

                }
            }
        });
        list = new ArrayList<>();
        list.add("Available weights");
      /*  spinner=(Spinner)view.findViewById(R.id.avail_spinner);*/

        itemDetails();
        /*total_weight.setText(weight_edittext.getText().toString());*/




        final ArrayAdapter<String> country_adaper = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, list) {
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
       /* spinner.setAdapter(country_adaper);
       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position>0) {
                    Object item = parent.getItemAtPosition(position);


                    weight_edittext.setText(item.toString());
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/

        ////////////////////////submit button//////////////
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*seal = spinner.getSelectedItem().toString();*/
                weig = weight_edittext.getText().toString();
                qty = quantity_edittext.getText().toString();
               /* purity_t=purity.getText().toString();
                size_t=size.getText().toString();
                length_t=length.getText().toString();*/

              /*  sharedPref = getActivity().getSharedPreferences("User_information", 0);*/
                cartid = sharedPref.getString("cartid", "");
                userid = sharedPref.getString("userid", "");

                Log.e("user id", userid);
                Log.e("cart id", cartid);
                Log.e("qty", qty);
                Log.e("Wiegt", weig);
                Log.e("product", item_clicked);
                Log.e("product_id", item_id);


                updateCart();
            }
        });


        return view;
    }

    private void display(int number) {
        displayInteger.setText("" + number);
    }


    ///////////adding Seal list to drop down////////////////////////////////////////////////////
  /*  public void sealList() {

        String url = ip+"gate/b2b/order/api/v1/order/seal";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        list.add(jsonArray.getString(i));
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

                sharedPref = getActivity().getSharedPreferences("USER_DETAILS", 0);

                String output = sharedPref.getString(ACCESS_TOKEN, null);
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "bearer " + output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
*/
    /////////////////////////////////////////----item specification//////////////////////////
    public void itemDetails() {
        bundle = this.getArguments();
        item_clicked = bundle.getString("item_name");
        product_textview.setText(item_clicked);
        item_id = bundle.getString("Item_Clicked");
        String url = ip+"gate/b2b/catalog/api/v1/product/" + item_id;
        Log.e("URL PLEAS",url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jp = jsonObject.getJSONObject("resourceSupport");
                    JSONArray jsonArray1=jp.getJSONArray("links");
                    JSONObject jsonObject1=jsonArray1.getJSONObject(1);
                    imageurl=jsonObject1.getString("href");
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

                                            weight_edittext.setText(avil_arra.getString(0));
                                    }
                                }
                         }
                    }

                        /*item_wieght = Double.parseDouble(((jsonArray.getString(0))));
                        weight_edittext.setText(String.valueOf(item_wieght));*/

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


        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    ////////////////////////updating cart///////////////////////////////////////
   /* public void updateCart ( ){

        JSONObject json1= new JSONObject();
        final JSONArray items_jsonArray=new JSONArray();
        try {
            json1.put("product","GEK015");
            json1.put("productID","5c403bdfeb3c814f80b76fa2");
            json1.put("quantity",1);
            json1.put("advance","");
            json1.put("description","");
            json1.put("seal","MRK");
            json1.put("melting","");
            json1.put("rate","");
            json1.put("netWeight","44.820");

            items_jsonArray.put(json1);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url="https://server.mrkzevar.com/gate/b2b/order/api/v1/cart/update";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

Log.e("UPDATE CATR,,,,,,,", String.valueOf(jsonObject));

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
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer", String.valueOf(253));
                params.put("items", String.valueOf(items_jsonArray));
                params.put("id", String.valueOf(51));
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{

                pref=getActivity().getSharedPreferences("USER_DETAILS",0);

                String output=pref.getString(ACCESS_TOKEN, null);
                Map<String, String> headr = new HashMap<String, String>();
                headr.put("Authorization","bearer "+output);
                headr.put("Content-Type", "application/x-www-form-urlencoded");
                return headr;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }*/

    public void updateCart() {

        JSONObject mainJasan= new JSONObject();
        if(cartid.equalsIgnoreCase("0")){
            url=ip+"gate/b2b/order/api/v1/cart/add";


            JSONObject json1= new JSONObject();
            final JSONArray items_jsonArray=new JSONArray();
            try {
                json1.put("product",item_clicked);
                json1.put("productID",item_id);
                json1.put("quantity",qty);
                json1.put("advance","");
              /*  json1.put("description",descriptio.getText());
                json1.put("purity",purity_t);
                json1.put("length",length_t);
                json1.put("size",size_t);*/
                json1.put("netWeight",weig);
                json1.put("productImage",imageurl);
                json1.put("seller",sharedPref.getString("Wholeseller_id", null));

                items_jsonArray.put(json1);
                mainJasan.put("customer",userid);
                mainJasan.put("items",items_jsonArray);
                Log.e("format",mainJasan.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Map<String, String> params = new HashMap<>();
            params.put("customer", userid);
            params.put("items", String.valueOf(items_jsonArray));

        }
        else {
            url = ip+"gate/b2b/order/api/v1/cart/update";


            JSONObject json1= new JSONObject();
            final JSONArray items_jsonArray=new JSONArray();
            try {
                json1.put("product",item_clicked);
                json1.put("productID",item_id);
                json1.put("quantity",qty);
                json1.put("advance","");
               /* json1.put("description",descriptio.getText());
                json1.put("purity",purity_t);
                json1.put("length",length_t);
                json1.put("size",size_t);*/
                json1.put("netWeight",weig);
                json1.put("productImage",imageurl);
                json1.put("seller",sharedPref.getString("Wholeseller_id", null));

                items_jsonArray.put(json1);
                mainJasan.put("customer",userid);
                mainJasan.put("items",items_jsonArray);
                mainJasan.put("id",cartid);

                Log.e("format",mainJasan.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Map<String, String> params = new HashMap<>();
            params.put("customer", userid);
            params.put("items", String.valueOf(items_jsonArray));
            params.put("id", cartid);
        }




        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, mainJasan, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    String cartid=response.getString("id");




                    myEditor.putString("cartid",cartid);
                    myEditor.apply();
                    myEditor.commit();
                    JSONArray jsonArray=response.getJSONArray("items");
/*
                    Log.e("No of items in cart", String.valueOf(jsonArray.length()));
                    cart_shared_preference = getActivity().getSharedPreferences("CART_ITEMS", 0);
                   cartEditor.putString("no_of_items", String.valueOf(jsonArray.length())).apply();
                   cartEditor.commit();*/

                   /* cart_shared_preference = getActivity().getSharedPreferences("CART_ITEMS", 0);*/
                   json_length= Integer.parseInt(sharedPref.getString("no_of_items",""));
                    myEditor.putString("no_of_items", String.valueOf(json_length+1)).apply();
                    myEditor.commit();
                  /*  MainActivity mActivity= new MainActivity();
                    mActivity.setupBadge(getContext());*/
                   /* if(getActivity().getClass().getSimpleName().equalsIgnoreCase("Displaying_complete_product_details_Activity")){
                        Displaying_complete_product_details_Activity displayingcompleteproductdetailsActivity = new Displaying_complete_product_details_Activity();
                        displayingcompleteproductdetailsActivity.setupBadge(getContext());
                    }*/
                 /*  Displaying_complete_product_details_Activity displayingcompleteproductdetailsActivity= new Displaying_complete_product_details_Activity();
                    displayingcompleteproductdetailsActivity.setupBadge(getContext());*/
                    Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }

                // Go to next activity

                 catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                  Log.d("Error", String.valueOf(error));
                Snackbar.make(getView(), "Sorry! Something went wrong", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
/////////////////////////check the user cart details for item exist/////////////////////////

   /* public void userCart_details() {
        String url="https://server.mrkzevar.com/gate/b2b/order/api/v1/cart/customer/"+userid;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectss = new JsonObjectRequest (Request.Method.GET,url,null,

                new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {


                        try {

                            user_cartid_de=response.getString("id");
                            JSONArray jsonArray=response.getJSONArray("items");
                            json_length= jsonArray.length();

                           *//* cart_shared_preference = getActivity().getSharedPreferences("CART_ITEMS", 0);
                            cartEditor.putString("no_of_items", String.valueOf(jsonArray.length()+1)).apply();
                            cartEditor.commit();*//*
                            *//*MainActivity mActivity= new MainActivity();
                            mActivity.setupBadge(getContext());
                            Displaying_complete_product_details_Activity displayingcompleteproductdetailsActivity= new Displaying_complete_product_details_Activity();
                            displayingcompleteproductdetailsActivity.setupBadge(getContext());*//*


                            if (jsonArray.length() == 0 ) {
                                updateCart();
                            }

                            for(int i=0;i<jsonArray.length();i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String userCart_product_id = jsonObject1.getString("productID");

                                if(item_id.equalsIgnoreCase(userCart_product_id)) {
                                    Toast.makeText(getContext(), "Product is alreasy added to Cart ", Toast.LENGTH_SHORT).show();
                                    getActivity().onBackPressed();
                                   return;
                                }

                                if (i == jsonArray.length() - 1 ) {
                                    updateCart();
                                }
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                // ADD_CUSTOMER_CART();

            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                pref=getActivity().getSharedPreferences("USER_DETAILS",0);

                String output=pref.getString(ACCESS_TOKEN, null);

                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                return params;
            }

        };
        requestQueue.add(jsonObjectss);


    }*/

}



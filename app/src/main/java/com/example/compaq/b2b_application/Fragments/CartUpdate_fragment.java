package com.example.compaq.b2b_application.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Activity.Cart_Activity;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartUpdate_fragment extends Fragment {
    public static FragmentManager fragmentManager;
    public static TextView displayInteger;
    public  int minteger = 1;
    public Spinner spinner;
    private FragmentTransaction fragmentTransaction;
    public List<String> list;
    /* SharedPreferences pref;*/
    public Bundle bundle;
    public String item_clicked = "";
    public String item_id = "";
    public double item_wieght;
    public TextView weight_edittext, quantity_edittext,product_textview,descriptio;
    public Button cancelbtn, submitbtn;
    public SharedPreferences sharedPref;
    public SharedPreferences.Editor myEditor;
    public String product,descr,weight,qty,product_id,cartid,userid,purity_t,size_t,length_t;
    Button button,button2;
    private int del_id;
    private String url;
    private EditText purity,length,size;

    public CartUpdate_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_update_fragment, container, false);

        sharedPref =getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        cartid = sharedPref.getString("cartid", "");
        userid = sharedPref.getString("userid", "");

        product_textview = (TextView) view.findViewById(R.id.product_id);
        cancelbtn = (Button) view.findViewById(R.id.cancel);
        submitbtn = (Button) view.findViewById(R.id.submit);
        quantity_edittext = (TextView) view.findViewById(R.id.result);
        weight_edittext = (TextView) view.findViewById(R.id.weight);
        /*purity=(EditText)view.findViewById(R.id.purity);
        size=(EditText)view.findViewById(R.id.size);
          descriptio= (TextView) view.findViewById(R.id.desc_text);
        length=(EditText)view.findViewById(R.id.length);*/
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minteger=1;
                getActivity().onBackPressed();
            }
        });

        bundle = this.getArguments();
try {
    Double total_double = Double.parseDouble(bundle.getString("netweight"));
    String total = new DecimalFormat("#0.000").format(total_double);
    weight_edittext.setText(total);
}
catch (NumberFormatException e){
    e.printStackTrace();
}
        quantity_edittext.setText(bundle.getString("qty"));
        product_textview.setText(bundle.getString("item_name"));
       /* descriptio.setText(bundle.getString("desc"));
        purity.setText(bundle.getString("purity"));
        size.setText(bundle.getString("size"));
        length.setText(bundle.getString("length"));*/

try {
    item_wieght = Double.parseDouble(weight_edittext.getText().toString());
}
       catch (NumberFormatException e)
       {

       }

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                product=product_textview.getText().toString();
                qty=quantity_edittext.getText().toString();
                weight=weight_edittext.getText().toString();
                product_id=bundle.getString("Item_Clicked");
                del_id=Integer.parseInt(bundle.getString("delete"));
               /* purity_t=purity.getText().toString();
                descr=descriptio.getText().toString();
                size_t=purity.getText().toString();
                length_t=length.getText().toString();*/
                updateCart();


            }
        });

        displayInteger = (TextView) view.findViewById(R.id.result);
         button = (Button) view.findViewById(R.id.increas);
         button2 = (Button) view.findViewById(R.id.decrease);

        button.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                minteger = Integer.parseInt(quantity_edittext.getText().toString()) + 1;
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
                minteger = Integer.parseInt(quantity_edittext.getText().toString());
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
        return  view;
    }
    private void display(int number) {
        displayInteger.setText("" + number);
    }
    public void updateCart() {

        JSONObject mainJasan= new JSONObject();
        if(cartid.equalsIgnoreCase("0")){
            url=ip+"gate/b2b/order/api/v1/cart/update";


            JSONObject json1= new JSONObject();
            final JSONArray items_jsonArray=new JSONArray();
            try {
                json1.put("product",product);

                json1.put("quantity",Integer.parseInt(qty));
                json1.put("netWeight",Double.parseDouble(weight));
                json1.put("productID",product_id);
                json1.put("id",del_id);
              /*  json1.put("purity", purity_t);
                json1.put("size", size_t);
                json1.put("description",descr);
                json1.put("length", length_t);*/


                items_jsonArray.put(json1);
                mainJasan.put("customer",Integer.parseInt(userid));
                mainJasan.put("items",items_jsonArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Map<String, String> params = new HashMap<>();
            params.put("customer", userid);
            params.put("items", String.valueOf(items_jsonArray));


        }
        else {
            /*JSONObject mainJasan = new JSONObject();*/
            url = ip + "gate/b2b/order/api/v1/cart/update";


            JSONObject json1 = new JSONObject();
            final JSONArray items_jsonArray = new JSONArray();
            try {
                json1.put("product", product);
                json1.put("quantity", Integer.parseInt(qty));
                json1.put("netWeight", Double.parseDouble(weight));
                json1.put("productID", product_id);
              /*  json1.put("description", descr);
                json1.put("purity", purity_t);
                json1.put("size", size_t);
                json1.put("length", length_t);*/


                json1.put("id", del_id);
                items_jsonArray.put(json1);
                mainJasan.put("customer", Integer.parseInt(userid));
                mainJasan.put("items", items_jsonArray);
                mainJasan.put("id", Integer.parseInt(cartid));

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
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
                    ((Cart_Activity)getActivity()).reloadData();
                    BottomSheet.Builder builder = new BottomSheet.Builder(getContext());
                    builder.setTitle("Updated Successfully");
                    builder.show();
                }

                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", String.valueOf(error));
                BottomSheet.Builder builder = new BottomSheet.Builder(getContext());
                builder.setTitle("Sorry! something went wrong");
                builder.show();

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
}

package com.example.compaq.b2b_application.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.example.compaq.b2b_application.Adapters.Adress_adapter;
import com.example.compaq.b2b_application.Model.Address_model;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.compaq.b2b_application.MainActivity.ip1;
import static com.example.compaq.b2b_application.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.SessionManagement.PREF_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewAddess extends Fragment {
    Spinner country,state_spi,type;

    public List<String> country_spin;
    public List<String> state;
    public List<String> type_state;
public Bundle bundle;
    public String output;
    private String delete_id;
    public CheckBox checkBox;

    public AddNewAddess() {
        // Required empty public constructor
    }
TextView title;
Button save,cancel;
EditText pincode,city,area;
public String key;
    public SharedPreferences sharedPref;
    private OnGreenFragmentListener mCallback;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_new_addess, container, false);

   checkBox=(CheckBox)view.findViewById(R.id.check) ;
        title=(TextView)getActivity().findViewById(R.id.zoom);
        title.setText("ADD ADDRESS");
        country=(Spinner)view.findViewById(R.id.country);
        state_spi=(Spinner)view.findViewById(R.id.state);
        type=(Spinner)view.findViewById(R.id.type);
        sharedPref =getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        save=(Button)view.findViewById(R.id.save) ;
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(key==null){
                    if(pincode.getText().toString().equalsIgnoreCase(""))
                    {
                        pincode.setError("Field cannot be empty");
                        return;
                    }
                    if(city.getText().toString().equalsIgnoreCase("")){
                        city.setError("Field cannot be empty");
                        return;
                    }
                    if(area.getText().toString().equalsIgnoreCase("")){
                        area.setError("Field cannot be empty");
                        return;
                    }
                    save_address();
                }
                else {
                    update_address();
                }

            }
        });
        cancel=(Button)view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

          pincode=(EditText)view.findViewById(R.id.pincode);
        city=(EditText)view.findViewById(R.id.city) ;
        area=(EditText)view.findViewById(R.id.area) ;

        country_spin = new ArrayList<>();
        country_spin.add("Select country");
        country_spin.add("India");



        final ArrayAdapter<String> country_adaper = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, country_spin) {
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
        country.setAdapter(country_adaper);

        state=new ArrayList<>();
        state.add("Select State");
        state.add("Andhra Pradesh");
        state.add("Arunachal Pradesh");
        state.add("Assam");
        state.add("Bihar");
        state.add("Chhattisgarh");
        state.add("Goa");
        state.add("Gujarat");
        state.add("Haryana");
        state.add("Himachal Pradesh");
        state.add("Jammu and Kashmir");
        state.add("Jharkhand");
        state.add("Karnataka");
        state.add("Kerala");
        state.add("Madhya Pradesh");
        state.add("Maharashtra");
        state.add("Meghalaya");
        state.add("Nagaland");
        state.add("Odisha");
        state.add("Punjab");
        state.add("Tamil Nadu");
        state.add("Telangana");
        state.add("Tripura");
        state.add("Uttarakhand  ");
        state.add("Delhi");
        state.add("Puducherry");

        final ArrayAdapter<String> state_adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, state) {
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

        state_spi.setAdapter(state_adapter);


        type_state=new ArrayList<>();
        type_state.add("Select address type");
        type_state.add("CURRENT");
        type_state.add("HOME");
        type_state.add("OFFICE");

        final ArrayAdapter<String> type_adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, type_state) {
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
        type.setAdapter(type_adapter);


try {
    bundle = this.getArguments();
    key=bundle.getString("key");
    delete_id=bundle.getString("delte_id");
    title.setText("Edit Address");
    bundle.getString("country");
    pincode.setText(bundle.getString("pin"));
    city.setText(bundle.getString("city"));

    area.setText(bundle.getString("area"));

}
catch (NullPointerException e){

}
        return  view;
    }


    public  void save_address(){

        JSONObject mainJasan= new JSONObject();

        try {
            mainJasan.put("addressType",type.getSelectedItem().toString());
            mainJasan.put("line",area.getText().toString());
            mainJasan.put("city",city.getText().toString());
            mainJasan.put("state",state_spi.getSelectedItem().toString());
            mainJasan.put("country",country.getSelectedItem().toString());
            mainJasan.put("zipcode",pincode.getText().toString());
            mainJasan.put("user_id",sharedPref.getString("userid",null));





        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("OBJECT",mainJasan.toString());
        String url = ip1+"/b2b/api/v1/address/save/"+sharedPref.getString("userid",null);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url,mainJasan , new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {


                         String id=response.getString("id");

                    if(checkBox.isChecked()){
                        make_default(id);
                    }
                    /*mCallback.messageFromGreenFragment();*/
                    else {
                        Toast.makeText(getContext(), "Address Saved", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }





                } catch (Exception e) {

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


                output=sharedPref.getString(ACCESS_TOKEN, null);
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "bearer "+output);
                params.put("Content-Type", "application/json");
                return params;
            }

        };

        requestQueue.add(objectRequest);

    }


    public  void update_address(){

        JSONObject mainJasan= new JSONObject();

        try {
            mainJasan.put("addressType",type.getSelectedItem().toString());
            mainJasan.put("line",area.getText().toString());
            mainJasan.put("city",city.getText().toString());
            mainJasan.put("state",state_spi.getSelectedItem().toString());
            mainJasan.put("country",country.getSelectedItem().toString());
            mainJasan.put("zipcode",pincode.getText().toString());
            mainJasan.put("user_id",sharedPref.getString("userid",null));
            mainJasan.put("id",delete_id);





        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = ip1+"/b2b/api/v1/address/update/"+sharedPref.getString("userid",null);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, url,mainJasan , new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {



                    String id=response.getString("id");

                    if(checkBox.isChecked()){
                        make_default(id);
                    }
                    /*mCallback.messageFromGreenFragment();*/
                    else {
                        Toast.makeText(getContext(), "Address Updated", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }



                } catch (Exception e) {

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


                output=sharedPref.getString(ACCESS_TOKEN, null);
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "bearer "+output);
                params.put("Content-Type", "application/json");
                return params;
            }

        };

        requestQueue.add(objectRequest);

    }



    public void make_default (String id ){

        String url = ip1+"/b2b/api/v1/user/update/default/address/"+sharedPref.getString("userid",null)+"?addressID="+id;
        Log.d("MAKE",url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {


                    Snackbar.make(getView(), "New Default address added", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    getActivity().getSupportFragmentManager().popBackStack();

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
                params.put("Authorization","bearer "+output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };




        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    public interface OnGreenFragmentListener {
        void messageFromGreenFragment( );
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGreenFragmentListener) {
            mCallback = (OnGreenFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnGreenFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }


}

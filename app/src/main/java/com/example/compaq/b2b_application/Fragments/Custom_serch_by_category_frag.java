package com.example.compaq.b2b_application.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.Customize_Oder_Adapter1;
import com.example.compaq.b2b_application.Adapters.Manage_Adapter;
import com.example.compaq.b2b_application.Helper_classess.SessionManagement;
import com.example.compaq.b2b_application.Model.Recy_model2;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip_cat;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class Custom_serch_by_category_frag extends Fragment {
    private View view;
    ExpandableListView expListView;
    public ArrayList<String> listDataHeader = new ArrayList<String>();
    HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
    HashMap<String, String> list_id = new HashMap<String, String>();
    Customize_Oder_Adapter1 listAdapter;
    public Manage_Adapter manage_adapter;
    private int lastExpandedPosition = -1;
    public SharedPreferences sharedPref;
    public static   String URL_DATA="";
    private Bundle bundle;
    SessionManagement session;
    public String SUB_URL = "";
    public String sname = "";
    public int position = 0;
    String output;
    String wholseller_id;
    private Mange_card_fragment mange_card_fragment;
    ProgressBar progressBar;
    Button button;
    private Spinner spinner, spi;
    private FragmentTransaction fragmentTransaction;
    public FragmentManager fragmentManager;
    /*private String path="";*/
    private SearchView searchView,search_by_name;
    public ArrayList<Recy_model2> productlist;
    Manage_category_frag1.TextClicked mCallback;
    private    StringBuilder category_path = new StringBuilder();
    private  List  list=new ArrayList();

    Dialog dialog;
    private LinearLayout parentLinearLayout;
    private RelativeLayout newLayout;
    private String selected = "";
    RecyclerView recyclerView;
    TextView ok,cancel,main_text;
    HashMap<String, String> all_id = new HashMap<String, String>();
    public List<String> product;
    private Button clear,submit,add_product;
    private RadioButton byname,byCat;
    private LinearLayout cat_view,recy_view,second_search;

    public Custom_serch_by_category_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_custom_serch_by_category_frag, container, false);

        listAdapter = new Customize_Oder_Adapter1(getActivity(), listDataHeader, listDataChild, list_id, getFragmentManager(), view);

        dialog = new Dialog(getContext());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.alert_popup);
        ok=(TextView)dialog.findViewById(R.id.ok);
        cancel=(TextView)dialog.findViewById(R.id.cancel);
        main_text=(TextView)dialog.findViewById(R.id.popup_textview);
        clear=(Button)view.findViewById(R.id.clear) ;
        submit=(Button)view.findViewById(R.id.submit);
        searchView =  getActivity().findViewById(R.id.custom_search);
        searchView.setVisibility(View.GONE);


        sharedPref = getActivity().getSharedPreferences("USER_DETAILS", 0);

        session = new SessionManagement(getActivity().getApplicationContext());
        output = sharedPref.getString(ACCESS_TOKEN, null);


        parentLinearLayout = (LinearLayout)view. findViewById(R.id.linearLayout);


        spinner = (Spinner) view.findViewById(R.id.country);

        wholseller_id = sharedPref.getString("userid", null);
        /* catalog();*/
        prepareListData("Jewellery");

        //////////////////click events//////////////
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*  parentLinearLayout.removeAllViews();*/
                if(parentLinearLayout.getChildCount()>1) {
                    parentLinearLayout.removeViewAt(parentLinearLayout.getChildCount() - 1);
                    try {
                        /* path = path.substring(0, path.lastIndexOf(',', path.lastIndexOf(',')+1));*/

                        productlist.clear();
                        manage_adapter.notifyDataSetChanged();
                        recy_view.setVisibility(View.GONE);

                    }
                    catch (Exception e){

                    }
                }
                 if(parentLinearLayout.getChildCount()==1){
                     spi.setSelection(0);
                    }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                String path="Jewellery";
                Log.e("CAT PATH",path);
                int childcout= parentLinearLayout.getChildCount();
                for(int i=0;i<childcout;i++){
                    View v = parentLinearLayout.getChildAt(i);
                    Spinner spinne = v.findViewById(R.id.dynamic_spinner);
                    if(spinne.getSelectedItemPosition()>0) {
                        path += ","+ spinne.getSelectedItem().toString();
                        Log.e("HILO", spinne.getSelectedItem().toString());
                        Log.e("HILO_", path);
                    }
                }
               /* by_category(path);*/
                Log.e("YEHAAA",path);

                Custom_order_search_fragment custom_order_search_fragment=new Custom_order_search_fragment();
                Bundle bundle = new Bundle();
                bundle.putString("path", path);
                custom_order_search_fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.search_frame, custom_order_search_fragment).addToBackStack(null).commit();

            }
        });
        return view;
    }

    //////////////////////////categories/////////////////////
    private RequestQueue requestQueue;

    private void prepareListData(String item) {

        String qy = "Jewellery";
        int childcout = parentLinearLayout.getChildCount();
        for (int i = 0; i < childcout; i++) {
            View v = parentLinearLayout.getChildAt(i);
            Spinner spinner = v.findViewById(R.id.dynamic_spinner);
            if (spinner.getSelectedItemPosition() > 0) {
                qy += "," + spinner.getSelectedItem().toString();
            }
            Log.e("QYYY", qy);
        }
       /* if (selected.equalsIgnoreCase("")) {
            selected = item;

        } else {
            selected = selected.concat("," + item);

        }*/

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity());
        }

        URL_DATA = ip_cat + "/category/byFirstLevelCategory/b2b/" + qy + "?wholesaler=" + wholseller_id;
        Log.e("SPI", URL_DATA);


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


                        String sname = (jObj.getString("name").replaceAll("\\s", ""));

                        array.add(sname);


                        all_id.put(jObj.getString("name").replaceAll("\\s", ""), jObj.getString("id"));


                    }

                    /* spinn(array);*/
                    /*  dynamicSpinnerAdd(array);*/
                    onAddField(array);

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
                            BottomSheet.Builder builder = new BottomSheet.Builder(getActivity());
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

    ////////////////////

    public void onAddField(ArrayList<String> string) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        final View rowView = inflater.inflate(R.layout.spinner_layout, null);


        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams.setMargins(0, 15, 0, 15);
        rowView.setLayoutParams(buttonLayoutParams);
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());

        spi = rowView.findViewById(R.id.dynamic_spinner);


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, string) {
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
        spi.setAdapter(spinnerArrayAdapter);
        spi.post(new Runnable() {
            public void run() {
                spi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            return;
                        }
                        /*    ((ViewManager)newLayout.getParent()).removeView(newLayout);*/
                        String selectedItem = parent.getItemAtPosition(position).toString();
                        /*  path+=(","+selectedItem);*/
                        parentLinearLayout.removeView(parentLinearLayout.getRootView());
                       /* newLayout.setVisibility(View.VISIBLE);*/
                        prepareListData(selectedItem);
                        try {

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } // to close the onItemSelected

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
    }
}

package com.example.compaq.b2b_application.Fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.example.compaq.b2b_application.Adapters.PaginationScrollListener;
import com.example.compaq.b2b_application.Adapters.Seller_orderhistory_Adaper;
import com.example.compaq.b2b_application.Model.OrderTobe_customer_model;
import com.example.compaq.b2b_application.Model.Seller_order_model;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.Helper_classess.SessionManagement;


import org.json.JSONArray;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class Seller_Order_HIst_Frag1 extends Fragment implements Toolbar.OnMenuItemClickListener{

    public RecyclerView recyclerView;
    public Seller_orderhistory_Adaper seller_orderhistory_adaper;
    public ArrayList<Seller_order_model> productlist;
    public Bundle bundle;
    public String userid="";
    public SharedPreferences sharedPref;
    public   SharedPreferences.Editor myEditor;
    public String href="";
    private  String output;
    public JSONArray ja_data;
    public Button check_out;
    SessionManagement session;
    private String URL;
    private String fil_URL;
    private int number=0;
    private int number2=0;
    private HashMap<String, OrderTobe_customer_model> details=new HashMap<>();
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES ;
    private int currentPage = PAGE_START;
    private   HashMap<String,String> names=new HashMap<>();
    public Seller_Order_HIst_Frag1() {
    }
    private  View view;
    private LinearLayoutManager linearLayoutManager;
    private RelativeLayout progress;
    private Dialog dialog;
    private Spinner order_det,order_status;
    private  ArrayList<String> detail ;
    private  ArrayList<String> stat ;
    private Button clear,submit;
    private EditText value;
    private TextView fromdate,to_date;
    private int mYear,mMonth,mDay;
    private String wholesaler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       if(view==null) {
           view = inflater.inflate(R.layout.fragment_seller__order__hist__frag1, container, false);
           sharedPref = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
           setHasOptionsMenu(true);
           recyclerView = (RecyclerView) view.findViewById(R.id.seller_history);
           productlist = new ArrayList<>();
           progress=(RelativeLayout) view.findViewById(R.id.progress);
           progress.setVisibility(View.VISIBLE);
         /*  recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
           recyclerView.setHasFixedSize(true);*/
           dialog = new Dialog(getContext());
           dialog.setCanceledOnTouchOutside(true);
           dialog.setContentView(R.layout.order_his_filter_layout);
           order_det=(Spinner)dialog.findViewById(R.id.orer_details) ;
           order_status=(Spinner)dialog.findViewById(R.id.order_status) ;
           clear=(Button) dialog.findViewById(R.id.clear) ;
           fil_URL=ip + "gate/b2b/order/api/v1/item/listitem/" + sharedPref.getString("userid", "");
           clear.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   dialog.dismiss();
                   value.setText("");
                   fromdate.setText("");
                   to_date.setText("");
                   isLastPage=false;
                   isLoading=false;
                   productlist.clear();
                   seller_orderhistory_adaper.notifyDataSetChanged();
                   currentPage = 0;
                   URL=ip + "gate/b2b/order/api/v1/item/listitem/" + sharedPref.getString("userid", "");
                   Seller_history(URL+"?page=0&size=15");

               }
           });
           submit=(Button)dialog.findViewById(R.id.submit) ;
           fromdate=(TextView) dialog.findViewById(R.id.fromdate);
           to_date=(TextView) dialog.findViewById(R.id.todate);
           value=(EditText)dialog.findViewById(R.id.values);

           to_date.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   final Calendar c = Calendar.getInstance();
                   mYear = c.get(Calendar.YEAR);
                   mMonth = c.get(Calendar.MONTH);
                   mDay = c.get(Calendar.DAY_OF_MONTH);
                   DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                           new DatePickerDialog.OnDateSetListener() {

                               @Override
                               public void onDateSet(DatePicker view, int year,
                                                     int monthOfYear, int dayOfMonth) {

                                   String dateString = String.format("%02d-%02d-%02d", year,  monthOfYear+1, dayOfMonth);
                                   to_date.setText(dateString);

                               }
                           }, mYear, mMonth, mDay);
                   datePickerDialog.show();
               }


           });
          fromdate.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  final Calendar c = Calendar.getInstance();
                  mYear = c.get(Calendar.YEAR);
                  mMonth = c.get(Calendar.MONTH);
                  mDay = c.get(Calendar.DAY_OF_MONTH);

                  final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                          new DatePickerDialog.OnDateSetListener() {

                              @Override
                              public void onDateSet(DatePicker view, int year,
                                                    int monthOfYear, int dayOfMonth) {
                                  String dateString = String.format("%02d-%02d-%02d", year,  monthOfYear+1, dayOfMonth);

                                  fromdate.setText(dateString);
                              }
                          }, mYear, mMonth, mDay);
                  datePickerDialog.show();
              }

          });
           submit.setOnClickListener(new View.OnClickListener() {
               String key;
               String val;
               @Override
               public void onClick(View view) {
                   dialog.dismiss();
                   String uri=null;
                   if(order_det.getSelectedItem().toString().equalsIgnoreCase("Mob.No.")&&!value.getText().toString().equalsIgnoreCase("")){
                        key="mobileNo";
                        val=" 91"+value.getText().toString();
                       try {
                           uri = Uri.parse(URL)
                                   .buildUpon()
                                   .appendQueryParameter(key,URLEncoder.encode(val, "UTF-8") ).build().toString();
                       } catch (UnsupportedEncodingException e) {
                           e.printStackTrace();
                       }

                   }
                       if (order_det.getSelectedItem().toString().equalsIgnoreCase("Order No.")){
                            key = "orderNo";
                            val = value.getText().toString();
                           uri = Uri.parse(URL)
                                   .buildUpon()
                                   .appendQueryParameter(key,val ).build().toString();
                   }
                   if (order_det.getSelectedItem().toString().equalsIgnoreCase("Item No.")){
                       key = "orderNo";
                       val = value.getText().toString();
                       uri = Uri.parse(URL)
                               .buildUpon()
                               .appendQueryParameter(key,val ).build().toString();
                   }
                   if(uri==null) {
                       Log.e("UUUUUUUUUURRRRRR",fil_URL);
                       uri = Uri.parse(fil_URL).
                               buildUpon().build().toString();
                   }
                   if(!order_status.getSelectedItem().toString().equalsIgnoreCase("Select")){
                       uri = Uri.parse(uri)
                               .buildUpon()
                               .appendQueryParameter("orderStatus",order_status.getSelectedItem().toString()).build().toString();
                       Log.e("TESST", uri);
                   }
                    if(!fromdate.getText().toString().equalsIgnoreCase("")&&!to_date.getText().toString().equalsIgnoreCase("")) {
                        try {
                            uri = Uri.parse(uri)
                                    .buildUpon()
                                    .appendQueryParameter("fromDate", URLEncoder.encode(fromdate.getText().toString(), "UTF-8"))
                                    .appendQueryParameter("toDate", URLEncoder.encode(to_date.getText().toString(), "UTF-8"))
                                    .build().toString();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Log.e("uri2", uri);
                        productlist.clear();
                        seller_orderhistory_adaper.notifyDataSetChanged();
                        URL=uri;
                          Seller_history(uri);
                    }
                    else {
                        uri = Uri.parse(uri)
                                .buildUpon().build().toString();
                      productlist.clear();
                      seller_orderhistory_adaper.notifyDataSetChanged();
                      URL=uri;
                      currentPage=0;
                      Seller_history(uri);
                    }
               }
           });
           detail = new ArrayList<>();
           detail.add("Select");
           detail.add("Mob.No.");
           detail.add("Order No.");
           detail.add("Item No.");
           final ArrayAdapter<String> pri = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, detail) {
               @Override
               public boolean isEnabled(int position) {
                   return true;
               }
           };
           order_det.setAdapter(pri);
           stat = new ArrayList<>();
           stat.add("Select");
           stat.add("ORDERED");
           stat.add("PROCESSING");
           stat.add("CANCELLED");
           stat.add("DELIVERED");
           stat.add("ACCEPTED");
           stat.add("AVAILABLE");
           stat.add("CONFIRMED");
           stat.add("SHIPPED");
           stat.add("REJECTED");
           stat.add("PACKING");
           final ArrayAdapter<String> st = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, stat) {
               @Override
               public boolean isEnabled(int position) {
                   return true;
               }
           };
           order_status.setAdapter(st);
           Toolbar toolbar= (Toolbar) getActivity().findViewById(R.id.cart_toolbar);
           toolbar.setOnMenuItemClickListener(this);
           sharedPref = getActivity().getSharedPreferences("USER_DETAILS", 0);
               wholesaler= sharedPref.getString("userid", "");
           output = sharedPref.getString(ACCESS_TOKEN, null);
           URL = ip + "gate/b2b/order/api/v1/item/listitem/" + sharedPref.getString("userid", "");
          /*  if (currentPage <= TOTAL_PAGES) seller_orderhistory_adaper.addLoadingFooter();
            else isLastPage = true;*/
           Seller_history(URL+"?page=0&size=15");
           linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
           recyclerView.setLayoutManager(linearLayoutManager);
               seller_orderhistory_adaper = new Seller_orderhistory_Adaper(getActivity(), productlist, details);
               recyclerView.setAdapter(seller_orderhistory_adaper);
       }
        return  view;
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }
RequestQueue requestQueue;
    public void Seller_history(String url) {
        Log.e("URRLss",url);
        number=0;
        number2=0;
        if(requestQueue==null)
        {
            requestQueue = Volley.newRequestQueue(getContext());

        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("response..",response+"");
                            ArrayList<String> oder_list=new ArrayList<>();
                            JSONObject obj = new JSONObject(response);
                            JSONObject jsonObject=obj.getJSONObject("page");
                            if(jsonObject.getInt("totalElements")==0){
                                Snackbar.make(view,"No Orders For this Filter",Snackbar.LENGTH_SHORT).show();
                            }
                            JSONObject obj1 = obj.getJSONObject("_embedded");

                            TOTAL_PAGES=jsonObject.getInt("totalPages");
                            Log.e("TOTAL", String.valueOf(TOTAL_PAGES));

                            JSONArray ja_data = obj1.getJSONArray("itemList");
                            for (int i = 0; i < ja_data.length(); i++) {
                                try {
                                    //do something with 'source'
                                    JSONObject jObj = ja_data.getJSONObject(i);
                                    JSONObject link_object = jObj.getJSONObject("_links");
                                    JSONObject order_object = link_object.getJSONObject("order");
                                    oder_list.add(order_object.getString("href"));
                                } catch (Exception e) { // catch any exception
                                    continue; // will just skip this iteration and jump to the next
                                }

                            }
                            order_urls(oder_list);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //displaying the error in toast if occurrs
                error.printStackTrace();

                NetworkResponse response = error.networkResponse;
                if(response != null ){
                    switch(response.statusCode){
                        case 404:
                            check_out.setVisibility(View.GONE);
                            BottomSheet.Builder builder = new BottomSheet.Builder(getActivity());
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;
                        case 401:
                            Snackbar.make(getView(),"Session expiered",Snackbar.LENGTH_LONG).show();
                            break;
                        case 500:
                            Snackbar.make(getView(),"Sorry something went wrong",Snackbar.LENGTH_LONG).show();
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
                Log.e("request send data", params.toString());
                return params;
            }

        };

        requestQueue.add(stringRequest);
    }

    private void order_urls(final ArrayList<String> list){
        for(int i=0;i<list.size();i++){
            detail(list.get(i),list);
        }
    }

    public void detail(String url, final ArrayList<String> li) {

        if(requestQueue==null)
        {
            requestQueue = Volley.newRequestQueue(getContext());

        }
        Log.d("urls",url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url
                ,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            ++number;
                            JSONObject obj = new JSONObject(response);
                            JSONObject obj1 = obj.getJSONObject("order");

                            String order_no=obj1.getString("orderNo");
                            String orderType=obj1.getString("orderType");
                            String consigneeName=obj1.getString("consigneeName");
                            String consigneeNumber=obj1.getString("consigneeNumber");
                            String s1 = obj1.getString("createdDate");
                            String[] order_date1 = s1.split("T");
                            String order_date = order_date1[0];

                            JSONArray ja_data = obj1.getJSONArray("items");

                            if(!orderType.equalsIgnoreCase("OFFLINE_SALES_ORDER")){
                                names.put(order_no,obj1.getString("customer"));
                            }

                            if(orderType.equalsIgnoreCase("ONLINE_SALES_ORDER")) {
                                productlist.add(new Seller_order_model(order_no, "ONLINE", order_date, String.valueOf(ja_data.length()), consigneeName, consigneeNumber));
                            }

                            if(orderType.equalsIgnoreCase("ONLINECUSTOM_ORDER")){
                                productlist.add(new Seller_order_model(order_no, "CUSTOM", order_date, String.valueOf(ja_data.length()), consigneeName, consigneeNumber));
                            }
                            if(orderType.equalsIgnoreCase("OFFLINE_SALES_ORDER")){
                                productlist.add(new Seller_order_model(order_no, "OFFLINE", order_date, String.valueOf(ja_data.length()), consigneeName, consigneeNumber));
                            }

                            if(number==li.size()) {
                                Log.e("SZEEEE",String.valueOf(names.size()));
                                user_detail(names);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //displaying the error in toast if occurrs


                NetworkResponse response = error.networkResponse;
                Log.e("Status" , String.valueOf(response.statusCode));
                if(response != null ){
                    switch(response.statusCode){
                        case 404:
                            check_out.setVisibility(View.GONE);
                            BottomSheet.Builder builder = new BottomSheet.Builder(getActivity());
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;
                        case 401:
                            Snackbar.make(getView(),"Session expiered",Snackbar.LENGTH_LONG).show();
                            break;
                        case 500:
                            Snackbar.make(getView(),"Sorry something went wrong",Snackbar.LENGTH_LONG).show();
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


        requestQueue.add(stringRequest);
    }

    private void user_detail(HashMap<String, String> nam){
        for ( Map.Entry<String, String> entry : nam.entrySet()) {
            String order = entry.getKey();
            String id = entry.getValue();
            if(id=="null") {
                ++ number2;
                continue;
            }
            else {
                customer_detail(id, order, nam);
            }

        }
    }

    public void customer_detail(String id, final String no, final  HashMap<String, String> nam) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                ip+"gate/b2b/order/api/v1/getUser/"+id,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            ++ number2;
                            JSONObject jsonObject = new JSONObject(response);

                            OrderTobe_customer_model orderTobe_customer_model=new OrderTobe_customer_model(jsonObject.getString("firstName"),jsonObject.getString("mobileNumber"));

                            details.put(no,orderTobe_customer_model);

                            if(number2==nam.size()) {
                                progress.setVisibility(View.GONE);
                              seller_orderhistory_adaper.notifyDataSetChanged();

                            }
                                recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
                                    @Override
                                    protected void loadMoreItems() {

                                        Log.e("LAAAAASt PAGE",String.valueOf(linearLayoutManager.getChildCount()));
                                        isLoading = true;
                                        currentPage += 1;
                                        seller_orderhistory_adaper.addLoadingFooter();
                                        recyclerView.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    seller_orderhistory_adaper.notifyItemInserted(productlist.size());

                                                }
                                        });
                                        // mocking network delay for API call
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (currentPage != TOTAL_PAGES) {
                                                    isLoading=false;
                                                    seller_orderhistory_adaper.removeLoadingFooter();

                                                    String uri=null;
                                                     uri = Uri.parse(URL)
                                                            .buildUpon()
                                                            .appendQueryParameter("page",String.valueOf(currentPage))
                                                             .appendQueryParameter("size","15")
                                                             .build().toString();

                                                    Seller_history(uri);

                                                  /*  Seller_history(URL.concat("?page="+currentPage+"&size=15"));*/

                                                }
                                              /*  Seller_history(URL.concat("?page="+1+"&size=15"));*/
                                            }
                                        }, 500);

                                    }

                                    @Override
                                    public int getTotalPageCount() {
                                        return TOTAL_PAGES;
                                    }

                                    @Override
                                    public boolean isLastPage() {
                                        return isLastPage;
                                    }

                                    @Override
                                    public boolean isLoading() {
                                        return isLoading;
                                    }
                                });
                        }
                        catch (Exception e) {

                            seller_orderhistory_adaper.removeLoadingFooter();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //displaying the error in toast if occurrs
                NetworkResponse response = error.networkResponse;
                Log.e("Status" , String.valueOf(response.statusCode));
                if(error != null ){
                    switch(response.statusCode){
                        case 404:
                            BottomSheet.Builder builder = new BottomSheet.Builder(getActivity());
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;

                        case 401:
                            Snackbar.make(view,"Session expiered",Snackbar.LENGTH_LONG).show();
                            break;
                        case 500:
                            Snackbar.make(view,"Sorry something went wrong",Snackbar.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        requestQueue.add(stringRequest);
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.filter:
                // click on 'up' button in the action bar, handle it here
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

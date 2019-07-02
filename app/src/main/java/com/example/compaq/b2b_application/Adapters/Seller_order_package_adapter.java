package com.example.compaq.b2b_application.Adapters;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Cart_Activity;
import com.example.compaq.b2b_application.Fragments.Company_info_fragment;
import com.example.compaq.b2b_application.Fragments.Seller_order_package;
import com.example.compaq.b2b_application.MainActivity;
import com.example.compaq.b2b_application.Model.Seller_order_package_model;
import com.example.compaq.b2b_application.Policy_activity;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.MainActivity.ip;
import static com.example.compaq.b2b_application.MainActivity.ip1;
import static com.example.compaq.b2b_application.MainActivity.seller_t;
import static com.example.compaq.b2b_application.SessionManagement.ACCESS_TOKEN;

public class Seller_order_package_adapter extends RecyclerView.Adapter<Seller_order_package_adapter.MyViewHolder> {
    private ArrayList<Seller_order_package_model> productlist;
    Context mContext;
    Dialog dialog,dialog2;
    TextView ok,cancel,main_text,reject2,cancell;
     Snackbar snackbar;
    String output,task_id,wholeseller_id;
    View view;
    String id;
    String no;
    int pos;
     com.example.compaq.b2b_application.Model.Seller_order_package_model listner2;
    SharedPreferences sharedPref;
    HashMap<String, String> image_map;
   /* private CallBack mCallback;

    public interface CallBack{
        public void sendText(String text);
    }*/
    private int p=0;
    public Seller_order_package_adapter(Context applicationContext, ArrayList<Seller_order_package_model> productlist, View view, HashMap<String, String> image_map) {
        this.mContext=applicationContext;
        this.productlist=productlist;
        this.view=view;
        this.image_map=image_map;

        for(int i=0;i<productlist.size();i++){
            final com.example.compaq.b2b_application.Model.Seller_order_package_model listner=  productlist.get(i);
            if(listner.getItem_status().equalsIgnoreCase("REJECTED")||listner.getItem_status().equalsIgnoreCase("DELIVERED")||listner.getItem_status().equalsIgnoreCase("CANCELLED")){
                ++p;
            }
        }
        if(p==productlist.size()){
            snackbar = Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_INDEFINITE);

            View custom = LayoutInflater.from(mContext).inflate(R.layout.snackbar_layout, null);
            snackbar.getView().setPadding(0,0,0,0);
            ((ViewGroup) snackbar.getView()).removeAllViews();
            ((ViewGroup) snackbar.getView()).addView(custom);
            TextView close = custom.findViewById(R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            });
            TextView textView = custom.findViewById(R.id.edit);
            textView.setText(" If you complete the task it will be cleared from the order processing,If you want the details you can check it in order history");
            snackbar.show();
            Button button=custom.findViewById(R.id.complete);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    complete(task_id);
                    snackbar.dismiss();
                }
            });
        }

      /*  try {
            mCallback = (CallBack) mContext;
        } catch (ClassCastException e) {
            throw new ClassCastException(mContext.toString()
                    + " must implement TextClicked");
        }*/
    }

    @NonNull
    @Override

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_package_layout, parent, false);
        dialog = new Dialog(mContext);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.alert_popup);

        ok=(TextView)dialog.findViewById(R.id.ok);
        cancel=(TextView)dialog.findViewById(R.id.cancel);
        main_text=(TextView)dialog.findViewById(R.id.popup_textview);


        dialog2 = new Dialog(mContext);
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setContentView(R.layout.rejecting_popup);
        reject2=(TextView)dialog2.findViewById(R.id.reject);
        cancell=(TextView)dialog2.findViewById(R.id.cancell);


        sharedPref=mContext.getSharedPreferences("USER_DETAILS",0);
        wholeseller_id=sharedPref.getString("Wholeseller_id",null);
        output=sharedPref.getString(ACCESS_TOKEN, null);


        return new MyViewHolder(itemView);
    }
    public void updateData() {
        if(p==productlist.size()){
            final Snackbar snackbar = Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_INDEFINITE);

            View custom = LayoutInflater.from(mContext).inflate(R.layout.snackbar_layout, null);
            snackbar.getView().setPadding(0,0,0,0);
            ((ViewGroup) snackbar.getView()).removeAllViews();
            ((ViewGroup) snackbar.getView()).addView(custom);
            TextView close = custom.findViewById(R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            });
            TextView textView = custom.findViewById(R.id.edit);
            textView.setText(" If you complete the task it will be cleared from the order processing,If you want the details you can check it in order history");
            snackbar.show();
        }
    }
    @Override
    public void onBindViewHolder(@NonNull final Seller_order_package_adapter.MyViewHolder holder, final int position) {
        final com.example.compaq.b2b_application.Model.Seller_order_package_model listner=  productlist.get(position);
        task_id=listner.getTaskid();
        holder.product.setText(listner.getProduct());
        holder.quantity.setText(listner.getQty());
        holder.order_no.setText(listner.getItem_no());
        holder.excepted.setText(listner.getExpected());

        if(!listner.getLenght().equals("null")){
            holder.deleverd.setText(listner.getLenght());
        }

       /* holder.length.setText(listner.getLenght());
        holder.size.setText(listner.getSize());
        holder.purity.setText(listner.getPurity());*/
        String url=  listner.getImage_url();
        if(url.equalsIgnoreCase("null")){

            Glide.with(mContext).load(image_map.get(listner.getProduct())).into(holder.imageView);
        }else {
            Glide.with(mContext).load(url).into(holder.imageView);
        }
        holder.weight.setText(listner.getWeight());

        holder.status.setTextColor(Color.RED);

        holder.status.setText(listner.getItem_status());
        if(listner.getItem_status().equalsIgnoreCase("AVAILABLE")){
            holder.accept.setText("DELIVER");
            holder.update.setVisibility(View.VISIBLE);
            holder.notify_layout.setVisibility(View.VISIBLE);
        }

          if(listner.getItem_status().equalsIgnoreCase("PROCESSING")){
              holder.accept.setText("AVAILABLE");
              holder.status.setTextColor(Color.MAGENTA);
          }
          if(listner.getItem_status().equalsIgnoreCase("ORDERED")){
              holder.status.setText("New Order");
              holder.update.setVisibility(View.GONE);
              holder.accept.setText("ACCEPT");
              holder.status.setTextColor(Color.BLUE);
          }
        if(listner.getItem_status().equalsIgnoreCase("REJECTED")){
            holder.linearLayout.setVisibility(View.GONE);
          /*  Snackbar.make(view, " If you complete the task it will be cleared from the order processing,If you want the details you can check it in order history",
                    Snackbar.LENGTH_INDEFINITE).setAction("Complete", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission

                }
            }).show();*/

        }
        if(listner.getItem_status().equalsIgnoreCase("CANCELLED")){
            holder.linearLayout.setVisibility(View.GONE);
        }

        if(listner.getItem_status().equalsIgnoreCase("DELIVERED")){
          holder.linearLayout.setVisibility(View.GONE);
            holder.status.setTextColor(Color.GREEN);
        }
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.accept.getText().toString().equalsIgnoreCase("ACCEPT")) {
                    id=listner.getId();
                    no=listner.getItem_no();
                    pos=holder.getAdapterPosition();
                    listner2=listner;
                    dialog.show();
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                              accept(id,listner2,pos);
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }

                if(holder.accept.getText().toString().equalsIgnoreCase("AVAILABLE")){
                    id=listner.getId();
                    no=listner.getItem_no();
                    pos=holder.getAdapterPosition();
                    listner2=listner;
                    main_text.setText("Do you want to make this order available ?");
                    dialog.show();
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            maekAvailable(id,listner2,pos);
                        }
                    });
                }
                if(holder.accept.getText().toString().equalsIgnoreCase("DELIVER")) {
                    id=listner.getId();
                    no=listner.getItem_no();
                    pos=holder.getAdapterPosition();
                    listner2=listner;
                    if (!listner.getLenght().equals("null")) {

                        main_text.setText("Do you want to deliver this order ?");
                        dialog.show();
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                deliver(id, listner2, pos);
                            }
                        });

                    }
                    else {

                           Snackbar.make(view, " Please add Delivered weight before delivering.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                }
            }).show();
                    }
                }
            }
        });
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.deleverd.getText().toString().equalsIgnoreCase("")){
                    Snackbar.make(view,"Please add Delevered weight before update",Snackbar.LENGTH_SHORT).show();
                }
                else{
                     update(listner.getId(),listner.getItem_status(),listner.getExpected(),listner.getProduct(),listner.getWeight(),listner.getProduct_id(),listner.getPay_status(),listner.getItem_no(),holder.deleverd.getText().toString(),holder.description.getText().toString());
                }
            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                 id=listner.getId();
                 no=listner.getItem_no();
                 pos=holder.getAdapterPosition();
                listner2=listner;
                dialog2.show();
            }
        });
        reject2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
                Log.e("ID",id);
                Log.e("ITEM_NO",no);
                Log.e("POSITION",String.valueOf(pos));

                reject(id,no,listner2,pos);
            }
        });
        cancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2 .dismiss();
            }
        });

        holder.notify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                main_text.setText("Do you want to Notify User about Product Availability ?");
                dialog.show();
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    String mobile_no=listner.getCust_mobile();
                    String item_no=listner.getItem_no();
                    @Override
                    public void onClick(View view) {
                        notify_user(mobile_no,item_no);
                    }
                });
            }
        });



    }
    @Override
    public int getItemCount() {
        return productlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView product,order_no,status,order_type,weight,quantity,description,excepted;
        EditText purity,size,length,deleverd;
        Button accept,reject,update,notify_button;
        LinearLayout linearLayout,notify_layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.imageview);
            product=(TextView) itemView.findViewById(R.id.check_produc_name);
            order_no=(TextView) itemView.findViewById(R.id.check_orderno);
            status=(TextView) itemView.findViewById(R.id.status);
            order_type=(TextView) itemView.findViewById(R.id.check_type);
            weight=(TextView) itemView.findViewById(R.id.check_weight);
            quantity=(TextView)itemView.findViewById(R.id.check_quantity);
            excepted=(TextView)itemView.findViewById(R.id.expected);
            deleverd=(EditText)itemView.findViewById(R.id.delivered_weight);
         /*   purity=(EditText)itemView.findViewById(R.id.check_purity);
            size=(EditText)itemView.findViewById(R.id.check_size);
            length=(EditText)itemView.findViewById(R.id.check_length);*/
            description=(EditText)itemView.findViewById(R.id.check_desc);
            accept=(Button)itemView.findViewById(R.id.accept_btn);
            update=(Button)itemView.findViewById(R.id.update);
            reject=(Button)itemView.findViewById(R.id.reject_btn);
            notify_button=(Button)itemView.findViewById(R.id.notify_button);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.buttons);
            notify_layout=(LinearLayout)itemView.findViewById(R.id.notify_layout);

        }
    }

    public  void accept(final String id, final Seller_order_package_model listner,final int position){
            String url = ip+"gate/b2b/order/api/v1/item/update/status/"+id+"/?orderStatus=PROCESSING&retailerId="+sharedPref.getString("userid",null);
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest objectRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    Toast.makeText(mContext, "Order Accepted", Toast.LENGTH_SHORT).show();


                     listner.setItem_status("PROCESSING");
                     notifyItemChanged(position);

                    updateData();


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
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "bearer "+output);
                params.put("Content-Type", "application/json");
                return params;
            }

        };

        requestQueue.add(objectRequest);

    }

    public  void reject(final String id, final String order_no, final  Seller_order_package_model listner,final int pose){
        String url = ip+"gate/b2b/order/api/v1/task/shipping/rejecting?items="+id+"&orderNo="+order_no;
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest objectRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    Toast.makeText(mContext, "Order Rejected ", Toast.LENGTH_SHORT).show();

                    listner.setItem_status("REJECTED");
                    notifyItemChanged(pose);
                    updateData();

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
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "bearer "+output);
                params.put("Content-Type", "application/json");
                return params;
            }

        };

        requestQueue.add(objectRequest);

    }

    public  void deliver(final String id, final Seller_order_package_model listner, final  int position){
        String url = ip+"gate/b2b/order/api/v1/item/update/status/"+id+"/?orderStatus=DELIVERED&retailerId="+sharedPref.getString("userid",null);
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest objectRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    Toast.makeText(mContext, "Order Deliverd", Toast.LENGTH_SHORT).show();

                    listner.setItem_status("DELIVERED");
                    notifyItemChanged(position);
                    updateData();


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
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "bearer "+output);
                params.put("Content-Type", "application/json");
                return params;
            }

        };

        requestQueue.add(objectRequest);

    }

    public  void maekAvailable(final String id,final Seller_order_package_model listner,final int position){
        String url = ip+"gate/b2b/order/api/v1/item/update/status/"+id+"/?orderStatus=AVAILABLE&retailerId="+sharedPref.getString("userid",null);
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest objectRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    Toast.makeText(mContext, "Status Changed", Toast.LENGTH_SHORT).show();

                    listner.setItem_status("AVAILABLE");
                    notifyItemChanged(position);
                    updateData();


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
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "bearer "+output);
                params.put("Content-Type", "application/json");
                return params;
            }

        };

        requestQueue.add(objectRequest);

    }

    public  void complete(String task_id){
        String url = ip+"gate/b2b/order/api/v1/task/orders/shipping/"+task_id;
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest objectRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    Company_info_fragment f = (Company_info_fragment)  ((AppCompatActivity)mContext).getSupportFragmentManager().findFragmentByTag("Company");

                    f.Seller_history();
                    Toast.makeText(mContext, "Order Completed", Toast.LENGTH_SHORT).show();
                    ((AppCompatActivity)mContext).getSupportFragmentManager().popBackStackImmediate();
                    someMethod();


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
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "bearer "+output);
                params.put("Content-Type", "application/json");
                return params;
            }

        };

        requestQueue.add(objectRequest);

    }
///////////////////////update/////////////////////////////////////

    public void update( String id, String item_status, String expected, String product, String weight,String product_id, String pay_status,String item_no,String deleverd,String description) {

        JSONObject mainJasan= new JSONObject();
        JSONArray jsonArray=new JSONArray();
        JSONObject link_object=new JSONObject();

        try {
            link_object.put("href","https://server.mrkzevar.com/gate/b2b/order/api/v1/item/"+id);
            mainJasan.put("gstBreakDown",jsonArray);
            mainJasan.put("itemNo",item_no);
            mainJasan.put("id",id);
            mainJasan.put("netWeight",weight);
            mainJasan.put("product",product);
            mainJasan.put("productID",product_id);
            mainJasan.put("quantity",1);
            mainJasan.put("expectedDeliveryDate",expected);
            mainJasan.put("itemStatus",item_status);
            mainJasan.put("shipmentId","");
            mainJasan.put("seller",wholeseller_id);
            mainJasan.put("shipping",jsonArray);
            mainJasan.put("description",description);
            mainJasan.put("_links",link_object);
            mainJasan.put("paymentStatus",pay_status);
            mainJasan.put("totalWeight",deleverd);

            mainJasan.put("custLink","https://server.mrkzevar.com/gate/b2b/order/api/v1/getUser/"+sharedPref.getString("userid",null));
            mainJasan.put("imgLink","http://34.243.90.152:8090/assets/image/"+product_id);

            Log.e("OBJECT",mainJasan.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        String url = ip+"gate/b2b/order/api/v1/item/update?notificcation=true";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, mainJasan, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                Snackbar.make(view,"Updated Successfully",Snackbar.LENGTH_SHORT).show();

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
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);
    }
////////////////////////////////////////////////////////////////////Notify///////////////////////
public  void notify_user(String mobile,String orderNO){
    String url = ip+"gate/b2b/order/api/v1/item/notification";
    String uri=null;
    uri = Uri.parse(url)
            .buildUpon()
            .appendQueryParameter("mobileNumber",mobile)
            .appendQueryParameter("msg","Your Order "+orderNO+" is now Available")
            .build().toString();
    Log.e("MOBILE",mobile);
   Log.e("LOG",uri);
    final RequestQueue requestQueue = Volley.newRequestQueue(mContext);
    StringRequest objectRequest = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            try {
                dialog.dismiss();
                if(response.equalsIgnoreCase("success")) {
                    Toast.makeText(mContext, "Notification has been sent.", Toast.LENGTH_SHORT).show();
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
            Map<String, String> params = new HashMap<String, String>();
            params.put("Authorization", "bearer "+output);
            params.put("Content-Type", "application/json");
            return params;
        }

    };

    requestQueue.add(objectRequest);

}
    // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception



    public void someMethod(){
      /*  mCallback.sendText("YOUR TEXT");*/
        try {
            snackbar.dismiss();
        }catch (Exception e){

        }
    }


}

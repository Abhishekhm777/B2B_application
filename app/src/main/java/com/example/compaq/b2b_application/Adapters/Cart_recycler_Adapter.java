package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Activity.Add_new_product_activity;
import com.example.compaq.b2b_application.Activity.Cart_Activity;
import com.example.compaq.b2b_application.Fragments.CartUpdate_fragment;
import com.example.compaq.b2b_application.Activity.Displaying_complete_product_details_Activity;
import com.example.compaq.b2b_application.Helper_classess.DatePickerFragment;
import com.example.compaq.b2b_application.Helper_classess.Number_picker_dialogue;
import com.example.compaq.b2b_application.Model.Cart_recy_model;
import com.example.compaq.b2b_application.Model.Recy_model2;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.support.v7.widget.RecyclerView.*;
import static com.example.compaq.b2b_application.Fragments.products_display_fragment.item_clicked;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.pref;

public class Cart_recycler_Adapter extends RecyclerView.Adapter<Cart_recycler_Adapter.MyViewHolder>{
    public FragmentActivity mCtx;
    private ArrayList<Cart_recy_model> productlist;
    private CardView cardView;
    private View view;
    public FragmentManager fragmentManager;
    public FragmentTransaction fragmentTransaction;
    public ImageView imageV;
    public Recy_model2 recyModel2;
    public  Recy_model2 onClickListener;
    public Bundle bundle;
    public Context mContext;
    private String total;
    private Double total_weight;
    private  Double total_double=0.0;
    public SharedPreferences sharedPref;
    public   SharedPreferences.Editor myEditor;
    private String output,userid,wholesaler_id ;
    DatePickerFragment datePickerFragment=new DatePickerFragment();
    private Menu menu;
    public Cart_recycler_Adapter(Context mContext, ArrayList<Cart_recy_model> productlist ,FragmentManager fragmentManager) {
        this.productlist=productlist;
        this.mContext=mContext;
        this.fragmentManager=fragmentManager;

        sharedPref=mContext.getSharedPreferences("USER_DETAILS",0);
        output=sharedPref.getString(ACCESS_TOKEN, null);
        userid = sharedPref.getString("userid", "");
        wholesaler_id = sharedPref.getString("Wholeseller_id", null);
       }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_card_layout, parent, false);
       /* LayoutInflater inflater=LayoutInflater.from(mContext);

        view=inflater.inflate(R.layout.cart_card_layout,null);*/

        if(productlist.size()==0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cart_, parent, false);
            ImageView imageView=view.findViewById(R.id.cart_emppty);
            imageView.setImageResource(R.drawable.emptycart);

            return new MyViewHolder(view);
        }
                return new MyViewHolder(itemView);
            }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder,  int position) {

        final com.example.compaq.b2b_application.Model.Cart_recy_model listner=  productlist.get(position);
        holder.d_qty.setText(listner.getQty());
        holder.d_product.setText(listner.getName());

        try {
            total_weight = Double.parseDouble(listner.getQty()) * Double.parseDouble(listner.getWeight());
             String set_total=new DecimalFormat("#0.000").format(total_weight);
             listner.setTotal_weight(set_total);
            holder.d_gweight.setText(set_total);
            calculateWeight();



        }
        catch (Exception e){
            e.printStackTrace();
        }


        if(!listner.getExpected().equalsIgnoreCase("null")){
            holder.excpected_date.setText(listner.getExpected());
        }
        else {
            holder.excpected_date.setText(datePickerFragment.getDate());
        }
        holder.seller_name.setText(listner.getSeller_name());

        /*holder.purity.setText(listner.getPurity());
        holder.size.setText(listner.getSize());
          holder.description.setText("Description:      "+listner.getDescript());
        holder.length.setText(listner.getLength());*/


        String url=  listner.getImg_url();
        Glide.with(mContext).load(url).into(holder.imageV);
        holder.update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                final Number_picker_dialogue number_picker_dialogue = new Number_picker_dialogue(mContext);
                number_picker_dialogue.showPicker();
                number_picker_dialogue.np.setValue(Integer.valueOf(listner.getQty()));
                number_picker_dialogue.set.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        number_picker_dialogue.myDialogue.dismiss();
                        listner.setQty(String.valueOf(number_picker_dialogue.np.getValue()));
                        holder.d_qty.setText(String.valueOf(number_picker_dialogue.np.getValue()));
                        listner.setWeight(holder.d_gweight.getText().toString());
                        notifyDataSetChanged();
                        calculateWeight();


                        updateCart(listner.getId(),listner.getImg_url(),listner.getQty(),listner.getWeight(),listner.getDel_id());
                    }
                });
    }
});
               holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                productlist.remove(holder.getAdapterPosition());


                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), productlist.size());
                delet_fromcart(listner.getDel_id());
                calculateWeight();
                sharedPref =mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                myEditor = sharedPref.edit();
               /* cart_shared_preference = mContext.getSharedPreferences("CART_ITEMS", 0);*/
               String no= String.valueOf((productlist.size()));
               myEditor.putString("no_of_items",no).apply();
                myEditor.commit();
              /*  MainActivity mActivity= new MainActivity();
                mActivity.setupBadge(mContext);
*/

               /* Displaying_complete_product_details_Activity displayingcompleteproductdetailsActivity= new Displaying_complete_product_details_Activity();
                displayingcompleteproductdetailsActivity.setupBadge(mContext);*/
            }
        });
        holder.imageV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name= listner.getName();
                String id= listner.getId();

                bundle=new Bundle();
                bundle.putString("item_name",name);
                bundle.putString("Item_Clicked",id);
                bundle.putString("lurl",item_clicked);
                Intent intent=new Intent(mContext, Displaying_complete_product_details_Activity.class).putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productlist.size();
    }

    public class MyViewHolder extends ViewHolder{
        ImageView imageV;

        ImageView C_imageview;
        Spinner spinner;
        TextView d_qty, purity,d_product,d_gweight,excpected_date,description,size,length,seller_name;
        public Button update,remove;
        public MyViewHolder(View view) {
            super(view);
          /*  purity=(TextView)itemView.findViewById(R.id.purity);
            size=(TextView)itemView.findViewById(R.id.size);
             description=itemView.findViewById(R.id.description);

           length=(TextView)itemView.findViewById(R.id.length);*/
            seller_name=(TextView)itemView.findViewById(R.id.seller_name);
            d_gweight=(TextView)itemView.findViewById(R.id.d_grpss_weight);
            excpected_date =(TextView)itemView.findViewById(R.id.delivery_date);
            d_product=(TextView)itemView.findViewById(R.id.d_produc_name);
            d_qty = (TextView) itemView.findViewById(R.id.d_quantity);
            imageV=(ImageView)itemView.findViewById(R.id.cart_image);
            update=(Button)itemView.findViewById(R.id.wishlist) ;
            remove=(Button)itemView.findViewById(R.id.remove);
            C_imageview=itemView.findViewById(R.id.cart_emppty);
           /* spinner=itemView.findViewById(R.id.item_spinner);
            ArrayList<String> array = new ArrayList<>();
            array.add("1");
            array.add("2");
            array.add("3");
            array.add("5");


            final ArrayAdapter<String> country_adaper = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, array) {
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
            spinner.setAdapter(country_adaper);*/
        }
    }

    public void delet_fromcart(int id){

        String url=ip+"gate/b2b/order/api/v1/item/delete/"+id;
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        JsonObjectRequest jsonObjectss = new JsonObjectRequest (Request.Method.DELETE,url,null,

                new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {

                        try {

                            Toast.makeText(mContext,"Removed Successfully",Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(mContext,"Removed Successfully",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Toast.makeText(mContext,"Removed Successfully",Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            public Map<String, String> getHeaders() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                params.put("Content-Type", "application/json");
                return params;
            }

        };
        requestQueue.add(jsonObjectss);

    }
    private void calculateWeight() {
        Double total_double = 0.0;
        for (int i = 0; i < productlist.size(); i++) {
            final com.example.compaq.b2b_application.Model.Cart_recy_model listner=  productlist.get(i);
            try {
                total_double = total_double + Double.parseDouble(listner.getTotal_weight());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        String total = new DecimalFormat("#0.000").format(Double.parseDouble(total_double.toString()));
        ((Cart_Activity)mContext).totoal_weight(total);
    }

    ///////////////////////////////////////////////update cart//////////////////////////////////////
    public void updateCart(String id, String img_url, String qty, String weight, int del_id) {

       String cartid = sharedPref.getString("cartid", "");
        JSONObject mainJasan= new JSONObject();
        JSONObject json1= new JSONObject();
            final JSONArray items_jsonArray=new JSONArray();
            try {
                json1.put("product",item_clicked);
                json1.put("productID",id);
                json1.put("quantity",qty);
                json1.put("advance","");
               /* json1.put("description",descriptio.getText());
                json1.put("purity",purity_t);
                json1.put("length",length_t);
                json1.put("size",size_t);*/
                json1.put("id", del_id);
                json1.put("grossWeight",weight);
                json1.put("productImage",img_url);
                json1.put("seller",wholesaler_id);

                items_jsonArray.put(json1);
                mainJasan.put("customer",userid);
                mainJasan.put("items",items_jsonArray);
                mainJasan.put("id",cartid);

                Log.e("format",mainJasan.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }


         String url = ip+"gate/b2b/order/api/v1/cart/update";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, mainJasan, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    Toast.makeText(mContext,"Updated Successfully",Toast.LENGTH_SHORT).show();
                }

                catch (Exception e) {
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
                HashMap<String, String> headr = new HashMap<>();

                headr.put("Authorization","bearer "+output);
                headr.put("Content-Type", "application/json");
                return headr;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);
    }
}



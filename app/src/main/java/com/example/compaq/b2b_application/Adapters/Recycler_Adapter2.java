package com.example.compaq.b2b_application.Adapters;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Fragments.Dropdown_fragment;
import com.example.compaq.b2b_application.Main2Activity;
import com.example.compaq.b2b_application.MainActivity;
import com.example.compaq.b2b_application.Model.Recy_model2;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Fragments.Fragment_2.item_clicked;
import static com.example.compaq.b2b_application.MainActivity.ip;
import static com.example.compaq.b2b_application.MainActivity.ip1;
import static com.example.compaq.b2b_application.MainActivity.wishlist2;
import static com.example.compaq.b2b_application.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.SessionManagement.PREF_NAME;
import static java.security.AccessController.getContext;

public class Recycler_Adapter2 extends RecyclerView.Adapter<Recycler_Adapter2.MyViewHolder>{

    public FragmentActivity mCtx;
    private ArrayList<Recy_model2> productlist;
    private CardView cardView;
    private View view;
    public FragmentManager fragmentManager;
    public FragmentTransaction fragmentTransaction;
    public ImageView imageV;
    public Recy_model2 recyModel2;
    public  Recy_model2 onClickListener;
    public Bundle bundle;
    public String name="";
    public ScaleAnimation scaleAnimation;
    public String id="";
    public String output;
    public String user_i,cartid;
   public SharedPreferences sharedPref;
    public   SharedPreferences.Editor myEditor;
   public String  itemclicked="";
   int json_length=0;
    private double item_wieght;
    private String url;

   /* public static  SharedPreferences cart_shared_preference;
    public SharedPreferences.Editor cartEditor;*/

   public Recycler_Adapter2(FragmentActivity mCtx, ArrayList<Recy_model2> productlist) {
        this.mCtx=mCtx;
        this.productlist=productlist;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(mCtx);


        view=inflater.inflate(R.layout.fragment2_cardlayout,null);


        sharedPref =mCtx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        myEditor = sharedPref.edit();

        sharedPref=mCtx.getSharedPreferences("USER_DETAILS",0);

         output=sharedPref.getString(ACCESS_TOKEN, null);



        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final com.example.compaq.b2b_application.Model.Recy_model2 listner= (com.example.compaq.b2b_application.Model.Recy_model2) productlist.get(position);

        /* holder.textView1.setText(listner.getSku());*/

        holder.textView.setText(listner.getName());
        String url=  listner.getImageId();
        if(wishlist2.contains(listner.getSku())){
           holder.whishlist_button.setChecked(true);
        }
        Glide.with(mCtx).load(url).into(holder.imageV);
        Log.e("Name of the clicked", String.valueOf(wishlist2.size()));
            /*Log.e("Name of the clicked", String.valueOf(wishlist.contains("TestR1SKU")));
            if(wishlist.contains("TestR1SKU")){
                holder.whishlist_button.setChecked(true);
            }*/

        holder.imageV.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Toast.makeText(mCtx, listner.getName(),Toast.LENGTH_SHORT).show();
        String name= listner.getName();
         itemclicked= listner.getId();
         String sku=listner.getSku();

        bundle=new Bundle();
        bundle.putString("item_name",name);
        bundle.putString("Item_Clicked",itemclicked);
        bundle.putString("lurl",item_clicked);
        bundle.putString("sku",sku);
        Intent intent=new Intent(mCtx, Main2Activity.class).putExtras(bundle);
        mCtx.startActivity(intent);
    }
});


        holder.addcart_button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        /*fragmentManager = mCtx.getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_card, new Dropdown_fragment());
        fragmentTransaction.addToBackStack(null).commit();


*/
         name= listner.getName();

         id= listner.getId();

        /*cart_shared_preference = mCtx.getSharedPreferences("CART_ITEMS", 0);
       cartEditor= cart_shared_preference.edit();*/
       /* String useris=sharedPref.getString("userid","");
*/

        sharedPref =mCtx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        myEditor = sharedPref.edit();


      /*  sharedPref=mCtx.getSharedPreferences("User_information",0);*/

         user_i =sharedPref.getString("userid","");
         cartid=sharedPref.getString("cartid", "");


       /* pref=mCtx.getSharedPreferences("USER_DETAILS",0);*/


        Log.e("USER IDIDIDID",user_i);

        bundle=new Bundle();
        bundle.putString("item_name",name);
        bundle.putString("Item_Clicked",id);
        Dropdown_fragment myFragment = new Dropdown_fragment();
        myFragment.setArguments(bundle);
        AppCompatActivity activity = (AppCompatActivity) view.getContext();

        activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_card, myFragment).addToBackStack(null).commit();

       /* userCart_details(user_i);*/
       /*
        bundle=new Bundle();
        bundle.putString("item_name",name);
        bundle.putString("Item_Clicked",id);
        Dropdown_fragment myFragment = new Dropdown_fragment();
        myFragment.setArguments(bundle);
        AppCompatActivity activity = (AppCompatActivity) view.getContext();

        activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_card, myFragment).addToBackStack(null).commit();
*/

    }
});
           scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        scaleAnimation.setDuration(800);
        BounceInterpolator  bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);
holder.whishlist_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        //animation
        compoundButton.startAnimation(scaleAnimation);
if(isChecked) {
Log.e("SKU",listner.getSku());

    Vibrator v = (Vibrator)mCtx.getSystemService(Context.VIBRATOR_SERVICE);
    v.vibrate(50);
    addToWhishlist(listner.getSku());



}
if(!isChecked){
    removeFromWhish(listner.getSku());
    Log.e("UNCHECKED",listner.getSku());


}
    }


});

}
    @Override
    public int getItemCount() {
        return productlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageV;
        TextView textView, textView1,textView2;
        public Button addcart_button;
        ToggleButton whishlist_button;
        public MyViewHolder(View itemView) {
            super(itemView);



            textView = (TextView) itemView.findViewById(R.id.navtext);
            imageV=(ImageView)itemView.findViewById(R.id.navimage);
            addcart_button=(Button)itemView.findViewById(R.id.cart_button) ;
            whishlist_button=(ToggleButton) itemView.findViewById(R.id.button_favorite);

        }
    }
    public void userCart_details(String us_id) {
        String url=ip+"gate/b2b/order/api/v1/cart/customer/"+us_id;
        RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
        JsonObjectRequest jsonObjectss = new JsonObjectRequest (Request.Method.GET,url,null,

                new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {


                        try {


                            JSONArray jsonArray=response.getJSONArray("items");
                            json_length= jsonArray.length();
                           Log.e("Length....", String.valueOf(json_length));
                           /* cart_shared_preference = mCtx.getSharedPreferences("CART_ITEMS", 0);*/
                            myEditor.putString("no_of_items", String.valueOf(json_length)).apply();
                            myEditor.commit();
                           /* cart_shared_preference = getActivity().getSharedPreferences("CART_ITEMS", 0);
                            cartEditor.putString("no_of_items", String.valueOf(jsonArray.length()+1)).apply();
                            cartEditor.commit();*/
                            /*MainActivity mActivity= new MainActivity();
                            mActivity.setupBadge(getContext());
                            Main2Activity main2Activity= new Main2Activity();
                            main2Activity.setupBadge(getContext());*/


                            if (jsonArray.length() == 0 ) {
                               /* itemDetails();
                                updateCart();*/
                                bundle=new Bundle();
                                bundle.putString("item_name",name);
                                bundle.putString("Item_Clicked",id);
                                Dropdown_fragment myFragment = new Dropdown_fragment();
                                myFragment.setArguments(bundle);
                                AppCompatActivity activity = (AppCompatActivity) view.getContext();

                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_card, myFragment).addToBackStack(null).commit();


                            }

                            for(int i=0;i<jsonArray.length();i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String userCart_product_id = jsonObject1.getString("product");
                                    Log.e("cart Alli irodu",userCart_product_id);
                                Log.e("click  Alli ",name);
                                if(name.equalsIgnoreCase(userCart_product_id)) {
                                    Log.e("cart Alli irodu",userCart_product_id);
                                    Log.e("cart Alli ",id);
                                    Toast.makeText(mCtx, "Product is already present inside Bag ", Toast.LENGTH_SHORT).show();

                                    return;
                                }

                                if (i == jsonArray.length() - 1 ) {
                                   /* itemDetails();
                                    updateCart();*/

                                    bundle=new Bundle();
                                    bundle.putString("item_name",name);
                                    bundle.putString("Item_Clicked",id);
                                    Dropdown_fragment myFragment = new Dropdown_fragment();
                                    myFragment.setArguments(bundle);
                                    AppCompatActivity activity = (AppCompatActivity) view.getContext();

                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_card, myFragment).addToBackStack(null).commit();
                                }
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                bundle=new Bundle();
                bundle.putString("item_name",name);
                bundle.putString("Item_Clicked",id);
                Dropdown_fragment myFragment = new Dropdown_fragment();
                myFragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_card, myFragment).addToBackStack(null).commit();

            }
        }){
            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                return params;
            }

        };
        requestQueue.add(jsonObjectss);


    }
    public  void addToWhishlist(final String sku){

        String url = ip1+"/b2b/api/v1/user/wish-list/"+sharedPref.getString("userid","")+"/AD?wishList="+sku;
        RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    //JsonObject jsonObject=new JsonObject(response);
                    String id=response.getString("id");

                        /*wishlist.add(sku);*/
                    Toast.makeText(mCtx, "Added to wishlist", Toast.LENGTH_SHORT).show();
                   wishlist2.add(sku);

                } catch (JSONException e) {

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


    public  void removeFromWhish(final String sku){

        String url = ip1+"/b2b/api/v1/user/wish-list/"+sharedPref.getString("userid","")+"/DL?wishList="+sku;
        RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {



                   /* wishlist.remove(sku);*/
                    Toast.makeText(mCtx, "Removed from wishlist", Toast.LENGTH_SHORT).show();
                    wishlist2.remove(sku);

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
    public void updateCart() {

        JSONObject mainJasan= new JSONObject();
        if(cartid.equalsIgnoreCase("0")){
            url=ip+"gate/b2b/order/api/v1/cart/add";


            JSONObject json1= new JSONObject();
            final JSONArray items_jsonArray=new JSONArray();
            try {
                json1.put("product",name);
                json1.put("productID",id);
                json1.put("quantity","1");
                json1.put("advance","");
                json1.put("description","");
                json1.put("seal","MRK");
                json1.put("melting","");
                json1.put("rate","");
                json1.put("netWeight",item_wieght);

                items_jsonArray.put(json1);
                mainJasan.put("customer",user_i);
                mainJasan.put("items",items_jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Map<String, String> params = new HashMap<>();
            params.put("customer", user_i);
            params.put("items", String.valueOf(items_jsonArray));

        }
        else {
            url = ip+"gate/b2b/order/api/v1/cart/update";


            JSONObject json1= new JSONObject();
            final JSONArray items_jsonArray=new JSONArray();
            try {
                json1.put("product",name);
                json1.put("productID",id);
                json1.put("quantity","1");
                json1.put("advance","");
                json1.put("description","");
                json1.put("seal","MRK");
                json1.put("melting","");
                json1.put("rate","");
                json1.put("netWeight",item_wieght);

                items_jsonArray.put(json1);
                mainJasan.put("customer",user_i);
                mainJasan.put("items",items_jsonArray);
                mainJasan.put("id",cartid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Map<String, String> params = new HashMap<>();
            params.put("customer", user_i);
            params.put("items", String.valueOf(items_jsonArray));
            params.put("id", cartid);
        }




        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, mainJasan, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    String cartid=response.getString("id");

                    sharedPref =mCtx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                    myEditor = sharedPref.edit();

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
                    MainActivity mActivity= new MainActivity();
                    mActivity.setupBadge(mCtx);
                   /* Main2Activity main2Activity= new Main2Activity();
                    main2Activity.setupBadge(getContext());*/
                    Toast.makeText(mCtx, "Added to Bag", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(mCtx, "Please select SEAL", Toast.LENGTH_SHORT).show();
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
        RequestQueue queue = Volley.newRequestQueue(mCtx);
        queue.add(request);
    }

    public void itemDetails() {

        String url = ip+"gate/b2b/catalog/api/v1/product/" + name;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jp = jsonObject.getJSONObject("resourceSupport");

                    JSONArray jsonArray = jp.getJSONArray("weights");
                    item_wieght = Double.parseDouble(((jsonArray.getString(0))));

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


        RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
        requestQueue.add(stringRequest);
    }
}

package com.example.compaq.b2b_application.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Activity.All_Sellers_Display_Activity;
import com.example.compaq.b2b_application.Helper_classess.AlertDialogManager;
import com.example.compaq.b2b_application.Fragments.Seller_info_display_fragment;
import com.example.compaq.b2b_application.Activity.MainActivity;
import com.example.compaq.b2b_application.Model.Recy_model2;
import com.example.compaq.b2b_application.Model.SellerPortal_model;
import com.example.compaq.b2b_application.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;

public class Seller_portal_fragment1Adapter extends RecyclerView.Adapter<Seller_portal_fragment1Adapter.MyViewHolder> implements Filterable {
    public FragmentActivity mCtx;
    private ArrayList<SellerPortal_model> productlist;
    private ArrayList<SellerPortal_model> mFilteredList;

    public Seller_portal_fragment2Adapter seller_portal_fragment2_adapter;
    private CardView cardView;
    private View view;
    public FragmentManager fragmentManager;
    public FragmentTransaction fragmentTransaction;
    public ImageView imageV;
    public Recy_model2 recyModel2;
    public  Recy_model2 onClickListener;
    public Bundle bundle;
    private Dialog myDialog;
    public Context mContext;
    private String text,company,image_url;
    public RecyclerView sellser_recycler;
    public SharedPreferences sharedPref;
    AlertDialogManager alert = new AlertDialogManager();
    private String output,user;
    SharedPreferences.Editor myEditior;

    public Seller_portal_fragment1Adapter(FragmentActivity mCtx, ArrayList<SellerPortal_model> productlist,FragmentManager fragmentManager ) {
        this.productlist=productlist;
        this.mCtx=mCtx;
        this.fragmentManager=fragmentManager;
        mFilteredList=productlist;

    }


    @NonNull
    @Override
    public Seller_portal_fragment1Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mCtx).inflate(R.layout.seller_card_layout, parent, false);

        sharedPref =mCtx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
         user=sharedPref.getString("userid", null);
        myEditior = sharedPref.edit();
        /*sellser_recycler = (RecyclerView)view.findViewById(R.id.seller_recycler2);
        sellser_recycler.setLayoutManager(new GridLayoutManager(mCtx, 1));*/
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final Seller_portal_fragment1Adapter.MyViewHolder holder, int position) {
        final SellerPortal_model sellerPortal_model=mFilteredList.get(position);
        holder.seller_name.setText(sellerPortal_model.getSeller_id());
        holder.seller_name.setText(sellerPortal_model.getName());
        Glide.with(mCtx).load(sellerPortal_model.getImageid()).into(holder.imageV);

       /* if(user.equalsIgnoreCase(sellerPortal_model.getSeller_id())){
            holder.view_profile.setVisibility(View.GONE);
            holder.sendrequest.setVisibility(View.GONE);
        }
*/

        company=sellerPortal_model.getName();
        image_url=sellerPortal_model.getImageid();
        holder.view_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                text=sellerPortal_model.getSeller_id();

                Log.e("idididid",text);
                bundle.putString("seller_id", sellerPortal_model.getSeller_id());
                bundle.putString("company",company);
                Seller_info_display_fragment sellerinfodisplay_fragment =new Seller_info_display_fragment();
                sellerinfodisplay_fragment.setArguments(bundle);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, sellerinfodisplay_fragment);
                fragmentTransaction.addToBackStack(null).commit();
            }
        });

        holder.sendrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String text=sellerPortal_model.getSeller_id();
                checkhStatus(text,sellerPortal_model.getWholeseller_no());
            }
        });
        holder.imageV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(user.equalsIgnoreCase(sellerPortal_model.getSeller_id())){

                    myEditior.putString("Wholeseller_mob",sellerPortal_model.getWholeseller_no());
                    myEditior.putString("Wholeseller_id",sellerPortal_model.getSeller_id());
                    myEditior.apply();
                    myEditior.commit();
                    Intent intent=new Intent(mCtx, MainActivity.class);
                    ((All_Sellers_Display_Activity)mCtx).finish();
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    mCtx.startActivity(intent);
                                 return;
                }

                myEditior.putString("Wholeseller_mob",sellerPortal_model.getWholeseller_no());
                myEditior.apply();
                myEditior.commit();
               String text=sellerPortal_model.getSeller_id();
                checkhStatus(text,sellerPortal_model.getWholeseller_no());


            }
        });


    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {

                    mFilteredList = productlist;
                } else {
                    ArrayList<SellerPortal_model> filteredList = new ArrayList<>();
                    for (SellerPortal_model androidVersion : productlist) {
                        if (androidVersion.getName().trim().toLowerCase().contains(charString.trim().toLowerCase())) {

                            filteredList.add(androidVersion);
                        }
                    }
                    mFilteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<SellerPortal_model>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageV;
        TextView seller_name,view_profile,sendrequest;
        CardView card;
        public MyViewHolder(View itemView) {
            super(itemView);

            DisplayMetrics dm = mCtx.getResources().getDisplayMetrics();  //set width of the card
            int width = dm.widthPixels/2;
            imageV=itemView.findViewById(R.id.sellerimage);
            seller_name=itemView.findViewById(R.id.sellername);
            view_profile=itemView.findViewById(R.id.view_profile);
            sendrequest=itemView.findViewById(R.id.send_request);
            card=(CardView)itemView.findViewById(R.id.sellercard_view);
            card.getLayoutParams().width=width;
            card.requestLayout();
        }
    }


    public void checkhStatus (final String seller,final String no){

        String id=sharedPref.getString("Seller_id",null);
        String user=sharedPref.getString("userid",null);
        String url=ip1+"/b2b/api/v1/relation/check/"+user+"/"+seller;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);

                    String status=jObj.getString("status");
                    if(status.equalsIgnoreCase("PENDING")){
                        AlertDialog.Builder alertDialog=new AlertDialog.Builder(mCtx);
                        alertDialog  .setTitle("Alert");
                        alertDialog .setMessage("Your request is pending.");
                        alertDialog  .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        alertDialog.show();

                    }
                    if(status.equalsIgnoreCase("ACCEPTED")){
                        AlertDialog.Builder alertDialog=new AlertDialog.Builder(mCtx);
                        alertDialog  .setTitle("Alert");
                        alertDialog .setMessage("You are already connected with this seller.   ");
                        alertDialog  .setNegativeButton("View Catalog", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                myEditior.putString("Wholeseller_mob",no);
                                myEditior.putString("Wholeseller_id",seller);
                                myEditior.apply();
                                myEditior.commit();
                                Intent intent=new Intent(mCtx, MainActivity.class);
                                ((All_Sellers_Display_Activity)mCtx).finish();
                                mCtx.startActivity(intent);
                            }
                        });
                        alertDialog.show();

                       /* myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        myDialog.show();*/

                    }
                    if(status.equalsIgnoreCase("BLOCKED")){
                        AlertDialog.Builder alertDialog=new AlertDialog.Builder(mCtx);
                        alertDialog  .setTitle("Alert");
                        alertDialog .setMessage("Your Request is Blocked    ");
                        alertDialog  .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               dialogInterface.dismiss();
                            }
                        });
                        alertDialog.show();

                    }
                    if(status.equalsIgnoreCase("REJECTED")){
                        final AlertDialog.Builder alertDialog=new AlertDialog.Builder(mCtx);
                        alertDialog  .setTitle("Alert");
                        alertDialog .setMessage("Your Request is Rejected");
                        alertDialog  .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               dialogInterface.dismiss();
                            }
                        });
                        alertDialog.show();

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(mCtx);
                alertDialog  .setTitle("Alert");
                alertDialog .setMessage("You can access the catalogue only if the seller is in your friends list. Send a request now.");

                alertDialog  .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialog  .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                           sendrequest(seller);
                    }
                }).show();

            }
        }){
            @Override
            public Map<String, String> getHeaders() {

                sharedPref=mCtx.getApplicationContext().getSharedPreferences("USER_DETAILS",0);

                output=sharedPref.getString(ACCESS_TOKEN, null);

                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                return params;
            }
        };




        RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
        requestQueue.add(stringRequest);
    }


    public void sendrequest (final String seller){

        String url=ip1+"/b2b/api/v1/relation/add/"+seller;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);

                    BottomSheet.Builder builder1 = new BottomSheet.Builder(mCtx);
                    builder1.setTitle("Your Request has been sent.");
                    builder1.show();



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                BottomSheet.Builder builder1 = new BottomSheet.Builder(mCtx);
                builder1.setTitle("Sorry!,could't send request.");
                builder1.show();
            }
        })

        {
            @Override
            public Map<String, String> getHeaders() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                params.put("Content-Type","application/json");
                return params;
            }
        };




        RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
        requestQueue.add(stringRequest);
    }

}

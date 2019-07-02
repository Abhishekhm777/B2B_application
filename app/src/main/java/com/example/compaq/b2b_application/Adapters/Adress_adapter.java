package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Address_Activivty;
import com.example.compaq.b2b_application.Fragments.AddNewAddess;
import com.example.compaq.b2b_application.Fragments.Dropdown_fragment;
import com.example.compaq.b2b_application.Fragments.MyAddress_fragment;
import com.example.compaq.b2b_application.Model.Address_model;
import com.example.compaq.b2b_application.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.MainActivity.ip1;
import static com.example.compaq.b2b_application.MainActivity.wishlist2;
import static com.example.compaq.b2b_application.SessionManagement.ACCESS_TOKEN;

public class Adress_adapter extends RecyclerView.Adapter<Adress_adapter.MyViewHolder>{
    Context mContext;
    public ArrayList<Address_model> adres;
    private FragmentTransaction fragmentTransaction;
    public FragmentManager fragmentManager;
    public SharedPreferences sharedPref;
    public Bundle bundle;


    public Adress_adapter(Context mContext, ArrayList<Address_model> adres){
        this.mContext=mContext;
        this.adres=adres;


    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_layout, parent, false);

        return   new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final com.example.compaq.b2b_application.Model.Address_model listner=  adres.get(position);

        holder.name.setText(listner.getName()+" "+listner.getLastname());

        holder.adreess.setText(listner.getLine()+","+listner.getCity()+","+listner.getZipcode());

        holder.address2.setText(listner.getState()+","+listner.getCountry());

        holder.type.setText("Address Type : " +listner.getType());

        holder.mob.setText("Contact No :  "+listner.getMob_no());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_address(listner.getId(),view);
                adres.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), adres.size());

            }
        });
        holder.radioButton.toggle();

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle=new Bundle();
                bundle.putString("key","key");
                bundle.putString("delte_id",listner.getId());
                bundle.putString("country",listner.getCountry());
                bundle.putString("pin", listner.getZipcode());
                bundle.putString("city", listner.getCity());
                bundle.putString("state", listner.getState());
                bundle.putString("type", listner.getType());
                bundle.putString("area", listner.getLine());

                AddNewAddess myFragment = new AddNewAddess();
                myFragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                fragmentManager =activity.getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout,  myFragment).addToBackStack(null);

                fragmentTransaction.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return adres.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,adreess,address2,type,mob;
RadioButton radioButton;
        ImageView delete,edit;
        public MyViewHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.name);
            adreess=(TextView) itemView.findViewById(R.id.address_details);
            address2=(TextView)itemView.findViewById(R.id.address2);
            type=(TextView)itemView.findViewById(R.id.type);
            mob=(TextView)itemView.findViewById(R.id.mob);
            delete=(ImageView)itemView.findViewById(R.id.delte);
            edit=(ImageView)itemView.findViewById(R.id.edit);
            radioButton=(RadioButton)itemView.findViewById(R.id.radio);

        }
    }
    public  void delete_address(final String id, final View view){

        String url = ip1+"/b2b/api/v1/address/delete/"+id;
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {


                    Toast.makeText(mContext," Address Deleted",Toast.LENGTH_SHORT).show();


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

                sharedPref=mContext.getSharedPreferences("USER_DETAILS",0);

               String output=sharedPref.getString(ACCESS_TOKEN, null);
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "bearer "+output);
                params.put("Content-Type", "application/json");
                return params;
            }

        };

        requestQueue.add(objectRequest);

    }
}

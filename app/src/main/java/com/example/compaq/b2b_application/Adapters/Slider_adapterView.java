package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.compaq.b2b_application.Model.Slider_model;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;

public class Slider_adapterView extends RecyclerView.Adapter<Slider_adapterView.MyViewHolder> {
    ArrayList<String>slider_list,slider_id_array;
    Context context;
    private String access_token,file_id;
    SharedPreferences sharedPreferences;

    public Slider_adapterView(Context context,ArrayList<String> slider_list,String file_id,ArrayList<String>slider_id_array){
        this.context=context;
        this.slider_list=slider_list;
        this.file_id=file_id;
        this.slider_id_array=slider_id_array;
    }


    @NonNull
    @Override
    public Slider_adapterView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.slider_add_view, viewGroup, false);
        sharedPreferences=context.getSharedPreferences("USER_DETAILS",0);
        access_token=sharedPreferences.getString(ACCESS_TOKEN, null);
        return new Slider_adapterView.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final Slider_adapterView.MyViewHolder holder, int i) {

        String url=slider_list.get(i);
       // Glide.with(context).load(url).into(holder.imageView);
        Glide
                .with(context)
                .load(url)
                .apply(new RequestOptions().override(1280, 500))
                .into(holder.imageView);
        holder.cancle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             delete(slider_id_array.get(holder.getAdapterPosition()),file_id,holder.getAdapterPosition(),v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return slider_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        Button cancle_button;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.iv_card);
            cancle_button=(Button) itemView.findViewById(R.id.slider_cancle_btn);
        }
    }

    public  void delete(final String file_id, final String slider_id, final int adapterPosition, final View view){
        String url = ip+"gate/b2b/catalog/api/v1/assets/secure/image/delete/slide/"+slider_id+"?fileIDs="+file_id;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest objectRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                   slider_list.remove(adapterPosition);
                    slider_id_array.remove(adapterPosition);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Slider image deleted sucessfully", Toast.LENGTH_SHORT).show();



                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Snackbar.make(view, "Sorry! something went wrong", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "bearer "+access_token);
                params.put("Content-Type", "application/json");
                return params;
            }

        };

        requestQueue.add(objectRequest);

    }
}

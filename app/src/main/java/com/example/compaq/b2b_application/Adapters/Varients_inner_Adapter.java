package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.compaq.b2b_application.Fragments.DisplayingCompletProduct_fragment1;
import com.example.compaq.b2b_application.Fragments.products_display_fragment;
import com.example.compaq.b2b_application.Model.Inner_Recy_model;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip;

public class Varients_inner_Adapter extends RecyclerView.Adapter<Varients_inner_Adapter.ListnerViewHolder> {
    public Context context;
    public ArrayList<Inner_Recy_model> details_list;
    private View view;
    private int value;
    private CardView cardView;
    private Map<Object,Object> params;
    private  Fragment productfragment;
    private  FragmentManager fragmentManager;
    private  FragmentTransaction fTransaction1;
 public   Varients_inner_Adapter(Context context, ArrayList<Inner_Recy_model> details_list, CardView cardView ,FragmentManager fragmentManager) {
        this.context = context;
        this.details_list=details_list;
        this.cardView=cardView;
        this.fragmentManager=fragmentManager;

     productfragment = new DisplayingCompletProduct_fragment1() ;

    }
    @NonNull
    @Override
    public Varients_inner_Adapter.ListnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.buyer_varients_inner_layout, parent, false);
        Varients_inner_Adapter.ListnerViewHolder holder=new Varients_inner_Adapter.ListnerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Varients_inner_Adapter.ListnerViewHolder holder, int position) {
        final Inner_Recy_model inner_recy_listner=details_list.get(position);
        holder.textView1.setText(inner_recy_listner.getKey());
        holder.textView2.setText(inner_recy_listner.getValues());
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardView.setBackgroundResource(R.drawable.background_design);
                    try {
                        fragmentManager.popBackStackImmediate(0,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                       /* fTransaction1 =fragmentManager.beginTransaction();
                        fTransaction1.replace(R.id.product_display_frame, productfragment);
                        fTransaction1.commit();*/
                        createUrl(details_list);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    @Override
    public int getItemCount() {
        return details_list.size();
    }

    public class ListnerViewHolder extends RecyclerView.ViewHolder {
        TextView textView1,textView2;
        LinearLayout linearLayout;
        public ListnerViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1=(TextView)itemView.findViewById(R.id.keytextview);
            textView2=(TextView)itemView.findViewById(R.id.valuetextview);
                linearLayout = itemView.findViewById(R.id.inner);
        }
    }
    public void createUrl(final ArrayList<Inner_Recy_model> inner_recy_listner) throws UnsupportedEncodingException {
        params = new LinkedHashMap<>();
        LinkedHashMap<Object,Object> neParams=new LinkedHashMap<>();
        JSONObject jsonObject=new JSONObject();
        JSONArray jsonArray=new JSONArray();
        String url="https://server.mrkzevar.com/gate/b2b/catalog/api/v1/product/5d43de0046e0fb0001cfe133";
     for(int i=0;i<inner_recy_listner.size();i++){
         Inner_Recy_model inner_recy_listner1=inner_recy_listner.get(i);
         try {
             jsonObject.put("variantKey", inner_recy_listner1.getKey());
             jsonObject.put("variantValue", inner_recy_listner1.getValues());
         } catch (JSONException e) {
             e.printStackTrace();
         }
     }
           jsonArray.put(jsonObject.toString());
        params.put("mappedData",(Serializable)jsonObject.toString());
        neParams.put("mappedData",(Serializable)params);
        String  uri = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter("mappedData", jsonObject.toString())
                .build().toString();
        addQueryStringToUrlString(url,neParams);
        Log.e("LOG URL",uri);
    }

    String addQueryStringToUrlString(String url, final Map<Object, Object> parameters) throws UnsupportedEncodingException {
        if (parameters == null) {
            return url;
        }

        for (Map.Entry<Object, Object> parameter : parameters.entrySet()) {

            final String encodedKey = URLEncoder.encode(parameter.getKey().toString(), "UTF-8");
            final String encodedValue = URLEncoder.encode(parameter.getValue().toString(), "UTF-8");

            if (!url.contains("?")) {
                url += "?" + encodedKey + "=" + encodedValue;
            } else {
                url += "&" + encodedKey + "=" + encodedValue;
            }
        }
        url+="&wholesaler=3";

          Log.e("URRRLLL",url);
        return url;
    }

    private Uri buildURI(String url, Map<String, String> params) {

        // build url with parameters.
        Uri.Builder builder = Uri.parse(url).buildUpon();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }

        fragmentManager.popBackStackImmediate(0,FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fTransaction1 =fragmentManager.beginTransaction();
        fTransaction1.replace(R.id.product_display_frame, productfragment);
        fTransaction1.addToBackStack(null);
        fTransaction1.commit();


        return builder.build();
    }

}

package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Activity.Cart_Activity;
import com.example.compaq.b2b_application.Activity.Offline_order;
import com.example.compaq.b2b_application.Fragments.Offline_fragment1;
import com.example.compaq.b2b_application.Helper_classess.Back_alert_class;
import com.example.compaq.b2b_application.Helper_classess.Number_picker_dialogue;
import com.example.compaq.b2b_application.Model.Offline_order_model;
import com.example.compaq.b2b_application.Model.OrderTobe_customer_model;
import com.example.compaq.b2b_application.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Offline_order_Adapter extends RecyclerView.Adapter<Offline_order_Adapter.MyViewHolder> {
    private ArrayList<Offline_order_model> productlist;
    Context mContext;
    public Bundle bundle;
    private FragmentTransaction fragmentTransaction;
    public FragmentManager fragmentManager;
    private HashMap<String, OrderTobe_customer_model> details;
    private Double grosswt;



    private View view;
    public   TextView textView;
    public Offline_order_Adapter(Context applicationContext, ArrayList<Offline_order_model> productlist, View view) {
        this.mContext=applicationContext;
        this.productlist=productlist;
        this.view=view;
         textView=view.findViewById(R.id.total);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.offline_order_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final com.example.compaq.b2b_application.Model.Offline_order_model listner = productlist.get(position);
        holder.pr_name.setText(listner.getName());
        holder.pro_sku.setText(listner.getSku());
        holder.pro_size.setText(listner.getSize());
        holder.qty.setText(listner.getQuantity());

        holder.qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Number_picker_dialogue number_picker_dialogue = new Number_picker_dialogue(mContext);

                number_picker_dialogue.showPicker();
                number_picker_dialogue.np.setValue(Integer.valueOf(listner.getQuantity()));
                number_picker_dialogue.set.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        number_picker_dialogue.myDialogue.dismiss();
                        listner.setQuantity(String.valueOf(number_picker_dialogue.np.getValue()));
                        holder.qty.setText(String.valueOf(number_picker_dialogue.np.getValue()));
                        notifyDataSetChanged();
                        calculateWeight();

                    }
                });

            }
        });


        Glide.with(mContext).load(listner.getImg_url()).into(holder.product_image);
        holder.itemlayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                holder.itemlayout.setAlpha(0.3f);
               final Back_alert_class back_alert_class=new Back_alert_class(mContext);
                back_alert_class.shoDeletAlert();

                back_alert_class.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        back_alert_class.myDialogue.dismiss();
                        holder.itemlayout.setAlpha(1.0f);
                    }
                });
                back_alert_class.yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.itemlayout.setAlpha(1.0f);
                        back_alert_class.myDialogue.dismiss();
                        productlist.remove(position);
                        notifyDataSetChanged();
                        calculateWeight();


                    }
                });

                return false;
            }

        });

        holder.product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Back_alert_class back_alert_class=new  Back_alert_class(mContext);
                back_alert_class.showPreview((holder.product_image.getDrawable()));


            }
        });

        try {

            grosswt = Double.parseDouble(listner.getQuantity()) * Double.parseDouble(listner.getWeight());
            String gross=new DecimalFormat("#0.000").format(Double.parseDouble(grosswt.toString()));
            holder.pro_gwt.setText(gross);
            listner.setTotal_weight(gross);
            calculateWeight();
           /* total_double = total_double + grosswt;
            String total=new DecimalFormat("#0.000").format(Double.parseDouble(total_double.toString()));
             textView.setText(total);*/
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return productlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
       ImageView product_image;
       TextView pr_name,pro_sku,pro_size,qty;
        EditText pro_gwt;
       RelativeLayout itemlayout;


        public MyViewHolder(View itemView) {
            super(itemView);
            pr_name=itemView.findViewById(R.id.name);
            pro_sku=itemView.findViewById(R.id.sku);
            pro_size=itemView.findViewById(R.id.size_length);
            pro_gwt=itemView.findViewById(R.id.g_wt_gms_);
            product_image=itemView.findViewById(R.id.image);
            itemlayout=itemView.findViewById(R.id.item_layout);
            qty=itemView.findViewById(R.id.qty);

        }
    }

      private void calculateWeight() {
          Double total_double = 0.0;
          for (int i = 0; i < productlist.size(); i++) {
              final com.example.compaq.b2b_application.Model.Offline_order_model listner = productlist.get(i);
              try {

                  total_double = total_double + Double.parseDouble(listner.getTotal_weight());

              } catch (NumberFormatException e) {
                  e.printStackTrace();
              }
          }

          String total = new DecimalFormat("#0.000").format(Double.parseDouble(total_double.toString()));
          textView.setText(total);
      }

}

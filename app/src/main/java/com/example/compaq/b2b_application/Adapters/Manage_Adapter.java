package com.example.compaq.b2b_application.Adapters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Customize_Order;
import com.example.compaq.b2b_application.Fragments.Custom_order;
import com.example.compaq.b2b_application.Fragments.Update_product_fragment;
import com.example.compaq.b2b_application.Manage_Exixsting_category_Activity;
import com.example.compaq.b2b_application.Model.Recy_model2;
import com.example.compaq.b2b_application.Model.SellerPortal_model;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;

public class Manage_Adapter extends RecyclerView.Adapter<Manage_Adapter.MyViewHolder> implements Filterable {
    public FragmentActivity mCtx;
    private ArrayList<Recy_model2> productlist;
    private ArrayList<Recy_model2> mFilteredList;
    private CardView cardView;
    private View view;
    public FragmentManager fragmentManager;
    public FragmentTransaction fragmentTransaction;
    public ImageView imageV;
    public Recy_model2 recyModel2;
    public  Recy_model2 onClickListener;

    public Manage_Adapter(FragmentActivity mCtx, ArrayList<Recy_model2> productlist) {
        this.mCtx=mCtx;
        this.productlist=productlist;
        mFilteredList=productlist;

    }
    @NonNull
    @Override
    public Manage_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_card_layout, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Manage_Adapter.MyViewHolder holder, int position) {
        final com.example.compaq.b2b_application.Model.Recy_model2 listner=  mFilteredList.get(position);
       final String url=  listner.getImageId();
        Glide.with(mCtx).load(url).into(holder.imageV);
        holder.textView.setText(listner.getName());

        holder.imageV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCtx instanceof Manage_Exixsting_category_Activity) {
                    Update_product_fragment update_product_fragment = new Update_product_fragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("image_id", listner.getId());
                    update_product_fragment.setArguments(bundle);
                    mCtx.getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, update_product_fragment,"Update_frag").addToBackStack(null).commit();
                }
                if(mCtx instanceof Customize_Order){
                    Custom_order custom_order=new Custom_order();
                    Bundle bundle = new Bundle();
                    bundle.putString("url", url);
                    bundle.putString("name", listner.getName());
                    bundle.putString("image_id", listner.getId());
                    custom_order.setArguments(bundle);
                    mCtx.getSupportFragmentManager().beginTransaction().replace(R.id.customize, custom_order).addToBackStack(null).commit();

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
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
                    ArrayList<Recy_model2> filteredList = new ArrayList<>();
                    for (Recy_model2 androidVersion : productlist) {
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
                mFilteredList = (ArrayList<Recy_model2>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageV;
        TextView textView;
        CardView card;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.navtext);
            imageV=(ImageView)itemView.findViewById(R.id.navimage);

            if(mCtx instanceof Customize_Order){

                imageV=(ImageView)itemView.findViewById(R.id.navimage);
                DisplayMetrics dm = mCtx.getResources().getDisplayMetrics();  //set width of the card
                int width = dm.widthPixels/2;
                card=(CardView)itemView.findViewById(R.id.navcardview);
                card.getLayoutParams().width=width;
                card.requestLayout();
            }
        }
    }
}

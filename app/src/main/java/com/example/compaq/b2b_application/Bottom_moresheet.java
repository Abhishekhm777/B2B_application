package com.example.compaq.b2b_application;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("ValidFragment")

public class Bottom_moresheet extends BottomSheetDialogFragment {
TextView logout,myaccount,myorders,sellerportal,wishlist;
    SessionManagement session;
    public FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    public Bottom_sheet_dialog.BottomSheetListner mlistner;

    public Bottom_moresheet(Bottom_sheet_dialog.BottomSheetListner mlistner) {
        this.mlistner=mlistner;
    }

    public Bottom_moresheet() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.more_layout, container, false);
        session = new SessionManagement(getContext());



        logout=(TextView)view.findViewById(R.id.logout_d);
        myaccount=(TextView)view.findViewById(R.id.myaccount_d);
        myorders=(TextView)view.findViewById(R.id.order_his_d);
        sellerportal=(TextView)view.findViewById(R.id.seller);
        wishlist=(TextView)view.findViewById(R.id.whish_d);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.logoutUser(getContext());
            }
        });

        sellerportal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SellerPortal_Activity) getContext()).finish();
                Intent i = new Intent(getContext(), Seller_Portal_Screen_Activity.class);
                startActivity(i);
            }
        });

        myaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*

                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.seller_activivty_conatiner, new Personal_info_fragment()).addToBackStack(null).commit();
*/
            }
        });
myorders.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent i = new Intent(getContext(), Orders_History_Activity.class);
        startActivity(i);
    }
});


wishlist.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent i = new Intent(getContext(), Wishlist_Activity.class);
        startActivity(i);
    }
});


        myaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Address_Activivty.class);
                startActivity(i);
            }
        });
        return  view;


    }
    public interface BottomSheetListner{
        void  onButtonClicked(String text);
    }
}

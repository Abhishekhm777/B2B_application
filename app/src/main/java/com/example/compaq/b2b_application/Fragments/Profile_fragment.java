package com.example.compaq.b2b_application.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.compaq.b2b_application.Activity.Address_Activivty;
import com.example.compaq.b2b_application.Activity.All_Sellers_Display_Activity;
import com.example.compaq.b2b_application.Activity.Orders_History_Activity;
import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.Helper_classess.SessionManagement;
import com.example.compaq.b2b_application.Activity.Wishlist_Activity;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile_fragment extends Fragment implements Toolbar.OnMenuItemClickListener,View.OnClickListener{
    SessionManagement session;
    public ListView liv;
    Toolbar toolbar;
  private   View _rootView;
    public FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    public  java.util.ArrayList<String> array=new java.util.ArrayList<String>();
    android.widget.ArrayAdapter<String> adapter;
    public Profile_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        session = new SessionManagement(getContext());
        // Inflate the layout for this fragment
        if(_rootView==null) {
            _rootView = inflater.inflate(R.layout.fragment_profile_fragment, container, false);

            toolbar = (Toolbar) _rootView.findViewById(R.id.profile_toolbar);

            liv = _rootView.findViewById(R.id.list);
            adapter = new android.widget.ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, array);

            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

            toolbar.setOnMenuItemClickListener(this);
            toolbar.inflateMenu(R.menu.bluedasrt);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                }
            });


            array.add("ORDER HISTORY");
            array.add("MY ADDRESS");
            array.add("WISHLIST");
            array.add("SIGNOUT");


            liv.setAdapter(adapter);
        }

        liv.setClickable(true);
        liv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String items= adapter.getItem(position);
                Log.e("CLICKED",items);
                if(items.equalsIgnoreCase("SIGNOUT")){
                    session.logoutUser(getActivity());
                }
                if(items.equalsIgnoreCase("ORDER HISTORY")){
                    /*fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.mainframe, new Oder_History()).addToBackStack(null).commit();*/

                    Intent i = new Intent(getContext(), Orders_History_Activity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                }

                if(items.equalsIgnoreCase("MY ACCOUNT")){
                    fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.mainframe, new Personal_info_fragment()).addToBackStack(null).commit();

                }
                if(items.equalsIgnoreCase("WISHLIST")){
                    /*fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.mainframe, new Whish_list_fragment()).addToBackStack(null).commit();
*/
                    Intent i = new Intent(getContext(), Wishlist_Activity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                }
                if(items.equalsIgnoreCase("MY ADDRESS")){
                    /*fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.mainframe, new Order_toB_processed_fragmenrt1()).addToBackStack(null).commit();
*/
                    Intent i = new Intent(getContext(), Address_Activivty.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                }
                if(items.equalsIgnoreCase("SELLER")){
                    getActivity().finish();
                    Intent i = new Intent(getContext(), All_Sellers_Display_Activity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                }
                if(items.equalsIgnoreCase("CUSTOM ORDER")){
                    fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.mainframe, new Custom_order()).addToBackStack(null).commit();
                }
            }
        });








        return  _rootView;
    }
    @Override
    public void onDestroyView() {
        if (_rootView.getParent() != null) {
            ((ViewGroup)_rootView.getParent()).removeView(_rootView);
        }
        super.onDestroyView();
    }
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();


    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View view) {

    }
}

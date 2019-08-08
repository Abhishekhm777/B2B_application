package com.example.compaq.b2b_application.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.compaq.b2b_application.Activity.UserProfileActivity;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;

import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;


public class UserProfileFragment extends Fragment {

    SharedPreferences sharedPref;
    SharedPreferences.Editor myEditior;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view= inflater.inflate(R.layout.fragment_user_profile, container, false);
        TextView personal_test=view.findViewById(R.id.personal);
        TextView company_text=view.findViewById(R.id.company);
        TextView user_text=view.findViewById(R.id.user_setting);
        TextView view_text=view.findViewById(R.id.view_setting);
        TextView pasword_text=view.findViewById(R.id.password_setting);
        UserProfileActivity.titleView.setText("USER PROFILE");
        personal_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment personal_fragment=new Personal_infoFragment();
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.profile_activity_layout, personal_fragment).addToBackStack(null).commit();
                UserProfileActivity.titleView.setText("PERSONAL INFORMATION");
            }
        });
        company_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment company_fragment=new Company_profile_Fragment();
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.profile_activity_layout, company_fragment).addToBackStack(null).commit();
                UserProfileActivity.titleView.setText("COMPANY INFORMATION");
            }
        });
        view_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment viewSettings_fragment=new ViewSettingsFragment();
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.profile_activity_layout, viewSettings_fragment).addToBackStack(null).commit();
                UserProfileActivity.titleView.setText("VIEW SETTINGS");
            }
        });
        user_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment userSettings_fragment=new UserSettings();
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.profile_activity_layout, userSettings_fragment).addToBackStack(null).commit();
                UserProfileActivity.titleView.setText("USER SETTINGS");
            }
        });
        pasword_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment changePassword_fragment=new ChangePasswordFragment();
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.profile_activity_layout, changePassword_fragment).addToBackStack(null).commit();
                UserProfileActivity.titleView.setText("UPDATE PASSWORD");
            }
        });


        return view;
    }


}

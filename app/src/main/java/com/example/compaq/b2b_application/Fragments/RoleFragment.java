package com.example.compaq.b2b_application.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.compaq.b2b_application.Activity.Sign_up_Activity;
import com.example.compaq.b2b_application.Adapters.CatalogueListAdapter;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;

import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;


public class RoleFragment extends Fragment {

    ArrayList<String> catalogueList;
    ListView listView;
    CatalogueListAdapter catalogueListAdapter;
    SharedPreferences sharedPref;
    SharedPreferences.Editor myEditior;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_role, container, false);
        listView=(ListView) view.findViewById(R.id.role_list);
        catalogueList=new ArrayList<>();
        catalogueList.add("RETAILER");
        catalogueList.add("WHOLESALER");
        catalogueList.add("MANUFACTURER");
        catalogueListAdapter=new CatalogueListAdapter(getActivity(),catalogueList);
        listView.setAdapter(catalogueListAdapter);
        sharedPref = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        myEditior = sharedPref.edit();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String catname = catalogueList.get(i);
                myEditior.putString("ROLE", catname);
                myEditior.commit();
                myEditior.apply();
                Sign_up_Activity.set_view(2);
            }
        });
        return  view;
    }


}

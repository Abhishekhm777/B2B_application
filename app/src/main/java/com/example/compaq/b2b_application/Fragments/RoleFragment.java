package com.example.compaq.b2b_application.Fragments;

import android.content.Context;
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


public class RoleFragment extends Fragment {

    ArrayList<String> catalogueList;
    ListView listView;
    CatalogueListAdapter catalogueListAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_role, container, false);
        listView=(ListView) view.findViewById(R.id.role_list);
        catalogueList=new ArrayList<>();
        catalogueList.add("Retailer");
        catalogueList.add("wholesaler");
        catalogueList.add("Manufacturer");
        catalogueListAdapter=new CatalogueListAdapter(getActivity(),catalogueList);
        listView.setAdapter(catalogueListAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Sign_up_Activity.set_view(2);
            }
        });
        return  view;
    }


}

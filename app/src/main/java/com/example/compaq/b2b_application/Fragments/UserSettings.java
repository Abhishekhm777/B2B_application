package com.example.compaq.b2b_application.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.compaq.b2b_application.Adapters.SealAdapter;
import com.example.compaq.b2b_application.Model.SealModel;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Activity.MainActivity.ip1;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.PREF_NAME;


public class UserSettings extends Fragment  {

    private SharedPreferences sharedPref;
    private String Acess_Token,adharNo="",pan="",role="",password="",adharDocumentId="",panDocumentId="",email="",user_fname="",user_lname="", teli_phone="";
    private JSONObject company_details,websiteSetting;
    private JSONArray meltingSealing,userClass,address,wishlist;
    SealAdapter sealAdapter;
    private int defaultAddressID=0,id=0;
    Boolean verified,verifyRequest;

    ArrayList<SealModel>sealModelArrayList;

    ArrayList<String>seallist,meltlist;
    public  RecyclerView seal_recycler;
    ImageButton addSeal;
    Button save_user;
    CheckBox zoombox;
    LinearLayout mainLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user_settings, container, false);
       mainLayout = (LinearLayout)view.findViewById(R.id.relative);
        sharedPref =getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Acess_Token=sharedPref.getString(ACCESS_TOKEN, null);
        seal_recycler=(RecyclerView)view.findViewById(R.id.seal_recyclerview);
        final GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 1);
        seal_recycler.setLayoutManager(mGridLayoutManager);
        zoombox=(CheckBox)view.findViewById(R.id.zoom_checkbox);

        seallist=new ArrayList<>();
        meltlist=new ArrayList<>();
        sealModelArrayList=new ArrayList<>();

        save_user=(Button)view.findViewById(R.id.change_usersettings_btn);
        addSeal=(ImageButton)view.findViewById(R.id.add_seal_btn);
        addSeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // sealmap.put("","");
                if(sealModelArrayList.size()>0) {
                    if (!seallist.contains("") && !meltlist.contains("")) {
                        sealModelArrayList.add(new SealModel("", ""));
                        seallist.add("");
                        meltlist.add("");
                        sealAdapter.notifyDataSetChanged();
                    }
                }
                else {
                    sealModelArrayList.add(new SealModel("", ""));
                    seallist.add("");
                    meltlist.add("");
                    sealAdapter=new SealAdapter(getActivity(),sealModelArrayList,seallist,meltlist);
                    seal_recycler.setAdapter(sealAdapter);

                }



            }
        });

        save_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUsers();
            }
        });


        setHasOptionsMenu(true);
        userInformation();
        return view;
    }




    //////////////////////////////////////////////send userinfo/////////////////////////////////////////////////

    public void updateUsers() {

        JSONObject mainJasan= new JSONObject();

        String   url=ip+"uaa/b2b/api/v1/user/update";
        JSONArray sealArray=new JSONArray();
        JSONObject webObject=new JSONObject();



        try {
            for(int i=0;i<sealModelArrayList.size();i++){
                JSONObject json1= new JSONObject();
                String seal=(((SealModel)sealModelArrayList.get(i)).getSeal());
                String melt=(((SealModel)sealModelArrayList.get(i)).getMelting());
                if(!seal.equals(null) &&!seal.equals("")  && !melt.equals(null) && !melt.equals("")) {
                    json1.put("seal", seal);
                    json1.put("melting", melt);
                    sealArray.put(json1);
                }
            }
            webObject.put("headerFontColor",websiteSetting.getString("headerFontColor"));
            webObject.put("headerFooterColor",websiteSetting.getString("headerFooterColor"));
            if(zoombox.isChecked()==true){
                webObject.put("zoomEnable",true);
            }
            else {
                webObject.put("zoomEnable",false);
            }
            mainJasan.put("address",address);
            mainJasan.put("adharDocumentId",adharDocumentId);
            mainJasan.put("adharNo",adharNo);
            mainJasan.put("company",company_details);
            mainJasan.put("defaultAddressID",defaultAddressID);
            mainJasan.put("email",email);
            mainJasan.put("firstName",user_fname);
            mainJasan.put("lastName",user_lname);
            mainJasan.put("id",id);
            mainJasan.put("meltingSealing",sealArray);
            mainJasan.put("mobileNumber",teli_phone);
            mainJasan.put("pan",pan);
            mainJasan.put("panDocumentId",panDocumentId);
            mainJasan.put("role",role);
            mainJasan.put("password",password);
            mainJasan.put("userClass",userClass);
            mainJasan.put("websiteSetting",webObject);
            mainJasan.put("wishList",wishlist);
            mainJasan.put("verified",verified);
            mainJasan.put("verifyRequest",verifyRequest);
            Log.d("response...",mainJasan.toString());
            Toast toast= Toast.makeText(getActivity(), "User Settings Updated Sucessfully", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();





        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, mainJasan, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    Log.d("response...",response.toString());

                    setHasOptionsMenu(true);


                }

                // Go to next activity

                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headr = new HashMap<>();

                headr.put("Authorization","bearer "+Acess_Token);
                headr.put("Content-Type", "application/json");
                return headr;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }



////////////////////////////////////////////store userinfo/////////////////////////////////////////////////////
    public void userInformation ( ){

        String url=ip1+"/b2b/api/v1/user/info";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    adharDocumentId=jObj.getString("adharDocumentId");
                    adharNo=jObj.getString("adharNo");
                    address=jObj.getJSONArray("address");
                    company_details=jObj.getJSONObject("company");
                    email=jObj.getString("email");
                    defaultAddressID=jObj.getInt("defaultAddressID");
                    user_fname=jObj.getString("firstName");
                    user_lname=jObj.getString("lastName");
                    teli_phone=jObj.getString("mobileNumber");
                    id=jObj.getInt("id");
                    meltingSealing=jObj.getJSONArray("meltingSealing");
                    pan=jObj.getString("pan");
                    panDocumentId=jObj.getString("panDocumentId");
                    password=jObj.getString("password");
                    role=jObj.getString("role");
                    userClass=jObj.getJSONArray("userClass");
                    verified=jObj.getBoolean("verified");
                    verifyRequest=jObj.getBoolean("verifyRequest");
                    websiteSetting=jObj.getJSONObject("websiteSetting");
                    wishlist=jObj.getJSONArray("wishList");

                    Log.d("userClass.....",userClass.toString());
                    if(websiteSetting.getBoolean("zoomEnable")==true){
                        zoombox.setChecked(true);

                    }
                    else {
                        zoombox.setChecked(false);
                    }
                    if(meltingSealing.length()>0){
                        add_row(meltingSealing.length());
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() {



                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+Acess_Token);
                params.put("Content-Type", "application/json");
                return params;
            }
        };




        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
//////////////////////////////////////add row////////////////////////////////////////////////////////
    public  void add_row(int count){


            try {
                for(int i=0;i<count;i++) {
                    JSONObject meltObj = meltingSealing.getJSONObject(i);
                    String seal = meltObj.getString("seal");
                    String melting = meltObj.getString("melting");
                    sealModelArrayList.add(new SealModel(seal,melting));
                    seallist.add(seal);
                    meltlist.add(melting);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        sealAdapter=new SealAdapter(getActivity(),sealModelArrayList,seallist,meltlist);
        seal_recycler.setAdapter(sealAdapter);


    }



///////////////////////////////////////////////////edit menu///////////////////////////////////////////

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.edit_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("item...", item.getItemId() + "");
        switch (item.getItemId()) {

            case R.id.edit_icon:
               /* user_fname.setEnabled(true);
                user_fname.setClickable(true);
                user_fname.setFocusableInTouchMode(true);
                user_lname.setEnabled(true);
                user_lname.setClickable(true);
                user_lname.setFocusableInTouchMode(true);
                email.setEnabled(true);
                email.setClickable(true);
                email.setFocusableInTouchMode(true);
                teli_phone.setEnabled(true);
                teli_phone.setClickable(true);
                teli_phone.setFocusableInTouchMode(true);*/
                setHasOptionsMenu(false);

                return true;


        }
        return  false;
    }


}

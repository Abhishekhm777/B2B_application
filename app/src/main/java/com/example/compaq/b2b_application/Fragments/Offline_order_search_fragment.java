package com.example.compaq.b2b_application.Fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Adapters.Custom_Order_search_Adapter;
import com.example.compaq.b2b_application.Model.Offline_order_model;
import com.example.compaq.b2b_application.Model.Top_model;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.compaq.b2b_application.Activity.MainActivity.ip;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.editor;

/**
 * A simple {@link Fragment} subclass.
 */
public class Offline_order_search_fragment extends Fragment {
    private View view;
    private ListView listView;
    private SearchView searchView;
    private AppBarLayout appBarLayout;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private String output, user_id, wholseller_id;
    private ArrayList<Top_model> skus;
    private ArrayList<Top_model> names;
    private FragmentTransaction fragmentTransaction;
    public FragmentManager fragmentManager;
    private ArrayList<String> ids;
    private Custom_Order_search_Adapter top_adapter;
    private RadioButton byName, byCategory;
    private Bundle bundle;
    private String path;
    private Button done_button;
    private  Dialog  options_dialog,myDialogue;
    private TextView name,sku,purity,gwt,size;
    private  Button confirm,cancel;
    private ImageView imageView;
    private Toolbar toolbar;
    private  String basee="https://server.mrkzevar.com/gate/b2b/catalog/api/v1/assets/image/";
    private String imageurl;
    private ArrayList<Offline_order_model> productlist;
    public Offline_order_search_fragment() {
        // Required empty public constructor
    }
    SubmitClicked mCallback;
    public interface SubmitClicked{
        public void sendText(String text);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_offline_order_search_fragment, container, false);

            listView = view.findViewById(R.id.custom_search_listview);

            toolbar=getActivity().findViewById(R.id.offline_tool);

            searchView = getActivity().findViewById(R.id.custom_search);
             productlist=new ArrayList<>();

            myDialogue = new Dialog(getContext());
            myDialogue.setContentView(R.layout.offline_order_dialog_layout);

            myDialogue.setCanceledOnTouchOutside(false);
            confirm=myDialogue.findViewById(R.id.confrim);
            cancel=myDialogue.findViewById(R.id.cancel);
            name=myDialogue.findViewById(R.id.name);
            sku=myDialogue.findViewById(R.id.sku);
            gwt=myDialogue.findViewById(R.id.weight_t);
            purity=myDialogue.findViewById(R.id.puri_t);
            size=myDialogue.findViewById(R.id.size_t);
            imageView=myDialogue.findViewById(R.id.image_view);

            myDialogue.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            Window window = myDialogue.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myDialogue.dismiss();
                }
            });
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("arraylist", productlist);
                    fragmentManager = getActivity().getSupportFragmentManager();

                        productlist.add(new Offline_order_model(name.getText().toString(),basee+imageurl,sku.getText().toString(),gwt.getText().toString(),size.getText().toString(),purity.getText().toString(),"1" ));
                    myDialogue.dismiss();
                    Fragment fragmentA = fragmentManager.findFragmentByTag("offline_frag1");
                    if (fragmentA == null) {
                       Log.e("EXISTSS","NPOO");
                        Offline_fragment1 offline_fragment1=new Offline_fragment1();
                        offline_fragment1.setArguments(bundle);
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.offline_frame, offline_fragment1, "offline_frag1");
                        fragmentTransaction.commit();
                    }
                    else{
                        //fragment exist
                        Log.e("EXISTSS","YESSSSSS");
                        mCallback.sendText("YOUR TEXT");
                        getActivity().getSupportFragmentManager().popBackStack();
                    }

                }
            });

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    if (!newText.equalsIgnoreCase("")) {
                        getSuggestions(newText);
                    }
                    if (newText.equalsIgnoreCase("")) {
                        try {
                            names.clear();
                            top_adapter.notifyDataSetChanged();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    return true;
                }
            });
            sharedPref = getActivity().getSharedPreferences("USER_DETAILS", 0);
            editor = sharedPref.edit();
            wholseller_id = sharedPref.getString("userid", null);
            output = sharedPref.getString(ACCESS_TOKEN, null);
            user_id = sharedPref.getString("userid", "");

                searchView.setIconified(false);
                searchView.requestFocusFromTouch();

            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    imageView.setImageDrawable(null);
                    purity.setText(null);
                    size.setText(null);
                    gwt.setText(null);
                    getProductDetails(ids.get(position));
                   Top_model top_model=names.get(position);
                   name.setText(top_model.getPname());
                   sku.setText(top_model.getPsku());
                    myDialogue.show();

                }
            });
        return view;
        }

    public void getSuggestions(String text) {
        String url = ip + "gate/b2b/catalog/api/v1/searching/facets";
        String uri = null;
        uri = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter("queryText", text)
                .appendQueryParameter("wholesaler", user_id)
                .appendQueryParameter("productType", "REGULAR")
                .build().toString();
        Log.e("JJIB", uri);
        getProduct(uri);
    }


    public void getProduct(String text) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    skus = new ArrayList<>();
                    names = new ArrayList<>();
                    ids = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject pro_object = jsonObject.getJSONObject("products");
                    JSONArray jsonArray = pro_object.getJSONArray("content");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject content = jsonArray.getJSONObject(i);
                        names.add(new Top_model(content.getString("name"), content.getString("sku")));
                        ids.add(content.getString("id"));

                    }
                    top_adapter = new Custom_Order_search_Adapter(getActivity(), names, wholseller_id);
                    listView.setAdapter(top_adapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;


                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 404:
                            BottomSheet.Builder builder = new BottomSheet.Builder(getContext());
                            builder.setTitle("Sorry! could't reach server");
                            builder.show();
                            break;
                        case 400:
                            BottomSheet.Builder builder1 = new BottomSheet.Builder(getContext());
                            builder1.setTitle("Sorry! No Products Available");
                            builder1.show();
                            break;
                        case 417:

                            Snackbar.make(getView(), "Sorry! No Products Available", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                           /* BottomSheet.Builder builder2 = new BottomSheet.Builder(getContext());
                            builder2.setTitle("Sorry! No Products Available");
                            builder2.show();*/
                            break;
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "bearer " + output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    RequestQueue requestQueue;
    public void getProductDetails( String id){
        if(requestQueue==null)
        {
            requestQueue = Volley.newRequestQueue(getActivity());
        }
        String url = ip+"gate/b2b/catalog/api/v1/product/"+id;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url   , new Response.Listener<String>() {
            @Override
            public
            void onResponse(String response) {
                try {

                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject pro_object=jsonObject.getJSONObject("resourceSupport");

                    JSONArray jsonArray=pro_object.getJSONArray("imageGridFsID");

                    JSONArray spec_arra=pro_object.getJSONArray("specification");
                    for(int i=0;i<spec_arra.length();i++){
                        JSONObject jsonObject1=spec_arra.getJSONObject(i);
                        if(jsonObject1.getString("heading").equalsIgnoreCase("Product Details")){

                            JSONArray jsonArray2=jsonObject1.getJSONArray("attributes");
                            for(int j=0;j<jsonArray2.length();j++){
                                JSONObject jsonObject2=jsonArray2.getJSONObject(j);
                                if(jsonObject2.getString("key").equalsIgnoreCase("Gross Weight (gms)")){
                                    JSONArray jsonArray1=jsonObject2.getJSONArray("values");
                                    gwt.setText(jsonArray1.getString(0));
                                }
                                if(jsonObject2.getString("key").equalsIgnoreCase("Size"))
                                {
                                    JSONArray jsonArray1=jsonObject2.getJSONArray("values");
                                    size.setText("Size");
                                    size.setText(jsonArray1.getString(0));
                                }
                                if(jsonObject2.getString("key").equalsIgnoreCase("Length"))
                                {
                                    JSONArray jsonArray1=jsonObject2.getJSONArray("values");
                                    size.setText("Length");
                                    size.setText(jsonArray1.getString(0));
                                }
                            }
                        }
                    }
                    imageurl=jsonArray.get(0).toString();
                    Glide.with(getActivity()).load(basee+ jsonArray.get(0).toString()).into(imageView);


                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        },new Response.ErrorListener() {
            @Override
            public
            void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;


                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 404:

                            Snackbar.make(getView(), "Sorry! could't reach server", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            break;
                        case 400:
                            Snackbar.make(getView(), "Sorry! No Products Available", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            break;
                        case 417:

                            Snackbar.make(getView(), "Sorry! No Products Available", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            break;
                    }
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","bearer "+output);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public ArrayList<Offline_order_model> getOrderDetails(){
        return productlist;
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (SubmitClicked) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TextClicked");
        }
    }

    public void someMethod(){

    }

    @Override
    public void onDetach() {
        mCallback = null; // => avoid leaking, thanks @Deepscorn
        super.onDetach();
    }
}

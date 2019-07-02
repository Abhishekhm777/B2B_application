package com.example.compaq.b2b_application.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.example.compaq.b2b_application.Model.OrderImageChild;
import com.example.compaq.b2b_application.Model.Order_history_inner_model;
import com.example.compaq.b2b_application.Model.Orderhistory_model;
import com.example.compaq.b2b_application.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class OrderHistoryAdapter extends BaseExpandableListAdapter {

    private ArrayList<Orderhistory_model> orderNumList;
    private Map<String,List<Order_history_inner_model>> orderChildMap;
    private Map<String,List<OrderImageChild>> imageChildMap;
    private LinkedHashMap<String,String> soldNameList;
    private LinkedHashMap<String,String> trackMap;
    private LayoutInflater inflater;
     TextView item_name,qty,price,pro_wait, puity,status,shipping_address,deleverd,seller_name,ordertype;
     Context cont;
    private String ACCESS_TOKEN,email,mob;
    private String url="";
    ImageView setImage ;
    TableRow tableRow;


    public OrderHistoryAdapter(Context context, ArrayList<Orderhistory_model> orderNumList, Map<String,List<Order_history_inner_model>> orderChildMap,
                               Map<String,List<OrderImageChild>> imageChildMap,LinkedHashMap<String,String> soldNameList) {

        inflater = LayoutInflater.from(context);
        this.orderNumList=orderNumList;
        this.orderChildMap=orderChildMap;
        this.imageChildMap=imageChildMap;
        cont=context;
        this.soldNameList=soldNameList;
    }


   @Override
    public int getGroupCount() {
        return orderNumList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //Log.d("getChildCount",""+listItemmap.get(itemname.get(groupPosition)).size());
        return (orderChildMap.get(orderNumList.get(groupPosition).getOrderno()).size());
    }

    @Override
    public Object getGroup(int groupPosition) {
        return  orderNumList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return orderChildMap.get(orderNumList.get(groupPosition).getOrderno()).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {

        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Orderhistory_model outer1=(Orderhistory_model)getGroup(groupPosition);

        String date1=outer1.getOrderdate();
       final String orderNo=outer1.getOrderno();
        ExpandableListView eLV = (ExpandableListView) parent;
        /*eLV.expandGroup(0);*/
        if(convertView==null){
            convertView=  inflater.inflate(R.layout.order_outer_view, parent,false);

        }

        convertView.setTag(date1);
        TextView txTitle=(TextView)convertView.findViewById(R.id.dateVal);
        TextView ordertxt=(TextView)convertView.findViewById(R.id.orderVal);
        ordertxt.setText(orderNo);
       if(date1.length()>10) {
           txTitle.setText(date1.substring(0, 10));
       }
       else
       {
           txTitle.setText(date1);
       }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
       Order_history_inner_model innerOrderList=(Order_history_inner_model) getChild(groupPosition,childPosition);
        if(convertView==null){
            convertView=  inflater.inflate(R.layout.order_history_child, parent,false);

        }
        try {
            Button cancel_button = (Button) convertView.findViewById(R.id.cancel);
           /* Button track_btn = (Button) convertView.findViewById(R.id.tack);*/
            item_name = (TextView) convertView.findViewById(R.id.order_name_val);
            qty = (TextView) convertView.findViewById(R.id.order_quantity_val);

            pro_wait = (TextView) convertView.findViewById(R.id.order_weight_val);
            puity = (TextView) convertView.findViewById(R.id.puri);
            deleverd = (TextView) convertView.findViewById(R.id.delivered);
            status = (TextView) convertView.findViewById(R.id.order_status_val);
            seller_name = (TextView) convertView.findViewById(R.id.sellername);
            setImage = (ImageView) convertView.findViewById(R.id.orderImage);
            ordertype = (TextView) convertView.findViewById(R.id.order_type);
            tableRow=(TableRow)convertView.findViewById(R.id.deliver_row) ;

            ArrayList<OrderImageChild> childArrayList = (ArrayList<OrderImageChild>) imageChildMap.get(innerOrderList.getProduct().toString());
            for (OrderImageChild imageChild : childArrayList) {
                item_name.setText(imageChild.getName());

                Glide.with(cont).load(imageChild.getImg_url()).into(setImage);
                Log.e("list group is...", imageChild.getImg_url());
                Log.e("list group is...",String.valueOf(imageChildMap.get(innerOrderList.getProduct()).size()) );
            }


            if(soldNameList.containsKey(innerOrderList.getSeller())==true){
                seller_name.setText(soldNameList.get(innerOrderList.getSeller()));
            }

            //item_name.setText(orderImageChild.getName().toString());
            qty.setText(innerOrderList.getQty());


            status.setText(innerOrderList.getStatus());
            ordertype.setText(innerOrderList.getOrdertype());
            if(innerOrderList.getStatus().equalsIgnoreCase("PROCESSING")|innerOrderList.getStatus().equalsIgnoreCase("ORDERED")){
                tableRow.setVisibility(View.GONE);
            }

            try {
                String set_total = new DecimalFormat("#0.000").format(Double.parseDouble(innerOrderList.getWeight()));
                pro_wait.setText(set_total);
                puity.setText(innerOrderList.getPurity());
            }catch (NumberFormatException e){
                e.printStackTrace();
            }

try {
    String deliv = new DecimalFormat("#0.000").format(Double.parseDouble(innerOrderList.getDelivered()));
    deleverd.setText(deliv);
}catch (NumberFormatException e){
            e.printStackTrace();
        }

       /* cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String order_no= orderNumList.get(groupPosition).getOrderno();
                Log.d("list group is...", order_no);



                String id=orderChildMap.get(orderNumList.get(groupPosition).getOrderno()).get(childPosition).ge();
                Log.d("child.22..",id);

                String status=orderChildMap.get(orderNumList.get(groupPosition).getOrderno()).get(childPosition).getStatus();
                String name="";
                ArrayList<OrderImageChild> childArrayList=(ArrayList<OrderImageChild>) imageChildMap.get(sku);
                for(OrderImageChild imageChild:childArrayList){
                    Log.d("imageChildMap111",imageChild.getName()+"");
                    name=imageChild.getName().toString().replaceAll(" ","%20");
                }


                url="https://server.mrkzevar.com/gate/order/api/v1/item/cancel/"+id+"/"+order_no+"?productName="+name+"&customerMobile="+mob+"&customerEmail="+email;
                Log.d("url......",url);
                if(status.equals("ORDERED")) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                    alertbox.setMessage("Are you sure to cancel this order?");

                    alertbox.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    delete_order(url);

                                }
                            });

                    alertbox.setNegativeButton("cancel",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    alertbox.show();
                }
            }
        });
*/

        }catch (Exception e){
            e.printStackTrace();
        }


        return convertView;
    }


    /////////////////////////////////////delete//////////////////////////////////
    public void delete_order(String url){
         Log.d("ordered","order"+url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url
                ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                         Log.d("rsponse",response.toString());

                            Toast.makeText(cont, "Order cancel sucessfully.", LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        makeText(cont, error.getMessage(), LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "bearer " + ACCESS_TOKEN);
                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(cont);
        requestQueue.add(stringRequest);

    }

////////////////////////////////////////tarck////////////////////////////
    public  void track(String url2, final View v){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url2,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray=response.getJSONArray("status");
                            JSONObject jsonObject=jsonArray.getJSONObject(0);
                            String date=jsonObject.getString("datetime");
                            String status=jsonObject.getString("status");


                            final AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                            alertbox.setTitle("Tracking Details");

                            alertbox.setMessage("status "+status+"\n"+"\n"+"date & time: "+date );





                            alertbox.setNegativeButton("cancel",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {

                                        }
                                    });
                            alertbox.show();


                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        makeText(cont, error.getMessage(), LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "bearer " + ACCESS_TOKEN);
                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(cont);
        requestQueue.add(jsonObjectRequest);

    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}

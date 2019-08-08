package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.compaq.b2b_application.Model.SealModel;
import com.example.compaq.b2b_application.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static android.view.View.FOCUSABLES_TOUCH_MODE;
import static android.view.View.FOCUS_DOWN;
import static com.example.compaq.b2b_application.Helper_classess.SessionManagement.ACCESS_TOKEN;

public class SealAdapter extends RecyclerView.Adapter<SealAdapter.MyViewHolder> {

    ArrayList<String>seallist,meltlist;
    Context context;
    private String access_token;
    SharedPreferences sharedPreferences;
    ArrayList<SealModel>sealModelArrayList;
    int position=0;


    public SealAdapter(Context context,ArrayList<SealModel>sealModelArrayList, ArrayList<String>seallist, ArrayList<String>meltlist){
        this.context=context;
        this.seallist=seallist;
        this.meltlist=meltlist;
        this.sealModelArrayList=sealModelArrayList;



    }


    @NonNull
    @Override
    public SealAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.seal_melting, viewGroup, false);

        sharedPreferences=context.getSharedPreferences("USER_DETAILS",0);
        access_token=sharedPreferences.getString(ACCESS_TOKEN, null);
        return new SealAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull  final MyViewHolder myViewHolder, int i) {

        myViewHolder.sealedit.setText((((SealModel)sealModelArrayList.get(myViewHolder.getAdapterPosition())).getSeal()).toUpperCase());
        String meltiing= (((SealModel)sealModelArrayList.get(myViewHolder.getAdapterPosition())).getMelting());

        myViewHolder.melting_edit.setText(meltiing);


        myViewHolder.delete_seal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             String deletekey=seallist.get(myViewHolder.getAdapterPosition());

             seallist.remove(deletekey);
             meltlist.remove(myViewHolder.getAdapterPosition());
             sealModelArrayList.remove(myViewHolder.getAdapterPosition());
             notifyDataSetChanged();

            }
        });
        myViewHolder.melting_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                ((SealModel)sealModelArrayList.get(myViewHolder.getAdapterPosition())).setMelting(s.toString());
                 meltlist.set(myViewHolder.getAdapterPosition(),s.toString());


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
///////////////////////////////////////////////////////////////////////////////////////
        myViewHolder.sealedit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String beforekey=seallist.get(myViewHolder.getAdapterPosition());
                Log.d("log.....",s.toString()+myViewHolder.getAdapterPosition()+beforekey);

                ((SealModel)sealModelArrayList.get(myViewHolder.getAdapterPosition())).setSeal(s.toString().toUpperCase());
                seallist.set(myViewHolder.getAdapterPosition(),s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    @Override
    public int getItemCount() {
        return sealModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        EditText sealedit,melting_edit;
        ImageButton delete_seal;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            sealedit=(EditText)itemView.findViewById(R.id.seal_edit);
            melting_edit=(EditText)itemView.findViewById(R.id.melting_edit) ;
            delete_seal=(ImageButton)itemView.findViewById(R.id.delete_seal_btn) ;
        }

    }

}

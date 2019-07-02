package com.example.compaq.b2b_application;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import uk.co.senab.photoview.PhotoViewAttacher;

public
class ImageOpening_activity extends AppCompatActivity {

   Toolbar toolbar;
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    public TextView textView;
    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_opening_activity);
         textView=(TextView)findViewById(R.id.zoom);
        /* textView.setText("Zoom View");*/
        ImageView imageView = (ImageView) findViewById(R.id.fullimageview);

        imageView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        if (getIntent() != null) {
            String url = getIntent().getExtras().getString("URL");
            Glide.with(ImageOpening_activity.this).load(url).into(imageView);
        }

        PhotoViewAttacher pAttacher;
        pAttacher = new PhotoViewAttacher(imageView);
        pAttacher.update();
        toolbar = (Toolbar) findViewById(R.id.image_toolbar);
        setSupportActionBar(toolbar);
      /*  toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {

                ImageOpening_activity.this.finish();
            }
        });

    }*/
    }
    @Override
    public
    boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.cancel_icon, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public
    boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.search){
            Intent i = new Intent(this,Search_Activity.class);
            startActivity(i);
            return true;
        }
        if(item.getItemId()==R.id.cart){
            Intent i = new Intent(this,Cart_Activity.class);
            startActivity(i);
            return true;

        }
        if(item.getItemId()==R.id.cancel){
            ImageOpening_activity.this.finish();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}

package com.example.compaq.b2b_application.Activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.compaq.b2b_application.R;
import com.example.compaq.b2b_application.Helper_classess.SessionManagement;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;


public class Seller_Dashboard_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button button;
    ListView myList;
    int NOTIFICATION_ID = 234;
    DrawerLayout drawerLayout;
    SessionManagement session;
    LinearLayout drawerPane;
    RelativeLayout content;
    private int currentNotificationID = 0;
    LinearLayout linearLayout;
    public SharedPreferences sharedPref;
    private View mLayout;
    private static final int PERMISSION_REQUEST_CAMERA = 0;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller__portal__screen_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        session = new SessionManagement(getApplicationContext());

        sharedPref = getSharedPreferences("USER_DETAILS", 0);
        mLayout=  findViewById(R.id.drawer_layout);

       /* button.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                NotificationManager notificationManager = (NotificationManager) Seller_Dashboard_Activity.this.getSystemService(Context.NOTIFICATION_SERVICE);

                    String CHANNEL_ID = "my_channel_01";
                    CharSequence name = "my_channel";
                    String Description = "This is my channel";
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                    mChannel.setDescription(Description);
                    mChannel.enableLights(true);
                    mChannel.setLightColor(Color.RED);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    mChannel.setShowBadge(false);
                    notificationManager.createNotificationChannel(mChannel);

                notificationBuilder = new NotificationCompat.Builder(Seller_Dashboard_Activity.this, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("NOTIFICATION")
                        .setBadgeIconType(R.drawable.ordofy_logggo)
                        .setContentText("Hello");

                Intent notificationIntent = new Intent(Seller_Dashboard_Activity.this, Seller_Dashboard_Activity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(Seller_Dashboard_Activity.this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                notificationBuilder.setContentIntent(contentIntent);
                Notification notification = notificationBuilder.build();
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notification.defaults |= Notification.DEFAULT_SOUND;
                currentNotificationID++;
                int notificationId = currentNotificationID;
                if (notificationId == Integer.MAX_VALUE - 1)
                    notificationId = 0;
                notificationManager.notify(notificationId, notification);

            }
        });
*/


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
            requestCameraPermission();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            // Permission is already available, start camera preview

        }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED) {
                // Permission is already available, start camera preview


        } else {
            // Permission is missing and must be requested.
            Log.e("UEJFBEFUDBFNJDFN","NOPERMISSINO <");
            requestCameraPermission();
        }




      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Snackbar.make(view, "Yet to impliment Seller View Please Move to Buyer View", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();





            }
        });*/

linearLayout=(LinearLayout)findViewById(R.id.graphlayout);
        GraphView graph = (GraphView) findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6),
                new DataPoint(4, 6),
                new DataPoint(4, 6),
                new DataPoint(4, 6),
                new DataPoint(4, 3)
        });

        series.setAnimated(true);
        series.setSpacing(3);
       graph.animate();
        graph.computeScroll();
        graph.setTitle("Most Ordered Product");
        graph.addSeries(series);

     /*   ObjectAnimator animation = ObjectAnimator.ofFloat(linearLayout, "translationX", 1000f);
        animation.setDuration(2000);
        animation.start();*/




        //////////////////////////////////////////////////////////////////////////////
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);

        TextView textView=headerLayout.findViewById(R.id.name);
        TextView email=headerLayout.findViewById(R.id.email);
        textView.setText(sharedPref.getString("firstname", null));
        email.setText(sharedPref.getString("email", null));
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.seller__portal__screen_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Seller_Dashboard_Activity.this.finish();
            Intent i = new Intent(this, All_Sellers_Display_Activity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.request) {
            Intent intent=new Intent(getApplicationContext(), Request_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
         else if (id == R.id.add_new) {
            Intent intent=new Intent(getApplicationContext(), Add_new_product_activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent,  ActivityOptions.makeSceneTransitionAnimation(this).toBundle());


        } else if (id == R.id.manage) {
            Intent intent=new Intent(getApplicationContext(), Manage_Exixsting_category_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        else if (id == R.id.manage_categories) {
            Intent intent=new Intent(getApplicationContext(), Manage_Categories.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
        else if (id == R.id.proccess) {
            Intent intent=new Intent(getApplicationContext(), Order_to_bProsessed.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        else if (id == R.id.offline) {
            Intent intent=new Intent(getApplicationContext(), Offline_order.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        else if (id == R.id.history) {

            Intent intent=new Intent(getApplicationContext(), Seller_Order_History.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

        }
        else  if(id==R.id.address){
            Intent intent=new Intent(getApplicationContext(), Address_Activivty.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
        else  if(id==R.id.customize){
            Intent intent=new Intent(getApplicationContext(), Customize_Order.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }

        else  if(id==R.id.logout){

            session.logoutUser(Seller_Dashboard_Activity.this);

        }

        return true;
    }

    private void requestCameraPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with cda button to request the missing permission.

                    // Request the permission
                    ActivityCompat.requestPermissions(Seller_Dashboard_Activity.this,
                            new String[]{Manifest.permission.CAMERA},
                            PERMISSION_REQUEST_CAMERA);



        } else {

            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CAMERA);
        }
    }



}

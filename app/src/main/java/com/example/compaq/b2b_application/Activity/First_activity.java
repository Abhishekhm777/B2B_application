package com.example.compaq.b2b_application.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.compaq.b2b_application.R;

import static java.lang.Thread.sleep;

public class First_activity extends AppCompatActivity {
Button button;
Animation bottom_animat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_activity);

        button=(Button)findViewById(R.id.button);
        bottom_animat= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        button.setAnimation(bottom_animat);
        Thread splash=new Thread()
        {
            public void run()
            {
                try{
                    sleep(2500);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    Intent intent=new Intent(First_activity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
        splash.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}

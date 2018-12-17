package com.pawe322dev.gymcalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    private ImageView iv;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        iv = findViewById(R.id.HomeIView);
        tv = findViewById(R.id.HomeTView);
        Animation myAnim = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        iv.startAnimation(myAnim);
        tv.startAnimation(myAnim);
        final Intent i = new Intent(this, MainActivity.class);
        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(4000);
                }catch(InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                }
            }
        };
        timer.start();
    }
}

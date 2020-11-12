package com.example.servicebypro.Activities.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.servicebypro.R;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;
    //Variables
    Animation bottomAnim, leftAnim, rightAnim;
    ImageView client, artisan;
    TextView logo;
    SharedPreferences onBoardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        //Animation
        rightAnim = AnimationUtils.loadAnimation(this,R.anim.right_animation);
        leftAnim = AnimationUtils.loadAnimation(this,R.anim.left_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Hooks
        client = findViewById(R.id.imageView);
        artisan = findViewById(R.id.imageView2);
        logo = findViewById(R.id.textView);

        client.setAnimation(rightAnim);
        artisan.setAnimation(leftAnim);
        logo.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                onBoardingScreen = getSharedPreferences("onBoardingScreen",MODE_PRIVATE);
                boolean isFirstTime = onBoardingScreen.getBoolean("firstTime",true);

                if(isFirstTime){
                    SharedPreferences.Editor editor = onBoardingScreen.edit();
                    editor.putBoolean("firstTime",false);
                    editor.apply();
                    Intent intent = new Intent(SplashScreen.this, OnBoarding.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(SplashScreen.this, StartUpScreen.class);//ArtisanDashboard
                    startActivity(intent);
                    finish();
                }


            }
        }, SPLASH_SCREEN);
    }
}


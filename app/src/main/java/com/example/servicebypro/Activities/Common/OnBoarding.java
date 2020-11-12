package com.example.servicebypro.Activities.Common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.servicebypro.R;
import com.example.servicebypro.Activities.Adapters.SliderAdapter;


public class OnBoarding extends AppCompatActivity {

    //Variables
    ViewPager viewPager;
    LinearLayout dotsLayout;
    SliderAdapter sliderAdapter;
    TextView[] dots;
    Button letsGetStarted,there_is_next;
    Animation animation;
    int currentPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_on_boarding);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Hooks
        viewPager = findViewById(R.id.slider);
        dotsLayout = findViewById(R.id.dots);
        letsGetStarted = findViewById(R.id.get_started_btn);
        there_is_next = findViewById(R.id.there_is_next);

        //Call adapter
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);

        //Dots
        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);
    }

    public void skip(View view) {
        startActivity(new Intent(this, StartUpScreen.class));
        finish();
    }

    public void next(View view) {
        viewPager.setCurrentItem(currentPos + 1);
    }

    private void addDots(int position) {

        dots = new TextView[4];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);

            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[position].setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.myblue));
        }

    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            currentPos = position;

            switch(position){
                case 0:
                case 1:
                case 2:
                    letsGetStarted.setVisibility(View.INVISIBLE);
                    there_is_next.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    animation = AnimationUtils.loadAnimation(OnBoarding.this, R.anim.on_boarding_btn_animation);
                    letsGetStarted.setAnimation(animation);
                    letsGetStarted.setVisibility(View.VISIBLE);
                    there_is_next.setVisibility(View.INVISIBLE);
                    break;
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) { }
    };

}
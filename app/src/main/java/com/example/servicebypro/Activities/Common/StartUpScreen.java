package com.example.servicebypro.Activities.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.servicebypro.Activities.Common.SignUp.SelectAccountTypeFragment;
import com.example.servicebypro.Activities.Dashboard.Dashboard;
import com.example.servicebypro.Activities.HelperClasses.RedirectUtil;
import com.example.servicebypro.R;
import com.example.servicebypro.SessionManager;


public class StartUpScreen extends AppCompatActivity implements IStartUpScreen, SelectAccountTypeFragment.FragmentSelectUserAccountType {
    private ImageView logo_image;
    private TextView logo_text;
    private Button goto_login_button, goto_register_button;
    String userAccountType = null;
    public SessionManager sessionManager;
    RedirectUtil redirectUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start_up_screen);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sessionManager = new SessionManager(StartUpScreen.this);
        redirectUtil = new RedirectUtil(this);
        //Hooks
        goto_login_button = findViewById(R.id.goto_login_button);
        goto_register_button = findViewById(R.id.goto_register_button);

        goto_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectUtil.initLoginUi();
            }
        });

        goto_register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectUtil.initSelectUserAccountTypeUi();
            }
        });

    }




    @Override
    protected void onStart() {
        super.onStart();

        //TODO check if User is logged in
        checkSession();
    }

    private void checkSession() {
        //if user is logged in -->move to main Dashboard
        if (sessionManager.checkLogin()) {
            Intent intent = new Intent(StartUpScreen.this, Dashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            //Do nothing
        }
    }

    @Override
    public void moveToMainDashboard() {
        Intent intent = new Intent(this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }


    @Override
    public void onInputSelectUserTypeSent(String type) {
        userAccountType = type;
    }

    @Override
    public String getUserAccountType() {
        return userAccountType;
    }

}


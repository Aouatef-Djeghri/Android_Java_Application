package com.example.servicebypro.Activities.Dashboard.Settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.servicebypro.Activities.Dashboard.Assignments.AssignmentRequestFragment;
import com.example.servicebypro.Activities.Dashboard.Assignments.AssignmentsFragment;
import com.example.servicebypro.Activities.Dashboard.Dashboard;
import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.RedirectUtil;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.User;
import com.example.servicebypro.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import okhttp3.ResponseBody;


public class SettingsFragment extends Fragment implements View.OnClickListener {

    private MyToast myToast;
    private Util util;
    private View view;
    private RedirectUtil redirectUtil;
    private SessionManager sessionManager;
    User userData = new User();
    private IDashboard iDashboard;
    TextView setting_user_first_name, setting_user_last_name, quote;

    ImageView avatar;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Utilities class contains reusable methods
        util = new Util(getContext());
        myToast = new MyToast(getContext());
        redirectUtil = new RedirectUtil(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_settings, container, false);
        iDashboard.setCurrentFragTag(getResources().getString(R.string.fragment_settings));
        sessionManager = new SessionManager(getContext());
        userData = sessionManager.getUser();
        //Setting listeners
        view.findViewById(R.id.go_to_profile).setOnClickListener(this);
        view.findViewById(R.id.logout).setOnClickListener(this);
        view.findViewById(R.id.redirect_to_post_project).setOnClickListener(this);
        view.findViewById(R.id.redirect_to_assignments).setOnClickListener(this);
        view.findViewById(R.id.change_password_layout).setOnClickListener(this);
        view.findViewById(R.id.redirect_to_freelancer_services).setOnClickListener(this);
        view.findViewById(R.id.redirect_to_user_reviews).setOnClickListener(this);
        view.findViewById(R.id.redirect_to_user_visibility).setOnClickListener(this);


        //hooks
        setting_user_first_name = view.findViewById(R.id.setting_user_first_name);
        setting_user_last_name = view.findViewById(R.id.setting_user_last_name);
        quote = view.findViewById(R.id.quote);
        avatar = view.findViewById(R.id.avatar);
        selectRandomQuote();
        //Setting text from session
        String firstName = userData.getFirstName();
        setting_user_first_name.setText(firstName.substring(0, 1).toUpperCase() + firstName.substring(1));

        String lastName = userData.getLastName();
        setting_user_last_name.setText(lastName.substring(0, 1).toUpperCase() + ".");

        String avatarLink = userData.getAvatar();
        util.setAvatar(avatarLink,avatar);
        return view;
    }

    public void selectRandomQuote() {

        String[] quotesTab = getContext().getResources().getStringArray(R.array.quotes);
        List<String> quotes = Arrays.asList(quotesTab);

        Random r = new Random();
        int lowRange = 0;
        int highRange = 17;
        int result = r.nextInt(highRange - lowRange) + lowRange;

        quote.setText(quotes.get(result));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.go_to_profile:
                //TODO do things
                iDashboard.inflateFragment(getString(R.string.fragment_profile));
                break;

            case R.id.logout:
                sessionManager.logout();
                break;

            case R.id.redirect_to_post_project:
                iDashboard.inflateFragment(getString(R.string.fragment_add));
                break;
            case R.id.redirect_to_user_visibility:
                redirectUtil.initUserVisibilityUi();
                break;
            case R.id.redirect_to_assignments:
                iDashboard.doFragmentTransaction(new AssignmentsFragment(), getString(R.string.fragment_assignment), false);
                break;
            case R.id.redirect_to_freelancer_services:
                redirectUtil.initUserServicesUi();
                break;
            case R.id.redirect_to_user_reviews:
                redirectUtil.initUserReviewsUi();
                break;
            case R.id.change_password_layout:
                redirectUtil.initChangePasswordUi();
                break;
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iDashboard = (IDashboard) getActivity();

    }
}
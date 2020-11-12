package com.example.servicebypro.Activities.Common.Login;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.servicebypro.Activities.Common.IStartUpScreen;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.RedirectUtil;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.SessionManager;

public class AccountValidationFragment extends DialogFragment implements
        android.view.View.OnClickListener {

    private Dialog mDialog;
    private Util util;
    private MyToast myToast;
    private View view;
    public static SessionManager sessionManager;
    private RedirectUtil redirectUtil;

    public AccountValidationFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new Dialog(getActivity(), R.style.DialogFragment);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(mDialog.getWindow().getAttributes());
        //   layoutParams.windowAnimations = R.style.SlideUpDownDialog;

        final View view = View.inflate(getActivity(), R.layout.fragment_account_validation, null);
        mDialog.getWindow().setAttributes(layoutParams);
        mDialog.setContentView(view);

        return mDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        util = new Util(getActivity());
        myToast = new MyToast(getActivity());
        redirectUtil = new RedirectUtil(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account_validation, container, false);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //Setting listeners
        view.findViewById(R.id.validation_logout).setOnClickListener(this);

        mDialog.setCanceledOnTouchOutside(false);
        return view;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        util.dismissAllDialogs(getFragmentManager());
        //sessionManager.logout();
        //     dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        //sessionManager.logout();
        // dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.validation_logout:
                //logout and close app
                sessionManager.logout();
                //               dismiss();
                break;


        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sessionManager = new SessionManager(getContext());
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
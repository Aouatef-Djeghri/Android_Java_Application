package com.example.servicebypro.Activities.Common.Login;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import android.app.Dialog;
import android.app.DialogFragment;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.LoadingDialog;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.RedirectUtil;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.User;
import com.example.servicebypro.ViewModels.LoginViewModel;
import com.google.android.material.textfield.TextInputLayout;

import okhttp3.ResponseBody;

public class ForgetPasswordFragment extends DialogFragment implements
        android.view.View.OnClickListener {

    private Dialog mDialog;
    private Util util;
    private MyToast myToast;
    private View view;
    private RedirectUtil redirectUtil;
    LoginViewModel loginViewModel;
    TextInputLayout email_layout;
    TextView reset_password_msg;
    LoadingDialog loadingDialog;


    public ForgetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Utilities class contains reusable methods
        util = new Util(getActivity());
        myToast = new MyToast(getContext());
        redirectUtil = new RedirectUtil(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new Dialog(getActivity(), R.style.DialogFragment);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(mDialog.getWindow().getAttributes());
        //   layoutParams.windowAnimations = R.style.SlideUpDownDialog;

        final View view = View.inflate(getActivity(), R.layout.fragment_forget_password, null);
        mDialog.getWindow().setAttributes(layoutParams);
        mDialog.setContentView(view);

        return mDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        hideSoftKeyboard();
        loginViewModel = new ViewModelProvider((ViewModelStoreOwner) getActivity()).get(LoginViewModel.class);
        //Setting listeners
        view.findViewById(R.id.forget_password_next_btn).setOnClickListener(this);
        view.findViewById(R.id.forget_password_back_btn).setOnClickListener(this);

        //hooks
        email_layout = view.findViewById(R.id.email_layout);
        reset_password_msg = view.findViewById(R.id.reset_password_msg);
        reset_password_msg.setVisibility(View.GONE);

        mDialog.setCanceledOnTouchOutside(true);
        return view;
    }

    private void hideSoftKeyboard() {
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onClick(View v) {
        reset_password_msg.setVisibility(View.GONE);

        switch (v.getId()) {

            case R.id.forget_password_back_btn:
                dismiss();
                break;

            case R.id.forget_password_next_btn:
                //todo verifiy if email exists
                // todo if yes send email fih news generated password
                //todo redirect to
                //todo if not show msg to user that this email dosn't exists
                if (!util.isInternetConnected()) {
                    //Show a dialog
                    util.ShowCustomDialog();
                }else{
                    if (!util.validateEmail(email_layout)) {
                        return;
                    }
                    loadingDialog.startLoadingDialog();
                    String email = email_layout.getEditText().getText().toString();
                    forgetPassword(email);
                }
                break;


        }
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    //    dismiss();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    //todo check if email exists and user not blocked or not validated yet
    private void forgetPassword(String email) {

        loginViewModel.forgetPassword(email).observe((LifecycleOwner) getActivity(), new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                myToast.showSuccess("A new password was successfully send to your email");
                loadingDialog.dismissDialog();
                redirectUtil.initLoginUi();
            }
        });
        loginViewModel.getToast().observe((LifecycleOwner) getActivity(), new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                loadingDialog.dismissDialog();
                //TODO Add something to notify the user that a problem has occurred
                reset_password_msg.setVisibility(View.VISIBLE);
                reset_password_msg.setText(errorResponse.getErrorMessage());
            }
        });

    }

}
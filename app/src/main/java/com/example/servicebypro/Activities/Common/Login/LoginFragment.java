package com.example.servicebypro.Activities.Common.Login;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.servicebypro.Activities.Common.IStartUpScreen;
import com.example.servicebypro.Activities.HelperClasses.LoadingDialog;
import com.example.servicebypro.Activities.HelperClasses.RedirectUtil;
import com.example.servicebypro.Activities.Dashboard.Dashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.User;
import com.example.servicebypro.SessionManager;
import com.example.servicebypro.ViewModels.LoginViewModel;
import com.google.android.material.textfield.TextInputLayout;


public class LoginFragment extends DialogFragment implements
        android.view.View.OnClickListener {

    private static final String TAG = "Login";
    //RelativeLayout progress_bar_layout;
    private TextInputLayout login_email, login_password;
    private Dialog mDialog;
    private Util util;
    private MyToast myToast;
    private View view;
    RelativeLayout login_layout;
    public static SessionManager sessionManager;
    LoginViewModel loginViewModel;
    IStartUpScreen iStartUpScreen;
    RedirectUtil redirectUtil;
    LoadingDialog loadingDialog;
    TextView account_access_msg;

    public LoginFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new Dialog(getActivity(), R.style.DialogFragment);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(mDialog.getWindow().getAttributes());
        //   layoutParams.windowAnimations = R.style.SlideUpDownDialog;

        final View view = View.inflate(getActivity(), R.layout.fragment_assignment_finished, null);
        mDialog.getWindow().setAttributes(layoutParams);
        mDialog.setContentView(view);

        return mDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        util = new Util(getActivity());
        myToast = new MyToast(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        hideSoftKeyboard();
        loginViewModel = new ViewModelProvider((ViewModelStoreOwner) getActivity()).get(LoginViewModel.class);
        redirectUtil = new RedirectUtil(getActivity());

        //Setting listeners
        view.findViewById(R.id.login_back_btn).setOnClickListener(this);
        view.findViewById(R.id.login_forget_password).setOnClickListener(this);
        view.findViewById(R.id.login_button).setOnClickListener(this);
        view.findViewById(R.id.login_goto_register_button).setOnClickListener(this);


        //hooks
        login_email = view.findViewById(R.id.login_email);
        login_password = view.findViewById(R.id.login_password);
        //progress_bar_layout = findViewById(R.id.progress_bar_layout);
        //progress_bar_layout.setVisibility(View.GONE);
        login_layout = view.findViewById(R.id.login_layout);
        account_access_msg = view.findViewById(R.id.account_access_msg);
        account_access_msg.setVisibility(View.GONE);
        mDialog.setCanceledOnTouchOutside(true);
        return view;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        // dismiss();
    }

    @Override
    public void onClick(View v) {
        account_access_msg.setVisibility(View.GONE);
        switch (v.getId()) {

            case R.id.login_back_btn:
                dismiss();
                break;
            case R.id.login_button:

                util.hideKeyBoardWhenButtonClicked(login_layout);

                //Test if connected to the internet
                if (!util.isInternetConnected()) {
                    //Show a dialog
                    util.ShowCustomDialog();
                } else {
                    //Perform login
                    if (!util.validateEmail(login_email) | !util.validatePassword(login_password)) {
                        return;
                    }
                    loadingDialog.startLoadingDialog();
                    //TODO show Progress Bar
                    //progress_bar_layout.setVisibility(View.VISIBLE);
                    String email = login_email.getEditText().getText().toString();
                    String password = login_password.getEditText().getText().toString();
                    login(email, password);
                }
                break;
            case R.id.login_forget_password:
                redirectUtil.initForgetPasswordUi();
                break;
            case R.id.login_goto_register_button:
                redirectUtil.initSignUpUi();
                break;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sessionManager = new SessionManager(getContext());
        iStartUpScreen = (IStartUpScreen) getActivity();
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onStart() {
        super.onStart();

        //TODO check if User is logged in
        checkSession();
    }

    private void hideSoftKeyboard() {
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    private void checkSession() {
        //if user is logged in -->move to main Dashboard
        if (sessionManager.checkLogin()) {
            Intent intent = new Intent(getContext(), Dashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            //do nothing
        }
    }


    private void login(String email, String password) {

        loginViewModel.login(email, password).observe((LifecycleOwner) getActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                loadingDialog.dismissDialog();
                //test if user account has been validated by the admin
                switch (user.getAccountActivation()) {
                    case 0:
                        //test if user account has not been validated by the admin
                        redirectUtil.initAccountValidationUi();
                        break;
                    case 1:
                        myToast.showSuccess(user.getFirstName());
                        //TODO log in to app and save session of user
                        sessionManager.saveSession(user);
                        //TODO move to main screen
                        iStartUpScreen.moveToMainDashboard();
                        break;
                    case 2:
                        account_access_msg.setVisibility(View.VISIBLE);
                        account_access_msg.setText("Sorry, this account was deleted!");
                        break;
                }
            }
        });
        loginViewModel.getToast().observe((LifecycleOwner) getActivity(), new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                loadingDialog.dismissDialog();
                //TODO Add something to notify the user that a problem has occurred
                myToast.showError(errorResponse.getErrorMessage());
            }
        });

    }
}
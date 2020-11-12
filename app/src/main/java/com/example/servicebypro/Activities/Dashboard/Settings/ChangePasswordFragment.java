package com.example.servicebypro.Activities.Dashboard.Settings;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.User;
import com.example.servicebypro.SessionManager;
import com.example.servicebypro.ViewModels.SettingsFragmentViewModel;
import com.google.android.material.textfield.TextInputLayout;

import okhttp3.ResponseBody;

public class ChangePasswordFragment extends DialogFragment implements
        android.view.View.OnClickListener {
    private Util util;
    private MyToast myToast;
    View view;
    User userData = new User();
    private Dialog mDialog;
    SessionManager sessionManager;
    private IDashboard iDashboard;
    TextInputLayout old_password_text_layout, new_password_text_layout;
    SettingsFragmentViewModel settingsFragmentViewModel;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        util = new Util(getActivity());
        myToast = new MyToast(getActivity());
        settingsFragmentViewModel = new ViewModelProvider((ViewModelStoreOwner) this.getActivity()).get(SettingsFragmentViewModel.class);
        sessionManager = new SessionManager(getContext());
        userData = sessionManager.getUser();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new Dialog(getActivity(), R.style.DialogFragment);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(mDialog.getWindow().getAttributes());

        final View view = View.inflate(getActivity(), R.layout.fragment_assignment_request, null);
        mDialog.getWindow().setAttributes(layoutParams);
        mDialog.setContentView(view);

        return mDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_change_password, container, false);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //hooks
        old_password_text_layout = view.findViewById(R.id.old_password_text_layout);
        new_password_text_layout = view.findViewById(R.id.new_password_text_layout);
        old_password_text_layout.getEditText().setText("");
        new_password_text_layout.getEditText().setText("");
        //setting listeners
        view.findViewById(R.id.back_search).setOnClickListener(this);
        view.findViewById(R.id.cancel_button).setOnClickListener(this);
        view.findViewById(R.id.save_password_button).setOnClickListener(this);

        mDialog.setCanceledOnTouchOutside(true);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_password_button:
                //TODO SAVE NEW password
                int id = userData.getIdUser();
                String oldPassword = old_password_text_layout.getEditText().getText().toString().trim();
                String newPassword = new_password_text_layout.getEditText().getText().toString().trim();
                if (!util.isInternetConnected()) {
                    //Show a dialog
                    util.ShowCustomDialog();
                }else{
                    updateUserPassword(id, oldPassword, newPassword);
                }
                //TODO test if user clicked on edit and didn't save an wanted to press back button, do it in profile activity
                break;
            case R.id.cancel_button:
                old_password_text_layout.getEditText().setText("");
                new_password_text_layout.getEditText().setText("");
                dismiss();
                break;
            case R.id.back_search:
                dismiss();
                break;
        }
    }

    public void updateUserPassword(int idUser, String oldPassword, String newPassword) {
        //check inputs validation
        if (!util.validateXInput(old_password_text_layout) | !util.validateXInput(new_password_text_layout)) {
            return;
        }
        settingsFragmentViewModel.updatePassword(idUser, oldPassword, newPassword).observe((LifecycleOwner)getActivity(), new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                myToast.showSuccess("Password updated successfully");
                old_password_text_layout.getEditText().setText("");
                new_password_text_layout.getEditText().setText("");
                dismiss();
            }
        });
        settingsFragmentViewModel.getUserRepoToast().observe((LifecycleOwner)getActivity(), new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                //TODO Add something to notify the user that a problem has occurred
                myToast.showError(errorResponse.getErrorMessage());
            }
        });
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        dismiss();
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }
}
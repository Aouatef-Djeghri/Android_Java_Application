package com.example.servicebypro.Activities.Dashboard.Settings;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
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
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.RedirectUtil;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.User;
import com.example.servicebypro.SessionManager;
import com.example.servicebypro.ViewModels.SettingsFragmentViewModel;

import okhttp3.ResponseBody;

public class UserVisibilityFragment extends DialogFragment implements
        android.view.View.OnClickListener {
    private Util util;
    private MyToast myToast;
    private View view;
    private RedirectUtil redirectUtil;
    private Dialog mDialog;
    private SessionManager sessionManager;
    Switch visibilitySwitch;
    int visibility;
    SettingsFragmentViewModel settingsFragmentViewModel;

    public UserVisibilityFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new Dialog(getActivity(), R.style.DialogFragment);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(mDialog.getWindow().getAttributes());
        //   layoutParams.windowAnimations = R.style.SlideUpDownDialog;

        final View view = View.inflate(getActivity(), R.layout.fragment_user_visibility, null);
        mDialog.getWindow().setAttributes(layoutParams);
        mDialog.setContentView(view);

        return mDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Utilities class contains reusable methods
        util = new Util(getActivity());
        myToast = new MyToast(getContext());
        redirectUtil = new RedirectUtil(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_visibility, container, false);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        settingsFragmentViewModel = new ViewModelProvider((ViewModelStoreOwner) getActivity()).get(SettingsFragmentViewModel.class);
        view.findViewById(R.id.back).setOnClickListener(this);
        visibilitySwitch = (Switch) view.findViewById(R.id.switch1);

        if (sessionManager.getUserVisibility() == 0) {
            visibilitySwitch.setChecked(false);
        } else {
            visibilitySwitch.setChecked(true);
        }

        if (!util.isInternetConnected()) {
            //Show a dialog
            util.ShowCustomDialog();
        } else {
            visibilitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        // The toggle is enabled
                        visibility = 1;
                        setUserVisibilityInSearch(sessionManager.getUser().getIdUser(), 1);
                    } else {
                        // The toggle is disabled
                        visibility = 0;
                        setUserVisibilityInSearch(sessionManager.getUser().getIdUser(), 0);
                    }
                }
            });
        }

        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sessionManager = new SessionManager(getActivity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back:
                dismiss();
                break;
        }
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Util.dismissAllDialogs(getFragmentManager());
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        // dismiss();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void setUserVisibilityInSearch(int userId, int visibility) {

        settingsFragmentViewModel.setUserVisibilityInSearch(userId, visibility).observe((LifecycleOwner) getActivity(), new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                sessionManager.updateUserVisibility(visibility);
            }

        });
        settingsFragmentViewModel.getUserRepoToast().observe((LifecycleOwner) getActivity(), new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                //TODO Add something to notify the user that a problem has occurred
                myToast.showError(errorResponse.getErrorMessage());
            }
        });

    }
}
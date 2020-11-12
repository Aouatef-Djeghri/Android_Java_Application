package com.example.servicebypro.Activities.Common.SignUp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.example.servicebypro.Activities.HelperClasses.RedirectUtil;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;


public class SelectAccountTypeFragment extends DialogFragment implements
        android.view.View.OnClickListener {

    RedirectUtil redirectUtil;
    private View view;
    private MyToast myToast;
    private Util util;
    private Dialog mDialog;

    public SelectAccountTypeFragment() {
        // Required empty public constructor
    }

    private SelectAccountTypeFragment.FragmentSelectUserAccountType listener;

    public interface FragmentSelectUserAccountType{
        void onInputSelectUserTypeSent(String type);
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_select_account_type, container, false);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        redirectUtil = new RedirectUtil(getActivity());
        view.findViewById(R.id.select_type_back_btn).setOnClickListener(this);
        view.findViewById(R.id.is_client).setOnClickListener(this);
        view.findViewById(R.id.is_artisan).setOnClickListener(this);

        mDialog.setCanceledOnTouchOutside(true);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.select_type_back_btn:
                dismiss();
                break;
            case R.id.is_client:
                listener.onInputSelectUserTypeSent("client");
                redirectUtil.initSignUpUi();
                break;
            case R.id.is_artisan:
                listener.onInputSelectUserTypeSent("artisan");
                redirectUtil.initSignUpUi();
                break;
        }
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myToast = new MyToast(getActivity());
        util = new Util(getActivity());
        if (context instanceof SelectAccountTypeFragment.FragmentSelectUserAccountType) {
            listener = (SelectAccountTypeFragment.FragmentSelectUserAccountType) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentAListener");
        }

    }
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
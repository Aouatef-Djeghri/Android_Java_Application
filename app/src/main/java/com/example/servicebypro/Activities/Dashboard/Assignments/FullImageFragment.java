package com.example.servicebypro.Activities.Dashboard.Assignments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.RedirectUtil;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.github.chrisbanes.photoview.PhotoView;


public class FullImageFragment extends DialogFragment implements
        android.view.View.OnClickListener {

    private Dialog mDialog;
    private Util util;
    private MyToast myToast;
    private View view;
    String imageLink;

IDashboard iDashboard;
    PhotoView photoView ;
    public FullImageFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new Dialog(getActivity(), R.style.DialogFragment);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(mDialog.getWindow().getAttributes());
        final View view = View.inflate(getActivity(), R.layout.fragment_full_image, null);
        mDialog.getWindow().setAttributes(layoutParams);
        mDialog.setContentView(view);

        return mDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        util = new Util(getActivity());
        myToast = new MyToast(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_full_image, container, false);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
         mDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_rounded_bg));
        view.findViewById(R.id.back_search).setOnClickListener(this);
        photoView = (PhotoView) view.findViewById(R.id.work_image);
        util.setPhotoView(iDashboard.getImageLink(),photoView);
        mDialog.setCanceledOnTouchOutside(true);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iDashboard = (IDashboard) getActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back_search:
                dismiss();
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
        dismiss();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
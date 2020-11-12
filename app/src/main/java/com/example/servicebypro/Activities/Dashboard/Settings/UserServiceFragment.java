package com.example.servicebypro.Activities.Dashboard.Settings;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.servicebypro.Activities.Adapters.UserServicesAdapter;
import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.RedirectUtil;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.Review;
import com.example.servicebypro.Remote.Models.Service;
import com.example.servicebypro.SessionManager;
import com.example.servicebypro.ViewModels.AssignmentsFragmentViewModel;
import com.example.servicebypro.ViewModels.SettingsFragmentViewModel;

import java.util.ArrayList;

public class UserServiceFragment extends DialogFragment implements
        android.view.View.OnClickListener , UserServicesAdapter.OnServiceListener{
    private Util util;
    private MyToast myToast;
    private View view;
    private RedirectUtil redirectUtil;
    private Dialog mDialog;
    SessionManager sessionManager;
    LinearLayout no_user_services_layout;
    private SettingsFragmentViewModel settingsFragmentViewModel;
    ArrayList<Service> serviceList = new ArrayList<>();
    RecyclerView my_services_list_recycler_view;

    RelativeLayout user_services_recycler_layout;
    public UserServiceFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Utilities class contains reusable methods
        util = new Util(getActivity());
        myToast = new MyToast(getContext());
        redirectUtil = new RedirectUtil(getActivity());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new Dialog(getActivity(), R.style.DialogFragment);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(mDialog.getWindow().getAttributes());

        final View view = View.inflate(getActivity(), R.layout.fragment_user_service, null);
        mDialog.getWindow().setAttributes(layoutParams);
        mDialog.setContentView(view);

        return mDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_service, container, false);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        settingsFragmentViewModel = new ViewModelProvider((ViewModelStoreOwner) getActivity()).get(SettingsFragmentViewModel.class);

        //hooks
        no_user_services_layout = view.findViewById(R.id.no_user_services_layout);
        user_services_recycler_layout = view.findViewById(R.id.user_services_recycler_layout);


        settingUpListeners();
        if (!util.isInternetConnected()) {
            //Show a dialog
            util.ShowCustomDialog();
        }else{
            fetchServicesArtisanList(sessionManager.getUser().getIdUser());
        }


        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        return view;
    }


    private void fetchServicesArtisanList(int idUser) {

        settingsFragmentViewModel.getServicesArtisanListNoSections(idUser).observe((LifecycleOwner)getActivity(), new Observer<ArrayList<Service>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(ArrayList<Service> services) {

                if (!services.isEmpty()) {
                    serviceList = services;
                    initRecyclerView(services);
                    no_user_services_layout.setVisibility(View.GONE);
                    user_services_recycler_layout.setVisibility(View.VISIBLE);
                }else{
                    user_services_recycler_layout.setVisibility(View.GONE);
                    no_user_services_layout.setVisibility(View.VISIBLE);
                }
            }
        });
        settingsFragmentViewModel.getServiceRepoToast().observe((LifecycleOwner)getActivity(), new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                //TODO Add something to notify the user that a problem has occurred
                myToast.showError(errorResponse.getErrorMessage());
            }
        });

    }
    private void initRecyclerView(ArrayList<Service> services) {
        my_services_list_recycler_view = (RecyclerView) view.findViewById(R.id.my_services_list_recycler_view);
        my_services_list_recycler_view.setHasFixedSize(true);
        my_services_list_recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        UserServicesAdapter sectionAdapter = new UserServicesAdapter(services, getContext(), this);
        my_services_list_recycler_view.setAdapter(sectionAdapter);
    }

    private void settingUpListeners() {

        view.findViewById(R.id.edit_user_services).setOnClickListener(this);
        view.findViewById(R.id.back).setOnClickListener(this);

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
                //dismiss();
                Util.dismissAllDialogs(getFragmentManager());
                break;
            case R.id.edit_user_services:
                redirectUtil.initUpdateServicesUi();
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

    @Override
    public void onServiceClick(int position) {

    }
}
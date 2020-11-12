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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.example.servicebypro.Activities.Adapters.SelectServicesAdapter;
import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.RedirectUtil;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.Service;
import com.example.servicebypro.Remote.Models.User;
import com.example.servicebypro.SessionManager;
import com.example.servicebypro.ViewModels.SettingsFragmentViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class UpdateServicesFragment extends DialogFragment implements
        android.view.View.OnClickListener, SelectServicesAdapter.OnServiceListener {
    private Dialog mDialog;
    View view;
    private MyToast myToast;
    SessionManager sessionManager;
    IDashboard iDashboard;
    private RedirectUtil redirectUtil;
    private static final String TAG = "SelectMyServices";
    private Util util;
    TextInputLayout filter_edit_text_layout;
    User userData = new User();
    SettingsFragmentViewModel settingsFragmentViewModel;
    RecyclerView select_all_services_recycler, my_services_list_recycler_view;
    SelectServicesAdapter adapter;
    ArrayList<Service> allServicesList = new ArrayList<>();
    ArrayList<Service> userServicesList = new ArrayList<>();
    Button apply_changes;


    public UpdateServicesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        util = new Util(getContext());
        myToast = new MyToast(getContext());
        redirectUtil = new RedirectUtil(getActivity());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new Dialog(getActivity(), R.style.DialogFragment);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(mDialog.getWindow().getAttributes());

        final View view = View.inflate(getActivity(), R.layout.fragment_assignment_finished, null);
        mDialog.getWindow().setAttributes(layoutParams);
        mDialog.setContentView(view);

        return mDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_update_services, container, false);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        settingsFragmentViewModel = new ViewModelProvider((ViewModelStoreOwner) getActivity()).get(SettingsFragmentViewModel.class);
        userServicesList = userData.getServices();
        //hooks
        apply_changes = view.findViewById(R.id.apply_changes);
        filter_edit_text_layout = view.findViewById(R.id.filter_edit_text);
        if (!util.isInternetConnected()) {
            //Show a dialog
            util.ShowCustomDialog();
        }else{
            fetchAllServicesList();
        }
        filter_edit_text_layout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        //listeners
        view.findViewById(R.id.apply_changes).setOnClickListener(this);
        view.findViewById(R.id.back_search).setOnClickListener(this);
        view.findViewById(R.id.cancel).setOnClickListener(this);

        mDialog.setCanceledOnTouchOutside(true);
        return view;
    }

    private void filter(String text) {
        ArrayList<Service> filteredList = new ArrayList<>();
        for (Service item : allServicesList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.getFilter().filter(text);
    }


    private void fetchAllServicesList() {

        settingsFragmentViewModel.getAllServicesList().observe((LifecycleOwner) getActivity(), new Observer<ArrayList<Service>>() {
            @Override
            public void onChanged(ArrayList<Service> services) {
                for (int i = 0; i < services.size(); i++) {
                    for (int j = 0; j < userServicesList.size(); j++) {
                        if (userServicesList.get(j).getName().equals(services.get(i).getName())) {
                            services.get(i).setSelected(true);
                        }
                    }
                }
                allServicesList = services;
                initRecyclerView(allServicesList);
            }
        });
        settingsFragmentViewModel.getServiceRepoToast().observe((LifecycleOwner) getActivity(), new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                //TODO Add something to notify the user that a problem has occurred
                myToast.showError(errorResponse.getErrorMessage());
            }
        });

    }

    private void initRecyclerView(ArrayList<Service> services) {
        select_all_services_recycler = view.findViewById(R.id.select_my_services_recycler);
        select_all_services_recycler.setHasFixedSize(true);
        select_all_services_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SelectServicesAdapter(services, getContext(), this);
        select_all_services_recycler.setAdapter(adapter);
    }


    @Override
    public void onServiceClick(int position) {

    }

    public ArrayList<Service> getUserNewServicesList() {
        ArrayList<Service> newList = new ArrayList<>();
        for (int j = 0; j < allServicesList.size(); j++) {
            if (allServicesList.get(j).isSelected()) {
                newList.add(allServicesList.get(j));
            }
        }
        return newList;
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
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back_search:
            case R.id.cancel:
                dismiss();
                break;

            case R.id.apply_changes:
                if (!filter_edit_text_layout.isEndIconVisible()) {
                    //put all in session or find a solution
                    //todo update it first in api if success do this

                    // sessionManager.saveUserServicesList(services);
                    if (!util.isInternetConnected()) {
                        //Show a dialog
                        util.ShowCustomDialog();
                    }else{
                        updateUserServicesList(userData.getIdUser(), getUserNewServicesList());
                    }
                }
                break;

        }
    }

    private void updateUserServicesList(int userId, ArrayList<Service> services) {
        ArrayList<Service> test = new ArrayList<>();

        for (int i = 0; i < services.size(); i++) {
            Service service = new Service(services.get(i).getIdService(), services.get(i).getCategorie(), services.get(i).getName());
            test.add(service);
        }


        settingsFragmentViewModel.updateUserServicesList(userId, test).observe((LifecycleOwner) getActivity(), new Observer<ArrayList<Service>>() {
            @Override
            public void onChanged(ArrayList<Service> services) {
                //myToast.showWarning("Alla serrv list " + services.size());
                sessionManager.saveUserServicesList(services);
                redirectUtil.initUserServicesUi();
            }
        });
        settingsFragmentViewModel.getServiceRepoToast().observe((LifecycleOwner) getActivity(), new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                //TODO Add something to notify the user that a problem has occurred
                myToast.showError(errorResponse.getErrorMessage());
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sessionManager = new SessionManager(getActivity());
        iDashboard = (IDashboard) getActivity();
        userData = sessionManager.getUser();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
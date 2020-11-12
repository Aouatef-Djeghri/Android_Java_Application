package com.example.servicebypro.Activities.Dashboard.Search;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.servicebypro.Activities.Adapters.BrowseWorkAdapter;
import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Api.RetrofitClient;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.User;
import com.example.servicebypro.Remote.Models.Work;
import com.example.servicebypro.SessionManager;
import com.example.servicebypro.ViewModels.AddFragmentViewModel;
import com.example.servicebypro.ViewModels.SearchFragmentViewModel;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.util.ArrayList;

import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckSearchedWorkListFragment extends DialogFragment implements
        android.view.View.OnClickListener, BrowseWorkAdapter.OnWorkListener {

    private Util util;
    private MyToast myToast;
    private View view;
    private IDashboard iDashboard;
    ImageView freelancer_avatar;
    TextView freelancer_name, freelancer_about, freelancer_address, freelancer_completed_projects, freelancer_rating;
    RecyclerView work_list_recycler;
    User user = new User();
    AddFragmentViewModel addFragmentViewModel;
    SearchFragmentViewModel searchFragmentViewModel;
    private Dialog mDialog;
    public Button no;

    ArrayList<Work> workList = new ArrayList<>();
    SessionManager sessionManager;
    BrowseWorkAdapter browseWorkAdapter;
    ImageView back_search;
    RelativeLayout no_work_layout;

    public CheckSearchedWorkListFragment() {
        // Required empty public constructor
    }

    private CheckSearchedWorkListFragment.FragmentSelectWorkForDetails listener;


    public interface FragmentSelectWorkForDetails {
        void onInputSelectWorkForDetailsSent(Work works);
    }

    private CheckSearchedWorkListFragment.FragmentHasUserAlreadyApplied hasAppliedListener;


    public interface FragmentHasUserAlreadyApplied {
        void onInputAlreadyAppliedSent(Boolean applied);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new Dialog(getActivity(), R.style.DialogFragment);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(mDialog.getWindow().getAttributes());

        final View view = View.inflate(getActivity(), R.layout.fragment_check_searched_work_list, null);
        mDialog.getWindow().setAttributes(layoutParams);
        mDialog.setContentView(view);

        return mDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_check_searched_work_list, container, false);
        addFragmentViewModel = new ViewModelProvider((ViewModelStoreOwner) this.getActivity()).get(AddFragmentViewModel.class);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        no_work_layout = view.findViewById(R.id.no_work_layout);
        work_list_recycler = view.findViewById(R.id.work_list_recycler);
        back_search = view.findViewById(R.id.back_search);
        back_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        workList = iDashboard.getSearchedWork();
        if (!workList.isEmpty()) {
            work_list_recycler.setVisibility(View.VISIBLE);
            no_work_layout.setVisibility(View.GONE);
            initRecyclerView();
        } else {
            work_list_recycler.setVisibility(View.GONE);
            no_work_layout.setVisibility(View.VISIBLE);
        }

        mDialog.setCanceledOnTouchOutside(true);
        return view;

    }

    private void initRecyclerView() {
        work_list_recycler = view.findViewById(R.id.work_list_recycler);
        work_list_recycler = (RecyclerView) view.findViewById(R.id.work_list_recycler);
        work_list_recycler.setHasFixedSize(true);
        work_list_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        browseWorkAdapter = new BrowseWorkAdapter(workList, getContext(), this);
        work_list_recycler.setAdapter(browseWorkAdapter);

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
    public void onClick(View v) {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sessionManager = new SessionManager(getActivity());
        myToast = new MyToast(getActivity());
        iDashboard = (IDashboard) getActivity();
        if (context instanceof CheckSearchedWorkListFragment.FragmentSelectWorkForDetails) {
            listener = (CheckSearchedWorkListFragment.FragmentSelectWorkForDetails) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentAListener");
        }
        if (context instanceof CheckSearchedWorkListFragment.FragmentHasUserAlreadyApplied) {
            hasAppliedListener = (CheckSearchedWorkListFragment.FragmentHasUserAlreadyApplied) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentAListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        hasAppliedListener = null;
    }

    @Override
    public void onWorkClick(int position) {
        //TODO will be redirected to work info fragment
        hasAppliedListener.onInputAlreadyAppliedSent(isUserAlreadyApplied(workList.get(position)));

        initWorkDetails(workList.get(position));

    }

    public void initWorkDetails(Work works) {
        listener.onInputSelectWorkForDetailsSent(works);
        DialogFragment dialogFragment = new CheckSearchedWorkDetailsFragment();
        dialogFragment.show(getActivity().getFragmentManager(), "dialog");
    }


    public boolean isUserAlreadyApplied(Work work) {

        for (int i = 0; i < work.getApplications().size(); i++) {
            if (work.getApplications().get(i).getUser().getIdUser().equals(sessionManager.getUser().getIdUser()) ) {
                return true;
            }
        }
        return false;
    }

}
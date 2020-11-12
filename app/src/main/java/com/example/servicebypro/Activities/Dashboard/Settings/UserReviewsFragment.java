package com.example.servicebypro.Activities.Dashboard.Settings;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;

import com.example.servicebypro.Activities.Adapters.ReviewAdapter;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.RedirectUtil;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.Review;
import com.example.servicebypro.SessionManager;
import com.example.servicebypro.ViewModels.AssignmentsFragmentViewModel;
import com.example.servicebypro.ViewModels.SettingsFragmentViewModel;

import java.util.ArrayList;


public class UserReviewsFragment extends DialogFragment implements
        android.view.View.OnClickListener, ReviewAdapter.OnReviewListener {
    private Util util;
    private MyToast myToast;
    private View view;
    private RedirectUtil redirectUtil;
    SessionManager sessionManager;
    private Dialog mDialog;
    private SegmentedButtonGroup sbg;
    private AssignmentsFragmentViewModel assignmentsFragmentViewModel;
    RecyclerView my_reviews_list_recycler_view;
    ArrayList<Review> reviewsList = new ArrayList<>();

    public UserReviewsFragment() {
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

        final View view = View.inflate(getActivity(), R.layout.fragment_user_reviews, null);
        mDialog.getWindow().setAttributes(layoutParams);
        mDialog.setContentView(view);

        return mDialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_reviews, container, false);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        assignmentsFragmentViewModel = new ViewModelProvider((ViewModelStoreOwner) getActivity()).get(AssignmentsFragmentViewModel.class);

        view.findViewById(R.id.back).setOnClickListener(this);
        getUserAsClientReviews(sessionManager.getUser().getIdUser());

        //Hooks
        sbg = (SegmentedButtonGroup) view.findViewById(R.id.sbg);

        sbg.setOnPositionChangedListener(new SegmentedButtonGroup.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(int position) {
                reviewsList = new ArrayList<>();
                switch (position) {
                    case 0:
                        if (!util.isInternetConnected()) {
                            //Show a dialog
                            util.ShowCustomDialog();
                        }else{
                            getUserAsClientReviews(sessionManager.getUser().getIdUser());
                        }
                        break;
                    case 1:
                        if (!util.isInternetConnected()) {
                            //Show a dialog
                            util.ShowCustomDialog();
                        }else{
                            getUserAsFreelancerReviews(sessionManager.getUser().getIdUser());
                        }
                        break;
                }
            }
        });


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

    private void initRecyclerViewReview(ArrayList<Review> reviews) {
        my_reviews_list_recycler_view = (RecyclerView) view.findViewById(R.id.my_reviews_list_recycler_view);
        ReviewAdapter reviewAdapter = new ReviewAdapter(reviews, getContext(), this);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setSmoothScrollbarEnabled(false);
        my_reviews_list_recycler_view.setLayoutManager(layoutManager);
        my_reviews_list_recycler_view.setAdapter(reviewAdapter);

    }

    private void getUserAsFreelancerReviews(int artisanId) {

        assignmentsFragmentViewModel.getUserAsArtisanReviews(artisanId).observe((LifecycleOwner) getActivity(), new Observer<ArrayList<Review>>() {
            @Override
            public void onChanged(ArrayList<Review> reviews) {
                reviewsList = reviews;
                initRecyclerViewReview(reviewsList);
            }
        });
        assignmentsFragmentViewModel.getToastReview().observe((LifecycleOwner) getActivity(), new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                //TODO Add something to notify the user that a problem has occurred
                //Toast.makeText(getActivity(), errorResponse.getErrorMessage(), Toast.LENGTH_LONG).show();
                myToast.showError(errorResponse.getErrorMessage());
            }
        });

    }

    private void getUserAsClientReviews(int clientId) {

        assignmentsFragmentViewModel.getUserAsClientReviews(clientId).observe((LifecycleOwner) getActivity(), new Observer<ArrayList<Review>>() {
            @Override
            public void onChanged(ArrayList<Review> reviews) {
                reviewsList = reviews;
                initRecyclerViewReview(reviewsList);
            }
        });
        assignmentsFragmentViewModel.getToastReview().observe((LifecycleOwner) getActivity(), new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                //TODO Add something to notify the user that a problem has occurred
                //Toast.makeText(getActivity(), errorResponse.getErrorMessage(), Toast.LENGTH_LONG).show();
                myToast.showError(errorResponse.getErrorMessage());
            }
        });

    }

    @Override
    public void onReviewClick(int position) {

    }
}
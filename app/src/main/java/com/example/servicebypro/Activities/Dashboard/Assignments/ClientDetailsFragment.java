package com.example.servicebypro.Activities.Dashboard.Assignments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.servicebypro.Activities.Adapters.ReviewAdapter;
import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.RedirectUtil;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.Review;
import com.example.servicebypro.Remote.Models.User;
import com.example.servicebypro.ViewModels.AssignmentsFragmentViewModel;

import java.util.ArrayList;


public class ClientDetailsFragment extends DialogFragment implements
        android.view.View.OnClickListener , ReviewAdapter.OnReviewListener {

    private Util util;
    private MyToast myToast;
    private View view;
    private RedirectUtil redirectUtil;
    private IDashboard iDashboard;
    ImageView client_avatar;
    TextView client_name, client_about, client_address, client_completed_projects, client_rating, client_join_date, client_phone;
    Button chat_with_client_button;
    //RecyclerView reviews_recycler;
    User user = new User();
    private Dialog mDialog;
    private AssignmentsFragmentViewModel assignmentsFragmentViewModel;
    ArrayList<Review> reviewsList = new ArrayList<>();
    RecyclerView my_reviews_list_recycler_view;
    public ClientDetailsFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new Dialog(getActivity(), R.style.DialogFragment);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(mDialog.getWindow().getAttributes());
        //   layoutParams.windowAnimations = R.style.SlideUpDownDialog;

        final View view = View.inflate(getActivity(), R.layout.fragment_client_details, null);
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
        view = inflater.inflate(R.layout.fragment_client_details, container, false);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        assignmentsFragmentViewModel = new ViewModelProvider((ViewModelStoreOwner) getActivity()).get(AssignmentsFragmentViewModel.class);
        //Setting listeners
        view.findViewById(R.id.back_search).setOnClickListener(this);
        view.findViewById(R.id.chat_with_client_button).setOnClickListener(this);

        user = iDashboard.getSelectedWork().getUser();
        settingUpHooks();
        settingUpUserData();
        mDialog.setCanceledOnTouchOutside(true);
        return view;
    }


    public void settingUpUserData() {

       // client_avatar.setImageResource(R.drawable.field_id_image_icone);
        util.setAvatar(user.getAvatar(),client_avatar);
        String first_name = user.getFirstName();
        String last_name = user.getLastName();
        client_name.setText(first_name.substring(0, 1).toUpperCase() + first_name.substring(1) + " " + last_name.substring(0, 1).toUpperCase() + last_name.substring(1));
        client_about.setText(user.getAboutMe());
        client_address.setText(user.getAddress().getWilaya() + ", " + user.getAddress().getCommune());

        //todo get user rating
        //todo get user reviews as a client
        getUserAsClientReviews(user.getIdUser());
    }

    public void settingUpHooks() {
        client_avatar = view.findViewById(R.id.client_avatar);
        client_name = view.findViewById(R.id.client_name);
        client_about = view.findViewById(R.id.client_about);
        client_address = view.findViewById(R.id.client_address);
        client_completed_projects = view.findViewById(R.id.client_completed_projects);
        client_rating = view.findViewById(R.id.client_rating);
        client_phone = view.findViewById(R.id.client_phone);

    }

    private void getUserAsClientReviews(int artisanId) {

        assignmentsFragmentViewModel.getUserAsClientReviews(artisanId).observe((LifecycleOwner)getActivity(), new Observer<ArrayList<Review>>() {
            @Override
            public void onChanged(ArrayList<Review> reviews) {
                reviewsList = reviews;
            //    myToast.showSuccess(reviews.size() + " reviews size");

                //TODO
                client_completed_projects.setText(String.valueOf(reviewsList.size()));
                client_rating.setText(util.calculateAverageRating(reviews)+"/5");

                initRecyclerViewReview(reviews);
            }
        });
        assignmentsFragmentViewModel.getToastReview().observe((LifecycleOwner)getActivity(), new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                //TODO Add something to notify the user that a problem has occurred
                myToast.showError(errorResponse.getErrorMessage());
            }
        });

    }

    private void initRecyclerViewReview(ArrayList<Review> reviews) {
        my_reviews_list_recycler_view = (RecyclerView) view.findViewById(R.id.my_reviews_list_recycler_view);
        ReviewAdapter reviewAdapter = new ReviewAdapter(reviews, getContext(), this);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setSmoothScrollbarEnabled(false);
        my_reviews_list_recycler_view.setLayoutManager(layoutManager);
        my_reviews_list_recycler_view.setAdapter(reviewAdapter);

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
            case R.id.chat_with_client_button:
               // myToast.showInfo("chat with client");
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

    @Override
    public void onReviewClick(int position) {

    }
}
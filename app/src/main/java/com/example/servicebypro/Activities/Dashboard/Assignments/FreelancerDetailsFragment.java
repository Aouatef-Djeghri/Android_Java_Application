package com.example.servicebypro.Activities.Dashboard.Assignments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.servicebypro.Activities.Adapters.ReviewAdapter;
import com.example.servicebypro.Activities.Adapters.UserServicesAdapter;
import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.RedirectUtil;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.Application;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.Review;
import com.example.servicebypro.Remote.Models.Service;
import com.example.servicebypro.Remote.Models.User;
import com.example.servicebypro.Remote.Models.Work;
import com.example.servicebypro.ViewModels.AssignmentsFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;


public class FreelancerDetailsFragment extends DialogFragment implements
        android.view.View.OnClickListener, UserServicesAdapter.OnServiceListener  , ReviewAdapter.OnReviewListener {

    private Util util;
    private MyToast myToast;
    private View view;
    private RedirectUtil redirectUtil;
    private IDashboard iDashboard;
    ImageView freelancer_avatar;
    TextView freelancer_name, freelancer_about, freelancer_address, freelancer_completed_projects, freelancer_rating, freelancer_about_empty, freelancer_phone;
    Button hire_me_button, chat_button;//, decline_button;
    //RecyclerView reviews_recycler;
    User user = new User();
    Work work = new Work();
    private Dialog mDialog;
    CardView cardView_services;
    RecyclerView my_services_list_recycler_view;
    private AssignmentsFragmentViewModel assignmentsFragmentViewModel;
    RelativeLayout no_services_layout;
    LinearLayout chat_button_layout, buttons_layout;
    RecyclerView my_reviews_list_recycler_view;
    ArrayList<Review> reviewsList = new ArrayList<>();

    public FreelancerDetailsFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new Dialog(getActivity(), R.style.DialogFragment);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(mDialog.getWindow().getAttributes());
        //   layoutParams.windowAnimations = R.style.SlideUpDownDialog;

        final View view = View.inflate(getActivity(), R.layout.fragment_freelancer_details, null);
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
        view = inflater.inflate(R.layout.fragment_freelancer_details, container, false);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        assignmentsFragmentViewModel = new ViewModelProvider((ViewModelStoreOwner) getActivity()).get(AssignmentsFragmentViewModel.class);
        //Setting listeners
        view.findViewById(R.id.cardView_services).setOnClickListener(this);
        view.findViewById(R.id.back_search).setOnClickListener(this);
        view.findViewById(R.id.hire_me_button).setOnClickListener(this);
        view.findViewById(R.id.chat_button).setOnClickListener(this);


        work = iDashboard.getSelectedWork();


        switch (work.getType()) {
            case "post":
                switch (work.getStatus()) {
                    case "request":
                        user = iDashboard.getUserApplication().getUser();
                        break;
                    case "pending":
                    case "finished":
                    case "canceled":
                        user = util.getAcceptedApplication(work).getUser();
                        break;
                }
                break;
            case "proposal":
                user = work.getApplications().get(0).getUser();
                break;
        }

        settingUpHooks();
        settingUpUserData();
        initRecyclerView(user.getServices());
        mDialog.setCanceledOnTouchOutside(true);
        return view;
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
    private void getUserAsFreelancerReviews(int artisanId) {

        assignmentsFragmentViewModel.getUserAsArtisanReviews(artisanId).observe((LifecycleOwner)getActivity(), new Observer<ArrayList<Review>>() {
            @Override
            public void onChanged(ArrayList<Review> reviews) {
                reviewsList = reviews;
                //myToast.showSuccess(reviews.size() + " reviews size");

                //TODO
                freelancer_completed_projects.setText(String.valueOf(reviewsList.size()));
                freelancer_rating.setText(util.calculateAverageRating(reviews)+"/5");

                initRecyclerViewReview(reviews);
            }
        });
        assignmentsFragmentViewModel.getToastReview().observe((LifecycleOwner)getActivity(), new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                //TODO Add something to notify the user that a problem has occurred
                //Toast.makeText(getActivity(), errorResponse.getErrorMessage(), Toast.LENGTH_LONG).show();
                myToast.showError(errorResponse.getErrorMessage());
            }
        });

    }

    public void settingUpUserData() {

        //freelancer_avatar.setImageResource(R.drawable.field_id_image_icone);
        util.setAvatar(user.getAvatar(),freelancer_avatar);
//        freelancer_name.setText(user.getFirstName() + " " + user.getLastName());
        String first_name = user.getFirstName();
        String last_name = user.getLastName();
        freelancer_name.setText(first_name.substring(0, 1).toUpperCase() + first_name.substring(1) + " " + last_name.substring(0, 1).toUpperCase() + last_name.substring(1));
        if (user.getAboutMe() == null) {
            freelancer_about.setVisibility(View.GONE);
            freelancer_about_empty.setVisibility(View.VISIBLE);
        } else {
            freelancer_about.setText(user.getAboutMe());
            freelancer_about.setVisibility(View.VISIBLE);
            freelancer_about_empty.setVisibility(View.GONE);
        }

        if (user.getAddress().getWilaya().equals("") || user.getAddress().getCommune().equals("")) {
            freelancer_address.setText(user.getAddress().getWilaya() + "" + user.getAddress().getCommune());
        } else {
            freelancer_address.setText(user.getAddress().getWilaya() + ", " + user.getAddress().getCommune());
        }
        //TODO
        freelancer_completed_projects.setText("15");
        freelancer_rating.setText("8.9");
        freelancer_phone = view.findViewById(R.id.freelancer_phone);

        switch (work.getStatus()) {
            case "request":
                freelancer_phone.setVisibility(View.GONE);
                switch (work.getType()) {
                    case "post":
                        buttons_layout.setVisibility(View.VISIBLE);
                        chat_button_layout.setVisibility(View.GONE);
                        break;
                    case "proposal":
                        buttons_layout.setVisibility(View.GONE);
                        chat_button_layout.setVisibility(View.VISIBLE);
                        break;
                }

                break;
            case "pending":
            case "finished":
                switch (work.getType()) {
                    case "post":
                        //todo get accepted freelancer
                        freelancer_phone.setText(util.getAcceptedApplication(work).getUser().getPhone());
                        break;
                    case "proposal":
                        freelancer_phone.setText(work.getApplications().get(0).getUser().getPhone());
                        break;
                }
                freelancer_phone.setVisibility(View.VISIBLE);
                chat_button_layout.setVisibility(View.VISIBLE);
                buttons_layout.setVisibility(View.VISIBLE);
                break;
        }

        //todo get user rating
        //todo get user reviews as a client
        getUserAsFreelancerReviews(user.getIdUser());
    }

    public void settingUpHooks() {
        freelancer_avatar = view.findViewById(R.id.freelancer_avatar);
        freelancer_name = view.findViewById(R.id.freelancer_name);
        freelancer_about = view.findViewById(R.id.freelancer_about);
        freelancer_address = view.findViewById(R.id.freelancer_address);
        freelancer_completed_projects = view.findViewById(R.id.freelancer_completed_projects);
        freelancer_rating = view.findViewById(R.id.freelancer_rating);
        hire_me_button = view.findViewById(R.id.hire_me_button);
        chat_button = view.findViewById(R.id.chat_button);
        chat_button_layout = view.findViewById(R.id.chat_button_layout);
        buttons_layout = view.findViewById(R.id.buttons_layout);
        cardView_services = view.findViewById(R.id.cardView_services);
        no_services_layout = view.findViewById(R.id.no_services_layout);
        freelancer_about_empty = view.findViewById(R.id.freelancer_about_empty);
    }

    private void initRecyclerView(ArrayList<Service> services) {
        my_services_list_recycler_view = (RecyclerView) view.findViewById(R.id.my_services_list_recycler_view);
        if (services.size() != 0) {
            no_services_layout.setVisibility(View.GONE);
            my_services_list_recycler_view.setVisibility(View.VISIBLE);
            my_services_list_recycler_view.setHasFixedSize(true);
            my_services_list_recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
            UserServicesAdapter sectionAdapter = new UserServicesAdapter(services, getContext(), this);
            my_services_list_recycler_view.setAdapter(sectionAdapter);
            //my_services_list_recycler_view.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        } else {
            my_services_list_recycler_view.setVisibility(View.GONE);
            no_services_layout.setVisibility(View.VISIBLE);
        }


    }


    public void initUiFreelancer() {
        switch (iDashboard.getUserApplication().getStatus()) {
            case "request":
                break;
            case "closed":
                break;
        }
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

            case R.id.hire_me_button:
                //TODO hire this freelancer and decline all other applications

                if (!util.isInternetConnected()) {
                    //Show a dialog
                    util.ShowCustomDialog();
                }else{
                    acceptWorkProposal(iDashboard.getUserApplication());
                }
                break;
            case R.id.chat_button:
               // myToast.showInfo("chat with this freelancer");
                break;
        }
    }

    public void acceptWorkProposal(Application application) {

        assignmentsFragmentViewModel.acceptWorkProposal(application).observe((LifecycleOwner) getActivity(), new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                util.dismissAllDialogs(getFragmentManager());
                iDashboard.inflateFragment(getString(R.string.fragment_assignment));
            }
        });
        assignmentsFragmentViewModel.getToast().observe((LifecycleOwner) getActivity(), new Observer<ErrorResponse>() {
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
    public void onServiceClick(int position) {

    }

    @Override
    public void onReviewClick(int position) {

    }
}
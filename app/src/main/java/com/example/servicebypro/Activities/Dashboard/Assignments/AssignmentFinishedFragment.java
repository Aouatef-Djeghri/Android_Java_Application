package com.example.servicebypro.Activities.Dashboard.Assignments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.servicebypro.Activities.Adapters.ApplicationAdapter;
import com.example.servicebypro.Activities.Adapters.ReviewAdapter;
import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.RedirectUtil;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.Application;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.Review;
import com.example.servicebypro.Remote.Models.User;
import com.example.servicebypro.Remote.Models.Work;
import com.example.servicebypro.SessionManager;
import com.example.servicebypro.ViewModels.AssignmentsFragmentViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;

import static com.example.servicebypro.Activities.HelperClasses.Constants.ERROR_DIALOG_REQUEST;
import static com.example.servicebypro.Activities.HelperClasses.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;


public class AssignmentFinishedFragment extends DialogFragment implements
        android.view.View.OnClickListener, ApplicationAdapter.OnApplicationListener, ReviewAdapter.OnReviewListener {

    private static final String TAG = "AssignmentFinishedFragm";
    LinearLayout expandableView_work_details;
    Button arrowBtn;
    CardView cardView, cardView_client, cardView_payment, cardView_freelancer, cardView_applications, cardView_service, cardView_location, cardView_reviews;
    private Util util;
    private RedirectUtil redirectUtil;
    private MyToast myToast;
    private View view;
    private IDashboard iDashboard;
    private RecyclerView recyclerView;
    private Dialog mDialog;
    ArrayList<Application> applicationList = new ArrayList<>();
    SessionManager sessionManager;
    TextView work_title, work_service, work_description, work_client_name, work_address, work_due_date, work_price, work_type;
    Button  arrowBtn_client, arrowBtn_payment, arrowBtn_freelancer, arrowBtn_applications, arrowBtn_service, arrowBtn_location, arrowBtn_reviews;
    LinearLayout work_price_layout, payment_method_layout, expandableView_work_reviews, expandableView_work_applications, expandableView_work_service, expandableView_work_location, expandableView_work_freelancer, expandableView_work_payment, expandableView_work_client;
    TextView work_payment_method, work_freelancer_name, work_category; //option_digit
    private ApplicationAdapter adapter;
    ScrollView pending_scroll;
    RecyclerView reviews_recycler;
    private AssignmentsFragmentViewModel assignmentsFragmentViewModel;
    ImageView work_image1,work_image2,work_image3,details_icon, location_icon, service_icon, payment_method_icon, proposals_icon, freelancer_icon, client_icon, reviews_icon,freelancer_avatar,client_avatar;
    Work work = new Work();

    private AssignmentFinishedFragment.FragmentOpenFullImage listenerImageClick;
    public interface FragmentOpenFullImage {
        void onImageLinkSent(String imageLink);
    }

    public AssignmentFinishedFragment() {
        // Required empty public constructor
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        util = new Util(getActivity());
        myToast = new MyToast(getActivity());
        redirectUtil = new RedirectUtil(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_assignment_finished, container, false);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        assignmentsFragmentViewModel = new ViewModelProvider((ViewModelStoreOwner) getActivity()).get(AssignmentsFragmentViewModel.class);

        //Setting listeners
        view.findViewById(R.id.back_search).setOnClickListener(this);
        view.findViewById(R.id.arrowBtn_client).setOnClickListener(this);
        view.findViewById(R.id.arrowBtn_payment).setOnClickListener(this);
        view.findViewById(R.id.arrowBtn).setOnClickListener(this);
        view.findViewById(R.id.arrowBtn_freelancer).setOnClickListener(this);
        view.findViewById(R.id.arrowBtn_applications).setOnClickListener(this);
        view.findViewById(R.id.arrowBtn_service).setOnClickListener(this);
        view.findViewById(R.id.work_address_in_map).setOnClickListener(this);
        view.findViewById(R.id.arrowBtn_location).setOnClickListener(this);
        view.findViewById(R.id.see_client_profile).setOnClickListener(this);
        view.findViewById(R.id.see_freelancer_profile).setOnClickListener(this);
        view.findViewById(R.id.arrowBtn_reviews).setOnClickListener(this);
        view.findViewById(R.id.work_image1).setOnClickListener(this);
        view.findViewById(R.id.work_image2).setOnClickListener(this);
        view.findViewById(R.id.work_image3).setOnClickListener(this);
        settingUpHooks();
        settingUpWorkData();

        mDialog.setCanceledOnTouchOutside(true);
        return view;
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
                dismiss();
                break;
            case R.id.work_image1:
                listenerImageClick.onImageLinkSent(work.getFirstImage());
                redirectUtil.initFullWorkImageUi();
                break;
            case R.id.work_image2:
                listenerImageClick.onImageLinkSent(work.getSecondImage());
                redirectUtil.initFullWorkImageUi();
                break;
            case R.id.work_image3:
                listenerImageClick.onImageLinkSent(work.getThirdImage());
                redirectUtil.initFullWorkImageUi();
                break;
            case R.id.see_client_profile:
                redirectUtil.initWorkClientUi();
                break;
            case R.id.see_freelancer_profile:
                redirectUtil.initAcceptedFreelancerDetails();
                break;
            case R.id.work_address_in_map:
                if (isServicesOK()) {
                    getLocationPermission();
                }
                break;
            case R.id.arrowBtn:
                if (expandableView_work_details.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandableView_work_details.setVisibility(View.VISIBLE);
                    arrowBtn.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                } else {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandableView_work_details.setVisibility(View.GONE);
                    arrowBtn.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                }
                break;

            case R.id.arrowBtn_payment:
                if (expandableView_work_payment.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(cardView_payment, new AutoTransition());
                    expandableView_work_payment.setVisibility(View.VISIBLE);
                    arrowBtn_payment.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                } else {
                    TransitionManager.beginDelayedTransition(cardView_payment, new AutoTransition());
                    expandableView_work_payment.setVisibility(View.GONE);
                    arrowBtn_payment.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                }
                break;


            case R.id.arrowBtn_client:
                if (expandableView_work_client.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(cardView_client, new AutoTransition());
                    expandableView_work_client.setVisibility(View.VISIBLE);
                    arrowBtn_client.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                } else {
                    TransitionManager.beginDelayedTransition(cardView_client, new AutoTransition());
                    expandableView_work_client.setVisibility(View.GONE);
                    arrowBtn_client.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                }
                break;

            case R.id.arrowBtn_freelancer:
                if (expandableView_work_freelancer.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(cardView_freelancer, new AutoTransition());
                    expandableView_work_freelancer.setVisibility(View.VISIBLE);
                    arrowBtn_freelancer.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                } else {
                    TransitionManager.beginDelayedTransition(cardView_freelancer, new AutoTransition());
                    expandableView_work_freelancer.setVisibility(View.GONE);
                    arrowBtn_freelancer.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                }
                break;
            case R.id.arrowBtn_applications:
                if (expandableView_work_applications.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(cardView_applications, new AutoTransition());
                    expandableView_work_applications.setVisibility(View.VISIBLE);
                    arrowBtn_applications.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                } else {
                    TransitionManager.beginDelayedTransition(cardView_applications, new AutoTransition());
                    expandableView_work_applications.setVisibility(View.GONE);
                    arrowBtn_applications.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                }
                break;
            case R.id.arrowBtn_service:
                if (expandableView_work_service.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(cardView_service, new AutoTransition());
                    expandableView_work_service.setVisibility(View.VISIBLE);
                    arrowBtn_service.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                } else {
                    TransitionManager.beginDelayedTransition(cardView_service, new AutoTransition());
                    expandableView_work_service.setVisibility(View.GONE);
                    arrowBtn_service.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                }
                break;

            case R.id.arrowBtn_location:
                if (expandableView_work_location.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(cardView_location, new AutoTransition());
                    expandableView_work_location.setVisibility(View.VISIBLE);
                    arrowBtn_location.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                } else {
                    TransitionManager.beginDelayedTransition(cardView_location, new AutoTransition());
                    expandableView_work_location.setVisibility(View.GONE);
                    arrowBtn_location.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                }
                break;


            case R.id.arrowBtn_reviews:
                if (expandableView_work_reviews.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(cardView_reviews, new AutoTransition());
                    expandableView_work_reviews.setVisibility(View.VISIBLE);
                    arrowBtn_reviews.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                } else {
                    TransitionManager.beginDelayedTransition(cardView_reviews, new AutoTransition());
                    expandableView_work_reviews.setVisibility(View.GONE);
                    arrowBtn_reviews.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                }
                break;

        }
    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            //myToast.showSuccess("Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occurred but we can resolve it
            Log.d(TAG, "isServicesOK: an error occurred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        return false;
    }


    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            redirectUtil.initSeeInMapUI();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        //locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //initialise our map
                    redirectUtil.initSeeInMapUI();
                } else {
                    myToast.showSuccess("Sorry, you can't open directions without enabling location permissions");
                }
            }
            break;
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sessionManager = new SessionManager(getActivity());
        iDashboard = (IDashboard) getActivity();


        if (context instanceof AssignmentFinishedFragment.FragmentOpenFullImage) {
            listenerImageClick = (AssignmentFinishedFragment.FragmentOpenFullImage) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement listenerImageClick");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        listenerImageClick = null;
    }

    public void settingUpHooks() {
        work_title = view.findViewById(R.id.work_title);
        work_service = view.findViewById(R.id.work_service);
        work_category = view.findViewById(R.id.work_category);
        work_description = view.findViewById(R.id.work_description);
        work_client_name = view.findViewById(R.id.work_client_name);
        work_address = view.findViewById(R.id.work_address);
        work_due_date = view.findViewById(R.id.work_due_date);
        work_price = view.findViewById(R.id.work_price);
        work_price_layout = view.findViewById(R.id.work_price_layout);
        payment_method_layout = view.findViewById(R.id.payment_method_layout);
        work_payment_method = view.findViewById(R.id.work_payment_method);
        cardView_client = view.findViewById(R.id.cardView_client);
        cardView_freelancer = view.findViewById(R.id.cardView_freelancer);
        expandableView_work_payment = view.findViewById(R.id.expandableView_work_payment);
        expandableView_work_client = view.findViewById(R.id.expandableView_work_client);
        arrowBtn_payment = view.findViewById(R.id.arrowBtn_payment);
        arrowBtn_client = view.findViewById(R.id.arrowBtn_client);
        arrowBtn_freelancer = view.findViewById(R.id.arrowBtn_freelancer);
        arrowBtn = view.findViewById(R.id.arrowBtn);
        cardView = view.findViewById(R.id.cardView);
        cardView_payment = view.findViewById(R.id.cardView_payment);
        expandableView_work_details = view.findViewById(R.id.expandableView_work_details);
        expandableView_work_freelancer = view.findViewById(R.id.expandableView_work_freelancer);
        work_freelancer_name = view.findViewById(R.id.work_freelancer_name);
        cardView_applications = view.findViewById(R.id.cardView_applications);
        arrowBtn_applications = view.findViewById(R.id.arrowBtn_applications);
        expandableView_work_applications = view.findViewById(R.id.expandableView_work_applications);
        recyclerView = view.findViewById(R.id.applications_recycler);
        work_type = view.findViewById(R.id.work_type);
        cardView_service = view.findViewById(R.id.cardView_service);
        //todo
        pending_scroll = view.findViewById(R.id.pending_scroll);
        arrowBtn_service = view.findViewById(R.id.arrowBtn_service);
        expandableView_work_service = view.findViewById(R.id.expandableView_work_service);

        cardView_location = view.findViewById(R.id.cardView_location);
        arrowBtn_location = view.findViewById(R.id.arrowBtn_location);
        expandableView_work_location = view.findViewById(R.id.expandableView_work_location);
        expandableView_work_reviews = view.findViewById(R.id.expandableView_work_reviews);
        cardView_reviews = view.findViewById(R.id.cardView_reviews);
        arrowBtn_reviews = view.findViewById(R.id.arrowBtn_reviews);
        reviews_recycler = (RecyclerView) view.findViewById(R.id.reviews_recycler);
        //icons hooks
        details_icon = view.findViewById(R.id.details_icon);
        location_icon = view.findViewById(R.id.location_icon);
        service_icon = view.findViewById(R.id.service_icon);
        payment_method_icon = view.findViewById(R.id.payment_method_icon);
        proposals_icon = view.findViewById(R.id.proposals_icon);
        freelancer_icon = view.findViewById(R.id.freelancer_icon);
        client_icon = view.findViewById(R.id.client_icon);
        reviews_icon = view.findViewById(R.id.reviews_icon);
        client_avatar = view.findViewById(R.id.client_avatar);
        freelancer_avatar = view.findViewById(R.id.freelancer_avatar);

        work_image1 = view.findViewById(R.id.work_image1);
        work_image2 = view.findViewById(R.id.work_image2);
        work_image3 = view.findViewById(R.id.work_image3);
    }


    public void settingUpWorkData() {

        work = iDashboard.getSelectedWork();
        //get work reviews
        getWorkReviews(work.getIdWork());

        util.setWorkImage(work.getFirstImage(), work_image1);
        util.setWorkImage(work.getSecondImage(), work_image2);
        util.setWorkImage(work.getThirdImage(), work_image3);

        work_title.setText(work.getTitle());
        work_type.setText("Your " + work.getType() + " details");
        work_service.setText(work.getService().getName());
        work_category.setText(work.getService().getCategorie().getName());
        work_description.setText(work.getDescription());

        if (work.getAddress().getWilaya().equals("") || work.getAddress().getCommune().equals("")) {
            work_address.setText(work.getAddress().getWilaya() + "" + work.getAddress().getCommune());
        } else {
            work_address.setText(work.getAddress().getWilaya() + ", " + work.getAddress().getCommune());
        }
        String formattedJoinDate = util.formationJoinDateTime(work.getDueDate());
        work_due_date.setText(formattedJoinDate);
        if (work.getPaymentMethod().equals("not_precise")) {
            work_payment_method.setText("Price not precised");
            work_price_layout.setVisibility(View.GONE);
        } else {
            work_payment_method.setText(work.getPaymentMethod());
            work_price_layout.setVisibility(View.VISIBLE);
            work_price.setText("Price " + work.getMinPrice() + " - " + work.getMaxPrice() + " $/Hour");
        }


        switch (iDashboard.getUserRole()) {
            case "client":
                cardView_client.setVisibility(View.GONE);
                cardView_freelancer.setVisibility(View.VISIBLE);

                if (work.getType().equals("proposal")) {
                    cardView_applications.setVisibility(View.GONE);
                    String freelancerFirstName = work.getApplications().get(0).getUser().getFirstName();
                    String freelancerLastName = work.getApplications().get(0).getUser().getLastName();
                    work_freelancer_name.setText(freelancerFirstName.substring(0, 1).toUpperCase() + freelancerFirstName.substring(1) + " " + freelancerLastName.substring(0, 1).toUpperCase() + freelancerLastName.substring(1));

                    util.setAvatar(work.getApplications().get(0).getUser().getAvatar(),freelancer_avatar);
                    ViewGroup.MarginLayoutParams reviews_params = (ViewGroup.MarginLayoutParams) cardView_reviews.getLayoutParams();
                    reviews_params.topMargin = 0;
                    ViewGroup.MarginLayoutParams params_freelancer = (ViewGroup.MarginLayoutParams) cardView_freelancer.getLayoutParams();
                    params_freelancer.bottomMargin = 8;
                } else {
                    //if post
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) cardView_freelancer.getLayoutParams();
                    params.bottomMargin = 0;

                    User user = util.getAcceptedApplication(work).getUser();
                    String freelancerFirstName = user.getFirstName();
                    String freelancerLastName = user.getLastName();
                    work_freelancer_name.setText(freelancerFirstName.substring(0, 1).toUpperCase() + freelancerFirstName.substring(1) + " " + freelancerLastName.substring(0, 1).toUpperCase() + freelancerLastName.substring(1));
                    util.setAvatar(user.getAvatar(),freelancer_avatar);
                    //todo get freelancer selected name
                    //todo show the list of all applications
                    initRecyclerView();
                    cardView_applications.setVisibility(View.VISIBLE);
                }


                break;
            case "artisan":
                cardView_client.setVisibility(View.VISIBLE);
                cardView_freelancer.setVisibility(View.GONE);
                util.setAvatar(work.getUser().getAvatar(),client_avatar);
                String clientFirstName = work.getUser().getFirstName();
                String clientLastName = work.getUser().getLastName();
                work_client_name.setText(clientFirstName.substring(0, 1).toUpperCase() + clientFirstName.substring(1) + " " + clientLastName.substring(0, 1).toUpperCase() + clientLastName.substring(1));
                cardView_applications.setVisibility(View.GONE);

                ViewGroup.MarginLayoutParams reviews_params = (ViewGroup.MarginLayoutParams) cardView_reviews.getLayoutParams();
                reviews_params.topMargin = 0;
                ViewGroup.MarginLayoutParams params_client = (ViewGroup.MarginLayoutParams) cardView_client.getLayoutParams();
                params_client.bottomMargin = 8;

                if (work.getType().equals("proposal")) {
                    // cardView_applications.setVisibility(View.GONE);
                } else {
                    //if post
                    // cardView_applications.setVisibility(View.VISIBLE);
                    //todo get freelancer selected name
                }
                break;
        }


        if (work.getStatus().equals("canceled")) {
            details_icon.setImageDrawable(getContext().getDrawable(R.drawable.ic_cancel));
            location_icon.setImageDrawable(getContext().getDrawable(R.drawable.ic_cancel));
            service_icon.setImageDrawable(getContext().getDrawable(R.drawable.ic_cancel));
            payment_method_icon.setImageDrawable(getContext().getDrawable(R.drawable.ic_cancel));
            proposals_icon.setImageDrawable(getContext().getDrawable(R.drawable.ic_cancel));
            freelancer_icon.setImageDrawable(getContext().getDrawable(R.drawable.ic_cancel));
            client_icon.setImageDrawable(getContext().getDrawable(R.drawable.ic_cancel));
            reviews_icon.setImageDrawable(getContext().getDrawable(R.drawable.ic_cancel));
        }

    }


    private void initRecyclerView() {
        applicationList = iDashboard.getSelectedWork().getApplications();
        recyclerView = (RecyclerView) view.findViewById(R.id.applications_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ApplicationAdapter(applicationList, getContext(), this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onApplicationClick(int position) {
        //myToast.showSuccess("application is clicked");
    }

    public void getWorkReviews(int workId) {

        assignmentsFragmentViewModel.getWorkReviews(workId).observe((LifecycleOwner) getActivity(), new Observer<ArrayList<Review>>() {
            @Override
            public void onChanged(ArrayList<Review> reviews) {
                if (!reviews.isEmpty()) {
                    initRecyclerViewReview(reviews);
                }
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

    private void initRecyclerViewReview(ArrayList<Review> reviews) {

        ReviewAdapter reviewAdapter = new ReviewAdapter(reviews, getContext(), this);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setSmoothScrollbarEnabled(false);
        reviews_recycler.setLayoutManager(layoutManager);
        reviews_recycler.setAdapter(reviewAdapter);

    }

    @Override
    public void onReviewClick(int position) {

    }
}
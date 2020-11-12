package com.example.servicebypro.Activities.Dashboard.Assignments;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;


public class ReviewFragment extends DialogFragment implements
        android.view.View.OnClickListener {

    private Util util;
    private MyToast myToast;
    private View view;
    private RedirectUtil redirectUtil;
    private Dialog mDialog;
    SessionManager sessionManager;
    ImageView rating_back;
    RatingBar mRatingBar;
    TextView mRatingScale, small_text,big_text;
    TextInputLayout mFeedback;
    Button btn_submit;
    int rating = 5;
    String reviewTitle = "Awesome. I love it";
    String reviewBody = "";
    IDashboard iDashboard;
    Work work = new Work();
    ArrayList<Review> workReviews = new ArrayList<>();
    //ScrollView rating_layout ;
    LinearLayout buttons_layout, reviews_body_layout;
    private AssignmentsFragmentViewModel assignmentsFragmentViewModel;

    public ReviewFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new Dialog(getActivity(), R.style.DialogFragment);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(mDialog.getWindow().getAttributes());
        //   layoutParams.windowAnimations = R.style.SlideUpDownDialog;

        final View view = View.inflate(getActivity(), R.layout.fragment_review, null);
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
        view = inflater.inflate(R.layout.fragment_review, container, false);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        assignmentsFragmentViewModel = new ViewModelProvider((ViewModelStoreOwner) getActivity()).get(AssignmentsFragmentViewModel.class);
        work = iDashboard.getSelectedWork();

        settingUpHooks();
        initUi();
        //Setting listeners
        view.findViewById(R.id.rating_back).setOnClickListener(this);
        view.findViewById(R.id.btn_submit).setOnClickListener(this);

        //myToast.showSuccess(work.getStatus());
        switch (work.getStatus()) {
            case "pending":
                setLayoutToUserAsClientBeforeFirstReview();
                mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        mRatingScale.setText(String.valueOf(v));
                        switch ((int) ratingBar.getRating()) {
                            case 1:
                                mRatingScale.setText("Very bad");
                                rating = 1;
                                reviewTitle = "Very bad";
                                break;
                            case 2:
                                mRatingScale.setText("Need some improvement");
                                rating = 2;
                                reviewTitle = "Need some improvement";
                                break;
                            case 3:
                                mRatingScale.setText("Good");
                                rating = 3;
                                reviewTitle = "Good";
                                break;
                            case 4:
                                mRatingScale.setText("Great");
                                rating = 4;
                                reviewTitle = "Great";
                                break;
                            case 5:
                                mRatingScale.setText("Awesome. I love it");
                                rating = 5;
                                reviewTitle = "Awesome. I love it";
                                break;
                            default:
                                mRatingScale.setText("");
                        }
                    }
                });

                break;

            case "mid_finished":
                if (!util.isInternetConnected()) {
                    //Show a dialog
                    util.ShowCustomDialog();
                }else{
                    getWorkReviews(work.getIdWork());
                }
                //test if session user is the client or the artisan and if he's the ne who already made the first review or not aka get work reviews
                break;
        }


        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        return view;
    }

    public void settingUpHooks() {
        mRatingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        mRatingScale = (TextView) view.findViewById(R.id.tvRatingScale);
        mFeedback = (TextInputLayout) view.findViewById(R.id.etFeedback);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);
        // rating_layout = view.findViewById(R.id.rating_layout);
        buttons_layout = view.findViewById(R.id.buttons_layout);
        reviews_body_layout = view.findViewById(R.id.reviews_body_layout);
        small_text = view.findViewById(R.id.small_text);
        big_text = view.findViewById(R.id.big_text);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iDashboard = (IDashboard) getActivity();
        sessionManager = new SessionManager(getActivity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rating_back:
                dismiss();
                break;
            case R.id.btn_submit:
                if (!util.validateXInput(mFeedback)) {
                    return;
                }
                reviewBody = mFeedback.getEditText().getText().toString();
               // myToast.showSuccess(reviewBody + rating);
                Review review = new Review(sessionManager.getUser(), work, reviewTitle, reviewBody, rating, util.getCurrentDateTime());
                if (!util.isInternetConnected()) {
                    //Show a dialog
                    util.ShowCustomDialog();
                }else{
                    addReview(review);
                }
                break;
        }
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        //getFragmentManager().beginTransaction().remove(mapFragment).commit();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        //dismiss();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void addReview(Review review) {

        assignmentsFragmentViewModel.addReview(review).observe((LifecycleOwner) getActivity(), new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                myToast.showSuccess("Thank you for your feedback!");
                doFinishWork(work);
                util.dismissAllDialogs(getFragmentManager());
                iDashboard.inflateFragment(getString(R.string.fragment_assignment));
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

    //todo add this inside add successful of review
    public void doFinishWork(Work work) {
        //only client can finish work
        if (work.getType().equals("proposal")) {
            finishWork(work.getApplications().get(0));
           // myToast.showInfo("Finish proposal");
        } else {
            //todo get which application has been accepted
           // myToast.showInfo("Finish post");
            Application application = util.getAcceptedApplication(work);
            if (application != null) {
                finishWork(application);
            } else {
                myToast.showInfo("Problem occurred while Finishing job");
            }
        }

    }


    public void finishWork(Application application) {

        assignmentsFragmentViewModel.finishWork(application).observe((LifecycleOwner) getActivity(), new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                myToast.showSuccess("Work is finished!");
                // dismissAllDialogs(getFragmentManager());
                //iDashboard.inflateFragment(getString(R.string.fragment_assignment));
            }
        });
        assignmentsFragmentViewModel.getToast().observe((LifecycleOwner) getActivity(), new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                //TODO Add something to notify the user that a problem has occurred
                //Toast.makeText(getActivity(), errorResponse.getErrorMessage(), Toast.LENGTH_LONG).show();
                myToast.showError(errorResponse.getErrorMessage());
            }
        });
    }

    public void getWorkReviews(int workId) {

        assignmentsFragmentViewModel.getWorkReviews(workId).observe((LifecycleOwner) getActivity(), new Observer<ArrayList<Review>>() {
            @Override
            public void onChanged(ArrayList<Review> reviews) {
                if (!reviews.isEmpty()) {

                    workReviews = reviews;
                    //if session user did the first comment
                    if (workReviews.get(0).getUser().getIdUser().equals(sessionManager.getUser().getIdUser())) {
                        //if session user it the client
                     //   myToast.showSuccess("artisan needs to send his feedback about you");
                        setLayoutToUserAsClientWhileFirstReview();
                  } else {
                        //if session user it the artisan
                      //  myToast.showSuccess("client made his reviw about you stil yours");
                        setLayoutToUserAsFreelancerWhileFirstReview();
                         }
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

    public void initUi(){
        mRatingBar.setVisibility(View.INVISIBLE);
        mRatingScale.setVisibility(View.INVISIBLE);
        reviews_body_layout.setVisibility(View.INVISIBLE);
        buttons_layout.setVisibility(View.INVISIBLE);
        big_text.setVisibility(View.INVISIBLE);
        small_text.setVisibility(View.INVISIBLE);
    }

    public void setLayoutToUserAsClientBeforeFirstReview(){
        mRatingBar.setVisibility(View.VISIBLE);
        mRatingScale.setVisibility(View.VISIBLE);
        reviews_body_layout.setVisibility(View.VISIBLE);
        buttons_layout.setVisibility(View.VISIBLE);
        big_text.setVisibility(View.VISIBLE);
        small_text.setVisibility(View.VISIBLE);
    }
    public void setLayoutToUserAsFreelancerBeforeFirstReview(){

    }

    public void setLayoutToUserAsClientWhileFirstReview(){
        mRatingBar.setVisibility(View.INVISIBLE);
        mRatingScale.setVisibility(View.INVISIBLE);
        reviews_body_layout.setVisibility(View.INVISIBLE);
        buttons_layout.setVisibility(View.INVISIBLE);
        big_text.setVisibility(View.VISIBLE);
        small_text.setVisibility(View.VISIBLE);
        big_text.setText("Thank You");
        small_text.setText("We received your feedback,\nFor this work to be completely finished, the artisan needs to send his feedback about you too!");
        small_text.setTextSize(14);
    }
    public void setLayoutToUserAsFreelancerWhileFirstReview(){
        mRatingBar.setVisibility(View.VISIBLE);
        mRatingScale.setVisibility(View.VISIBLE);
        reviews_body_layout.setVisibility(View.VISIBLE);
        buttons_layout.setVisibility(View.VISIBLE);
        big_text.setVisibility(View.VISIBLE);
        small_text.setVisibility(View.VISIBLE);
        small_text.setText("We hope that you liked working for this employer !");

    }

    public void setLayoutToUserAsClientAfterFirstReview(){

    }
    public void setLayoutToUserAsFreelancerAfterFirstReview(){

    }
}
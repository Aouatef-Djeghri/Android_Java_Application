package com.example.servicebypro.Activities.Dashboard.Add;

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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.servicebypro.Activities.Adapters.ReviewAdapter;
import com.example.servicebypro.Activities.Adapters.UserServicesAdapter;
import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.LoadingDialog;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.Application;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.ExpandableListDataPump;
import com.example.servicebypro.Remote.Models.Review;
import com.example.servicebypro.Remote.Models.Service;
import com.example.servicebypro.Remote.Models.UploadResponse;
import com.example.servicebypro.Remote.Models.User;
import com.example.servicebypro.Remote.Models.Work;
import com.example.servicebypro.SessionManager;
import com.example.servicebypro.ViewModels.AddFragmentViewModel;
import com.example.servicebypro.ViewModels.AssignmentsFragmentViewModel;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.Instant;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;


public class CheckFreelancerInfo extends DialogFragment implements
        android.view.View.OnClickListener, UserServicesAdapter.OnServiceListener , ReviewAdapter.OnReviewListener {

    private Util util;
    private MyToast myToast;
    private View view;
    private IDashboard iDashboard;
    ImageView freelancer_avatar;
    TextView freelancer_name, freelancer_about, freelancer_address, freelancer_completed_projects, freelancer_rating;
    User user = new User();
    AddFragmentViewModel addFragmentViewModel;
    private Dialog mDialog;
    public Button no;
    RecyclerView my_services_list_recycler_view;
    RecyclerView my_reviews_list_recycler_view;
    LinearLayout expandableView_user_services;
    CardView cardView_services;
    Button arrowBtn_services;
    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    ArrayList<Review> reviewsList = new ArrayList<>();
    Button hire_button;
    private AssignmentsFragmentViewModel assignmentsFragmentViewModel;

    public CheckFreelancerInfo() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(getActivity());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new Dialog(getActivity(), R.style.DialogFragment);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(mDialog.getWindow().getAttributes());
        //layoutParams.windowAnimations = R.style.SlideUpDownDialog;

        final View view = View.inflate(getActivity(), R.layout.fragment_check_freelancer_info, null);
        mDialog.getWindow().setAttributes(layoutParams);
        mDialog.setContentView(view);

        return mDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_check_freelancer_info, container, false);
        addFragmentViewModel = new ViewModelProvider((ViewModelStoreOwner) this.getActivity()).get(AddFragmentViewModel.class);
        assignmentsFragmentViewModel = new ViewModelProvider((ViewModelStoreOwner) getActivity()).get(AssignmentsFragmentViewModel.class);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        settingUpHooks();
        settingUpUserData();
        view.findViewById(R.id.back_search).setOnClickListener(this);
        view.findViewById(R.id.hire_button).setOnClickListener(this);
        mDialog.setCanceledOnTouchOutside(true);

        return view;
    }

    private void createWorkProposal(String imageOnePath, String imageTwoPath, String imageThreePath) {
        String dueDate;
        String input = iDashboard.getWorkDate().replace(" ", "T");
        LocalDateTime ldt = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ldt = LocalDateTime.parse(input);
            dueDate = ldt.toString();
        } else {
            Instant instant = Instant.parse(input);
            dueDate = instant.toString();
        }

        Work work = new Work(iDashboard.getWorkAddress(), iDashboard.getWorkService(), sessionManager.getUser(), iDashboard.getWorkTitle(),
                iDashboard.getWorkDescription(), dueDate, imageOnePath, imageTwoPath, imageThreePath, "request",
                iDashboard.getWorkType(), iDashboard.getWorkPaymentType(), 0.0, 0.0);

        Application application = new Application(iDashboard.getWorkFreelancer(), work, "request");
        addFragmentViewModel.createWorkProposal(application).observe((LifecycleOwner) this.getActivity(), new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                myToast.showSuccess("Work Proposal created successfully");
                loadingDialog.dismissDialog();
                //todo redirect to assignment frag
                //    iDashboard.inflateFragment(getString(R.string.fragment_assignment));
                dismiss();
                //todo redirect to messenger or else, still haven't figured out where
                iDashboard.inflateFragment(getString(R.string.fragment_assignment));
            }
        });
        addFragmentViewModel.getToastService().observe((LifecycleOwner) this.getActivity(), new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                //TODO Add something to notify the user that a problem has occurred
                loadingDialog.dismissDialog();
                myToast.showError(errorResponse.getErrorMessage());
            }
        });
    }

    public void settingUpUserData() {
        user = iDashboard.getWorkFreelancer();
      //  initRecyclerViewServices(user.getServices());
        //freelancer_name.setText(user.getFirstName() + " " + user.getLastName());
        util.setAvatar(user.getAvatar(),freelancer_avatar);
        String first_name = user.getFirstName();
        String last_name = user.getLastName();
        freelancer_name.setText(first_name.substring(0, 1).toUpperCase() + first_name.substring(1) + " " + last_name.substring(0, 1).toUpperCase() + last_name.substring(1));
        freelancer_about.setText(user.getAboutMe());
        freelancer_address.setText(user.getAddress().getWilaya() + ", " + user.getAddress().getCommune());

        //todo get user rating
        //todo get user reviews as a freelancer
        getUserAsArtisanReviews(user.getIdUser());

    }

    private void getUserAsArtisanReviews(int artisanId) {

        assignmentsFragmentViewModel.getUserAsArtisanReviews(artisanId).observe((LifecycleOwner)getActivity(), new Observer<ArrayList<Review>>() {
            @Override
            public void onChanged(ArrayList<Review> reviews) {
                reviewsList = reviews;

                freelancer_rating.setText(util.calculateAverageRating(reviews)+"/5");
                freelancer_completed_projects.setText(String.valueOf(reviewsList.size()));
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

    public void settingUpHooks() {
        freelancer_avatar = view.findViewById(R.id.freelancer_avatar);
        freelancer_name = view.findViewById(R.id.freelancer_name);
        freelancer_about = view.findViewById(R.id.freelancer_about);
        freelancer_address = view.findViewById(R.id.freelancer_address);
        freelancer_completed_projects = view.findViewById(R.id.freelancer_completed_projects);
        freelancer_rating = view.findViewById(R.id.freelancer_rating);
        hire_button = view.findViewById(R.id.hire_button);
        arrowBtn_services = view.findViewById(R.id.arrowBtn_services);
        cardView_services = view.findViewById(R.id.cardView_services);
        expandableView_user_services = view.findViewById(R.id.expandableView_user_services);
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

        switch (v.getId()) {

            case R.id.hire_button:
                // listener.onInputSelectLocationFromMapSent(addressSelected);
                if (!util.isInternetConnected()) {
                    //Show a dialog
                    util.ShowCustomDialog();
                }else{
                   // createWorkProposal();
                    loadingDialog.startLoadingDialog();
                    if(iDashboard.getWorkImageOne() == null && iDashboard.getWorkImageTwo() == null && iDashboard.getWorkImageThree() == null){
                        createWorkProposal(iDashboard.getWorkImageOne(),iDashboard.getWorkImageTwo(),iDashboard.getWorkImageThree());
                    }else{
                        uploadWorkImages(iDashboard.getWorkImageOne(),iDashboard.getWorkImageTwo(),iDashboard.getWorkImageThree());
                    }

                }
                break;

            case R.id.back_search:
                dismiss();
                break;
        }
    }

    private void uploadWorkImages(String imageOnePath, String imageTwoPath, String imageThreePath) {

        if(imageOnePath == null && imageTwoPath == null && imageThreePath == null){
            return;
        }
        //if(imageOnePath != null && imageTwoPath != null && imageThreePath != null){
        addFragmentViewModel.uploadWorkImages(
                prepareFilePart(imageOnePath, "ImageOne"),
                prepareFilePart(imageTwoPath, "ImageTwo"),
                prepareFilePart(imageThreePath, "ImageThree")).observe((LifecycleOwner)getActivity(), new Observer<UploadResponse>() {
            @Override
            public void onChanged(UploadResponse responseBody) {
               // myToast.showSuccess("Images uploaded successfully");
                ArrayList<String> list = util.jsonStringToArray(responseBody.getImagePath());
                switch (list.size()){
                    case 1:
                        createWorkProposal(list.get(0),"","");
                        break;
                    case 2:
                        createWorkProposal(list.get(0),list.get(1),"");
                        break;
                    case 3:
                        createWorkProposal(list.get(0),list.get(1),list.get(2));
                        break;
                }
            }
        });
        addFragmentViewModel.getToast().observe((LifecycleOwner)getActivity(), new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                loadingDialog.dismissDialog();
                //TODO Add something to notify the user that a problem has occurred
                myToast.showError(errorResponse.getErrorMessage());
            }
        });
        //}


    }


    @NonNull
    private MultipartBody.Part prepareFilePart(String path, String partName) {

        // create RequestBody instance from file
        if (path != null) {
            File file = new File(path);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            return MultipartBody.Part.createFormData(partName, file.getName(), requestBody);
        } else {
            return null;
        }

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sessionManager = new SessionManager(getActivity());
        myToast = new MyToast(getActivity());
        util = new Util(getActivity());
        iDashboard = (IDashboard) getActivity();
        loadingDialog = new LoadingDialog(getActivity());
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












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
import androidx.recyclerview.widget.RecyclerView;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.RedirectUtil;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.Application;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.User;
import com.example.servicebypro.Remote.Models.Work;
import com.example.servicebypro.SessionManager;
import com.example.servicebypro.ViewModels.AssignmentsFragmentViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;

import static com.example.servicebypro.Activities.HelperClasses.Constants.ERROR_DIALOG_REQUEST;
import static com.example.servicebypro.Activities.HelperClasses.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

public class AssignmentPendingFragment extends DialogFragment implements
        android.view.View.OnClickListener {
    private static final String TAG = "AssignmentPendingFragme";
    LinearLayout expandableView_work_details;
    Button arrowBtn, finish_work_button;
    CardView cardView, cardView_client, cardView_payment, cardView_freelancer, cardView_service, cardView_location, see_client_profile;
    private Util util;
    private RedirectUtil redirectUtil;
    private MyToast myToast;
    private View view;
    private IDashboard iDashboard;
    private RecyclerView recyclerView;
    private Dialog mDialog;
    ArrayList<Application> applicationList = new ArrayList<>();
    //    public Button no;
    SessionManager sessionManager;
    TextView work_title, work_service, work_description, work_client_name, work_address, work_due_date, work_price, work_type;
    Button apply_button, arrowBtn_client, arrowBtn_payment, arrowBtn_freelancer, arrowBtn_applications, arrowBtn_service, arrowBtn_location;
    LinearLayout work_price_layout, payment_method_layout, expandableView_work_applications, expandableView_work_service, expandableView_work_location, work_status_by_time_layout, expandableView_work_freelancer, expandableView_work_payment, expandableView_work_client;
    TextView option_digit, work_payment_method, work_freelancer_name, freelancer_name, work_category, work_status_by_time, work_address_in_map;
    ScrollView pending_scroll;
    Work work = new Work();
    ImageView client_avatar,work_image1,work_image2,work_image3;
    LinearLayout finish_work_layout;
    //  private boolean locationPermissionGranted = false;
    private AssignmentsFragmentViewModel assignmentsFragmentViewModel;


    private AssignmentPendingFragment.FragmentOpenFullImage listenerImageClick;
    public interface FragmentOpenFullImage {
        void onImageLinkSent(String imageLink);
    }


    public AssignmentPendingFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new Dialog(getActivity(), R.style.DialogFragment);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(mDialog.getWindow().getAttributes());
        //   layoutParams.windowAnimations = R.style.SlideUpDownDialog;

        final View view = View.inflate(getActivity(), R.layout.fragment_assignment_pending, null);
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
        view = inflater.inflate(R.layout.fragment_assignment_pending, container, false);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        assignmentsFragmentViewModel = new ViewModelProvider((ViewModelStoreOwner) getActivity()).get(AssignmentsFragmentViewModel.class);
        work = iDashboard.getSelectedWork();

        settingUpHooks();
        settingUpWorkData();


        //Setting listeners
        view.findViewById(R.id.back_search).setOnClickListener(this);
        view.findViewById(R.id.arrowBtn_client).setOnClickListener(this);
        view.findViewById(R.id.arrowBtn_payment).setOnClickListener(this);
        view.findViewById(R.id.arrowBtn).setOnClickListener(this);
        view.findViewById(R.id.arrowBtn_freelancer).setOnClickListener(this);
        view.findViewById(R.id.arrowBtn_service).setOnClickListener(this);
        view.findViewById(R.id.arrowBtn_location).setOnClickListener(this);
        view.findViewById(R.id.work_address_in_map).setOnClickListener(this);
        view.findViewById(R.id.see_client_profile).setOnClickListener(this);
        view.findViewById(R.id.see_freelancer_profile).setOnClickListener(this);
        view.findViewById(R.id.finish_work_button).setOnClickListener(this);
        view.findViewById(R.id.work_image1).setOnClickListener(this);
        view.findViewById(R.id.work_image2).setOnClickListener(this);
        view.findViewById(R.id.work_image3).setOnClickListener(this);

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


    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
           // myToast.showSuccess("Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occurred but we can resolve it
            Log.d(TAG, "isServicesOK: an error occurred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        return false;
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
                } else {
                }
                break;

            case R.id.finish_work_button:
                redirectUtil.initAddReviewUi();
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

        }
    }


    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //     locationPermissionGranted = true;
            redirectUtil.initDirectionsMapUI();
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
                    // locationPermissionGranted = true;
                    //initialise our map
                    redirectUtil.initDirectionsMapUI();
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
        myToast = new MyToast(getActivity());
        iDashboard = (IDashboard) getActivity();

        if (context instanceof AssignmentPendingFragment.FragmentOpenFullImage) {
            listenerImageClick = (AssignmentPendingFragment.FragmentOpenFullImage) context;
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
        option_digit = view.findViewById(R.id.option_digit);
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
        arrowBtn_applications = view.findViewById(R.id.arrowBtn_applications);
        expandableView_work_applications = view.findViewById(R.id.expandableView_work_applications);
        recyclerView = view.findViewById(R.id.applications_recycler);
        work_type = view.findViewById(R.id.work_type);
        cardView_service = view.findViewById(R.id.cardView_service);
        arrowBtn_service = view.findViewById(R.id.arrowBtn_service);
        expandableView_work_service = view.findViewById(R.id.expandableView_work_service);
        freelancer_name = view.findViewById(R.id.freelancer_name);

        work_status_by_time = view.findViewById(R.id.work_status_by_time);
        //todo
        pending_scroll = view.findViewById(R.id.pending_scroll);
        work_status_by_time_layout = view.findViewById(R.id.work_status_by_time_layout);

        work_image1 = view.findViewById(R.id.work_image1);
        work_image2 = view.findViewById(R.id.work_image2);
        work_image3 = view.findViewById(R.id.work_image3);

        cardView_location = view.findViewById(R.id.cardView_location);
        arrowBtn_location = view.findViewById(R.id.arrowBtn_location);
        expandableView_work_location = view.findViewById(R.id.expandableView_work_location);
        finish_work_layout = view.findViewById(R.id.finish_work_layout);
        client_avatar = view.findViewById(R.id.client_avatar);

    }


    public void settingUpWorkData() {


        work_title.setText(work.getTitle());
        work_service.setText(work.getService().getName());
        work_category.setText(work.getService().getCategorie().getName());
        work_description.setText(work.getDescription());

        util.setWorkImage(work.getFirstImage(), work_image1);
        util.setWorkImage(work.getSecondImage(), work_image2);
        util.setWorkImage(work.getThirdImage(), work_image3);

        if (work.getAddress().getWilaya().equals("") || work.getAddress().getCommune().equals("")) {
            work_address.setText(work.getAddress().getWilaya() + "" + work.getAddress().getCommune());
        } else {
            work_address.setText(work.getAddress().getWilaya() + ", " + work.getAddress().getCommune());
        }
        String formattedJoinDate = util.formationJoinDateTime(work.getDueDate());
        work_due_date.setText(formattedJoinDate);

        Date ldt = null;
        try {
            ldt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(work.getDueDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date currentTime = Calendar.getInstance().getTime();
        if (currentTime.after(ldt)) {
            //your_date_is_outdated = true;
            work_status_by_time.setText("OUTDATED");
            work_status_by_time.setTextColor(ContextCompat.getColor(getContext(), R.color.my_red));
            work_status_by_time_layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.my_light_red));
        } else {
            // your_date_is_outdated = false;
            work_status_by_time.setText("PENDING");
            work_status_by_time.setTextColor(ContextCompat.getColor(getContext(), R.color.myblue));
            work_status_by_time_layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.my_light_blue));
        }

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

                work_type.setText("Your " + work.getType() + " details");
                if (work.getType().equals("proposal")) {
                    String freelancerFirstName = work.getApplications().get(0).getUser().getFirstName();
                    String freelancerLastName = work.getApplications().get(0).getUser().getLastName();
                    work_freelancer_name.setText(freelancerFirstName.substring(0, 1).toUpperCase() + freelancerFirstName.substring(1) + " " + freelancerLastName.substring(0, 1).toUpperCase() + freelancerLastName.substring(1));
                    freelancer_name.setText("Work for " + freelancerFirstName.substring(0, 1).toUpperCase() + freelancerFirstName.substring(1) + " " + freelancerLastName.substring(0, 1).toUpperCase() + ".");
                } else {
                    //if post
                    //todo get freelancer selected name
                    //todo show the list of all applications
                    User user = util.getAcceptedApplication(work).getUser();
                    String freelancerFirstName = user.getFirstName();
                    String freelancerLastName = user.getLastName();
                    work_freelancer_name.setText(freelancerFirstName.substring(0, 1).toUpperCase() + freelancerFirstName.substring(1) + " " + freelancerLastName.substring(0, 1).toUpperCase() + freelancerLastName.substring(1));
                    freelancer_name.setText("Work for " + freelancerFirstName.substring(0, 1).toUpperCase() + freelancerFirstName.substring(1) + " " + freelancerLastName.substring(0, 1).toUpperCase() + ".");

                }


                finish_work_layout.setVisibility(View.VISIBLE);


               option_digit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Display Option menu
                        PopupMenu menu = new PopupMenu(getContext(), option_digit);
                        menu.getMenu().add(1, 1, 1, "Cancel");

                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case 1:
                                        //todo user can cancel work only if due date is not outdated
                                       // myToast.showInfo("Cancel");

                                        if (!util.isInternetConnected()) {
                                            //Show a dialog
                                            util.ShowCustomDialog();
                                        }else{
                                            cancelWork(work.getIdWork());
                                        }
                                        break;
                                }
                                return false;
                            }
                        });
                        menu.show();
                    }
                });
                break;
            case "artisan":
                cardView_client.setVisibility(View.VISIBLE);
                cardView_freelancer.setVisibility(View.GONE);
                util.setAvatar(work.getUser().getAvatar(),client_avatar);
                option_digit.setVisibility(View.GONE);
                String clientFirstName = work.getUser().getFirstName();
                String clientLastName = work.getUser().getLastName();
                work_client_name.setText(clientFirstName.substring(0, 1).toUpperCase() + clientFirstName.substring(1) + " " + clientLastName.substring(0, 1).toUpperCase() + clientLastName.substring(1));

                if (work.getType().equals("proposal")) {
                    work_type.setText("work proposed to you");
                } else {
                    //if post
                    work_type.setText("You applied for this work");
                    //todo get freelancer selected name
                }

                if (work.getStatus().equals("pending")) {
                    finish_work_layout.setVisibility(View.GONE);
                } else {
                    finish_work_layout.setVisibility(View.VISIBLE);
                }

                break;
        }

    }

    public  void cancelWork(int workId){

        assignmentsFragmentViewModel.cancelWork(workId).observe((LifecycleOwner) getActivity(), new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                myToast.showSuccess("You canceled this work!");
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
}
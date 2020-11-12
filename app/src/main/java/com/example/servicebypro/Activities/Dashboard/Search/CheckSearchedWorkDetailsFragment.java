package com.example.servicebypro.Activities.Dashboard.Search;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servicebypro.Activities.Adapters.BrowseWorkAdapter;
import com.example.servicebypro.Activities.Dashboard.Assignments.AssignmentRequestFragment;
import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.RedirectUtil;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.Application;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.Work;
import com.example.servicebypro.SessionManager;
import com.example.servicebypro.ViewModels.AddFragmentViewModel;
import com.example.servicebypro.ViewModels.SearchFragmentViewModel;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.util.ArrayList;

import okhttp3.ResponseBody;

public class CheckSearchedWorkDetailsFragment extends DialogFragment implements
        android.view.View.OnClickListener {

    private Util util;
    private MyToast myToast;
    private View view;
    private IDashboard iDashboard;
    RecyclerView work_list_recycler;
    private RedirectUtil redirectUtil;
    AddFragmentViewModel addFragmentViewModel;
    SearchFragmentViewModel searchFragmentViewModel;
    private Dialog mDialog;
    public Button no;
    Work work = new Work();
    ArrayList<Work> workList = new ArrayList<>();
    SessionManager sessionManager;
    BrowseWorkAdapter browseWorkAdapter;
    ImageView back_search;
    RelativeLayout no_work_layout;
    LinearLayout price_layout, work_images_title_layout;
    TextView work_title, work_service, work_description, work_client_name, work_address, work_due_date,
            work_payment_type, work_price;
    Button apply_button;
    ImageView work_image1, work_image2, work_image3;

    public CheckSearchedWorkDetailsFragment() {
        // Required empty public constructor
    }

    private AssignmentRequestFragment.FragmentOpenFullImage listenerImageClick;

    public interface FragmentOpenFullImage {
        void onImageLinkSent(String imageLink);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new Dialog(getActivity(), R.style.DialogFragment);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(mDialog.getWindow().getAttributes());
        //   layoutParams.windowAnimations = R.style.SlideUpDownDialog;

        final View view = View.inflate(getActivity(), R.layout.fragment_check_searched_work_details, null);
        mDialog.getWindow().setAttributes(layoutParams);
        mDialog.setContentView(view);

        return mDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(getActivity());
        util = new Util(getActivity());
        myToast = new MyToast(getActivity());
        redirectUtil = new RedirectUtil(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_check_searched_work_details, container, false);
        addFragmentViewModel = new ViewModelProvider((ViewModelStoreOwner) this.getActivity()).get(AddFragmentViewModel.class);
        searchFragmentViewModel = new ViewModelProvider((ViewModelStoreOwner) this.getActivity()).get(SearchFragmentViewModel.class);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        work = iDashboard.getSelectedWork();

        //hooks
        apply_button = view.findViewById(R.id.apply_button);
        //Setting listeners
        view.findViewById(R.id.back_search).setOnClickListener(this);
        view.findViewById(R.id.work_image1).setOnClickListener(this);
        view.findViewById(R.id.work_image2).setOnClickListener(this);
        view.findViewById(R.id.work_image3).setOnClickListener(this);

        if (iDashboard.hasApplied()) {
            apply_button.setClickable(false);
            apply_button.setText("You have already applied");
            apply_button.setTextColor(ContextCompat.getColor(getContext(), R.color.myblue));
            apply_button.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.rectangle_bg_blue_bg_white));
        } else {
            view.findViewById(R.id.apply_button).setOnClickListener(this);
        }


        settingUpHooks();
        settingUpWorkData();
        mDialog.setCanceledOnTouchOutside(true);
        return view;

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

            case R.id.back_search:
                dismiss();
                break;

            case R.id.apply_button:
                //todo create job application
                if (!util.isInternetConnected()) {
                    //Show a dialog
                    util.ShowCustomDialog();
                } else {
                    createApplication();
                }
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
        }
    }

    private void createApplication() {
        Application application = new Application(sessionManager.getUser(), iDashboard.getSelectedWork(), "request");
        searchFragmentViewModel.createApplication(application).observe((LifecycleOwner) this.getActivity(), new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                myToast.showSuccess("Work Application created successfully");
                //todo redirect to assignment frag
                iDashboard.inflateFragment(getString(R.string.fragment_assignment));
                //dismiss();
                util.dismissAllDialogs(getFragmentManager());
            }
        });
        searchFragmentViewModel.getToastService().observe((LifecycleOwner) this.getActivity(), new Observer<ErrorResponse>() {
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

        if (context instanceof AssignmentRequestFragment.FragmentOpenFullImage) {
            listenerImageClick = (AssignmentRequestFragment.FragmentOpenFullImage) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement listenerImageClick");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void settingUpHooks() {
        work_title = view.findViewById(R.id.work_title);
        work_service = view.findViewById(R.id.work_service);
        work_description = view.findViewById(R.id.work_description);
        work_client_name = view.findViewById(R.id.work_client_name);
        work_address = view.findViewById(R.id.work_address);
        work_due_date = view.findViewById(R.id.work_due_date);
        work_payment_type = view.findViewById(R.id.work_payment_type);
        work_price = view.findViewById(R.id.work_price);
        price_layout = view.findViewById(R.id.price_layout);
        work_image1 = view.findViewById(R.id.work_image1);
        work_image2 = view.findViewById(R.id.work_image2);
        work_image3 = view.findViewById(R.id.work_image3);
        work_images_title_layout = view.findViewById(R.id.work_images_title_layout);

    }

    public void settingUpWorkData() {

        work_title.setText(work.getTitle());
        work_service.setText(work.getService().getName());
        work_description.setText(work.getDescription());
        work_client_name.setText(work.getUser().getFirstName() + " " + work.getUser().getLastName());
        work_address.setText(work.getAddress().getWilaya() + ", " + work.getAddress().getCommune());
        String formattedJoinDate = util.formationJoinDateTime(work.getDueDate());
        work_due_date.setText(formattedJoinDate);
        if (work.getPaymentMethod().equals("not_precise")) {
            work_payment_type.setText("Not precised");
        } else {
            work_payment_type.setText(work.getPaymentMethod());
        }

        if (!work.getPaymentMethod().equals("not_precise")) {
            price_layout.setVisibility(View.VISIBLE);
            work_price.setText("$ " + work.getMinPrice() + " - " + work.getMaxPrice() + "/Hour");
        } else {
            price_layout.setVisibility(View.GONE);
        }

        if (work.getFirstImage() == null && work.getSecondImage() == null && work.getThirdImage() == null ) {
            //work.getFirstImage().equals("")
            work_images_title_layout.setVisibility(View.GONE);
            work_image1.setVisibility(View.GONE);
            work_image2.setVisibility(View.GONE);
            work_image3.setVisibility(View.GONE);
        }else{
            util.setWorkImage(work.getFirstImage(), work_image1);
            util.setWorkImage(work.getSecondImage(), work_image2);
            util.setWorkImage(work.getThirdImage(), work_image3);
        }


    }


}

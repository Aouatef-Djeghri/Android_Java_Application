package com.example.servicebypro.Activities.Dashboard.Add;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.LoadingDialog;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.Service;
import com.example.servicebypro.Remote.Models.UploadResponse;
import com.example.servicebypro.Remote.Models.Work;
import com.example.servicebypro.SessionManager;
import com.example.servicebypro.ViewModels.AddFragmentViewModel;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;


public class Post8SelectTypeFragment extends Fragment implements View.OnClickListener {
    private MyToast myToast;
    private Util util;
    private View view;
    IDashboard iDashboard;
    protected int current_step = 8;
    private SessionManager sessionManager;
    private Post8SelectTypeFragment.FragmentSelectTypeListener listener;
    AddFragmentViewModel addFragmentViewModel;
    LoadingDialog loadingDialog;
    public interface FragmentSelectTypeListener {
        void onInputSelectTypeSent(String type);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post_select_type, container, false);
        iDashboard.getButtonNext().setVisibility(View.GONE);
        addFragmentViewModel = new ViewModelProvider(this).get(AddFragmentViewModel.class);
        sessionManager = new SessionManager(getContext());
        if (getActivity().getIntent().hasExtra("service")) {
            Service service = getActivity().getIntent().getParcelableExtra("service");
            // myToast.showSuccess("" + service.getName());
        }

        //Setting listeners
        view.findViewById(R.id.quick_search).setOnClickListener(this);
        view.findViewById(R.id.post_project).setOnClickListener(this);

        return view;
    }


    public Post8SelectTypeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Utilities class contains reusable methods
        util = new Util(getContext());
        myToast = new MyToast(getContext());
        loadingDialog = new LoadingDialog(getActivity());
        //  iPost.setToastToTest("this is the final toast type");
        iDashboard.setAddPostTitleText("Type", iDashboard.getTextView());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.quick_search:
                listener.onInputSelectTypeSent("proposal");
                //todo redirect to map ot list of available freelancer
                iDashboard.nextStep(iDashboard.getCurrent_step());
                iDashboard.inflateFragment(getString(R.string.fragment_freelancer));
                break;
            case R.id.post_project:
                listener.onInputSelectTypeSent("post");
                if (!util.isInternetConnected()) {
                    //Show a dialog
                    util.ShowCustomDialog();
                }else{
                    loadingDialog.startLoadingDialog();
                    if(iDashboard.getWorkImageOne() == null && iDashboard.getWorkImageTwo() == null && iDashboard.getWorkImageThree() == null){
                        createWorkPost(iDashboard.getWorkImageOne(),iDashboard.getWorkImageTwo(),iDashboard.getWorkImageThree());
                    }else{
                        uploadWorkImages(iDashboard.getWorkImageOne(),iDashboard.getWorkImageTwo(),iDashboard.getWorkImageThree());
                    }
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createWorkPost(String imageOne , String imageTwo ,String imageThree) {

        String input = iDashboard.getWorkDate().replace(" ", "T");
        LocalDateTime ldt = LocalDateTime.parse(input);

        Work work = new Work(iDashboard.getWorkAddress(), iDashboard.getWorkService(), sessionManager.getUser(), iDashboard.getWorkTitle(),
                iDashboard.getWorkDescription(), ldt.toString(),imageOne, imageTwo, imageThree, "request",
                iDashboard.getWorkType(), iDashboard.getWorkPaymentType(), iDashboard.getWorkMinPrice(), iDashboard.getWorkMaxPrice());

        addFragmentViewModel.createWorkPost(work).observe(this, new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                loadingDialog.dismissDialog();
                myToast.showSuccess("Work Post created successfully");
                //todo redirect to assignment frag
                iDashboard.inflateFragment(getString(R.string.fragment_assignment));
            }
        });
        addFragmentViewModel.getToastWork().observe(this, new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                //TODO Add something to notify the user that a problem has occurred
                loadingDialog.dismissDialog();
                myToast.showError(errorResponse.getErrorMessage());
            }
        });
    }


    private void uploadWorkImages(String imageOnePath, String imageTwoPath, String imageThreePath) {

        if(imageOnePath == null && imageTwoPath == null && imageThreePath == null){
            return;
        }
        //if(imageOnePath != null && imageTwoPath != null && imageThreePath != null){
        addFragmentViewModel.uploadWorkImages(
                util.prepareFilePart(imageOnePath, "ImageOne"),
                util.prepareFilePart(imageTwoPath, "ImageTwo"),
                util.prepareFilePart(imageThreePath, "ImageThree")
        ).observe(this, new Observer<UploadResponse>() {
            @Override
            public void onChanged(UploadResponse responseBody) {
               // myToast.showSuccess("Images uploaded successfully");
                ArrayList<String> list = util.jsonStringToArray(responseBody.getImagePath());
                switch (list.size()){
                    case 1:
                        createWorkPost(list.get(0),"","");
                        break;
                    case 2:
                        createWorkPost(list.get(0),list.get(1),"");
                        break;
                    case 3:
                        createWorkPost(list.get(0),list.get(1),list.get(2));
                        break;
                }
            }
        });
        addFragmentViewModel.getToast().observe(this, new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                loadingDialog.dismissDialog();
                //TODO Add something to notify the user that a problem has occurred
                myToast.showError(errorResponse.getErrorMessage());
            }
        });
        //}
    }





    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iDashboard = (IDashboard) getActivity();
        iDashboard.setCurrent_step(current_step);
        if (context instanceof Post8SelectTypeFragment.FragmentSelectTypeListener) {
            listener = (Post8SelectTypeFragment.FragmentSelectTypeListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentAListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
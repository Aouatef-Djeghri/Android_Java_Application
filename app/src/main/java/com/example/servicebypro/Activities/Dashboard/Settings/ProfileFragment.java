package com.example.servicebypro.Activities.Dashboard.Settings;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.loader.content.CursorLoader;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.RedirectUtil;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.UploadResponse;
import com.example.servicebypro.Remote.Models.User;
import com.example.servicebypro.SessionManager;
import com.example.servicebypro.ViewModels.RegisterViewModel;
import com.example.servicebypro.ViewModels.SettingsFragmentViewModel;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import static android.app.Activity.RESULT_OK;
import static com.example.servicebypro.Activities.Common.SignUp.SignUpFragment.PERMISSIONS_STORAGE;
import static com.example.servicebypro.Activities.HelperClasses.Constants.REQUEST_EXTERNAL_STORAGE;

public class ProfileFragment extends Fragment implements
        android.view.View.OnClickListener {
    private SessionManager sessionManager;
    private MyToast myToast;
    private Util util;

    private RedirectUtil redirectUtil;

    TextView first_name, last_name;
    User userData = new User();
    ImageView back_search;
    View view;
    TextView about, phone, email,
            address;
    ImageView avatar, change_avatar;
    TextView join_date;
    SettingsFragmentViewModel settingsFragmentViewModel;
    RegisterViewModel registerViewModel;

    private IDashboard iDashboard;

    public ProfileFragment() {
        // Required empty public constructor
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
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        settingsFragmentViewModel = new ViewModelProvider((ViewModelStoreOwner) getActivity()).get(SettingsFragmentViewModel.class);
        registerViewModel = new ViewModelProvider((ViewModelStoreOwner) getActivity()).get(RegisterViewModel.class);
        view.findViewById(R.id.edit_button).setOnClickListener(this);
        view.findViewById(R.id.back_search).setOnClickListener(this);
        view.findViewById(R.id.change_avatar).setOnClickListener(this);

        sessionManager = new SessionManager(getContext());
        userData = sessionManager.getUser();

        //Hooks
        settingUpHooks();

        //Setting text from session
        String firstName = userData.getFirstName();
        first_name.setText(firstName.substring(0, 1).toUpperCase() + firstName.substring(1));

        String lastName = userData.getLastName();
        last_name.setText(lastName.substring(0, 1).toUpperCase() + lastName.substring(1));


        preFillingEditTextsWithUserData();

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_button:
                redirectUtil.initEditProfileDialog();
                break;
            case R.id.back_search:
                getActivity().onBackPressed();
                break;
            case R.id.change_avatar:
                verifyStoragePermissions(getActivity());
                break;
        }
    }


    private void settingUpHooks() {
        first_name = view.findViewById(R.id.first_name);
        last_name = view.findViewById(R.id.last_name);
        back_search = view.findViewById(R.id.back_search);
        avatar = view.findViewById(R.id.avatar);
        about = view.findViewById(R.id.about);
        phone = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);
        address = view.findViewById(R.id.address);
        join_date = view.findViewById(R.id.join_date);
    }

    private void preFillingEditTextsWithUserData() {
        //setting text from session
        about.setText(userData.getAboutMe());
        phone.setText(userData.getPhone());
        email.setText(userData.getEmail());
        address.setText(userData.getAddress().getCommune() + "," + userData.getAddress().getWilaya());
        String formattedJoinDate = util.formationJoinDate(userData.getJoinDate());
        join_date.setText(formattedJoinDate);

        String avatarLink = userData.getAvatar();
        util.setAvatar(avatarLink, avatar);

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iDashboard = (IDashboard) getActivity();
        iDashboard.setCurrentFragTag(getString(R.string.fragment_profile));
        sessionManager = new SessionManager(getContext());
        userData = sessionManager.getUser();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    String imagePath = null;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            imagePath = util.getRealPathFromUri(path, getActivity());
            uploadIdImage(imagePath, String.valueOf(sessionManager.getUser().getIdUser()));
            util.setAvatar(imagePath, avatar);
        }
    }

    public void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            myToast.showError("U need permission");

        } else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 0);
        }
    }


    private void uploadIdImage(String imagePath, String imageName) {

        File file = new File(imagePath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

        registerViewModel.uploadIdImage(body, "avatar", imageName).observe((LifecycleOwner) getActivity(), new Observer<UploadResponse>() {
            @Override
            public void onChanged(UploadResponse responseBody) {
                String imageName = responseBody.getImagePath();
                User user = sessionManager.getUser();
                user.setAvatar(imageName);
                updateUser(user);
            }
        });
        registerViewModel.getToastImage().observe((LifecycleOwner) getActivity(), new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                //TODO Add something to notify the user that a problem has occurred
                myToast.showError(errorResponse.getErrorMessage());
            }
        });
    }


    public void updateUser(User user) {

        settingsFragmentViewModel.updateUser(user).observe((LifecycleOwner) getActivity(), new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                myToast.showSuccess("Avatar updated successfully");
                sessionManager.updateUserData(user);
            }
        });
        settingsFragmentViewModel.getUserRepoToast().observe((LifecycleOwner) getActivity(), new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                //TODO Add something to notify the user that a problem has occurred
                myToast.showError(errorResponse.getErrorMessage());
            }
        });
    }
}
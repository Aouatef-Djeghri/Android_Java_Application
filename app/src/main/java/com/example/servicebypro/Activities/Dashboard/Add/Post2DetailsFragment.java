package com.example.servicebypro.Activities.Dashboard.Add;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.content.CursorLoader;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.UploadResponse;
import com.example.servicebypro.ViewModels.AddFragmentViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static com.example.servicebypro.Activities.Common.SignUp.SignUpFragment.PERMISSIONS_STORAGE;
import static com.example.servicebypro.Activities.HelperClasses.Constants.ERROR_DIALOG_REQUEST;
import static com.example.servicebypro.Activities.HelperClasses.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.servicebypro.Activities.HelperClasses.Constants.REQUEST_EXTERNAL_STORAGE;

public class Post2DetailsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "Post2DetailsFragment";
    AddFragmentViewModel addFragmentViewModel;
    private MyToast myToast;
    private Util util;
    private View view;
    protected int current_step = 2;
    IDashboard iDashboard;
    String imagePath = null;
    String imageOnePath, imageTwoPath, imageThreePath = null;
    String imageOne, imageTwo,imageThree = "";
    HashMap<String, String> uploadedImages = new HashMap<String, String>();
    TextInputLayout workTitle, workDescription;
    TextView workTitleCharNumber, workDescriptionCharNumber, first_image_path, second_image_path, third_image_path;
    LinearLayout details_layout, select_file_button, display_first_selected_file, display_second_selected_file,
            display_third_selected_file, separation_line_files_one, separation_line_files_two, separation_line_files_three;
    ImageView delete_image_one, delete_image_two, delete_image_three;
    private boolean locationPermissionGranted = false;
    private Post2DetailsFragment.FragmentSetDetailsListener listener;

    public interface FragmentSetDetailsListener {
        void onInputDetailsSent(String title, String description, String imageOne, String imageTwo, String imageThree);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post_details, container, false);
        iDashboard.getButtonNext().setVisibility(View.VISIBLE);
        addFragmentViewModel = new ViewModelProvider(this).get(AddFragmentViewModel.class);

        //Setting listeners add_file_button
        iDashboard.getButtonNext().setOnClickListener(this);

        view.findViewById(R.id.select_file_button).setOnClickListener(this);
        view.findViewById(R.id.delete_image_one).setOnClickListener(this);
        view.findViewById(R.id.delete_image_two).setOnClickListener(this);
        view.findViewById(R.id.delete_image_three).setOnClickListener(this);

        //hooks
        details_layout = view.findViewById(R.id.details_layout);
        workTitle = view.findViewById(R.id.work_title);
        workDescription = view.findViewById(R.id.work_description);
        workDescriptionCharNumber = view.findViewById(R.id.work_description_char_number);
        workTitleCharNumber = view.findViewById(R.id.work_title_char_number);
        //
        select_file_button = view.findViewById(R.id.select_file_button);
        display_first_selected_file = view.findViewById(R.id.display_first_selected_file);
        display_second_selected_file = view.findViewById(R.id.display_second_selected_file);
        display_third_selected_file = view.findViewById(R.id.display_third_selected_file);
        //
        separation_line_files_one = view.findViewById(R.id.separation_line_files_one);
        separation_line_files_two = view.findViewById(R.id.separation_line_files_two);
        separation_line_files_three = view.findViewById(R.id.separation_line_files_three);
        //,,
        first_image_path = view.findViewById(R.id.first_image_path);
        second_image_path = view.findViewById(R.id.second_image_path);
        third_image_path = view.findViewById(R.id.third_image_path);


        workTitle.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int length = workTitle.getEditText().length();
                String convert = String.valueOf(length + 1);
                workTitleCharNumber.setText(convert + "/50");
                util.validateWorkTitleCharacterNumber(workTitle,
                        workTitleCharNumber, 50, 10, length + 1);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        workDescription.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int length = workDescription.getEditText().length();
                String convert = String.valueOf(length + 1);
                workDescriptionCharNumber.setText(convert + "/500");
                util.validateWorkTitleCharacterNumber(workDescription,
                        workDescriptionCharNumber, 500, 10, length + 1);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    //    preFillingEditTextIfDataExists();
        return view;
    }

    public Post2DetailsFragment() {
        // Required empty public constructor
    }

    private void hideSoftKeyboard() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        util = new Util(getContext());
        myToast = new MyToast(getContext());

        // iPost.setToastToTest("this is the final toast details");
        iDashboard.setAddPostTitleText("Details", iDashboard.getTextView());
        hideSoftKeyboard();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iDashboard = (IDashboard) getActivity();
        iDashboard.setCurrent_step(current_step);

        if (context instanceof Post1SelectServiceFragment.FragmentSelectServiceListener) {
            listener = (Post2DetailsFragment.FragmentSetDetailsListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentAListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.add_post_next_btn:
                if (isServicesOK()) {
                    getLocationPermission();
                    if(locationPermissionGranted){
                        // myToast.showSuccess("this is clicked");
                        submitDetails();
                    }else{
                        myToast.showWarning("we need location permissions in order to select a work location");
                        boolean showRationale = shouldShowRequestPermissionRationale( Manifest.permission.ACCESS_FINE_LOCATION );
                        if (! showRationale) {
                            // user also CHECKED "never ask again"
                            // you can either enable some fall back,
                            // disable features of your app
                            // or open another dialog explaining
                            // again the permission and directing to
                            // the app setting
                            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // show a dialog
                                new AlertDialog.Builder(getContext()).setMessage("You need to enable permissions to use this feature").setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // navigate to settings
                                        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                                    }
                                }).setNegativeButton("Go back", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // leave?
                                        // getActivity().onBackPressed();
                                    }
                                }).show();
                            }
                        }
                    }
                } else {
                    myToast.showWarning("You need to activate google services in order to select a work location");

                }
                break;

            case R.id.select_file_button:
                if (uploadedImages.size() == 3) {
                    myToast.showWarning("can't upload more than 3 images");
                    return;
                }
                verifyStoragePermissions(getActivity());
                break;

            case R.id.delete_image_one:
                if (uploadedImages.get("image01") != null) {
                    uploadedImages.remove("image01");
                    imageOnePath = null;
                    imageOne= "";
                    display_first_selected_file.setVisibility(View.GONE);
                    if (uploadedImages.size() == 0) {
                        separation_line_files_one.setVisibility(View.GONE);
                    }
                    //myToast.showWarning("removed first image size of map " + uploadedImages.size() + "");
                } else {
                    //myToast.showWarning("nothing to remove");
                }
                break;

            case R.id.delete_image_two:
                if (uploadedImages.get("image02") != null) {
                    uploadedImages.remove("image02");
                    imageTwoPath = null;
                    imageTwo= "";
                    display_second_selected_file.setVisibility(View.GONE);
                    separation_line_files_two.setVisibility(View.GONE);
                    if (uploadedImages.size() == 0) {
                        separation_line_files_one.setVisibility(View.GONE);
                    }
                   // myToast.showWarning("removed second image size of map " + uploadedImages.size() + "");
                } else {
                    //myToast.showWarning("nothing to remove");
                }
                break;

            case R.id.delete_image_three:
                if (uploadedImages.get("image03") != null) {
                    uploadedImages.remove("image03");
                    imageThreePath = null;
                    imageThree = "";
                    display_third_selected_file.setVisibility(View.GONE);
                    separation_line_files_three.setVisibility(View.GONE);
                    if (uploadedImages.size() == 0) {
                        separation_line_files_one.setVisibility(View.GONE);
                    }
                    //myToast.showWarning("removed third image size of map " + uploadedImages.size() + "");
                } else {
                   // myToast.showWarning("nothing to remove");
                }
                break;


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
        } else {
           // myToast.showError("You can't make map requests");
        }
        return false;
    }


    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
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
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                     locationPermissionGranted = true;
                } else {
                    myToast.showSuccess("Sorry, you can't open directions without enabling location permissions");
                }
            }
            break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            imagePath = util.getRealPathFromUri(path,getActivity());
            String[] parts = imagePath.split("/");
            // myToast.showWarning(imagePath);

            if (uploadedImages.size() < 3) {

                if (uploadedImages.get("image01") == null) {
                    ImageView myImage = (ImageView) view.findViewById(R.id.image_one);
                    Glide.with(getContext()).load(imagePath).optionalFitCenter().into(myImage);
                    uploadedImages.put("image01", imagePath);
                    // myToast.showSuccess(uploadedImages.size()+"             1");
                    display_first_selected_file.setVisibility(View.VISIBLE);
                    separation_line_files_one.setVisibility(View.VISIBLE);
                    first_image_path.setText(parts[parts.length - 1]);
                    imageOnePath = imagePath;
                    imageOne = parts[parts.length - 1];
                } else if (uploadedImages.get("image02") == null) {
                    ImageView myImage = (ImageView) view.findViewById(R.id.image_two);
                    Glide.with(getContext()).load(imagePath).optionalFitCenter().into(myImage);
                    uploadedImages.put("image02", imagePath);
                    // myToast.showSuccess(uploadedImages.size()+"             2");
                    display_second_selected_file.setVisibility(View.VISIBLE);
                    separation_line_files_two.setVisibility(View.VISIBLE);
                    second_image_path.setText(parts[parts.length - 1]);
                    imageTwoPath = imagePath;
                    imageTwo = parts[parts.length - 1];
                } else if (uploadedImages.get("image03") == null) {
                    ImageView myImage = (ImageView) view.findViewById(R.id.image_three);
                    Glide.with(getContext()).load(imagePath).optionalFitCenter().into(myImage);
                    uploadedImages.put("image03", imagePath);
                    //  myToast.showSuccess(uploadedImages.size()+"               3");
                    display_third_selected_file.setVisibility(View.VISIBLE);
                    separation_line_files_three.setVisibility(View.VISIBLE);
                    third_image_path.setText(parts[parts.length - 1]);
                    imageThreePath = imagePath;
                    imageThree = parts[parts.length - 1];
                }
            }
        } else {

            myToast.showWarning("unable to choose image");
        }
    }



    public void submitDetails() {
        //uploadWorkImages(imageOnePath, imageTwoPath, imageThreePath);
        int titleLength = workTitle.getEditText().length();
        int descriptionLength = workDescription.getEditText().length();

        if (!util.validateWorkTitleCharacterNumber(workDescription,
                workDescriptionCharNumber, 500, 10, descriptionLength + 1) |

                !util.validateWorkTitleCharacterNumber(workTitle,
                        workTitleCharNumber, 50, 10, titleLength + 1)) {
            return;
        } else {
            String title = workTitle.getEditText().getText().toString();
            String description = workDescription.getEditText().getText().toString();
            iDashboard.nextStep(iDashboard.getCurrent_step());
            iDashboard.inflateFragment(getString(R.string.fragment_location));
            listener.onInputDetailsSent(title, description, imageOnePath,imageTwoPath,imageThreePath);
        }
    }

}
package com.example.servicebypro.Activities.Common.SignUp;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.loader.content.CursorLoader;

import android.provider.MediaStore;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.servicebypro.Activities.Common.IStartUpScreen;
import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.LoadingDialog;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.RedirectUtil;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.Address;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.UploadResponse;
import com.example.servicebypro.Remote.Models.User;
import com.example.servicebypro.SessionManager;
import com.example.servicebypro.ViewModels.RegisterViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import static android.app.Activity.RESULT_OK;
import static com.example.servicebypro.Activities.HelperClasses.Constants.REQUEST_EXTERNAL_STORAGE;

public class SignUpFragment extends DialogFragment implements
        android.view.View.OnClickListener {

    private static final String TAG = "SignUpFragment";
    private View view;
    private MyToast myToast;
    private Util util;
    private Dialog mDialog;
    private TextInputLayout signup_first_name, signup_last_name, signup_email, signup_password, signup_phone;
    private String wilaya = "";
    private String commune = "";
    private float latitude = 0;
    private float longitude = 0;
    IStartUpScreen iStartUpScreen;
    RegisterViewModel registerViewModel;
    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    String photoPath = null;
    String imagePath = null;
    User user = new User();
    Spinner sp_wilaya, sp_commune;

    ArrayList<String> arrayList_wilaya;
    ArrayAdapter<String> arrayAdapter_wilaya;
    ArrayList<String> arrayList_commune;
    ArrayAdapter<String> arrayAdapter_commune;

    RelativeLayout sign_up_layout;
    RedirectUtil redirectUtil;
    LoadingDialog loadingDialog;
    String firstName,lastName,email, password,phoneNo = "";
    Address address = new Address();
    public SignUpFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new Dialog(getActivity(), R.style.DialogFragment);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(mDialog.getWindow().getAttributes());
        //   layoutParams.windowAnimations = R.style.SlideUpDownDialog;


        final View view = View.inflate(getActivity(), R.layout.fragment_assignment_finished, null);
        mDialog.getWindow().setAttributes(layoutParams);
        mDialog.setContentView(view);

        return mDialog;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void hideSoftKeyboard() {
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        hideSoftKeyboard();
        redirectUtil = new RedirectUtil(getActivity());
        registerViewModel = new ViewModelProvider((ViewModelStoreOwner) getActivity()).get(RegisterViewModel.class);
        //Setting listeners
        settingUpListeners();
        //Setting hooks
        settingUpHooks();


        //setting up wilayas
        String[] wilaya = getContext().getResources().getStringArray(R.array.wilaya);
        arrayList_wilaya = new ArrayList<>(Arrays.asList(wilaya));
        arrayAdapter_wilaya = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, arrayList_wilaya);
        sp_wilaya.setSelection(0);
        sp_wilaya.setAdapter(arrayAdapter_wilaya);

        //initialising commun spinner with empty list
        emptyCommunList();

        sp_wilaya.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
/*                if (adapterView.getItemAtPosition(position).equals("Select Commune"))
                {
                    //do nothing.
                }
                else
                {
                    // write code on what you want to do with the item selection
                }*/
               // myToast.showInfo(util.getNameFromString(sp_wilaya.getSelectedItem().toString()));
                setWilaya(util.getNameFromString(sp_wilaya.getSelectedItem().toString()));
                setCommune("");
                if (position > 0) {
                    sp_commune.setEnabled(true);
                    sp_commune.setClickable(true);
                    // get spinner value
                    switch (position) {
                        case 1:
                            latitude = (float) 26.488816;
                            longitude = (float)-1.358244 ;
                            String[] communes_1 = getContext().getResources().getStringArray(R.array.communes_1);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_1));
                            break;
                        case 2:
                            latitude = (float)36.20342 ;
                            longitude =(float) 1.26807 ;
                            String[] communes_2 = getContext().getResources().getStringArray(R.array.communes_2);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_2));
                            break;
                        case 3:
                            latitude =  (float)33.750441 ;
                            longitude = (float)2.643109 ;
                            String[] communes_3 = getContext().getResources().getStringArray(R.array.communes_3);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_3));
                            break;
                        case 4:
                            latitude = (float)35.810581 ;
                            longitude = (float)7.018418 ;
                            String[] communes_4 = getContext().getResources().getStringArray(R.array.communes_4);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_4));
                            break;
                        case 5:
                            latitude = (float)(float)(float)35.338429 ;
                            longitude = (float)(float)(float)5.731545 ;
                            String[] communes_5 = getContext().getResources().getStringArray(R.array.communes_5);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_5));
                            break;
                        case 6:
                            latitude = (float)(float)(float)36.751178  ;
                            longitude =(float)(float)(float) 5.064369 ;
                            String[] communes_6 = getContext().getResources().getStringArray(R.array.communes_6);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_6));
                            break;
                        case 7:
                            latitude =(float)(float)(float) 34.784564 ;
                            longitude =  (float)(float)(float)5.812435 ;
                            String[] communes_7 = getContext().getResources().getStringArray(R.array.communes_7);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_7));
                            break;
                        case 8:
                            latitude = (float)(float)(float)31.385726 ;
                            longitude = (float)(float)(float)-2.011596 ;
                            String[] communes_8 = getContext().getResources().getStringArray(R.array.communes_8);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_8));
                            break;
                        case 9:
                            latitude = (float)(float)(float)36.470165 ;
                            longitude = (float)(float)(float)2.828799;
                            String[] communes_9 = getContext().getResources().getStringArray(R.array.communes_9);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_9));
                            break;
                        case 10:
                            latitude = (float)(float)36.231648 ;
                            longitude =(float)(float)3.908258 ;
                            String[] communes_10 = getContext().getResources().getStringArray(R.array.communes_10);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_10));
                            break;
                        case 11:
                            latitude = (float)(float)24.375344 ;
                            longitude = (float)(float)4.320844;
                            String[] communes_11 = getContext().getResources().getStringArray(R.array.communes_11);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_11));
                            break;
                        case 12:
                            latitude = (float)(float)35.124945 ;
                            longitude = (float)(float)7.901174 ;
                            String[] communes_12 = getContext().getResources().getStringArray(R.array.communes_12);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_12));
                            break;
                        case 13:
                            latitude = (float)(float)34.881789;
                            longitude = (float)(float)-1.316699 ;
                            String[] communes_13 = getContext().getResources().getStringArray(R.array.communes_13);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_13));
                            break;
                        case 14:
                            latitude =  (float)(float)34.894758;
                            longitude =(float)(float)1.594579 ;
                            String[] communes_14 = getContext().getResources().getStringArray(R.array.communes_14);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_14));
                            break;
                        case 15:
                            latitude = (float)36.681618  ;
                            longitude = (float)4.237186 ;
                            String[] communes_15 = getContext().getResources().getStringArray(R.array.communes_15);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_15));
                            break;
                        case 16:
                            latitude = (float)36.775361 ;
                            longitude = (float)3.060188 ;
                            String[] communes_16 = getContext().getResources().getStringArray(R.array.communes_16);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_16));
                            break;
                        case 17:
                            latitude =  (float)34.342841  ;
                            longitude =  (float)3.217253 ;
                            String[] communes_17 = getContext().getResources().getStringArray(R.array.communes_17);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_17));
                            break;
                        case 18:
                            latitude = (float)36.729219 ;
                            longitude = (float)5.960778 ;
                            String[] communes_18 = getContext().getResources().getStringArray(R.array.communes_18);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_18));
                            break;
                        case 19:
                            latitude = (float)36.189275 ;
                            longitude = (float)5.403493 ;
                            String[] communes_19 = getContext().getResources().getStringArray(R.array.communes_19);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_19));
                            break;
                        case 20:
                            latitude = (float)34.743349 ;
                            longitude =(float) 0.244076 ;
                            String[] communes_20 = getContext().getResources().getStringArray(R.array.communes_20);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_20));
                            break;
                        case 21:
                            latitude = (float)36.754512 ;
                            longitude = (float)6.885626 ;
                            String[] communes_21 = getContext().getResources().getStringArray(R.array.communes_21);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_21));
                            break;
                        case 22:
                            latitude = (float)34.682268 ;
                            longitude = (float)-0.435755 ;
                            String[] communes_22 = getContext().getResources().getStringArray(R.array.communes_22);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_22));
                            break;
                        case 23:
                            latitude =(float) 36.898217 ;
                            longitude =(float) 7.754927 ;
                            String[] communes_23 = getContext().getResources().getStringArray(R.array.communes_23);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_23));
                            break;
                        case 24:
                            latitude = (float)36.349164  ;
                            longitude = (float)7.409499 ;
                            String[] communes_24 = getContext().getResources().getStringArray(R.array.communes_24);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_24));
                            break;
                        case 25:
                            latitude = (float)36.364519 ;
                            longitude =  (float)6.60826 ;
                            String[] communes_25 = getContext().getResources().getStringArray(R.array.communes_25);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_25));
                            break;
                        case 26:
                            latitude = (float)35.975205  ;
                            longitude = (float)3.01235 ;
                            String[] communes_26 = getContext().getResources().getStringArray(R.array.communes_26);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_26));
                            break;

                        case 27:
                            latitude =  (float)36.002692 ;
                            longitude =  (float)0.368687 ;
                            String[] communes_27 = getContext().getResources().getStringArray(R.array.communes_27);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_27));
                            break;

                        case 28:
                            latitude =  (float)35.130021 ;
                            longitude = (float)4.200311;
                            String[] communes_28 = getContext().getResources().getStringArray(R.array.communes_28);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_28));
                            break;
                        case 29:
                            latitude = (float)35.397839  ;
                            longitude = (float)0.24302 ;
                            String[] communes_29 = getContext().getResources().getStringArray(R.array.communes_29);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_29));
                            break;

                        case 30:
                            latitude = (float)30.998015 ;
                            longitude = (float)6.766454 ;
                            String[] communes_30 = getContext().getResources().getStringArray(R.array.communes_30);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_30));
                            break;

                        case 31:
                            latitude = (float)35.703275 ;
                            longitude = (float)-0.649298 ;
                            String[] communes_31 = getContext().getResources().getStringArray(R.array.communes_31);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_31));
                            break;
                        case 32:
                            latitude = (float)32.570303 ;
                            longitude = (float)1.125958 ;
                            String[] communes_32 = getContext().getResources().getStringArray(R.array.communes_32);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_32));
                            break;
                        case 33:
                            latitude =  (float)27.852851 ;
                            longitude = (float)7.818964 ;
                            String[] communes_33 = getContext().getResources().getStringArray(R.array.communes_33);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_33));
                            break;
                        case 34:
                            latitude = (float)36.095506 ;
                            longitude = (float)4.6611 ;
                            String[] communes_34 = getContext().getResources().getStringArray(R.array.communes_34);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_34));
                            break;
                        case 35:
                            latitude = (float)36.735803 ;
                            longitude = (float)3.616305 ;
                            String[] communes_35 = getContext().getResources().getStringArray(R.array.communes_35);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_35));
                            break;
                        case 36:
                            latitude = (float)36.671356 ;
                            longitude = (float)8.070134 ;
                            String[] communes_36 = getContext().getResources().getStringArray(R.array.communes_36);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_36));
                            break;
                        case 37:
                            latitude = (float)27.543907 ;
                            longitude = (float)-6.240054 ;
                            String[] communes_37 = getContext().getResources().getStringArray(R.array.communes_37);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_37));
                            break;
                        case 38:
                            latitude = (float)35.785898  ;
                            longitude = (float)1.834096 ;
                            String[] communes_38 = getContext().getResources().getStringArray(R.array.communes_38);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_38));
                            break;
                        case 39:
                            latitude =  (float)33.215441 ;
                            longitude = (float)7.155321 ;
                            String[] communes_39 = getContext().getResources().getStringArray(R.array.communes_39);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_39));
                            break;
                        case 40:
                            latitude = (float)34.913346 ;
                            longitude =  (float)6.905943 ;
                            String[] communes_40 = getContext().getResources().getStringArray(R.array.communes_40);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_40));
                            break;
                        case 41:
                            latitude =(float) 36.137868 ;
                            longitude = (float)7.826243 ;
                            String[] communes_41 = getContext().getResources().getStringArray(R.array.communes_41);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_41));
                            break;
                        case 42:
                            latitude =(float) 36.527274 ;
                            longitude =  (float)2.168369 ;
                            String[] communes_42 = getContext().getResources().getStringArray(R.array.communes_42);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_42));
                            break;
                        case 43:
                            latitude = (float)36.438022;
                            longitude =  (float)6.247579 ;
                            String[] communes_43 = getContext().getResources().getStringArray(R.array.communes_43);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_43));
                            break;
                        case 44:
                            latitude = (float)36.158684 ;
                            longitude = (float)2.084282 ;
                            String[] communes_44 = getContext().getResources().getStringArray(R.array.communes_44);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_44));
                            break;
                        case 45:
                            latitude = (float)33.233685  ;
                            longitude =  (float)-0.815196;
                            String[] communes_45 = getContext().getResources().getStringArray(R.array.communes_45);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_45));
                            break;
                        case 46:
                            latitude = (float)35.365047 ;
                            longitude =(float)-0.945281 ;
                            String[] communes_46 = getContext().getResources().getStringArray(R.array.communes_46);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_46));
                            break;
                        case 47:
                            latitude = (float)30.979872 ;
                            longitude =  (float)3.099085;
                            String[] communes_47 = getContext().getResources().getStringArray(R.array.communes_47);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_47));
                            break;
                        case 48:
                            latitude = (float)35.836319  ;
                            longitude = (float)0.911854 ;
                            String[] communes_48 = getContext().getResources().getStringArray(R.array.communes_48);
                            arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, Arrays.asList(communes_48));
                            break;

                    }
                } else {
                    emptyCommunList();
                }

                sp_commune.setAdapter(arrayAdapter_commune);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        sp_commune.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               // myToast.showInfo(getNameFromString(sp_commune.getSelectedItem().toString()));
                setCommune(sp_commune.getSelectedItem().toString().trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mDialog.setCanceledOnTouchOutside(true);
        return view;
    }

    public void emptyCommunList() {
        String[] commune = getContext().getResources().getStringArray(R.array.select_commune_empty);
        arrayList_commune = new ArrayList<>(Arrays.asList(commune));
        arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, arrayList_commune);
        sp_commune.setEnabled(false);
        sp_commune.setClickable(false);
        sp_commune.setAdapter(arrayAdapter_commune);
        /*        myToast.showInfo("You need to select wilaya");*/
    }

    public void setWilaya(String wilayaSelected) {
        wilaya = wilayaSelected;
    }

    public void setCommune(String communeSelected) {
        commune = communeSelected;
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back_sign_up:
                dismiss();
                break;

            case R.id.signup_btn:

                if (!util.isInternetConnected()) {
                    //Show a dialog
                    util.ShowCustomDialog();
                } else {

                    util.hideKeyBoardWhenButtonClicked(sign_up_layout);
                    checkingUserData(user);
                }

                break;
            case R.id.signup_login_btn:
/*                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);*/
                break;

            case R.id.btn_choose_file:
                verifyStoragePermissions(getActivity());
                break;

        }
    }

    public void settingUpHooks() {
        signup_first_name = view.findViewById(R.id.signup_first_name);
        signup_last_name = view.findViewById(R.id.signup_last_name);
        signup_email = view.findViewById(R.id.signup_email);
        signup_password = view.findViewById(R.id.signup_password);
        signup_phone = view.findViewById(R.id.signup_phone);

        sp_wilaya = view.findViewById(R.id.sp_wilaya);
        sp_commune = view.findViewById(R.id.sp_commune);

        sign_up_layout = view.findViewById(R.id.sign_up_layout);

    }

    public void settingUpListeners() {
        view.findViewById(R.id.back_sign_up).setOnClickListener(this);
        view.findViewById(R.id.signup_btn).setOnClickListener(this);
        view.findViewById(R.id.signup_login_btn).setOnClickListener(this);
        view.findViewById(R.id.btn_choose_file).setOnClickListener(this);
    }


    private void checkingUserData(User user) {


        if (!util.validateXInput(signup_first_name) | !util.validateXInput(signup_last_name)
                | !util.validateEmail(signup_email) | !util.validatePassword(signup_password)
                | !util.validatePhoneNo(signup_phone)) {
            return;
        }

        if (wilaya.equals("") | wilaya.equals("Wilaya")) {
            myToast.showWarning("Wilaya must be selected!");
            return;
        }

        if (commune.equals("") | commune.equals("Commune")) {
            myToast.showWarning("Commune must be selected!");
            return;
        }

        //get inputs values
         firstName = signup_first_name.getEditText().getText().toString().trim();
         lastName = signup_last_name.getEditText().getText().toString().trim();
         email = signup_email.getEditText().getText().toString().trim();
         password = signup_password.getEditText().getText().toString().trim();
         phoneNo = signup_phone.getEditText().getText().toString();
         address = new Address(latitude, longitude, wilaya, commune);

        if (imagePath != null) {
            loadingDialog.startLoadingDialog();
            uploadIdImage(imagePath);
        } else {
            myToast.showWarning("Please select an image !");
            return;
        }
    }

    public void signUpUser() {




        user = new User(address, firstName, lastName, phoneNo, email, iStartUpScreen.getUserAccountType(),
                password, photoPath);

        registerViewModel.createUser(user).observe((LifecycleOwner) getActivity(), new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                loadingDialog.dismissDialog();
                myToast.showSuccess("Account created successfully");
                redirectUtil.initAccountValidationUi();
            }
        });
        registerViewModel.getToast().observe((LifecycleOwner) getActivity(), new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                loadingDialog.dismissDialog();
                //TODO Add something to notify the user that a problem has occurred
                myToast.showError(errorResponse.getErrorMessage());
            }
        });
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

    private void uploadIdImage(String imagePath) {

        File file = new File(imagePath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

        registerViewModel.uploadIdImage(body,"ids",phoneNo).observe((LifecycleOwner) getActivity(), new Observer<UploadResponse>() {
            @Override
            public void onChanged(UploadResponse responseBody) {
                //myToast.showSuccess("Image uploaded successfully");
                //todo redirect to add frag
/*                Toast.makeText(SignUpSecond.this, "success\n path : " + responseBody.getImagePath()
                        + "\ncode :" + responseBody.getCode(), Toast.LENGTH_LONG).show();*/

                photoPath = responseBody.getImagePath();
                signUpUser();
                //myToast.showSuccess("Id image is ready to upload");
                //(R.id.signup_next_btn).setVisibility(View.VISIBLE);
            }
        });
        registerViewModel.getToastImage().observe((LifecycleOwner) getActivity(), new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                loadingDialog.startLoadingDialog();
                //TODO Add something to notify the user that a problem has occurred
                myToast.showError(errorResponse.getErrorMessage());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            imagePath = util.getRealPathFromUri(path,getActivity());
        } else {
            myToast.showError("unable to choose image");
        }
    }



    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        //dismiss();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myToast = new MyToast(getActivity());
        util = new Util(getActivity());
        iStartUpScreen = (IStartUpScreen) getActivity();
        loadingDialog = new LoadingDialog(getActivity());
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
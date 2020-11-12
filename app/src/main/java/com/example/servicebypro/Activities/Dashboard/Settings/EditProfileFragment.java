package com.example.servicebypro.Activities.Dashboard.Settings;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.Address;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.User;
import com.example.servicebypro.Remote.Models.Wilaya;
import com.example.servicebypro.SessionManager;
import com.example.servicebypro.ViewModels.SettingsFragmentViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.ResponseBody;

import static com.example.servicebypro.Activities.HelperClasses.Constants.getWilayaAndCommunes;


public class EditProfileFragment extends DialogFragment implements
        android.view.View.OnClickListener {

    private MyToast myToast;
    private Util util;
    private View view;
    User userData = new User();
    SessionManager sessionManager;
    TextInputLayout about_text_layout, phone_text_layout, email_text_layout;
    Button cancel, save;
    SettingsFragmentViewModel settingsFragmentViewModel;
    LinearLayout profile_info_layout;
    private Dialog mDialog;
    private IDashboard iDashboard;
    Spinner sp_wilaya, sp_commune;
    ArrayList<String> arrayList_wilaya;
    ArrayAdapter<String> arrayAdapter_wilaya;
    ArrayList<String> arrayList_commune;
    ArrayAdapter<String> arrayAdapter_commune;

    private String wilaya = "";
    private String commune = "";
    private float latitude = 0;
    private float longitude = 0;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new Dialog(getActivity(), R.style.DialogFragment);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(mDialog.getWindow().getAttributes());

        final View view = View.inflate(getActivity(), R.layout.fragment_edit_profile, null);
        mDialog.getWindow().setAttributes(layoutParams);
        mDialog.setContentView(view);

        return mDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsFragmentViewModel = new ViewModelProvider((ViewModelStoreOwner) getActivity()).get(SettingsFragmentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        sessionManager = new SessionManager(getContext());
        userData = sessionManager.getUser();
        hideSoftKeyboard();

        settingUpHooks();
        preFillingEditTextsWithUserData();
        settingUpListeners();


        //setting up wilayas
        String[] wilaya = getContext().getResources().getStringArray(R.array.wilaya);
        arrayList_wilaya = new ArrayList<>(Arrays.asList(wilaya));
        arrayAdapter_wilaya = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, arrayList_wilaya);
        sp_wilaya.setAdapter(arrayAdapter_wilaya);
        preSelectWillay();
        sp_wilaya.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                setWilaya(util.getNameFromString(sp_wilaya.getSelectedItem().toString()));
                setLatLongFromSlecetedWilaya(sp_wilaya.getSelectedItem().toString());
                setCommune("");
                if (position > 0) {
                    sp_commune.setEnabled(true);
                    sp_commune.setClickable(true);
                    // get spinner value
                    arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, getWilayaAndCommunes(getContext()).get(position - 1).getCommunes());
                    int spinnerPosition = arrayAdapter_commune.getPosition(userData.getAddress().getCommune());
                    sp_commune.setAdapter(arrayAdapter_commune);
                    sp_commune.getItemAtPosition(sp_commune.getSelectedItemPosition());
                    if (sp_commune.getAdapter().getCount() >= spinnerPosition && spinnerPosition != -1) {
                        if (sp_commune.getAdapter().getItem(spinnerPosition).equals(userData.getAddress().getCommune())) {
                            sp_commune.setSelection(spinnerPosition);
                        }
                    } else {
                        sp_commune.setSelection(0);
                    }
                } else {
                    emptyCommunList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        sp_commune.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                setCommune(sp_commune.getSelectedItem().toString().trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mDialog.setCanceledOnTouchOutside(true);
        return view;
    }

    private void hideSoftKeyboard() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void hideKeyBoardWhenButtonClicked() {
        try {
            // Then just use the following:
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(profile_info_layout.getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void emptyCommunList() {
        String[] commune = getContext().getResources().getStringArray(R.array.select_commune_empty);
        arrayList_commune = new ArrayList<>(Arrays.asList(commune));
        arrayAdapter_commune = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.spinner_textView, arrayList_commune);
        sp_commune.setEnabled(false);
        sp_commune.setClickable(false);
        sp_commune.setAdapter(arrayAdapter_commune);
    }

    public void setWilaya(String wilayaSelected) {
        wilaya = wilayaSelected;
    }

    public void setCommune(String communeSelected) {
        commune = communeSelected;
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
    public void onDetach() {
        super.onDetach();
        dismiss();
    }

    @Override
    public void onClick(View view) {
        hideKeyBoardWhenButtonClicked();
        switch (view.getId()) {
            case R.id.back_search:
            case R.id.cancel:
                dismiss();
                break;

            case R.id.save:
                //TODO SAVE NEW PROFILE INFO

                if (!util.isInternetConnected()) {
                    //Show a dialog
                    util.ShowCustomDialog();
                } else {
                    User user = gettingUserDataFromEditTexts();
                    updateUser(user);
                }
                //TODO test if user clicked on edit and didn't save an wanted to press back button, do it in profile activity
                break;
        }
    }


    public void updateUser(User user) {
        //check inputs validation
        if (wilaya.equals("") | wilaya.equals("Wilaya")) {
            myToast.showWarning("Wilaya must be selected!");
            return;
        }

        if (commune.equals("") | commune.equals("Commune")) {
            myToast.showWarning("Commune must be selected!");
            return;
        }
        if (!util.validateEmail(email_text_layout) | !util.validatePhoneNo(phone_text_layout)) {
            return;
        }
        settingsFragmentViewModel.updateUser(user).observe((LifecycleOwner) getActivity(), new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                myToast.showSuccess("User updated successfully");
                sessionManager.updateUserData(user);
                dismiss();
                iDashboard.inflateFragment(getString(R.string.fragment_profile));
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

    private User gettingUserDataFromEditTexts() {

        User user = new User();
        user.setIdUser(userData.getIdUser());
        user.setAboutMe(about_text_layout.getEditText().getText().toString().trim());
        user.setPhone(phone_text_layout.getEditText().getText().toString().trim());
        user.setEmail(email_text_layout.getEditText().getText().toString().trim());
        user.setAvatar(userData.getAvatar());
        Address address = new Address();
        address.setWilaya(wilaya);
        address.setCommune(commune);
        address.setLatitude(latitude);
        address.setLongitude(longitude);
        user.setAddress(address);
        return user;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        util = new Util(getContext());
        myToast = new MyToast(getContext());
        iDashboard = (IDashboard) getActivity();
    }

    private void settingUpListeners() {
        view.findViewById(R.id.save).setOnClickListener(this);
        view.findViewById(R.id.cancel).setOnClickListener(this);
        view.findViewById(R.id.back_search).setOnClickListener(this);
    }

    private void settingUpHooks() {
        about_text_layout = view.findViewById(R.id.about_text);
        phone_text_layout = view.findViewById(R.id.phone_text_layout);
        email_text_layout = view.findViewById(R.id.email_text_layout);
        save = view.findViewById(R.id.save);
        cancel = view.findViewById(R.id.cancel);
        sp_wilaya = view.findViewById(R.id.sp_wilaya);
        sp_commune = view.findViewById(R.id.sp_commune);
    }


    private void preFillingEditTextsWithUserData() {
        //setting text from session
        about_text_layout.getEditText().setText(userData.getAboutMe());
        phone_text_layout.getEditText().setText(userData.getPhone());
        email_text_layout.getEditText().setText(userData.getEmail());
    }


    public void preSelectWillay() {

        ArrayList<Wilaya> willayArrayList = getWilayaAndCommunes(getContext());
        for (int i = 0; i < willayArrayList.size(); i++) {
            if (willayArrayList.get(i).getName().contains(userData.getAddress().getWilaya())) {
                sp_wilaya.setSelection(i + 1);
            }
        }
    }

    public void setLatLongFromSlecetedWilaya(String wilayaName) {
        longitude = latitude = 0;
        ArrayList<Wilaya> willayArrayList = getWilayaAndCommunes(getContext());
        for (int i = 0; i < willayArrayList.size(); i++) {
            if (willayArrayList.get(i).getName().contains(wilayaName)) {

                longitude = willayArrayList.get(i).getLongitude();
                latitude = willayArrayList.get(i).getLatitude();
            }
        }
    }
}
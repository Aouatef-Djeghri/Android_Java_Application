package com.example.servicebypro.Activities.HelperClasses;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;

import com.example.servicebypro.Activities.Common.Login.AccountValidationFragment;
import com.example.servicebypro.Activities.Common.Login.ForgetPasswordFragment;
import com.example.servicebypro.Activities.Common.Login.ForgetPasswordSuccessMessageFragment;
import com.example.servicebypro.Activities.Common.Login.LoginFragment;
import com.example.servicebypro.Activities.Common.SignUp.SelectAccountTypeFragment;
import com.example.servicebypro.Activities.Common.SignUp.SignUpFragment;
import com.example.servicebypro.Activities.Dashboard.Assignments.AssignmentFinishedFragment;
import com.example.servicebypro.Activities.Dashboard.Assignments.AssignmentPendingFragment;
import com.example.servicebypro.Activities.Dashboard.Assignments.AssignmentRequestFragment;
import com.example.servicebypro.Activities.Dashboard.Assignments.ClientDetailsFragment;
import com.example.servicebypro.Activities.Dashboard.Assignments.DirectionsMapFragment;
import com.example.servicebypro.Activities.Dashboard.Assignments.FreelancerDetailsFragment;
import com.example.servicebypro.Activities.Dashboard.Assignments.FullImageFragment;
import com.example.servicebypro.Activities.Dashboard.Assignments.ReviewFragment;
import com.example.servicebypro.Activities.Dashboard.Assignments.SeeLocationInMapFragment;
import com.example.servicebypro.Activities.Dashboard.Settings.ChangePasswordFragment;
import com.example.servicebypro.Activities.Dashboard.Settings.EditProfileFragment;
import com.example.servicebypro.Activities.Dashboard.Settings.UpdateServicesFragment;
import com.example.servicebypro.Activities.Dashboard.Settings.UserReviewsFragment;
import com.example.servicebypro.Activities.Dashboard.Settings.UserServiceFragment;
import com.example.servicebypro.Activities.Dashboard.Settings.UserVisibilityFragment;

public class RedirectUtil {

    Activity activity;

    public RedirectUtil(Activity activity) {
        this.activity = activity;
    }

    public void initSignUpUi() {
        DialogFragment dialogFragment = new SignUpFragment();
        dialogFragment.show(activity.getFragmentManager(), "dialog");
    }

    public void initLoginUi() {
        DialogFragment dialogFragment = new LoginFragment();
        dialogFragment.show(activity.getFragmentManager(), "dialog");
    }

    public void initSelectUserAccountTypeUi() {
        DialogFragment dialogFragment = new SelectAccountTypeFragment();
        dialogFragment.show(activity.getFragmentManager(), "dialog");
    }

    public void initSeeInMapUI() {
        DialogFragment dialogFragment = new SeeLocationInMapFragment();
        dialogFragment.show(activity.getFragmentManager(), "dialog");
    }

    public void initAcceptedFreelancerDetails() {
        DialogFragment dialogFragment = new FreelancerDetailsFragment();
        dialogFragment.show(activity.getFragmentManager(), "dialog");
    }

    public void initWorkClientUi() {
        DialogFragment dialogFragment = new ClientDetailsFragment();
        dialogFragment.show(activity.getFragmentManager(), "dialog");
    }

    public void initDirectionsMapUI() {
        DialogFragment dialogFragment = new DirectionsMapFragment();
        dialogFragment.show(activity.getFragmentManager(), "dialog");
    }


    public void initRequestWork() {
        DialogFragment dialogFragment = new AssignmentRequestFragment();
        dialogFragment.show(activity.getFragmentManager(), "dialog");
    }

    public void initPendingWork() {
        DialogFragment dialogFragment = new AssignmentPendingFragment();
        dialogFragment.show(activity.getFragmentManager(), "dialog");
    }

    public void initFinishedWork() {
        DialogFragment dialogFragment = new AssignmentFinishedFragment();
        dialogFragment.show(activity.getFragmentManager(), "dialog");
    }

    public void initEditProfileDialog() {
        DialogFragment dialogFragment = new EditProfileFragment();
        dialogFragment.show(activity.getFragmentManager(), "dialog");
    }

    public void initUpdateServicesUi() {
        DialogFragment dialogFragment = new UpdateServicesFragment();
        dialogFragment.show(activity.getFragmentManager(), "dialog");
    }

    public void initChangePasswordUi() {
        android.app.DialogFragment dialogFragment = new ChangePasswordFragment();
        dialogFragment.show(activity.getFragmentManager(), "dialog");
    }

    public void initAddReviewUi() {
        android.app.DialogFragment dialogFragment = new ReviewFragment();
        dialogFragment.show(activity.getFragmentManager(), "dialog");
    }

    public void initAccountValidationUi() {
        android.app.DialogFragment dialogFragment = new AccountValidationFragment();
        dialogFragment.show(activity.getFragmentManager(), "dialog");
    }

    public void initForgetPasswordUi() {
        android.app.DialogFragment dialogFragment = new ForgetPasswordFragment();
        dialogFragment.show(activity.getFragmentManager(), "dialog");
    }

    public void initSuccessfulPasswordResetUi() {
        android.app.DialogFragment dialogFragment = new ForgetPasswordSuccessMessageFragment();
        dialogFragment.show(activity.getFragmentManager(), "dialog");
    }

    public void initUserServicesUi() {
        android.app.DialogFragment dialogFragment = new UserServiceFragment();
        dialogFragment.show(activity.getFragmentManager(), "dialog");
    }

    public void initUserReviewsUi() {
        android.app.DialogFragment dialogFragment = new UserReviewsFragment();
        dialogFragment.show(activity.getFragmentManager(), "dialog");
    }

    public void initUserVisibilityUi() {
        android.app.DialogFragment dialogFragment = new UserVisibilityFragment();
        dialogFragment.show(activity.getFragmentManager(), "dialog");
    }


    public void initFullWorkImageUi() {
        android.app.DialogFragment dialogFragment = new FullImageFragment();
        dialogFragment.show(activity.getFragmentManager(), "dialog");
    }
}

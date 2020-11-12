package com.example.servicebypro.Activities.Dashboard;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.servicebypro.Remote.Models.Address;
import com.example.servicebypro.Remote.Models.Application;
import com.example.servicebypro.Remote.Models.Categorie;
import com.example.servicebypro.Remote.Models.Review;
import com.example.servicebypro.Remote.Models.Service;
import com.example.servicebypro.Remote.Models.User;
import com.example.servicebypro.Remote.Models.Work;

import java.util.ArrayList;

public interface IDashboard {

    void inflateFragment(String fragmentTag);

    void setToastToTest(String msg);

    String getCurrentFragTag();

    void setCurrentFragTag(String tag);

    void setWork(Work work);

    Work getWork();

    void setFreelancerInfo(User user);

    User getFreelancerInfo();

    int getCurrent_step();

    void setCurrent_step(int current_step);

    int getPrevious_step(int current_step);

    void nextStep(int progress);

    void backStep(int progress);

    Service getWorkService();

    String getWorkDate();

    String getWorkTitle();

    String getWorkDescription();

    String getWorkType();

    String getWorkPaymentType();

    Address getWorkAddress();

    void setAddPostTitleText(String titleText, TextView textView);

    String getCurrentFragmentTag(int current_step);

    void setProgressBar(ProgressBar pB);

    ProgressBar getProgressBar();

    TextView getTextView();

    void setTextView(TextView textView);

    Categorie getWorkCategory();

    Button getButtonNext();

    void setButtonNext(Button buttonNext);

    void setButtonNextVisibility(boolean isVisible);

    void doFragmentTransaction(Fragment fragment, String tag, boolean addToBackStack);


    User getWorkFreelancer();

    String getWorkImageOne();

    String getWorkImageTwo();

    String getWorkImageThree();

    ArrayList<Work> getSearchedWork();

    Work getSelectedWork();

    Double getWorkMinPrice();

    Double getWorkMaxPrice();

    Boolean hasApplied();

    Application getUserApplication();

    String getUserRole();

    String getImageLink();

}

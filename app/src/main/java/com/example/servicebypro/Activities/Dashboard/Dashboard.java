package com.example.servicebypro.Activities.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.servicebypro.Activities.Dashboard.Add.AddFragment;
import com.example.servicebypro.Activities.Dashboard.Add.ConfirmAddress;
import com.example.servicebypro.Activities.Dashboard.Add.Post1SelectServiceFragment;
import com.example.servicebypro.Activities.Dashboard.Add.Post2DetailsFragment;
import com.example.servicebypro.Activities.Dashboard.Add.Post3LoactionFragment;
import com.example.servicebypro.Activities.Dashboard.Add.Post4DateFragment;
import com.example.servicebypro.Activities.Dashboard.Add.Post5PaymentTypeFragment;
import com.example.servicebypro.Activities.Dashboard.Add.Post6PaymentHourlyFragment;
import com.example.servicebypro.Activities.Dashboard.Add.Post7PaymentFixedFragment;
import com.example.servicebypro.Activities.Dashboard.Add.Post8SelectTypeFragment;
import com.example.servicebypro.Activities.Dashboard.Add.Post0JobFragment;
import com.example.servicebypro.Activities.Dashboard.Add.Post9SelectFreelancer;
import com.example.servicebypro.Activities.Dashboard.Assignments.AssignmentFinishedFragment;
import com.example.servicebypro.Activities.Dashboard.Assignments.AssignmentPendingFragment;
import com.example.servicebypro.Activities.Dashboard.Assignments.AssignmentRequestFragment;
import com.example.servicebypro.Activities.Dashboard.Assignments.AssignmentsFragment;
import com.example.servicebypro.Activities.Dashboard.Search.CheckSearchedWorkListFragment;
import com.example.servicebypro.Activities.Dashboard.Search.SearchFragment;
import com.example.servicebypro.Activities.Dashboard.Settings.ProfileFragment;
import com.example.servicebypro.Activities.Dashboard.Settings.SettingsFragment;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.RedirectUtil;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.Address;
import com.example.servicebypro.Remote.Models.Application;
import com.example.servicebypro.Remote.Models.Categorie;
import com.example.servicebypro.Remote.Models.Service;
import com.example.servicebypro.Remote.Models.User;
import com.example.servicebypro.Remote.Models.Work;
import com.example.servicebypro.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.servicebypro.Activities.HelperClasses.Constants.MAX_STEP;

public class Dashboard extends AppCompatActivity implements IDashboard, Post1SelectServiceFragment.FragmentSelectServiceListener,
        Post2DetailsFragment.FragmentSetDetailsListener,
        Post4DateFragment.FragmentSelectDateListener,
        Post8SelectTypeFragment.FragmentSelectTypeListener,
        Post5PaymentTypeFragment.FragmentSelectPaymentTypeListener,
        Post3LoactionFragment.FragmentSelectLocationListener,
        AddFragment.FragmentSelectCategoryListener,
        ConfirmAddress.FragmentSelectLocationFromMapListener,
        Post9SelectFreelancer.FragmentSelectFreelancer,
        SearchFragment.FragmentSelectSearchedWork,
        CheckSearchedWorkListFragment.FragmentSelectWorkForDetails,
        SearchFragment.FragmentSelectWorkForDetails,
        Post6PaymentHourlyFragment.FragmentSelectPriceListener,
        Post7PaymentFixedFragment.FragmentSelectPriceListener,
        CheckSearchedWorkListFragment.FragmentHasUserAlreadyApplied,
        SearchFragment.FragmentHasUserAlreadyApplied,
        AssignmentsFragment.FragmentSelectWorkForDetails,
        AssignmentsFragment.FragmentUserRole,
        AssignmentRequestFragment.FragmentSelectApplicationForDetails,
        AssignmentRequestFragment.FragmentOpenFullImage,
        AssignmentPendingFragment.FragmentOpenFullImage,
        AssignmentFinishedFragment.FragmentOpenFullImage {

    private BottomNavigationView bottomNavigationView;
    private Util util;
    private MyToast myToast;
    private String currentFragTag;
    HashMap<String, String> userData;
    SessionManager sessionManager;
    Work work = new Work();
    User freelancer = new User();
    Service service = new Service();
    Categorie categorie = new Categorie();
    String date, title, description, type, paymentType, imageLink = "";
    Address address = null;
    String imageOne, imageTwo, imageThree = "";
    User artisan = new User();
    Double min_price, max_price = 0.0;
    Boolean hasApplied = false;
    ArrayList<Work> worksSearched = new ArrayList<>();
    protected int current_step = 0;
    private int previousStep = -1;
    Application application = new Application();
    String role = "client";
    private ProgressBar progressBarr;
    TextView add_post_title_text;
    Button add_post_next_btn;
    boolean isNextButtonVisible;
    SharedPreferences isArtisanSetServicesScreen;
    RedirectUtil redirectUtil;

    @Override
    public ProgressBar getProgressBar() {
        return progressBarr;
    }

    @Override
    public void setProgressBar(ProgressBar pB) {
        progressBarr = pB;
    }

    @Override
    public TextView getTextView() {
        return add_post_title_text;
    }

    @Override
    public void setTextView(TextView textView) {
        add_post_title_text = textView;
    }

    @Override
    public Button getButtonNext() {
        return add_post_next_btn;
    }

    @Override
    public void setButtonNext(Button buttonNext) {
        add_post_next_btn = buttonNext;
    }

    @Override
    public void setButtonNextVisibility(boolean isVisible) {
        isNextButtonVisible = isVisible;
        if (isVisible) {
            add_post_next_btn.setVisibility(View.VISIBLE);
        } else {
            add_post_next_btn.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        currentFragTag = getResources().getString(R.string.fragment_add);
        sessionManager = new SessionManager(Dashboard.this);

        //Utilities class contains reusable methods
        util = new Util(this);
        myToast = new MyToast(this);
        redirectUtil = new RedirectUtil(this);

        //TODO Add session tests
        //TODO Add conx tests
        //TODO AnimatedBottomBar or buttom animation
        //Hooks
        bottomNavigationView = findViewById(R.id.main_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavListener);
        bottomNavigationView.setSelectedItemId(R.id.nav_bottom_add);


        //Todo Logout method Test
        init();
    }

    private void init() {
        setCurrent_step(0);
        setCurrentFragTag(getString(R.string.fragment_add));
        doFragmentTransaction(new AddFragment(), getString(R.string.fragment_add), false);
        //todo added this
        SharedPreferences isArtisanSetServicesScreen;
        isArtisanSetServicesScreen = getSharedPreferences("onFirstArtisanLogin", MODE_PRIVATE);
        boolean isFirstTime = isArtisanSetServicesScreen.getBoolean("onFirstArtisanLogin", true);
        if (isFirstTime) {
            SharedPreferences.Editor editor = isArtisanSetServicesScreen.edit();
            editor.putBoolean("onFirstArtisanLogin", false);
            editor.apply();
            if ((sessionManager.getUser().getRole()).equals("artisan")) {
                redirectUtil.initUpdateServicesUi();
            }
        }
    }

    @Override
    public void doFragmentTransaction(Fragment fragment, String tag, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, tag);

        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }


    private void doPostFragmentsTransaction(Fragment fragment, String tag, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.post_job_fragment_container, fragment, tag);

        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    //Test if connected to the internet
                    if (!util.isInternetConnected()) {
                        //Show a dialog
                        util.ShowCustomDialog();
                    } else {
                        //Perform actions
                        switch (menuItem.getItemId()) {

                            case R.id.nav_bottom_assignment:
                                doFragmentTransaction(new AssignmentsFragment(), getResources().getString(R.string.fragment_assignment), false);
                                break;
                            case R.id.nav_bottom_add:
                                doFragmentTransaction(new AddFragment(), getResources().getString(R.string.fragment_add), false);
                                break;
                            case R.id.nav_bottom_search:
                                doFragmentTransaction(new SearchFragment(), getResources().getString(R.string.fragment_search), false);
                                break;
                            case R.id.nav_bottom_messages:
                                doFragmentTransaction(new MessagesFragment(), getResources().getString(R.string.fragment_messages), false);
                                break;
                            case R.id.nav_bottom_profile:
                                doFragmentTransaction(new SettingsFragment(), getResources().getString(R.string.fragment_settings), false);
                                break;
                        }
                    }

                    return true;
                }
            };

    @Override
    public void onBackPressed() {

        if (R.id.nav_bottom_add != bottomNavigationView.getSelectedItemId()) {

            if (R.id.nav_bottom_profile == bottomNavigationView.getSelectedItemId() && getCurrentFragTag().equals(getString(R.string.fragment_profile))) {
                inflateFragment(getString(R.string.fragment_settings));
            } else {
                bottomNavigationView.setSelectedItemId(R.id.nav_bottom_add);
            }

        } else {
            if (getCurrent_step() == 0) {
                deleteAllFragment();
                this.finish();
            } else if (getCurrent_step() == 1) {
                init();
            } else {
                backStep(current_step);
                inflateFragment(getCurrentFragmentTag(current_step));
            }
        }
    }


    @Override
    public void backStep(int progress) {

        if (current_step == 1) {
            onBackPressed();
        } else if (current_step == 7) {
            current_step = 5;
            fadeOutIn(getProgressBar());
            getProgressBar().setProgress(current_step);
            inflateFragment(getCurrentFragmentTag(current_step));
        } else if (current_step == 8) {
            current_step = previousStep;
            fadeOutIn(getProgressBar());
            getProgressBar().setProgress(current_step);
            inflateFragment(getCurrentFragmentTag(current_step));
        } else if (progress > 1) {
            progress--;
            current_step = progress;
            fadeOutIn(getProgressBar());
        }
        if (progressBarr != null) {
            getProgressBar().setProgress(current_step);
        }
    }

    @Override
    public void inflateFragment(String fragmentTag) {

        if (fragmentTag.equals(getString(R.string.fragment_post_container))) {
            doFragmentTransaction(new Post0JobFragment(), fragmentTag, false);
        } else if (fragmentTag.equals(getString(R.string.fragment_service))) {
            doPostFragmentsTransaction(new Post1SelectServiceFragment(), fragmentTag, false);
        } else if (fragmentTag.equals(getString(R.string.fragment_details))) {
            doPostFragmentsTransaction(new Post2DetailsFragment(), fragmentTag, false);
        } else if (fragmentTag.equals(getString(R.string.fragment_date))) {
            doPostFragmentsTransaction(new Post4DateFragment(), fragmentTag, false);
        } else if (fragmentTag.equals(getString(R.string.fragment_location))) {
            doPostFragmentsTransaction(new Post3LoactionFragment(), fragmentTag, false);
        } else if (fragmentTag.equals(getString(R.string.fragment_payment))) {
            doPostFragmentsTransaction(new Post5PaymentTypeFragment(), fragmentTag, false);
        } else if (fragmentTag.equals(getString(R.string.fragment_payment_hourly))) {
            doPostFragmentsTransaction(new Post6PaymentHourlyFragment(), fragmentTag, false);
        } else if (fragmentTag.equals(getString(R.string.fragment_payment_fixed))) {
            doPostFragmentsTransaction(new Post7PaymentFixedFragment(), fragmentTag, false);
        } else if (fragmentTag.equals(getString(R.string.fragment_type))) {
            doPostFragmentsTransaction(new Post8SelectTypeFragment(), fragmentTag, false);
        } else if (fragmentTag.equals(getString(R.string.fragment_assignment))) {
            bottomNavigationView.setSelectedItemId(R.id.nav_bottom_assignment);
            doFragmentTransaction(new AssignmentsFragment(), fragmentTag, false);
        } else if (fragmentTag.equals(getString(R.string.fragment_add))) {
            bottomNavigationView.setSelectedItemId(R.id.nav_bottom_add);
            doFragmentTransaction(new AddFragment(), fragmentTag, false);
        } else if (fragmentTag.equals(getString(R.string.fragment_freelancer))) {
            doPostFragmentsTransaction(new Post9SelectFreelancer(), fragmentTag, false);
        } else if (fragmentTag.equals(getString(R.string.fragment_search))) {
            bottomNavigationView.setSelectedItemId(R.id.nav_bottom_search);
            doFragmentTransaction(new SearchFragment(), fragmentTag, false);
        } else if (fragmentTag.equals(getString(R.string.fragment_profile))) {
            doFragmentTransaction(new ProfileFragment(), fragmentTag, false);
        } else if (fragmentTag.equals(getString(R.string.fragment_settings))) {
            bottomNavigationView.setSelectedItemId(R.id.nav_bottom_profile);
            doFragmentTransaction(new SettingsFragment(), fragmentTag, false);
        }

    }

    @Override
    public void setToastToTest(String msg) {
        myToast.showInfo(msg);
    }

    @Override
    public String getCurrentFragTag() {
        return currentFragTag;
    }

    @Override
    public void setCurrentFragTag(String tag) {
        currentFragTag = tag;
    }
    @Override
    public void setWork(Work workSelected) {
        work = workSelected;
    }

    @Override
    public Work getWork() {
        return work;
    }

    @Override
    public void setFreelancerInfo(User user) {
        freelancer = user;
    }

    @Override
    public User getFreelancerInfo() {
        return freelancer;
    }

    @Override
    public void nextStep(int progress) {
        if (progress < MAX_STEP) {
            progress++;
            current_step = progress;
            fadeOutIn(getProgressBar());
        }
        getProgressBar().setProgress(current_step);
    }


    public void deleteAllFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment fragment_service = fragmentManager.findFragmentByTag(getString(R.string.fragment_service));
        if (fragment_service != null)
            fragmentTransaction.remove(fragment_service);

        Fragment fragment_date = fragmentManager.findFragmentByTag(getString(R.string.fragment_date));
        if (fragment_date != null)
            fragmentTransaction.remove(fragment_date);

        Fragment fragment_details = fragmentManager.findFragmentByTag(getString(R.string.fragment_details));
        if (fragment_details != null)
            fragmentTransaction.remove(fragment_details);

        Fragment fragment_type = fragmentManager.findFragmentByTag(getString(R.string.fragment_type));
        if (fragment_type != null)
            fragmentTransaction.remove(fragment_type);


        Fragment fragment_location = fragmentManager.findFragmentByTag(getString(R.string.fragment_location));
        if (fragment_location != null)
            fragmentTransaction.remove(fragment_location);


        Fragment fragment_payment = fragmentManager.findFragmentByTag(getString(R.string.fragment_payment));
        if (fragment_payment != null)
            fragmentTransaction.remove(fragment_payment);

        Fragment fragment_payment_fixed = fragmentManager.findFragmentByTag(getString(R.string.fragment_payment_fixed));
        if (fragment_payment_fixed != null)
            fragmentTransaction.remove(fragment_payment_fixed);

        Fragment fragment_payment_hourly = fragmentManager.findFragmentByTag(getString(R.string.fragment_payment_hourly));
        if (fragment_payment_hourly != null)
            fragmentTransaction.remove(fragment_payment_hourly);

        Fragment fragment_freelancer = fragmentManager.findFragmentByTag(getString(R.string.fragment_freelancer));
        if (fragment_freelancer != null)
            fragmentTransaction.remove(fragment_freelancer);


        fragmentTransaction.commit();
    }

    public static void fadeOutIn(View view) {
        view.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 0.f, 0.5f, 1.f);
        ObjectAnimator.ofFloat(view, "alpha", 0.f).start();
        animatorAlpha.setDuration(500);
        animatorSet.play(animatorAlpha);
        animatorSet.start();
    }

    @Override
    public String getCurrentFragmentTag(int current_step) {
        String fragmentTag = null;
        switch (current_step) {
            case 0:
                fragmentTag = getString(R.string.fragment_post_container);
                break;
            case 1:
                fragmentTag = getString(R.string.fragment_service);
                break;
            case 2:
                fragmentTag = getString(R.string.fragment_details);
                break;
            case 3:
                fragmentTag = getString(R.string.fragment_location);
                break;
            case 4:
                fragmentTag = getString(R.string.fragment_date);
                break;
            case 5:
                fragmentTag = getString(R.string.fragment_payment);
                break;
            case 6:
                fragmentTag = getString(R.string.fragment_payment_hourly);
                break;
            case 7:
                fragmentTag = getString(R.string.fragment_payment_fixed);
                break;
            case 8:
                fragmentTag = getString(R.string.fragment_type);
                break;
            case 9:
                fragmentTag = getString(R.string.fragment_freelancer);
                break;
        }
        return fragmentTag;
    }

    @Override
    public void setAddPostTitleText(String titleText, TextView add_post_title_text) {
        add_post_title_text.setText(titleText);
    }


    @Override
    public int getCurrent_step() {
        return current_step;
    }

    @Override
    public void setCurrent_step(int current_step) {
        this.current_step = current_step;
    }


    @Override
    public int getPrevious_step(int previousStep) {
        this.previousStep = previousStep;
        return previousStep;
    }


    @Override
    public Service getWorkService() {
        return service;
    }

    @Override
    public void onInputSelectServiceSent(Service serviceSelected) {
        service = serviceSelected;
    }

    @Override
    public void onInputSelectDateSent(String dateSelected) {
        date = dateSelected;
    }

    @Override
    public String getWorkDate() {
        return date;
    }

    @Override
    public void onInputDetailsSent(String titleSelected, String descriptionSelected, String imageOneSelected, String imageTwoSelected, String imageThreeSelected) {
        title = titleSelected;
        description = descriptionSelected;
        imageOne = imageOneSelected;
        imageTwo = imageTwoSelected;
        imageThree = imageThreeSelected;
    }

    @Override
    public String getWorkImageOne() {
        return imageOne;
    }

    @Override
    public String getWorkImageTwo() {
        return imageTwo;
    }

    @Override
    public String getWorkImageThree() {
        return imageThree;
    }

    @Override
    public String getWorkTitle() {
        return title;
    }

    @Override
    public String getWorkDescription() {
        return description;
    }


    @Override
    public String getWorkType() {
        return type;
    }

    @Override
    public void onInputSelectTypeSent(String typeSelected) {
        type = typeSelected;
    }


    @Override
    public void onInputSelectPaymentTypeSent(String paymentTypeSelected) {
        paymentType = paymentTypeSelected;
    }

    @Override
    public String getWorkPaymentType() {
        return paymentType;
    }


    @Override
    public void onInputSelectLocationSent(Address addressSelected) {
        address = addressSelected;
    }

    @Override
    public Address getWorkAddress() {
        return address;
    }


    @Override
    public void onInputSelectCategorySent(Categorie categorySelected) {
        categorie = categorySelected;
    }

    @Override
    public Categorie getWorkCategory() {

        return categorie;
    }

    @Override
    public void onInputSelectLocationFromMapSent(Address addressSelected) {
        address = addressSelected;
    }

    @Override
    public void onInputSelectFreelancerSent(User user) {
        artisan = user;
    }

    @Override
    public User getWorkFreelancer() {
        return artisan;
    }

    @Override
    public void onInputSelectSearchedWorkSent(ArrayList<Work> works) {
        worksSearched = works;
    }

    @Override
    public ArrayList<Work> getSearchedWork() {
        return worksSearched;
    }

    @Override
    public void onInputSelectWorkForDetailsSent(Work workSelected) {
        work = workSelected;
    }

    @Override
    public Work getSelectedWork() {
        return work;
    }

    @Override
    public void onInputSelectPriceSent(Double minPriceSelected, Double maxPriceSelected) {
        min_price = minPriceSelected;
        max_price = maxPriceSelected;
    }

    @Override
    public Double getWorkMinPrice() {
        return min_price;
    }

    @Override
    public Double getWorkMaxPrice() {
        return max_price;
    }

    @Override
    public void onInputAlreadyAppliedSent(Boolean applied) {
        hasApplied = applied;
    }

    @Override
    public Boolean hasApplied() {
        return hasApplied;
    }


    @Override
    public void onInputSelectUserApplicationForDetailsSent(Application applicationSelected) {
        application = applicationSelected;
    }

    @Override
    public Application getUserApplication() {
        return application;
    }

    @Override
    public void onInputUserRoleSent(String roleSelected) {
        role = roleSelected;
    }

    @Override
    public String getUserRole() {
        return role;
    }

    @Override
    public void onImageLinkSent(String imageLinkSelected) {
        imageLink = imageLinkSelected;
    }

    @Override
    public String getImageLink() {
        return imageLink;
    }
}
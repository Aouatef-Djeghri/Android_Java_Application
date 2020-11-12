package com.example.servicebypro.Activities.Dashboard.Assignments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;
import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.RedirectUtil;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.Activities.Adapters.WorkAdapter;

import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.User;
import com.example.servicebypro.Remote.Models.Work;
import com.example.servicebypro.R;
import com.example.servicebypro.SessionManager;
import com.example.servicebypro.ViewModels.AssignmentsFragmentViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class AssignmentsFragment extends Fragment implements WorkAdapter.OnWorkListener, View.OnClickListener {

    private AssignmentsFragmentViewModel workModel;
    private SegmentedButtonGroup sbg, sbg_options;
    private RecyclerView recyclerView;
    private WorkAdapter adapter;
    private Util util;
    private RedirectUtil redirectUtil;
    private MyToast myToast;
    private View view;
    private IDashboard iDashboard;
    private int clientWorkPosition = 0, artisanWorkPosition = 0;
    private ShimmerFrameLayout shimmerFrameLayout;
    LinearLayout linearLayout;
    Button data_not_available_Button;
    TextView data_not_available_text;
    ImageView data_not_available_image;
    ArrayList<Work> worksList = new ArrayList<>();
    SessionManager sessionManager;
    User userData;

    public AssignmentsFragment() {
        // Required empty public constructor
    }

    private AssignmentsFragment.FragmentSelectWorkForDetails listener;


    public interface FragmentSelectWorkForDetails {
        void onInputSelectWorkForDetailsSent(Work works);
    }

    private AssignmentsFragment.FragmentUserRole listenerRole;


    public interface FragmentUserRole {
        void onInputUserRoleSent(String role);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_assignments, container, false);
        workModel = new ViewModelProvider(requireActivity()).get(AssignmentsFragmentViewModel.class);
        iDashboard.setCurrentFragTag(getResources().getString(R.string.fragment_assignment));
        //Hooks
        sbg = (SegmentedButtonGroup) view.findViewById(R.id.sbg);
        sbg_options = (SegmentedButtonGroup) view.findViewById(R.id.sbg_options);
        data_not_available_Button = view.findViewById(R.id.data_not_available_Button);
        data_not_available_text = view.findViewById(R.id.data_not_available_text);
        data_not_available_image = view.findViewById(R.id.data_not_available_image);

        //Setting listeners
        view.findViewById(R.id.data_not_available_Button).setOnClickListener(this);


        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        linearLayout = view.findViewById(R.id.data_not_available);

        //TODO fix this depending on the user Role , this can be removed entirely if i remove the artisan/client role thing
        String Role = "client";
        if (Role == "artisan") {
            sbg.setPosition(1, true);
            sbg_options.setPosition(artisanWorkPosition, true);
        } else if (Role == "client") {
            sbg.setPosition(0, true);
            sbg_options.setPosition(clientWorkPosition, true);
        }


        sbg.setOnPositionChangedListener(new SegmentedButtonGroup.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(int position) {
                //Test if connected to the internet
                if (!util.isInternetConnected()) {
                    util.ShowCustomDialog();
                } else {
                    //Initialise work list to empty
                    worksList = new ArrayList<>();
                    //fetch updated work list
                    switch (position) {
                        case 0:
                            if (getContext() != null) {
                                listenerRole.onInputUserRoleSent(getString(R.string.client));
                            }
                            //TODO show client stuff
                            sbg_options.setPosition(clientWorkPosition, true);
                            switch (sbg_options.getPosition()) {
                                case 0:
                                    fetchData("client", userData.getIdUser());
                                    break;
                                case 1:
                                    fetchData("client", userData.getIdUser(), "request");

                                    break;
                                case 2:
                                    fetchData("client", userData.getIdUser(), "pending");
                                    break;
                                case 3:
                                    fetchData("client", userData.getIdUser(), "finished");
                                    break;
                            }
                            break;
                        case 1:
                            if (getContext() != null) {
                                listenerRole.onInputUserRoleSent(getString(R.string.artisan));
                            }
                            //TODO show artisan stuff
                            sbg_options.setPosition(artisanWorkPosition, true);
                            switch (sbg_options.getPosition()) {
                                case 0:
                                    fetchData("artisan", userData.getIdUser());
                                    break;
                                case 1:
                                    fetchData("artisan", userData.getIdUser(), "request");
                                    break;
                                case 2:
                                    fetchData("artisan", userData.getIdUser(), "pending");
                                    break;
                                case 3:
                                    fetchData("artisan", userData.getIdUser(), "finished");
                                    break;
                            }
                            break;
                    }
                }
            }
        });

        sbg_options.setOnPositionChangedListener(new SegmentedButtonGroup.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(int position) {

                //Test if connected to the internet
                if (!util.isInternetConnected()) {
                    util.ShowCustomDialog();
                } else {
                    //Initialise work list to empty
                    worksList = new ArrayList<>();
                    //fetch updated work list
                    switch ((int) sbg.getPosition()) {
                        case 0:
                            clientWorkPosition = sbg_options.getPosition();
                            switch (position) {
                                case 0:
                                    fetchData("client", userData.getIdUser());
                                    break;
                                case 1:
                                    fetchData("client", userData.getIdUser(), "request");

                                    break;
                                case 2:
                                    fetchData("client", userData.getIdUser(), "pending");
                                    break;
                                case 3:
                                    fetchData("client", userData.getIdUser(), "finished");
                                    break;
                            }

                            break;
                        case 1:
                            artisanWorkPosition = sbg_options.getPosition();
                            switch (position) {
                                case 0:
                                    fetchData("artisan", userData.getIdUser());
                                    break;
                                case 1:
                                    fetchData("artisan", userData.getIdUser(), "request");
                                    break;
                                case 2:
                                    fetchData("artisan", userData.getIdUser(), "pending");
                                    break;
                                case 3:
                                    fetchData("artisan", userData.getIdUser(), "finished");
                                    break;
                            }
                            break;
                    }
                }
            }
        });

        return view;
    }

    private void initRecyclerView(ArrayList<Work> works) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new WorkAdapter(works, getContext(), this);
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        recyclerView.setAdapter(adapter);

    }

    private void fetchData(String role, int idUser) {
        linearLayout.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        workModel.getWorkList(role, idUser).observe(this, new Observer<ArrayList<Work>>() {
            @Override
            public void onChanged(ArrayList<Work> works) {
                worksList = works;
                initRecyclerView(works);
                adapter.notifyDataSetChanged();
                if (works.isEmpty()) {
                    shimmerFrameLayout.setVisibility(View.GONE);
                    shimmerFrameLayout.stopShimmer();
                    linearLayout.setVisibility(View.VISIBLE);

                    switch (role) {
                        case "artisan":
                            data_not_available_Button.setText("Brows jobs");
                            data_not_available_text.setText("You are not working on any jobs at the moment. Brows to get hired");
                            data_not_available_image.setImageResource(R.drawable.artisan);
                            break;
                        case "client":
                            data_not_available_Button.setText("Post a job");
                            data_not_available_text.setText("You don't have any posted jobs at the moment. Post a job to hire talented freelancers");
                            data_not_available_image.setImageResource(R.drawable.client);
                            break;
                    }
                }
            }
        });
        workModel.getToast().observe(this, new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                //TODO Add something to notify the user that a problem has occurred
                //Toast.makeText(getActivity(), errorResponse.getErrorMessage(), Toast.LENGTH_LONG).show();
                myToast.showError(errorResponse.getErrorMessage());
            }
        });

    }

    private void fetchData(String role, int idUser, String status) {
        linearLayout.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        workModel.getWorkList(role, idUser, status).observe(this, new Observer<ArrayList<Work>>() {
            @Override
            public void onChanged(ArrayList<Work> works) {
                worksList = works;
                initRecyclerView(works);
                adapter.notifyDataSetChanged();
                if (works.isEmpty()) {
                    shimmerFrameLayout.setVisibility(View.GONE);
                    shimmerFrameLayout.stopShimmer();
                    linearLayout.setVisibility(View.VISIBLE);

                    switch (role) {
                        case "artisan":
                            data_not_available_Button.setText("Brows jobs");
                            data_not_available_text.setText("You are not working on any jobs at the moment. Brows to get hired");
                            data_not_available_image.setImageResource(R.drawable.artisan);
                            break;
                        case "client":
                            data_not_available_Button.setText("Post a job");
                            data_not_available_text.setText("You don't have any posted jobs at the moment. Post a job to hire talented freelancers");
                            data_not_available_image.setImageResource(R.drawable.client);
                            break;
                    }
                }
            }
        });
        workModel.getToast().observe(this, new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                //TODO Add something to notify the user that a problem has occurred
                // Toast.makeText(getActivity(), errorResponse.getErrorMessage(), Toast.LENGTH_LONG).show();
                myToast.showError(errorResponse.getErrorMessage());
            }
        });

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Utilities class contains reusable methods
        util = new Util(getActivity());
        myToast = new MyToast(getContext());
        sessionManager = new SessionManager(getContext());
        userData = sessionManager.getUser();
        redirectUtil = new RedirectUtil(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmer();
    }

    @Override
    public void onPause() {
        shimmerFrameLayout.startShimmer();
        super.onPause();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iDashboard = (IDashboard) getActivity();
        if (context instanceof AssignmentsFragment.FragmentSelectWorkForDetails) {
            listener = (AssignmentsFragment.FragmentSelectWorkForDetails) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentAListener");
        }
        if (context instanceof AssignmentsFragment.FragmentUserRole) {
            listenerRole = (AssignmentsFragment.FragmentUserRole) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentAListener");
        }
    }


    @Override
    public void onWorkClick(int position) {

        if(worksList.size()!=0){
            listener.onInputSelectWorkForDetailsSent(worksList.get(position));
            whichWorkDialogToOpen(worksList.get(position));
        }

    }

    //all
    public void whichWorkDialogToOpen(Work work) {

        switch (sbg_options.getPosition()) {
            case 0:
                switch (work.getStatus()) {
                    case "request":
                        redirectUtil.initRequestWork();
                        break;
                    case "pending":

                        if(work.getType().equals("post")) {
                            if(util.isMyApplicationAccepted(work, sessionManager.getUser())) {
                                redirectUtil.initPendingWork();
                            } else {
                                redirectUtil.initPendingWork();
                            }
                        } else {
                            if (util.isMyApplicationAccepted(work, sessionManager.getUser())) {
                                redirectUtil.initPendingWork();
                            } else {

                                if(work.getUser().getIdUser().equals(sessionManager.getUser().getIdUser())){
                                    //IF IM THE WORK OWNER
                                    redirectUtil.initPendingWork();
                                }else{
                                    redirectUtil.initRequestWork();//original
                                }
                            }
                        }
                        break;
                    case "finished":
                    case "canceled":
                        redirectUtil.initFinishedWork();
                        break;
                    case "mid_finished":
                       //TODO
                       // redirectUtil.initAddReviewUi();
                        redirectUtil.initPendingWork();
                        break;
                }

                break;
            case 1:

                if (work.getStatus().equals("pending")) {

                        if (util.isMyApplicationAccepted(work, sessionManager.getUser())) {
                            redirectUtil.initPendingWork();
                        } else {
                            redirectUtil.initRequestWork();

                        }

                }else{
                    redirectUtil.initRequestWork();
                }
                break;
            case 2:

                redirectUtil.initPendingWork();
                break;
            case 3:
                redirectUtil.initFinishedWork();
                break;
        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.data_not_available_Button:
            //    myToast.showWarning(sbg.getPosition() + "");
                if (sbg.getPosition() == 1) {
                    //redirect to search fragment
                    iDashboard.inflateFragment(getContext().getString(R.string.fragment_search));
                } else {
                    //redirect to add fragment
                    iDashboard.inflateFragment(getContext().getString(R.string.fragment_add));
                }
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        listenerRole = null;
    }
}

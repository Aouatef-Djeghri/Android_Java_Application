package com.example.servicebypro.Activities.Dashboard.Add;

import android.Manifest;
import android.app.DialogFragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.servicebypro.Activities.Adapters.UsersAdapter;
import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.Address;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.Review;
import com.example.servicebypro.Remote.Models.User;
import com.example.servicebypro.Remote.Models.Work;
import com.example.servicebypro.SessionManager;
import com.example.servicebypro.ViewModels.AddFragmentViewModel;
import com.example.servicebypro.ViewModels.AssignmentsFragmentViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static com.example.servicebypro.Activities.HelperClasses.Constants.DEFAULT_ZOOM;
import static com.example.servicebypro.Activities.HelperClasses.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;


public class Post9SelectFreelancer extends Fragment implements UsersAdapter.OnUserListener, OnMapReadyCallback, android.view.View.OnClickListener {

    private MyToast myToast;
    private Util util;
    private View view;
    IDashboard iDashboard;
    protected int current_step = 9;
    private SessionManager sessionManager;
    AddFragmentViewModel addFragmentViewModel;
    ArrayList<User> usersList = new ArrayList<User>();
    private RecyclerView recyclerView;
    UsersAdapter usersAdapter;
    RelativeLayout no_freelancer_layout;
    LinearLayout list_freelancer_layout;
    private AssignmentsFragmentViewModel assignmentsFragmentViewModel;
    LatLng workLatLng = null;
    private GoogleMap googleMap;
    private MapView map;
    private final LatLngBounds ALGERIA_BOUNDS = new LatLngBounds(new LatLng(18.92874, -8.704895),
            new LatLng(37.77284, 12.03598)); //bottom, left, top,right
    private boolean locationPermissionGranted = false;
    ImageView synchronization_list;

    private Post9SelectFreelancer.FragmentSelectFreelancer listener;

    public interface FragmentSelectFreelancer {
        void onInputSelectFreelancerSent(User user);
    }


    public Post9SelectFreelancer() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Utilities class contains reusable methods
        util = new Util(getActivity());
        myToast = new MyToast(getContext());
        sessionManager = new SessionManager(getContext());
        iDashboard.setAddPostTitleText("Select Freelancer", iDashboard.getTextView());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post_select_freelancer, container, false);
        addFragmentViewModel = new ViewModelProvider(requireActivity()).get(AddFragmentViewModel.class);
        assignmentsFragmentViewModel = new ViewModelProvider((ViewModelStoreOwner) getActivity()).get(AssignmentsFragmentViewModel.class);
        iDashboard.getButtonNext().setVisibility(View.GONE);
        view.findViewById(R.id.ic_target).setOnClickListener(this);
        Address workAddress = iDashboard.getWorkAddress();
        if (iDashboard.getWorkService() != null) {
            if (!util.isInternetConnected()) {
                //Show a dialog
                util.ShowCustomDialog();
            } else {
                fetchData(iDashboard.getWorkService().getIdService(), sessionManager.getUser().getIdUser());
            }
        }
        map = view.findViewById(R.id.map);
        map.onCreate(savedInstanceState);
        map.onResume(); // needed to get the map to display immediately
        getLocationPermission();
        workLatLng = new LatLng(workAddress.getLatitude(), workAddress.getLongitude());


        no_freelancer_layout = view.findViewById(R.id.no_freelancer_layout);
        synchronization_list = view.findViewById(R.id.synchronization_list);
        view.findViewById(R.id.synchronization_list).setOnClickListener(this);
        list_freelancer_layout = view.findViewById(R.id.list_freelancer_layout);
        list_freelancer_layout.setVisibility(View.VISIBLE);
        no_freelancer_layout.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iDashboard = (IDashboard) getActivity();
        iDashboard.setCurrent_step(current_step);
        if (context instanceof Post9SelectFreelancer.FragmentSelectFreelancer) {
            listener = (Post9SelectFreelancer.FragmentSelectFreelancer) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentAListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void initRecyclerView(ArrayList<User> users) {

        recyclerView = (RecyclerView) view.findViewById(R.id.users_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        usersAdapter = new UsersAdapter(users, getContext(), this);

        recyclerView.setAdapter(usersAdapter);

    }

    private void fetchData(int serviceId, int clientId) {

        addFragmentViewModel.listAllUsersByService(serviceId, clientId).observe(getViewLifecycleOwner(), new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                usersList = users;

                if (!users.isEmpty()) {
                    settingDistance(users);
                    initRecyclerView(users);
                    usersAdapter.notifyDataSetChanged();
                    //place each available freelancer in map
                    placingFreelancersInMap(users);
                } else {
                    list_freelancer_layout.setVisibility(View.GONE);
                    no_freelancer_layout.setVisibility(View.VISIBLE);
                  //  myToast.showInfo("Sorry , no artisan was found !");
                }
            }
        });
        addFragmentViewModel.getToastWork().observe(getViewLifecycleOwner(), new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse errorResponse) {
                //TODO Add something to notify the user that a problem has occurred
                //Toast.makeText(getActivity(), errorResponse.getErrorMessage(), Toast.LENGTH_LONG).show();
                myToast.showError(errorResponse.getErrorMessage());
            }
        });

    }

    public void settingDistance(ArrayList<User> users) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            double distance = distance(workLatLng.latitude, workLatLng.longitude, user.getAddress().getLatitude(), user.getAddress().getLongitude(), 'K');
            user.setDistance(distance);
        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        //straight line distance
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public void placingFreelancersInMap(ArrayList<User> users) {

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            //Add Marker on route g position
            MarkerOptions marker = new MarkerOptions();
            LatLng latLng = new LatLng(user.getAddress().getLatitude(), user.getAddress().getLongitude());
            marker.position(latLng);
            marker.title(user.getFirstName() + " " + user.getLastName());
            Drawable endDrawable = getContext().getDrawable(R.drawable.ic_location);
            marker.icon(util.getMarkerIconFromDrawable(endDrawable));
            googleMap.addMarker(marker);
        }

    }

    @Override
    public void onUserClick(int position) {
        //myToast.showSuccess(usersList.get(position).getFirstName());
        if (usersList.get(position) != null) {
            listener.onInputSelectFreelancerSent(usersList.get(position));

            DialogFragment dialogFragment = new CheckFreelancerInfo();
            //dialogFragment.setArguments(args);
            dialogFragment.show(getActivity().getFragmentManager(), "dialog");
        } else {
           // myToast.showSuccess("null");
        }

    }

    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            initMap();
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
                    //initialise our map
                    initMap();
                    goToWorkLocation();
                } else {
                    myToast.showSuccess("Sorry, you can't open directions without enabling location permissions");
                }
            }
            break;
        }
    }

    public void goToWorkLocation() {
        //        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(workLatLng, 5f);
        googleMap.animateCamera(cameraUpdate);
        //Add Marker on route ending position
        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(workLatLng);
        endMarker.title("Work Location");
        Drawable endDrawable = getContext().getDrawable(R.drawable.ic_job);
        endMarker.icon(util.getMarkerIconFromDrawable(endDrawable));
        googleMap.addMarker(endMarker);

    }

    private void initMap() {
        map.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
        //  startUserLocationsRunnable(); // update user locations every 'LOCATION_UPDATE_INTERVAL'
    }


    @Override
    public void onMapReady(GoogleMap map) {

        //myToast.showInfo("maps is ready !");
        googleMap = map;
        googleMap.setLatLngBoundsForCameraTarget(ALGERIA_BOUNDS);

        if (locationPermissionGranted) {
            goToWorkLocation();
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
        googleMap.addCircle(new CircleOptions()
                .center(workLatLng)
                .radius(90000.0)
                .strokeWidth(3f)
                .strokeColor(Color.BLUE)
                .fillColor(Color.argb(44, 44, 167, 224)));
/*
        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                goToWorkLocation();
                return false;
            }
        });*/
    }


    @Override
    public void onStart() {
        super.onStart();
        map.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        map.onStop();
    }

    @Override
    public void onPause() {
        map.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        map.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_target:
                goToWorkLocation();
                break;
            case R.id.synchronization_list:

                if (iDashboard.getWorkService() != null) {
                    if (!util.isInternetConnected()) {
                        //Show a dialog
                        util.ShowCustomDialog();
                    } else {
                        fetchData(iDashboard.getWorkService().getIdService(), sessionManager.getUser().getIdUser());
                    }
                }
                break;


        }
    }
}
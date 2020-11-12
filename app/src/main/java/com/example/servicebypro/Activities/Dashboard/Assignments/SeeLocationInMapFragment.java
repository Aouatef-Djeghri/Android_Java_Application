package com.example.servicebypro.Activities.Dashboard.Assignments;

import android.Manifest;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.RedirectUtil;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.SessionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import static com.example.servicebypro.Activities.HelperClasses.Constants.DEFAULT_ZOOM;
import static com.example.servicebypro.Activities.HelperClasses.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

public class SeeLocationInMapFragment extends DialogFragment implements
        android.view.View.OnClickListener, OnMapReadyCallback {

    private Util util;
    private MyToast myToast;
    private View view;
    private RedirectUtil redirectUtil;
    private IDashboard iDashboard;
    private Dialog mDialog;
    SessionManager sessionManager;
    private GoogleMap googleMap;
    private MapView seeInMapView;
    private final LatLngBounds ALGERIA_BOUNDS = new LatLngBounds(new LatLng(18.92874, -8.704895),
            new LatLng(37.77284, 12.03598)); //bottom, left, top,right
    private boolean locationPermissionGranted = false;

    //current and destination location objects
    LatLng clientLatLng = null;

    public SeeLocationInMapFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new Dialog(getActivity(), R.style.DialogFragment);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(mDialog.getWindow().getAttributes());
        //   layoutParams.windowAnimations = R.style.SlideUpDownDialog;

        final View view = View.inflate(getActivity(), R.layout.fragment_see_location_in_map, null);
        // seeInMapView = view.findViewById(R.id.see_in_map);
        mDialog.getWindow().setAttributes(layoutParams);
        mDialog.setContentView(view);

        return mDialog;
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
        view = inflater.inflate(R.layout.fragment_see_location_in_map, container, false);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        seeInMapView = view.findViewById(R.id.see_in_map);
        seeInMapView.onCreate(savedInstanceState);
        seeInMapView.onResume(); // needed to get the map to display immediately
        getLocationPermission();
        clientLatLng = new LatLng(iDashboard.getWork().getAddress().getLatitude(), iDashboard.getWork().getAddress().getLongitude());
        //Setting listeners
        view.findViewById(R.id.back_search).setOnClickListener(this);
        view.findViewById(R.id.ic_target).setOnClickListener(this);
        mDialog.setCanceledOnTouchOutside(true);
        return view;
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
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back_search:
                dismiss();
                break;

            case R.id.ic_target:
                goToClientLocation();
                break;
        }
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sessionManager = new SessionManager(getActivity());
        myToast = new MyToast(getActivity());
        iDashboard = (IDashboard) getActivity();
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onMapReady(GoogleMap map) {

        //myToast.showInfo("maps is ready !");
        googleMap = map;
        googleMap.setLatLngBoundsForCameraTarget(ALGERIA_BOUNDS);

        if (locationPermissionGranted) {
            goToClientLocation();
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }


        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                goToClientLocation();
                return false;
            }
        });
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
                    goToClientLocation();
                } else {
                    myToast.showSuccess("Sorry, you can't open directions without enabling location permissions");
                }
            }
            break;
        }
    }

    public void goToClientLocation() {
        //        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(clientLatLng, DEFAULT_ZOOM);
        googleMap.animateCamera(cameraUpdate);
        //Add Marker on route ending position
        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(clientLatLng);
        endMarker.title("Work Location");
        Drawable endDrawable = getContext().getDrawable(R.drawable.ic_job);
        endMarker.icon(util.getMarkerIconFromDrawable(endDrawable));
        googleMap.addMarker(endMarker);

    }

    private void initMap() {
        seeInMapView.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        seeInMapView.onResume();
        //  startUserLocationsRunnable(); // update user locations every 'LOCATION_UPDATE_INTERVAL'
    }


    @Override
    public void onStart() {
        super.onStart();
        seeInMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        seeInMapView.onStop();
    }

    @Override
    public void onPause() {
        seeInMapView.onPause();
        // stopLocationUpdates(); // stop updating user locations
        super.onPause();
    }

    @Override
    public void onDestroy() {
        seeInMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        seeInMapView.onLowMemory();
    }


}
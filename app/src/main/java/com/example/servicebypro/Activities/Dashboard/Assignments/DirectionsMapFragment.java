package com.example.servicebypro.Activities.Dashboard.Assignments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.servicebypro.Activities.Dashboard.Dashboard;
import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.RedirectUtil;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;
import com.example.servicebypro.SessionManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.servicebypro.Activities.HelperClasses.Constants.DEFAULT_ZOOM;
import static com.example.servicebypro.Activities.HelperClasses.Constants.ERROR_DIALOG_REQUEST;
import static com.example.servicebypro.Activities.HelperClasses.Constants.LOCATION_REQUEST_CODE;
import static com.example.servicebypro.Activities.HelperClasses.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

public class DirectionsMapFragment extends DialogFragment implements
        android.view.View.OnClickListener, OnMapReadyCallback, RoutingListener {

    private Util util;
    private MyToast myToast;
    private View view;
    private RedirectUtil redirectUtil;
    private IDashboard iDashboard;
    private Dialog mDialog;
    SessionManager sessionManager;
    private GoogleMap googleMap;
    private Location lastKnownLocation;

    private LocationCallback locationCallback;
    private MapView directionsMapView;
    private final LatLngBounds ALGERIA_BOUNDS = new LatLngBounds(new LatLng(18.92874, -8.704895),
            new LatLng(37.77284, 12.03598)); //bottom, left, top,right
    private FusedLocationProviderClient fusedLocationClient;
    private PlacesClient placesClient;
    boolean isSelectedLocationMap = false;
    private boolean locationPermissionGranted = false;


    //current and destination location objects
    //Location myLocation = null;
    LatLng clientLatLong = null;
    protected LatLng start = null;
    protected LatLng end = null;
    //polyline object
    private List<Polyline> polylines = null;

    public DirectionsMapFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new Dialog(getActivity(), R.style.DialogFragment);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(mDialog.getWindow().getAttributes());
        final View view = View.inflate(getActivity(), R.layout.fragment_directions_map, null);
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
        view = inflater.inflate(R.layout.fragment_directions_map, container, false);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        directionsMapView = view.findViewById(R.id.directions_map);
        directionsMapView.onCreate(savedInstanceState);
        directionsMapView.onResume(); // needed to get the map to display immediately
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        getLocationPermission();
        //Setting listeners
        view.findViewById(R.id.back_search).setOnClickListener(this);
        view.findViewById(R.id.ic_target).setOnClickListener(this);

        clientLatLong = new LatLng(iDashboard.getWork().getAddress().getLatitude(), iDashboard.getWork().getAddress().getLongitude());
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
                getDeviceLocation();
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

       // myToast.showInfo("maps is ready !");
        googleMap = map;
        ////todo uncomment this when not using emulator
        //googleMap.setLatLngBoundsForCameraTarget(ALGERIA_BOUNDS);
        if (locationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //check if gps is enabled or not and then request user to enable it
            checkGPS();
            //get destination location when user click on map
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    getDeviceLocation();
                }
            });
        }


    }

    // function to find Routes.
    public void findRoutes(LatLng Start, LatLng End) {
        if (Start == null || End == null) {
            myToast.showError("Unable to get location");
        } else {

            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key(getContext().getString(R.string.google_maps_api_key))  //also define your api key here.
                    .build();
            routing.execute();
        }
    }


    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(getContext(),
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
                    // getMyLocation();
                    getDeviceLocation();
                } else {
                    myToast.showSuccess("Sorry, you can't open directions without enabling location permissions");
                }
            }
            break;
        }
    }

    private void initMap() {
        directionsMapView.getMapAsync(this);
    }

    private void getDeviceLocation() {


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        fusedLocationClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                LatLng latlng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, DEFAULT_ZOOM);
                                googleMap.animateCamera(cameraUpdate);
                                //todo you can use the above or this to move the camera
                                // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            } else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }
                                        lastKnownLocation = locationResult.getLastLocation();
                                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                        fusedLocationClient.removeLocationUpdates(locationCallback);
                                    }
                                };
                                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

                            }
                        } else {
                            myToast.showError("unable to get last location");
                        }
                    }
                });

        if (lastKnownLocation != null) {
            googleMap.clear();
            start = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            //start route finding
            findRoutes(start, clientLatLong);
        } else {
            myToast.showError("can't find directions without gps enabled !");
        }

    }

    public void checkGPS() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });

        task.addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(getActivity(), 51);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    myToast.showError("enable to use gps");
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        directionsMapView.onResume();
        //  startUserLocationsRunnable(); // update user locations every 'LOCATION_UPDATE_INTERVAL'
    }


    @Override
    public void onStart() {
        super.onStart();
        directionsMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        directionsMapView.onStop();
    }

    @Override
    public void onPause() {
        directionsMapView.onPause();
        // stopLocationUpdates(); // stop updating user locations
        super.onPause();
    }

    @Override
    public void onDestroy() {
        directionsMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        directionsMapView.onLowMemory();
    }

    //Routing call back functions.
    @Override
    public void onRoutingFailure(RouteException e) {

      //  myToast.showError(e.toString());
        myToast.showError("Routing Failed");
//        Findroutes(start,end);
    }

    @Override
    public void onRoutingStart() {
        myToast.showInfo("Finding Route...");
    }

    //If Route finding success..
    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        if (polylines != null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng = null;
        LatLng polylineEndLatLng = null;


        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i < route.size(); i++) {

            if (i == shortestRouteIndex) {
                polyOptions.color(ContextCompat.getColor(getContext(),R.color.colorPrimary));
                polyOptions.width(7);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = googleMap.addPolyline(polyOptions);
                polylineStartLatLng = polyline.getPoints().get(0);
                int k = polyline.getPoints().size();
                polylineEndLatLng = polyline.getPoints().get(k - 1);
                polylines.add(polyline);

            } else {

            }

        }

        //Add Marker on route starting position
        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()));
        startMarker.title("My Location");

        Drawable starDrawable = getContext().getDrawable(R.drawable.ic_you_are_here);
        startMarker.icon(util.getMarkerIconFromDrawable(starDrawable));
        googleMap.addMarker(startMarker);

        //Add Marker on route ending position
        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);
        endMarker.title("Destination");
        Drawable endDrawable = getContext().getDrawable(R.drawable.ic_job);
        endMarker.icon(util.getMarkerIconFromDrawable(endDrawable));
        googleMap.addMarker(endMarker);
    }



    @Override
    public void onRoutingCancelled() {
        findRoutes(start, end);
    }


}
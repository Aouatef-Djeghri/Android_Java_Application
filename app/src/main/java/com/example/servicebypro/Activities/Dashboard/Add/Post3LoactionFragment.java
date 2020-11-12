package com.example.servicebypro.Activities.Dashboard.Add;

import android.Manifest;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.Activities.HelperClasses.Util;
import com.example.servicebypro.R;

import com.example.servicebypro.Remote.Models.Service;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import android.os.Handler;
import android.widget.TextView;


import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.servicebypro.Activities.HelperClasses.Constants.DEFAULT_ZOOM;
import static com.example.servicebypro.Activities.HelperClasses.Constants.ERROR_DIALOG_REQUEST;
import static com.example.servicebypro.Activities.HelperClasses.Constants.LOCATION_REQUEST_CODE;
import static com.example.servicebypro.Activities.HelperClasses.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

public class Post3LoactionFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener { //,GoogleMap.OnPolylineClickListen
    private static final String TAG = "Post3LoactionFragment";
    protected int current_step = 3;
    //variables related to google maps and google places
    private GoogleMap googleMap;

    private final LatLngBounds ALGERIA_BOUNDS = new LatLngBounds(new LatLng(18.92874, -8.704895),
            new LatLng(37.77284, 12.03598)); //bottom, left, top,right
    private FusedLocationProviderClient fusedLocationClient;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;

    private Location lastKnownLocation;
    private LocationCallback locationCallback;

    private MapView mapView;
    private MaterialSearchBar materialSearchBar;
    private RippleBackground rippleBg;

    private ImageView my_location_marker;

    boolean isSelectedLocationMap = false;
    //Utilities variables
    private Util util;
    private IDashboard iDashboard;
    private MyToast myToast;

    //Fragment variables
    private LinearLayout location_layout;
    private View view;
    private boolean locationPermissionGranted = false;
    //private Switch switch_current_location;


    private ImageView mInfo, mPlacePicker;

    private Post3LoactionFragment.FragmentSelectLocationListener listener;

    public interface FragmentSelectLocationListener {
        void onInputSelectLocationSent(com.example.servicebypro.Remote.Models.Address address);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post_location, container, false);
        //Setting listeners
        iDashboard.getButtonNext().setOnClickListener(this);
        iDashboard.getButtonNext().setVisibility(View.VISIBLE);

        //hooks
        materialSearchBar = view.findViewById(R.id.searchBar);
        materialSearchBar.findViewById(R.id.mt_container).setBackground(getResources().getDrawable(R.drawable.round_corner_toggle_my_light_gray));
/*        materialSearchBar.findViewById(R.id.mt_container).setOutlineAmbientShadowColor(getResources().getColor(R.color.white));
        materialSearchBar.findViewById(R.id.mt_container).setOutlineSpotShadowColor(getResources().getColor(R.color.white));*/
        rippleBg = view.findViewById(R.id.ripple_bg);
        my_location_marker = view.findViewById(R.id.my_location_marker);
        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume(); // needed to get the map to display immediately

        if (isServicesOK()) {
            getLocationPermission();
        }
        location_layout = view.findViewById(R.id.location_layout);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        initializePlaces();
        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();


        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //startSearch(text.toString(), true, null, true);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

                if (buttonCode == MaterialSearchBar.BUTTON_NAVIGATION) {
                    //opening or closing a navigation drawer
                    // myToast.showWarning("BUTTON_NAVIGATION");
                    if (materialSearchBar.isSuggestionsVisible())
                        materialSearchBar.clearSuggestions();
                    //materialSearchBar.hideSuggestionsList();

                    if (materialSearchBar.isSearchOpened())
                        materialSearchBar.closeSearch();
                } else if (buttonCode == MaterialSearchBar.BUTTON_BACK) {
                    // myToast.showWarning("BUTTON_BACK");
                    materialSearchBar.closeSearch();
                    materialSearchBar.clearSuggestions();
                    //materialSearchBar.hideSuggestionsList();
                }
            }
        });

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                RectangularBounds bounds = RectangularBounds.newInstance(
                        new LatLng(-8.68439978681, 19.0573642034),
                        new LatLng(11.9995056495, 37.1183806422));

                FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.builder()
                        //.setLocationRestriction(bounds)
                        .setCountry("dz")
                        //.setTypeFilter(TypeFilter.REGIONS)
                        //.setTypeFilter(TypeFilter.CITIES)
                        //.setTypeFilter(TypeFilter.ADDRESS)
                        //.setTypeFilter(TypeFilter.ESTABLISHMENT)
                        //.setTypeFilter(TypeFilter.GEOCODE)
                        .setSessionToken(token)
                        .setQuery(s.toString())
                        .build();
                placesClient.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {
                        if (task.isSuccessful()) {
                            FindAutocompletePredictionsResponse predictionsResponse = task.getResult();
                            if (predictionsResponse != null) {
                                predictionList = predictionsResponse.getAutocompletePredictions();
                                List<String> suggestionsList = new ArrayList<>();
                                for (int i = 0; i < predictionList.size(); i++) {
                                    AutocompletePrediction prediction = predictionList.get(i);
                                    suggestionsList.add(prediction.getFullText(null).toString());
                                }
                                materialSearchBar.updateLastSuggestions(suggestionsList);
                                if (!materialSearchBar.isSuggestionsVisible()) {
                                    materialSearchBar.showSuggestionsList();
                                }
                            }
                        } else {
                            Log.i(TAG, "prediction fetching task unsuccessful");
                        }
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        materialSearchBar.findViewById(R.id.mt_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialSearchBar.setText(null);
                //  myToast.showSuccess("Im here ");
                materialSearchBar.clearSuggestions();

            }
        });

        materialSearchBar.setSuggestionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int position, View v) {
                if (position >= predictionList.size()) {
                    return;
                }
                AutocompletePrediction selectedPrediction = predictionList.get(position);
                String suggestion = materialSearchBar.getLastSuggestions().get(position).toString();
                materialSearchBar.setText(suggestion);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        materialSearchBar.clearSuggestions();
                    }
                }, 1000);
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(materialSearchBar.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                final String placeId = selectedPrediction.getPlaceId();
                List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG);

                FetchPlaceRequest fetchPlaceRequest = FetchPlaceRequest.builder(placeId, placeFields).build();
                placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                    @Override
                    public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                        Place place = fetchPlaceResponse.getPlace();
                        Log.i("mytag", "Place found: " + place.getName());
                        LatLng latLngOfPlace = place.getLatLng();
                        if (latLngOfPlace != null) {
                            util.hideSoftKeyboard(getActivity());
                            hideKeyBoardWhenButtonClicked();
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOfPlace, DEFAULT_ZOOM));

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            apiException.printStackTrace();
                            int statusCode = apiException.getStatusCode();
                            Log.i("mytag", "place not found: " + e.getMessage());
                            Log.i("mytag", "status code: " + statusCode);
                        }
                    }
                });
            }

            @Override
            public void OnItemDeleteListener(int position, View v) {
                materialSearchBar.getLastSuggestions().remove(position);
                List list = materialSearchBar.getLastSuggestions();
                materialSearchBar.updateLastSuggestions(list);
            }
        });

        my_location_marker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng currentMarkerLocation = googleMap.getCameraPosition().target;
                isSelectedLocationMap = true;
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(currentMarkerLocation);

                markerOptions.title(getAddress(currentMarkerLocation));
                googleMap.clear();
                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                        currentMarkerLocation, 15);
                googleMap.animateCamera(location);
                googleMap.addMarker(markerOptions);
            }
        });

        materialSearchBar.getSearchEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {

                    //execute our method for searching
                    geoLocate();
                }
                return false;
            }
        });
        return view;
    }


    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Utilities class contains reusable methods
        util = new Util(getContext());
        myToast = new MyToast(getContext());
        // iPost.setToastToTest("this is the final toast location");
        iDashboard.setAddPostTitleText("location", iDashboard.getTextView());
        //hideSoftKeyboard();


    }

    private String getAddress(LatLng latLng) {

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            double lat = addresses.get(0).getLatitude();
            double lng = addresses.get(0).getLongitude();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {

                ft.remove(prev);
            }
            ft.addToBackStack(null);
            DialogFragment dialogFragment = new ConfirmAddress();

            Bundle args = new Bundle();
            args.putDouble("lat", latLng.latitude);
            args.putDouble("long", latLng.longitude);
            args.putString("address", address);
            args.putString("wilaya", state);
            args.putString("commune", city);
            args.putDouble("lat", lat);
            args.putDouble("lng", lng);
            dialogFragment.setArguments(args);
            dialogFragment.show(getActivity().getFragmentManager(), "dialog");
            return address;
        } catch (IOException e) {
            e.printStackTrace();
            return "No Address Found";

        }


    }


    public Post3LoactionFragment() {
        // Required empty public constructor
    }


    public void hideKeyBoardWhenButtonClicked() {
        try {
            // Then just use the following:
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(location_layout.getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    private void initializePlaces() {
        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(getActivity(), getResources().getString(R.string.google_maps_api_key));
        }
        // Retrieve a PlacesClient (previously initialized - see MainActivity)
        placesClient = Places.createClient(getContext());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.add_post_next_btn:

                if (isSelectedLocationMap == false || iDashboard.getWorkAddress()==null) { //!switch_current_location.isChecked() &&
                    myToast.showWarning("Please Click on red pin and confirm selected location!");
                } else {
                    //myToast.showInfo(iDashboard.getWorkAddress().toString() + "     ===");
                    util.hideSoftKeyboard(getActivity());
                    iDashboard.nextStep(iDashboard.getCurrent_step());
                    iDashboard.inflateFragment(getString(R.string.fragment_date));
                }

                break;

        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iDashboard = (IDashboard) getActivity();
        iDashboard.setCurrent_step(current_step);
        if (context instanceof Post3LoactionFragment.FragmentSelectLocationListener) {
            listener = (Post3LoactionFragment.FragmentSelectLocationListener) context;
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
                }
            }
            break;
        }
    }


    private void initMap() {
        mapView.getMapAsync(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        //  startUserLocationsRunnable(); // update user locations every 'LOCATION_UPDATE_INTERVAL'
    }


    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        // stopLocationUpdates(); // stop updating user locations
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        //myToast.showInfo("maps is ready !");
        googleMap = map;
        googleMap.setLatLngBoundsForCameraTarget(ALGERIA_BOUNDS);


        if (locationPermissionGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);

            //check if gps is enabled or not and then request user to enable it
            checkGPS();

            googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    if (materialSearchBar.isSuggestionsVisible())
                        materialSearchBar.clearSuggestions();
                    if (materialSearchBar.isSearchOpened())
                        materialSearchBar.closeSearch();
                    return false;
                }
            });
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 51) {
            if (resultCode == RESULT_OK) {
                getDeviceLocation();
            }
        }
    }


    private void geoLocate() {
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = materialSearchBar.getSearchEditText().getText().toString();

        Geocoder geocoder = new Geocoder(getContext());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);

            //Log.d(TAG, "geoLocate: found a location: " + address.toString());

            //myToast.showInfo(address.toString());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM));
        }
    }


    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occurred but we can resolve it
            Log.d(TAG, "isServicesOK: an error occurred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
          //  myToast.showError("You can't make map requests");
        }
        return false;
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
    }

}
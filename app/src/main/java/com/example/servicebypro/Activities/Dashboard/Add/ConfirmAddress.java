package com.example.servicebypro.Activities.Dashboard.Add;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.servicebypro.Activities.Dashboard.IDashboard;
import com.example.servicebypro.Activities.HelperClasses.MyToast;
import com.example.servicebypro.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.app.DialogFragment;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ConfirmAddress extends DialogFragment implements
        android.view.View.OnClickListener, OnMapReadyCallback {

    public Button no;
    View view;
    private GoogleMap mMap;
    Double Lat;
    Double Long;
    String Address;
    String wilaya;
    String commune;
    double lat;
    double lng;

    TextView myAddress;
    Button SelectBtn;
    Button ChangeBtn;
    private IDashboard iDashboard;
    private MyToast myToast;
    public ConfirmAddress.FragmentSelectLocationFromMapListener listener;

    public interface FragmentSelectLocationFromMapListener {
        void onInputSelectLocationFromMapSent(com.example.servicebypro.Remote.Models.Address address);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Lat = getArguments().getDouble("lat");
        Long = getArguments().getDouble("long");
        Address = getArguments().getString("address");
        wilaya = getArguments().getString("wilaya");
        commune = getArguments().getString("commune");
        lat = getArguments().getDouble("lat");
        lng = getArguments().getDouble("lng");

        myToast = new MyToast(getActivity());

        iDashboard = (IDashboard) getActivity();
        listener = (ConfirmAddress.FragmentSelectLocationFromMapListener) getActivity();

    }

    public ConfirmAddress() {
        // Required empty public constructor
    }


    MapFragment mapFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.custom_confirm_address, container, false);
        myAddress=(TextView)view.findViewById(R.id.myAddress);
        SelectBtn=(Button) view.findViewById(R.id.Select);
        ChangeBtn=(Button) view.findViewById(R.id.Change);



        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapp);
        mapFragment.getMapAsync(this);
        // Toast.makeText(getActivity(),mNum,Toast.LENGTH_LONG).show();

        SelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                com.example.servicebypro.Remote.Models.Address addressSelected =
                        new com.example.servicebypro.Remote.Models.Address();
                addressSelected.setLatitude((float) lat);
                addressSelected.setLongitude((float) lng);
                addressSelected.setWilaya(wilaya);
                addressSelected.setCommune(commune);
                listener.onInputSelectLocationFromMapSent(addressSelected);
                getFragmentManager().beginTransaction().remove(mapFragment).commit();
                dismiss();
            }
        });
        ChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(mapFragment).commit();
                dismiss();
            }
        });
        getDialog().setCanceledOnTouchOutside(true);
        return view;

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        getFragmentManager().beginTransaction().remove(mapFragment).commit();

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        dismiss();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        myAddress.setText(Address);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(Lat,Long));

        markerOptions.title(Address);
        mMap.clear();
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                new LatLng(Lat,Long), 16f);
        mMap.animateCamera(location);
        mMap.addMarker(markerOptions);
        Log.d("status","success");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }


    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
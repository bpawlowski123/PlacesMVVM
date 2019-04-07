package com.googleplaces.borisp.myapplication.View;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.googleplaces.borisp.myapplication.Data.DataWrapper;
import com.googleplaces.borisp.myapplication.Data.PlaceObject;
import com.googleplaces.borisp.myapplication.MainApplication;
import com.googleplaces.borisp.myapplication.R;
import com.googleplaces.borisp.myapplication.ViewModel.MainViewModel;

import java.util.Objects;


public class MapFramgnet extends BaseWrappableLiveDataFragment {
    MainViewModel viewModelToWorkWith = null;
    MapView mMapView;
    Observer<DataWrapper<Location>> locationObserver;
    private final static int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 123;

    private GoogleMap googleMap;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_framgnet, container, false);
        mMapView = rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(mMap -> googleMap = mMap);

        return rootView;
    }

    private void requestPlaces(){
        viewModelToWorkWith.getPlaces();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModelToWorkWith = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        locationObserver = wrappedLocation -> {
            if(wrappedLocation!=null && wrappedLocation.hasError()){
                showErrorMessage(wrappedLocation.getError());
            }else{
                Location location = wrappedLocation.getData();
                LatLng currentDevicePosition = new LatLng(location.getLatitude(), location.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder().target(currentDevicePosition).zoom(17).build();
                if(googleMap!=null) {
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
                requestPlaces();
            }
        };
        getLocationPermission();

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        viewModelToWorkWith.getPlacesLiveData().observe(Objects.requireNonNull(this.getActivity()), placeObjectDataWrapper -> {
            if(placeObjectDataWrapper!=null &&  placeObjectDataWrapper.hasError()){
                showErrorMessage(placeObjectDataWrapper.getError());
            }else{
                for(PlaceObject currentPlace:placeObjectDataWrapper.getData()){
                    LatLng currentPlaceLoc = new LatLng(Double.parseDouble(currentPlace.geometry.locationA.lat), Double.parseDouble(currentPlace.geometry.locationA.lng));
                    if(googleMap!=null) {
                        googleMap.addMarker(new MarkerOptions().position(currentPlaceLoc).title(currentPlace.name +" "+currentPlace.infoDistance.distance.text));
                    }
                }
            }});
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(),android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            ((MainApplication)getActivity().getApplication()).setHasLocationPermission(true);
            viewModelToWorkWith.getLocation().observe(getActivity(), locationObserver);
        } else {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String permissions[], @NonNull int[] grantResults) {
        ((MainApplication)getActivity().getApplication()).setHasLocationPermission(false);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ((MainApplication)getActivity().getApplication()).setHasLocationPermission(true);
                    viewModelToWorkWith.getLocation().observe(getActivity(),locationObserver);
                }
            }
        }
    }

}

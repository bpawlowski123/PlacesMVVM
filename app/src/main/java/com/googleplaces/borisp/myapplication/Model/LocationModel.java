package com.googleplaces.borisp.myapplication.Model;

import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.googleplaces.borisp.myapplication.MainApplication;

import javax.inject.Inject;

import io.reactivex.Single;


public class LocationModel {
    FusedLocationProviderClient mFusedLocationProviderClient;

    @Inject
    public LocationModel() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainApplication.getContext());
    }

    public Single<Location> getDeviceLocation() {
        return Single.create(subscriber -> {
            try {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult()!= null) {
                        Location mLastKnownLocation = (Location) task.getResult();
                        subscriber.onSuccess(mLastKnownLocation);
                    }
                });
            } catch (SecurityException e) {
                subscriber.onError(e);
            }
        });


    }
}

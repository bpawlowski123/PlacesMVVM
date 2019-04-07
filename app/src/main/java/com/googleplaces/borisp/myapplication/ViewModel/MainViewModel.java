package com.googleplaces.borisp.myapplication.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.googleplaces.borisp.myapplication.Data.DataWrapper;
import com.googleplaces.borisp.myapplication.Data.PlaceObject;
import com.googleplaces.borisp.myapplication.Data.PlacesPojo;
import com.googleplaces.borisp.myapplication.Data.ResultDistanceMatrix;
import com.googleplaces.borisp.myapplication.MainApplication;
import com.googleplaces.borisp.myapplication.Model.LocationModel;
import com.googleplaces.borisp.myapplication.Model.PlacesModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends ViewModel {
    private MutableLiveData<DataWrapper<Location>> currentLocation = new MutableLiveData<>();
    private MutableLiveData<DataWrapper<List<PlaceObject>>> placesLiveData = new MutableLiveData<>();
    @Inject LocationModel locationModel;
    @Inject PlacesModel placesModel;

    public MainViewModel() {
        super();
        ((MainApplication)MainApplication.getContext()).getMyComponent().inject(this);

    }

    public MutableLiveData<DataWrapper<List<PlaceObject>>> getPlacesLiveData() {
        return placesLiveData;
    }

    public MutableLiveData<DataWrapper<Location>> getLocation(){
        locationModel.getDeviceLocation()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(location -> {
                    DataWrapper<Location> locationData=  new DataWrapper<>();
                    locationData.setData(location);
                    currentLocation.setValue(locationData);
                })
                .doOnError(throwable -> {
                    DataWrapper<Location> locationData =  new DataWrapper<>();
                    locationData.setError("Location error");
                    currentLocation.setValue(locationData);
                }).subscribe();


        return currentLocation;
    }

    public void getPlaces(){
        if(currentLocation.getValue()!=null && currentLocation.getValue().getData()!=null) {
            Location locationToWorkWith = currentLocation.getValue().getData();
            String locationString = (locationToWorkWith.getLatitude() + "," + locationToWorkWith.getLongitude());

            placesModel.getPlaces(locationString)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess(root -> requestDistance(root,locationString))
                    .doOnError(throwable -> {
                        DataWrapper<Location> locationData=  new DataWrapper<>();
                        locationData.setError(throwable.getLocalizedMessage());
                        currentLocation.setValue(locationData);
                    })
                    .subscribe();
        }

    }

    private void requestDistance(PlacesPojo.Root root,String locationString){
        StringBuilder stringBuilder = new StringBuilder();
        for(PlacesPojo.CustomA currentPlace : root.customA){
            stringBuilder.append(currentPlace.geometry.locationA.lat);
            stringBuilder.append(",");
            stringBuilder.append(currentPlace.geometry.locationA.lng);
            stringBuilder.append("|");
        }

        placesModel.getDistance(locationString,stringBuilder.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(resultDistanceMatrix -> formData(root,resultDistanceMatrix))
                .doOnError(throwable -> {
                    DataWrapper<Location> locationData=  new DataWrapper<>();
                    locationData.setError(throwable.getLocalizedMessage());
                    currentLocation.setValue(locationData);
                })
                .subscribe();
    }

    private void formData(@NonNull PlacesPojo.Root root, ResultDistanceMatrix resultDistanceMatrix){

        List<PlaceObject> dataForTheView = new ArrayList<>();
        for(int i=0;i<root.customA.size();i++){

            if(root.customA.get(i)!=null && resultDistanceMatrix.rows.get(0).elements.get(i)!=null) {
                PlaceObject currentObject = new PlaceObject();
                currentObject.geometry = root.customA.get(i).geometry;
                currentObject.name = root.customA.get(i).name;
                currentObject.vicinity = root.customA.get(i).vicinity;

                currentObject.infoDistance = resultDistanceMatrix.rows.get(0).elements.get(i);
                dataForTheView.add(currentObject);
            }
        }

        DataWrapper<List<PlaceObject>> dataToSet = new DataWrapper<>();
        dataToSet.setData(dataForTheView);
        placesLiveData.setValue(dataToSet);

    }
}

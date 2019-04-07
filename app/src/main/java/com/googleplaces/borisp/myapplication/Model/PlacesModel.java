package com.googleplaces.borisp.myapplication.Model;

import com.googleplaces.borisp.myapplication.MainApplication;
import com.googleplaces.borisp.myapplication.View.MainActivity;
import com.googleplaces.borisp.myapplication.di.Api;
import com.googleplaces.borisp.myapplication.Data.PlacesPojo;
import com.googleplaces.borisp.myapplication.Data.ResultDistanceMatrix;

import javax.inject.Inject;

import io.reactivex.Single;

public class PlacesModel {
    Api retrofitService;

    @Inject
    public PlacesModel(Api retrofitService) {
        this.retrofitService = retrofitService;
        ((MainApplication)MainApplication.getContext()).getMyComponent().inject(this);

    }

    public Single<PlacesPojo.Root> getPlaces(String currentLocation){
        return  retrofitService.getPlaces("", "500",currentLocation, "<API_KEY>");
    }


    public Single<ResultDistanceMatrix> getDistance(String currentLocation,String destinations){
        return retrofitService.getDistance("<API_KEY>",currentLocation,destinations);
    }

}

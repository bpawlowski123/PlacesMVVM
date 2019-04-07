package com.googleplaces.borisp.myapplication.di;

import com.googleplaces.borisp.myapplication.Data.PlacesPojo;
import com.googleplaces.borisp.myapplication.Data.ResultDistanceMatrix;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface Api {
    @GET("place/nearbysearch/json?")
    Single<PlacesPojo.Root> getPlaces(@Query(value = "type") String type, @Query(value = "radius") String radius, @Query(value = "location",encoded = true) String location, @Query(value = "key") String key);

    @GET("distancematrix/json")
    Single<ResultDistanceMatrix> getDistance(@Query("key") String key, @Query("origins") String origins, @Query("destinations") String destinations);
}

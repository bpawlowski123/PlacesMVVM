package com.googleplaces.borisp.myapplication;

import android.app.Application;
import android.content.Context;

import com.googleplaces.borisp.myapplication.di.AppComponent;
import com.googleplaces.borisp.myapplication.di.DaggerAppComponent;

public class MainApplication extends Application {
    private static Context context;
    private boolean hasLocationPermission = false;
    private AppComponent mMyComponent;


    public boolean isHasLocationPermission() {
        return hasLocationPermission;
    }

    public void setHasLocationPermission(boolean hasLocationPermission) {
        this.hasLocationPermission = hasLocationPermission;
    }

    @Override
    public void onCreate() {
        context = this;
        mMyComponent = createMyComponent();
        super.onCreate();
    }

    public AppComponent getMyComponent() {
        return mMyComponent;
    }
    public static Context getContext() {
        return context;
    }

    private AppComponent createMyComponent() {
        return DaggerAppComponent
                .builder()
                .build();
    }
}

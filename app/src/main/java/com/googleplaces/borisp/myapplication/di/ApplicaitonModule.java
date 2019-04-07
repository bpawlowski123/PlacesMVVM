package com.googleplaces.borisp.myapplication.di;

import android.app.Application;

import com.googleplaces.borisp.myapplication.MainApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicaitonModule {
    MainApplication mApplication;

    public ApplicaitonModule(MainApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }
}

package com.googleplaces.borisp.myapplication.di;

import com.googleplaces.borisp.myapplication.Model.PlacesModel;
import com.googleplaces.borisp.myapplication.ViewModel.MainViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules= {ApplicaitonModule.class, NetworkModule.class} )
public interface AppComponent  {
    void inject(PlacesModel service);
    void inject(MainViewModel viewModel);
}

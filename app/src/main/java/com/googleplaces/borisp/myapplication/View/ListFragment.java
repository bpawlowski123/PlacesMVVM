package com.googleplaces.borisp.myapplication.View;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.googleplaces.borisp.myapplication.R;
import com.googleplaces.borisp.myapplication.ViewModel.MainViewModel;

import java.util.Objects;


public class ListFragment extends BaseWrappableLiveDataFragment {
    MainViewModel viewModelToWorkWith = null;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModelToWorkWith = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MainViewModel.class);
        return inflater.inflate(R.layout.fragment_list, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();

        recyclerView = getView().findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        viewModelToWorkWith.getPlacesLiveData().observe(Objects.requireNonNull(this.getActivity()), placeObjectDataWrapper -> {
            if(placeObjectDataWrapper!=null && placeObjectDataWrapper.hasError()){
                showErrorMessage(placeObjectDataWrapper.getError());
            }else{
                mAdapter = new PlacesAdapter(placeObjectDataWrapper.getData(), item -> {
                    String stringURI = "geo:" +
                            item.geometry.locationA.lat +
                            "," +
                            item.geometry.locationA.lng +
                            "?q=" +
                            item.geometry.locationA.lat +
                            "," +
                            item.geometry.locationA.lng +
                            "(" +
                            item.name +
                            ")";
                    Uri gmmIntentUri = Uri.parse(stringURI);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                });
                recyclerView.setAdapter(mAdapter);
            }});
    }
}

package com.googleplaces.borisp.myapplication.View;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.googleplaces.borisp.myapplication.Data.DataWrapper;

public class BaseWrappableLiveDataFragment extends Fragment {
    protected void showErrorMessage(String msg){
        Toast.makeText(this.getActivity(),msg,Toast.LENGTH_LONG).show();
    }
}

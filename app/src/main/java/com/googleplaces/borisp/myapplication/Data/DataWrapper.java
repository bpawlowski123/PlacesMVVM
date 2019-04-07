package com.googleplaces.borisp.myapplication.Data;

import java.util.List;

public class DataWrapper<T>{
    private T data;
    private String error;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean hasError(){
        return this.getError() != null && !this.getError().isEmpty();
    }
}
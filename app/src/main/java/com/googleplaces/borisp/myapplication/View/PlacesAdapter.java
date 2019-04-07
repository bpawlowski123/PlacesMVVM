package com.googleplaces.borisp.myapplication.View;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.googleplaces.borisp.myapplication.Data.PlaceObject;
import com.googleplaces.borisp.myapplication.R;

import java.util.List;


public class PlacesAdapter  extends RecyclerView.Adapter<PlacesAdapter.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(PlaceObject item);
    }

    private List<PlaceObject> mDataset;
    private OnItemClickListener listener;


    PlacesAdapter(List<PlaceObject> myDataset,OnItemClickListener listener) {
        this.mDataset = myDataset;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlacesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.place_list_item, parent, false);
        return  new MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(mDataset.get(position), listener);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout relativeLayout;
        TextView placeName;
        TextView distanceToPlace;
        MyViewHolder(View v) {
            super(v);
            this.relativeLayout = v.findViewById(R.id.root_view);
            this.placeName = v.findViewById(R.id.tv_place_name);
            this.distanceToPlace = v.findViewById(R.id.tv_place_distance);
        }
        void bind(final PlaceObject item, final OnItemClickListener listener) {
            placeName.setText(item.name);
            distanceToPlace.setText(item.infoDistance.distance.text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

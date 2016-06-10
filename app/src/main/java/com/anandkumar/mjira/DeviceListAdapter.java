package com.anandkumar.mjira;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Anand on 6/10/2016.
 */
public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceViewHolder>{

    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView deviceName;
        TextView deviceImei;
        ImageView personPhoto;


        DeviceViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            deviceName = (TextView)itemView.findViewById(R.id.card_deviceName);
            deviceImei = (TextView)itemView.findViewById(R.id.card_deviceIMEI);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }
    }

    List<Device> devices;

    DeviceListAdapter(List<Device> devices){
        this.devices = devices;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        DeviceViewHolder dvh = new DeviceViewHolder(v);
        return dvh;
    }
    @Override
    public int getItemCount() {
        return devices.size();
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder deviceViewHolder, int i) {
        deviceViewHolder.deviceName.setText(devices.get(i).getName());
        deviceViewHolder.deviceImei.setText(devices.get(i).getImei());
        //deviceViewHolder.personPhoto.setImageResource(persons.get(i).photoId);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
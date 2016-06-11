package com.anandkumar.mjira;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Anand on 6/11/2016.
 */
public class SearchDeviceAdapter extends RecyclerView.Adapter<SearchDeviceAdapter.DeviceViewHolder>{


    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView deviceName;
        TextView deviceImei;
        TextView deviceOwner;


        DeviceViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            deviceName = (TextView)itemView.findViewById(R.id.device_name);
            deviceImei = (TextView)itemView.findViewById(R.id.device_imei);
            deviceOwner = (TextView) itemView.findViewById(R.id.device_owner);
        }
    }

    List<Device> devices;

    SearchDeviceAdapter(List<Device> devices){
        this.devices = devices;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_search_list, viewGroup, false);
        DeviceViewHolder sdvh = new DeviceViewHolder(v);
        return sdvh;
    }
    @Override
    public int getItemCount() {
        return devices.size();
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder deviceViewHolder, int i) {
        deviceViewHolder.deviceName.setText(devices.get(i).getName());
        deviceViewHolder.deviceImei.setText(devices.get(i).getImei());
        deviceViewHolder.deviceOwner.setText(devices.get(i).getOwner());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public Device getDevice(int position){

        return (null!= devices?devices.get(position) :null);
    }

}



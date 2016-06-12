package com.anandkumar.mjira;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Anand on 6/11/2016.
 */
public class IncomingRequestAdapter extends RecyclerView.Adapter<IncomingRequestAdapter.DeviceRequestViewHolder>{

    public static class DeviceRequestViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView deviceName;
        TextView deviceImei;
        TextView fromUser;

        LinearLayout cv_linlaHeaderProgress;



        DeviceRequestViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_acceptDevice);
            deviceName = (TextView)itemView.findViewById(R.id.dName);
            deviceImei = (TextView)itemView.findViewById(R.id.dIMEI);
            fromUser=(TextView)itemView.findViewById(R.id.fromUser);
            cv_linlaHeaderProgress=(LinearLayout)itemView.findViewById(R.id.cv_linlaHeaderProgress);




        }

    }

    List<DeviceRequest> deviceRequests;

    IncomingRequestAdapter(List<DeviceRequest> deviceRequests){
        this.deviceRequests = deviceRequests;
    }

    @Override
    public DeviceRequestViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_accept_request, viewGroup, false);
        DeviceRequestViewHolder dvh = new DeviceRequestViewHolder(v);
        return dvh;
    }
    @Override
    public int getItemCount() {
        return deviceRequests.size();
    }

    @Override
    public void onBindViewHolder(DeviceRequestViewHolder deviceViewHolder, int i) {
        deviceViewHolder.deviceName.setText(deviceRequests.get(i).getName());
        deviceViewHolder.deviceImei.setText(deviceRequests.get(i).getImei());
        deviceViewHolder.fromUser.setText(deviceRequests.get(i).getFromUser());


        //deviceViewHolder.personPhoto.setImageResource(persons.get(i).photoId);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public DeviceRequest getDevice(int position){

        return (null!= deviceRequests?deviceRequests.get(position) :null);
    }

    public void delete(int position) { //removes the row
        deviceRequests.remove(position);
        notifyItemRemoved(position);
    }
}
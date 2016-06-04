package com.anandkumar.mjira;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceDetailsFragment extends Fragment {

    private TextView deviceOwner;
    private TextView deviceName;
    private TextView deviceImei;
    private Button requestButton;

    public DeviceDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_device_details, container, false);
        deviceName=(TextView)view.findViewById(R.id.dName);
        deviceOwner=(TextView)view.findViewById(R.id.dOwner);
        deviceImei=(TextView)view.findViewById(R.id.dIMEI);
        requestButton=(Button) view.findViewById(R.id.requestButton);

        Intent intent=getActivity().getIntent();
        String deviceNameString=intent.getStringExtra("name").toString();
        final String deviceOwnerString=intent.getStringExtra("toUser").toString();
        final String deviceImeiString=intent.getStringExtra("imei").toString();


        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DeviceRequest deviceRequest=new DeviceRequest();

                deviceRequest.setImei(deviceImeiString);
                deviceRequest.setToUser(deviceOwnerString);
                final String fromUser=Backendless.UserService.loggedInUser();
                Backendless.UserService.findById(fromUser, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        deviceRequest.setFromUser(response.getProperty("name").toString());

                        BackendlessDataQuery query=new BackendlessDataQuery();
                        Backendless.Persistence.save(deviceRequest, new AsyncCallback<DeviceRequest>() {
                            @Override
                            public void handleResponse(DeviceRequest response) {
                                Toast.makeText(getActivity(),"Device Request Succesfully Sent",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(getActivity(),"Device Request failed",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                    }
                });


            }
        });



        //Log.d("Request to :","Owner"+deviceOwnerString+"\t Device Name : "+deviceNameString+"\t Device IMEI: "+deviceImeiString);

        deviceName.setText(deviceNameString);
        deviceImei.setText(deviceImeiString);
        deviceOwner.setText(deviceOwnerString);

        return view;
    }

}

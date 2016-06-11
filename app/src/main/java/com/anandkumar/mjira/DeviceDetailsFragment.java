package com.anandkumar.mjira;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.DeliveryOptions;
import com.backendless.messaging.MessageStatus;
import com.backendless.messaging.PublishOptions;
import com.backendless.persistence.BackendlessDataQuery;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceDetailsFragment extends Fragment {

    private TextView deviceOwner;
    private TextView deviceName;
    private TextView deviceImei;
    private TextView requestButton;

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
        requestButton=(TextView) view.findViewById(R.id.requestButton);

        final Intent intent=getActivity().getIntent();
        final String deviceNameString=intent.getStringExtra("name").toString();
        final String currentUser=intent.getStringExtra("currentUser");
        final String deviceOwnerString=intent.getStringExtra("toUser").toString();
        final String deviceImeiString=intent.getStringExtra("imei").toString();


        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DeviceRequest deviceRequest=new DeviceRequest();
                deviceRequest.setName(deviceNameString);
                deviceRequest.setImei(deviceImeiString);
                deviceRequest.setToUser(deviceOwnerString);
                deviceRequest.setAccepted(false);


                deviceRequest.setFromUser(currentUser);





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


                BackendlessDataQuery query=new BackendlessDataQuery();
                query.setWhereClause(String.format("name= '%s'",deviceOwnerString));

                Backendless.Persistence.of(BackendlessUser.class).find(query, new AsyncCallback<BackendlessCollection<BackendlessUser>>() {
                    @Override
                    public void handleResponse(BackendlessCollection<BackendlessUser> response) {
                        List<BackendlessUser> users=response.getData();

                        Toast.makeText(getActivity()," Query result length : \t"+users.size(),Toast.LENGTH_SHORT).show();
                        for(BackendlessUser user:users){
                            final String deviceId=user.getProperty("device").toString();


                            Toast.makeText(getActivity(),"Push message sent to "+deviceId,Toast.LENGTH_SHORT).show();

                            DeliveryOptions deliveryOptions = new DeliveryOptions();
                            deliveryOptions.addPushSinglecast( deviceId );

                            PublishOptions publishOptions = new PublishOptions();
                            publishOptions.putHeader( "android-ticker-text", "You just got a Device Request!" );
                            publishOptions.putHeader( "android-content-title", "From "+ currentUser );
                            publishOptions.putHeader( "android-content-text", "Reg:" +deviceNameString+"::"+deviceImeiString);

                            Backendless.Messaging.publish("this is a private message!", publishOptions, deliveryOptions, new AsyncCallback<MessageStatus>() {
                                @Override
                                public void handleResponse(MessageStatus response) {
                                    Toast.makeText(getActivity(),"Push message sent to "+deviceId,Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(getActivity(),SearchDeviceActivity.class);
                                    startActivity(intent);

                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    Toast.makeText(getActivity(),"Push message sending failed",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
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

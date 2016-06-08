package com.anandkumar.mjira;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class IncomingRequestFragment extends Fragment {

    private ArrayList<String> fromUsers;
    private ArrayList<DeviceRequest> deviceRequests;
    private ArrayAdapter<String> adapter;
    Intent intent;

    public IncomingRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_incoming_request, container, false);


        intent=getActivity().getIntent();
        String currentUser=intent.getStringExtra("currentUser");

        fromUsers=new ArrayList<String>();
        deviceRequests=new ArrayList<DeviceRequest>();
        adapter=new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                fromUsers
        );

        ListView deviceRequests=(ListView)view.findViewById(R.id.deviceRequests);
        deviceRequests.setAdapter(adapter);

        deviceRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showAlertDialog(position);
            }
        });

        //Get name of the current loggedin user




        getIncomingFriendRequests(currentUser);




        return view;
    }


    private void showAlertDialog(final int position) {

        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
        dialog.setMessage("Accept Device Request from "+ fromUsers.get(position)+ " ?");

        dialog.setNegativeButton("Not Now", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                acceptRequest(deviceRequests.get(position));
            }
        });

        dialog.create();
        dialog.show();
    }

    private void acceptRequest(final DeviceRequest dRequest) {

        dRequest.setAccepted(true);
        Backendless.Persistence.save(dRequest, new AsyncCallback<DeviceRequest>() {
            @Override
            public void handleResponse(DeviceRequest response) {
                BackendlessDataQuery query=new BackendlessDataQuery();
                query.setWhereClause(String.format("imei= '%s'",dRequest.getImei()));

                Backendless.Persistence.of(Device.class).find(query, new AsyncCallback<BackendlessCollection<Device>>() {
                    @Override
                    public void handleResponse(BackendlessCollection<Device> response) {
                        List<Device> devices=response.getData();

                        for(Device device:devices){
                            device.setOwner(dRequest.getFromUser());
                            Backendless.Persistence.save(device, new AsyncCallback<Device>() {
                                @Override
                                public void handleResponse(Device response) {
                                    Toast.makeText(getActivity(),"Device Request Accepted Successfully",Toast.LENGTH_SHORT).show();



                                    BackendlessDataQuery query=new BackendlessDataQuery();
                                    query.setWhereClause(String.format("name= '%s'",dRequest.getFromUser()));

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
                                                publishOptions.putHeader( "android-ticker-text", "Your Device Request is Accepted!" );
                                                publishOptions.putHeader( "android-content-title","" );
                                                publishOptions.putHeader( "android-content-text", "Reg:" +user.getProperty("imei"));

                                                Backendless.Messaging.publish("this is a private message!", publishOptions, deliveryOptions, new AsyncCallback<MessageStatus>() {
                                                    @Override
                                                    public void handleResponse(MessageStatus response) {
                                                        Toast.makeText(getActivity(),"Push message sent to "+deviceId,Toast.LENGTH_SHORT).show();
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

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    Toast.makeText(getActivity(),"Device Request Failed",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }


                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }

    private void getIncomingFriendRequests(String currentUserName) {

        BackendlessDataQuery query=new BackendlessDataQuery();
        query.setWhereClause(String.format("toUser= '%s' and accepted= '%s'",currentUserName,false));

        Backendless.Persistence.of(DeviceRequest.class).find(query, new AsyncCallback<BackendlessCollection<DeviceRequest>>() {
            @Override
            public void handleResponse(BackendlessCollection<DeviceRequest> response) {
                List<DeviceRequest> incomingRequests=response.getData();
                for(DeviceRequest request:incomingRequests ){
                    fromUsers.add(request.getFromUser()+"::"+request.getImei());
                    deviceRequests.add(request);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }

}

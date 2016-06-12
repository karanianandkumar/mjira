package com.anandkumar.mjira;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class IncomingRequestFragment extends Fragment {



    private RecyclerView recyclerView;
    private LinearLayout linlaHeaderProgress;
    private LinearLayout cv_linlaHeaderProgress;
    private String currentUser;
    DeviceRequest clickedDevice;
    private int clickedPosition;
    IncomingRequestAdapter adapter;


    Intent intent;

    public IncomingRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_incoming_request, container, false);

        recyclerView=(RecyclerView)view.findViewById(R.id.rv_deviceList);
        recyclerView.setHasFixedSize(true);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        linlaHeaderProgress = (LinearLayout)view.findViewById(R.id.linlaHeaderProgress);
        linlaHeaderProgress.setVisibility(View.VISIBLE);

        intent=getActivity().getIntent();
        currentUser=intent.getStringExtra("currentUser");


        recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity(), recyclerView, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                clickedDevice=adapter.getDevice(position);
                clickedPosition=position;
                showAlertDialog(position);

            }

            @Override
            public void onItemLongClick(View view, int position) {
                //Toast.makeText(MainActivity.this,"Long Press",Toast.LENGTH_SHORT).show();

            }
        }));

/*
        deviceRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showAlertDialog(position);
            }
        });*/

        //Get name of the current loggedin user




        getIncomingFriendRequests(currentUser);




        return view;
    }


    private void showAlertDialog(final int position) {

        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
        dialog.setMessage("Accept Device Request from "+ clickedDevice.getFromUser()+ " ?");

        dialog.setNegativeButton("Not Now", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                acceptRequest(clickedDevice);
            }
        });

        dialog.create();
        dialog.show();
    }

    private void acceptRequest(final DeviceRequest dRequest) {

        cv_linlaHeaderProgress = (LinearLayout)getView().findViewById(R.id.cv_linlaHeaderProgress);
        cv_linlaHeaderProgress.setVisibility(View.VISIBLE);

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
                                public void handleResponse(Device deviceResponse) {
                                    Toast.makeText(getActivity(),"Device Request Accepted Successfully",Toast.LENGTH_SHORT).show();

                                    final String dName=deviceResponse.getName();
                                    final String dImei=deviceResponse.getImei();

                                    BackendlessDataQuery query=new BackendlessDataQuery();
                                    query.setWhereClause(String.format("name= '%s'",dRequest.getFromUser()));

                                    Backendless.Persistence.of(BackendlessUser.class).find(query, new AsyncCallback<BackendlessCollection<BackendlessUser>>() {
                                        @Override
                                        public void handleResponse(BackendlessCollection<BackendlessUser> response) {
                                            List<BackendlessUser> users=response.getData();

                                            Toast.makeText(getActivity()," Query result length : \t"+users.size(),Toast.LENGTH_SHORT).show();
                                            for(BackendlessUser user:users){
                                                final String deviceId=user.getProperty("device").toString();


                                                Preferences saveData=new Preferences();

                                                String uname=saveData.readString(getActivity().getApplicationContext(),saveData.USER_NAME,null);



                                                Toast.makeText(getActivity(),"Push message sent to "+deviceId,Toast.LENGTH_SHORT).show();

                                                DeliveryOptions deliveryOptions = new DeliveryOptions();
                                                deliveryOptions.addPushSinglecast( deviceId );

                                                PublishOptions publishOptions = new PublishOptions();
                                                publishOptions.putHeader( "android-ticker-text", "Your Device Request is Accepted!" );
                                                publishOptions.putHeader( "android-content-title","Your Device Request is accepted by "+ uname);
                                                publishOptions.putHeader( "android-content-text", "Reg:" + dName+"::"+dImei);

                                                Backendless.Messaging.publish("this is a private message!", publishOptions, deliveryOptions, new AsyncCallback<MessageStatus>() {
                                                    @Override
                                                    public void handleResponse(MessageStatus response) {
                                                        Toast.makeText(getActivity(),"Push message sent to "+deviceId,Toast.LENGTH_SHORT).show();

                                                        cv_linlaHeaderProgress.setVisibility(View.GONE);
                                                        adapter.delete(clickedPosition);
                                                        //getIncomingFriendRequests(currentUser);
                                                    }

                                                    @Override
                                                    public void handleFault(BackendlessFault fault) {
                                                        cv_linlaHeaderProgress.setVisibility(View.GONE);
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

                int size=incomingRequests.size();
                Log.d("No.of Devices:\t", ": " +size );
                if(size==0){
                    Toast.makeText(getActivity(), "No Devices!!", Toast.LENGTH_SHORT).show();

                }else {
                    adapter = new IncomingRequestAdapter(incomingRequests);
                    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
                    alphaAdapter.setDuration(1000);
                    recyclerView.setAdapter(alphaAdapter);

                }

                linlaHeaderProgress.setVisibility(View.GONE);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                linlaHeaderProgress.setVisibility(View.GONE);
            }
        });
    }

}

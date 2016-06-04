package com.anandkumar.mjira;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceListFragment extends Fragment {

    private ArrayList<String> deviceList;
    private ListView deviceListView;


    private ArrayAdapter<String> deviceListAdapter;

    private LinearLayout linlaHeaderProgress;

    public DeviceListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_device_list, container, false);


        linlaHeaderProgress = (LinearLayout)view.findViewById(R.id.linlaHeaderProgress);
        linlaHeaderProgress.setVisibility(View.VISIBLE);


        deviceListView=(ListView)view.findViewById(R.id.deviceList);

        deviceList=new ArrayList<String>();

        deviceListAdapter=new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                deviceList);
        deviceListView.setAdapter(deviceListAdapter);

         String currentUser= Backendless.UserService.loggedInUser();
        Backendless.UserService.findById(currentUser, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser user) {



                String userName=user.getProperty("name").toString();
                getDeviceList(userName);
                linlaHeaderProgress.setVisibility(View.GONE);
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });

        return view;
    }

    private void getDeviceList(String user) {

        BackendlessDataQuery query=new BackendlessDataQuery();
        query.setWhereClause(String.format("owner= '%s'",user));

        Backendless.Persistence.of(Device.class).find(query, new AsyncCallback<BackendlessCollection<Device>>() {
            @Override
            public void handleResponse(BackendlessCollection<Device> response) {
                List<Device> dList=response.getData();

                for(Device devicelist:dList){
                    deviceList.add(devicelist.getImei());
                    Log.d("IMEI","  is: "+devicelist.getImei());
                }
                deviceListAdapter.notifyDataSetChanged();
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }

}

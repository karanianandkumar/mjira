package com.anandkumar.mjira;


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
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceListFragment extends Fragment {

   /* private ArrayList<String> deviceList;
    private ListView deviceListView;*/

    private RecyclerView recyclerView;
    private DeviceListAdapter adapter;

   // private ArrayAdapter<String> deviceListAdapter;

    private LinearLayout linlaHeaderProgress;

    public DeviceListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_device_list, container, false);


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

        Intent intent=getActivity().getIntent();
        String currentUser=intent.getStringExtra("currentUser");



        getDeviceList(currentUser);




        return view;
    }

    private void getDeviceList(String user) {

        BackendlessDataQuery query=new BackendlessDataQuery();
        query.setWhereClause(String.format("owner= '%s'",user));

        QueryOptions options=new QueryOptions();
        options.addSortByOption("name ASC");
        query.setQueryOptions(options);

        Backendless.Persistence.of(Device.class).find(query, new AsyncCallback<BackendlessCollection<Device>>() {
            @Override
            public void handleResponse(BackendlessCollection<Device> response) {
                List<Device> dList=response.getData();

                int size=dList.size();
                Log.d("No.of Devices:\t", ": " +size );
                if(size==0){
                    Toast.makeText(getActivity(), "No Devices!!", Toast.LENGTH_SHORT).show();


                }else {
                     adapter = new DeviceListAdapter(dList);
                    recyclerView.setAdapter(adapter);
                }


                linlaHeaderProgress.setVisibility(View.GONE);
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }

}

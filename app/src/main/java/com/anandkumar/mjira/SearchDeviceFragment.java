package com.anandkumar.mjira;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchDeviceFragment extends Fragment {

    private Button searchButton;
    private EditText searchET;
    private ListView searchResults;
    private ArrayList<String> queryResult;
    private ArrayAdapter<String> queryResultAdapter;

    public SearchDeviceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_search_device, container, false);

        queryResult=new ArrayList<String>();
        queryResultAdapter=new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                queryResult
        );

        searchButton=(Button)view.findViewById(R.id.searchButton);
        searchET=(EditText)view.findViewById(R.id.searchET);
        searchResults=(ListView)view.findViewById(R.id.searchResults);

        searchResults.setAdapter(queryResultAdapter);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryResult.clear();
                String device=searchET.getText().toString();
                searchForDevice(device);
            }
        });
        return view;
    }

    private void searchForDevice(String device) {

        BackendlessDataQuery query=new BackendlessDataQuery();
        query.setWhereClause(String.format("name= '%s'",device));

        Backendless.Persistence.of(Device.class).find(query, new AsyncCallback<BackendlessCollection<Device>>() {
            @Override
            public void handleResponse(BackendlessCollection<Device> response) {
                List<Device> resultData=response.getData();
                Log.d("No.of Devices:\t",": "+resultData.size());
                if(resultData.size()!=0){
                    for(Device resultDevice:resultData){
                        queryResult.add(resultDevice.getOwner()+"::"+resultDevice.getImei() );
                    }

                    queryResultAdapter.notifyDataSetChanged();


                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(getActivity(),"No Devices!!",Toast.LENGTH_SHORT).show();
            }
        });
    }

}

package com.anandkumar.mjira;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    private ArrayList<Device> queryDeviceList;
    private ArrayAdapter<String> queryResultAdapter;
    Intent intent;

    public SearchDeviceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_search_device, container, false);

        queryResult=new ArrayList<String>();
        queryDeviceList=new ArrayList<Device>();
        queryResultAdapter=new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                queryResult
        );

        Intent intent1=getActivity().getIntent();
        final String currentUser=intent1.getStringExtra("currentUser");


        searchButton=(Button)view.findViewById(R.id.searchButton);
        searchET=(EditText)view.findViewById(R.id.searchET);
        searchResults=(ListView)view.findViewById(R.id.searchResults);

        searchResults.setAdapter(queryResultAdapter);

        searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent=new Intent(getActivity(),DeviceDetails.class);
                intent.putExtra("toUser",queryDeviceList.get(position).getOwner());
                intent.putExtra("name",queryDeviceList.get(position).getName());
                intent.putExtra("imei",queryDeviceList.get(position).getImei());
                intent.putExtra("currentUser",currentUser);

                startActivity(intent);
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryResult.clear();
                String device=searchET.getText().toString();
                searchForDevice(device,currentUser);
            }
        });
        return view;
    }

    private void searchForDevice(final String device,String user) {









        BackendlessDataQuery query=new BackendlessDataQuery();
        query.setWhereClause(String.format("name LIKE '%s' and owner!= '%s' ",device+"%",user));

        Backendless.Persistence.of(Device.class).find(query, new AsyncCallback<BackendlessCollection<Device>>() {
            @Override
            public void handleResponse(BackendlessCollection<Device> response) {
                List<Device> resultData=response.getData();
                Log.d("No.of Devices:\t",": "+resultData.size());
                if(resultData.size()!=0){
                    for(Device resultDevice:resultData){
                        queryResult.add(resultDevice.getOwner()+"::"+resultDevice.getImei() );
                        queryDeviceList.add(resultDevice);
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

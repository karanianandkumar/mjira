package com.anandkumar.mjira;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchDeviceFragment extends Fragment {

    private Button searchButton;
    private EditText searchET;
    private ListView searchResults;
    private SearchDeviceAdapter adapter;

    Intent intent;

    private RecyclerView recyclerView;

    private LinearLayout linlaHeaderProgress;
    private LinearLayoutManager llm;

    private final String LIST_STATE_KEY = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    Parcelable mListState;


    public SearchDeviceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_search_device, container, false);

        recyclerView=(RecyclerView)view.findViewById(R.id.rv_searchList);
        recyclerView.setHasFixedSize(true);


        llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);





        Preferences saveData=new Preferences();
        final String currentUser=saveData.readString(getActivity().getApplicationContext(),saveData.USER_NAME,null);


        searchButton=(Button)view.findViewById(R.id.searchButton);
        searchET=(EditText)view.findViewById(R.id.searchET);




        recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity(), recyclerView, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                intent=new Intent(getActivity(),DeviceDetails.class);
                Device clickedDevice=adapter.getDevice(position);
                intent.putExtra("toUser",clickedDevice.getOwner());
                intent.putExtra("name",clickedDevice.getName());
                intent.putExtra("imei",clickedDevice.getImei());
                intent.putExtra("currentUser",currentUser);

                startActivity(intent);

            }

            @Override
            public void onItemLongClick(View view, int position) {
                //Toast.makeText(MainActivity.this,"Long Press",Toast.LENGTH_SHORT).show();

            }
        }));


                searchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // queryResult.clear();
                        String device = searchET.getText().toString();
                        searchForDevice(device, currentUser);
                        linlaHeaderProgress = (LinearLayout)view.findViewById(R.id.linlaHeaderProgress);
                        linlaHeaderProgress.setVisibility(View.VISIBLE);
                    }
                });
                return view;
            }

            private void searchForDevice(final String device, String user) {


                BackendlessDataQuery query = new BackendlessDataQuery();
                query.setWhereClause(String.format("name LIKE '%s' and owner!= '%s' ", device + "%", user));

                QueryOptions options = new QueryOptions();
                options.addSortByOption("name ASC");
                query.setQueryOptions(options);

                Backendless.Persistence.of(Device.class).find(query, new AsyncCallback<BackendlessCollection<Device>>() {
                    @Override
                    public void handleResponse(BackendlessCollection<Device> response) {
                        List<Device> resultData = response.getData();
                        int size=resultData.size();
                        Log.d("No.of Devices:\t", ": " +size );
                        if(size==0){
                            Toast.makeText(getActivity(), "No Devices!!", Toast.LENGTH_SHORT).show();


                        }else {
                             adapter = new SearchDeviceAdapter(resultData);

                            AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
                            alphaAdapter.setDuration(1000);
                            recyclerView.setAdapter(alphaAdapter);

                        }
                        linlaHeaderProgress.setVisibility(View.GONE);

                    }


                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(getActivity(),fault.getMessage(), Toast.LENGTH_SHORT).show();
                        linlaHeaderProgress.setVisibility(View.GONE);
                    }
                });

            }



}



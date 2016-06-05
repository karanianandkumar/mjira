package com.anandkumar.mjira;


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
import com.backendless.persistence.BackendlessDataQuery;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoggedInFragment extends Fragment {


    private ListView loggedInLV;
    private ArrayAdapter<String> loggedAdapter;
    public LoggedInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_logged_in, container, false);
        String [] afterLogin={
                "Devices",
                "Logout",
                "Add Device",
                "Search",
                "Device Requests"
        };
        loggedInLV=(ListView) view.findViewById(R.id.loggedInpage);
        loggedAdapter=new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                afterLogin
                );
        loggedInLV.setAdapter(loggedAdapter);
        loggedInLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                     Intent intent=new Intent(getActivity(),DeviceListActivity.class);
                    startActivity(intent);
                }else if(position==1){

                    Backendless.UserService.logout(new AsyncCallback<Void>() {
                        @Override
                        public void handleResponse(Void response) {
                            Toast.makeText(getActivity(),"Successfully Logged out",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getActivity(),MainActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(getActivity(),"Failed to LogOut",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else if(position==2){
                    Intent intent=new Intent(getActivity(),AddDeviceActivity.class);
                    startActivity(intent);
                }else if(position==3){
                    Intent intent=new Intent(getActivity(),SearchDeviceActivity.class);
                    startActivity(intent);
                }else if(position==4){
                    Intent intent=new Intent(getActivity(),IncomingRequestActivity.class);
                    startActivity(intent);
                }else if(position==5){
//                    Intent intent=new Intent(getActivity(),InboxActivity.class);
//                    startActivity(intent);
                }
            }
        });
        upDateLoginDevice();
        return  view;
    }

    private void upDateLoginDevice() {


        final SaveData saveData=new SaveData(getActivity());
        final String loginDeviceId=saveData.getDeviceId();

        Toast.makeText(getActivity(),"Login device ID is: \t "+loginDeviceId,Toast.LENGTH_SHORT).show();


        BackendlessDataQuery query=new BackendlessDataQuery();
        //Toast.makeText(getActivity(),"Current User name:\t"+saveData.getCurrentUser(),Toast.LENGTH_SHORT).show();

        query.setWhereClause(String.format("name= '%s'",saveData.getCurrentUser()));
        Backendless.Persistence.of(BackendlessUser.class).find(query, new AsyncCallback<BackendlessCollection<BackendlessUser>>() {
            @Override
            public void handleResponse(BackendlessCollection<BackendlessUser> response) {
                List<BackendlessUser> users=response.getData();
                Toast.makeText(getActivity(),"The Users Size"+users.size(),Toast.LENGTH_SHORT).show();

                String cUser=saveData.getCurrentUser();
                String dID=saveData.getDeviceId();
                Toast.makeText(getActivity(),"User Name is :\t "+cUser+" and device ID is: \t "+dID,Toast.LENGTH_SHORT).show();


                for(BackendlessUser user:users) {
                    String storedId = user.getProperty("deviceID").toString();
                    if (loginDeviceId != storedId) {


                        Toast.makeText(getActivity(),"User Name is :\t "+cUser+" and device ID is: \t "+dID,Toast.LENGTH_SHORT).show();
                        user.setProperty("deviceID", loginDeviceId);
                        Backendless.Persistence.save(user, new AsyncCallback<BackendlessUser>() {
                            @Override
                            public void handleResponse(BackendlessUser response) {
                                Toast.makeText(getActivity(), "The Device ID updated", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {

                            }
                        });
                    }
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });




    }


}


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
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoggedInFragment extends Fragment {


    private ListView loggedInLV;
    private ArrayAdapter<String> loggedAdapter;

    String Currentuser=null;

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

         String user= Backendless.UserService.loggedInUser();


        Backendless.UserService.findById(user, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {

               Currentuser =response.getProperty("name").toString();
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });

        Toast.makeText(getActivity(),"Current User is :\t"+ Currentuser,Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(),"Device Id  is :\t"+ new SaveData(getActivity().getApplicationContext()).getDeviceId(),Toast.LENGTH_SHORT).show();


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
                    intent.putExtra("currentUser",Currentuser);
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
                    intent.putExtra("currentUser",Currentuser);
                    startActivity(intent);
                }else if(position==3){
                    Intent intent=new Intent(getActivity(),SearchDeviceActivity.class);
                    intent.putExtra("currentUser",Currentuser);
                    startActivity(intent);
                }else if(position==4){
                    Intent intent=new Intent(getActivity(),IncomingRequestActivity.class);
                    intent.putExtra("currentUser",Currentuser);
                    startActivity(intent);
                }else if(position==5){
//                    Intent intent=new Intent(getActivity(),InboxActivity.class);
//                    startActivity(intent);
                }
            }
        });

        return  view;
    }

    }






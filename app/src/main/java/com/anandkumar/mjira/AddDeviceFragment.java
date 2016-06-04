package com.anandkumar.mjira;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddDeviceFragment extends Fragment {

    private EditText dName;
    private EditText dIMEI;
    private Button addButton;

    public AddDeviceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_device, container, false);
        dName=(EditText)view.findViewById(R.id.deviceName);
        dIMEI=(EditText)view.findViewById(R.id.deviceIMEI);
        addButton=(Button)view.findViewById(R.id.addDevice);
        final SaveData saveData=new SaveData(getActivity());

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Device device=new Device();
                device.setOwner(saveData.getCurrentUser());
                device.setName(dName.getText().toString());
                device.setImei(dIMEI.getText().toString());
                addNewDetails(device);

            }
        });

        return view;
    }



    private void addNewDetails(Device device) {
        Backendless.Persistence.save(device, new AsyncCallback<Device>() {
            @Override
            public void handleResponse(Device response) {
                Toast.makeText(getActivity(),"Device Added Successfully",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(getActivity(),"Failed to add Device",Toast.LENGTH_SHORT).show();
            }
        });
    }

}

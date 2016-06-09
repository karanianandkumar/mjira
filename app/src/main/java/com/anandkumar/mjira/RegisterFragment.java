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
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {


    Button registerButton;
    EditText usernameField;
    EditText passwordField;
    private EditText mailField;
    private String deviceID;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_register, container, false);

        usernameField=(EditText)view.findViewById(R.id.userNameField);
        passwordField=(EditText)view.findViewById(R.id.passwordField);
        mailField=(EditText)view.findViewById(R.id.loginMailField);
        final Preferences saveData=new Preferences();

        String deviceId_pref=saveData.readString(getActivity().getApplicationContext(),saveData.DEVICE_ID,null);

        Toast.makeText(getActivity(), "The register Id  is(4):\t" +deviceId_pref , Toast.LENGTH_SHORT).show();

        registerButton=(Button)view.findViewById(R.id.register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username=usernameField.getText().toString();
                String password=passwordField.getText().toString();
                String mail=mailField.getText().toString();

                BackendlessUser backendlessUser=new BackendlessUser();
                backendlessUser.setPassword(password);
                backendlessUser.setProperty("name",username);
                backendlessUser.setEmail(mail);

                backendlessUser.setProperty("device",saveData.readString(getActivity().getApplicationContext(),saveData.DEVICE_ID,null));

                Toast.makeText(getActivity(), "The register Id  is(5):\t" + saveData.readString(getActivity().getApplicationContext(),saveData.DEVICE_ID,null), Toast.LENGTH_SHORT).show();






                Backendless.UserService.register(backendlessUser, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {

                        /*SaveData saveData1=new SaveData(getActivity().getApplicationContext());
                        saveData1.setCurrentUser(response.getProperty("name").toString());
                        saveData1.setDeviceId(response.getProperty("device").toString());
                        Toast.makeText(getActivity(),"Successfully Register",Toast.LENGTH_SHORT).show();



                        Toast.makeText(getActivity(),"User Name is :\t "+saveData.getCurrentUser()+" device ID is: \t "+saveData.getDeviceId(),Toast.LENGTH_SHORT).show();

                        Toast.makeText(getActivity(),"User Name is :\t "+saveData1.getCurrentUser()+" device ID is: \t "+saveData1.getDeviceId(),Toast.LENGTH_SHORT).show();*/
                        Toast.makeText(getActivity(),"Successfully Registered...",Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(getActivity(),MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(getActivity(),"User not Registered",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });





        return view;
    }

}

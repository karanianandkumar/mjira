package com.anandkumar.mjira;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.backendless.Backendless;
import com.backendless.DeviceRegistration;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class MainActivity extends AppCompatActivity {

    public static final String APP_ID="3C71DBB6-F08E-7898-FF78-18A0BF6B4900";
    public static final String SECRET_KEY="A704FC51-2383-ED58-FF2D-E09181A44D00";
    public static final String VERSION="v1";
    public static final String GCMSENDER_ID="1003552598634";
    private String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Backendless.initApp(this, APP_ID, SECRET_KEY, VERSION);

        final Preferences saveData = new Preferences();

        final String deviceId = saveData.readString(getApplicationContext(),saveData.DEVICE_ID,null);


        Backendless.Messaging.registerDevice(GCMSENDER_ID);

        if(deviceId==null) {
            Backendless.Messaging.getDeviceRegistration(new AsyncCallback<DeviceRegistration>() {
                @Override
                public void handleResponse(DeviceRegistration response) {

                    saveData.writeString(getApplicationContext(),saveData.DEVICE_ID,response.getDeviceId());;
                }

                @Override
                public void handleFault(BackendlessFault fault) {

                }
            });

        }
       

            if (Backendless.UserService.loggedInUser() == "") {
                MenuFragment menuFragment = new MenuFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.container, menuFragment).commit();
            } else {

                Intent intent = new Intent(MainActivity.this, LoggedInActivity.class);
                startActivity(intent);
            }
        }
    }


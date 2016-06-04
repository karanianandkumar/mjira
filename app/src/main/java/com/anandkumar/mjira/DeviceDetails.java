package com.anandkumar.mjira;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DeviceDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);

        DeviceDetailsFragment deviceDetailsFragment=new DeviceDetailsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.deviceDetailsContainer,deviceDetailsFragment).commit();
    }
}

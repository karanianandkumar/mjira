package com.anandkumar.mjira;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddDeviceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        AddDeviceFragment addDeviceFragment=new AddDeviceFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.addDeviceContainer,addDeviceFragment).commit();
    }
}

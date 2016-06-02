package com.anandkumar.mjira;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DeviceListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        DeviceListFragment deviceListFragment=new DeviceListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.deviceListContainer,deviceListFragment).commit();
    }
}

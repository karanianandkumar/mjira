package com.anandkumar.mjira;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SearchDeviceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_device);

        SearchDeviceFragment searchDeviceFragment=new SearchDeviceFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.searchDeviceContainer,searchDeviceFragment).commit();
    }
}

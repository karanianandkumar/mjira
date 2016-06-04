package com.anandkumar.mjira;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class IncomingRequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_request);


        IncomingRequestFragment incomingRequestFragment=new IncomingRequestFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.incomingRequestContainer,incomingRequestFragment).commit();
    }
}

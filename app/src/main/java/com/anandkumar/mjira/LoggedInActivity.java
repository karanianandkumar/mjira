package com.anandkumar.mjira;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class LoggedInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        LoggedInFragment loggedInFragment=new LoggedInFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.loggedInContainer,loggedInFragment).commit();

    }

}

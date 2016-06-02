package com.anandkumar.mjira;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RegisterFragment registerFragment=new RegisterFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.registerContainer,registerFragment).commit();
    }
}

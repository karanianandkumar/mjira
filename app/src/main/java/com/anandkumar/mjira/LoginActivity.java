package com.anandkumar.mjira;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginFragment loginFragment=new LoginFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.loginContainer,loginFragment).commit();
    }
}

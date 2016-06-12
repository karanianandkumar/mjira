package com.anandkumar.mjira;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        MenuFragment menuFragment=new MenuFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.menuContainer,menuFragment).commit();
    }
}

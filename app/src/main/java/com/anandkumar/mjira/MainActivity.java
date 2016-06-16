package com.anandkumar.mjira;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.backendless.Backendless;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);







        ConnectionDetector connectionDetector = new ConnectionDetector(getApplicationContext());
        boolean connection = connectionDetector.isConnectingToInternet();

        if (connection == true) {



            if (Backendless.UserService.loggedInUser() == "") {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {

                Intent intent = new Intent(MainActivity.this, LoggedInActivity.class);
                startActivity(intent);
            }
        }else{


            Toast.makeText(this,"Please check Intenet connection",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);



        }

    }
}

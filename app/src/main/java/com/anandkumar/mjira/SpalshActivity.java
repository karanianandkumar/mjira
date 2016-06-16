package com.anandkumar.mjira;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.DeviceRegistration;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class SpalshActivity extends Activity {

    public static final String APP_ID="3C71DBB6-F08E-7898-FF78-18A0BF6B4900";
    public static final String SECRET_KEY="A704FC51-2383-ED58-FF2D-E09181A44D00";
    public static final String VERSION="v1";

    public static final String GCMSENDER_ID="1003552598634";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_spalsh);

        Backendless.initApp(this, APP_ID, SECRET_KEY, VERSION);

        final Preferences saveData = new Preferences();

        final String deviceId = saveData.readString(getApplicationContext(), saveData.DEVICE_ID, null);

        ConnectionDetector connectionDetector = new ConnectionDetector(getApplicationContext());
        boolean connection = connectionDetector.isConnectingToInternet();

        if(connection==true){
            Backendless.Messaging.registerDevice(GCMSENDER_ID);

            if (deviceId == null) {
                Backendless.Messaging.getDeviceRegistration(new AsyncCallback<DeviceRegistration>() {
                    @Override
                    public void handleResponse(DeviceRegistration response) {

                        saveData.writeString(getApplicationContext(), saveData.DEVICE_ID, response.getDeviceId());
                        Intent intent=new Intent(SpalshActivity.this,MainActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                    }
                });

            }else{
                Intent intent=new Intent(SpalshActivity.this,MainActivity.class);
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

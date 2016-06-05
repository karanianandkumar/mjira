package com.anandkumar.mjira;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.backendless.Backendless;
import com.backendless.DeviceRegistration;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class MainActivity extends AppCompatActivity {

    public static final String APP_ID="3C71DBB6-F08E-7898-FF78-18A0BF6B4900";
    public static final String SECRET_KEY="A704FC51-2383-ED58-FF2D-E09181A44D00";
    public static final String VERSION="v1";
    public static final String GCMSENDER_ID="1003552598634";
    private String data=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Backendless.initApp(this,APP_ID,SECRET_KEY,VERSION);

        final SaveData saveData=new SaveData(getApplicationContext());

        String deviceId=saveData.getDeviceId();

        //Toast.makeText(MainActivity.this,"The register Id of Device is:\t"+deviceId,Toast.LENGTH_SHORT).show();


        if(deviceId==null) {
        Backendless.Messaging.registerDevice(GCMSENDER_ID, new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void response) {
                //Toast.makeText(MainActivity.this,"The register Id of Device is stored successfully",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });




            Backendless.Messaging.getDeviceRegistration(new AsyncCallback<DeviceRegistration>() {
                @Override
                public void handleResponse(DeviceRegistration response) {
                    data = response.getDeviceId();
                    saveData.setDeviceId(data);
                    //Toast.makeText(MainActivity.this,"The register Id of Device is:\t"+saveData.getDeviceId(),Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MainActivity.this,"The register Id of Device is:\t"+data,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void handleFault(BackendlessFault fault) {

                }
            });
        }


//
//        DeliveryOptions deliveryOptions = new DeliveryOptions();
//        deliveryOptions.addPushSinglecast( data );
//
//        PublishOptions publishOptions = new PublishOptions();
//        publishOptions.putHeader( "android-ticker-text", "You just got a private push notification!" );
//        publishOptions.putHeader( "android-content-title", "This is a notification title" );
//        publishOptions.putHeader( "android-content-text", "Push Notifications are cool" );
//
//         Backendless.Messaging.publish("this is a private message!", publishOptions, deliveryOptions, new AsyncCallback<MessageStatus>() {
//            @Override
//            public void handleResponse(MessageStatus response) {
//                Toast.makeText(MainActivity.this,"Push message sent to "+data,Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void handleFault(BackendlessFault fault) {
//                Toast.makeText(MainActivity.this,"Push message sending failed",Toast.LENGTH_SHORT).show();
//            }
//        });



       if(Backendless.UserService.loggedInUser()==""){
           MenuFragment menuFragment=new MenuFragment();
           getSupportFragmentManager().beginTransaction().add(R.id.container,menuFragment).commit();
        }else{
//           LoggedInFragment loggedInFragment=new LoggedInFragment();
//           getSupportFragmentManager().beginTransaction().add(R.id.container,loggedInFragment).commit();
           Intent intent=new Intent(MainActivity.this,LoggedInActivity.class);
           startActivity(intent);
       }
    }
}

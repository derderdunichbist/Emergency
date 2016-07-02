package org.hsrt.mc.emergency.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.hsrt.mc.emergency.R;
import org.hsrt.mc.emergency.gps.GPS;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreas Mueller on 18.06.2016.
        */
public class FirstRunActivity extends AppCompatActivity
{

    private Button continueBt;
    private GPS gps;

    public static final int PERMISSIONS = 1;



    /*
     * This function will recognize if the application runs for the first time. If this is the case a initialization
     * screen will launch as the first activity and ask for the required permissions. If it's not the first run
     * tbe main activity will be shown otherwise.
     * Includes a Actionlistener for the Continue Button to get to main activity when the permissions are granted.
    */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_run);

        continueBt = (Button) findViewById(R.id.continueButton);

        Boolean isFirstTime;

        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(FirstRunActivity.this);

        final SharedPreferences.Editor editor = app_preferences.edit();

        isFirstTime = app_preferences.getBoolean("isFirstTime", true);

        if (!isFirstTime) {

        Intent main = new Intent(FirstRunActivity.this, MainActivity.class);
        startActivity(main);
        finish();
        }

        continueBt.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            { // Actionlistener, launches the main activity after the permissions are granted.


                if(grantPermissions() && isGPSenabled())
                {
                    editor.putBoolean("isFirstTime", false);
                    editor.commit();
                    Intent vpa = new Intent (FirstRunActivity.this, MainActivity.class);
                    startActivity(vpa);

                }else if(!(grantPermissions()))
                {
                    Toast.makeText( getApplicationContext(), R.string.must_allow_permissions, Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    /*
     * instantiate the gps class with the context of this class and checks the canGetLocation() which returns falls when the gps service is disabled
     * and will show the settings dialog so the user can enable gps.
    */
    private boolean isGPSenabled()
    {
        gps = new GPS(this);

        if(!(gps.canGetLocation())){
            gps.showSettings();
            return false;
        }else{
            return true;
        }
    }

/*
 * Checks for required permissions and will be so long false until the list ist not empty.
 * Asks for the same permissions which are also declareted in the Manifest.xml
 * The Permissions are: SEND_SMS, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, READ_CONTACTS, VIBRATE and CALL_PHONE
 */
    private  boolean grantPermissions()
    {

        int smsPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS);
        int finLocationPermission = ContextCompat.checkSelfPermission(this,  android.Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationPermission = ContextCompat.checkSelfPermission(this,  android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int readContactsPermission = ContextCompat.checkSelfPermission(this,  android.Manifest.permission.READ_CONTACTS);
        int vibratePermission = ContextCompat.checkSelfPermission(this,  android.Manifest.permission.VIBRATE);
        int callPermission = ContextCompat.checkSelfPermission(this,  Manifest.permission.CALL_PHONE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (smsPermission != PackageManager.PERMISSION_GRANTED)
        {
            listPermissionsNeeded.add(android.Manifest.permission.SEND_SMS);
        }

        if (finLocationPermission != PackageManager.PERMISSION_GRANTED)
        {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (coarseLocationPermission != PackageManager.PERMISSION_GRANTED)
        {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (readContactsPermission != PackageManager.PERMISSION_GRANTED)
        {
            listPermissionsNeeded.add(  android.Manifest.permission.READ_CONTACTS);
        }

        if (vibratePermission != PackageManager.PERMISSION_GRANTED)
        {
            listPermissionsNeeded.add(android.Manifest.permission.VIBRATE);
        }

        if (callPermission != PackageManager.PERMISSION_GRANTED)
        {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSIONS);
            return false;
        }

        return true;
    }

}



package org.hsrt.mc.emergency.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.hsrt.mc.emergency.R;
import org.hsrt.mc.emergency.backend.UserMessage;

import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity
{

    Button gpslocationbtn;
    GPS gps;
    UserMessage msg;

    SharedPreferences detectFirstRun = null;

    final private int PERMISSION = 23;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        gpslocationbtn = (Button) findViewById(R.id.button);

        detectFirstRun = getSharedPreferences("org.hsrt.mc.emergency.activities", MODE_PRIVATE);


        gpslocationbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                gps = new GPS(MainActivity.this);

                if(gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    if(latitude == 0.0 && longitude == 0.0){
                        Toast.makeText(
                                getApplicationContext(),
                                "Bitte erlauben Sie die Standort-Berechtigung", Toast.LENGTH_LONG).show();
                    }else{

                        // Vorerst Standort als Längen-und Breitengrad
                        String location = "Latitude: " + latitude + "Longitude: " +  longitude;
                        Toast.makeText(
                                getApplicationContext(), location
                                , Toast.LENGTH_LONG).show();

                    // msg = new UserMessage(?USER?, location);

                    }

                } else {
                    gps.showSettingsAlert();
                }
            }
        });

    }

    private void grantPermissionOnFirstRun()
    {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (detectFirstRun.getBoolean("firstrun", true))
        {
            grantPermissionOnFirstRun();
           detectFirstRun.edit().putBoolean("firstrun", false).commit();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        TextView textView = (TextView)findViewById(R.id.textView2);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1001:
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Set keys = extras.keySet();
                        Iterator iterate = keys.iterator();
                        while (iterate.hasNext()) {
                            String key = (String) iterate.next();
                            // Log.v(DEBUG_TAG, key + "[" + extras.get(key) + "]");
                        }
                    }
                    Uri result = data.getData();
                    textView.setText("Got a result: " + result.toString());

                    Cursor c = managedQuery(result, null, null, null, null);
                    if (c.moveToFirst()) {


                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null);
                            phones.moveToFirst();
                            String cNumber = phones.getString(phones.getColumnIndex("data1"));
                            System.out.println("number is:" + cNumber);
                        }
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        textView.setText(name);

                        break;
                    }


            }
        } else {
            throw new RuntimeException("Bad result");
        }
    }

}

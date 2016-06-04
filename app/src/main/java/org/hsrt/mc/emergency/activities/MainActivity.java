package org.hsrt.mc.emergency.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.hsrt.mc.emergency.R;
import org.hsrt.mc.emergency.backend.GPS;
import org.hsrt.mc.emergency.backend.SendingService;
import org.hsrt.mc.emergency.backend.UserMessage;

import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback
{

    ImageButton sosBtn;
    GPS gps; // GPS Modul
    UserMessage msg;
    private GoogleMap mMap;
    private static boolean  sosCallsend;
    private float timer;

    private Vibrator vib;
    CountDownTimer countDown;

    SharedPreferences detectFirstRun = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        vib = (Vibrator) MainActivity.this.getSystemService(MainActivity.this.VIBRATOR_SERVICE);


        sosBtn = (ImageButton) findViewById(R.id.fab);

        detectFirstRun = getSharedPreferences("org.hsrt.mc.emergency.activities", MODE_PRIVATE);

        sosBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {

                // Request Permission for sdk 23

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions((Activity) MainActivity.this,
                            new String[]{Manifest.permission.SEND_SMS},
                            24);
                }else{


                 countDown =  new CountDownTimer(10000, 100)
                    {

                        public void onTick(long millisUntilFinished)
                        {
                            timer = millisUntilFinished / 100;


                        }

                        public void onFinish()
                        {
                            if(sosCallsend){


                            Intent serviceIntent = new Intent(getApplicationContext(), SendingService.class);
                            startService(serviceIntent);

                            Toast.makeText( getApplicationContext(), "Emergency-SMS is sent", Toast.LENGTH_LONG).show();
                            sosCallsend = false;
                            }
                        }
                    }.start();



                }
            if(sosCallsend){
                showCancelDialog();
            }else{
                setVibrationTime(10000);

            }
                sosCallsend = true;


            }
        });

    }

    private void showCancelDialog()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        alertDialog.setTitle("Notruf");

        alertDialog.setMessage("Wollen Sie den Notruf wirklich abbrechen?");

        alertDialog.setPositiveButton("Ja", new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(timer > 0){
                    countDown.cancel();
                    sosCallsend = false;
                    vib.cancel();

                }

                Toast.makeText( getApplicationContext(), "Notruf abgebrochen", Toast.LENGTH_LONG).show();

            }
        });

        alertDialog.setNegativeButton("Nein", new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void setVibrationTime(int time)
    {
        if(time >= 0){

            vib.vibrate(time);

        }
    }

    private void grantPermissionOnFirstRun()
    {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                23);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
        // Add a marker in your OwnPosition and move the camera
        LatLng hochschule = new LatLng(48.482494, 9.1879501);
        mMap.addMarker(new MarkerOptions().position(hochschule).title("Mein Standort"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hochschule));
        mMap.animateCamera(zoom);
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

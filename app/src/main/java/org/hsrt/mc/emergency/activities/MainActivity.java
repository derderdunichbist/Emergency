package org.hsrt.mc.emergency.activities;

import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    private static boolean sosButtonPressed;
    private static float timer;

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
                grantPermissionOnFirstRun();
                 countDown =  new CountDownTimer(10000, 100)
                    {

                        public void onTick(long millisUntilFinished)
                        {
                            timer = millisUntilFinished / 100;


                        }

                        public void onFinish()
                        {
                            if(sosButtonPressed){


                            Intent serviceIntent = new Intent(getApplicationContext(), SendingService.class);
                            startService(serviceIntent);

                            Toast.makeText( getApplicationContext(), "Emergency-SMS is sent", Toast.LENGTH_LONG).show();
                            sosButtonPressed = false;
                            }
                        }
                    }.start();

            if(sosButtonPressed){
                showCancelDialog();
            }else{
                setVibrationTime(10000);

            }
                sosButtonPressed = true;


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
                    sosButtonPressed = false;
                    vib.cancel();
                    Toast.makeText( getApplicationContext(), "Notruf abgebrochen", Toast.LENGTH_LONG).show();

                }


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
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.SEND_SMS},
                    23);
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
